package com.example.user.century.Profil;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


import javax.net.ssl.HttpsURLConnection;

public class UbahProfilActivity extends AppCompatActivity {
    RequestQueue queue;
    SessionManagement session;
    ImageView ivProfil;
    EditText etNama,etAlamat,etTanggalLahir,etPhone;
    RadioButton rbPria,rbWanita;
    Button btnSimpan;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    DatePicker datePicker;
    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 999;
    String JK;
    Context context;
    public static final String UBAH_PROFIL_URL = "http://fransis.rawatwajah.com/century/ubahProfil.php";
    ///foto
//    private Bitmap bitmap;
//    private File destination = null;
//    private String imgPath = null;
    final int CAMERA_REQUEST = 13323;
    final int GALLERY_REQUEST = 22131;
//    public static final String UNGAH_FOTO_URL = "http://fransis.rawatwajah.com/century/ungahFoto.php";
    //////////////////////////////////////////////////////////////////////////////////

    Bitmap bitmap;

    boolean check = true;

    ProgressDialog progressDialog ;


    String ImageName = "image_name" ;

    String ImagePath = "image_path" ;

    public static final String UNGAH_FOTO_URL = "http://fransis.rawatwajah.com/century/ungahFoto.php";


    ProgressBar pbKoneksi,pb;
    Button btnRefresh;
    NestedScrollView mainLayout;
    LinearLayout koneksiLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_profil);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setTitle(Html.fromHtml("<'font color='#ffffff'>Ubah Profil</font>"));
        session = new SessionManagement(getApplicationContext());
        final HashMap<String, String> user = session.getUserDetails();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        builder = new AlertDialog.Builder(this);

        context =this;
        pb = (ProgressBar) findViewById(R.id.pb);
        pbKoneksi = (ProgressBar) findViewById(R.id.pbKoneksi);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        koneksiLayout = (LinearLayout) findViewById(R.id.koneksiLayout);
        mainLayout = (NestedScrollView) findViewById(R.id.mainLayout);

        etNama  = (EditText) findViewById(R.id.etNama);
        etAlamat = (EditText) findViewById(R.id.etAlamat);
        etTanggalLahir = (EditText) findViewById(R.id.etTanggalLahir);
        //etMember = (EditText) findViewById(R.id.etMember);
        rbPria = (RadioButton) findViewById(R.id.rbPria);
        rbWanita = (RadioButton) findViewById(R.id.rbWanita);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        ivProfil = (ImageView) findViewById(R.id.ivProfil);
        etPhone = (EditText) findViewById(R.id.etPhone);
        setCurrentDateOnView();
        addListenerOnButton();
        queue = Volley.newRequestQueue(this);
        String url13 = "http://fransis.rawatwajah.com/century/selectCustomer.php?email="+user.get(SessionManagement.KEY_EMAIL);
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
                                etNama.setText(obj.getString("Nama"));
                                if(obj.getString("Jenis_Kelamin").equals("Pria")){
                                    rbPria.setChecked(true);
                                }
                                else if(obj.getString("Jenis_Kelamin").equals("Wanita")){
                                    rbWanita.setChecked(true);
                                }
