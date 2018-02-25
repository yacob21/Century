package com.example.user.century.produkKategori;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.century.CONFIG;
import com.example.user.century.GuestLoginActivity.SessionLokasi;
import com.example.user.century.R;
import com.example.user.century.Search;
import com.example.user.century.SearchPackage.SearchActivity;
import com.example.user.century.Session.SessionKategori;
import com.example.user.century.Session.SessionManagement;
import com.example.user.century.Session.SessionSearch;
import com.example.user.century.brandAdapter;
import com.example.user.century.terakhirSearchAdapter;
import com.example.user.century.topSearchAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProdukKategoriActivity extends AppCompatActivity {

    private static final int DIFFICULTY_EASY = 1;
//    String as,ad;
    Toolbar toolbar;
    Context context;

    private MenuItem mSearchAction;
    private boolean isSearchOpened = true;
    EditText etSearch;

    RequestQueue queue;


    RecyclerView cardListProduk,cardListTopSearch;
    public static RecyclerView cardListTerakhirSearch;
    List<Produk> result = new ArrayList<>();
    produkAdapter pa;
    String nama,merk,gambar,subkategori;
    int harga;
    String id_kategori,id_promosi,id_produk,stok,diskon,id_produk_per_lokasi;
    SessionManagement session;
    SessionKategori sessionKategori;
    String id_lokasi;
    ProgressBar pb,pb2;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int offset =20;
    NestedScrollView scrollSearch;


    private int previousTotal = 0;

    ProgressBar pbKoneksi;
    Button btnRefresh;
    FrameLayout mainLayout;
    LinearLayout koneksiLayout,layoutSort,btnFilter,linearTemp;


    public static String cekFilter= null;

    LinearLayout mainLinear,searchLinear;

    List<Search> resultTop = new ArrayList<>();
    List<Search> resultTerakhir = new ArrayList<>();
    String terakhirSearch,topSearch,idSearch;
    terakhirSearchAdapter tersa;
    topSearchAdapter topsa;
    TextView tvSearchTemp,tvKategoriTemp;
    TextView tvPencarian,tvTeratas,tvHapusSemua;
    public static LinearLayout linearSearchTerakhirProduk;

    public static final String INSERT_SEARCH_URL = "http://fransis.rawatwajah.com/century/insertSearch.php";
    public static final String DELETE_SEMUA_SEARCH_URL = "http://fransis.rawatwajah.com/century/deleteSemuaPencarian.php";
//    public static final String UPDATE_SEARCH_URL = "http://fransis.rawatwajah.com/century/updatePencarian.php";
    SessionSearch sessionSearch;

    SessionLokasi sessionLokasi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk_kategori);

        Intent a = getIntent();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleMenuSearch();
            }
        });

        sessionKategori = new SessionKategori(getApplicationContext());
        sessionLokasi = new SessionLokasi(getApplicationContext());
        final HashMap<String, String> lokasi = sessionLokasi.getLokasiDetails();
        final HashMap<String, String> kategori = sessionKategori.getKategoriDetails();
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>"+kategori.get(SessionKategori.KEY_KATEGORI)+"</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvSearchTemp = (TextView) findViewById(R.id.tvSearchTemp);
        tvKategoriTemp = (TextView) findViewById(R.id.tvKategoriTemp);
        mainLinear = (LinearLayout) findViewById(R.id.mainLinear);
        searchLinear = (LinearLayout) findViewById(R.id.searchLinear);
        pbKoneksi = (ProgressBar) findViewById(R.id.pbKoneksi);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        koneksiLayout = (LinearLayout) findViewById(R.id.koneksiLayout);
        mainLayout = (FrameLayout) findViewById(R.id.mainLayout);
        layoutSort = (LinearLayout) findViewById(R.id.layoutSort);
        scrollSearch = (NestedScrollView) findViewById(R.id.scrollSearch);
        btnFilter = (LinearLayout) findViewById(R.id.btnFilter);
        sessionSearch = new SessionSearch(getApplicationContext());
        cardListProduk = (RecyclerView) findViewById(R.id.cardListProduk);
        cardListProduk.setHasFixedSize(true);
        pb2 = (ProgressBar) findViewById(R.id.progressBar);
        pb = (ProgressBar) findViewById(R.id.pb);
        cardListTerakhirSearch = (RecyclerView) findViewById(R.id.cardListTerakhirSearch);
        cardListTerakhirSearch.setHasFixedSize(true);
        linearTemp = (LinearLayout) findViewById(R.id.linearTemp);
        cardListTopSearch = (RecyclerView) findViewById(R.id.cardListTopSearch);
        cardListTopSearch.setHasFixedSize(true);
        LinearLayoutManager llmTerakhir = new LinearLayoutManager(this);
        llmTerakhir.setOrientation(LinearLayoutManager.VERTICAL);
        cardListTerakhirSearch.setLayoutManager(llmTerakhir);

        LinearLayoutManager llmTop = new LinearLayoutManager(this);
        llmTop.setOrientation(LinearLayoutManager.VERTICAL);
        cardListTopSearch.setLayoutManager(llmTop);
        tvPencarian  = (TextView) findViewById(R.id.tvPencarian);
        tvTeratas = (TextView) findViewById(R.id.tvTeratas);
        linearSearchTerakhirProduk  = (LinearLayout) findViewById(R.id.linearSearchTerakhirProduk);
        tvHapusSemua = (TextView) findViewById(R.id.tvHapusSemua);

