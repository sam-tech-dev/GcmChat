package com.example.sattar.gcmchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class IndividualChatDisp extends Activity {



    static  IndividualChatAdapter  mAdapter;
    static ArrayList<IndividualChatwrapper> IndChatList,IndividualChatList;
    TextView individual;
    static RecyclerView indlistv;
    static Context context;
    ImageButton back,sendBtn;
    EditText message;
    static  String	 number;
    String	 name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_chat_disp);


        context=this;

        Bundle bundle = getIntent().getExtras();
        number = bundle.getString("monumber");
        name = bundle.getString("name");

        if(name.equals("Unknown Person")){
            name=number;
        }



        back=(ImageButton)findViewById(R.id.backbutton);
        sendBtn=(ImageButton)findViewById(R.id.sendIt);
        indlistv=(RecyclerView) findViewById(R.id.recycler_view);
        indlistv.setLayoutManager(new LinearLayoutManager(this));
        message=(EditText)findViewById(R.id.msg);
        individual=(TextView)findViewById(R.id.indname);
        individual.setText(name);


        Log.d("azad",number);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();

            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                //Toast.makeText(getApplicationContext(),"sattar :"+number, Toast.LENGTH_LONG).show();

                String msg=message.getText().toString();

                if(number!=null){
                    new NewTalk(context,number, msg).chatSend();
                }
                    message.setText("");
            }
        });

/*
        indlistv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                // TODO Auto-generated method stub

                AlertDialog.Builder alert= new AlertDialog.Builder(context);

                alert.setTitle("Alert!!");
                alert.setMessage("Are you sure to delete SMS?");

                final String idofs= IndividualChatList.get(position).get_id();
                final String num=IndividualChatList.get(position).get_number();

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        Integer noSMS=new DataBase(context).deleteSingleSms(new DataBase(context), idofs,num);
                        if(noSMS==1){

                            Intent intent=new Intent(IndividualChatDisp.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            update();
                        }

                        dialog.dismiss();



                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();


                return true;
            }



        });

*/
        IndChatList=individualComChatList();
        if(IndChatList!=null) {
            setIndividualChatAdapter(IndChatList);
        }
    }


    static  ArrayList<IndividualChatwrapper> individualComChatList(){

        DataBase object=  new DataBase(context);


        Cursor cursor = object.getIndividualMsgs(object, number);
        Log.d("ruk",String.valueOf(cursor));

        IndividualChatList=new ArrayList<IndividualChatwrapper>();

        if( cursor.moveToFirst()){

            do{

                IndividualChatwrapper indMsginstance=new IndividualChatwrapper();



                Log.d("azad",cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3)+" "+cursor.getString(4)+" "+cursor.getString(5));

                indMsginstance.set_id(cursor.getString(0));

                indMsginstance.set_name(cursor.getString(1));

                indMsginstance.set_number(cursor.getString(2));

                indMsginstance.set_date(cursor.getString(3));

                indMsginstance.set_time(cursor.getString(4));

                indMsginstance.set_content(cursor.getString(5));

                indMsginstance.set_type(cursor.getString(6));

                IndividualChatList.add(indMsginstance);

            }while(cursor.moveToNext());

            cursor.close();

            return  IndividualChatList;
        }else{
            return null;
        }



    }




    static	private void setIndividualChatAdapter(ArrayList<IndividualChatwrapper> IndividualChatList) {
        // TODO Auto-generated method stub
        mAdapter = new IndividualChatAdapter(context, IndividualChatList);
            indlistv.setAdapter(mAdapter);
        indlistv.scrollToPosition(indlistv.getAdapter().getItemCount()-1);
    }




    static void update(){

        try{
            IndividualChatList.clear();
             individualComChatList();
            setIndividualChatAdapter(IndividualChatList);

        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }



}

