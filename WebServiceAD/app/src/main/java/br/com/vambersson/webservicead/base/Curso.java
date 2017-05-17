package br.com.vambersson.webservicead.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vambersson on 16/05/2017.
 */

public class Curso {

    private Integer codigo;

    private String nome;

    private List<Disciplina> disciplinas;

    public Curso() {

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
}
