package com.example.sattar.gcmchat;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Sattar on 6/10/2016.
 */
public class UpdateCommonList {

    static ArrayList<ContactsWrapper> contactList, serverlist, commonList=null;
    public static boolean Server, moContacts,complete;
    Context context;
    public UpdateCommonList(Context conn){

        context=conn;
        Server=false;
        moContacts=false;
        complete=false;

        new GetContacts().execute();
        new totalRegistredClients().execute(ServerUrls.contactsUrl);
        new CommonContacts().execute();
    }






    class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            contactList = new ArrayList<ContactsWrapper>();

            ContentResolver resolver = context.getContentResolver();
            Cursor cursor=resolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
            while(cursor.moveToNext()){

                ContactsWrapper contactInstance=new ContactsWrapper();


                String id= cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                Cursor phCursor=resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{id}, null);

                String phNumber=null;

                if(phCursor.moveToNext()) {
                    phNumber = phCursor.getString(phCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phNumber=phNumber.replaceAll("\\s+","");
                }

                phCursor.close();
                if(phNumber!=null) {
                    contactInstance.set_name(name);
                    contactInstance.set_status("SatChat");
                    contactInstance.set_numbers(phNumber);

                    contactList.add(contactInstance);
                }
            }
            cursor.close();

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            moContacts=true;
        }
    }




    class totalRegistredClients extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;

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
                connection.setRequestProperty("Content-Language","en-US");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null){
                    sb.append(line);
                }

                return sb.toString();

            } catch (Exception e) {
                return "E:" + e;
            }
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            serverlist=parseJSON(result);
            Server=true;
        }

    }

    class CommonContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            while(!(Server&&moContacts));

            commonList=new ArrayList<ContactsWrapper>();
            commonList.clear();

            SharedPreferences sharedpreferences = context.getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
            String selfnumber = sharedpreferences.getString("number", "");

            String number1,number;
            Iterator iterator=contactList.iterator();
            while(iterator.hasNext()){
                ContactsWrapper contact=(ContactsWrapper)iterator.next();
                number=contact.get_numbers();
                Iterator iterator1=serverlist.iterator();
                while(iterator1.hasNext()){
                    ContactsWrapper contact1=(ContactsWrapper)iterator1.next();
                    number1=contact1.get_numbers();
                    if(!checkInList(number1)&&(number.equals(number1)||number.equals("+91"+number1)||number.equals("91"+number1))&&!(number.equals(selfnumber)||number.equals("+91"+selfnumber)||number.equals("91"+selfnumber))){

                        ContactsWrapper cont=new ContactsWrapper();
                        cont.set_name(contact.get_name());
                        cont.set_numbers(contact1.get_numbers());
                        cont.set_status(contact1.get_status());
                        commonList.add(cont);

                    }

                }

            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            complete=true;
            new updateDatabaseContacts().execute();

        }
    }


    public boolean checkInList(String num){
        if(commonList!=null){
            Iterator iterator=commonList.iterator();
            while(iterator.hasNext()) {
                ContactsWrapper contact = (ContactsWrapper) iterator.next();
                 if(num.equals(contact.get_numbers())){
                     return  true;
                 }
            }
        }
      return  false;
    }


    class updateDatabaseContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            DataBase obj=new DataBase(context);
            obj.deleteCommonContacts(obj);

            Iterator iterator=commonList.iterator();
            while(iterator.hasNext()){
                ContactsWrapper contact=(ContactsWrapper)iterator.next();
                String name=contact.get_name();
                String number=contact.get_numbers();
                String status=contact.get_status();
                obj.insertCommonContact(obj,name,number,status);
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ContactFragment.updateContactList();
        }
    }





    private ArrayList<ContactsWrapper> parseJSON(String response) {
        ArrayList<ContactsWrapper>  list = new ArrayList<ContactsWrapper>();

        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                ContactsWrapper contact = new ContactsWrapper();
                contact.set_name("Sam");
                contact.set_numbers(jsonObject.getString("mobileno"));
                contact.set_status(jsonObject.getString("status"));

                list.add(contact);
            }
        } catch (JSONException e) {

        }

        return list;
    }

}
