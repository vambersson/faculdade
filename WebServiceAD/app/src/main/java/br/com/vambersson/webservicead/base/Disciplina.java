package br.com.vambersson.webservicead.base;

/**
 * Created by Vambersson on 16/05/2017.
 */

public class Disciplina {

    private Integer codigo;

    private String nome;

    private Integer periodo;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }
}
