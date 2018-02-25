package com.example.user.century.Pembelian;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.century.Cart.CartActivity;
import com.example.user.century.Home.MainActivity;
import com.example.user.century.Pembayaran.DetailPembayaran;
import com.example.user.century.Pembayaran.DetailPembayaranAdapter;
import com.example.user.century.R;
import com.example.user.century.Session.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailStatusPembayaran extends AppCompatActivity {
    TextView tvHarga,tvNamaLokasi,tvAlamatLokasi,tvBatas,tvCoundDown,tvDetailPembayaran,tvHargaUnik,tvSalinBca,tvSalinMandiri,tvSalinBri,tvRekBca,tvRekMandiri,tvRekBri,tvSalinJumlah,tvWarning;
    RequestQueue queue;
    SessionManagement session;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    //////////////////
    LinearLayout linearBca,linearMandiri,linearBri,linearDetailBca,linearDetailMandiri,linearDetailBri,linearBawahBca,linearBawahMandiri,linearBawahBri;

    String tempBca="",tempBri="",tempMandiri="";

    ////////////////////////////////////
    private CountDownTimer countDownTimer;
    long timeLeft;
    ///////////////////////////////////
    //Detail Pembayaran
    RecyclerView cardListDetailPembayaran;
    List<DetailPembayaran> result = new ArrayList<>();
    DetailPembayaranAdapter dpa;
    int harga,totalharga,kodeunik;

    RelativeLayout detailLayout;
    ScrollView mainLayout;
    TableLayout linearsub;
    TableRow tbSubTotal,tbUnik,tbDiskon,tbTotal;
    TextView tvSubTotal,tvUnik,tvDiskon,tvTotal;
    String cekMember="";


    ProgressBar pb;
    Button btnRefresh;
    LinearLayout koneksiLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_status_pembayaran);
        getSupportActionBar().setTitle(Html.fromHtml("<'font color='#ffffff'>Status Pembayaran </font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        queue = Volley.newRequestQueue(this);
        tvHarga = (TextView) findViewById(R.id.tvHarga);
        tvHargaUnik = (TextView) findViewById(R.id.tvHargaUnik);
        tvBatas = (TextView) findViewById(R.id.tvBatas);
        tvSalinJumlah = (TextView) findViewById(R.id.tvSalinJumlah);
        tvNamaLokasi = (TextView) findViewById(R.id.tvNamaLokasi);
        tvAlamatLokasi = (TextView) findViewById(R.id.tvAlamatLokasi);
        tvSalinJumlah.setPaintFlags(tvSalinJumlah.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tvRekBca = (TextView) findViewById(R.id.tvRekBca);
        tvRekBri = (TextView) findViewById(R.id.tvRekBri);
        tvRekMandiri = (TextView) findViewById(R.id.tvRekMandiri);
        tvWarning = (TextView) findViewById(R.id.tvWarning);
        tvWarning.setText("Transfer tepat sampai 3 digit terakhir. Perbedaan\njumlah pembayaran akan menghambat proses\nverifikasi");

        tvSalinBca = (TextView) findViewById(R.id.tvSalinBca);
        tvSalinBca.setPaintFlags(tvSalinBca.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvSalinBri = (TextView) findViewById(R.id.tvSalinBri);
        tvSalinBri.setPaintFlags(tvSalinBri.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvSalinMandiri = (TextView) findViewById(R.id.tvSalinMandiri);
        tvSalinMandiri.setPaintFlags(tvSalinMandiri.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tvDetailPembayaran = (TextView) findViewById(R.id.tvDetailPembayaran);
        tvDetailPembayaran.setPaintFlags(tvDetailPembayaran.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvCoundDown = (TextView) findViewById(R.id.tvCountDown);
        linearBca = (LinearLayout) findViewById(R.id.linearBca);
        linearMandiri = (LinearLayout) findViewById(R.id.linearMandiri);
        linearBri = (LinearLayout) findViewById(R.id.linearBri);
        linearDetailBca = (LinearLayout) findViewById(R.id.linearDetailBca);
        linearDetailMandiri = (LinearLayout) findViewById(R.id.linearDetailMandiri);
        linearDetailBri = (LinearLayout) findViewById(R.id.linearDetailBri);
        linearBawahBca = (LinearLayout) findViewById(R.id.linearBawahBca);
        linearBawahMandiri = (LinearLayout) findViewById(R.id.linearBawahMandiri);
        linearBawahBri = (LinearLayout) findViewById(R.id.linearBawahBri);

//        startTimer();
        session = new SessionManagement(getApplicationContext());
        final HashMap<String, String> user = session.getUserDetails();

        detailLayout = (RelativeLayout) findViewById(R.id.detailLayout);
        mainLayout = (ScrollView) findViewById(R.id.mainLayout);
        koneksiLayout = (LinearLayout) findViewById(R.id.koneksiLayout);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);


        linearsub = (TableLayout) findViewById(R.id.linearsub);
        tbSubTotal = (TableRow) findViewById(R.id.tbSubTotal);
        tbDiskon = (TableRow) findViewById(R.id.tbDiskon);
        tbTotal = (TableRow) findViewById(R.id.tbTotal);
        tbUnik = (TableRow) findViewById(R.id.tbUnik);


        tvSubTotal = (TextView) findViewById(R.id.tvSubtotal);
        tvUnik = (TextView) findViewById(R.id.tvUnik);
        tvDiskon = (TextView) findViewById(R.id.tvDiskon);
        tvTotal = (TextView) findViewById(R.id.tvTotal);

        cardListDetailPembayaran = (RecyclerView) findViewById(R.id.cardListDetailPembayaran);
        cardListDetailPembayaran.setHasFixedSize(true);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(this);
        cardListDetailPembayaran.setLayoutManager(llm);
        linearBca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tempBca.equals("")){
                    tempBca="a";
                    linearDetailBca.setVisibility(View.VISIBLE);
                    linearBawahBca.setVisibility(View.VISIBLE);
                }
                else{
                    tempBca="";
                    linearDetailBca.setVisibility(View.GONE);
                    linearBawahBca.setVisibility(View.GONE);
                }

            }
        });

        linearMandiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tempMandiri.equals("")){
                    tempMandiri="a";
                    linearDetailMandiri.setVisibility(View.VISIBLE);
                    linearBawahMandiri.setVisibility(View.VISIBLE);
                }
                else{
                    tempMandiri="";
                    linearDetailMandiri.setVisibility(View.GONE);
                    linearBawahMandiri.setVisibility(View.GONE);
                }

            }
        });

        linearBri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tempBri.equals("")){
                    tempBri="a";
                    linearDetailBri.setVisibility(View.VISIBLE);
                    linearBawahBri.setVisibility(View.VISIBLE);
                }
                else{
                    tempBri="";
                    linearDetailBri.setVisibility(View.GONE);
                    linearBawahBri.setVisibility(View.GONE);
                }

            }
        });
        final Intent z = getIntent();
        String urlPembayaran = "http://fransis.rawatwajah.com/century/selectPembayaranByKodeTransaksi.php?kode="+z.getStringExtra("panduanBarcode");
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
                        for (int i = 0; i < users.length(); i++) {
                            try {
                                JSONObject obj = users.getJSONObject(i);

                                final String s = (String.format("%,d", Integer.valueOf(obj.getString("Total_Harga"))+Integer.valueOf(obj.getString("Kode_Unik")))).replace(',', '.');
                                tvHarga.setText("Rp "+s.replace(s.substring(s.length()-3),""));
                                tvHargaUnik.setText(s.substring(s.length()-3));
                                tvBatas.setText(obj.getString("Batas_Waktu"));
                                timeLeft = obj.getLong("Time_Left");
                                tvNamaLokasi.setText(obj.getString("Nama_Lokasi"));
                                tvAlamatLokasi.setText(obj.getString("Alamat_Lokasi"));
                                pb.setVisibility(View.GONE);
                                mainLayout.setVisibility(View.VISIBLE);
                                countDownTimer = new CountDownTimer(timeLeft,1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            timeLeft=millisUntilFinished;
                                            updateTimer();
                                        }

                                        @Override
                                        public void onFinish() {

                                        }
                                }.start();

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
        queue.add(reqPembayaran);



        tvSalinJumlah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailStatusPembayaran.this, "Berhasil Tersalin", Toast.LENGTH_SHORT).show();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label",tvHarga.getText().toString().replace("Rp ","").replace(".","")+tvHargaUnik.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        });




        String urlDetail = "http://fransis.rawatwajah.com/century/selectDetailPembayaran.php?kode="+z.getStringExtra("panduanBarcode");
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
                                cardListDetailPembayaran.setAdapter(dpa);
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

        tvDetailPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailLayout.setVisibility(View.VISIBLE);
                mainLayout.setVisibility(View.GONE);
            }
        });

        tvSalinMandiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailStatusPembayaran.this, "Berhasil Tersalin", Toast.LENGTH_SHORT).show();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", tvRekMandiri.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        });

        tvSalinBca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailStatusPembayaran.this, "Berhasil Tersalin", Toast.LENGTH_SHORT).show();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", tvRekBca.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        });


        tvSalinBri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailStatusPembayaran.this, "Berhasil Tersalin", Toast.LENGTH_SHORT).show();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", tvRekBri.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isDataConnectionAvailable(DetailStatusPembayaran.this) == false){
                    Toast.makeText(DetailStatusPembayaran.this, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
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

    public void updateTimer(){
        int hour = (int) (timeLeft/3600000);
        int minutes = (int) (timeLeft % 3600000 /60000);
        int seconds = (int) (timeLeft % 60000 /1000);
        String stringTimeleft ="";
        if(hour<10) stringTimeleft +="0";
        stringTimeleft = stringTimeleft + hour +"     :      ";
        if(minutes<10) stringTimeleft +="0";
        stringTimeleft = stringTimeleft + minutes;
        stringTimeleft += "      :      ";
        if(seconds<10) stringTimeleft +="0";
        stringTimeleft += seconds;
        tvCoundDown.setText(stringTimeleft);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isDataConnectionAvailable(DetailStatusPembayaran.this) == false){
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
