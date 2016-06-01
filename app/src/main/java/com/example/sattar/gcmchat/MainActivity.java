package com.example.sattar.gcmchat;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText name, email;
    Button register;
    public Context context;
    TextView result;
    String Token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;




        name=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.email);
        register=(Button)findViewById(R.id.register);
        result=(TextView)findViewById(R.id.result);

        context=this;


        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub



               // String nameTv=name.getText().toString();
               // String emailTv=email.getText().toString();



            }
        });

    }






}

