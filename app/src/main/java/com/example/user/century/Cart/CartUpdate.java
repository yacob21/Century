package com.example.user.century.Cart;


public class CartUpdate {
    String Nama;
    int Harga;
    String Quantity;
    String Gambar;
    String Id;

    public CartUpdate(){
    }

    public CartUpdate(String nama,int harga,String quantity,String gambar,String id){
        this.Nama=nama;
        this.Harga=harga;
        this.Quantity=quantity;
        this.Gambar=gambar;
        this.Id=id;
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
