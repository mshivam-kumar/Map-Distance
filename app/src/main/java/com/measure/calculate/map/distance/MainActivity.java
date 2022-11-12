package com.measure.calculate.map.distance;

import java.util.*;
import java.lang.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class MainActivity extends AppCompatActivity {

    private static double endLatitude;
    private static double endLongitude;
    private TextView AddressText;
    private Button startButton;
    private Button endButton;
    private Button loadSavedStartPoint;
    private LocationRequest locationRequest;
    public static double latitude1=0;
    public static double longitude1=0;
    private static double latitude2=0;
    private static double longitude2=0;
    public static boolean flagStartBtn=false;
    public static boolean flagEndBtnPressed=false;
    private TextView showMsg,inKm,inMeter,inYards;

    private static final long TIME_INTERVAL_GAP=2000;
    private long lastTimeClicked=System.currentTimeMillis();
    private   boolean endButtonClickedForFirstTime=false;


    private String SPrefLatitude,SPrefLongitude;
    SharedPreferences SharedPLatitudeLongitude;
    SharedPreferences.Editor SharedPLatitudeLongitudeEditor;



    private static boolean NotSavedInSharedPref=true;
    private static boolean NotPressedStartButton=true;
    private ProgressBar mProgressBar;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
//        Log.d("svm","get intent : "+getIntent());
//        startActivity(new Intent(MainActivity.this));
//        startActivity(getIntent());
        System.exit(0);
    }

//    @Override
//    protected void onRestart() {//With back button reload this activity to update changes in group list
////        MainActivity.flagEndBtnPressed=false;
////        MainActivity.NotSavedInSharedPref=true;
////        MainActivity.NotPressedStartButton=true;
////        MainActivity.flagEndBtnPressed=false;
////        MainActivity.flagStartBtn=false;
////        Intent intent = getIntent();
////        finish();
////        finish();
//        super.onRestart();
//        finish();
//        Log.d("svm","inside onRestart method");
////        startActivity(getIntent());
////        super.onRestart();
////        startActivity(intent);
////        super.onRestart();
////        finish();
//        overridePendingTransition(0, 0);
//        startActivity(getIntent());
//        overridePendingTransition(0, 0);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPLatitudeLongitude= getSharedPreferences("latitude_longitude_details",MODE_PRIVATE);
        SharedPLatitudeLongitudeEditor=SharedPLatitudeLongitude.edit();

        SPrefLatitude=SharedPLatitudeLongitude.getString("latitude","");
        SPrefLongitude=SharedPLatitudeLongitude.getString("longitude","");




//        AddressText = findViewById(R.id.addressText);
        showMsg=findViewById(R.id.showMsgs);
        startButton = findViewById(R.id.startButton);
        endButton = findViewById(R.id.endButton);

        mProgressBar= findViewById(R.id.progress_circular);
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);


        inKm=findViewById(R.id.inKiloMeter);
         inMeter=findViewById(R.id.inMeter);
         inYards=findViewById(R.id.inYards);


//        TextView inKm=findViewById(R.id.inKiloMeter);
//        TextView inMeter=findViewById(R.id.inMeter);
//        TextView inYards=findViewById(R.id.inYards);


       

        loadSavedStartPoint=findViewById(R.id.loadSavedStartPoint);


        endButton.setEnabled(false);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showMsg.setTextColor(Color.parseColor("#CD352A"));

                inKm.setText("");
                inMeter.setText("");
                inYards.setText("");

                MainActivity.NotPressedStartButton=false;//necessary condition for getCurrentLocation method

                flagStartBtn=true;//startButton pressed
                flagEndBtnPressed=false;

                showMsg.setText("Please Wait !!");
                mProgressBar.setVisibility(ProgressBar.VISIBLE);

                startButton.setEnabled(false);
                loadSavedStartPoint.setEnabled(false);


//                Log.d("svm","using l1 arrayList Lat : "+MainActivity.latitude1+" Lon : "+MainActivity.longitude1);



            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showMsg.setTextColor(Color.parseColor("#CD352A"));

                long now=System.currentTimeMillis();
                if(((now-lastTimeClicked)<TIME_INTERVAL_GAP) && endButtonClickedForFirstTime) {
//                    UserLogIn.endButtonClickedForFirstTime=true;

                    inKm.setText("");
                    inMeter.setText("");
                    inYards.setText("");

                    showMsg.setText("Cllick The End Button After 2 Seconds \n      For More Accurate Results");
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                    return;
                }
                lastTimeClicked=now;

                endButtonClickedForFirstTime=true;


                showMsg.setText("PLEASE WAIT !!");
                mProgressBar.setVisibility(ProgressBar.VISIBLE);

                flagEndBtnPressed=true;//endButton pressed

                getCurrentLocation();
                endButton.setEnabled(false);


