package com.example.user.century.Profil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.user.century.R;
import com.example.user.century.Session.SessionManagement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class GantiPasswordActivity extends AppCompatActivity {
    RequestQueue queue;
    SessionManagement session;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    EditText etBaru,etKonfirmasi;
    Button btnSimpan;
    Context context;
    String tempPassword;
    public static final String GANTI_PASSWORD_URL = "http://fransis.rawatwajah.com/century/gantiPassword.php";
    public static final String LOGIN_URL = "http://fransis.rawatwajah.com/century/login.php";

    ProgressBar pbKoneksi;
    Button btnRefresh;
    LinearLayout mainLayout;
    LinearLayout koneksiLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);
        queue = Volley.newRequestQueue(this);
        getSupportActionBar().setTitle(Html.fromHtml("<'font color='#ffffff'>Ganti Kata Sandi</font>"));
        session = new SessionManagement(getApplicationContext());
        final HashMap<String, String> user = session.getUserDetails();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        builder = new AlertDialog.Builder(this);
        context = this;
//        etLama = (EditText) findViewById(R.id.etLama);
        etBaru = (EditText) findViewById(R.id.etBaru);
        etKonfirmasi = (EditText) findViewById(R.id.etKonfirmasi);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        pbKoneksi = (ProgressBar) findViewById(R.id.pbKoneksi);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        koneksiLayout = (LinearLayout) findViewById(R.id.koneksiLayout);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);


        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etBaru.getText().toString().equals("")||etKonfirmasi.getText().toString().equals("")){
                    builder.setMessage("Kata sandi tidak boleh kosong");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog = builder.show();
                }
                else if(etBaru.length()<6 || etKonfirmasi.length() <6){
                    builder.setMessage("Kata sandi minimal 6 karakter");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog = builder.show();
                }
                else if(!etBaru.getText().toString().equals(etKonfirmasi.getText().toString())){
                    builder.setMessage("Kata sandi baru tidak boleh berbeda dengan konfirmasi kata sandi");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog = builder.show();
                }
                else{
                    update(etBaru.getText().toString());
//                    JSONObject objLogin = new JSONObject();
//                    try {
//                        JSONArray arrData = new JSONArray();
//                        JSONObject objDetail = new JSONObject();
//                        objDetail.put("email",user.get(SessionManagement.KEY_EMAIL));
//                        objDetail.put("password",etLama.getText().toString());
//                        arrData.put(objDetail);
//                        objLogin.put("data",arrData);
//                    } catch (JSONException e1) {
//                        e1.printStackTrace();
//                    }
//
//                    JsonObjectRequest stringRequest2 = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, objLogin,
//                            new Response.Listener<JSONObject>() {
//                                @Override
//
//                                public void onResponse(JSONObject response) {
//                                    try {
//                                        if(response.getString("status").equals("OK")) {
//                                            /////////////////////////////////////////////////////////////////////////
//                                            JSONObject objRegister = new JSONObject();
//                                            try {
//                                                JSONArray arrData = new JSONArray();
//                                                JSONObject objDetail = new JSONObject();
//                                                objDetail.put("email",user.get(SessionManagement.KEY_EMAIL));
//                                                objDetail.put("password",etBaru.getText().toString());
//                                                arrData.put(objDetail);
//                                                objRegister.put("data",arrData);
//                                            } catch (JSONException e1) {
//                                                e1.printStackTrace();
//                                            }
//                                            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, GANTI_PASSWORD_URL, objRegister,
//                                                    new Response.Listener<JSONObject>() {
//                                                        @Override
//
//                                                        public void onResponse(JSONObject response) {
//                                                            try {
//                                                                if(response.getString("status").equals("OK")) {
//                                                                    Toast.makeText(context, "Berhasil Ganti Password", Toast.LENGTH_SHORT).show();
//                                                                    finish();
//                                                                }
//                                                                else if(response.getString("status").equals("FALSE")){
//
//                                                                }
//                                                            } catch (JSONException e1) {
//                                                                e1.printStackTrace();
//                                                            }
//
//                                                        }
//                                                    },
//                                                    new Response.ErrorListener() {
//                                                        @Override
//                                                        public void onErrorResponse(VolleyError error) {
//                                                            // Toast.makeText(Register.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
//                                                        }
//                                                    }) ;
//                                            RequestQueue requestQueue = Volley.newRequestQueue(GantiPasswordActivity.this);
//                                            requestQueue.add(stringRequest);
//                                            ////////////////////////////////////////////////////////////////////////////
//                                        }
//                                        else if(response.getString("status").equals("FALSE")){
//                                            builder.setMessage("Password lama Anda tidak cocok");
//                                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                }
//                                            });
//                                            dialog = builder.show();
//                                        }
//                                    } catch (JSONException e1) {
//                                        e1.printStackTrace();
//                                    }
//
//                                }
//                            },
//                            new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                }
//                            }) ;
//                    RequestQueue requestQueue2 = Volley.newRequestQueue(GantiPasswordActivity.this);
//                    requestQueue2.add(stringRequest2);
                }

            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isDataConnectionAvailable(GantiPasswordActivity.this) == false){
                    Toast.makeText(GantiPasswordActivity.this, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(GantiPasswordActivity.this,ProfilActivity.class);
                startActivity(i);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(GantiPasswordActivity.this,ProfilActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isDataConnectionAvailable(GantiPasswordActivity.this) == false){
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

    public void update(String password){
        FirebaseUser userFirebase = FirebaseAuth.getInstance().getCurrentUser();
        userFirebase.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Berhasil ganti Kata sandi", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }
}
