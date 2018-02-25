package com.example.user.century;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LupaPassword extends AppCompatActivity {
    EditText etLupa;
    Button btnLupa;
    LinearLayout layout,layout2;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Lupa Kata Sandi</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etLupa = (EditText) findViewById(R.id.etLupa);
        btnLupa = (Button) findViewById(R.id.btnLupa);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        layout = (LinearLayout) findViewById(R.id.layout);
        layout2 = (LinearLayout) findViewById(R.id.layout2);



        btnLupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etLupa.getText().toString().equals("")){
                    Toast.makeText(LupaPassword.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(etLupa.getText().toString()).matches()){
                    Toast.makeText(LupaPassword.this, "Email tidak valid", Toast.LENGTH_SHORT).show();
                }
                else{
                    layout.setVisibility(View.GONE);
                    pb.setVisibility(View.VISIBLE);
                    FirebaseAuth.getInstance().sendPasswordResetEmail(etLupa.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        layout2.setVisibility(View.VISIBLE);
                                        layout.setVisibility(View.GONE);
                                        pb.setVisibility(View.GONE);
                                    }
                                    else{
                                        Toast.makeText(LupaPassword.this, "Gagal Mengirim Email", Toast.LENGTH_SHORT).show();
                                        layout.setVisibility(View.VISIBLE);
                                        pb.setVisibility(View.GONE);
                                    }
                                }
                            });
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
                finish();
        }


        return super.onOptionsItemSelected(item);
    }
}
