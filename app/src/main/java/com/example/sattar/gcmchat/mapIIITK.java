package com.example.sattar.gcmchat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class mapIIITK extends AppCompatActivity implements OnMapReadyCallback {

    ImageButton pBhavan,dispen,auro,basket;
    Marker m;
    GoogleMap ma;
    List<LatLng> poly;
    double latitude = 0;
    double longitude = 0;
    LocationManager locationManager;
    Location location;
    GoogleApiClient mGoogleApiClient;


    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 100;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_iiitk);



        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.fragment);

        mapFragment.getMapAsync(this);



        pBhavan=(ImageButton)findViewById(R.id.prabhavan);
         pBhavan.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 m.setPosition(new LatLng(26.864292, 75.810683));
                 m.setTitle("MNIT Design Center");
                 m.showInfoWindow();
                 m.setVisible(true);
                 m.setDraggable(true);
                 ma.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(26.865141, 75.807810), 15));

             }
         });


        dispen=(ImageButton)findViewById(R.id.dispens);
        dispen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                m.setPosition(new LatLng(26.862231, 75.812241));
                m.setTitle("MNIT Dispensary");
                m.showInfoWindow();
                m.setVisible(true);
                m.setDraggable(true);
                ma.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(26.862231, 75.812241), 15));

            }
        });



        auro=(ImageButton)findViewById(R.id.aurob);
        auro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                m.setPosition(new LatLng(26.862745, 75.820356));
                m.setTitle("Aurobindo Hostel");
                m.showInfoWindow();
                m.setVisible(true);
                m.setDraggable(true);
                ma.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(26.862745, 75.820356), 15));

            }
        });

        basket=(ImageButton)findViewById(R.id.basketb);
        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                m.setPosition(new LatLng(26.861734, 75.814705));
                m.setTitle("MNIT Basketball Court");
                m.showInfoWindow();
                m.setVisible(true);
                m.setDraggable(true);
                ma.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(26.861734, 75.814705), 15));

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap map) {


         ma=map;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        ma.setMyLocationEnabled(true);
        ma.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(26.8626, 75.8186), 15));
        m= map.addMarker(new MarkerOptions()
                .title("MNIT Jaipur")
                .position(new LatLng(26.8626, 75.8186)));

       // drawRoute();

     Intent intent=new Intent(this,registrationService.class);
        startService(intent);

    }






    void drawRoute(){

        LatLng dest= new LatLng(26.861734, 75.814705);
        LatLng orig=  new LatLng(26.864292, 75.810683);
        String url = getdirecUrl(orig, dest);

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);


    }




    private String getdirecUrl(LatLng origin,LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }



    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }




    private class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }



    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }


        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();


            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();


                List<HashMap<String, String>> path = result.get(i);


                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(3);
                lineOptions.color(Color.BLUE);
            }


            ma.addPolyline(lineOptions);
        }
    }



}







          /*
         locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
     Criteria criteria = new Criteria();
       // String provider = locationManager.getBestProvider(criteria, true);

        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        Log.d("providers",String.valueOf(providers.size()));








        try {

           Boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
          Boolean  isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                  Toast.makeText(getApplicationContext(),"No network provider is not available",Toast.LENGTH_LONG).show();
            } else {
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }else{
                            Toast.makeText(getApplicationContext(),"location is null in network",Toast.LENGTH_LONG).show();
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                                Toast.makeText(getApplicationContext(),latitude+" sam "+longitude,Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"location is null in gps",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();


            Toast.makeText(getApplicationContext(),String.valueOf(e),Toast.LENGTH_LONG).show();
        }




*/






//Location l = locationManager.getLastKnownLocation(providers.get(0));

    /*    if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            Location l = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            //System.out.print(l);
            Log.d("provider sam", String.valueOf(l));
        }else{
            Log.d("provider sam", "network is not enabled");
        }*/
//        for (String provider : providers) {
//            Log.d("providers",provider);
//            Location l = locationManager.getLastKnownLocation(provider);
//            if (l == null) {
//                continue;
//            }
//            if (bestLocation == null
//                    || l.getAccuracy() < bestLocation.getAccuracy()) {
//                bestLocation = l;
//            }
//        }

//    if (bestLocation == null) {
//        Toast.makeText(getApplicationContext(),"location is null",Toast.LENGTH_LONG).show();

//     }else{

// Toast.makeText(getApplicationContext(),l.getLatitude()+" sam "+l.getLongitude(),Toast.LENGTH_LONG).show();
//  }