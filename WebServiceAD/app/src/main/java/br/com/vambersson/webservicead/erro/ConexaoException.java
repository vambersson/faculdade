package br.com.vambersson.webservicead.erro;

/**
 * Created by Vambersson on 13/04/2017.
 */

public class ConexaoException extends Exception {

    public ConexaoException(){}
    public ConexaoException(String txt){super(txt);}
    public ConexaoException(Exception e){super(e);}

}
