package com.example.sattar.gcmchat;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerFunctions {


    // public static String  serverUrl="http://172.21.4.28/gcmServer/registration.php";
    public static String  serverUrl="http://198.168.1.109/gcmServer/registration.php";
     public static  Map<String, String> parameters;
     public  Context context;





    public void register(Context context,String name,String email, String regId){
        parameters = new HashMap<String, String>();
        parameters.put("regId", regId);
        parameters.put("name", name);
        parameters.put("email", email);

          serverUrl=serverUrl+"?regId="+regId+"&name="+name+"&email="+email;

        Log.d("Sattar",serverUrl);

        new CustomAsyncTask().execute();
}


    public static String httpConnection() throws IOException {



//        StringBuilder bodyBuilder = new StringBuilder();
//        Iterator<Map.Entry<String, String>> iterator = parameters.entrySet().iterator();
//
//        while (iterator.hasNext()) {
//            Map.Entry<String, String> param = iterator.next();
//            bodyBuilder.append(param.getKey()).append('=')
//                    .append(param.getValue());
//            if (iterator.hasNext()) {
//                bodyBuilder.append('&');
//            }
//        }
//        String body = bodyBuilder.toString();
//        byte[] bytes = body.getBytes();


        URL url=null;
        try {
             url = new URL(serverUrl);
            HttpURLConnection  connection = (HttpURLConnection)url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String s = br.readLine();
            return s;
        }
        catch (Exception e) {
            return "E:" + e;
        }


//        connection.setAllowUserInteraction(false);
//        connection.setInstanceFollowRedirects(true);
//        connection.setDoOutput(true);
//        connection.setUseCaches(false);
//        connection.setFixedLengthStreamingMode(bytes.length);
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//        OutputStream out = connection.getOutputStream();
//        out.write(bytes);
//        out.close();


//         Log.d("Sattar","go on ");
//
//        Log.d("Sattar",serverUrl+"  cbdhfv  "+s);
//        return s;
    }




    class CustomAsyncTask extends AsyncTask<Void, Void, String> {

        String url;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();



           // Toast.makeText(context, "onPreExecute", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = "";
            try {
                 return httpConnection();
                /*
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
*/
            } catch (IOException e) {
                return "EIO : " + e; }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("Sattar",serverUrl+"  result "+result);

     //       Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        }


    }





}
