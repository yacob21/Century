package com.example.user.century.produkKategori;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.century.CONFIG;
import com.example.user.century.GuestLoginActivity.SessionLokasi;
import com.example.user.century.R;
import com.example.user.century.Session.SessionManagement;
import com.example.user.century.brandAdapter;
import com.example.user.century.Session.SessionKategori;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.apptik.widget.MultiSlider;

public class FilterActivity extends AppCompatActivity {
    EditText etMin,etMax;
    RadioButton rbTerendah,rbTertinggi,rbPenjualan;
    LinearLayout btnBrand;
    Button btnTerapkan;
    TextView tvBrand;
    RequestQueue queue;
    SessionManagement session;
    SessionKategori sessionKategori;
    String id_lokasi;

    public static String min;
    public static String max;
    public static String jenis;
    public static String order = "Id";
    public static String by = "asc";
    public static String brand;

    ProgressBar pb;
    Button btnRefresh;
    LinearLayout koneksiLayout,mainLayout;

    SessionLokasi sessionLokasi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Filter</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etMin = (EditText) findViewById(R.id.etMin);
        etMax = (EditText) findViewById(R.id.etMax);
        final MultiSlider multiSlider1 = (MultiSlider)findViewById(R.id.range_slider1);

        rbTerendah = (RadioButton) findViewById(R.id.rbTerendah);
        rbTertinggi = (RadioButton) findViewById(R.id.rbTertinggi);
        rbPenjualan = (RadioButton) findViewById(R.id.rbPenjualan);
        btnBrand = (LinearLayout) findViewById(R.id.btnBrand);
        tvBrand = (TextView) findViewById(R.id.tvBrand);
        btnTerapkan = (Button) findViewById(R.id.btnTerapkan);
        session = new SessionManagement(getApplicationContext());
        sessionKategori = new SessionKategori(getApplicationContext());
        sessionLokasi = new SessionLokasi(getApplicationContext());

