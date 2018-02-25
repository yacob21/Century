package com.example.user.century.Cart;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.user.century.DatabaseHandler;
import com.example.user.century.GuestLoginActivity.GuestActivity;
import com.example.user.century.GuestLoginActivity.SessionLokasi;
import com.example.user.century.Home.MainActivity;
import com.example.user.century.LoginCartActivity;
import com.example.user.century.Pembayaran.PembayaranActivity;
import com.example.user.century.ProdukTemp;
import com.example.user.century.R;
import com.example.user.century.Session.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    RecyclerView cardListCart,cardListCartUpdate;
    RequestQueue queue;
    SessionManagement session;

    List<Cart> resultCart = new ArrayList<>();
    CartAdapter ca;

    Context context;

    CartUpdateAdapter cua;

    AlertDialog dialog;
    AlertDialog.Builder builder;
    int id_customer;
    String nama,quantity,gambar,id,id_cart,id_lokasi,id_produk_per_lokasi;
    int harga;

    Button btnBatal,btnSimpan,btnKonfirmasi;
    TextView tvEdit,textTotal;
    TextView tvLokasi,tvTotal;
    LinearLayout lokasi,bawah;
    FrameLayout frameCard;

    int totalHarga,totalHargaMember;

    String kode;
    String idCustomer;
    String idCart;
    String today,hour;
    String batas;

    int unik;

    String tempidcustomer;
    String  tempidcart;

    int countKosong;

    String tempIdProduk;
    ArrayList<String>tempQty = new ArrayList<>();
    ////////////////////////////
    public static final String INSERT_PEMBELIAN_URL = "http://fransis.rawatwajah.com/century/insertPembayaran.php";
    public static final String INSERT_DETAIL_PEMBELIAN_URL = "http://fransis.rawatwajah.com/century/insertDetailPembayaran.php";
    public static final String DELETE_DETAIL_CART_URL="http://fransis.rawatwajah.com/century/deleteIdDetailCart.php";
    public static final String UPDATE_QUANTITY_DETAIL_CART_URL="http://fransis.rawatwajah.com/century/updateQuantityCart.php";
    public static final String SELECT_PRODUK_GUEST_URL = "http://fransis.rawatwajah.com/century/selectProdukGuest.php";
    ProgressBar pb;

    ProgressBar pbKoneksi;
    Button btnRefresh;
    LinearLayout koneksiLayout;



    String tempProduk ="";

    SessionLokasi sessionLokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        context = this;
        queue = Volley.newRequestQueue(this);
        builder = new AlertDialog.Builder(this);
        session = new SessionManagement(getApplicationContext());
        sessionLokasi = new SessionLokasi(getApplicationContext());

        final HashMap<String, String> user = session.getUserDetails();
        final HashMap<String, String> lokasiTemp = sessionLokasi.getLokasiDetails();

        cardListCart = (RecyclerView) findViewById(R.id.cardListCart);
        cardListCart.setHasFixedSize(true);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(this);
        cardListCart.setLayoutManager(llm);



        cardListCartUpdate = (RecyclerView) findViewById(R.id.cardListCartUpdate);
        cardListCartUpdate.setHasFixedSize(true);
        RecyclerView.LayoutManager llm2 = new LinearLayoutManager(this);
        cardListCartUpdate.setLayoutManager(llm2);
        pb = (ProgressBar) findViewById(R.id.pb);
        tvEdit = (TextView) findViewById(R.id.tvEdit);
        btnBatal = (Button) findViewById(R.id.btnBatal);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        tvLokasi = (TextView) findViewById(R.id.tvLokasi);
        lokasi = (LinearLayout) findViewById(R.id.Lokasi);
        bawah = (LinearLayout) findViewById(R.id.bawah);
        frameCard = (FrameLayout) findViewById(R.id.frameCard);
        textTotal = (TextView) findViewById(R.id.textTotal);
        pbKoneksi = (ProgressBar) findViewById(R.id.pbKoneksi);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        koneksiLayout = (LinearLayout) findViewById(R.id.koneksiLayout);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        btnKonfirmasi = (Button) findViewById(R.id.btnKonfirmasi);
        btnBatal.setClickable(false);
        btnSimpan.setClickable(false);
        getSupportActionBar().setTitle(Html.fromHtml("<'font color='#ffffff'>Keranjang</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ////////////////////////////////////////////////////////////////////////////////////////////



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
                                    tvLokasi.setText(obj.getString("Nama_Lokasi"));
                                    id_lokasi = obj.getString("Id_Lokasi");
                                    id_customer = obj.getInt("Id_Customer");
                                    ///////////////////////////////////////////////////////////////////////////////////
                                    String url1 = "http://fransis.rawatwajah.com/century/listCart.php?email="+user.get(SessionManagement.KEY_EMAIL);
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
                                                        builder.setMessage("Anda belum memiliki barang di keranjang");
                                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Intent i = new Intent(CartActivity.this,MainActivity.class);
                                                                startActivity(i);
                                                            }
                                                        });
                                                        dialog = builder.show();
                                                    }
                                                    else {

                                                        lokasi.setVisibility(View.VISIBLE);
                                                        bawah.setVisibility(View.VISIBLE);
                                                        frameCard.setVisibility(View.VISIBLE);
                                                        btnKonfirmasi.setVisibility(View.VISIBLE);
                                                        for (int i = 0; i < users.length(); i++) {
                                                            pb.setVisibility(View.GONE);
                                                            try {
                                                                JSONObject obj = users.getJSONObject(i);
                                                                nama = obj.getString("Nama_Produk");

                                                                if(obj.getString("Diskon").equals("0")){
                                                                    harga = obj.getInt("Harga");
                                                                }

                                                                else {
                                                                    int diskonz = (int) (obj.getInt("Harga") * Float.valueOf(obj.getString("Diskon")) / 100);
                                                                    int net = obj.getInt("Harga") - diskonz;
                                                                    harga = net;
                                                                }
                                                                quantity = obj.getString("Qty");
                                                                gambar = obj.getString("Gambar");
                                                                //Toast.makeText(CartActivity.this, obj.getString("Id"), Toast.LENGTH_SHORT).show();
                                                                id = obj.getString("Id");
                                                                id_produk_per_lokasi = obj.getString("Id_Produk_Per_Lokasi");
                                                                //tempidcart.add(String.format("%02d",Integer.valueOf(id_cart)));
                                                                tempidcart = String.format("%02d", Integer.valueOf(id));
                                                                totalHarga += (harga * Integer.valueOf(quantity));
                                                                resultCart.add(new Cart(nama, harga * Integer.valueOf(quantity), quantity, gambar, id,id_produk_per_lokasi));
                                                                ca = new CartAdapter(resultCart);
                                                                cua = new CartUpdateAdapter(resultCart);
                                                                cardListCart.setAdapter(ca);
                                                                cardListCartUpdate.setAdapter(cua);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            String s = (String.format("%,d", totalHarga)).replace(',', '.');
                                                            tvTotal.setText("Rp " + s);
                                                            totalHargaMember = totalHarga - (totalHarga*5/100);
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
                                //tempidcustomer.add(String.format("%05d",id_customer));
                                tempidcustomer = String.format("%05d",id_customer);
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
            DatabaseHandler db = new DatabaseHandler(this);
            final List<ProdukTemp> produkTemp = db.getAllProdukTemp();
            if (produkTemp.size() == 0) {
                builder.setMessage("Anda belum memiliki barang di keranjang");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(CartActivity.this, GuestActivity.class);
                        startActivity(i);
                    }
                });
                dialog = builder.show();
            } else {
                lokasi.setVisibility(View.VISIBLE);
                bawah.setVisibility(View.VISIBLE);
                frameCard.setVisibility(View.VISIBLE);
                btnKonfirmasi.setVisibility(View.VISIBLE);
                String url1 = "http://fransis.rawatwajah.com/century/selectLokasiByIdLokasi.php?id="+lokasiTemp.get(SessionLokasi.KEY_ID_LOKASI);
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
                                        pb.setVisibility(View.GONE);
                                        try {
                                            JSONObject obj = users.getJSONObject(i);
                                            tvLokasi.setText(obj.getString("Nama_Lokasi"));
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
                //////////////////////////////////////////////////////////////////////////////////////////////////
                for(final ProdukTemp pt :produkTemp) {
                    String urlProduk = "http://fransis.rawatwajah.com/century/selectProdukGuest.php?id_produk_per_lokasi="+pt.getId_produk_per_lokasi();
                    JsonObjectRequest reqProduk = new JsonObjectRequest(urlProduk, null,
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
                                        pb.setVisibility(View.GONE);
                                        try {
                                            JSONObject obj = users.getJSONObject(i);
                                            nama = obj.getString("Nama_Produk");

                                            if (obj.getString("Diskon").equals("0")) {
                                                harga = obj.getInt("Harga");
                                            } else {
                                                int diskonz = (int) (obj.getInt("Harga") * Float.valueOf(obj.getString("Diskon")) / 100);
                                                int net = obj.getInt("Harga") - diskonz;
                                                harga = net;
                                            }
                                            quantity = String.valueOf(pt.getQty());
                                            gambar = obj.getString("Gambar");
                                            //Toast.makeText(CartActivity.this, obj.getString("Id"), Toast.LENGTH_SHORT).show();
                                            id = obj.getString("Id");
                                            id_produk_per_lokasi = obj.getString("Id");
                                            //tempidcart.add(String.format("%02d",Integer.valueOf(id_cart)));
                                            tempidcart = String.format("%02d", Integer.valueOf(id));
                                            totalHarga += (harga * Integer.valueOf(quantity));
                                            resultCart.add(new Cart(nama, harga * Integer.valueOf(quantity), quantity, gambar, id, id_produk_per_lokasi));
                                            ca = new CartAdapter(resultCart);
                                            cua = new CartUpdateAdapter(resultCart);
                                            cardListCart.setAdapter(ca);
                                            cardListCartUpdate.setAdapter(cua);


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        String s = (String.format("%,d", totalHarga)).replace(',', '.');
                                        tvTotal.setText("Rp " + s);
                                        totalHargaMember = totalHarga - (totalHarga * 5 / 100);
                                    }

                                }

                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                        }
                    });
                    queue.add(reqProduk);
                }
            }
        }


        unik = 1 + (int)(Math.random()*(999));

        Date date = Calendar.getInstance().getTime();

        DateFormat formatter = new SimpleDateFormat("yyMMdd");
        DateFormat jam= new SimpleDateFormat("HHmmss");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        batas = sdf1.format(c.getTime());

        today = formatter.format(date);

        hour = jam.format(date);


        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnKonfirmasi.setEnabled(false);
                pb.setVisibility(View.VISIBLE);
                if(CONFIG.CekGuest.equals("tidak")){
                    String urlCekStock = "http://fransis.rawatwajah.com/century/pengecekanQtyCart.php?email="+user.get(SessionManagement.KEY_EMAIL);
                    JsonObjectRequest reqCekStock = new JsonObjectRequest(urlCekStock, null,
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
                                            if(obj.getInt("Qty") > obj.getInt("Display_Stock")){
                                                tempProduk = tempIdProduk+", "+obj.getString("Nama_Produk");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if(tempProduk.equals("")){
                                        builder.setMessage("Apakah Anda telah yakin dengan barang yang Anda pilih?");
                                        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                HashMap<String, String> user = session.getUserDetails();
                                                String url12 = "http://fransis.rawatwajah.com/century/selectCustomer.php?email="+user.get(SessionManagement.KEY_EMAIL);
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
                                                                        if(obj.getString("Member").equals("") || obj.getString("Member").equals(" ") ){
                                                                            ///////////////////////////////////////////////////////////
                                                                            //INSERT PEMBAYARAN///////////////////////////////////////
                                                                            /////////////////////////////////////////////////////////
                                                                            JSONObject objAdd = new JSONObject();
                                                                            try {
                                                                                HashMap<String, String> user = session.getUserDetails();
                                                                                JSONArray arrData = new JSONArray();
                                                                                JSONObject objDetail = new JSONObject();
                                                                                objDetail.put("kode_transaksi",today+hour+tempidcustomer);
                                                                                objDetail.put("email",user.get(SessionManagement.KEY_EMAIL));
                                                                                objDetail.put("total_harga",totalHarga);
                                                                                objDetail.put("member","N");
                                                                                objDetail.put("unik",unik);
                                                                                objDetail.put("id_lokasi",id_lokasi);
                                                                                //objDetail.put("batas",batas);
                                                                                arrData.put(objDetail);
                                                                                objAdd.put("data",arrData);
                                                                            } catch (JSONException e1) {
                                                                                e1.printStackTrace();
                                                                            }
                                                                            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, INSERT_PEMBELIAN_URL, objAdd,
                                                                                    new Response.Listener<JSONObject>() {
                                                                                        @Override

                                                                                        public void onResponse(JSONObject response) {
                                                                                            try {
                                                                                                if(response.getString("status").equals("OK")) {
                                                                                                    ///////////////////////////////////////////////////////////
                                                                                                    //INSERT Detail Pembayaran////////////////////////////////////////
                                                                                                    /////////////////////////////////////////////////////////
                                                                                                    JSONObject objAdd = new JSONObject();
                                                                                                    try {
                                                                                                        JSONArray arrData = new JSONArray();
                                                                                                        for(int i =0 ; i <ca.cartList.size();i++){
                                                                                                            Cart ct = ca.cartList.get(i);
                                                                                                            JSONObject objDetail = new JSONObject();
                                                                                                            objDetail.put("kode_transaksi",today+hour+tempidcustomer);
                                                                                                            objDetail.put("nama_produk",ct.getNama());
                                                                                                            objDetail.put("jumlah",ct.getQuantity());
                                                                                                            objDetail.put("harga",ct.getHarga());
                                                                                                            objDetail.put("id_produk_per_lokasi",ct.Id_Produk_Per_Lokasi);
                                                                                                            arrData.put(objDetail);

                                                                                                        }
                                                                                                        objAdd.put("data",arrData);
                                                                                                    } catch (JSONException e1) {
                                                                                                        e1.printStackTrace();
                                                                                                    }
                                                                                                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, INSERT_DETAIL_PEMBELIAN_URL, objAdd,
                                                                                                            new Response.Listener<JSONObject>() {
                                                                                                                @Override

                                                                                                                public void onResponse(JSONObject response) {
                                                                                                                    try {
                                                                                                                        if(response.getString("status").equals("OK")) {
                                                                                                                            btnKonfirmasi.setEnabled(true);
                                                                                                                            pb.setVisibility(View.GONE);
                                                                                                                            Intent i = new Intent(CartActivity.this,PembayaranActivity.class);
                                                                                                                            i.putExtra("panduanBarcode",today+hour+tempidcustomer);
                                                                                                                            i.putExtra("panduanTotal",String.valueOf(totalHarga));
                                                                                                                            i.putExtra("panduanBatas",batas);
                                                                                                                            i.putExtra("unik",String.valueOf(unik));
                                                                                                                            startActivity(i);

                                                                                                                        }
                                                                                                                        else if(response.getString("status").equals("FALSE")){

                                                                                                                        }
                                                                                                                    } catch (JSONException e1) {
                                                                                                                        e1.printStackTrace();
                                                                                                                    }

                                                                                                                }
                                                                                                            },
                                                                                                            new Response.ErrorListener() {
                                                                                                                @Override
                                                                                                                public void onErrorResponse(VolleyError error) {
                                                                                                                    Toast.makeText(CartActivity.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                                                                                                                }
                                                                                                            }) ;
                                                                                                    RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
                                                                                                    requestQueue.add(stringRequest);

                                                                                                }
                                                                                                else if(response.getString("status").equals("FALSE")){

                                                                                                }
                                                                                            } catch (JSONException e1) {
                                                                                                e1.printStackTrace();
                                                                                            }

                                                                                        }
                                                                                    },
                                                                                    new Response.ErrorListener() {
                                                                                        @Override
                                                                                        public void onErrorResponse(VolleyError error) {
                                                                                            Toast.makeText(CartActivity.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                                                                                        }
                                                                                    }) ;
                                                                            RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
                                                                            requestQueue.add(stringRequest);
                                                                        }
                                                                        else{
                                                                            ///////////////////////////////////////////////////////////
                                                                            //INSERT Pembayaran MEMBER//////////////////////////////////
                                                                            /////////////////////////////////////////////////////////
                                                                            JSONObject objAdd = new JSONObject();
                                                                            try {
                                                                                HashMap<String, String> user = session.getUserDetails();
                                                                                JSONArray arrData = new JSONArray();
                                                                                JSONObject objDetail = new JSONObject();
                                                                                objDetail.put("kode_transaksi",today+hour+tempidcustomer);
                                                                                objDetail.put("email",user.get(SessionManagement.KEY_EMAIL));
                                                                                objDetail.put("total_harga",totalHargaMember);
                                                                                objDetail.put("unik",unik);
                                                                                objDetail.put("member","Y");
                                                                                objDetail.put("id_lokasi",id_lokasi);
                                                                                //objDetail.put("batas",batas);
                                                                                arrData.put(objDetail);
                                                                                objAdd.put("data",arrData);
                                                                            } catch (JSONException e1) {
                                                                                e1.printStackTrace();
                                                                            }
                                                                            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, INSERT_PEMBELIAN_URL, objAdd,
                                                                                    new Response.Listener<JSONObject>() {
                                                                                        @Override

                                                                                        public void onResponse(JSONObject response) {
                                                                                            try {
                                                                                                if(response.getString("status").equals("OK")) {

                                                                                                    ///////////////////////////////////////////////////////////
                                                                                                    //INSERT Detail Pembayaran MEMBER//////////////////////////
                                                                                                    /////////////////////////////////////////////////////////
                                                                                                    JSONObject objAdd = new JSONObject();
                                                                                                    try {
                                                                                                        JSONArray arrData = new JSONArray();
                                                                                                        for(int i =0 ; i <ca.cartList.size();i++){
                                                                                                            Cart ct = ca.cartList.get(i);
                                                                                                            JSONObject objDetail = new JSONObject();
                                                                                                            objDetail.put("kode_transaksi",today+hour+tempidcustomer);
                                                                                                            objDetail.put("nama_produk",ct.getNama());
                                                                                                            objDetail.put("jumlah",ct.getQuantity());
                                                                                                            objDetail.put("harga",ct.getHarga());
                                                                                                            objDetail.put("id_produk_per_lokasi",ct.Id_Produk_Per_Lokasi);
                                                                                                            arrData.put(objDetail);

                                                                                                        }
                                                                                                        objAdd.put("data",arrData);
                                                                                                    } catch (JSONException e1) {
                                                                                                        e1.printStackTrace();
                                                                                                    }
                                                                                                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, INSERT_DETAIL_PEMBELIAN_URL, objAdd,
                                                                                                            new Response.Listener<JSONObject>() {
                                                                                                                @Override

                                                                                                                public void onResponse(JSONObject response) {
                                                                                                                    try {
                                                                                                                        if(response.getString("status").equals("OK")) {
                                                                                                                            btnKonfirmasi.setEnabled(true);
                                                                                                                            pb.setVisibility(View.GONE);
                                                                                                                            Intent i = new Intent(CartActivity.this,PembayaranActivity.class);
                                                                                                                            i.putExtra("panduanBarcode",today+hour+tempidcustomer);
                                                                                                                            i.putExtra("panduanTotal",String.valueOf(totalHargaMember));
                                                                                                                            i.putExtra("panduanBatas",batas);
                                                                                                                            i.putExtra("unik",String.valueOf(unik));
                                                                                                                            startActivity(i);
                                                                                                                        }
                                                                                                                        else if(response.getString("status").equals("FALSE")){

                                                                                                                        }
                                                                                                                    } catch (JSONException e1) {
                                                                                                                        e1.printStackTrace();
                                                                                                                    }

                                                                                                                }
                                                                                                            },
                                                                                                            new Response.ErrorListener() {
                                                                                                                @Override
                                                                                                                public void onErrorResponse(VolleyError error) {
                                                                                                                    Toast.makeText(CartActivity.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                                                                                                                    btnKonfirmasi.setEnabled(true);
                                                                                                                    pb.setVisibility(View.GONE);
                                                                                                                }
                                                                                                            }) ;
                                                                                                    RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
                                                                                                    requestQueue.add(stringRequest);

                                                                                                }
                                                                                                else if(response.getString("status").equals("FALSE")){

                                                                                                }
                                                                                            } catch (JSONException e1) {
                                                                                                e1.printStackTrace();
                                                                                            }

                                                                                        }
                                                                                    },
                                                                                    new Response.ErrorListener() {
                                                                                        @Override
                                                                                        public void onErrorResponse(VolleyError error) {
                                                                                            Toast.makeText(CartActivity.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                                                                                            btnKonfirmasi.setEnabled(true);
                                                                                            pb.setVisibility(View.GONE);
                                                                                        }
                                                                                    }) ;
                                                                            RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
                                                                            requestQueue.add(stringRequest);
                                                                        }


                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    //tempidcustomer.add(String.format("%05d",id_customer));
                                                                    tempidcustomer = String.format("%05d",id_customer);
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
                                        });
                                        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                btnKonfirmasi.setEnabled(true);
                                                pb.setVisibility(View.GONE);
                                            }
                                        });
                                        dialog = builder.show();
                                    }
                                    else{
                                        ///stok kosong
                                        builder.setMessage("Stok "+tempProduk.replace("null, ","")+" Kosong");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                btnKonfirmasi.setEnabled(true);
                                                pb.setVisibility(View.GONE);
                                            }
                                        });
                                        builder.setNegativeButton("", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                btnKonfirmasi.setEnabled(true);
                                                pb.setVisibility(View.GONE);
                                            }
                                        });
                                        dialog = builder.show();
                                    }
                                }

                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                        }
                    });

                    queue.add(reqCekStock);

                }
                else if(CONFIG.CekGuest.equals("ya")){
                    pb.setVisibility(View.GONE);
                    builder.setMessage("Anda harus masuk sebagai pengguna terlebih dahulu");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(CartActivity.this, LoginCartActivity.class);
                            startActivity(i);
                        }
                    });
                    dialog = builder.show();
                }
            }
        });


        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < ca.cartList.size(); i++) {
                    Cart ct = ca.cartList.get(i);
                    tempQty.add(ct.Quantity);
                }
                btnBatal.setClickable(true);
                btnSimpan.setClickable(true);
                cardListCart.setVisibility(View.GONE);
                cardListCartUpdate.setVisibility(View.VISIBLE);
                btnKonfirmasi.setVisibility(View.INVISIBLE);
                if(CONFIG.CekGuest.equals("tidak")){
                    btnBatal.setVisibility(View.VISIBLE);
                }
                else if(CONFIG.CekGuest.equals("ya")){
                    btnBatal.setVisibility(View.GONE);
                }
                btnSimpan.setVisibility(View.VISIBLE);
                textTotal.setVisibility(View.INVISIBLE);
                tvTotal.setVisibility(View.INVISIBLE);
            }
        });







        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getIntent());
                finish();
            }
        });



        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isDataConnectionAvailable(CartActivity.this) == false){
                    Toast.makeText(context, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
                    lokasi.setVisibility(View.GONE);
                    bawah.setVisibility(View.GONE);
                    frameCard.setVisibility(View.GONE);
                    koneksiLayout.setVisibility(View.VISIBLE);
                }else{
                    overridePendingTransition( 0, 0);
                    startActivity(getIntent());
                    finish();
                    overridePendingTransition( 0, 0);
                }

            }
        });




        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CONFIG.CekGuest.equals("tidak")){
                    countKosong=0;
                    for(int i=0;i<cua.cartUpdateList.size();i++) {
                        Cart ct = cua.cartUpdateList.get(i);
                        if(ct.Quantity.equals("0") || ct.Quantity.equals("00")){
                            countKosong++;
                        }
                    }

                    if(cua.tempId.size() > 0 && countKosong == 0){
                        JSONObject objAktivitas = new JSONObject();
                        try {
                            JSONArray arrData = new JSONArray();
                            for (int i = 0; i < cua.cartUpdateList.size(); i++) {
                                Cart ct = cua.cartUpdateList.get(i);
                                JSONObject objDetail = new JSONObject();
                                objDetail.put("qty", ct.getQuantity());
                                objDetail.put("id", ct.getId());
                                arrData.put(objDetail);
                            }
                            objAktivitas.put("data", arrData);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, UPDATE_QUANTITY_DETAIL_CART_URL, objAktivitas,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            if (response.getString("status").equals("OK")) {
                                                finish();
                                                startActivity(getIntent());
                                            }
                                            else {
                                                finish();
                                                startActivity(getIntent());
                                                Toast.makeText(context, "gagal update produk", Toast.LENGTH_SHORT).show();
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
                                });
                        RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
                        requestQueue.add(stringRequest);
                        ///////////////////////////////////////////////////////////
                        JSONObject objAdd = new JSONObject();
                        try {
                            JSONArray arrData = new JSONArray();
                            for (int i = 0; i < cua.tempId.size(); i++) {
                                //Toast.makeText(context,cua.tempId.get(i), Toast.LENGTH_SHORT).show();
                                JSONObject objDetail = new JSONObject();
                                objDetail.put("id", cua.tempId.get(i));
                                arrData.put(objDetail);
                            }
                            objAdd.put("data", arrData);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        JsonObjectRequest stringRequest2 = new JsonObjectRequest(Request.Method.POST, DELETE_DETAIL_CART_URL, objAdd,
                                new Response.Listener<JSONObject>() {
                                    @Override

                                    public void onResponse(JSONObject response) {
                                        try {
                                            if (response.getString("status").equals("OK")) {
                                                finish();
                                                startActivity(getIntent());

                                            } else if (response.getString("status").equals("FALSE")) {
                                                finish();
                                                startActivity(getIntent());
                                                Toast.makeText(context, "gagal update produk", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        RequestQueue requestQueue2 = Volley.newRequestQueue(CartActivity.this);
                        requestQueue2.add(stringRequest2);
                    }
                    else if(countKosong == 0){
                        JSONObject objAktivitas = new JSONObject();
                        try {
                            JSONArray arrData = new JSONArray();
                            for (int i = 0; i < cua.cartUpdateList.size(); i++) {
                                Cart ct = cua.cartUpdateList.get(i);
                                JSONObject objDetail = new JSONObject();
                                objDetail.put("qty", ct.getQuantity());
                                objDetail.put("id", ct.getId());
                                arrData.put(objDetail);
                            }
                            objAktivitas.put("data", arrData);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, UPDATE_QUANTITY_DETAIL_CART_URL, objAktivitas,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            if (response.getString("status").equals("OK")) {
                                                finish();
                                                startActivity(getIntent());
                                            }
                                            else {
                                                finish();
                                                startActivity(getIntent());
                                                Toast.makeText(context, "gagal update produk", Toast.LENGTH_SHORT).show();
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
                                });
                        RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
                        requestQueue.add(stringRequest);
                    }
                    else if (cua.tempId.size() > 0) {
                        JSONObject objAdd = new JSONObject();
                        try {
                            JSONArray arrData = new JSONArray();
                            for (int i = 0; i < cua.tempId.size(); i++) {
                                Toast.makeText(context,cua.tempId.get(i), Toast.LENGTH_SHORT).show();
                                JSONObject objDetail = new JSONObject();
                                objDetail.put("id", cua.tempId.get(i));
                                arrData.put(objDetail);
                            }
                            objAdd.put("data", arrData);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, DELETE_DETAIL_CART_URL, objAdd,
                                new Response.Listener<JSONObject>() {
                                    @Override

                                    public void onResponse(JSONObject response) {
                                        try {
                                            if (response.getString("status").equals("OK")) {
                                                finish();
                                                startActivity(getIntent());

                                            } else if (response.getString("status").equals("FALSE")) {
                                                finish();
                                                startActivity(getIntent());
                                                Toast.makeText(context, "gagal update produk", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
                        requestQueue.add(stringRequest);
                    }
                    else if(countKosong>0){

                        builder.setMessage("Quantity tidak boleh kosong");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                countKosong = 0;
                            }
                        });
                        dialog = builder.show();
                    }
                }
                else if(CONFIG.CekGuest.equals("ya")){
                finish();
                startActivity(getIntent());
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
        Intent x = getIntent();
        switch (id){
            case android.R.id.home:
                if (btnBatal.getVisibility() == View.VISIBLE && btnSimpan.getVisibility() == View.VISIBLE) {
                    builder.setMessage("Harap simpan perubahan data");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog = builder.show();
                } else {
                    CONFIG.CekBelanja="tidak";
                    ComponentName prev = this.getCallingActivity();
                    if(String.valueOf(prev).contains("MainActivity")){
                        Intent i = new Intent(CartActivity.this,MainActivity.class);
                        startActivity(i);
                    }
                    else{
                        finish();
                    }
                }
                return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        //setResult(RESULT_OK, new Intent().putExtra("panduanIdProduk", x.getStringExtra("panduanId")));
        if (btnBatal.getVisibility() == View.VISIBLE && btnSimpan.getVisibility() == View.VISIBLE) {
            builder.setMessage("Harap simpan perubahan data");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog = builder.show();
        } else {
            CONFIG.CekBelanja="tidak";
            ComponentName prev = this.getCallingActivity();
            if(String.valueOf(prev).contains("MainActivity")){
                Intent i = new Intent(CartActivity.this,MainActivity.class);
                startActivity(i);
            }
            else{
                finish();
            }
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        if(isDataConnectionAvailable(CartActivity.this) == false){
            //Toast.makeText(context, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
            lokasi.setVisibility(View.GONE);
            bawah.setVisibility(View.GONE);
            frameCard.setVisibility(View.GONE);
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

