package br.com.vambersson.portalparatodos.base;

import java.io.Serializable;

/**
 * Created by Vambersson on 20/05/2017.
 */

public class Faculdade implements Serializable{

    private Integer codigo;

    private String nome;

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
}
