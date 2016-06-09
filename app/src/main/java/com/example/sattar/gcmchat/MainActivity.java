package com.example.sattar.gcmchat;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    ViewPager mViewPager;
    TabLayout tabLayout;
   static  ArrayList<ContactsWrapper> contactList, serverlist, commonList;
    public static boolean Server, moContacts,complete;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Server=false;
        moContacts=false;
        complete=false;


        new GetContacts().execute();
        new totalRegistredClients().execute(ServerUrls.contactsUrl);
        new CommonContacts().execute();
        SharedPreferences sharedpreferences = getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
        if(sharedpreferences.contains("number")){
            new getStatus().execute(ServerUrls.getstatusUrl);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mViewPager = (ViewPager) findViewById(R.id.container);
        setUpViewPager(mViewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void setUpViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new ChatFragment() , "CHATS");
        adapter.addFragment(new ContactFragment() , "CONTACTS");
        viewPager.setAdapter(adapter);

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.register) {


            SharedPreferences sharedpreferences = getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
            if(!sharedpreferences.contains("registerCheck")){
            Intent intent =new Intent(MainActivity.this,RegisterOnServer.class);
            startActivity(intent);
            finish();
            }else{
                Toast.makeText(this,"This App already registered",Toast.LENGTH_LONG).show();
            }
        }


        if (id == R.id.status) {

            SharedPreferences sharedpreferences = getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
             if(sharedpreferences.contains("status")){

                 String status=sharedpreferences.getString("status","");
                 Toast.makeText(this,status,Toast.LENGTH_LONG).show();
                // Intent intent =new Intent(MainActivity.this,RegisterOnServer.class);
                // startActivity(intent);

              }

        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    class ViewPagerAdapter extends FragmentPagerAdapter {


        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment , String fragmentTitle){
            fragmentList.add(fragment);
            fragmentTitleList.add(fragmentTitle);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }





    class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            contactList = new ArrayList<ContactsWrapper>();

            ContentResolver resolver = getContentResolver();
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

            String number1,number;
            Iterator iterator=contactList.iterator();
            while(iterator.hasNext()){
                ContactsWrapper contact=(ContactsWrapper)iterator.next();
                 number=contact.get_numbers();
                Iterator iterator1=serverlist.iterator();
                while(iterator1.hasNext()){
                    ContactsWrapper contact1=(ContactsWrapper)iterator1.next();
                     number1=contact1.get_numbers();
                       if(number.equals(number1)){

                           ContactsWrapper cont=new ContactsWrapper();
                            cont.set_name(contact.get_name());
                            cont.set_numbers(contact.get_numbers());
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

            Iterator iterator=commonList.iterator();
            /*
            while(iterator.hasNext()){
                ContactsWrapper contact=(ContactsWrapper)iterator.next();
                String name=contact.get_name();
                String number=contact.get_numbers();
                String status=contact.get_status();
                Log.d("azad","name : "+name+" number : "+number+" status : "+status);
            }
                  */
        }
    }




    class getStatus extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            String  parameters=null;
            SharedPreferences sharedpreferences = getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
            String number = sharedpreferences.getString("number", "");

            try {
               parameters ="mobileno=" + URLEncoder.encode(number, "UTF-8");

            }
            catch (UnsupportedEncodingException e){
                Log.d("exception",e.getMessage());
            }
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

                /*StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null){
                    sb.append(line);
                }*/

                return br.readLine();

            } catch (Exception e) {
                return "E:" + e;
            }
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("ruks",result);
            SharedPreferences sharedpreferences = getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedpreferences.edit();
            edit.putString("status", result);
            edit.commit();


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
