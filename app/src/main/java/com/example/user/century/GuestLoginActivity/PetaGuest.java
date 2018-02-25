package com.example.user.century.GuestLoginActivity;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.century.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.defaultValue;

public class PetaGuest extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    RequestQueue queue;
    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    public Location lastlocation;
    private Circle circle;

    private Marker currentLocationmMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    static double latitude, longitude;

    double tempLatitude,tempLongitude;
    String tempNamaLokasi,tempAlamatLokasi;
    LatLng mark;
    TextView  tvNama,tvJarak,tvAlamat;
    int tempIdLokasi;

    public static final String UBAH_LOKASI_URL = "http://fransis.rawatwajah.com/century/ubahLokasi.php";

    private static final LatLng INDONESIA = new LatLng(-0.7893,113.9213);
    Context context;

    //////////
    FrameLayout frameLokasi;
    TableLayout tableLokasi;
    Button btnSimpan,btnLokasi;
//////////////////


    ///////////
    String panduanNama;
    String panduanAlamat;
    String panduanId;
    LatLng latLng;

    String pengecekan =null;

    AlertDialog dialog;
    AlertDialog.Builder builder;
    SessionLokasi sessionLokasi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peta_guest);
        //Toast.makeText(this, "Harap pilih outlet Century yang Anda inginkan", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        builder = new AlertDialog.Builder(this);
        queue = Volley.newRequestQueue(this);

        tableLokasi = (TableLayout) findViewById(R.id.tableLokasi);
        frameLokasi = (FrameLayout) findViewById(R.id.frameLokasi);
        tvAlamat = (TextView) findViewById(R.id.tvAlamat);
        tvJarak = (TextView) findViewById(R.id.tvJarak);
        tvNama  = (TextView) findViewById(R.id.tvNama);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        btnLokasi = (Button) findViewById(R.id.btnLokasi);

        statusCheck();
        context = this;
        sessionLokasi = new SessionLokasi(getApplicationContext());

        Intent a = getIntent();
        pengecekan  =a.getStringExtra("panduanNama");
        tvNama.setText(a.getStringExtra("panduanNama"));
        tvAlamat.setText(a.getStringExtra("panduanAlamat"));
        tvJarak.setText(a.getStringExtra("panduanJarak"));
        panduanId = a.getStringExtra("panduanId");
        if(tvAlamat.getText().toString().equals("")||tvNama.getText().toString().equals("")||tvJarak.getText().toString().equals("")||tvNama.getText().toString().equals("Nama Apotek") || tvAlamat.getText().toString().equals("Alamat Apotek") || tvJarak.getText().toString().equals("1 km")){
            tableLokasi.setVisibility(View.INVISIBLE);
            btnSimpan.setVisibility(View.INVISIBLE);
        }
        else{
            tableLokasi.setVisibility(View.VISIBLE);
            btnSimpan.setVisibility(View.VISIBLE);
        }



        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionLokasi.createLokasi(panduanId);
                Toast.makeText(context, "Berhasil Pilih Lokasi", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(PetaGuest.this, GuestActivity.class);
                startActivity(i);

            }
        });



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (client == null) {
                            bulidGoogleApiClient();

                        }
                        mMap.setMyLocationEnabled(true);

                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            bulidGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        Intent a = getIntent();
        double lat = a.getDoubleExtra("panduanLatitude2", defaultValue);
        double longi = a.getDoubleExtra("panduanLongitude2", defaultValue);
        mark = new LatLng(lat, longi);
        if(pengecekan!=null) {
            CameraPosition cp = new CameraPosition.Builder().target(mark).zoom(15).build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
            //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
        }
        String url1 = "http://fransis.rawatwajah.com/century/lokasiawal.php";
        JsonObjectRequest req = new JsonObjectRequest(url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray users = null;
                        try {
                            users = response.getJSONArray("result1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < users.length(); i++) {
                            try {
                                JSONObject obj = users.getJSONObject(i);
                                tempNamaLokasi = obj.getString("Nama_Lokasi");
                                tempAlamatLokasi=obj.getString("Alamat_Lokasi");
                                tempLatitude = obj.getDouble("Latitude");
                                tempLongitude = obj.getDouble("Longitude");
                                tempIdLokasi = obj.getInt("Id_Lokasi");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // Toast.makeText(PetaPertama.this, String.valueOf(tempLongitude), Toast.LENGTH_SHORT).show();
                            mark= new LatLng(tempLatitude,tempLongitude);
                            mMap.addMarker(new MarkerOptions().position(mark).title(tempNamaLokasi.toString().replace("[","").replace("]","")+" | "+tempAlamatLokasi.toString().replace("[","").replace("]","")+"["+tempIdLokasi+"]"));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
            }
        });
        queue.add(req);


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker mark) {
                Animation anim = AnimationUtils.loadAnimation(PetaGuest.this, R.anim.translate);
                tableLokasi.setAnimation(anim);
                btnSimpan.setAnimation(anim);
                tableLokasi.setVisibility(View.VISIBLE);
                btnSimpan.setVisibility(View.VISIBLE);
                Location locationA = new Location("point A");
                Location locationB = new Location("point B");

                locationA.setLatitude(latitude);
                locationA.setLongitude(longitude);

                locationB.setLatitude(mark.getPosition().latitude);
                locationB.setLongitude(mark.getPosition().longitude);
                double jarak = locationA.distanceTo(locationB)/1000;

                panduanNama = mark.getTitle().substring(0,mark.getTitle().indexOf("|")-1);
                panduanAlamat = mark.getTitle().substring(mark.getTitle().indexOf("|")+2,mark.getTitle().indexOf("["));
                panduanId = mark.getTitle().substring(mark.getTitle().indexOf("["),mark.getTitle().length()).replace("[","").replace("]","");

                tvNama.setText(panduanNama);
                tvAlamat.setText(panduanAlamat);
                tvJarak.setText(String.valueOf(String.format( "%.2f km", jarak )));
                //tvJarak.setText(String.valueOf( getDistanceFromLatLonInKm(latitude,longitude,mark.getPosition().latitude,mark.getPosition().longitude)));
                return false;
            }
        });



    }



    protected synchronized void bulidGoogleApiClient() {
        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();

    }






    @Override
    public void onLocationChanged(Location location) {
        LocationManager lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lastlocation = location;
        btnLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PetaGuest.this,PilihPetaGuest.class);
                i.putExtra("panduanLatitude",String.valueOf(latitude));
                i.putExtra("panduanLongitude",String.valueOf(longitude));
                startActivity(i);
            }
        });
        latLng = new LatLng(location.getLatitude() , location.getLongitude());
        if(pengecekan==null){
            CameraPosition cp = new CameraPosition.Builder().target(latLng).zoom(15).build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));

        }

        if(client != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }


    public boolean checkLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED )
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            return false;

        }
        else
            return true;
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }




    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Harap aktifkan GPS pada perangkat Anda")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        Toast.makeText(context, "Aplikasi tidak dapat diakses bila GPS tidak dinyalakan", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sessionLokasi.hapusLokasi();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


}