        pb= (ProgressBar) findViewById(R.id.progressBar);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        koneksiLayout = (LinearLayout) findViewById(R.id.koneksiLayout);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);

        queue = Volley.newRequestQueue(this);

        HashMap<String, String> user = session.getUserDetails();
        final HashMap<String, String> kategori = sessionKategori.getKategoriDetails();
        HashMap<String, String> lokasi = sessionLokasi.getLokasiDetails();

        if(CONFIG.CekGuest.equals("ya")){
            String url1 = "http://fransis.rawatwajah.com/century/getMinMax.php?nama_kategori="+kategori.get(SessionKategori.KEY_KATEGORI).replace(" ","+")
                    +"&id_lokasi="+lokasi.get(SessionLokasi.KEY_ID_LOKASI);
            JsonObjectRequest req = new JsonObjectRequest(url1, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray users = null;
                            try {
                                users = response.getJSONArray("result");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            for (int i = 0; i < users.length(); i++) {
                                try {
                                    pb.setVisibility(View.GONE);
                                    mainLayout.setVisibility(View.VISIBLE);
                                    JSONObject obj = users.getJSONObject(i);
                                    etMax.setText(obj.getString("HargaMax"));
                                    etMin.setText(obj.getString("HargaMin"));
                                    multiSlider1.setMax(obj.getInt("HargaMax"));
                                    multiSlider1.setMin(obj.getInt("HargaMin"));
                                    multiSlider1.getThumb(0).setValue(obj.getInt("HargaMin"));
                                    multiSlider1.getThumb(1).setValue(obj.getInt("HargaMax"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                }
            });

            queue.add(req);
        }
        else if(CONFIG.CekGuest.equals("tidak")){
            String url1Filter = "http://fransis.rawatwajah.com/century/selectLokasi.php?email="+user.get(SessionManagement.KEY_EMAIL);
            JsonObjectRequest reqFilter = new JsonObjectRequest(url1Filter, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray users = null;
                            try {
                                users = response.getJSONArray("result");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            for (int i = 0; i < users.length(); i++) {
                                try {
                                    JSONObject obj = users.getJSONObject(i);
                                    id_lokasi = obj.getString("Id_Lokasi");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String url1 = "http://fransis.rawatwajah.com/century/getMinMax.php?nama_kategori="+kategori.get(SessionKategori.KEY_KATEGORI).replace(" ","+")
                                        +"&id_lokasi="+id_lokasi;
                                JsonObjectRequest req = new JsonObjectRequest(url1, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                JSONArray users = null;
                                                try {
                                                    users = response.getJSONArray("result");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                for (int i = 0; i < users.length(); i++) {
                                                    try {
                                                        pb.setVisibility(View.GONE);
                                                        mainLayout.setVisibility(View.VISIBLE);
                                                        JSONObject obj = users.getJSONObject(i);
                                                        etMax.setText(obj.getString("HargaMax"));
                                                        etMin.setText(obj.getString("HargaMin"));
                                                        multiSlider1.setMax(obj.getInt("HargaMax"));
                                                        multiSlider1.setMin(obj.getInt("HargaMin"));
                                                        multiSlider1.getThumb(0).setValue(obj.getInt("HargaMin"));
                                                        multiSlider1.getThumb(1).setValue(obj.getInt("HargaMax"));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                                    }
                                });

                                queue.add(req);
                            }

                        }

                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                }

            });
            queue.add(reqFilter);
        }




        if(brandAdapter.tempBrand != null){
            tvBrand.setText(brandAdapter.tempBrand.replace("null, ",""));
        }

        btnBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandAdapter.tempBrand=null;
                Intent i = new Intent(FilterActivity.this,BrandActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });


        if(!rbTertinggi.isChecked() && !rbTerendah.isChecked()){
            order = "Id";
            by = "asc";
        }

        rbTertinggi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    order="HargaDiskon";
                    by="desc";
                }

            }
        });

        rbTerendah.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    order="HargaDiskon";
                    by="asc";
                }
            }
        });

        rbPenjualan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    order="hasil";
                    by="desc";
                }
            }
        });



        multiSlider1.setOnThumbValueChangeListener(new MultiSlider.SimpleChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int
                    thumbIndex, int value) {
                if (thumbIndex == 0) {
                    etMin.setText(String.valueOf(value));
                } else {
                    etMax.setText(String.valueOf(value));
                }
            }
        });



        btnTerapkan.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ProdukKategoriActivity.cekFilter="ada";

               if(tvBrand.getText().toString().equals("Brand")){
                   jenis = "uang";
                   brand= "null";
               }
               else{
                   jenis = "brand";
                   brand = "\"*"+tvBrand.getText().toString().replace(" ","*\" or \"*").replace(",","")+"*\"";
                   brandAdapter.tempBrand=null;
               }
               min = etMin.getText().toString().replace(",","");
               max = etMax.getText().toString().replace(",","");
               Intent i = new Intent(FilterActivity.this,ProdukKategoriActivity.class);
               startActivity(i);

           }
       });


        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isDataConnectionAvailable(FilterActivity.this) == false){
                    Toast.makeText(FilterActivity.this, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
                    mainLayout.setVisibility(View.GONE);
                    pb.setVisibility(View.GONE);
                    koneksiLayout.setVisibility(View.VISIBLE);
                }else{
                    overridePendingTransition( 0, 0);
                    startActivity(getIntent());
                    finish();
                    overridePendingTransition( 0, 0);
                }

            }
        });





    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                finish();
                brandAdapter.tempBrand=null;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(isDataConnectionAvailable(FilterActivity.this) == false){
            //Toast.makeText(context, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
            mainLayout.setVisibility(View.GONE);
            pb.setVisibility(View.GONE);
            koneksiLayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        brandAdapter.tempBrand=null;
    }


    public static boolean isDataConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


}
