package Unibh;

public class Mapeamento {

    private final Percepcao percepcao;
    private final String acao;

    public Percepcao getPercepcao() {
        return this.percepcao;
    }

    public String getAcao() {
        return this.acao;
    }

    public Mapeamento(Percepcao percepcao, String acao) {
        this.percepcao = percepcao;
        this.acao = acao;
    }
}
