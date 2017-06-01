package br.com.vambersson.portalparatodos.util;

import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by Vambersson on 01/06/2017.
 */

public class PlayServicesUtil {

    private static final int REQUEST_CODE_ERRO_PLAY_SERVICES = 900;


    public static boolean playServicesdisponivel(FragmentActivity act){

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(act);

        if(ConnectionResult.SUCCESS == resultCode){
            return true;
        }else {
            return false;
        }

    }






}
