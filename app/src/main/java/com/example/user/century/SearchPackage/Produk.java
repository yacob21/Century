package com.example.user.century.SearchPackage;

/**
 * Created by yacob on 8/31/2017.
 */

public class Produk {
    public String Nama;
    public int Harga;
    String Merk;
    public String Gambar;
    String Subkategori;
    String Id_kategori;
    String Id_promosi;
    public String Id_Produk;
    public String Id_Produk_Per_Lokasi;
    String Stok;
    public String Diskon;


    public Produk(String nama, int harga, String merk, String id_kategori, String id_promosi, String subkategori, String gambar, String id_produk, String stok, String diskon, String id_Produk_Per_Lokasi){
        this.Nama = nama;
        this.Harga=harga;
        this.Merk=merk;
        this.Gambar=gambar;
        this.Subkategori=subkategori;
        this.Id_kategori=id_kategori;
        this.Id_promosi = id_promosi;
        this.Id_Produk = id_produk;
        this.Stok = stok;
        this.Diskon=diskon;
        this.Id_Produk_Per_Lokasi = id_Produk_Per_Lokasi;
    }

    public Produk(String nama, String merk, String id_kategori, String subkategori, String gambar, String id_produk, String id_Produk_Per_Lokasi){
        this.Nama = nama;
        this.Merk=merk;
        this.Gambar=gambar;
        this.Subkategori=subkategori;
        this.Id_kategori=id_kategori;
        this.Id_Produk = id_produk;
        this.Id_Produk_Per_Lokasi = id_Produk_Per_Lokasi;
    }

    public String getId_Produk_Per_Lokasi() {
        return Id_Produk_Per_Lokasi;
    }

    public void setId_Produk_Per_Lokasi(String id_Produk_Per_Lokasi) {
        Id_Produk_Per_Lokasi = id_Produk_Per_Lokasi;
    }

    public String getDiskon() {
        return Diskon;
    }

    public void setDiskon(String diskon) {
        Diskon = diskon;
    }

    public String getStok() {
        return Stok;
    }

    public void setStok(String stok) {
        Stok = stok;
    }

    public String getId_Produk() {
        return Id_Produk;
    }

    public void setId_Produk(String id_Produk) {
        Id_Produk = id_Produk;
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

    public String getMerk() {
        return Merk;
    }

    public void setMerk(String merk) {
        Merk = merk;
    }

    public String getGambar() {
        return Gambar;
    }

    public void setGambar(String gambar) {
        Gambar = gambar;
    }

    public String getSubkategori() {
        return Subkategori;
    }

    public void setSubkategori(String subkategori) {
        Subkategori = subkategori;
    }

    public String getId_kategori() {
        return Id_kategori;
    }

    public void setId_kategori(String id_kategori) {
        Id_kategori = id_kategori;
    }

    public String getId_promosi() {
        return Id_promosi;
    }

    public void setId_promosi(String id_promosi) {
        Id_promosi = id_promosi;
    }
}
