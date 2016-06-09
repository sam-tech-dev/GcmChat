package com.example.sattar.gcmchat;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Sattar on 6/5/2016.
 */
public class ChatFragment extends Fragment implements RCVClickListener{


    static  RecyclerView recyclerView;
    static  ArrayList<AllChatsWrapper> ChatList,chatsList,dummyList;
    static  public   AllChatsAdapter mAdapter;
    static DataBase obj;
    static   Context context;
    static Cursor crr;
    static int checkedCount=0;


    public  ChatFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView =  inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.program_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        obj=new DataBase(getActivity());

        crr=obj.getDatatodisplay(obj);

        if(crr!=null&& crr.moveToFirst()){

            ChatList=makeChatsList();

            setChatsAdapter(ChatList);

        }
        else{

           Toast.makeText(context,"No Chats to Show", Toast.LENGTH_LONG).show();
        }


        return rootView;
    }



    static ArrayList<AllChatsWrapper> makeChatsList(){
        Cursor csr;
        int noofSms;

        //  DataBase obj=new DataBase(context);
        crr=obj.getDatatodisplay(obj);
        chatsList = new ArrayList<AllChatsWrapper>();
        if(crr!=null&&crr.moveToFirst()){

            do{
                int id= crr.getInt(0);

                csr= obj.getData(obj, id);
                csr.moveToFirst();

                noofSms = obj.getCountSms(obj, csr.getString(2));

                AllChatsWrapper messageinstance=new AllChatsWrapper();

                messageinstance.set_id(csr.getInt(0));
                messageinstance.set_name(csr.getString(1));
                messageinstance.set_number(csr.getString(2));
                messageinstance.set_date(csr.getString(3));
                messageinstance.set_time(csr.getString(4));
                messageinstance.set_content(csr.getString(5));
                messageinstance.set_count(String.valueOf(noofSms));

                chatsList.add(messageinstance);

                //Toast.makeText(getApplicationContext(), csr.getString(0), Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(),csr.getString(2), Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(),String.valueOf(noofSms), Toast.LENGTH_LONG).show();


            }while(crr.moveToNext());

            crr.close();
            csr.close();

        }


        return chatsList;


    }



    private void setChatsAdapter(ArrayList<AllChatsWrapper> chatList) {
        // TODO Auto-generated method stub
        mAdapter = new AllChatsAdapter(context, chatList);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnRCVClickListener(this);

        checkedCount=0;



        Collections.sort(chatsList, new Comparator<AllChatsWrapper>()
        {

            @Override
            public int compare(AllChatsWrapper lhs, AllChatsWrapper rhs) {
                // TODO Auto-generated method stub
                return   rhs.get_id()-lhs.get_id();
            }

        });


    }



    static void updateFrontList(){

        try{
            chatsList.clear();

            ArrayList<AllChatsWrapper> newList=makeChatsList();

           new ChatFragment().setChatsAdapter(chatsList);
            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount()-1);
            // mAdapter.notifyDataSetChanged();

        }catch(Exception e){

//            Toast.makeText(context, "error in update", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    @Override
    public void onRCVClick(View view, int position) {
        Intent intent = new Intent(context , IndividualChatDisp.class);

        intent.putExtra("monumber",chatsList.get(position).get_number());
        intent.putExtra("name",chatsList.get(position).get_name());

        startActivity(intent);
    }
}
