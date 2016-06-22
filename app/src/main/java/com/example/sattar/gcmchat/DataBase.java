package com.example.sattar.gcmchat;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Sattar on 6/5/2016.
 */
public class DataBase  extends SQLiteOpenHelper  {

        Context context;
        public static final int database_version = 1;

        public String sql_query="create table messages (id integer primary key autoincrement, name varchar(20),  number varchar(20) not NULL , date_message varchar(15), time_message varchar(15), content text(50), type varchar(20));";
          public String sql_query2="create table contacts ( name varchar(20),  number varchar(20) not NULL, status varchar(70));";

        public DataBase(Context context) {
            super(context, "messsages_operation",null, database_version);
            // TODO Auto-generated constructor stub

            Log.d("databse operation", "database is created");

        }




        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(sql_query);
            db.execSQL(sql_query2);
            Log.d("databse operation", "table is created");

        }



        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub


        }



    public  void  insertCommonContact(DataBase ob,String name,String number, String status){



        SQLiteDatabase sq=ob.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put("name", name);
        cv.put("number", number);
        cv.put("status", status);

        sq.insert("contacts", null, cv);

        Log.d("database operation", "data is inserted");

    }



    public Cursor getWholeCommonContacts(DataBase object){

        SQLiteDatabase SQ=object.getReadableDatabase();

        String sql_query="select * from contacts";

        Cursor cr = SQ.rawQuery(sql_query, null);


        return cr;


    }



    public void deleteCommonContacts(DataBase obj){

        SQLiteDatabase SQ=obj.getWritableDatabase();
        SQ.execSQL("delete from contacts");


    }



    public  void  insertionrow(DataBase ob,String name,String number, String date,String time, String content_msg , String typeofmsg ){

            SQLiteDatabase sq=ob.getWritableDatabase();
            ContentValues cv=new ContentValues();




            cv.put("name", name);
            cv.put("number", number);
            cv.put("date_message", date);
            cv.put("time_message", time);
            cv.put("content", content_msg);
            cv.put("type", typeofmsg);

            sq.insert("messages", null, cv);

            Log.d("database operation", "data is inserted");


        }





        public Cursor getData(DataBase object, int idofsms){

            SQLiteDatabase SQ=object.getReadableDatabase();

            String sql_query="select * from messages where id="+idofsms;

            Cursor cr = SQ.rawQuery(sql_query, null);


            return cr;


        }


        public Cursor getIndividualMsgs(DataBase object,String mobileno){

            Cursor cr=null;

            try{ SQLiteDatabase SQ=object.getReadableDatabase();

                String sql_query="select * from messages where number='"+mobileno+"'";

                cr = SQ.rawQuery(sql_query, null);


            }catch(SQLiteException e){

                Toast.makeText(context, "sqlite exception", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            return cr;


        }



        public Cursor 	getDatatodisplay(DataBase ob){
            Cursor csr=null;
            try{
                SQLiteDatabase SQ=ob.getReadableDatabase();


                String sql_query="select max(id) from messages group by number";

                csr = SQ.rawQuery(sql_query, null);

            }catch(SQLiteException e){

                //	Toast.makeText(context, "sqlite exception error in max id query", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            return csr;
        }


        public int getCountSms(DataBase ob,String mono){

            Context context=null;
            Cursor csr;
            int noSms=0;

            SQLiteDatabase SQ=ob.getReadableDatabase();

            try{
                String sql_query="select * from messages where number='"+mono+"'";

                csr = SQ.rawQuery(sql_query, null);
                noSms = csr.getCount();

                //Toast.makeText(context,String.valueOf(noSms), Toast.LENGTH_LONG).show();

                //Log.d("no of Sms","SMS"+String.valueOf(noSms));

            }catch(Exception e){

                //Toast.makeText(context,"no of sms not found", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }



            return noSms;
        }


        public Integer deleteSingleSms(DataBase obj, String id,String num){


            SQLiteDatabase SQR=obj.getReadableDatabase();
            String sql_query="select * from messages where number='"+num+"'";

            Cursor csr = SQR.rawQuery(sql_query, null);
            Integer noSms = csr.getCount();

            SQLiteDatabase SQ=obj.getWritableDatabase();
            SQ.execSQL("delete from messages where id='"+id+"'");

            return noSms;
        }



        public void  deleteIndividualAllSms(DataBase obj, String number){

            SQLiteDatabase SQS=obj.getReadableDatabase();
            String sql_query="select distinct number from messages";

            Cursor csr = SQS.rawQuery(sql_query, null);
            Integer noSm = csr.getCount();

            SQLiteDatabase SQ=obj.getWritableDatabase();
            // Toast.makeText(context,"in delete all method", Toast.LENGTH_LONG).show();
            SQ.execSQL("delete from messages where number='"+number+"'");

           new ChatFragment().updateFrontList();

        }


        public static String getContactName(Context context,String phonenumber){

            Cursor cursor=null;
            String contactname=null;

            try{

                ContentResolver ctrsvr=context.getContentResolver();

                Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phonenumber));

                cursor=ctrsvr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},null , null, null);

                if(cursor.moveToFirst()){

                    contactname=cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));

                }else{
                    Uri uri1=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode("+91"+phonenumber));

                    cursor=ctrsvr.query(uri1, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},null , null, null);

                    if(cursor.moveToFirst()) {

                        contactname = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                    }else{
                        cursor=null;
                    }
                }

            }catch(Exception e){

               // Toast.makeText(context, "contact name is not found", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


            if(cursor==null){
                return null;
            }

            if(cursor!=null&&!cursor.isClosed()){

                cursor.close();
            }

            return contactname;
        }





}




