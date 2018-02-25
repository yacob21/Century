package com.example.user.century;

/**
 * Created by yacob on 12/3/2017.
 */

public class ProdukTemp {
    int Id_produk_per_lokasi;
    int Qty;
    int Id;


    public ProdukTemp(int id_produk_per_lokasi,int qty){
        this.Id_produk_per_lokasi=id_produk_per_lokasi;
        this.Qty=qty;
    }

    public ProdukTemp() {

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getId_produk_per_lokasi() {
        return Id_produk_per_lokasi;
    }

    public void setId_produk_per_lokasi(int id_produk_per_lokasi) {
        Id_produk_per_lokasi = id_produk_per_lokasi;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }
}
