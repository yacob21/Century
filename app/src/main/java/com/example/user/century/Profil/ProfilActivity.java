package com.example.user.century.Profil;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
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
import com.example.user.century.Home.MainActivity;
import com.example.user.century.R;
import com.example.user.century.Session.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProfilActivity extends AppCompatActivity {
    ImageView ivProfil;
    TextView tvNama,tvMember,tvEmail,tvPhone,tvAlamat,tvDob,tvApotek;
    LinearLayout btnSetting,linearMember;
    RequestQueue queue;
    SessionManagement session;
    String flag;
    ProgressBar pb;

    ProgressBar pbKoneksi;
    Button btnRefresh;
    NestedScrollView mainLayout;
    LinearLayout koneksiLayout,linearLayout2;
    LinearLayout frameProfil;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        queue = Volley.newRequestQueue(this);
        getSupportActionBar().setTitle(Html.fromHtml("<'font color='#ffffff'>Profil</font>"));
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
        frameProfil = (LinearLayout) findViewById(R.id.frameProfil);
        pbKoneksi = (ProgressBar) findViewById(R.id.pbKoneksi);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        koneksiLayout = (LinearLayout) findViewById(R.id.koneksiLayout);
        mainLayout = (NestedScrollView) findViewById(R.id.mainLayout);
        ivProfil = (ImageView) findViewById(R.id.ivProfil);
        tvNama = (TextView) findViewById(R.id.tvNama);
        //tvMember = (TextView) findViewById(R.id.tvMember);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvAlamat = (TextView) findViewById(R.id.tvAlamat);
        tvDob = (TextView) findViewById(R.id.tvDob);
        btnSetting = (LinearLayout) findViewById(R.id.btnSetting);
        //linearMember = (LinearLayout) findViewById(R.id.linearMember);
        tvApotek = (TextView) findViewById(R.id.tvApotek);
        pb = (ProgressBar) findViewById(R.id.pb);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilActivity.this,PengaturanActivity.class);
                startActivity(i);
            }
        });

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
                                    linearLayout2.setVisibility(View.VISIBLE);
                                    frameProfil.setVisibility(View.VISIBLE);
                                    pb.setVisibility(View.GONE);
                                    JSONObject obj = users.getJSONObject(i);
                                    tvNama.setText(obj.getString("Nama"));
                                    tvEmail.setText(obj.getString("Email"));
                                    String inisial = obj.getString("Nama").substring(0,1).toLowerCase();
                                    Resources res = getResources();
                                    int resID = res.getIdentifier(inisial , "drawable", getPackageName());
                                    Drawable drawable = res.getDrawable(resID );
                                    ivProfil.setImageDrawable(drawable);
                                    tvPhone.setText(obj.getString("Nomor_HP"));
                                    tvAlamat.setText(obj.getString("Alamat"));
                                    tvDob.setText(obj.getString("TglLahir"));
                                    tvApotek.setText(obj.getString("Nama_Lokasi"));
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

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isDataConnectionAvailable(ProfilActivity.this) == false){
                    Toast.makeText(ProfilActivity.this, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                Intent i = new Intent(ProfilActivity.this,MainActivity.class);
                startActivity(i);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ProfilActivity.this,MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isDataConnectionAvailable(ProfilActivity.this) == false){
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





}
