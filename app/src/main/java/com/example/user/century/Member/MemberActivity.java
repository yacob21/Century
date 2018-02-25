package com.example.user.century.Member;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
import com.example.user.century.Home.MainActivity;
import com.example.user.century.Profil.UbahProfilActivity;
import com.example.user.century.R;
import com.example.user.century.Session.SessionManagement;
import com.example.user.century.model.Code128;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MemberActivity extends AppCompatActivity {
    RequestQueue queue;
    SessionManagement session;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    ImageView ivBarcode,ivMember;
    Context context;
    TextView tvNama;
    String today;
    public static final String UPDATE_MEMBER_URL = "http://fransis.rawatwajah.com/century/updateMember.php";

    ProgressBar pbKoneksi,pb;
    Button btnRefresh;
    FrameLayout mainLayout;
    LinearLayout koneksiLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        queue = Volley.newRequestQueue(this);
        builder = new AlertDialog.Builder(this);
        session = new SessionManagement(getApplicationContext());
        final HashMap<String, String> user = session.getUserDetails();

        pb = (ProgressBar) findViewById(R.id.pb);
        pbKoneksi = (ProgressBar) findViewById(R.id.pbKoneksi);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        koneksiLayout = (LinearLayout) findViewById(R.id.koneksiLayout);
        mainLayout = (FrameLayout) findViewById(R.id.mainLayout);
        ivMember = (ImageView) findViewById(R.id.ivMember);
        ivBarcode = (ImageView) findViewById(R.id.ivBarcode);
        tvNama = (TextView) findViewById(R.id.tvNama);

        context = this;

        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyMM");
        today = formatter.format(date);
        
        
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
                                final JSONObject obj = users.getJSONObject(i);
                                if(obj.getString("Member").equals("") || obj.getString("Member").equals(" ")){
                                    pb.setVisibility(View.GONE);
                                    builder.setMessage("Anda Tidak Memiliki Member, Apakah Anda Ingin Mendaftar Sebagai Member?");
                                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            builder.setMessage("Pembuatan Member menggunakan data pribadi di profil Anda, apakah Anda ingin menggantinya terlebih dahulu?");
                                            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent i = new Intent (MemberActivity.this,UbahProfilActivity.class);
                                                    startActivity(i);
                                                }
                                            });
                                            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    /////////////////////////UPDATE MEMBER
                                                    JSONObject objRegister = new JSONObject();
                                                    try {
                                                        JSONArray arrData = new JSONArray();
                                                        JSONObject objDetail = new JSONObject();
                                                        objDetail.put("email",user.get(SessionManagement.KEY_EMAIL));
                                                        objDetail.put("member", today+String.format("%06d",obj.getInt("Id_Customer")));
                                                        arrData.put(objDetail);
                                                        objRegister.put("data",arrData);
                                                    } catch (JSONException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, UPDATE_MEMBER_URL, objRegister,
                                                            new Response.Listener<JSONObject>() {
                                                                @Override

                                                                public void onResponse(JSONObject response) {
                                                                    try {
                                                                        if(response.getString("status").equals("OK")) {
                                                                            //Toast.makeText(MemberActivity.this, today+String.format("%06d",obj.getInt("Id_Customer")), Toast.LENGTH_SHORT).show();
                                                                            Toast.makeText(context, "Berhasil Membuat Member", Toast.LENGTH_SHORT).show();
                                                                            startActivity(getIntent());
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
                                                                    // Toast.makeText(Register.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                                                                }
                                                            }) ;
                                                    RequestQueue requestQueue = Volley.newRequestQueue(MemberActivity.this);
                                                    requestQueue.add(stringRequest);
                                                    ///////////////////////////////////////
                                                }
                                            });
                                            dialog = builder.show();
                                        }
                                    });
                                    builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                                    dialog = builder.show();
                                }
                                else{
                                    pb.setVisibility(View.GONE);
                                    ivMember.setVisibility(View.VISIBLE);
                                    tvNama.setVisibility(View.VISIBLE);
                                    ivBarcode.setVisibility(View.VISIBLE);
                                    tvNama.setText(obj.getString("Nama"));
                                    drawBarcode();


                                }
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

                if(isDataConnectionAvailable(MemberActivity.this) == false){
                    Toast.makeText(context, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
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

    private void drawBarcode() {

        HashMap<String, String> user = session.getUserDetails();
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
                                    String barcode = obj.getString("Member");
                                    Code128 code = new Code128(context);
                                    code.setData(barcode);
                                    Bitmap bitmap = code.getBitmap(800, 200);
                                    ivBarcode = (ImageView)findViewById(R.id.ivBarcode);
                                    ivBarcode.setImageBitmap(bitmap);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MemberActivity.this,MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isDataConnectionAvailable(MemberActivity.this) == false){
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
