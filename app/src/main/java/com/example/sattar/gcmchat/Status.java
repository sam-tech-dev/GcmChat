package com.example.sattar.gcmchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Status extends AppCompatActivity {

    TextView currebtStatus;
    ImageButton  editStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        currebtStatus=(TextView)findViewById(R.id.status);
        editStatus=(ImageButton)findViewById(R.id.edit);


        SharedPreferences sharedpreferences = getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
        if(sharedpreferences.contains("status")){
            String status = sharedpreferences.getString("status", "");
            currebtStatus.setText(status);
        }

        editStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Status.this, EditStatus.class);
                startActivity(intent);
            }
        });



    }
}
