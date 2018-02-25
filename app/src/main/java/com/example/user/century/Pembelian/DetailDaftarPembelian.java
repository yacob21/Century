package com.example.user.century.Pembelian;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.century.Pembayaran.DetailPembayaran;
import com.example.user.century.Pembayaran.DetailPembayaranAdapter;
import com.example.user.century.R;
import com.example.user.century.Session.SessionManagement;
import com.example.user.century.model.Code128;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailDaftarPembelian extends AppCompatActivity {
    TextView tvHarga,tvNamaLokasi,tvAlamatLokasi,tvDetailPembelian,tvStatus,tvWarning,tvKode;
    ImageView ivBarcode;
    RequestQueue queue;
    SessionManagement session;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    //////////////////

    //Detail Pembayaran
    RecyclerView cardListDetailPembelian;
    List<DetailPembayaran> result = new ArrayList<>();
    DetailPembayaranAdapter dpa;
    int harga,totalharga,kodeunik;

    RelativeLayout detailLayout;
    ScrollView mainLayout;
    TableRow tbSubTotal,tbUnik,tbDiskon,tbTotal;
    TextView tvSubTotal,tvUnik,tvDiskon,tvTotal;
    String cekMember="";

    ProgressBar pb;
    Button btnRefresh;
    LinearLayout koneksiLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_daftar_pembelian);
        getSupportActionBar().setTitle(Html.fromHtml("<'font color='#ffffff'>Daftar Pembelian</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        queue = Volley.newRequestQueue(this);
        tvHarga = (TextView) findViewById(R.id.tvHarga);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvWarning = (TextView) findViewById(R.id.tvWarning);
        tvNamaLokasi = (TextView) findViewById(R.id.tvNamaLokasi);
        tvAlamatLokasi = (TextView) findViewById(R.id.tvAlamatLokasi);
        tvKode = (TextView) findViewById(R.id.tvKode);
        tvDetailPembelian = (TextView) findViewById(R.id.tvDetailPembelian);
        tvDetailPembelian.setPaintFlags( tvDetailPembelian.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        session = new SessionManagement(getApplicationContext());


        detailLayout = (RelativeLayout) findViewById(R.id.detailLayout);
        mainLayout = (ScrollView) findViewById(R.id.mainLayout);
        koneksiLayout = (LinearLayout) findViewById(R.id.koneksiLayout);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);


        tbSubTotal = (TableRow) findViewById(R.id.tbSubTotal);
        tbDiskon = (TableRow) findViewById(R.id.tbDiskon);
        tbTotal = (TableRow) findViewById(R.id.tbTotal);
        tbUnik = (TableRow) findViewById(R.id.tbUnik);


        tvSubTotal = (TextView) findViewById(R.id.tvSubtotal);
        tvUnik = (TextView) findViewById(R.id.tvUnik);
        tvDiskon = (TextView) findViewById(R.id.tvDiskon);
        tvTotal = (TextView) findViewById(R.id.tvTotal);

        cardListDetailPembelian = (RecyclerView) findViewById(R.id.cardListDetailPembelian);
        cardListDetailPembelian.setHasFixedSize(true);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(this);
        cardListDetailPembelian.setLayoutManager(llm);

        tvDetailPembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailLayout.setVisibility(View.VISIBLE);
                mainLayout.setVisibility(View.GONE);
            }
        });

        final Intent z = getIntent();
        String urlPemesanan= "http://fransis.rawatwajah.com/century/selectPembelianByKodeTransaksi.php?kode="+z.getStringExtra("panduanBarcode");
        JsonObjectRequest reqPemesanan = new JsonObjectRequest(urlPemesanan, null,
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
                                final String s = (String.format("%,d", Integer.valueOf(obj.getString("Total_Harga"))+Integer.valueOf(obj.getString("Kode_Unik")))).replace(',', '.');
                                tvHarga.setText("Rp "+s);
                                tvNamaLokasi.setText(obj.getString("Nama_Lokasi"));
                                tvAlamatLokasi.setText(obj.getString("Alamat_Lokasi"));
                                tvStatus.setText("Transaksi "+obj.getString("Status"));
                                if(obj.getString("Status").equals("Terlambat Diambil")){
                                    tvStatus.setTextColor(Color.parseColor("#ff0000"));
                                    tvWarning.setText("Barang Anda terlambat diambil harap hubungi pihak kami untuk proses lebih lanjut");
                                    tvWarning.setVisibility(View.VISIBLE);
                                }
                                else if(obj.getString("Status").equals("Dibatalkan")){
                                    tvStatus.setTextColor(Color.parseColor("#808080"));
                                    ivBarcode.setVisibility(View.GONE);
                                    tvKode.setVisibility(View.GONE);
                                }
                                else{
                                    tvStatus.setTextColor(Color.parseColor("#008000"));
                                    ivBarcode.setVisibility(View.GONE);
                                    tvKode.setVisibility(View.GONE);
                                }
                                pb.setVisibility(View.GONE);
                                mainLayout.setVisibility(View.VISIBLE);
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
        queue.add(reqPemesanan);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isDataConnectionAvailable(DetailDaftarPembelian.this) == false){
                    Toast.makeText(DetailDaftarPembelian.this, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
                    detailLayout.setVisibility(View.GONE);
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

        drawBarcode();
        String urlDetail = "http://fransis.rawatwajah.com/century/selectDetailPembelian.php?kode="+z.getStringExtra("panduanBarcode");
        JsonObjectRequest reqDetail = new JsonObjectRequest(urlDetail, null,
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
                                if (obj.getString("Diskon_Member").equals("Y")){
                                    cekMember ="Y";
                                }
                                harga = obj.getInt("Harga");
                                result.add(new DetailPembayaran(obj.getString("Nama_Produk"),harga*Integer.valueOf(obj.getString("Jumlah")),obj.getString("Jumlah"),obj.getString("Gambar")));
                                dpa = new DetailPembayaranAdapter(result);
                                cardListDetailPembelian.setAdapter(dpa);
                                totalharga += harga*Integer.valueOf(obj.getString("Jumlah"));
                                kodeunik = obj.getInt("Kode_Unik");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(cekMember.equals("Y")){

                            String s = (String.format("%,d", totalharga)).replace(',', '.');
                            tvSubTotal.setText(s);

                            String u = (String.format("%,d",kodeunik)).replace(',', '.');
                            tvUnik.setText(u);

                            int diskonz = (int) (totalharga * Float.valueOf(5) / 100);
                            String diskonzzz = (String.format("%,d", diskonz)).replace(',', '.');
                            tvDiskon.setText(diskonzzz);

                            String t = (String.format("%,d", totalharga-(totalharga*5/100)+kodeunik).replace(',', '.'));
                            tvTotal.setText(t);
                        }
                        else{
                            tbDiskon.setVisibility(View.GONE);
                            String s = (String.format("%,d", totalharga)).replace(',', '.');
                            tvSubTotal.setText(s);

                            String u = (String.format("%,d",kodeunik)).replace(',', '.');
                            tvUnik.setText(u);

                            String t = (String.format("%,d", totalharga+kodeunik).replace(',', '.'));
                            tvTotal.setText(t);
                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
            }
        });
        queue.add(reqDetail);
    }

    private void drawBarcode(){
        Intent i = getIntent();
        String barcode = i.getStringExtra("panduanBarcode");
        Code128 code = new Code128(this);
        code.setData(barcode);
        Bitmap bitmap = code.getBitmap(1000,300);
        ivBarcode = (ImageView) findViewById(R.id.ivBarcode);
        ivBarcode.setImageBitmap(bitmap);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                if(detailLayout.getVisibility() == View.GONE) {
                    finish();
                }
                else if(detailLayout.getVisibility() == View.VISIBLE){
                    detailLayout.setVisibility(View.GONE);
                    mainLayout.setVisibility(View.VISIBLE);
                }
                return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if(detailLayout.getVisibility() == View.GONE) {
            finish();
        }
        else if(detailLayout.getVisibility() == View.VISIBLE){
            detailLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(isDataConnectionAvailable(DetailDaftarPembelian.this) == false){
            detailLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.GONE);
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

}
