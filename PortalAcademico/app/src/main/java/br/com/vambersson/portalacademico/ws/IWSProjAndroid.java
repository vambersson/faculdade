package br.com.vambersson.portalacademico.ws;

import br.com.vambersson.portalacademico.base.Aluno;
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


    @POST("cadastraAluno={aluno}")
    Call<String> cadastraAluno(@Path("aluno") String aluno);

    @PUT("atualizarAluno={aluno}")
    Call<String> atualizarAluno(@Path("aluno") String aluno);

    @GET("pesquisaAluno={matricula}")
    Call<Aluno> pesquisaAluno(@Path("matricula") String matricula);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.0.78:8080/WSProjAndroid/servicos/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
