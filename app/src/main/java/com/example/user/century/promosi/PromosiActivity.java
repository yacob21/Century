package com.example.user.century.promosi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.example.user.century.produkKategori.Produk;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PromosiActivity extends AppCompatActivity {

    RecyclerView cardListPromosi;
    List<Produk> result = new ArrayList<>();
    String nama,merk,gambar,subkategori;
    int harga;
    String id_kategori,id_promosi,id_produk,stok,diskon,id_produk_per_lokasi;
    promosiAdapter pa;
    ImageView ivPromosi;
    AlertDialog dialog;
    SessionManagement session;
    AlertDialog.Builder builder;
    RequestQueue queue;
    Context context;
    ProgressBar pb;
    ProgressBar pbKoneksi;
    Button btnRefresh;
    FrameLayout mainLayout;
    LinearLayout koneksiLayout;
    SessionLokasi sessionLokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promosi);
        getSupportActionBar().setTitle(Html.fromHtml("<'font color='#ffffff'>Promosi</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        cardListPromosi = (RecyclerView) findViewById(R.id.cardListPromosi);
        cardListPromosi.setHasFixedSize(true);
        ivPromosi = (ImageView) findViewById(R.id.ivPromosi);
        builder = new AlertDialog.Builder(this);
        RecyclerView.LayoutManager llm = new GridLayoutManager(this,2);
        cardListPromosi.setLayoutManager(llm);
        queue = Volley.newRequestQueue(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        double wi=(double)width/(double)dm.xdpi;
        double hi=(double)height/(double)dm.ydpi;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        double screenInches = Math.sqrt(x+y);
        final Intent z = getIntent();
        pb = (ProgressBar) findViewById(R.id.pb);
        Picasso.with(PromosiActivity.this)
                .load(z.getStringExtra("panduanImage"))
                .placeholder(R.drawable.defaultpromosi)
                .error(R.drawable.defaultpromosi)
                .into(ivPromosi);

        pbKoneksi = (ProgressBar) findViewById(R.id.pbKoneksi);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        koneksiLayout = (LinearLayout) findViewById(R.id.koneksiLayout);
        mainLayout = (FrameLayout) findViewById(R.id.mainLayout);

        cardListPromosi.addItemDecoration(new PromosiActivity.GridSpacingItemDecoration(1, dpToPx(1), true));
        cardListPromosi.setItemAnimator(new DefaultItemAnimator());

        sessionLokasi = new SessionLokasi(getApplicationContext());
        HashMap<String, String> lokasi = sessionLokasi.getLokasiDetails();

        if(CONFIG.CekGuest.equals("tidak")) {
            String url1 = "http://fransis.rawatwajah.com/century/selectCustomer.php?email=" + user.get(SessionManagement.KEY_EMAIL);
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
                                    //Toast.makeText(PromosiActivity.this, obj.getString("Id_Lokasi"), Toast.LENGTH_SHORT).show();
                                    String url1 = "http://fransis.rawatwajah.com/century/selectpromosi.php?image=" + z.getStringExtra("panduanImage") + "&id_lokasi=" + obj.getString("Id_Lokasi");
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
                                                        builder.setMessage("Promosi Tidak Tersedia di Lokasi Ini");
                                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                finish();
                                                            }
                                                        });
                                                        dialog = builder.show();
                                                        pb.setVisibility(View.GONE);
                                                    } else {
                                                        for (int i = 0; i < users.length(); i++) {
                                                            try {
                                                                ivPromosi.setVisibility(View.VISIBLE);
                                                                pb.setVisibility(View.GONE);
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
                                                            result.add(new Produk(nama, harga, merk, id_kategori, id_promosi, subkategori, gambar, id_produk, stok, diskon, id_produk_per_lokasi));
                                                            pa = new promosiAdapter(result);
                                                            cardListPromosi.setAdapter(pa);
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
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            queue.add(req);
        }
        else if(CONFIG.CekGuest.equals("ya")){
            String url1 = "http://fransis.rawatwajah.com/century/selectpromosi.php?image="+z.getStringExtra("panduanImage")+"&id_lokasi="+lokasi.get(SessionLokasi.KEY_ID_LOKASI);
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
                            if(users.length()==0){
                                builder.setMessage("Promosi Tidak Tersedia di Lokasi Ini");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                                dialog = builder.show();
                                pb.setVisibility(View.GONE);
                            }
                            else {
                                for (int i = 0; i < users.length(); i++) {
                                    try {
                                        ivPromosi.setVisibility(View.VISIBLE);
                                        pb.setVisibility(View.GONE);
                                        JSONObject obj = users.getJSONObject(i);
                                        nama = obj.getString("Nama_Produk");
                                        harga = obj.getInt("Harga");
                                        merk = obj.getString("Merk");
                                        gambar = obj.getString("Gambar");
                                        subkategori = obj.getString("Subkategori");
                                        id_kategori = obj.getString("Id_Kategori");
                                        id_promosi = obj.getString("Id_Promosi");
                                        id_produk = obj.getString("Id_Produk");
                                        stok  = obj.getString("Stok");
                                        diskon = obj.getString("Diskon");
                                        id_produk_per_lokasi = obj.getString("Id");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    result.add(new Produk(nama, harga, merk, id_kategori, id_promosi, subkategori, gambar, id_produk,stok,diskon,id_produk_per_lokasi));
                                    pa = new promosiAdapter(result);
                                    cardListPromosi.setAdapter(pa);
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

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isDataConnectionAvailable(PromosiActivity.this) == false){
                    Toast.makeText(context, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
                    mainLayout.setVisibility(View.GONE);
                    ivPromosi.setVisibility(View.GONE);
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
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        if(isDataConnectionAvailable(PromosiActivity.this) == false){
            //Toast.makeText(context, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
            mainLayout.setVisibility(View.GONE);
            ivPromosi.setVisibility(View.GONE);
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
        finish();
    }
}



/*

 */