//                                if(obj.getString("Member").equals("0")|| obj.getString("Member").equals("") || obj.getString("Member").equals(null)){
//
//                                }
//                                else{
//                                    etMember.setText(obj.getString("Member"));
//                                }
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
                //Toast.makeText(DetailProduk.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
            }
        });

        queue.add(req3);



        if(rbPria.isChecked()){
            JK="Pria";
        }
        else if(rbWanita.isChecked()){
            JK="Wanita";
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


        /////////////////////////////////////////////////////////////////////
        /////  FOTO
        ///////////////////////////////////////////////////////////////////////


//
//        ivProfil.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                builder.setMessage("Bagaimana Anda ingin mengatur gambar Anda ?");
//                builder.setPositiveButton("KAMERA", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (Build.VERSION.SDK_INT >= 23) {
//                            String[] PERMISSIONS = {android.Manifest.permission.CAMERA};
//                            if (!hasPermissions(context, PERMISSIONS)) {
//                                ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, CAMERA_REQUEST );
//                            } else {
//                                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                                startActivityForResult(intent, CAMERA_REQUEST);
//                            }
//                        } else {
//                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            startActivityForResult(takePicture, CAMERA_REQUEST);
//                        }
//                    }
//                });
//                builder.setNegativeButton("GALLERY", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (Build.VERSION.SDK_INT >= 23) {
//                            String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                            if (!hasPermissions(context, PERMISSIONS)) {
//                                ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, GALLERY_REQUEST );
//                            } else {
//                                ////
//
//                                Intent intent = new Intent();
//
//                                intent.setType("image/*");
//
//                                intent.setAction(Intent.ACTION_GET_CONTENT);
//
//                                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), GALLERY_REQUEST);
//
//                            }
//                        } else {
//                                //////
//                            Intent intent = new Intent();
//
//                            intent.setType("image/*");
//
//                            intent.setAction(Intent.ACTION_GET_CONTENT);
//
//                            startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), GALLERY_REQUEST);
//
//                        }
//
//
//
//
//                    }
//                });
//                dialog = builder.show();
//            }
//        });
        ///////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////


        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNama.getText().toString().equals("") || etAlamat.getText().toString().equals("") || etTanggalLahir.getText().toString().equals("")
                        || etPhone.getText().toString().equals("")){
                    builder.setMessage("Harap isi data Anda");
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
                else{
                    JSONObject objRegister = new JSONObject();
                    try {
                        JSONArray arrData = new JSONArray();
                        JSONObject objDetail = new JSONObject();
                        objDetail.put("email",user.get(SessionManagement.KEY_EMAIL));
                        objDetail.put("nama",etNama.getText().toString());
                        objDetail.put("alamat",etAlamat.getText().toString());
                        objDetail.put("tanggal_lahir",etTanggalLahir.getText().toString());
                        objDetail.put("jenis_kelamin",JK);
                        objDetail.put("nomor_hp",etPhone.getText().toString());
                        //objDetail.put("member",etMember.getText().toString());
                        arrData.put(objDetail);
                        objRegister.put("data",arrData);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, UBAH_PROFIL_URL, objRegister,
                            new Response.Listener<JSONObject>() {
                                @Override

                                public void onResponse(JSONObject response) {
                                    try {
                                        if(response.getString("status").equals("OK")) {
                                            Toast.makeText(context, "Berhasil Ubah Profil", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(UbahProfilActivity.this,ProfilActivity.class);
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
                                    // Toast.makeText(Register.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                                }
                            }) ;
                    RequestQueue requestQueue = Volley.newRequestQueue(UbahProfilActivity.this);
                    requestQueue.add(stringRequest);

                }
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isDataConnectionAvailable(UbahProfilActivity.this) == false){
                    Toast.makeText(UbahProfilActivity.this, "Tidak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(UbahProfilActivity.this,ProfilActivity.class);
                startActivity(i);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }


    public void setCurrentDateOnView() {

        datePicker = (DatePicker) findViewById(R.id.datePicker);

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



    private Bitmap getRotatedBitmap(Bitmap source, int angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap bitmap1 = Bitmap.createBitmap(source,0,0,source.getWidth(),source.getHeight(),matrix,true);
        return bitmap1;
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case CAMERA_REQUEST:
                if(resultCode == RESULT_OK && imageReturnedIntent != null && imageReturnedIntent.getData() != null){
                  Uri uri = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        ivProfil.setImageBitmap(bitmap);

                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                }

                break;
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri uri = imageReturnedIntent.getData();

                    try {

                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        ivProfil.setImageBitmap(bitmap);

                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
                break;
        }
    }



    /*get Permissions Result*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
            }
            case GALLERY_REQUEST:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
            }
        }

    }

       /*check permissions  for marshmallow*/

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void ImageUploadToServerFunction(){

        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(UbahProfilActivity.this,"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Toast.makeText(UbahProfilActivity.this,string1,Toast.LENGTH_LONG).show();

                // Setting image as transparent after done uploading.
                ivProfil.setImageResource(android.R.color.transparent);


            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();
                final HashMap<String, String> user = session.getUserDetails();
                HashMapParams.put(ImageName, user.get(SessionManagement.KEY_EMAIL));

                HashMapParams.put(ImagePath, ConvertImage);

                String FinalData = imageProcessClass.ImageHttpRequest(UNGAH_FOTO_URL, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)

                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        if(isDataConnectionAvailable(UbahProfilActivity.this) == false){
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
        //super.onBackPressed();
        Intent i = new Intent(UbahProfilActivity.this,ProfilActivity.class);
        startActivity(i);
    }
}
