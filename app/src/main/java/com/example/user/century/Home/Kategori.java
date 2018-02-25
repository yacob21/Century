package com.example.user.century.Home;

/**
 * Created by user on 24/08/2017.
 */

public class Kategori {
    protected Integer idKategori;
    protected String namaKategori;

    public Kategori(){

    }

    public Integer getIdKategori() {
        return idKategori;
    }

    public void setIdKategori(Integer idKategori) {
        this.idKategori = idKategori;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public void setNamaKategori(String namaKategori) {
        this.namaKategori = namaKategori;
    }

    public Kategori(Integer idKategori,String namakategori){
        this.idKategori=idKategori;
        this.namaKategori=namakategori;
    }
}
