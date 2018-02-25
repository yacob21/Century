package com.example.user.century.Home;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.user.century.BadgeDrawable;
import com.example.user.century.BantuanActivity;
import com.example.user.century.CONFIG;
import com.example.user.century.Cart.CartActivity;
import com.example.user.century.GuestLoginActivity.SessionLokasi;
import com.example.user.century.Member.MemberActivity;
import com.example.user.century.Pembelian.Pembayaran;
import com.example.user.century.Pembelian.Pembelian;
import com.example.user.century.Pembelian.PembelianActivity;
import com.example.user.century.Pembelian.listPembayaranAdapter;
import com.example.user.century.Pembelian.listPembelianAdapter;
import com.example.user.century.Pembelian.listPemesananAdapter;
import com.example.user.century.Profil.ProfilActivity;
import com.example.user.century.R;
import com.example.user.century.Register.PetaPertama;
import com.example.user.century.Search;
import com.example.user.century.SearchPackage.SearchActivity;
import com.example.user.century.Session.SessionCek;
import com.example.user.century.Session.SessionKategori;
import com.example.user.century.Session.SessionManagement;
import com.example.user.century.Session.SessionSearch;
import com.example.user.century.cekStock.CekStockActivity;
import com.example.user.century.slider.FirstSlider;
import com.example.user.century.terakhirSearchAdapter;
import com.example.user.century.topSearchAdapter;
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//import com.google.android.gms.iid.InstanceID;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
//import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView cardListHome,cardListRekomendasi,cardListTerakhir,cardListTopSearch,cardlistTop;
    public static  RecyclerView cardListTerakhirSearch;
    RequestQueue queue;
    SessionManagement session;
    TextView tvKategori,textProdukRekomendasi,textKategori,textTerakhir,textTop;
    Integer idKategori;
    String namaKategori;

    LinearLayout layoutTop;

    List<Kategori> result = new ArrayList<>();
    List<Rekomendasi> result2 = new ArrayList<>();
    List<Terakhir> result3= new ArrayList<>();
    List<Top> result4= new ArrayList<>();
    List<Search> resultTop = new ArrayList<>();
    List<Search> resultTerakhir = new ArrayList<>();

    ListHomeAdapter lha;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    String tempNama;
    String id;
    Context context;

    ProgressBar pb,pbKoneksi;
    Button btnRefresh;
    NestedScrollView mainLayout;
    LinearLayout koneksiLayout;
    ImageView iv1;
    TextView text2,text3;

    LinearLayout mainLinear,searchLinear;


    int count;

    //////////
    private Toolbar toolbar;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = true;
    static EditText etSearch;


    String terakhirSearch,topSearch,idSearch;

    //////////////
    terakhirSearchAdapter tersa;
    topSearchAdapter topsa;
    rekomendasiAdapter ra;
    terakhirAdapter ta;
    topAdapter toa;

    String nama,merk,gambar,subkategori;
    int harga;
    String id_kategori,id_promosi,id_produk,stok,diskon,id_produk_per_lokasi;

    String nama2,merk2,gambar2,subkategori2;
    int harga2;
    String id_kategori2,id_promosi2,id_produk2,stok2,diskon2,id_produk_per_lokasi2;

    String nama3,merk3,gambar3,subkategori3;
    int harga3;
    String id_kategori3,id_promosi3,id_produk3,stok3,diskon3,id_produk_per_lokasi3;


    ///////////////////////////////////////
    ViewPager viewPager;
    viewpagerAdapter vpa;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    /////////////////////////////////////
    TextView tvPencarian,tvTeratas,tvHapusSemua;
    public static LinearLayout linearSearchTerakhirMain;

    SessionKategori sessionKategori;
    public static final String INSERT_SEARCH_URL = "http://fransis.rawatwajah.com/century/insertSearch.php";
    public static final String INSERT_TOKEN_URL = "http://fransis.rawatwajah.com/century/registerToken.php";
    public static final String DELETE_SEMUA_SEARCH_URL = "http://fransis.rawatwajah.com/century/deleteSemuaPencarian.php";
    private FirebaseAuth mAuth;
    SessionSearch sessionSearch;
    SessionLokasi sessionLokasi;
    SessionCek sessionCek;

    TextView pemesanan,pembelian,pembayaran;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Search Disini...</font>"));
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleMenuSearch();
            }
        });
        context = this;
        sessionLokasi  = new SessionLokasi(getApplicationContext());
        sessionKategori = new SessionKategori(getApplicationContext());
        session = new SessionManagement(getApplicationContext());

        queue = Volley.newRequestQueue(this);
        CONFIG.CekGuest="tidak";

        if(CONFIG.CekBelanja.equals("ya")){
            CONFIG.CekBelanja.equals("tidak");
            Intent i = new Intent(MainActivity.this,CartActivity.class);
            startActivity(i);
        }



        final HashMap<String, String> user = session.getUserDetails();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        String urlCounter = "http://fransis.rawatwajah.com/century/selectCounter.php?email="+user.get(SessionManagement.KEY_EMAIL);
        JsonObjectRequest reqCounter  = new JsonObjectRequest(urlCounter , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray users = null;
                        try {
                            users = response.getJSONArray("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONObject obj = users.getJSONObject(0);
                            getSupportActionBar().setHomeAsUpIndicator(setBadgeCount(MainActivity.this,R.drawable.ic_burger,obj.getInt("Counter")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(reqCounter);

        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        pemesanan = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.navPemesanan));
        pembayaran = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.navPembayaran));
        pembelian = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.navDaftarPembelian));

        mAuth = FirebaseAuth.getInstance();
        builder = new AlertDialog.Builder(this);
        tvKategori = (TextView) findViewById(R.id.tvKategori);
        pb = (ProgressBar) findViewById(R.id.pb);
        pbKoneksi = (ProgressBar) findViewById(R.id.pbKoneksi);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        koneksiLayout = (LinearLayout) findViewById(R.id.koneksiLayout);
        mainLayout = (NestedScrollView) findViewById(R.id.mainLayout);
        mainLinear = (LinearLayout) findViewById(R.id.mainLinear);
        searchLinear = (LinearLayout) findViewById(R.id.searchLinear);
        cardListHome = (RecyclerView) findViewById(R.id.cardListKategori);
        iv1 = (ImageView) findViewById(R.id.iv1);
        text2 = (TextView) findViewById(R.id.text2);
        tvPencarian  = (TextView) findViewById(R.id.tvPencarian);
        tvTeratas = (TextView) findViewById(R.id.tvTeratas);
        text3 = (TextView) findViewById(R.id.text3);
        cardListHome.setHasFixedSize(true);
        textTop = (TextView) findViewById(R.id.textTop);
        textKategori = (TextView) findViewById(R.id.textKategori);
        textTerakhir = (TextView) findViewById(R.id.textProdukTerakhir);
        tvHapusSemua = (TextView) findViewById(R.id.tvHapusSemua);
        layoutTop = (LinearLayout) findViewById(R.id.layoutTop);
        linearSearchTerakhirMain = (LinearLayout) findViewById(R.id.linearSearchTerakhir);
        textProdukRekomendasi  = (TextView) findViewById(R.id.textProdukRekomendasi);

        cardListRekomendasi = (RecyclerView) findViewById(R.id.cardListRekomendasi);
        cardListRekomendasi.setHasFixedSize(true);

        cardListTerakhir = (RecyclerView) findViewById(R.id.cardListTerakhir);
        cardListTerakhir.setHasFixedSize(true);

        cardListTerakhirSearch = (RecyclerView) findViewById(R.id.cardListTerakhirSearch);
        cardListTerakhirSearch.setHasFixedSize(true);

        cardListTopSearch = (RecyclerView) findViewById(R.id.cardListTopSearch);
        cardListTopSearch.setHasFixedSize(true);

        cardlistTop = (RecyclerView) findViewById(R.id.cardListTop);
        cardlistTop.setHasFixedSize(true);

        LinearLayoutManager llmTerakhir = new LinearLayoutManager(this);
        llmTerakhir.setOrientation(LinearLayoutManager.VERTICAL);
        cardListTerakhirSearch.setLayoutManager(llmTerakhir);

        LinearLayoutManager llmTop = new LinearLayoutManager(this);
        llmTop.setOrientation(LinearLayoutManager.VERTICAL);
        cardListTopSearch.setLayoutManager(llmTop);
        sessionSearch = new SessionSearch(getApplicationContext());

        RecyclerView.LayoutManager llm2 = new GridLayoutManager(this,3,LinearLayoutManager.VERTICAL,false);
        cardListHome.setLayoutManager(llm2);
        cardListHome.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1), false));

        RecyclerView.LayoutManager llm = new GridLayoutManager(this,1,LinearLayoutManager.HORIZONTAL,false);
        cardListRekomendasi.setLayoutManager(llm);
        cardListRekomendasi.addItemDecoration(new GridSpacingItemDecoration(5, dpToPx(5), true));

        RecyclerView.LayoutManager llm3 = new GridLayoutManager(this,1,LinearLayoutManager.HORIZONTAL,false);
        cardListTerakhir.setLayoutManager(llm3);
        cardListTerakhir.addItemDecoration(new GridSpacingItemDecoration(5, dpToPx(5), true));

        RecyclerView.LayoutManager llm4 = new GridLayoutManager(this,1,LinearLayoutManager.HORIZONTAL,false);
        cardlistTop.setLayoutManager(llm4);
        cardlistTop.addItemDecoration(new GridSpacingItemDecoration(5, dpToPx(5), true));


        cardListHome.setItemAnimator(new DefaultItemAnimator());
        cardListRekomendasi.setItemAnimator(new DefaultItemAnimator());
        cardListTerakhir.setItemAnimator(new DefaultItemAnimator());
        cardlistTop.setItemAnimator(new DefaultItemAnimator());

        sessionCek = new SessionCek(getApplicationContext());
        sessionCek.createCek("login");


        sessionLokasi.createLokasi("");
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

            /////////////////////////////////////////////////////////////////////////////////////////////////
            ///////////IMAGE CAROUSEL
            ///////////////////////////////////

            String url12 = "http://fransis.rawatwajah.com/century/getPromosi.php";
            JsonObjectRequest req2 = new JsonObjectRequest(url12, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray users = null;
                            try {
                                users = response.getJSONArray("result1");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String[] image = new String[users.length()];

                            for (int i = 0; i < users.length(); i++) {
                                try {
                                    JSONObject obj = users.getJSONObject(i);
                                    //url[i]= obj.getString("Gambar_Promosi");
                                    image[i] = obj.getString("Gambar_Promosi");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                vpa = new viewpagerAdapter(MainActivity.this,image);
                                viewPager.setAdapter(vpa);
                            }
                            dotscount = vpa.getCount();
                            dots = new ImageView[dotscount];

                            for(int i = 0; i < dotscount; i++){

                                dots[i] = new ImageView(context);
                                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                params.setMargins(8, 0, 8, 0);

                                sliderDotspanel.addView(dots[i], params);

                            }

                            dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                }
            });

            queue.add(req2);



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);
        /////////////////////////////////////////////////////////////////////////////////////
        mulai();
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isDataConnectionAvailable(MainActivity.this) == false){
                    Toast.makeText(context, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
                    mainLayout.setVisibility(View.GONE);
                    koneksiLayout.setVisibility(View.VISIBLE);
                }else{
                    overridePendingTransition( 0, 0);
                    startActivity(getIntent());
                    overridePendingTransition( 0, 0);
                }

            }
        });


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
//                                        finish();
//                                        startActivity(getIntent());
                                        linearSearchTerakhirMain.setVisibility(View.GONE);
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
                RequestQueue requestQueue2 = Volley.newRequestQueue(MainActivity.this);
                requestQueue2.add(stringRequest2);
            }
        });

