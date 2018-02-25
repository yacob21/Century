package com.example.user.century.cekStock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.century.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailCekActivity extends AppCompatActivity {
    TextView tvNama,tvDeskripsi;
    ImageView ivProduk;
    RequestQueue queue;

    RecyclerView cardListDetailCek;
    List<DetailCek> result = new ArrayList<>();
    detailCekAdapter dca;

    AlertDialog dialog;
    AlertDialog.Builder builder;
    String nama,merk,gambar,subkategori,alamat;
    int harga;
    String id_kategori,id_promosi,id_produk,stok,diskon;

    TextView textView11;
    ProgressBar pbKoneksi,pb;
    Button btnRefresh;
    LinearLayout mainLayout;
    LinearLayout koneksiLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_cek);

        Intent x = getIntent();
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Cek Stok</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        builder = new AlertDialog.Builder(this);
        tvNama = (TextView) findViewById(R.id.tvNama);
        tvDeskripsi = (TextView) findViewById(R.id.tvDeskripsi);
        ivProduk = (ImageView) findViewById(R.id.ivProduk);
        queue = Volley.newRequestQueue(this);
        cardListDetailCek = (RecyclerView) findViewById(R.id.cardListDetailCek);
        cardListDetailCek.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        cardListDetailCek.setLayoutManager(llm);

        textView11 = (TextView) findViewById(R.id.textView11);
        pb = (ProgressBar) findViewById(R.id.pb);
        pbKoneksi = (ProgressBar) findViewById(R.id.pbKoneksi);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        koneksiLayout = (LinearLayout) findViewById(R.id.koneksiLayout);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);



        String url1 = "http://fransis.rawatwajah.com/century/selectProduk.php?id_produk="+x.getStringExtra("panduanIdProduk");
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
                                    textView11.setVisibility(View.VISIBLE);
                                    mainLayout.setVisibility(View.VISIBLE);
                                    cardListDetailCek.setVisibility(View.VISIBLE);
                                    JSONObject obj = users.getJSONObject(i);
                                    tvNama.setText(obj.getString("Nama_Produk"));
                                    tvDeskripsi.setText(obj.getString("Deskripsi"));
                                    Picasso.with(DetailCekActivity.this)
                                            .load(obj.getString("Gambar"))
                                            .config(Bitmap.Config.RGB_565)
                                            .placeholder(R.drawable.defaultpromosi)
                                            .error(R.drawable.defaultpromosi)
                                            .fit()
                                            .centerInside()
                                            .into(ivProduk);
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

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        String url12 = "http://fransis.rawatwajah.com/century/selectLokasiBerdasarkanProduk.php?id_produk="+x.getStringExtra("panduanIdProduk");
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
                        if(users.length() == 0){
                            builder.setMessage("Stok Produk Sedang Kosong");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            dialog = builder.show();
                        }
                        for (int i = 0; i < users.length(); i++) {
                            try {
                                JSONObject obj = users.getJSONObject(i);
                                nama = obj.getString("Nama_Lokasi");
                                alamat = obj.getString("Alamat_Lokasi");
                                harga= obj.getInt("Harga");
                                diskon = obj.getString("Diskon");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            result.add(new DetailCek(nama,alamat,harga,diskon));
                            dca = new detailCekAdapter(result);
                            cardListDetailCek.setAdapter(dca);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
            }
        });
        queue.add(req2);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isDataConnectionAvailable(DetailCekActivity.this) == false){
                    Toast.makeText(DetailCekActivity.this, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
                    mainLayout.setVisibility(View.GONE);
                    textView11.setVisibility(View.GONE);
                    mainLayout.setVisibility(View.GONE);
                    cardListDetailCek.setVisibility(View.GONE);
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
        if(isDataConnectionAvailable(DetailCekActivity.this) == false){
            //Toast.makeText(context, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
            mainLayout.setVisibility(View.GONE);
            textView11.setVisibility(View.GONE);
            mainLayout.setVisibility(View.GONE);
            cardListDetailCek.setVisibility(View.GONE);
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



}
