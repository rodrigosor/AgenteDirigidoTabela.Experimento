package Unibh;

import java.awt.Color;
import java.util.ArrayList;
import robocode.ScannedRobotEvent;
import robocode.StatusEvent;

/**
 * AURA - Apenas Um Robô Autônomo Limitado
 *
 * @version 1.0.0
 * @author Rodrigo Reis, Igor Bueloni, Wendell Ronald, Ivan Paulovich
 */
public class Aural extends Aura {

    public Aural() throws NoSuchMethodException {
        tabela = new ArrayList();
        tabela.add(new Mapeamento(Percepcao.VarreduraDoInimigo, "Procurar"));
        tabela.add(new Mapeamento(Percepcao.InimigoDetectado, "Atacar"));
        tabela.add(new Mapeamento(Percepcao.PoucaEnergia, "Amarelar"));
        fator = 255;
    }

    @Override
    public void Escurecer() {
        fator -= 20;
        if (fator < 0) {
            fator = 255;
        }
        setColors(new Color(0, fator, 0), new Color(0, fator, 0), new Color(0, fator, 0));
    }

    @Override
    public void run() {
        setColors(Color.green, Color.green, Color.green);
        ExecutarAcao(Percepcao.VarreduraDoInimigo, null);
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        ExecutarAcao(Percepcao.InimigoDetectado, e);
    }

    @Override
    public void onStatus(StatusEvent e) {
        ExecutarAcao(Percepcao.PoucaEnergia, e.getStatus());
    }
}
