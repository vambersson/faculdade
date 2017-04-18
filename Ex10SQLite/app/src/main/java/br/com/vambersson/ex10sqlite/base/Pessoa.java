package br.com.vambersson.ex10sqlite.base;

import java.util.ArrayList;

/**
 * Created by Vambersson on 17/04/2017.
 */

public class Pessoa {

    private String nome;
    private ArrayList<Pessoa> lista ;

    public Pessoa(){
        lista = new ArrayList<Pessoa>();
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Pessoa> listaPessoa(){

        return lista;
    }

    public void adiciona(Pessoa p){
        lista.add(p);
    }



}

