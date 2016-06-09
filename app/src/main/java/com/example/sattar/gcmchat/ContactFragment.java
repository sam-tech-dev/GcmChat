package com.example.sattar.gcmchat;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by Sattar on 6/5/2016.
 */
public class ContactFragment extends Fragment  implements RCVClickListener{

     static ArrayList<ContactsWrapper> contactList;
     static  Context context;
    static RecyclerView recyclerView;
    static ContactsAdapter mAdapter;


    public  ContactFragment(){

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_contacts, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.contact_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    new  waitforList().execute();
        return rootView;
    }



    private void setContactAdapter(ArrayList<ContactsWrapper> chatList) {
        // TODO Auto-generated method stub
        mAdapter = new ContactsAdapter(context, chatList);

        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnRCVClickListener(this);
    }

    @Override
    public void onRCVClick(View view, int position) {
        Intent intent = new Intent(context , IndividualChatDisp.class);

        intent.putExtra("monumber",contactList.get(position).get_numbers());
        intent.putExtra("name",contactList.get(position).get_name());

        startActivity(intent);
    }


    class waitforList extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            while(!MainActivity.complete);

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            contactList=MainActivity.commonList;
            setContactAdapter(contactList);

        }
    }




}
