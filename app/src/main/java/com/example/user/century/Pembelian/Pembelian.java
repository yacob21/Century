package com.example.user.century.Pembelian;

/**
 * Created by Administrator on 11/29/2017.
 */

public class Pembelian {
    String Tanggal;
    String Kode;
    String Status;

    public Pembelian(String tanggal,String kode,String status){
        this.Tanggal = tanggal;
        this.Kode = kode;
        this.Status = status;


    }

    public String getTanggal() {
        return Tanggal;
    }

    public void setTanggal(String tanggal) {
        Tanggal = tanggal;
    }

    public String getKode() {
        return Kode;
    }

    public void setKode(String kode) {
        Kode = kode;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
