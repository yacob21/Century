package com.example.user.century.Pembelian;

/**
 * Created by Administrator on 11/28/2017.
 */

public class Pembayaran {
    String Tanggal;
    String Kode;
    int Harga;

    public Pembayaran(String tanggal,String kode,int harga){
        this.Tanggal = tanggal;
        this.Kode = kode;
        this.Harga = harga;
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

    public int getHarga() {
        return Harga;
    }

    public void setHarga(int harga) {
        Harga = harga;
    }
}
