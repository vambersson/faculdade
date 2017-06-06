package br.com.vambersson.portalparatodos.base;

import java.io.Serializable;

/**
 * Created by derick on 16/05/17.
 */

public class Disciplina implements Serializable {

    private Integer codigo;

    private String nome;

    private Integer periodo;

    private String selecionou;

    private Integer numerodia;

    private Integer ordem;

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

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
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

    public String getSelecionou() {
        return selecionou;
    }

    public void setSelecionou(String selecionou) {
        this.selecionou = selecionou;
    }

    public Integer getNumerodia() {
        return numerodia;
    }

    public void setNumerodia(Integer numerodia) {
        this.numerodia = numerodia;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }
}
