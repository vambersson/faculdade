package br.com.vambersson.portalacademico.base;

import java.io.Serializable;

/**
 * Created by Vambersson on 12/05/2017.
 */

public class Aluno implements Serializable {

    private int matricula;

    private String nome;

    private String status;

    private Login login;

    public Aluno(){
        login = new Login();
    }

    public Aluno(int matricula, String nome, String status, Login login) {
        this.matricula = matricula;
        this.nome = nome;
        this.status = status;
        this.login = login;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }


}
