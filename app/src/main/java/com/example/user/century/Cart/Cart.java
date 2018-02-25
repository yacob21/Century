package com.example.user.century.Cart;

/**
 * Created by yacob on 9/7/2017.
 */

public class Cart {
    String Nama;
    int Harga;
    String Quantity;
    String Gambar;
    String Id;
    String Id_Produk_Per_Lokasi;

    public Cart(){
    }

    public Cart(String nama,int harga,String quantity,String gambar,String id,String id_Produk_Per_Lokasi){
        this.Nama=nama;
        this.Harga=harga;
        this.Quantity=quantity;
        this.Gambar=gambar;
        this.Id=id;
        this.Id_Produk_Per_Lokasi=id_Produk_Per_Lokasi;
    }

    public String getId_Produk_Per_Lokasi() {
        return Id_Produk_Per_Lokasi;
    }

    public void setId_Produk_Per_Lokasi(String id_Produk_Per_Lokasi) {
        Id_Produk_Per_Lokasi = id_Produk_Per_Lokasi;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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
