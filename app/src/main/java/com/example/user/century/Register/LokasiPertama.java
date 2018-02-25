package com.example.user.century.Register;

/**
 * Created by yacob on 8/28/2017.
 */

public class LokasiPertama {
    public String Nama;
    public String Alamat;
    public int Id;
    public double Longitude;
    public double Latitude;
    public double Jarak;

    public LokasiPertama(){

    }
    public LokasiPertama(String nama,String alamat,int id,double latitude,double longitude,double jarak){
        this.Nama=nama;
        this.Alamat=alamat;
        this.Id=id;
        this.Latitude=latitude;
        this.Longitude=longitude;
        this.Jarak=jarak;
    }

    public double getJarak() {
        return Jarak;
    }

    public void setJarak(double jarak) {
        Jarak = jarak;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
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

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
