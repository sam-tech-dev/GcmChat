package com.example.sattar.gcmchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class EditStatus extends AppCompatActivity {

    EditText newStatus;
    Button cancel,ok;
    ImageButton back;
    String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_status);

        back=(ImageButton)findViewById(R.id.backbutton);
        newStatus=(EditText)findViewById(R.id.newstatus);
        cancel=(Button)findViewById(R.id.cancel);
        ok=(Button)findViewById(R.id.ok);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditStatus.this,Status.class);
                startActivity(intent);
                finish();

            }
        });



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditStatus.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                status=newStatus.getText().toString();
                new  updateStatus().execute(ServerUrls.updatestatusUrl);

            }
        });

    }


    class updateStatus extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            String parameters = null;
            SharedPreferences sharedpreferences = getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedpreferences.edit();
            edit.putString("status",status);
            edit.commit();

            String number = sharedpreferences.getString("number", "");

            Log.d("its",number+"  "+status+"  "+params[0]);

            try {
                parameters = "status=" + URLEncoder.encode(status, "UTF-8") + "&mobileno=" + URLEncoder.encode(number, "UTF-8");

            } catch (UnsupportedEncodingException e) {
                Log.d("exception", e.getMessage());
            }

            URL url = null;
            try {
                url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", Integer.toString(parameters.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                DataOutputStream rt = new DataOutputStream(connection.getOutputStream());
                rt.write(parameters.getBytes());
                rt.flush();
                rt.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = br.readLine();
                return line;

            } catch (Exception e) {
                return "E:" + e;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
              Log.d("az","result of edit status"+result);

            Intent intent=new Intent(EditStatus.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

    }



}
