package br.com.vambersson.webservicead.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vambersson on 16/05/2017.
 */

public class Usuario {

    private Integer matricula;

    private String nome;

    private String email;

    private String tipo;

    private String status;

    private Integer login;

    private String senha;

    private List<Curso> cursos;

    public Usuario(){
        cursos = new ArrayList<Curso>();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
