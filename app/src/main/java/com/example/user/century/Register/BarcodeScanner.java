package com.example.user.century.Register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.user.century.R;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class BarcodeScanner extends AppCompatActivity  implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    static String bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        mScannerView = new ZBarScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(BarcodeScanner.this);
        mScannerView.startCamera();

    }


    @Override
    public void handleResult(Result rawResult) {
        bar=rawResult.getContents().toString();
        //finish();
//        Toast.makeText(this, rawResult.getContents().toString(), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(BarcodeScanner.this,RegisterActivity.class);
        //session.createRegisterSession(etNama.getText().toString(), etAlamat.getText().toString(),etTanggalLahir.getText().toString(),JK,etEmail.getText().toString(),etPhone.getText().toString(),etPassword.getText().toString(),etMember.getText().toString());
       // i.putExtra("panduanBarcode",rawResult.getContents().toString());
        startActivity(i);
        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
}
