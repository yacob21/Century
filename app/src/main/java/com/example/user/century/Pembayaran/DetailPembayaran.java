package com.example.user.century.Pembayaran;

/**
 * Created by Administrator on 11/27/2017.
 */

public class DetailPembayaran {
    String Nama;
    int Harga;
    String Quantity;
    String Gambar;

    public DetailPembayaran(String nama,int harga,String quantity,String gambar){
        this.Nama=nama;
        this.Harga=harga;
        this.Quantity=quantity;
        this.Gambar=gambar;
    }



    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public int getHarga() {
        return Harga;
    }

    public void setHarga(int harga) {
        Harga = harga;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getGambar() {
        return Gambar;
    }

    public void setGambar(String gambar) {
        Gambar = gambar;
    }
}