//
//        OneSignal.startInit(this)
//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .unsubscribeWhenNotificationsAreDisabled(true)
//                .init();

        initializeCountDrawer();


    }



    public void mulai (){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeaderView = navigationView.getHeaderView(0);
        final TextView navNama = (TextView) navHeaderView.findViewById(R.id.navNama);
        final ImageView navPicture = (ImageView) navHeaderView.findViewById(R.id.navPicture);
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        final String email = user.get(SessionManagement.KEY_EMAIL);
        String url1 = "http://fransis.rawatwajah.com/century/iddetaillokasi.php?email="+email;
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
                                if(obj.getInt("Id_Lokasi") == 0 ){
                                    builder.setMessage("Harap pilih lokasi Apotek Century yang Anda inginkan untuk proses pengambilan barang");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent v = new Intent(MainActivity.this,PetaPertama.class);
                                            startActivity(v);
                                        }
                                    });
                                    dialog = builder.show();
                                }

                                id = String.valueOf(obj.getInt("Id_Lokasi"));
                                navNama.setText(obj.getString("Nama"));
                                String inisial = obj.getString("Nama").substring(0,1).toLowerCase();
                                Resources res = getResources();
                                int resID = res.getIdentifier(inisial , "drawable", getPackageName());
                                Drawable drawable = res.getDrawable(resID );
                                navPicture.setImageDrawable(drawable);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        String url12 = "http://fransis.rawatwajah.com/century/detaillokasi.php?id="+id;
                        JsonObjectRequest req2 = new JsonObjectRequest(url12, null,
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
                                            pb.setVisibility(View.GONE);
                                            textKategori.setVisibility(View.VISIBLE);
                                            cardListHome.setVisibility(View.VISIBLE);
                                            try {
                                                JSONObject obj = users.getJSONObject(i);
                                                idKategori = (Integer) obj.get("Id_Kategori");
                                                namaKategori = (String) obj.get("Nama_Kategori");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            result.add(new Kategori(idKategori,namaKategori));
                                            lha = new ListHomeAdapter(result);
                                            cardListHome.setAdapter(lha);
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                            }
                        });

                        queue.add(req2);

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        String urlrekomendasi = "http://fransis.rawatwajah.com/century/getProdukRekomendasi.php?email="+email+"&id="+id;
                        JsonObjectRequest reqrekomendasi = new JsonObjectRequest(urlrekomendasi, null,
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
                                            textProdukRekomendasi.setVisibility(View.VISIBLE);
                                            cardListRekomendasi.setVisibility(View.VISIBLE);
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
                                            result2.add(new Rekomendasi(nama,harga,merk,id_kategori,id_promosi,subkategori,gambar,id_produk,stok,diskon,id_produk_per_lokasi));
                                            ra = new rekomendasiAdapter(result2);
                                            cardListRekomendasi.setAdapter(ra);
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                            }
                        });

                        queue.add(reqrekomendasi);

                        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
                        String urlprodukterakhir = "http://fransis.rawatwajah.com/century/getProdukTerakhir.php?email="+email+"&id="+id;
                        JsonObjectRequest reqprodukterakhir = new JsonObjectRequest(urlprodukterakhir, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        JSONArray users = null;
                                        try {
                                            users = response.getJSONArray("result1");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        if (users.length() == 0) {

                                        } else {
                                            for (int i = 0; i < users.length(); i++) {
                                                textTerakhir.setVisibility(View.VISIBLE);
                                                cardListTerakhir.setVisibility(View.VISIBLE);
                                                try {
                                                    JSONObject obj2 = users.getJSONObject(i);
                                                    nama2 = obj2.getString("Nama_Produk");
                                                    harga2 = obj2.getInt("Harga");
                                                    merk2 = obj2.getString("Merk");
                                                    gambar2 = obj2.getString("Gambar");
                                                    subkategori2 = obj2.getString("Subkategori");
                                                    id_kategori2 = obj2.getString("Id_Kategori");
                                                    id_promosi2 = obj2.getString("Id_Promosi");
                                                    id_produk2 = obj2.getString("Id_Produk");
                                                    stok2 = obj2.getString("Stok");
                                                    diskon2 = obj2.getString("Diskon");
                                                    id_produk_per_lokasi2 = obj2.getString("Id");

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                // Toast.makeText(MainActivity.this, nama+" "+harga2+" "+merk2+" "+id_kategori2+" "+id_promosi2+" "+subkategori2+" "+gambar2+" "+id_produk2+" "+stok2+" "+diskon2, Toast.LENGTH_SHORT).show();
                                                result3.add(new Terakhir(nama2, harga2, merk2, id_kategori2, id_promosi2, subkategori2, gambar2, id_produk2, stok2, diskon2,id_produk_per_lokasi2));
                                                ta = new terakhirAdapter(result3);
                                                cardListTerakhir.setAdapter(ta);

                                            }

                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Toast.makeText(MainActivity.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                            }
                        });

                        queue.add(reqprodukterakhir);
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        String urlTop = "http://fransis.rawatwajah.com/century/getProdukTop.php?id="+id;
                        JsonObjectRequest reqTop = new JsonObjectRequest(urlTop, null,
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
                                            layoutTop.setVisibility(View.VISIBLE);
                                            textTop.setVisibility(View.VISIBLE);
                                            cardlistTop.setVisibility(View.VISIBLE);
                                            try {
                                                JSONObject obj = users.getJSONObject(i);
                                                nama3 = obj.getString("Nama_Produk");
                                                harga3 = obj.getInt("Harga");
                                                merk3 = obj.getString("Merk");
                                                gambar3 = obj.getString("Gambar");
                                                subkategori3 = obj.getString("Subkategori");
                                                id_kategori3 = obj.getString("Id_Kategori");
                                                id_promosi3 = obj.getString("Id_Promosi");
                                                id_produk3 = obj.getString("Id_Produk");
                                                stok3 = obj.getString("Stok");
                                                diskon3 = obj.getString("Diskon");
                                                id_produk_per_lokasi3 = obj.getString("Id_Produk_Per_Lokasi");

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            result4.add(new Top(nama3,harga3,merk3,id_kategori3,id_promosi3,subkategori3,gambar3,id_produk3,stok,diskon3,id_produk_per_lokasi3));
                                            toa = new topAdapter(result4);
                                            cardlistTop.setAdapter(toa);
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                            }
                        });

                        queue.add(reqTop);
                        ///////////////////////////////////////////////////////////////////////////////
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
            }
        });

        queue.add(req);



        ///set top search
        String urltopsearch = "http://fransis.rawatwajah.com/century/getTopSearch.php";
        JsonObjectRequest reqtopsearch = new JsonObjectRequest(urltopsearch, null,
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

        queue.add(reqtopsearch);

        //////////////////////set terakhir search
        String urlterakhirsearch = "http://fransis.rawatwajah.com/century/getTerakhirSearch.php?email="+user.get(SessionManagement.KEY_EMAIL);
        JsonObjectRequest reqterakhirsearch = new JsonObjectRequest(urlterakhirsearch, null,
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
                            tvPencarian.setVisibility(View.GONE);
                            cardListTerakhirSearch.setVisibility(View.GONE);
                        }
                        for (int i = 0; i < users.length(); i++) {
                            try {

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
                            tvHapusSemua.setVisibility(View.VISIBLE);
                            linearSearchTerakhirMain.setVisibility(View.VISIBLE);
                            cardListTerakhirSearch.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
            }
        });

        queue.add(reqterakhirsearch);
    }




    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

        @Override
        protected void onStart() {
            super.onStart();

            final HashMap<String, String> user = session.getUserDetails();
            JSONObject objAdd2 = new JSONObject();
            try {
                JSONArray arrData = new JSONArray();
                JSONObject objDetail = new JSONObject();
                objDetail.put("token",FirebaseInstanceId.getInstance().getToken());
                objDetail.put("email",user.get(SessionManagement.KEY_EMAIL));
                arrData.put(objDetail);
                objAdd2.put("data",arrData);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            JsonObjectRequest stringRequest2 = new JsonObjectRequest(Request.Method.POST, INSERT_TOKEN_URL, objAdd2,
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
            RequestQueue requestQueue2 = Volley.newRequestQueue(MainActivity.this);
            requestQueue2.add(stringRequest2);








            session.checkLogin();
            if(isDataConnectionAvailable(MainActivity.this) == false){
                //Toast.makeText(context, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
                mainLayout.setVisibility(View.GONE);
                koneksiLayout.setVisibility(View.VISIBLE);
            }

            //Toast.makeText(context, user.get(SessionManagement.KEY_EMAIL), Toast.LENGTH_SHORT).show();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
            if(!previouslyStarted) {
                sessionCek  = new SessionCek(getApplicationContext());
                sessionCek.createCek("login");
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
                edit.commit();
                //Pindah ke pertama
                Intent i = new Intent(MainActivity.this,FirstSlider.class);
                startActivity(i);
            }



        }


    public void handleMenuSearch(){
        ActionBar action = getSupportActionBar();

        if(!isSearchOpened){
            searchLinear.setVisibility(View.GONE);
            mainLinear.setVisibility(View.VISIBLE);
            View view = this.getCurrentFocus();

            action.setDisplayShowCustomEnabled(false);
            action.setDisplayShowTitleEnabled(true);

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            imm.hideSoftInputFromWindow(etSearch.getWindowToken(),0);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            /////icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search_white_24dp));
            isSearchOpened = true;
        }else{
            searchLinear.setVisibility(View.VISIBLE);
            mainLinear.setVisibility(View.GONE);
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
        }
    }

    private void doSearch() {
        sessionKategori.createKategori("");
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(MainActivity.this);
        requestQueue2.add(stringRequest2);


        



        if (etSearch.getText().toString().equals("")) {
            Toast.makeText(context, "Pencarian Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        } else {
            sessionSearch.createSearch(etSearch.getText().toString());
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            i.putExtra("panduanSearch", etSearch.getText().toString());
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        if(!isSearchOpened){
            handleMenuSearch();
            View view = this.getCurrentFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return;
        }
        else {
            builder.setMessage("Ingin keluar dari aplikasi ?");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                    return;
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            dialog = builder.show();
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        HashMap<String, String> user = session.getUserDetails();
        getMenuInflater().inflate(R.menu.menu_main,menu);
        getMenuInflater().inflate(R.menu.menu_cart,menu);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        String url13 = "http://fransis.rawatwajah.com/century/selectCountCart.php?email="+user.get(SessionManagement.KEY_EMAIL);
        JsonObjectRequest req3 = new JsonObjectRequest(url13, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray users = null;
                        try {
                            users = response.getJSONArray("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        count = users.length();
                        //Toast.makeText(context, String.valueOf(count), Toast.LENGTH_SHORT).show();
                        menuItem.setIcon(buildCounterDrawable(count, R.drawable.ic_shopping_cart_white_24dp));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(MainActivity.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
            }
        });

        queue.add(req3);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                handleMenuSearch();
                return true;
            case R.id.action_cart:
                Intent i = new Intent(MainActivity.this,CartActivity.class);
                startActivityForResult(i,1);
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        NavigationView nv= (NavigationView) findViewById(R.id.nav_view);
        Menu m = nv.getMenu();
        int id = item.getItemId();

        if (id == R.id.navHome) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.navPembelian) {
            boolean a=!m.findItem(R.id.navDaftarPembelian).isVisible();
            m.findItem(R.id.navDaftarPembelian).setVisible(a);
            boolean b=!m.findItem(R.id.navPembayaran).isVisible();
            m.findItem(R.id.navPembayaran).setVisible(b);
            boolean c=!m.findItem(R.id.navPemesanan).isVisible();
            m.findItem(R.id.navPemesanan).setVisible(c);
        }
        else if(id == R.id.navDaftarPembelian){
            Intent i = new Intent(MainActivity.this,PembelianActivity.class);
            i.putExtra("FRAGMENT_ID", "2");
            startActivity(i);
        }
        else if(id == R.id.navPemesanan){
            Intent i = new Intent(MainActivity.this,PembelianActivity.class);
            i.putExtra("FRAGMENT_ID", "1");
            startActivity(i);
        }
        else if(id == R.id.navPembayaran){
            Intent i = new Intent(MainActivity.this,PembelianActivity.class);
            i.putExtra("FRAGMENT_ID", "0");
            startActivity(i);
        }
        else if(id == R.id.navCek) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            Intent i = new Intent(MainActivity.this, CekStockActivity.class);
            startActivity(i);
        }
        else if(id==R.id.navMember){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            Intent i = new Intent(MainActivity.this,MemberActivity.class);
            startActivity(i);
        }
        else if (id == R.id.navProfil) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            Intent i = new Intent(MainActivity.this,ProfilActivity.class);
            startActivity(i);
        } else if (id == R.id.navHubungi) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            builder.setMessage("Ingin menghubungi Customer Service?");

            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:(021)1234567"));
                    startActivity(intent);
                }
            });

            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            dialog = builder.show();
        } else if (id == R.id.navBantuan) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            Intent i = new Intent (MainActivity.this,BantuanActivity.class);

            startActivity(i);
        } else if (id == R.id.navKeluar) {
            session.logoutUser();
            CONFIG.CekGuest="ya";
            sessionCek.createCek("guest");
//            mAuth.signOut();
        }


        return true;
    }


    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.counter_menu_item_layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }



    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(viewPager.getCurrentItem() == 0){
                        viewPager.setCurrentItem(1);
                    } else if(viewPager.getCurrentItem() == 1){
                        viewPager.setCurrentItem(2);
                    }
                    else if(viewPager.getCurrentItem() == 2){
                        viewPager.setCurrentItem(3);
                    }
                    else if(viewPager.getCurrentItem() == 3){
                        viewPager.setCurrentItem(4);
                    }
                        else if(viewPager.getCurrentItem() == 4){
                        viewPager.setCurrentItem(0);
                    }
                    else {
                        viewPager.setCurrentItem(0);
                    }

                }
            });

        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        startActivity(getIntent());
        finish();
    }

    public static boolean isDataConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }

        return false;
    }

    private void initializeCountDrawer(){
        HashMap<String, String> user = session.getUserDetails();
        pembayaran.setGravity(Gravity.CENTER_VERTICAL);
        pembayaran.setTypeface(null, Typeface.BOLD);
        pembayaran.setTextColor(Color.parseColor("#ff0000"));
        String urlPembayaran = "http://fransis.rawatwajah.com/century/selectPembayaran.php?email="+user.get(SessionManagement.KEY_EMAIL);
        JsonObjectRequest reqPembayaran = new JsonObjectRequest(urlPembayaran, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray users = null;
                        try {
                            users = response.getJSONArray("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pembayaran.setText(String.valueOf(users.length()));
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
            }
        });
        queue.add(reqPembayaran);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        pembelian.setGravity(Gravity.CENTER_VERTICAL);
//        pembelian.setTypeface(null,Typeface.BOLD);
//        pembelian.setTextColor(Color.parseColor("#ff0000"));
//        String urlPembelian = "http://fransis.rawatwajah.com/century/selectPembelian.php?email="+user.get(SessionManagement.KEY_EMAIL);
//        JsonObjectRequest reqPembelian = new JsonObjectRequest(urlPembelian, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        JSONArray users = null;
//                        try {
//                            users = response.getJSONArray("result");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        pembelian.setText(String.valueOf(users.length()));
//                    }
//
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
//            }
//        });
//        queue.add(reqPembelian);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        pemesanan.setGravity(Gravity.CENTER_VERTICAL);
        pemesanan.setTypeface(null,Typeface.BOLD);
        pemesanan.setTextColor(Color.parseColor("#ff0000"));
        String urlPemesanan = "http://fransis.rawatwajah.com/century/selectPemesanan.php?email="+user.get(SessionManagement.KEY_EMAIL);
        JsonObjectRequest reqPemesanan  = new JsonObjectRequest(urlPemesanan , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray users = null;
                        try {
                            users = response.getJSONArray("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pemesanan.setText(String.valueOf(users.length()));
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(reqPemesanan );

    }

    private Drawable setBadgeCount(Context context, int res, int badgeCount){
        LayerDrawable icon = (LayerDrawable) ContextCompat.getDrawable(context, R.drawable.ic_badge_drawable);
        Drawable mainIcon = ContextCompat.getDrawable(context, res);
        BadgeDrawable badge = new BadgeDrawable(context);
        badge.setCount(String.valueOf(badgeCount));
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
        icon.setDrawableByLayerId(R.id.ic_main_icon, mainIcon);
        return icon;
    }





}


