package com.example.sattar.gcmchat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class RegisterOnServer extends AppCompatActivity {


    public EditText edName, edEmail;
    public Button btnRegister;
    String name, email, regId;
    Context context;
    String parameters = null;
    String token=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_on_server);

        final String serverUrl = "http://192.168.1.121/gcmServer/registration.php";

        context = this;



        edName = (EditText) findViewById(R.id.name);
        edEmail = (EditText) findViewById(R.id.email);
        btnRegister = (Button) findViewById(R.id.register);

        Intent intent=new Intent(this,registrationService.class);
        startService(intent);


        new GetToken().execute();


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edName.getText().toString();
                email = edEmail.getText().toString();

try {
   parameters = "regId=" + URLEncoder.encode(token, "UTF-8") + "&name=" + URLEncoder.encode(name, "UTF-8") + "&email=" + URLEncoder.encode(email, "UTF-8");

}
               catch (UnsupportedEncodingException e){
                    Log.d("exception",e.getMessage());
                }
                new CustomAsyncTask().execute(serverUrl);

            }
        });

    }



    class CustomAsyncTask extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            Toast.makeText(context, "onPreExecute", Toast.LENGTH_SHORT).show();

            progressDialog = new ProgressDialog(RegisterOnServer.this);
            progressDialog.setMessage("inserting data...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(1);
            progressDialog.setCancelable(false);
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
            Toast.makeText(context, values[0], Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            Log.d("sam", result);
        }


    }








    class GetToken extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;

        private Context context;


        @Override
        protected Void doInBackground(Void... params) {

            boolean isDataFetched = false;
            while(!isDataFetched)
            {
                token = registrationService.getToken();
                if(token!=null)
                {
                    isDataFetched = true;
                }

            }

            return  null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(RegisterOnServer.this);
            progressDialog.setMessage("Token...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(1);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

                if(progressDialog!=null){
                    progressDialog.dismiss();
                }

        }
    }











}

