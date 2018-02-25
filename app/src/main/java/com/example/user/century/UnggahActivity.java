package com.example.user.century;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.data;

public class UnggahActivity extends AppCompatActivity {
    protected static final int SELECT_PHOTO = 100;
    protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 90;

    Uri selectedImage,imageUri;
    FirebaseStorage storage;
    StorageReference storageRef,imageRef;
    ProgressDialog progressDialog;
    UploadTask uploadTask;
    ImageView imageView;
    LinearLayout btnGallery,btnKamera,btnUnggah;
    Context context;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unggah);
        context = this;
        imageView = (ImageView) findViewById(R.id.imageView);
        btnGallery = (LinearLayout) findViewById(R.id.btnGallery);
        btnKamera = (LinearLayout) findViewById(R.id.btnKamera);
        btnUnggah = (LinearLayout) findViewById(R.id.btnUpload);

        storage = FirebaseStorage.getInstance();
        //creates a storage reference
        storageRef = storage.getReference();


        String[] PERMISSIONS = {android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, 0 );
        }


        btnUnggah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedImage != null) {
                    uploadImage(v);
                }
                else{
                    Toast.makeText(context, "Harap masukkan gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnKamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    String[] PERMISSIONS = {android.Manifest.permission.CAMERA};
                    if (!hasPermissions(context, PERMISSIONS)) {
                        ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE );
                    } else {
                        takeImage();
                    }
                } else {
                    takeImage();
                }
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                            String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            if (!hasPermissions(context, PERMISSIONS)) {
                                ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, SELECT_PHOTO );
                            } else {
                                selectImage(v);
                            }
                } else {
                    selectImage(v);
                }
            }
        });
    }
    public void selectImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }


    public void takeImage(){
        Intent i = getIntent();
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(),"picture"+i.getStringExtra("kode")+".jpg");
        imageUri = Uri.fromFile(photo);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
                if ( requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
                    Toast.makeText(UnggahActivity.this,"Image selected, click on upload button",Toast.LENGTH_SHORT).show();
                    selectedImage = imageReturnedIntent.getData();
                    Toast.makeText(context, String.valueOf(selectedImage), Toast.LENGTH_SHORT).show();
                    Picasso.with(UnggahActivity.this).load(selectedImage).into(imageView);
                }
                else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && imageReturnedIntent != null && imageReturnedIntent.getData() != null) {
                    Toast.makeText(UnggahActivity.this,"Image selected, click on upload button",Toast.LENGTH_SHORT).show();
                    selectedImage = imageReturnedIntent.getData();
                    Toast.makeText(context, String.valueOf(selectedImage), Toast.LENGTH_SHORT).show();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        imageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
    }
    public void uploadImage(View view) {
        //create reference to images folder and assing a name to the file that will be uploaded
        Intent i = getIntent();
        imageRef = storageRef.child("images/"+i.getStringExtra("kode"));
        //creating and showing progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        progressDialog.setCancelable(false);
        //starting upload
        uploadTask = imageRef.putFile(selectedImage);
        // Observe state change events such as progress, pause, and resume
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                //sets and increments value of progressbar
                progressDialog.incrementProgressBy((int) progress);
            }
        });
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(UnggahActivity.this,"Error in uploading!",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                Toast.makeText(UnggahActivity.this,"Upload successful",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
                //showing the uploaded image in ImageView using the download url
//                Picasso.with(UnggahActivity.this).load(downloadUrl).into(imageView);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
            }
            case SELECT_PHOTO:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
            }
        }

    }

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


}