//                Log.d("svm","Before calcualating : Lat1 : "+MainActivity.latitude1+" Lon1  :  "+MainActivity.longitude1+"" +
//                        "\nLat2 : "+MainActivity.latitude2+" Lon2 :  "+MainActivity.longitude2);

//                double distanceInKM=distance();

//                double distanceInMeter=distanceInKM*1000;
//                double distanceInYards=distanceInMeter*1.0936132983;
//
//                inKm.setText("K.M. : "+distanceInKM);
//                inMeter.setText("Meter : "+distanceInMeter);
//                inYards.setText("Yards : "+distanceInYards);
//
//
//                Log.d("svm","Calculated distance (KM) : "+distanceInKM);
//                Log.d("svm","Calculated distance (M) : "+distanceInMeter);
//                Log.d("svm","Calculated distance (Yards) : "+distanceInYards);

            }
        });

        loadSavedStartPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showMsg.setTextColor(Color.parseColor("#CD352A"));

                inKm.setText("");
                inMeter.setText("");
                inYards.setText("");

                SPrefLatitude=SharedPLatitudeLongitude.getString("latitude","");
                SPrefLongitude=SharedPLatitudeLongitude.getString("longitude","");

                Log.d("svm","Previous saved loaded coordinates : Latitude = "+SPrefLatitude+" Longitude = "+SPrefLongitude);
                if(SPrefLatitude.length()>0 && SPrefLongitude.length()>0) {
                    MainActivity.NotPressedStartButton = false;
                    flagStartBtn = true;//startButton pressed
                    flagEndBtnPressed = false;

                    showMsg.setText("Please Wait !!");
                    mProgressBar.setVisibility(ProgressBar.VISIBLE);

                    startButton.setEnabled(false);
                    loadSavedStartPoint.setEnabled(false);
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                    endButton.setEnabled(true);
                    showMsg.setText("Previous Used Start Point Loaded Successfully.\n          Press the end button to get distance ");

                }
                else
                {
                    showMsg.setText("No Last Time Used Start Point Found.Click The Start Button To Save The Current Start Point.");
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    getCurrentLocation();

                }else {

                    turnOnGPS();
                }
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }

    private void getCurrentLocation(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        if(NotPressedStartButton)
                                        {
                                            inKm.setText("");
                                            inMeter.setText("");
                                            inYards.setText("");

                                            showMsg.setText("Please Press Start Button First Or Load Previous Start Point");
                                            mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                                            return;

                                        }

                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();

//                                        showMsg.setText("");

                                        Log.d("svm","Current Latitude : "+latitude+
                                                "\nCurrent Longitude : "+longitude);


                                        if(MainActivity.NotSavedInSharedPref && !MainActivity.flagEndBtnPressed)
                                        {
                                            MainActivity.NotSavedInSharedPref=false;
                                            MainActivity.NotPressedStartButton=false;
                                            SharedPLatitudeLongitudeEditor.putString("latitude", latitude+"");
                                            SharedPLatitudeLongitudeEditor.commit();
                                            SharedPLatitudeLongitudeEditor.putString("longitude",longitude+"");
                                            SharedPLatitudeLongitudeEditor.commit();

                                            Log.d("svm","Saved in Shared Preference");

                                            Log.d("svm"," start point details Latitude : "+ latitude+" Longitude : "+longitude);


                                            showMsg.setText("Start Point Saved Successfully For Next Use.\n       Press the end button to get distance ");
                                            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                                            endButton.setEnabled(true);




                                            Toast.makeText(MainActivity.this, "Start Position Data Captured Successfully",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                        else if(MainActivity.flagEndBtnPressed)
                                        {
                                            MainActivity.NotSavedInSharedPref=true;

                                            MainActivity.endLatitude=latitude;
                                            MainActivity.endLongitude=longitude;

                                            Log.d("svm"," end point details Latitude : "+ latitude+" Longitude : "+longitude);

                                            showMsg.setText("Calculating Distance ...");
                                            mProgressBar.setVisibility(ProgressBar.VISIBLE);


                                            double distanceInKM=distance();
                                            double distanceInMeter=distanceInKM*1000;
                                            double distanceInYards=distanceInMeter*1.0936132983;

                                            startButton.setEnabled(true);
                                            loadSavedStartPoint.setEnabled(true);



                                            showMsg.setTextColor(Color.parseColor("#24B62A"));
                                            showMsg.setText("Calculated Results May Not Be 100% Accurate\n" +
                                                    "          It Depends On Your Internet Speed");
                                            mProgressBar.setVisibility(ProgressBar.INVISIBLE);


                                            inKm.setText("K.Ms. : "+distanceInKM);
                                            inMeter.setText("Meters : "+distanceInMeter);
                                            inYards.setText("Yards : "+distanceInYards);



//                                           endButton.setEnabled(true);


                                            Log.d("svm","Calculated distance (KM) : "+distanceInKM);
                                            Log.d("svm","Calculated distance (M) : "+distanceInMeter);
                                            Log.d("svm","Calculated distance (Yards) : "+distanceInYards);


                                        }




//                                        endButton.setEnabled(true);

//                                        if(MainActivity.L1 && !MainActivity.L2)
//                                        {
//                                            Log.d("svm","Update latitude 1");
//
//                                            MainActivity.latitude1=latitude;
//                                            MainActivity.longitude1=longitude;
//                                            MainActivity.L1=false;
//                                            MainActivity.L2=true;
//
//                                        }
////                                        else if(MainActivity.latitude2==0.0 && MainActivity.latitude2!=0.0)
//                                        else
//                                        {
//                                            Log.d("svm","Update latitude 2");
//                                            MainActivity.latitude2=latitude;
//                                            MainActivity.longitude2=longitude;
//                                        }
//                                        LLDetails[0] =new LatitudeLongitudeDetails(latitude,longitude);
//                                        LLDetails.setLatitude(latitude);
//                                        LLDetails.setLongitude(longitude);
//


//                                        endButton.setEnabled(true);


//                                        AddressText.setText("Latitude: "+ latitude + "\n" + "Longitude: "+ longitude);
                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

    }

    private void turnOnGPS() {



        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(MainActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                resolvableApiException.startResolutionForResult(MainActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }

    private  double distance()
    {
        SPrefLatitude=SharedPLatitudeLongitude.getString("latitude","");
        SPrefLongitude=SharedPLatitudeLongitude.getString("longitude","");
        double lat1=Double.parseDouble(SPrefLatitude);
        double lon1=Double.parseDouble(SPrefLongitude);
        double lat2=MainActivity.endLatitude;
        double lon2=MainActivity.endLongitude;


        Log.d("svm"," Inside distance Lat1 : "+lat1+"" +
                "\nLon1 : "+lon1+"\nLat2 : "+lat2+"\n Lon2 : "+lon2);
        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
//        lon1 = Math.toRadians(lon1);
//        lon2 = Math.toRadians(lon2);
//        lat1 = Math.toRadians(lat1);
//        lat2 = Math.toRadians(lat2);
//
//        // Haversine formula
//        double dlon = lon2 - lon1;
//        double dlat = lat2 - lat1;
//        double a = Math.pow(Math.sin(dlat / 2), 2)
//                + Math.cos(lat1) * Math.cos(lat2)
//                * Math.pow(Math.sin(dlon / 2),2);
//
//        double c = 2 * Math.asin(Math.sqrt(a));
//
//        // Radius of earth in kilometers. Use 3956
//        // for miles
//        double r = 6371;

        double p = 0.017453292519943295;    // Math.PI / 180
//        double c = Math.cos;
        double a;



//        SharedPLatitudeLongitudeEditor.putString("latitude", "0");
//        SharedPLatitudeLongitudeEditor.commit();
//        SharedPLatitudeLongitudeEditor.putString("longitude","0");
//        SharedPLatitudeLongitudeEditor.commit();


        MainActivity.endLatitude=0;
        MainActivity.endLongitude=0;

        MainActivity.flagStartBtn=false;
        MainActivity.NotPressedStartButton=true;

        showMsg.setText("");
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);



        // calculate the result\
        return 12742 * Math.asin(Math.sqrt(a)); // 2 * R; R = 6371 km

    }


}
