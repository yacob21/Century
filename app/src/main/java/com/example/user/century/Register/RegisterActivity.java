package com.example.user.century.Register;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.century.LoginActivity;
import com.example.user.century.R;
import com.example.user.century.Session.SessionCek;
import com.example.user.century.Session.SessionManagement;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    EditText etNama,etTanggalLahir,etAlamat,etEmail,etPassword,etKonfirmasiPassword,etMember,etPhone;
    Button btnRegis;
    CheckBox cbMember;
    RadioButton rbPria,rbWanita;
    DatePicker datePicker;
    Context context;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    ImageView ivBarcode;
    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;

    public static final String REGISTER_URL = "http://fransis.rawatwajah.com/century/register.php";
    SessionManagement session;
    static String JK;

    static String namas="",alamats="",tanggallahirs="",genders="",emails="",handphones="",passwords="",confirms="";

    private FirebaseAuth mAuth;
    SessionCek sessionCek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sessionCek = new SessionCek(getApplicationContext());
        session = new SessionManagement(getApplicationContext());
        builder = new AlertDialog.Builder(this);
        context = this;
        mAuth = FirebaseAuth.getInstance();

        final HashMap<String, String> user = session.getUserDetails();
        etNama = (EditText) findViewById(R.id.etNama);
        etTanggalLahir = (EditText) findViewById(R.id.etTanggalLahir);
        etAlamat = (EditText) findViewById(R.id.etAlamat);
        ivBarcode = (ImageView) findViewById(R.id.ivBarcode);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etKonfirmasiPassword = (EditText) findViewById(R.id.etKonfirmasiPassword);
        etMember = (EditText) findViewById(R.id.etMember);
        rbPria = (RadioButton) findViewById(R.id.rbPria);
        rbWanita = (RadioButton) findViewById(R.id.rbWanita);
        etPhone = (EditText) findViewById(R.id.etPhone);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[] {Manifest.permission.CAMERA}, 111);
        }


        Intent i = getIntent();
        if(LoginActivity.cekFB=="ADA"){
            etNama.setText(i.getStringExtra("nama"));
            etNama.setEnabled(false);
            etEmail.setText(i.getStringExtra("email"));
            etEmail.setEnabled(false);
            String tGender = i.getStringExtra("gender");
            if (tGender.equals("male")){
                rbPria.setChecked(true);
                JK= "Pria";
            }else if(tGender.equals("female")){
                rbWanita.setChecked(true);
                JK= "Wanita";
            }
//            LoginActivity.cekFB=null;
        }

        if(namas !=null || alamats != null || tanggallahirs != null || genders != null || emails != null || handphones!=null || passwords!=null || confirms!=null){
            etNama.setText(namas);
            etAlamat.setText(alamats);
            etTanggalLahir.setText(tanggallahirs);
            if(JK!=null && genders.equals("Pria")){
                rbPria.setChecked(true);
            }
            else if( JK != null && genders.equals("Wanita")){
               rbWanita.setChecked(true);
            }
            else{
                rbPria.setChecked(false);
                rbWanita.setChecked(false);
            }
            etEmail.setText(emails);
            etPhone.setText(handphones);
            etPassword.setText(passwords);
            etKonfirmasiPassword.setText(confirms);

            passwords=null;
            confirms=null;
            namas = null;
            alamats = null;
            tanggallahirs = null;
            genders = null;
            emails = null;
            handphones = null;
        }



        rbPria.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    JK="Pria";
                }
            }
        });

        rbWanita.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    JK="Wanita";
                }
            }
        });

        cbMember = (CheckBox) findViewById(R.id.cbMember);
        cbMember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cbMember.isChecked()){
                    cbMember.setVisibility(View.GONE);
                    etMember.setVisibility(View.VISIBLE);
                    ivBarcode.setVisibility(View.VISIBLE);
                }
            }
        });

        //Toast.makeText(this, BarcodeScanner.bar, Toast.LENGTH_SHORT).show();

        if(BarcodeScanner.bar != null) {
            cbMember.setVisibility(View.GONE);
            etMember.setVisibility(View.VISIBLE);
            ivBarcode.setVisibility(View.VISIBLE);
            etMember.setText(BarcodeScanner.bar);
            BarcodeScanner.bar=null;

        }

        ivBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namas = etNama.getText().toString();
                alamats = etAlamat.getText().toString();
                tanggallahirs = etTanggalLahir.getText().toString();
                genders = JK;
                passwords=etPassword.getText().toString();
                confirms=etKonfirmasiPassword.getText().toString();
                emails = etEmail.getText().toString();
                handphones = etPhone.getText().toString();
                Intent i = new Intent(RegisterActivity.this,BarcodeScanner.class);
                startActivity(i);
            }
        });



        btnRegis = (Button) findViewById(R.id.btnRegis);
        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNama.getText().toString().equals("") || etAlamat.getText().toString().equals("") || etTanggalLahir.getText().toString().equals("")
                        || etEmail.getText().toString().equals("") ||etPhone.getText().toString().equals("")|| etPassword.getText().toString().equals("") || etKonfirmasiPassword.getText().toString().equals("")
                        ){
                    builder.setMessage("Harap isi data Anda");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog = builder.show();
                }
                else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()){
                    builder.setMessage("Email tidak valid");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog = builder.show();
                }
                else if(etPassword.length() < 6 || etKonfirmasiPassword.length() <6){
                    builder.setMessage("Panjang Kata Sandi Minimal 6");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog = builder.show();
                }
                else if(!rbPria.isChecked() && !rbWanita.isChecked()){
                    builder.setMessage("Harap pilih jenis kelamin Anda");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog = builder.show();
                }
                else if(!etPassword.getText().toString().equals(etKonfirmasiPassword.getText().toString())){
                    builder.setMessage("Konfirmasi kata sandi Anda salah");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog = builder.show();
                }
                else{
                    JSONObject objRegister = new JSONObject();
                    try {
                        JSONArray arrData = new JSONArray();
                        JSONObject objDetail = new JSONObject();
                        objDetail.put("nama",etNama.getText().toString());
                        objDetail.put("alamat",etAlamat.getText().toString());
                        objDetail.put("tanggal",etTanggalLahir.getText().toString());
                        objDetail.put("jk",JK);
                        objDetail.put("email",etEmail.getText().toString());
                        objDetail.put("nomor_hp",etPhone.getText().toString());
                        objDetail.put("password",etPassword.getText().toString());
                        objDetail.put("member",etMember.getText().toString());
                        objDetail.put("id_lokasi","0");
                        arrData.put(objDetail);
                        objRegister.put("data",arrData);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, REGISTER_URL, objRegister,
                            new Response.Listener<JSONObject>() {
                                @Override

                                public void onResponse(JSONObject response) {
                                    try {
                                        if(response.getString("status").equals("OK")) {
                                            builder.setMessage("Harap pilih lokasi Apotek Century yang Anda inginkan untuk proses pengambilan barang");
                                            builder.setCancelable(false);
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    sessionCek.createCek("loign");
                                                    session.createLoginSession(etEmail.getText().toString(),"");
                                                    Intent i = new Intent(RegisterActivity.this,PetaPertama.class);
                                                    startActivity(i);
                                                    mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString());
                                                }
                                            });
                                            dialog = builder.show();
                                        }
                                        else if(response.getString("status").equals("FALSE")){
                                            builder.setMessage("Email Anda tidak tersedia");

                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

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
                                    // Toast.makeText(Register.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                                }
                            }) ;
                    RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                    requestQueue.add(stringRequest);



                    ////////////////////////////////////////////////////////////////////

                }
            }
        });
        statusCheck();
        setCurrentDateOnView();
        addListenerOnButton();
    }



    public void setCurrentDateOnView() {

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        etTanggalLahir = (EditText) findViewById(R.id.etTanggalLahir);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        // set current date into textview
        //etTanggalLahir.setText(new StringBuilder().append(day).append("-").append(month + 1).append("-").append(year).append(" "), TextView.BufferType.EDITABLE);

        // set current date into datepicker
        datePicker.init(year, month, day, null);
    }

    public void addListenerOnButton() {
        etTanggalLahir.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                showDialog(DATE_DIALOG_ID);
                return false;
            }
        });
    }




    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            etTanggalLahir.setText(new StringBuilder().append(day).append("-").append(month + 1).append("-").append(year).append(" "), TextView.BufferType.EDITABLE);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            // set selected date into datepicker also
            datePicker.init(year, month, day, null);

        }
    };

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Harap aktifkan GPS pada perangkat Anda")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        Toast.makeText(context, "Aplikasi tidak dapat diakses bila GPS tidak dinyalakan", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //LoginManager.getInstance().logOut();
//        session.cleanRegisterUser();
//        LoginManager.getInstance().logOut();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //LoginManager.getInstance().logOut();
//        session.cleanRegisterUser();
//        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
//        startActivity(i);
//        LoginManager.getInstance().logOut();
    }


    @Override
    public void onClick(View v) {

    }
}

