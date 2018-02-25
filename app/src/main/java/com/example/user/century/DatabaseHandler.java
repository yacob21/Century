package com.example.user.century;

/**
 * Created by yacob on 12/3/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Produk_Temp";

    // table name
    private static final String TABLE_PRODUK_TEMP = "produkTemp";

    // Table Columns names
    private static final String KEY_ID_PRODUK_PER_LOKASI = "id_produk_per_lokasi";
    private static final String KEY_QTY = "qty";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUK_TEMP_TABLE = "CREATE TABLE " + TABLE_PRODUK_TEMP + "("
                + KEY_ID_PRODUK_PER_LOKASI + " INTEGER,"
                + KEY_QTY + " INTEGER"
                + ")";
        db.execSQL(CREATE_PRODUK_TEMP_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUK_TEMP);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new
    public void addProdukTemp(ProdukTemp produkTemp) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_PRODUK_PER_LOKASI, produkTemp.getId_produk_per_lokasi());
        values.put(KEY_QTY, produkTemp.getQty());
        // Inserting Row
        db.insert(TABLE_PRODUK_TEMP, null, values);
        db.close(); // Closing database connection
    }

    // Getting single
    ProdukTemp getProdukTemp(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PRODUK_TEMP, new String[]{KEY_ID_PRODUK_PER_LOKASI,
                        KEY_QTY}, KEY_ID_PRODUK_PER_LOKASI + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ProdukTemp produkTemp = new ProdukTemp(Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)));
        return produkTemp;
    }


    public List<ProdukTemp> getAllProdukTemp() {
        List<ProdukTemp> produkTempsList = new ArrayList<ProdukTemp>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUK_TEMP;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ProdukTemp produktemp = new ProdukTemp();
                produktemp.setId_produk_per_lokasi(Integer.parseInt(cursor.getString(0)));
                produktemp.setQty(Integer.parseInt(cursor.getString(1)));
                produkTempsList.add(produktemp);
            } while (cursor.moveToNext());
        }

        return produkTempsList;
    }

    public int updateProdukTemp(ProdukTemp produkTemp) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_PRODUK_PER_LOKASI, produkTemp.getId_produk_per_lokasi());
        values.put(KEY_QTY, produkTemp.getQty());

        // updating row
        return db.update(TABLE_PRODUK_TEMP, values, KEY_ID_PRODUK_PER_LOKASI + " = ?",
                new String[]{String.valueOf(produkTemp.getId_produk_per_lokasi())});
    }


    public void deleteProdukTemp(ProdukTemp produkTemp) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUK_TEMP, KEY_ID_PRODUK_PER_LOKASI + " = ?",
                new String[]{String.valueOf(produkTemp.getId_produk_per_lokasi())});
        db.close();
    }



    public int getProdukTempCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PRODUK_TEMP;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}