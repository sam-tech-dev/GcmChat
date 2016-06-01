package com.example.sattar.gcmchat;

/**
 * Created by Sattar on 5/28/2016.
 */

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

public class instanceIDListener extends InstanceIDListenerService  {



    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, registrationService.class);
        startService(intent);
    }



}
