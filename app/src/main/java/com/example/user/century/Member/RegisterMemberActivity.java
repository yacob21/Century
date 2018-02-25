package com.example.user.century.Member;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.century.R;
import com.example.user.century.Session.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterMemberActivity extends AppCompatActivity {
    EditText etEmail,etNama,etAlamat,etPhone,etTanggalLahir;
    Button btnBuat;
    RequestQueue queue;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_member);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etNama = (EditText) findViewById(R.id.etNama);
        etAlamat = (EditText) findViewById(R.id.etAlamat);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etTanggalLahir = (EditText) findViewById(R.id.etTanggalLahir);
        btnBuat = (Button) findViewById(R.id.btnBuat);
        queue = Volley.newRequestQueue(this);
        session = new SessionManagement(getApplicationContext());
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
                                etNama.setText(obj.getString("Nama"));
                                etEmail.setText(obj.getString("Email"));
                                etNama.setEnabled(false);
                                etEmail.setEnabled(false);
                                etPhone.setText(obj.getString("Nomor_HP"));
                                etAlamat.setText(obj.getString("Alamat"));
                                etTanggalLahir.setText(obj.getString("TglLahir"));
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

        btnBuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




    }
}
