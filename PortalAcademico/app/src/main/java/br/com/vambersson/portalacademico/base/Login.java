package br.com.vambersson.portalacademico.base;

import java.io.Serializable;

/**
 * Created by Vambersson on 13/05/2017.
 */

public class Login implements Serializable{

    private int matricula;

    private String login;

    private String senha;

    private String statusLogado;


    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getStatusLogado() {
        return statusLogado;
    }

    public void setStatusLogado(String statusLogado) {
        this.statusLogado = statusLogado;
    }
}
