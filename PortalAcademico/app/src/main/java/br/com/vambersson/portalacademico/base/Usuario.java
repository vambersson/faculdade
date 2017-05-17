package br.com.vambersson.portalacademico.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by derick on 16/05/17.
 */

public class Usuario implements Serializable {

    private Integer matricula;

    private String nome;

    private String email;

    private String tipo;

    private String status;

    private Integer login;

    private String senha;

    private List<Curso> cursos;

    public Usuario(){

        setCursos(new ArrayList<Curso>());

    }


    public Integer getMatricula() {
        return matricula;
    }

    public void setMatricula(Integer matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getLogin() {
        return login;
    }

    public void setLogin(Integer login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
