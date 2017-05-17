package br.com.vambersson.portalacademico.ws;

import br.com.vambersson.portalacademico.base.Aluno;
import br.com.vambersson.portalacademico.base.Usuario;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Vambersson on 13/05/2017.
 */

public interface IWSProjAndroid {

    @GET("logar={usuario}")
    Call<Usuario> logar(@Path("usuario") String usuario);

    @GET("verificarPrimeiroAcesso={usuario}")
    Call<Usuario> verificarPrimeiroAcesso(@Path("usuario") String usuario);





    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.43.123:8080/PortalAcademico/servicos/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
