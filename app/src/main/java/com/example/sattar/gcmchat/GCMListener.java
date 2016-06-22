package com.example.sattar.gcmchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import java.text.SimpleDateFormat;

import static com.google.android.gms.internal.zzir.runOnUiThread;

/**
 * Created by Sattar on 5/31/2016.
 */
public class GCMListener  extends GcmListenerService {


    @Override
    public void onMessageReceived(String from, Bundle data) {

        String   message = data.getString("message");

        String  number = data.getString("From").trim();


        Log.d("azad", "From:" + from);
        Log.d("azad", "Message: " + message);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
            Log.d("azad", "Message2");
        } else {
            // normal downstream message.
        }

        sendToDatabase(message,number);

    }


    void sendToDatabase(String message,String number){

        String name="";
        String dateString="";
        String timeString="";
        String type="";

        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
        dateString = sdf.format(date);

        SimpleDateFormat stime = new SimpleDateFormat("h:mm a");
        timeString = stime.format(date);

        //  Toast.makeText(context, timeString, Toast.LENGTH_LONG).show();



        name= new DataBase(getApplicationContext()).getContactName(getApplicationContext(),number);

        if(name==null){
            name="Unknown Person";
        }

        type="received";

        new DataBase(getApplicationContext()).insertionrow(new DataBase(getApplicationContext()),name,number,dateString,timeString,message,type);




       runOnUiThread(new Runnable() {
            @Override
            public void run() {

                IndividualChatDisp.update();

                SharedPreferences sharedpreferences = getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
                if(sharedpreferences.contains("chats")) {
                    new  ChatFragment().updateFrontList();
                }else{
                    SharedPreferences.Editor edit = sharedpreferences.edit();
                    edit.putString("chats", "true");
                    edit.commit();

                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


            }
        });




    }

}
