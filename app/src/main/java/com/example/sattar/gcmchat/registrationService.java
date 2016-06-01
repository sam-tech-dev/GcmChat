package com.example.sattar.gcmchat;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import java.io.IOException;

/**
 * Created by Sattar on 5/28/2016.
 */
public class registrationService extends IntentService {

    private static final String TAG = "RegIntentService";
       public registrationService(){
           super(TAG);
       }
   static  String id;


    @Override
    protected void onHandleIntent(Intent intent) {

        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            id=token;
            Log.d("azad", "GCM Registration Token: " + token);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

        public static String getToken(){
            return id;
        }


}
