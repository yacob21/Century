package com.example.user.century.cekStock;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.example.user.century.R;
import com.example.user.century.Search;
import com.example.user.century.Session.SessionManagement;
import com.example.user.century.Session.SessionSearch;
import com.example.user.century.produkKategori.Produk;
import com.example.user.century.terakhirSearchAdapter;
import com.example.user.century.topSearchAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CekStockActivity extends AppCompatActivity {
    Toolbar toolbar;
    String tempSearch=null;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = true;
    EditText etSearch;
    TextView text;
    RequestQueue queue;
    Context context;
    RecyclerView cardListCek,cardListTopSearch;
    public static RecyclerView cardListTerakhirSearch;
    List<Produk> result = new ArrayList<>();
    cekAdapter ca;
    String nama,merk,gambar,subkategori;
    int harga;
    String id_kategori,id_promosi,id_produk,stok,diskon,id_produk_per_lokasi;

//    String[] kata = new String[5];
//    String[] spasi;
    AlertDialog dialog;
    SessionManagement session;
    AlertDialog.Builder builder;
    public static String cek=null;
    static ProgressBar pb ;
    ProgressBar pb2;
    Button btnRefresh;
    FrameLayout mainLayout;
    LinearLayout koneksiLayout;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int offset =20;
    private int previousTotal = 0;


    LinearLayout mainLinear,searchLinear;
    TextView tvPencarian,tvTeratas,tvHapusSemua;
    public static LinearLayout linearSearchTerakhirCek;

    List<Search> resultTop = new ArrayList<>();
    List<Search> resultTerakhir = new ArrayList<>();
    String terakhirSearch,topSearch,idSearch;
    terakhirSearchAdapter tersa;
    topSearchAdapter topsa;

    public static final String INSERT_SEARCH_URL = "http://fransis.rawatwajah.com/century/insertSearch.php";
    public static final String DELETE_SEMUA_SEARCH_URL = "http://fransis.rawatwajah.com/century/deleteSemuaPencarian.php";
//    public static final String UPDATE_SEARCH_URL = "http://fransis.rawatwajah.com/century/updatePencarian.php";

    SessionSearch sessionSearch;
    NestedScrollView scrollSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek_stock);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleMenuSearch();
            }
        });
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Cek Stock Barang...</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        queue = Volley.newRequestQueue(this);
        context = this;
        mainLinear = (LinearLayout) findViewById(R.id.mainLinear);
        searchLinear = (LinearLayout) findViewById(R.id.searchLinear);

        text  = (TextView) findViewById(R.id.text);
        pb = (ProgressBar) findViewById(R.id.pb);
        pb2 = (ProgressBar) findViewById(R.id.pb2);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        koneksiLayout = (LinearLayout) findViewById(R.id.koneksiLayout);
        mainLayout = (FrameLayout) findViewById(R.id.mainLayout);
        cardListCek = (RecyclerView) findViewById(R.id.cardListCek);
        scrollSearch = (NestedScrollView) findViewById(R.id.scrollSearch);
        cardListCek.setHasFixedSize(true);
        final LinearLayoutManager llm  = new GridLayoutManager(this,2);
        cardListCek.setLayoutManager(llm);
        cardListCek.addItemDecoration(new CekStockActivity.GridSpacingItemDecoration(1, dpToPx(1), true));

        cardListTerakhirSearch = (RecyclerView) findViewById(R.id.cardListTerakhirSearch);
        cardListTerakhirSearch.setHasFixedSize(true);

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
        linearSearchTerakhirCek  = (LinearLayout) findViewById(R.id.linearSearchTerakhirCek);
        tvHapusSemua = (TextView) findViewById(R.id.tvHapusSemua);
        sessionSearch = new SessionSearch(getApplicationContext());
        cardListCek.addOnScrollListener(new RecyclerView.OnScrollListener()
        {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {

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
                        append();
                        offset+=20;
                        //Toast.makeText(context, String.valueOf(offset), Toast.LENGTH_SHORT).show();
                        loading = true;
                    }
                }
            }
        });


        cardListCek.setItemAnimator(new DefaultItemAnimator());



        if(cek == null){
           // Toast.makeText(context, "a" + cek, Toast.LENGTH_SHORT).show();

        }
        else {
            cek=null;
            Intent i = getIntent();
            tempSearch  = i.getStringExtra("panduanSearch");
            String url1 = "http://fransis.rawatwajah.com/century/selectCek.php?search=\"*"+tempSearch.replace(" ","*\" or \"*")+"*\"";
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
                            if (users.length() == 0) {
                                text.setVisibility(View.VISIBLE);
                                pb .setVisibility(View.GONE);
                            } else {
                                for (int i = 0; i < users.length(); i++) {
                                    try {
                                        pb .setVisibility(View.GONE);
                                        JSONObject obj = users.getJSONObject(i);
                                        nama = obj.getString("Nama_Produk");
                                        merk = obj.getString("Merk");
                                        gambar = obj.getString("Gambar");
                                        subkategori = obj.getString("Subkategori");
                                        id_kategori = obj.getString("Id_Kategori");
                                        id_produk = obj.getString("Id_Produk");
                                        id_produk_per_lokasi = obj.getString("Id");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    //Toast.makeText(CekStockActivity.this, nama, Toast.LENGTH_SHORT).show();
                                    result.add(new Produk(nama, merk, id_kategori,subkategori, gambar, id_produk,id_produk_per_lokasi));
                                    ca = new cekAdapter(result);
                                    cardListCek.setAdapter(ca);
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
                                        linearSearchTerakhirCek.setVisibility(View.GONE);
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
                RequestQueue requestQueue2 = Volley.newRequestQueue(CekStockActivity.this);
                requestQueue2.add(stringRequest2);
            }
        });



        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isDataConnectionAvailable(CekStockActivity.this) == false){
                    Toast.makeText(context, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
                    mainLayout.setVisibility(View.GONE);
                    koneksiLayout.setVisibility(View.VISIBLE);
                }else{
                    pb.setVisibility(View.VISIBLE);
                    overridePendingTransition( 0, 0);
                    startActivity(getIntent());
                    finish();
                    overridePendingTransition( 0, 0);
                }

            }
        });


        //////////////////////////////////
        ////////////////////set top search
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

        ////////////////////set terakhir search
        ///////////////////////////////////
        session = new SessionManagement(getApplicationContext());
        final HashMap<String, String> user = session.getUserDetails();

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
                            linearSearchTerakhirCek.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
            }
        });

        queue.add(req7);

        ////////////////////////////////////

    }

    public void handleMenuSearch(){
        ActionBar action = getSupportActionBar();

        if(!isSearchOpened){

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

    private void doSearch(){
        scrollSearch.setVisibility(View.GONE);
        searchLinear.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(CekStockActivity.this);
        requestQueue2.add(stringRequest2);



        if(etSearch.getText().toString().equals("")){
            Toast.makeText(context, "Pencarian Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        }
        else {
            pb.setVisibility(View.VISIBLE);
            cek = "lala";
            Intent i = new Intent(this, CekStockActivity.class);
            sessionSearch.createSearch(etSearch.getText().toString());
            i.putExtra("panduanSearch", etSearch.getText().toString());
            startActivity(i);
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                finish();
//                Intent i = new Intent(CekStockActivity.this,MainActivity.class);
//                startActivity(i);
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
            handleMenuSearch();
            View view = this.getCurrentFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return;
        }
        else {
            finish();
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

        if(isDataConnectionAvailable(CekStockActivity.this) == false){
            //Toast.makeText(context, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
            mainLayout.setVisibility(View.GONE);
            koneksiLayout.setVisibility(View.VISIBLE);
        }

    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        startActivity(getIntent());
//        finish();
//    }


    public static boolean isDataConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }



    public void append(){
        String url1 = "http://fransis.rawatwajah.com/century/selectAppendCek.php?search=\"*"+tempSearch.replace(" ","*\" or \"*")+"*\""+"&offset="+offset;
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
                        if(users.length() == 0){
                            pb2.setVisibility(View.GONE);
                        }
                        for (int i = 0; i < users.length(); i++) {
                            try {
                                pb2 .setVisibility(View.GONE);
                                JSONObject obj = users.getJSONObject(i);
                                nama = obj.getString("Nama_Produk");
                                merk = obj.getString("Merk");
                                gambar = obj.getString("Gambar");
                                subkategori = obj.getString("Subkategori");
                                id_kategori = obj.getString("Id_Kategori");
                                id_produk = obj.getString("Id_Produk");
                                id_produk_per_lokasi = obj.getString("Id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            result.add(new Produk(nama, merk, id_kategori,subkategori, gambar, id_produk,id_produk_per_lokasi));
                            ca = new cekAdapter(result);
                            ca.notifyDataSetChanged();
                            cardListCek.swapAdapter(ca,false);

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
