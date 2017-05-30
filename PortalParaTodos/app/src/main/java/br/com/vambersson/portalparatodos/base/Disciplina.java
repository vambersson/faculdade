package br.com.vambersson.portalparatodos.base;

import java.io.Serializable;

/**
 * Created by derick on 16/05/17.
 */

public class Disciplina implements Serializable {

    private Integer codigo;

    private String nome;

    private String periodo;

    private Faculdade faculdade;

    private Curso curso;

    public Disciplina(){
        faculdade = new Faculdade();
        curso = new Curso();
    }

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

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Faculdade getFaculdade() {
        return faculdade;
    }

    public void setFaculdade(Faculdade faculdade) {
        this.faculdade = faculdade;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}
