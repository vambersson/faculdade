package br.com.vambersson.portalparatodos.aula.notificacao;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import br.com.vambersson.portalparatodos.R;

/**
 * Created by Vambersson on 13/06/2017.
 */

public class PushListenerService extends GcmListenerService {


    private static final String TAG = "PushListenerService";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "De: " + from);
        Log.d(TAG, "Mensagem: " + message);
        enviarNotificacao(message);
    }

    private void enviarNotificacao(String message) {
        Intent intent = new Intent(this, NotificacaoAgenda.class);

        intent.putExtra("mensagem", message);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent= PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("NotificaçãoPush para o YouTube")
                .setContentText(message).setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

}
