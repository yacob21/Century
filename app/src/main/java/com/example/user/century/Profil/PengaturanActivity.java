package com.example.user.century.Profil;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.century.Cart.CartActivity;
import com.example.user.century.R;
import com.example.user.century.Session.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PengaturanActivity extends AppCompatActivity {
    RequestQueue queue;
    SessionManagement session;
    LinearLayout linearUbahProfil,linearUbahLokasi,linearGantiPassword;
    String pengecekanku;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);
        queue = Volley.newRequestQueue(this);
        getSupportActionBar().setTitle(Html.fromHtml("<'font color='#ffffff'>Pengaturan</font>"));
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        builder = new AlertDialog.Builder(this);

        ////////////////////////////////////////////////////////////////////////////
        ///cek cart
        //////////////////////////////////////////////////////////////////////////
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
                                    pengecekanku="ada";
                                }
                                else{
                                    pengecekanku=null;
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




        linearUbahProfil = (LinearLayout) findViewById(R.id.linearUbahProfil);
        linearUbahLokasi = (LinearLayout) findViewById(R.id.linearUbahLokasi);
        linearGantiPassword = (LinearLayout) findViewById(R.id.linearGantiPassword);

        linearUbahProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PengaturanActivity.this,UbahProfilActivity.class);
                startActivity(i);
            }
        });


        linearUbahLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pengecekanku==null) {
                    Intent i = new Intent(PengaturanActivity.this, UbahLokasiActivity.class);
                    startActivity(i);
                }
                else{
                    builder.setMessage("Anda Masih memiliki barang di Keranjang");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(PengaturanActivity.this,CartActivity.class);
                            startActivity(i);
                        }
                    });
                    dialog = builder.show();
                }
            }
        });


        linearGantiPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PengaturanActivity.this,GantiPasswordActivity.class);
                startActivity(i);
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
                return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
