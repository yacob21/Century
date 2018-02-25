package com.example.user.century.cekStock;

/**
 * Created by yacob on 10/5/2017.
 */

public class DetailCek {
    String Nama;
    String Alamat;
    int Harga;
    String Diskon;


    public DetailCek(String nama,String alamat,int harga,String diskon){
        this.Nama=nama;
        this.Alamat=alamat;
        this.Harga=harga;
        this.Diskon=diskon;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getAlamat() {
        return Alamat;
    }

    public void setAlamat(String alamat) {
        Alamat = alamat;
    }

    public int getHarga() {
        return Harga;
    }

    public void setHarga(int harga) {
        Harga = harga;
    }

    public String getDiskon() {
        return Diskon;
    }

    public void setDiskon(String diskon) {
        Diskon = diskon;
    }
}
