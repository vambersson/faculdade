package br.com.vambersson.portalparatodos.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by derick on 16/05/17.
 */

public class Curso implements Serializable {

    private Integer codigo;

    private String nome;

    private Faculdade faculdade;

    private List<Disciplina> disciplinas;

    public Curso(){
        faculdade = new Faculdade();
        disciplinas = new ArrayList<Disciplina>();
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

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public Faculdade getFaculdade() {
        return faculdade;
    }

    public void setFaculdade(Faculdade faculdade) {
        this.faculdade = faculdade;
    }
}
