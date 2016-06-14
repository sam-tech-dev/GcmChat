package com.example.sattar.gcmchat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

/**
 * Created by Sattar on 6/6/2016.
 */
public class NewTalk {

       Context context;
       String receiver,message,parameters;
       String url,sender;
       String timeString,dateString,name,type;

    public NewTalk(Context conn,String num,String msg){

        this.context=conn;
        this.receiver=num;
        this.message=msg;
        this.url=ServerUrls.senderUrl;
        SharedPreferences sharedpreferences =context.getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
        this.sender=sharedpreferences.getString("number", "");



    }


    void chatSend(){


        try {
            parameters = "message="+ URLEncoder.encode(message, "UTF-8")+"&senderNo="+URLEncoder.encode(sender, "UTF-8")+"&receiverNo="+URLEncoder.encode(receiver, "UTF-8");

        }
        catch (UnsupportedEncodingException e){
            Log.d("exception",e.getMessage());
        }

        new sendingTask().execute(url);

        try{
            long date = System.currentTimeMillis();

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
            dateString = sdf.format(date);

            SimpleDateFormat stime = new SimpleDateFormat("h:mm a");
            timeString = stime.format(date);


            name=new DataBase(context).getContactName(context,receiver);

            if(name==null){
                name="Unknown Person";
            }

            type="sent";

            new DataBase(context).insertionrow(new DataBase(context), name,receiver, dateString,timeString, message,type);
            Toast.makeText(context, "sent SMS",Toast.LENGTH_SHORT).show();

            IndividualChatDisp.update();

            //  MainActivity.updateFrontList();

        }catch(Exception e){
            //  Toast.makeText(getApplicationContext(), "try again", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }



    class sendingTask extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

           // Toast.makeText(context, "onPreExecute", Toast.LENGTH_SHORT).show();

            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("sending...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(1);
            progressDialog.setCancelable(true);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {


            publishProgress(params[0]);
            URL url = null;
            try {
                url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length",Integer.toString(parameters.getBytes().length));
                connection.setRequestProperty("Content-Language","en-US");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                DataOutputStream rt = new DataOutputStream(connection.getOutputStream());
                rt.write(parameters.getBytes());
                rt.flush();
                rt.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String s = br.readLine();

                return s;
            } catch (Exception e) {
                return "E:" + e;
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values[0]);
           // Toast.makeText(context, values[0], Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (progressDialog != null) {
                progressDialog.dismiss();
            }

           // Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            Log.d("aza", result);
        }


    }


}
