package com.example.sattar.gcmchat;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Sattar on 5/31/2016.
 */
public class GCMListener  extends GcmListenerService {


    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.d("azad", "in listener");

        Toast.makeText(getApplicationContext(),"in listener",Toast.LENGTH_LONG).show();

        String message = data.getString("message");
        String fm = data.getString("From");
        Log.d("azad", "From: " + from);
        Log.d("azad", "Message: " + message);
        Log.d("azad", "Message: " + fm);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }


    }

}
