package Unibh;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import robocode.AdvancedRobot;
import robocode.DeathEvent;
import robocode.HitByBulletEvent;
import robocode.HitWallEvent;
import robocode.RobotStatus;
import robocode.ScannedRobotEvent;
import robocode.StatusEvent;
import robocode.WinEvent;

/**
 * AURA - Apenas Um Robô Autônomo
 *
 * @version 1.0.0
 * @author Rodrigo Reis, Igor Bueloni, Wendell Ronald, Ivan Paulovich
 */
public class Aura extends AdvancedRobot {

    protected List<Mapeamento> tabela;
    protected int fator;

    public Aura() throws NoSuchMethodException {
        tabela = new ArrayList();
        tabela.add(new Mapeamento(Percepcao.VarreduraDoInimigo, "Procurar"));
        tabela.add(new Mapeamento(Percepcao.InimigoDetectado, "Atacar"));
        tabela.add(new Mapeamento(Percepcao.AtingidoPeloInimigo, "Fugir"));
        tabela.add(new Mapeamento(Percepcao.ColidirLimiteArena, "MudarDirecao"));
        tabela.add(new Mapeamento(Percepcao.PoucaEnergia, "Amarelar"));
        tabela.add(new Mapeamento(Percepcao.InimigoDerrotado, "ExecutarDancaVitoria"));
        tabela.add(new Mapeamento(Percepcao.Derrotado, "Chorar"));
        fator = 255;
    }

    public void ExecutarAcao(Percepcao percepcao, Object parametro) {
        try {
            Optional<Mapeamento> mapeamento = tabela.stream().filter(m -> m.getPercepcao() == percepcao).findFirst();
            if (parametro != null) {
                Method acao = this.getClass().getMethod(mapeamento.get().getAcao(), parametro.getClass());
                acao.invoke(this, parametro);
            } else {
                Method acao = this.getClass().getMethod(mapeamento.get().getAcao());
                acao.invoke(this);
            }
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
            Logar("Percepcao -> Acao nao mapeada. [%s]", ex.getMessage());
        }
    }

    public void Escurecer() {
        fator -= 20;
        if (fator < 0) {
            fator = 255;
        }
        setColors(new Color(fator, 0, 0), new Color(fator, 0, 0), new Color(fator, 0, 0));
    }

    public void Logar(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    public void Procurar() {
        Logar(">> VarreduraDoInimigo");
        setAdjustRadarForGunTurn(true);
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);
        setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
    }

    public void Atacar(ScannedRobotEvent e) {
        Logar(">> InimigoDetectado");
        double angulo = getHeading() - getGunHeading() + e.getBearing();
        setTurnGunRight(angulo);
        setTurnRight(angulo);
        setAhead(30);
        fire(1);
    }

    public void Fugir() {
        Logar(">> AtingidoPeloInimigo");
        stop();
        setBack(300);
        Escurecer();
        Procurar();
    }

    public void MudarDirecao() {
        Logar(">> ColidirLimiteArena");
        stop();
        setTurnRight(90);
        Procurar();
    }

    public void Amarelar(RobotStatus e) {
        if (e.getEnergy() < 20) {
            Logar(">> PoucaEnergia");
            setColors(Color.yellow, Color.yellow, Color.yellow);
        }
    }

    public void ExecutarDancaVitoria() {
        Logar(">> InimigoDerrotado");
        for (int i = 0; i < 50; i++) {
            stop();
            setTurnRight(30);
            setTurnLeft(30);
        }
    }

    public void Chorar() {
        Logar(">> Derrotado");
        Logar("(T_T) -> https://lidianta.files.wordpress.com/2014/09/engole1.png");
    }

    @Override
    public void run() {
        setColors(Color.red, Color.red, Color.red);
        ExecutarAcao(Percepcao.VarreduraDoInimigo, null);
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        ExecutarAcao(Percepcao.InimigoDetectado, e);
    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        ExecutarAcao(Percepcao.AtingidoPeloInimigo, null);
    }

    @Override
    public void onHitWall(HitWallEvent e) {
        ExecutarAcao(Percepcao.ColidirLimiteArena, null);
    }

    @Override
    public void onStatus(StatusEvent e) {
        ExecutarAcao(Percepcao.PoucaEnergia, e.getStatus());
    }

    @Override
    public void onWin(WinEvent e) {
        ExecutarAcao(Percepcao.InimigoDerrotado, null);
    }

    @Override
    public void onDeath(DeathEvent e) {
        ExecutarAcao(Percepcao.Derrotado, null);
    }
}
