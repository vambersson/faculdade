package br.com.vambersson.portalparatodos.aula.notificacao;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by Vambersson on 13/06/2017.
 */

public class ServicoNotificacaoWebApi extends IntentService {

    private static final String TAG = "ServicoNotificacao";
    private static final String[] TOPICS = {"global"};


    /**
     * Construtor
     */
    public ServicoNotificacaoWebApi() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences sharedPreferences=  PreferenceManager.getDefaultSharedPreferences(this);

        try {
            synchronized (TAG) {
                InstanceID instanceID= InstanceID.getInstance(this);

                String token = instanceID.getToken("762110991375", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                Log.i(TAG, "Registrodo ID: " + token);
                sendRegistrationToServer(token);

                subscribeTopics(token);

                sharedPreferences.edit().putBoolean("enviado", true).apply();
            }
        } catch (Exception e) {
            Log.d(TAG, "Falha na geraçãodo token", e);
            sharedPreferences.edit().putBoolean("enviado", false).apply();
        }
        Intent registrationComplete= new Intent("registration Complete");
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);


    }

    private void sendRegistrationToServer(String token) {
        // Este token precisa ser enviado ao nosso servidor
         Log.d(TAG, "Aqui está sendo feito o registro do token: " + token);
    }

    private void subscribeTopics(String token) throws IOException {
        for (String topic : TOPICS) {GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }












}
