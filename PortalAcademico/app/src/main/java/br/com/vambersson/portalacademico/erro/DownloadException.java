package br.com.vambersson.portalacademico.erro;

/**
 * Created by Vambersson on 13/04/2017.
 */

public class DownloadException extends Exception {

    public DownloadException(){}
    public DownloadException(String txt){super(txt);}
    public DownloadException(Exception e){super(e);}


}
