package com.example.user.century.Profil;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.century.Register.LokasiPertama;
import com.example.user.century.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PilihPetaUbah extends AppCompatActivity {
    List<LokasiPertama> result = new ArrayList<>();
    List<LokasiPertama> resultSearch = new ArrayList<>();
    PilihPetaUbahAdapter pppa;
    RecyclerView cardListPetaUbah;

    Context context;
    String nama,alamat;
    double longitude,latitude,jarak;
    int Id;
    RequestQueue queue;

    ProgressBar pbKoneksi,pb;
    Button btnRefresh;
    LinearLayout koneksiLayout,linearLokasi;
    EditText etSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_peta_ubah);
        context = this;
        queue = Volley.newRequestQueue(this);
        getSupportActionBar().setTitle(Html.fromHtml("<'font color='#ffffff'>Pilih Lokasi</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etSearch = (EditText) findViewById(R.id.etSearch);
        cardListPetaUbah = (RecyclerView) findViewById(R.id.cardListPetaUbah);
        cardListPetaUbah.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        cardListPetaUbah.setLayoutManager(llm);

        pb = (ProgressBar) findViewById(R.id.pb);
        pbKoneksi = (ProgressBar) findViewById(R.id.pbKoneksi);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        koneksiLayout = (LinearLayout) findViewById(R.id.koneksiLayout);
        linearLokasi = (LinearLayout) findViewById(R.id.linearLokasi);

        Intent i = getIntent();
        String url1 = "http://fransis.rawatwajah.com/century/lokasicentury.php?lat="+i.getStringExtra("panduanLatitude")+"&long="+i.getStringExtra("panduanLongitude");
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
                                pb.setVisibility(View.GONE);
                                linearLokasi.setVisibility(View.VISIBLE);
                                JSONObject obj = users.getJSONObject(i);
                                nama = obj.getString("Nama_Lokasi");
                                alamat = obj.getString("Alamat_Lokasi");
                                Id=obj.getInt("Id_Lokasi");
                                longitude = obj.getDouble("Longitude");
                                latitude = obj.getDouble("Latitude");
                                jarak = obj.getDouble("Jarak");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            result.add(new LokasiPertama(nama,alamat,Id,latitude,longitude,jarak));
                            pppa = new PilihPetaUbahAdapter(context,result);
                            cardListPetaUbah.setAdapter(pppa);


                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(PilihPetaUbah.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
            }
        });
        queue.add(req);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               int textlength = etSearch.getText().length();
                resultSearch.clear();
                for (int i = 0; i < result.size(); i++) {
                    if (textlength <= result.get(i).getAlamat().length()) {
                        if (result.get(i).getAlamat().toLowerCase().trim().contains(
                                etSearch.getText().toString().toLowerCase().trim())) {
                                resultSearch.add(result.get(i));
                        }
                    }
                }
                pppa = new PilihPetaUbahAdapter(PilihPetaUbah.this, resultSearch);
                cardListPetaUbah.setAdapter(pppa);
                cardListPetaUbah.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            }
        });


        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isDataConnectionAvailable(PilihPetaUbah.this) == false){
                    Toast.makeText(PilihPetaUbah.this, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
                    linearLokasi.setVisibility(View.GONE);
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
                Intent i = new Intent(PilihPetaUbah.this,UbahLokasiActivity.class);
                startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(isDataConnectionAvailable(PilihPetaUbah.this) == false){
            //Toast.makeText(context, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
            linearLokasi.setVisibility(View.GONE);
            pb.setVisibility(View.GONE);
            koneksiLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    public static boolean isDataConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(PilihPetaUbah.this,UbahLokasiActivity.class);
        startActivity(i);
    }



}