//        Toast.makeText(context, FilterActivity.min, Toast.LENGTH_SHORT).show();
        final LinearLayoutManager llm  = new GridLayoutManager(this,2);
        cardListProduk.setLayoutManager(llm);
        cardListProduk.addItemDecoration(new ProdukKategoriActivity.GridSpacingItemDecoration(1, dpToPx(1), true));
        cardListProduk.addOnScrollListener(new RecyclerView.OnScrollListener()
        {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                Animation muncul = AnimationUtils.loadAnimation(ProdukKategoriActivity.this, R.anim.translatefilter);
                Animation ilang = AnimationUtils.loadAnimation(ProdukKategoriActivity.this, R.anim.alphailanng);
                layoutSort.setAnimation(ilang);
                layoutSort.setVisibility(View.GONE);
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = llm.getChildCount();
                    totalItemCount = llm.getItemCount();
                    pastVisiblesItems = llm.findFirstVisibleItemPosition();

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (pastVisiblesItems + visibleItemCount)) {
                        // End has been reached

                        // Do something
                        pb2.setVisibility(View.VISIBLE);
                        if(FilterActivity.min == null) {
                            append();
                        }
                        else{
                            append2();
                        }
                        offset+=20;
                        //Toast.makeText(context, String.valueOf(offset), Toast.LENGTH_SHORT).show();
                        loading = true;
                    }
                    layoutSort.setAnimation(ilang);
                    layoutSort.setVisibility(View.GONE);
                }
                else if(dy<0){
                   //Animation anim = AnimationUtils.loadAnimation(ProdukKategoriActivity.this, R.anim.translate);
                    layoutSort.setAnimation(muncul);
                    layoutSort.setVisibility(View.VISIBLE);
                }
                else{
                   // Animation anim = AnimationUtils.loadAnimation(ProdukKategoriActivity.this, R.anim.translate);
                    layoutSort.setAnimation(ilang);
                    layoutSort.setVisibility(View.GONE);
                }
            }
        });


        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProdukKategoriActivity.this,FilterActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });


        cardListProduk.setItemAnimator(new DefaultItemAnimator());
        session = new SessionManagement(getApplicationContext());
        final HashMap<String, String> user = session.getUserDetails();
        queue = Volley.newRequestQueue(this);

        if(CONFIG.CekGuest.equals("ya")){
            if(cekFilter == null){
                String url1 = "http://fransis.rawatwajah.com/century/getProdukKategori.php?nama="+kategori.get(SessionKategori.KEY_KATEGORI).replace(" ","+")+"&id="+lokasi.get(SessionLokasi.KEY_ID_LOKASI);
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
                                        pb2.setVisibility(View.GONE);
                                        JSONObject obj = users.getJSONObject(i);
                                        nama = obj.getString("Nama_Produk");
                                        harga = obj.getInt("Harga");
                                        merk = obj.getString("Merk");
                                        gambar = obj.getString("Gambar");
                                        subkategori = obj.getString("Subkategori");
                                        id_kategori = obj.getString("Id_Kategori");
                                        id_promosi = obj.getString("Id_Promosi");
                                        id_produk = obj.getString("Id_Produk");
                                        stok = obj.getString("Stok");
                                        diskon = obj.getString("Diskon");
                                        id_produk_per_lokasi = obj.getString("Id");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    result.add(new Produk(nama,harga,merk,id_kategori,id_promosi,subkategori,gambar,id_produk,stok,diskon,id_produk_per_lokasi));
                                    pa = new produkAdapter(result);
                                    cardListProduk.setAdapter(pa);
                                    //cardListProduk.addOnScrollListener(scrollListener);
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
            ///////////////////////////////////////////////////////////////////////////////////FILTER AND SORT
            else{
                cekFilter=null;
                String url1 = "http://fransis.rawatwajah.com/century/filter.php?nama_kategori="+kategori.get(SessionKategori.KEY_KATEGORI).replace(" ","+")
                        +"&id_lokasi="+lokasi.get(SessionLokasi.KEY_ID_LOKASI)+"&min="+FilterActivity.min+"&max="+FilterActivity.max+"&jenis="+FilterActivity.jenis+"&order="+FilterActivity.order+"&by="+FilterActivity.by+"&merk="+FilterActivity.brand;
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
                                        pb2.setVisibility(View.GONE);
                                        JSONObject obj = users.getJSONObject(i);
                                        nama = obj.getString("Nama_Produk");
                                        harga = obj.getInt("Harga");
                                        merk = obj.getString("Merk");
                                        gambar = obj.getString("Gambar");
                                        subkategori = obj.getString("Subkategori");
                                        id_kategori = obj.getString("Id_Kategori");
                                        id_promosi = obj.getString("Id_Promosi");
                                        id_produk = obj.getString("Id_Produk");
                                        stok = obj.getString("Stok");
                                        diskon = obj.getString("Diskon");
                                        id_produk_per_lokasi = obj.getString("Id");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    //Toast.makeText(ProdukKategoriActivity.this, nama, Toast.LENGTH_SHORT).show();
                                    result.add(new Produk(nama,harga,merk,id_kategori,id_promosi,subkategori,gambar,id_produk,stok,diskon,id_produk_per_lokasi));
                                    pa = new produkAdapter(result);
                                    cardListProduk.setAdapter(pa);
                                    //cardListProduk.addOnScrollListener(scrollListener);
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
        else if(CONFIG.CekGuest.equals("tidak")){
            if(cekFilter == null){
                String url12 = "http://fransis.rawatwajah.com/century/selectLokasi.php?email="+user.get(SessionManagement.KEY_EMAIL);
                JsonObjectRequest req2 = new JsonObjectRequest(url12, null,
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
                                    String url1 = "http://fransis.rawatwajah.com/century/getProdukKategori.php?nama="+kategori.get(SessionKategori.KEY_KATEGORI).replace(" ","+")+"&id="+id_lokasi;
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
                                                            pb2.setVisibility(View.GONE);
                                                            JSONObject obj = users.getJSONObject(i);
                                                            nama = obj.getString("Nama_Produk");
                                                            harga = obj.getInt("Harga");
                                                            merk = obj.getString("Merk");
                                                            gambar = obj.getString("Gambar");
                                                            subkategori = obj.getString("Subkategori");
                                                            id_kategori = obj.getString("Id_Kategori");
                                                            id_promosi = obj.getString("Id_Promosi");
                                                            id_produk = obj.getString("Id_Produk");
                                                            stok = obj.getString("Stok");
                                                            diskon = obj.getString("Diskon");
                                                            id_produk_per_lokasi = obj.getString("Id");

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        result.add(new Produk(nama,harga,merk,id_kategori,id_promosi,subkategori,gambar,id_produk,stok,diskon,id_produk_per_lokasi));
                                                        pa = new produkAdapter(result);
                                                        cardListProduk.setAdapter(pa);
                                                        //cardListProduk.addOnScrollListener(scrollListener);
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
                queue.add(req2);

            }
            ///////////////////////////////////////////////////////////////////////////////////FILTER AND SORT
            else{
                cekFilter=null;
                Log.v("Hasil","Min: "+FilterActivity.min+"&max="+FilterActivity.max+"&jenis="+FilterActivity.jenis+"&order="+FilterActivity.order+"&by="+FilterActivity.by+"&merk="+FilterActivity.brand);
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
                                    String url1 = "http://fransis.rawatwajah.com/century/filter.php?nama_kategori="+kategori.get(SessionKategori.KEY_KATEGORI).replace(" ","+")
                                            +"&id_lokasi="+id_lokasi+"&min="+FilterActivity.min+"&max="+FilterActivity.max+"&jenis="+FilterActivity.jenis+"&order="+FilterActivity.order+"&by="+FilterActivity.by+"&merk="+FilterActivity.brand;
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
                                                            pb2.setVisibility(View.GONE);
                                                            JSONObject obj = users.getJSONObject(i);
                                                            nama = obj.getString("Nama_Produk");
                                                            harga = obj.getInt("Harga");
                                                            merk = obj.getString("Merk");
                                                            gambar = obj.getString("Gambar");
                                                            subkategori = obj.getString("Subkategori");
                                                            id_kategori = obj.getString("Id_Kategori");
                                                            id_promosi = obj.getString("Id_Promosi");
                                                            id_produk = obj.getString("Id_Produk");
                                                            stok = obj.getString("Stok");
                                                            diskon = obj.getString("Diskon");
                                                            id_produk_per_lokasi = obj.getString("Id");

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        //Toast.makeText(ProdukKategoriActivity.this, nama, Toast.LENGTH_SHORT).show();
                                                        result.add(new Produk(nama,harga,merk,id_kategori,id_promosi,subkategori,gambar,id_produk,stok,diskon,id_produk_per_lokasi));
                                                        pa = new produkAdapter(result);
                                                        cardListProduk.setAdapter(pa);
                                                        //cardListProduk.addOnScrollListener(scrollListener);
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

        }




        tvHapusSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HashMap<String, String> user = session.getUserDetails();
                JSONObject objAdd2 = new JSONObject();
                try {
                    JSONArray arrData = new JSONArray();
                    JSONObject objDetail = new JSONObject();
                    objDetail.put("email",user.get(SessionManagement.KEY_EMAIL));
                    arrData.put(objDetail);
                    objAdd2.put("data",arrData);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                JsonObjectRequest stringRequest2 = new JsonObjectRequest(Request.Method.POST, DELETE_SEMUA_SEARCH_URL, objAdd2,
                        new Response.Listener<JSONObject>() {
                            @Override

                            public void onResponse(JSONObject response) {
                                try {
                                    if(response.getString("status").equals("OK")) {
                                        linearSearchTerakhirProduk.setVisibility(View.GONE);
                                        cardListTerakhirSearch.setVisibility(View.GONE);
                                        tersa.notifyDataSetChanged();
                                    }
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) ;
                RequestQueue requestQueue2 = Volley.newRequestQueue(ProdukKategoriActivity.this);
                requestQueue2.add(stringRequest2);
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////////////////

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isDataConnectionAvailable(ProdukKategoriActivity.this) == false){
                    Toast.makeText(context, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
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


        ////////////////////////////////////
        //////////////////////set top search
        String url19 = "http://fransis.rawatwajah.com/century/getTopSearch.php";
        JsonObjectRequest req9 = new JsonObjectRequest(url19, null,
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
                                topSearch = obj.getString("Hasil_Search");
                                idSearch = obj.getString("Id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            resultTop.add(new Search(topSearch,idSearch));
                            topsa = new topSearchAdapter(context,resultTop);
                            cardListTopSearch.setAdapter(topsa);
                            tvTeratas.setVisibility(View.VISIBLE);
                            cardListTopSearch.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
            }
        });

        queue.add(req9);

        //////////////////////set terakhir search
        /////////////////////////////////////
        String url17 = "http://fransis.rawatwajah.com/century/getTerakhirSearch.php?email="+user.get(SessionManagement.KEY_EMAIL);
        JsonObjectRequest req7 = new JsonObjectRequest(url17, null,
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
                                cardListTerakhirSearch.setVisibility(View.VISIBLE);
                                JSONObject obj = users.getJSONObject(i);
                                terakhirSearch = obj.getString("Hasil_Search");
                                idSearch = obj.getString("Id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            resultTerakhir.add(new Search(terakhirSearch,idSearch));
                            tersa = new terakhirSearchAdapter(context,resultTerakhir);
                            cardListTerakhirSearch.setAdapter(tersa);
                            tvPencarian.setVisibility(View.VISIBLE);
                            cardListTerakhirSearch.setVisibility(View.VISIBLE);
                            tvHapusSemua.setVisibility(View.VISIBLE);
                            linearSearchTerakhirProduk.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
            }
        });

        queue.add(req7);

        //////////////////////////////////////



        tvKategoriTemp.setText("Cari di Kategori "+kategori.get(SessionKategori.KEY_KATEGORI));
        linearTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionSearch.createSearch(etSearch.getText().toString());
                Intent i = new Intent(ProdukKategoriActivity.this,SearchActivity.class);
                startActivity(i);
            }
        });


    }




    public void append(){
        //sessionKategori = new SessionKategori(getApplicationContext());
        final HashMap<String, String> kategori = sessionKategori.getKategoriDetails();
        HashMap<String, String> user = session.getUserDetails();
        final HashMap<String, String> lokasi = sessionLokasi.getLokasiDetails();


        if(CONFIG.CekGuest.equals("tidak")){
            String url12 = "http://fransis.rawatwajah.com/century/selectLokasi.php?email="+user.get(SessionManagement.KEY_EMAIL);
            JsonObjectRequest req2 = new JsonObjectRequest(url12, null,
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
                                String url1 = "http://fransis.rawatwajah.com/century/getAppendProdukKategori.php?nama="+kategori.get(SessionKategori.KEY_KATEGORI).replace(" ","+")+"&id="+id_lokasi+"&offset="+offset;
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
                                                if(users.length() == 0){
                                                    pb2.setVisibility(View.GONE);
                                                }
                                                for (int i = 0; i < users.length(); i++) {
                                                    try {

                                                        JSONObject obj = users.getJSONObject(i);
                                                        nama = obj.getString("Nama_Produk");
                                                        harga = obj.getInt("Harga");
                                                        merk = obj.getString("Merk");
                                                        gambar = obj.getString("Gambar");
                                                        subkategori = obj.getString("Subkategori");
                                                        id_kategori = obj.getString("Id_Kategori");
                                                        id_promosi = obj.getString("Id_Promosi");
                                                        id_produk = obj.getString("Id_Produk");
                                                        stok = obj.getString("Stok");
                                                        diskon = obj.getString("Diskon");
                                                        id_produk_per_lokasi = obj.getString("Id");

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    result.add(new Produk(nama,harga,merk,id_kategori,id_promosi,subkategori,gambar,id_produk,stok,diskon,id_produk_per_lokasi));
                                                    pa = new produkAdapter(result);
                                                    pa.notifyDataSetChanged();
                                                    cardListProduk.swapAdapter(pa,false);
                                                    pb2.setVisibility(View.GONE);

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
            queue.add(req2);
        }
        else if(CONFIG.CekGuest.equals("ya")){
            String url1 = "http://fransis.rawatwajah.com/century/getAppendProdukKategori.php?nama="+kategori.get(SessionKategori.KEY_KATEGORI).replace(" ","+")+"&id="+lokasi.get(SessionLokasi.KEY_ID_LOKASI)+"&offset="+offset;
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
                            if(users.length() == 0){
                                pb2.setVisibility(View.GONE);
                            }
                            for (int i = 0; i < users.length(); i++) {
                                try {

                                    JSONObject obj = users.getJSONObject(i);
                                    nama = obj.getString("Nama_Produk");
                                    harga = obj.getInt("Harga");
                                    merk = obj.getString("Merk");
                                    gambar = obj.getString("Gambar");
                                    subkategori = obj.getString("Subkategori");
                                    id_kategori = obj.getString("Id_Kategori");
                                    id_promosi = obj.getString("Id_Promosi");
                                    id_produk = obj.getString("Id_Produk");
                                    stok = obj.getString("Stok");
                                    diskon = obj.getString("Diskon");
                                    id_produk_per_lokasi = obj.getString("Id");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                result.add(new Produk(nama,harga,merk,id_kategori,id_promosi,subkategori,gambar,id_produk,stok,diskon,id_produk_per_lokasi));
                                pa = new produkAdapter(result);
                                pa.notifyDataSetChanged();
                                cardListProduk.swapAdapter(pa,false);
                                pb2.setVisibility(View.GONE);

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



    public void append2(){
        final HashMap<String, String> kategori = sessionKategori.getKategoriDetails();
        HashMap<String, String> user = session.getUserDetails();
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
                            if(users.length() == 0){
                                pb2.setVisibility(View.GONE);
                            }
                            else{
                                pb2.setVisibility(View.GONE);
                                for (int i = 0; i < users.length(); i++) {
                                    try {
                                        JSONObject obj = users.getJSONObject(i);
                                        id_lokasi = obj.getString("Id_Lokasi");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    String url1 = "http://fransis.rawatwajah.com/century/appendFilter.php?nama_kategori="+kategori.get(SessionKategori.KEY_KATEGORI).replace(" ","+")
                                            +"&id_lokasi="+id_lokasi+"&min="+FilterActivity.min+"&max="+FilterActivity.max+"&jenis="+FilterActivity.jenis+"&order="+FilterActivity.order+"&by="+FilterActivity.by+"&merk="+FilterActivity.brand+"&offset="+offset;
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
                                                            pb2.setVisibility(View.GONE);
                                                            JSONObject obj = users.getJSONObject(i);
                                                            nama = obj.getString("Nama_Produk");
                                                            harga = obj.getInt("Harga");
                                                            merk = obj.getString("Merk");
                                                            gambar = obj.getString("Gambar");
                                                            subkategori = obj.getString("Subkategori");
                                                            id_kategori = obj.getString("Id_Kategori");
                                                            id_promosi = obj.getString("Id_Promosi");
                                                            id_produk = obj.getString("Id_Produk");
                                                            stok = obj.getString("Stok");
                                                            diskon = obj.getString("Diskon");
                                                            id_produk_per_lokasi = obj.getString("Id");

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        result.add(new Produk(nama,harga,merk,id_kategori,id_promosi,subkategori,gambar,id_produk,stok,diskon,id_produk_per_lokasi));
                                                        pa = new produkAdapter(result);
                                                        pa.notifyDataSetChanged();
                                                        cardListProduk.swapAdapter(pa,false);
                                                        pb2.setVisibility(View.GONE);

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
            String url1 = "http://fransis.rawatwajah.com/century/appendFilter.php?nama_kategori="+kategori.get(SessionKategori.KEY_KATEGORI).replace(" ","+")
                    +"&id_lokasi="+lokasi.get(SessionLokasi.KEY_ID_LOKASI)+"&min="+FilterActivity.min+"&max="+FilterActivity.max+"&jenis="+FilterActivity.jenis+"&order="+FilterActivity.order+"&by="+FilterActivity.by+"&merk="+FilterActivity.brand+"&offset="+offset;
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
                                    pb2.setVisibility(View.GONE);
                                    JSONObject obj = users.getJSONObject(i);
                                    nama = obj.getString("Nama_Produk");
                                    harga = obj.getInt("Harga");
                                    merk = obj.getString("Merk");
                                    gambar = obj.getString("Gambar");
                                    subkategori = obj.getString("Subkategori");
                                    id_kategori = obj.getString("Id_Kategori");
                                    id_promosi = obj.getString("Id_Promosi");
                                    id_produk = obj.getString("Id_Produk");
                                    stok = obj.getString("Stok");
                                    diskon = obj.getString("Diskon");
                                    id_produk_per_lokasi = obj.getString("Id");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                result.add(new Produk(nama,harga,merk,id_kategori,id_promosi,subkategori,gambar,id_produk,stok,diskon,id_produk_per_lokasi));
                                pa = new produkAdapter(result);
                                pa.notifyDataSetChanged();
                                cardListProduk.swapAdapter(pa,false);
                                pb2.setVisibility(View.GONE);

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



    public void handleMenuSearch(){
        ActionBar action = getSupportActionBar();

        if(!isSearchOpened){
            scrollSearch.setVisibility(View.GONE);
            searchLinear.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
            action.setDisplayShowCustomEnabled(false);
            action.setDisplayShowTitleEnabled(true);

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            imm.hideSoftInputFromWindow(etSearch.getWindowToken(),0);
            /////icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search_white_24dp));
            //////
            isSearchOpened = true;
        }else{
            scrollSearch.setVisibility(View.VISIBLE);
            searchLinear.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.GONE);
            layoutSort.setVisibility(View.GONE);
            action.setDisplayShowCustomEnabled(true);
            action.setCustomView(R.layout.search_bar);

            action.setDisplayShowTitleEnabled(false);

            etSearch = (EditText) action.getCustomView().findViewById(R.id.etSearch);

            etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId == EditorInfo.IME_ACTION_SEARCH){
                        doSearch();
                        return true;
                    }
                    return false;
                }
            });
            etSearch.requestFocus();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etSearch,InputMethodManager.SHOW_IMPLICIT);
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.close));
            isSearchOpened=false;

            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (etSearch.getText().toString().equals("")){
                        linearTemp.setVisibility(View.GONE);
                    }
                    else{
                        linearTemp.setVisibility(View.VISIBLE);
                        tvSearchTemp.setText(etSearch.getText().toString());
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void doSearch(){
        scrollSearch.setVisibility(View.GONE);
        searchLinear.setVisibility(View.GONE);
        mainLinear.setVisibility(View.VISIBLE);
        final HashMap<String, String> user = session.getUserDetails();
        JSONObject objAdd2 = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("hasil_search",etSearch.getText().toString());
            objDetail.put("email",user.get(SessionManagement.KEY_EMAIL));
            arrData.put(objDetail);
            objAdd2.put("data",arrData);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JsonObjectRequest stringRequest2 = new JsonObjectRequest(Request.Method.POST, INSERT_SEARCH_URL, objAdd2,
                new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("status").equals("OK")) {

                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) ;
        RequestQueue requestQueue2 = Volley.newRequestQueue(ProdukKategoriActivity.this);
        requestQueue2.add(stringRequest2);

        sessionSearch.createSearch(etSearch.getText().toString());
        Intent i = new Intent(ProdukKategoriActivity.this,SearchActivity.class);
        i.putExtra("panduanSearch",etSearch.getText().toString());
        sessionKategori.createKategori("");
        startActivity(i);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main,menu);

        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        startActivity(getIntent());
//        finish();
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
                brandAdapter.tempBrand = null;
                FilterActivity.min = null;
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                handleMenuSearch();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
       if(!isSearchOpened){
           linearTemp.setVisibility(View.GONE);
           handleMenuSearch();
           View view = this.getCurrentFocus();
           InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
           imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
           return;
        }
        else {
            finish();
            brandAdapter.tempBrand = null;
            FilterActivity.min = null;
        }
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isDataConnectionAvailable(ProdukKategoriActivity.this) == false){
            //Toast.makeText(context, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
            mainLayout.setVisibility(View.GONE);
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
