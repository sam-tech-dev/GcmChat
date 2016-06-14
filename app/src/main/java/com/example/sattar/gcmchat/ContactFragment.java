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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by Sattar on 6/5/2016.
 */
public class ContactFragment extends Fragment  implements RCVClickListener{

     static ArrayList<ContactsWrapper> contactList,dummyList;
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

        dummyList=getCommonContactList();
        if(dummyList!=null){
            setContactAdapter(dummyList);
        }else{
            Toast.makeText(context,"Loading",Toast.LENGTH_SHORT).show();
        }


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

       // ContactFragment newFragment = new ContactFragment();

       // getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, new ContactFragment()).commit();

        if(isAdded()){
            Intent intent = new Intent(context, IndividualChatDisp.class);
            intent.putExtra("monumber", contactList.get(position).get_numbers());
            intent.putExtra("name", contactList.get(position).get_name());
            startActivity(intent);
        }
    }



    static  ArrayList<ContactsWrapper> getCommonContactList(){

        DataBase object=  new DataBase(context);

        Cursor cursor = object.getWholeCommonContacts(object);
        contactList=new ArrayList<ContactsWrapper>();

        if( cursor.moveToFirst()){

            do{
                ContactsWrapper indMsginstance=new ContactsWrapper();
                indMsginstance.set_name(cursor.getString(0));
                indMsginstance.set_numbers(cursor.getString(1));
                indMsginstance.set_status(cursor.getString(2));

                contactList.add(indMsginstance);

            }while(cursor.moveToNext());

            cursor.close();

            return  contactList;
        }else{
            return null;
        }

    }

    static void updateContactList(){

        try{
            contactList.clear();
            ArrayList<ContactsWrapper> newList=getCommonContactList();
            new ContactFragment().setContactAdapter(contactList);
            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount()-1);

        }catch(Exception e){

            Toast.makeText(context, "error in update", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


}
