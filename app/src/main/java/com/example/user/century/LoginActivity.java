package com.example.user.century;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.user.century.Home.MainActivity;
import com.example.user.century.Register.RegisterActivity;
import com.example.user.century.Session.SessionCek;
import com.example.user.century.Session.SessionManagement;
import com.example.user.century.slider.FirstSlider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername,etPass;
    Button btnLogin,btnRegister;
    SessionManagement session;
    TextView tvLupa;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    ProgressBar pb;
    RequestQueue queue;
    String user,pass;
    String namafb,emailfb,genderfb;
    String emailSQL;

    public static final String LOGIN_URL = "http://fransis.rawatwajah.com/century/login.php";
    public static final String LOGIN_FB = "http://fransis.rawatwajah.com/century/cekPassword.php";

    public static String cekFB = null;

    private FirebaseAuth mAuth;
    SessionCek sessionCek;
    //////////facebook
//    CallbackManager callbackManager;
//    LoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        callbackManager = CallbackManager.Factory.create();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        queue = Volley.newRequestQueue(this);
        mAuth = FirebaseAuth.getInstance();
        session = new SessionManagement(getApplicationContext());
        sessionCek = new SessionCek(getApplicationContext());
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPass = (EditText) findViewById(R.id.etPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        tvLupa= (TextView) findViewById(R.id.tvLupa);
        SpannableString content = new SpannableString("Lupa Kata Sandi?");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tvLupa.setText(content);

        tvLupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,LupaPassword.class);
                startActivity(i);
            }
        });

        builder = new AlertDialog.Builder(this);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn(etUsername.getText().toString(), etPass.getText().toString());
            }
        });


    }





    @Override
    public void onBackPressed() {
//        builder.setMessage("Ingin keluar dari aplikasi ?");
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                startActivity(intent);
//                return;
//            }
//        });
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
////        dialog = builder.show();
        Intent i = new Intent(LoginActivity.this, FirstSlider.class);
        startActivity(i);
    }




    private void signIn(String email, String password) {
        pb.setVisibility(View.VISIBLE);
        if (email.equals("") || password.equals("")) {
            builder.setMessage("Email atau Password tidak boleh kosong");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    pb.setVisibility(View.GONE);
                }
            });
            dialog = builder.show();
        }
        else{
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startService(new Intent(LoginActivity.this, ServiceCentury.class));
                                sessionCek.createCek("login");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Email atau password salah", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                                pb.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            session.createLoginSession(etUsername.getText().toString(), "");
        } else {

        }
    }

}
