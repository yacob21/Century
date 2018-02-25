package com.example.user.century.SearchPackage;

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
import com.example.user.century.Brand;
import com.example.user.century.CONFIG;
import com.example.user.century.GuestLoginActivity.SessionLokasi;
import com.example.user.century.R;
import com.example.user.century.Session.SessionManagement;
import com.example.user.century.Session.SessionSearch;
import com.example.user.century.brandAdapter;
import com.example.user.century.Session.SessionKategori;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BrandSearch extends AppCompatActivity {
    RecyclerView cardListBrand;
    RequestQueue queue;
    SessionManagement session;
    SessionKategori sessionKategori;
    String id_lokasi;
    List<Brand> result = new ArrayList<>();
    List<Brand> resultSearch = new ArrayList<>();
    brandAdapter ba;
    Button btnSimpan;

    ProgressBar pb;
    Button btnRefresh;
    LinearLayout koneksiLayout,mainLayout;
    EditText etSearch;
    Context context;
    SessionSearch sessionSearch;

    SessionLokasi sessionLokasi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_search);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Brand</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        cardListBrand = (RecyclerView) findViewById(R.id.cardListBrand);
        cardListBrand.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        cardListBrand.setLayoutManager(llm);
        etSearch = (EditText) findViewById(R.id.etSearch);
        session = new SessionManagement(getApplicationContext());
        sessionKategori = new SessionKategori(getApplicationContext());
        sessionSearch = new SessionSearch(getApplicationContext());
        sessionLokasi = new SessionLokasi(getApplicationContext());


        context  =this;
        pb= (ProgressBar) findViewById(R.id.progressBar);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        koneksiLayout = (LinearLayout) findViewById(R.id.koneksiLayout);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);

        queue = Volley.newRequestQueue(this);
        HashMap<String, String> user = session.getUserDetails();
        final HashMap<String, String> kategori = sessionKategori.getKategoriDetails();
        final HashMap<String, String> search = sessionSearch.getSearchDetails();
        final HashMap<String, String> lokasi = sessionLokasi.getLokasiDetails();

        if(CONFIG.CekGuest.equals("tidak")){
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
                                String url1 = "http://fransis.rawatwajah.com/century/getBrandSearch.php?nama_kategori="+kategori.get(SessionKategori.KEY_KATEGORI).replace(" ","+")
                                        +"&id_lokasi="+id_lokasi+"&search=\"*"+search.get(SessionSearch.KEY_SEARCH).replaceAll(" ","*\" or \"*")+"*\"";
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
                                                        JSONObject obj = users.getJSONObject(i);
                                                        pb.setVisibility(View.GONE);
                                                        mainLayout.setVisibility(View.VISIBLE);
                                                        result.add(new Brand(obj.getString("Merk")));
                                                        ba = new brandAdapter(context,result);
                                                        cardListBrand.setAdapter(ba);
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
        else if(CONFIG.CekGuest.equals("ya")){
            String url1 = "http://fransis.rawatwajah.com/century/getBrandSearch.php?nama_kategori="+kategori.get(SessionKategori.KEY_KATEGORI).replace(" ","+")
                    +"&id_lokasi="+lokasi.get(SessionLokasi.KEY_ID_LOKASI)+"&search=\"*"+search.get(SessionSearch.KEY_SEARCH).replaceAll(" ","*\" or \"*")+"*\"";
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
                                    JSONObject obj = users.getJSONObject(i);
                                    pb.setVisibility(View.GONE);
                                    mainLayout.setVisibility(View.VISIBLE);
                                    result.add(new Brand(obj.getString("Merk")));
                                    ba = new brandAdapter(context,result);
                                    cardListBrand.setAdapter(ba);
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
                    if (textlength <= result.get(i).getNama().length()) {
                        if (result.get(i).getNama().toLowerCase().trim().contains(
                                etSearch.getText().toString().toLowerCase().trim())) {
                            resultSearch.add(result.get(i));
                        }
                    }
                }
                ba = new brandAdapter(BrandSearch.this, resultSearch);
                cardListBrand.setAdapter(ba);
                cardListBrand.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            }
        });





        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BrandSearch.this,FilterSearchActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isDataConnectionAvailable(BrandSearch.this) == false){
                    Toast.makeText(BrandSearch.this, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
                    mainLayout.setVisibility(View.GONE);
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
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(isDataConnectionAvailable(BrandSearch.this) == false){
            //Toast.makeText(context, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
            mainLayout.setVisibility(View.GONE);
            pb.setVisibility(View.GONE);
            koneksiLayout.setVisibility(View.VISIBLE);
        }
    }


    public static boolean isDataConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }



}
