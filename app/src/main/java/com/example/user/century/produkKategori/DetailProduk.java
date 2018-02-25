package com.example.user.century.produkKategori;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.example.user.century.CONFIG;
import com.example.user.century.Cart.CartActivity;
import com.example.user.century.DatabaseHandler;
import com.example.user.century.GuestLoginActivity.SessionLokasi;
import com.example.user.century.ProdukTemp;
import com.example.user.century.R;
import com.example.user.century.Session.SessionCek;
import com.example.user.century.Session.SessionManagement;
import com.example.user.century.Session.SessionProduk;
import com.example.user.century.Session.SessionSearch;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailProduk extends AppCompatActivity {
    ImageView ivBack,ivProduk,ivKurang,ivTambah,ivCart,ivRed;
    TextView tvNamaProduk,tvHargaProduk,tvDeskripsiProduk,tvHargaDiskon;
    EditText etQtyProduk;
    Button btnAddCart;
    RequestQueue queue;
    int price;
    ImageView ivKosong;
    String stok;
//    View garis;

    SessionProduk sessionProduk;
    SessionSearch sessionSearch;
    LinearLayout layoutDetail;
    FrameLayout layoutButton;
    int qty;
    int countGuest=0;
    int tempQty;
    ProgressBar pb;
    /////////////////////////////////////////////////
    public static final String UPDATE_CART_URL = "http://fransis.rawatwajah.com/century/updateQuantityProdukSama.php";
    public static final String INSERT_CART_URL = "http://fransis.rawatwajah.com/century/insertCart.php";
    public static final String INSERT_RIWAYAT_URL = "http://fransis.rawatwajah.com/century/insertRiwayat.php";
    public static final String UPDATE_RIWAYAT_URL = "http://fransis.rawatwajah.com/century/updateRiwayat.php";
    public static final String UPDATE_RIWAYAT_GA_BELI_URL = "http://fransis.rawatwajah.com/century/updateRiwayatGaBeli.php";

    SessionManagement session;
    AlertDialog dialog;
    AlertDialog.Builder builder;
//    String pengecekanku;
    int tempCek=0;
    String tempId;
    String tempLokasi [] = new String[1];
    ArrayList<String> temp = new ArrayList<>();
/////////////////////////////////
ProgressBar pbKoneksi;
    Button btnRefresh;
    FrameLayout mainLayout;
    LinearLayout koneksiLayout;
    SessionLokasi sessionLokasi;
    SessionCek sessionCek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        session = new SessionManagement(getApplicationContext());
        sessionProduk = new SessionProduk(getApplicationContext());
        sessionSearch = new SessionSearch(getApplicationContext());
        sessionLokasi = new SessionLokasi(getApplicationContext());
        sessionCek = new SessionCek(getApplicationContext());

        builder = new AlertDialog.Builder(this);
        queue = Volley.newRequestQueue(this);
        pbKoneksi = (ProgressBar) findViewById(R.id.pbKoneksi);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        koneksiLayout = (LinearLayout) findViewById(R.id.koneksiLayout);
        mainLayout = (FrameLayout) findViewById(R.id.mainLayout);

        ivRed  = (ImageView) findViewById(R.id.ivRed);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        ivCart = (ImageView) findViewById(R.id.ivCart);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivProduk = (ImageView) findViewById(R.id.ivProduk);
        ivKurang = (ImageView) findViewById(R.id.ivKurang);
        ivKosong = (ImageView) findViewById(R.id.ivKosong);
        ivTambah = (ImageView) findViewById(R.id.ivTambah);
        tvNamaProduk = (TextView) findViewById(R.id.tvNamaProduk);
        tvHargaProduk = (TextView) findViewById(R.id.tvHargaProduk);
        tvDeskripsiProduk = (TextView) findViewById(R.id.tvDeskripsiProduk);
        etQtyProduk = (EditText) findViewById(R.id.etQtyProduk);
        btnAddCart = (Button) findViewById(R.id.btnAddCart);
        tvHargaDiskon = (TextView) findViewById(R.id.tvHargaDiskon);
        layoutButton  = (FrameLayout) findViewById(R.id.layoutButton);
        layoutDetail = (LinearLayout) findViewById(R.id.layoutDetail);

        final HashMap<String, String> user = session.getUserDetails();
        final HashMap<String, String> produk = sessionProduk.getProdukDetails();
        final HashMap<String, String> lokasi = sessionLokasi.getLokasiDetails();
        final HashMap<String, String> cek = sessionCek.getCekDetails();

        tempId = produk.get(sessionProduk.KEY_ID);
        //Toast.makeText(this, tempId, Toast.LENGTH_SHORT).show();
        
        Intent riwayat = getIntent();
        JSONObject objAdd2 = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("id_produk_per_lokasi",tempId);
            objDetail.put("hasil_search",riwayat.getStringExtra("riwayatsearch"));
            objDetail.put("email",user.get(SessionManagement.KEY_EMAIL));
            arrData.put(objDetail);
            objAdd2.put("data",arrData);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JsonObjectRequest stringRequest2 = new JsonObjectRequest(Request.Method.POST, INSERT_RIWAYAT_URL, objAdd2,
                new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("status").equals("OK")) {

                            }
                            else{

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(DetailProduk.this);
        requestQueue2.add(stringRequest2);


        final DatabaseHandler db = new DatabaseHandler(this);
        final List<ProdukTemp> produkTemp = db.getAllProdukTemp();
        //Toast.makeText(this, String.valueOf(produkTemp.size()), Toast.LENGTH_SHORT).show();
        if(produkTemp.size() == 0 && cek.get(SessionCek.KEY_CEK).equals("guest")){
            ivRed.setVisibility(View.INVISIBLE);
        }
        else if(produkTemp.size() > 0  && cek.get(SessionCek.KEY_CEK).equals("guest")){
            ivRed.setVisibility(View.VISIBLE);
        }


        //////////////////////////////////////////////////////////////////////////////////////////////////////
        String url13 = "http://fransis.rawatwajah.com/century/cekCart2.php?email="+user.get(SessionManagement.KEY_EMAIL);
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
                        for (int i = 0; i < users.length(); i++) {
                            try {
                                JSONObject obj = users.getJSONObject(i);
                                if(obj.getString("Email") != null){
                                    ivRed.setVisibility(View.INVISIBLE);
                                }
                                else{
                                    ivRed.setVisibility(View.INVISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(DetailProduk.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
            }
        });

        queue.add(req3);

        //////////////////////////////////////////////////////////////////////////////////////////////////////
        if(CONFIG.CekGuest.equals("tidak")){
            String urlKosong = "http://fransis.rawatwajah.com/century/selectCustomer.php?email="+user.get(SessionManagement.KEY_EMAIL);
            JsonObjectRequest reqKosong = new JsonObjectRequest(urlKosong, null,
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
                                    String url1 = "http://fransis.rawatwajah.com/century/getDisplayStok.php?id_produk_per_lokasi="+tempId+"&id_lokasi="+obj.getString("Id_Lokasi");
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
                                                            if (obj.getString("Display_Stock").equals("0")) {
                                                                ivKosong.setVisibility(View.VISIBLE);
                                                                btnAddCart.setVisibility(View.GONE);
                                                            }
                                                            //Toast.makeText(DetailProduk.this,obj.getString("Display_Stock"), Toast.LENGTH_SHORT).show();
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }

                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(DetailProduk.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
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
                    //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                }
            });

            queue.add(reqKosong);
        }
        else if(CONFIG.CekGuest.equals("ya")){
            String url1 = "http://fransis.rawatwajah.com/century/getDisplayStok.php?id_produk_per_lokasi="+tempId+"&id_lokasi="+lokasi.get(SessionLokasi.KEY_ID_LOKASI);
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
                                    if (obj.getString("Display_Stock").equals("0")) {
                                        ivKosong.setVisibility(View.VISIBLE);
                                        btnAddCart.setVisibility(View.GONE);
                                    }
                                    //Toast.makeText(DetailProduk.this,obj.getString("Display_Stock"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(DetailProduk.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                }
            });

            queue.add(req);
        }



        String url1 = "http://fransis.rawatwajah.com/century/detailProduk.php?id_produk_per_lokasi="+tempId;
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
                                layoutDetail.setVisibility(View.VISIBLE);
                                layoutButton.setVisibility(View.VISIBLE);
                                pb.setVisibility(View.GONE);
                                JSONObject obj = users.getJSONObject(i);
                                tvNamaProduk.setText(obj.getString("Nama_Produk"));

                                price = obj.getInt("Harga");

                                if(obj.getString("Diskon").equals("0")){
                                    tvHargaDiskon.setVisibility(View.GONE);
                                    String s = (String.format("%,d", price)).replace(',', '.');
                                    tvHargaProduk.setText("Rp "+s);
                                }
                                else {
                                    String s = (String.format("%,d", price)).replace(',', '.');
                                    tvHargaProduk.setText("Rp "+s);
                                    tvHargaProduk.setPaintFlags(tvHargaProduk.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    tvHargaDiskon.setVisibility(View.VISIBLE);



                                    int diskonz = (int) (obj.getInt("Harga")*Float.valueOf(obj.getString("Diskon"))/100);
                                    int harga = obj.getInt("Harga")-diskonz;
                                    String diskon = (String.format("%,d", harga));
                                    tvHargaDiskon.setText("Rp "+diskon.replace(',', '.'));



                                }
                                stok = obj.getString("Stok");
                                tvDeskripsiProduk.setText(obj.getString("Deskripsi"));
                                Picasso.with(DetailProduk.this)
                                        .load(obj.getString("Gambar"))
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
                Toast.makeText(DetailProduk.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
            }
        });

        queue.add(req);

        qty = Integer.parseInt(etQtyProduk.getText().toString());
        ivTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CONFIG.CekGuest.equals("tidak")){
                    String url1 = "http://fransis.rawatwajah.com/century/selectCustomer.php?email="+user.get(SessionManagement.KEY_EMAIL);
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
                                            String url1 = "http://fransis.rawatwajah.com/century/getDisplayStok.php?id_produk_per_lokasi="+tempId+"&id_lokasi="+obj.getString("Id_Lokasi");
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
                                                                    if (!etQtyProduk.getText().toString().equals("99") && !etQtyProduk.getText().toString().equals(obj.getString("Display_Stock"))) {
                                                                        etQtyProduk.setText(String.valueOf(qty+=1));
                                                                    }
                                                                    if(etQtyProduk.getText().toString().equals(obj.getString("Display_Stock"))){
                                                                        ivTambah.setEnabled(false);
                                                                        builder.setMessage("Stok Tidak Mencukupi");
                                                                        builder.setCancelable(false);
                                                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                ivTambah.setEnabled(true);
                                                                            }
                                                                        });
                                                                        builder.setNegativeButton("", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                ivTambah.setEnabled(true);
                                                                            }
                                                                        });
                                                                        dialog = builder.show();
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }

                                                        }
                                                    }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(DetailProduk.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
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
                            //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                        }
                    });

                    queue.add(req);

                }
                else if(CONFIG.CekGuest.equals("ya")){
                    String url1 = "http://fransis.rawatwajah.com/century/getDisplayStok.php?id_produk_per_lokasi="+tempId+"&id_lokasi="+lokasi.get(SessionLokasi.KEY_ID_LOKASI);
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
                                            if (!etQtyProduk.getText().toString().equals("99") && !etQtyProduk.getText().toString().equals(obj.getString("Display_Stock"))) {
                                                etQtyProduk.setText(String.valueOf(qty+=1));
                                            }
                                            if(etQtyProduk.getText().toString().equals(obj.getString("Display_Stock"))){
                                                ivTambah.setEnabled(false);
                                                builder.setMessage("Stok Tidak Mencukupi");
                                                builder.setCancelable(false);
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        ivTambah.setEnabled(true);
                                                    }
                                                });
                                                builder.setNegativeButton("", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        ivTambah.setEnabled(true);
                                                    }
                                                });
                                                dialog = builder.show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(DetailProduk.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                        }
                    });

                    queue.add(req);
                }

            }
        });

        ivKurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etQtyProduk.getText().toString().equals("1")) {
                    etQtyProduk.setText(String.valueOf(qty-=1));
                }
            }
        });


        
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddCart.setEnabled(false);
                pb.setVisibility(View.VISIBLE);
                if(CONFIG.CekGuest.equals("tidak")){
                    /////////////////////////////////////////////////////
                    ///CEK BARANG
                    /////////////////////////////////////////////////////
                    String url1 = "http://fransis.rawatwajah.com/century/selectCart.php?id_produk_per_lokasi="+tempId+"&email="+user.get(SessionManagement.KEY_EMAIL);
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
                                    if(users.length() == 0 ){
                                        JSONObject objAdd2 = new JSONObject();
                                        try {
                                            JSONArray arrData = new JSONArray();
                                            JSONObject objDetail = new JSONObject();
                                            objDetail.put("qty",etQtyProduk.getText().toString());
                                            objDetail.put("email",user.get(SessionManagement.KEY_EMAIL));
                                            objDetail.put("id_produk_per_lokasi",tempId);
                                            objDetail.put("diskon_member","0");
                                            arrData.put(objDetail);
                                            objAdd2.put("data",arrData);
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                        JsonObjectRequest stringRequest2 = new JsonObjectRequest(Request.Method.POST, INSERT_CART_URL, objAdd2,
                                                new Response.Listener<JSONObject>() {
                                                    @Override

                                                    public void onResponse(JSONObject response) {
                                                        try {
                                                            if(response.getString("status").equals("OK")) {

                                                                JSONObject objAdd2 = new JSONObject();
                                                                try {
                                                                    JSONArray arrData = new JSONArray();
                                                                    JSONObject objDetail = new JSONObject();
                                                                    objDetail.put("id_produk_per_lokasi",tempId);
                                                                    objDetail.put("email",user.get(SessionManagement.KEY_EMAIL));
                                                                    arrData.put(objDetail);
                                                                    objAdd2.put("data",arrData);
                                                                } catch (JSONException e1) {
                                                                    e1.printStackTrace();
                                                                }
                                                                JsonObjectRequest stringRequest2 = new JsonObjectRequest(Request.Method.POST, UPDATE_RIWAYAT_URL, objAdd2,
                                                                        new Response.Listener<JSONObject>() {
                                                                            @Override

                                                                            public void onResponse(JSONObject response) {
                                                                                try {
                                                                                    if(response.getString("status").equals("OK")) {

                                                                                        builder.setMessage("Berhasil menambahkan barang ke keranjang belanja");
                                                                                        builder.setCancelable(false);
                                                                                        builder.setPositiveButton("Konfirmasi", new DialogInterface.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                                Intent i = new Intent(DetailProduk.this,CartActivity.class);
                                                                                                startActivityForResult(i,1);
                                                                                                btnAddCart.setEnabled(true);
                                                                                                pb.setVisibility(View.GONE);
                                                                                            }
                                                                                        });
                                                                                        builder.setNegativeButton("Lanjut Belanja", new DialogInterface.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                                finish();
                                                                                                startActivity(getIntent());
                                                                                                btnAddCart.setEnabled(true);
                                                                                                pb.setVisibility(View.GONE);

                                                                                            }
                                                                                        });
                                                                                        dialog = builder.show();
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
                                                                RequestQueue requestQueue2 = Volley.newRequestQueue(DetailProduk.this);
                                                                requestQueue2.add(stringRequest2);
                                                            }
                                                            else{
                                                                pb.setVisibility(View.GONE);
                                                                Toast.makeText(DetailProduk.this, "Gagal Menambahkan Produk Ke Keranjang", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (JSONException e1) {
                                                            e1.printStackTrace();
                                                        }

                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        pb.setVisibility(View.GONE);
                                                    }
                                                }) ;
                                        RequestQueue requestQueue2 = Volley.newRequestQueue(DetailProduk.this);
                                        requestQueue2.add(stringRequest2);
                                    }
                                    else{
                                        //cek display stock lagi

                                        String url1 = "http://fransis.rawatwajah.com/century/selectCustomer.php?email="+user.get(SessionManagement.KEY_EMAIL);
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
                                                                String url1 = "http://fransis.rawatwajah.com/century/getDisplayStok.php?id_produk_per_lokasi="+tempId+"&id_lokasi="+obj.getString("Id_Lokasi");
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
                                                                                        final JSONObject obj2 = users.getJSONObject(i);
                                                                                        String url1 = "http://fransis.rawatwajah.com/century/selectCartProduk.php?id_produk_per_lokasi="+tempId+"&email="+user.get(SessionManagement.KEY_EMAIL);
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
                                                                                                                JSONObject obj3 = users.getJSONObject(i);
                                                                                                                if(Integer.valueOf(etQtyProduk.getText().toString())+obj3.getInt("Qty") > obj2.getInt("Display_Stock")){
                                                                                                                    builder.setMessage("Stok Tidak Mencukupi");
                                                                                                                    builder.setCancelable(false);
                                                                                                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                                                        @Override
                                                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                                                            btnAddCart.setEnabled(true);
                                                                                                                            pb.setVisibility(View.GONE);
                                                                                                                        }
                                                                                                                    });
                                                                                                                    builder.setNegativeButton("", new DialogInterface.OnClickListener() {
                                                                                                                        @Override
                                                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                                                            btnAddCart.setEnabled(true);
                                                                                                                            pb.setVisibility(View.GONE);
                                                                                                                        }
                                                                                                                    });
                                                                                                                    dialog = builder.show();
                                                                                                                }
                                                                                                                else{
                                                                                                                    //update cart
                                                                                                                    JSONObject objAdd2 = new JSONObject();
                                                                                                                    try {
                                                                                                                        JSONArray arrData = new JSONArray();
                                                                                                                        JSONObject objDetail = new JSONObject();
                                                                                                                        objDetail.put("id_produk_per_lokasi",tempId);
                                                                                                                        objDetail.put("qty",etQtyProduk.getText().toString());
                                                                                                                        objDetail.put("email",user.get(SessionManagement.KEY_EMAIL));
                                                                                                                        arrData.put(objDetail);
                                                                                                                        objAdd2.put("data",arrData);
                                                                                                                    } catch (JSONException e1) {
                                                                                                                        e1.printStackTrace();
                                                                                                                    }
                                                                                                                    JsonObjectRequest stringRequest2 = new JsonObjectRequest(Request.Method.POST, UPDATE_CART_URL, objAdd2,
                                                                                                                            new Response.Listener<JSONObject>() {
                                                                                                                                @Override

                                                                                                                                public void onResponse(JSONObject response) {
                                                                                                                                    try {
                                                                                                                                        if(response.getString("status").equals("OK")) {
                                                                                                                                            JSONObject objAdd2 = new JSONObject();
                                                                                                                                            try {
                                                                                                                                                JSONArray arrData = new JSONArray();
                                                                                                                                                JSONObject objDetail = new JSONObject();
                                                                                                                                                objDetail.put("id_produk_per_lokasi",tempId);
                                                                                                                                                objDetail.put("email",user.get(SessionManagement.KEY_EMAIL));
                                                                                                                                                arrData.put(objDetail);
                                                                                                                                                objAdd2.put("data",arrData);
                                                                                                                                            } catch (JSONException e1) {
                                                                                                                                                e1.printStackTrace();
                                                                                                                                            }
                                                                                                                                            JsonObjectRequest stringRequest2 = new JsonObjectRequest(Request.Method.POST, UPDATE_RIWAYAT_URL, objAdd2,
                                                                                                                                                    new Response.Listener<JSONObject>() {
                                                                                                                                                        @Override

                                                                                                                                                        public void onResponse(JSONObject response) {
                                                                                                                                                            try {
                                                                                                                                                                if(response.getString("status").equals("OK")) {
                                                                                                                                                                    builder.setMessage("Berhasil menambahkan barang ke keranjang belanja");
                                                                                                                                                                    builder.setCancelable(false);
                                                                                                                                                                    builder.setPositiveButton("Konfirmasi", new DialogInterface.OnClickListener() {
                                                                                                                                                                        @Override
                                                                                                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                                                                                                            pb.setVisibility(View.GONE);
                                                                                                                                                                            Intent i = new Intent(DetailProduk.this,CartActivity.class);
                                                                                                                                                                            startActivityForResult(i,1);
                                                                                                                                                                            btnAddCart.setEnabled(true);
                                                                                                                                                                        }
                                                                                                                                                                    });
                                                                                                                                                                    builder.setNegativeButton("Lanjut Belanja", new DialogInterface.OnClickListener() {
                                                                                                                                                                        @Override
                                                                                                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                                                                                                            finish();
                                                                                                                                                                            startActivity(getIntent());
                                                                                                                                                                            btnAddCart.setEnabled(true);
                                                                                                                                                                            pb.setVisibility(View.GONE);

                                                                                                                                                                        }
                                                                                                                                                                    });
                                                                                                                                                                    dialog = builder.show();
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
                                                                                                                                            RequestQueue requestQueue2 = Volley.newRequestQueue(DetailProduk.this);
                                                                                                                                            requestQueue2.add(stringRequest2);


                                                                                                                                        }
                                                                                                                                        else{
                                                                                                                                            pb.setVisibility(View.GONE);
                                                                                                                                            Toast.makeText(DetailProduk.this, "Gagal Menambahkan Produk Ke Keranjang", Toast.LENGTH_SHORT).show();
                                                                                                                                        }
                                                                                                                                    } catch (JSONException e1) {
                                                                                                                                        e1.printStackTrace();
                                                                                                                                    }

                                                                                                                                }
                                                                                                                            },
                                                                                                                            new Response.ErrorListener() {
                                                                                                                                @Override
                                                                                                                                public void onErrorResponse(VolleyError error) {
                                                                                                                                    pb.setVisibility(View.GONE);
                                                                                                                                }
                                                                                                                            }) ;
                                                                                                                    RequestQueue requestQueue2 = Volley.newRequestQueue(DetailProduk.this);
                                                                                                                    requestQueue2.add(stringRequest2);
                                                                                                                }
                                                                                                            } catch (JSONException e) {
                                                                                                                e.printStackTrace();
                                                                                                            }

                                                                                                        }

                                                                                                    }
                                                                                                }, new Response.ErrorListener() {
                                                                                            @Override
                                                                                            public void onErrorResponse(VolleyError error) {
                                                                                                Toast.makeText(DetailProduk.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
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
                                                                        Toast.makeText(DetailProduk.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
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
                                                //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                                            }
                                        });

                                        queue.add(req);
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pb.setVisibility(View.GONE);
                            Toast.makeText(DetailProduk.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                        }
                    });
                    queue.add(req);
                }
                else if(CONFIG.CekGuest.equals("ya")){
                    List<ProdukTemp> produkTemp = db.getAllProdukTemp();
                            if(produkTemp.size() == 0){
                                db.addProdukTemp(new ProdukTemp(Integer.valueOf(tempId),Integer.valueOf(etQtyProduk.getText().toString())));
                                builder.setMessage("Berhasil menambahkan barang ke keranjang belanja");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Konfirmasi", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        pb.setVisibility(View.GONE);
                                        Intent i = new Intent(DetailProduk.this,CartActivity.class);
                                        startActivityForResult(i,1);
                                        btnAddCart.setEnabled(true);
                                    }
                                });
                                builder.setNegativeButton("Lanjut Belanja", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        startActivity(getIntent());
                                        btnAddCart.setEnabled(true);
                                        pb.setVisibility(View.GONE);

                                    }
                                });
                                dialog = builder.show();
                            }
                            else{
                                for(ProdukTemp pt :produkTemp){
                                    if(pt.getId_produk_per_lokasi() == Integer.valueOf(tempId)){
                                        countGuest++;
                                        tempQty=pt.getQty();
                                    }
                                }

                                if(countGuest>0){
                                    db.updateProdukTemp(new ProdukTemp(Integer.valueOf(tempId),tempQty + Integer.valueOf(etQtyProduk.getText().toString())));
                                    builder.setMessage("Berhasil menambahkan barang ke keranjang belanja");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("Konfirmasi", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            pb.setVisibility(View.GONE);
                                            Intent i = new Intent(DetailProduk.this, CartActivity.class);
                                            startActivityForResult(i, 1);
                                            btnAddCart.setEnabled(true);
                                        }
                                    });
                                    builder.setNegativeButton("Lanjut Belanja", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                            startActivity(getIntent());
                                            btnAddCart.setEnabled(true);
                                            pb.setVisibility(View.GONE);

                                        }
                                    });
                                    dialog = builder.show();
                                }
                                else if(countGuest == 0){
                                    db.addProdukTemp(new ProdukTemp(Integer.valueOf(tempId),Integer.valueOf(etQtyProduk.getText().toString())));
                                    builder.setMessage("Berhasil menambahkan barang ke keranjang belanja");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("Konfirmasi", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            pb.setVisibility(View.GONE);
                                            Intent i = new Intent(DetailProduk.this,CartActivity.class);
                                            startActivityForResult(i,1);
                                            btnAddCart.setEnabled(true);
                                        }
                                    });
                                    builder.setNegativeButton("Lanjut Belanja", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                            startActivity(getIntent());
                                            btnAddCart.setEnabled(true);
                                            pb.setVisibility(View.GONE);

                                        }
                                    });
                                    dialog = builder.show();
                                }
                    }
                }



            }
        });


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject objAdd2 = new JSONObject();
                try {
                    JSONArray arrData = new JSONArray();
                    JSONObject objDetail = new JSONObject();
                    objDetail.put("id_produk_per_lokasi",tempId);
                    objDetail.put("email",user.get(SessionManagement.KEY_EMAIL));
                    arrData.put(objDetail);
                    objAdd2.put("data",arrData);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                JsonObjectRequest stringRequest2 = new JsonObjectRequest(Request.Method.POST, UPDATE_RIWAYAT_GA_BELI_URL, objAdd2,
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
                RequestQueue requestQueue2 = Volley.newRequestQueue(DetailProduk.this);
                requestQueue2.add(stringRequest2);
                finish();
            }
        });


        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = getIntent();
                Intent i = new Intent(DetailProduk.this,CartActivity.class);
                startActivityForResult(i,1);
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isDataConnectionAvailable(DetailProduk.this) == false){
                    Toast.makeText(DetailProduk.this, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
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
    protected void onStart() {
        super.onStart();
        if(isDataConnectionAvailable(DetailProduk.this) == false){
            //Toast.makeText(context, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
            mainLayout.setVisibility(View.GONE);
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
        final HashMap<String, String> user = session.getUserDetails();
        JSONObject objAdd2 = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("id_produk_per_lokasi",tempId);
            objDetail.put("email",user.get(SessionManagement.KEY_EMAIL));
            arrData.put(objDetail);
            objAdd2.put("data",arrData);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JsonObjectRequest stringRequest2 = new JsonObjectRequest(Request.Method.POST, UPDATE_RIWAYAT_GA_BELI_URL, objAdd2,
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(DetailProduk.this);
        requestQueue2.add(stringRequest2);
        finish();
    }




    @Override
    protected void onPause() {
        super.onPause();
        final HashMap<String, String> user = session.getUserDetails();
        JSONObject objAdd2 = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("id_produk_per_lokasi",tempId);
            objDetail.put("email",user.get(SessionManagement.KEY_EMAIL));
            arrData.put(objDetail);
            objAdd2.put("data",arrData);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JsonObjectRequest stringRequest2 = new JsonObjectRequest(Request.Method.POST, UPDATE_RIWAYAT_GA_BELI_URL, objAdd2,
                new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("status").equals("OK")) {

                            }
                            else{

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(DetailProduk.this);
        requestQueue2.add(stringRequest2);
    }

}
