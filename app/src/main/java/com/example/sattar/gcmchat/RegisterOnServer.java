package com.example.sattar.gcmchat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    EditText mono;
    Button regist;
    Context context;
    String parameters = null;
    String token=null;
    final String serverUrl =ServerUrls.registerUrl;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_on_server);
        mono=(EditText)findViewById(R.id.mobile);
        regist=(Button)findViewById(R.id.registration);
        context = this;

        Intent intent=new Intent(this,registrationService.class);
        startService(intent);


        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                number=mono.getText().toString().trim();

                new GetToken().execute();

                Intent intent=new Intent(RegisterOnServer.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }

    class CustomAsyncTask extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {

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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result.equals("This number is already registered")){
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            }else{

                if(result.equals("You have Successfully Registered")){
                    Toast.makeText(context, "You has registered on server", Toast.LENGTH_SHORT).show();
                   SharedPreferences sharedpreferences = context.getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedpreferences.edit();
                    edit.putString("registerCheck","true");
                    edit.putString("number",number);
                    edit.putString("status","You are using SatChat!");
                    edit.commit();

                    new UpdateCommonList(RegisterOnServer.this);

                }else{
                    Toast.makeText(context, "error 404", Toast.LENGTH_SHORT).show();
                }

            }
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

            try {
                parameters = "regId=" + URLEncoder.encode(token, "UTF-8")+"&mobileno=" + URLEncoder.encode(number, "UTF-8");

            }
            catch (UnsupportedEncodingException e){
                Log.d("exception",e.getMessage());
            }
            new CustomAsyncTask().execute(serverUrl);
        }
    }











}

