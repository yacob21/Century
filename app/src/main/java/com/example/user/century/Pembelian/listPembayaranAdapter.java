package com.example.user.century.Pembelian;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.century.Cart.Cart;
import com.example.user.century.Cart.CartActivity;
import com.example.user.century.Home.MainActivity;
import com.example.user.century.Pembayaran.DetailPembayaran;
import com.example.user.century.Pembayaran.PembayaranActivity;
import com.example.user.century.R;
import com.example.user.century.SearchPackage.SearchActivity;
import com.example.user.century.UnggahActivity;
import com.example.user.century.cekStock.CekStockActivity;
import com.example.user.century.produkKategori.ProdukKategoriActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 11/28/2017.
 */


public class listPembayaranAdapter extends RecyclerView.Adapter<listPembayaranAdapter.ListPembayaranViewHolder> {
    public List<Pembayaran> PembayaranList;
    public int count = 0;
    Context context;

    public static final String DELETE_DETAIL_PEMBAYARAN_URL="http://fransis.rawatwajah.com/century/deleteDetailPembayaran.php";
    public static final String DELETE_PEMBAYARAN_URL="http://fransis.rawatwajah.com/century/deletePembayaran.php";
    public static final String UPDATE_PEMBELIAN_URL="http://fransis.rawatwajah.com/century/updatePembelianKetikaDelete.php";
    public static final String INSERT_PEMBELIAN_URL="http://fransis.rawatwajah.com/century/insertPembelianKetikaDelete.php";
    public static final String INSERT_DETAIL_PEMBELIAN_URL="http://fransis.rawatwajah.com/century/insertDetailPembelianKetikaDelete.php";
    AlertDialog dialog;
    AlertDialog.Builder builder;


    public listPembayaranAdapter(FragmentActivity activity, List<Pembayaran> PembayaranList) {
        this.context=activity;
        this.PembayaranList=PembayaranList;
    }


    @Override
    public void onBindViewHolder(final ListPembayaranViewHolder listPembayaranViewHolder, final int i) {
        builder = new AlertDialog.Builder(listPembayaranViewHolder.itemView.getContext());
        final Pembayaran p = PembayaranList.get(i);
        listPembayaranViewHolder.tvKode.setText(p.Kode);
        String s = (String.format("%,d", p.Harga)).replace(',', '.');
        listPembayaranViewHolder.tvHarga.setText("Rp "+s);
        listPembayaranViewHolder.tvTanggal.setText(p.Tanggal);

        listPembayaranViewHolder.tvHarga.setVisibility(View.VISIBLE);


        listPembayaranViewHolder.buttonViewOption.setVisibility(View.VISIBLE);
        listPembayaranViewHolder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popup = new PopupMenu(listPembayaranViewHolder.itemView.getContext(), listPembayaranViewHolder.buttonViewOption);
                popup.inflate(R.menu.options_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {




                        switch (item.getItemId()) {
                            case R.id.btnBatal:
                                builder.setTitle("Konfirmasi Pembatalan Transaksi");
                                builder.setMessage("Tidak disarankan untuk membatalkan transaksi jika Anda sudah melakukan pembayaran.");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        JSONObject objAdd = new JSONObject();
                                        try {
                                            JSONArray arrData = new JSONArray();
                                            JSONObject objDetail = new JSONObject();
                                            objDetail.put("kode",p.Kode);
                                            arrData.put(objDetail);
                                            objAdd.put("data",arrData);
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, INSERT_PEMBELIAN_URL, objAdd,
                                                new Response.Listener<JSONObject>() {
                                                    @Override

                                                    public void onResponse(JSONObject response) {
                                                        try {
                                                            if(response.getString("status").equals("OK")) {
                                                                JSONObject objAdd = new JSONObject();
                                                                try {
                                                                    JSONArray arrData = new JSONArray();
                                                                    JSONObject objDetail = new JSONObject();
                                                                    objDetail.put("kode",p.Kode);
                                                                    arrData.put(objDetail);
                                                                    objAdd.put("data",arrData);
                                                                } catch (JSONException e1) {
                                                                    e1.printStackTrace();
                                                                }
                                                                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, UPDATE_PEMBELIAN_URL, objAdd,
                                                                        new Response.Listener<JSONObject>() {
                                                                            @Override

                                                                            public void onResponse(JSONObject response) {
                                                                                try {
                                                                                    if(response.getString("status").equals("OK")) {
                                                                                        JSONObject objAdd = new JSONObject();
                                                                                        try {
                                                                                            JSONArray arrData = new JSONArray();
                                                                                            JSONObject objDetail = new JSONObject();
                                                                                            objDetail.put("kode",p.Kode);
                                                                                            arrData.put(objDetail);
                                                                                            objAdd.put("data",arrData);
                                                                                        } catch (JSONException e1) {
                                                                                            e1.printStackTrace();
                                                                                        }
                                                                                        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, INSERT_DETAIL_PEMBELIAN_URL, objAdd,
                                                                                                new Response.Listener<JSONObject>() {
                                                                                                    @Override

                                                                                                    public void onResponse(JSONObject response) {
                                                                                                        try {
                                                                                                            if(response.getString("status").equals("OK")) {
                                                                                                                JSONObject objAdd = new JSONObject();
                                                                                                                try {
                                                                                                                    JSONArray arrData = new JSONArray();
                                                                                                                    JSONObject objDetail = new JSONObject();
                                                                                                                    objDetail.put("kode",p.Kode);
                                                                                                                    arrData.put(objDetail);
                                                                                                                    objAdd.put("data",arrData);
                                                                                                                } catch (JSONException e1) {
                                                                                                                    e1.printStackTrace();
                                                                                                                }
                                                                                                                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, DELETE_DETAIL_PEMBAYARAN_URL, objAdd,
                                                                                                                        new Response.Listener<JSONObject>() {
                                                                                                                            @Override

                                                                                                                            public void onResponse(JSONObject response) {
                                                                                                                                try {
                                                                                                                                    if(response.getString("status").equals("OK")) {
                                                                                                                                        JSONObject objAdd = new JSONObject();
                                                                                                                                        try {
                                                                                                                                            JSONArray arrData = new JSONArray();
                                                                                                                                            JSONObject objDetail = new JSONObject();
                                                                                                                                            objDetail.put("kode",p.Kode);
                                                                                                                                            arrData.put(objDetail);
                                                                                                                                            objAdd.put("data",arrData);
                                                                                                                                        } catch (JSONException e1) {
                                                                                                                                            e1.printStackTrace();
                                                                                                                                        }
                                                                                                                                        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, DELETE_PEMBAYARAN_URL, objAdd,
                                                                                                                                                new Response.Listener<JSONObject>() {
                                                                                                                                                    @Override

                                                                                                                                                    public void onResponse(JSONObject response) {
                                                                                                                                                        try {
                                                                                                                                                            if(response.getString("status").equals("OK")) {
                                                                                                                                                                removeAt(i);
//                                                                                                                                                                DaftarPembelian.cardListPembelian.getAdapter().notifyDataSetChanged();
//                                                                                                                                                                DaftarPembelian.cardListPembelian.getAdapter().notifyItemInserted(i);
                                                                                                                                                                Toast.makeText(view.getContext(), "Berhasil Membatalkan Pesanan", Toast.LENGTH_SHORT).show();

                                                                                                                                                            }
                                                                                                                                                        } catch (JSONException e1) {
                                                                                                                                                            e1.printStackTrace();
                                                                                                                                                        }

                                                                                                                                                    }
                                                                                                                                                },
                                                                                                                                                new Response.ErrorListener() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onErrorResponse(VolleyError error) {
                                                                                                                                                        Toast.makeText(view.getContext(),"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                                                                                                                                                    }
                                                                                                                                                }) ;
                                                                                                                                        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                                                                                                                                        requestQueue.add(stringRequest);

                                                                                                                                    }
                                                                                                                                } catch (JSONException e1) {
                                                                                                                                    e1.printStackTrace();
                                                                                                                                }

                                                                                                                            }
                                                                                                                        },
                                                                                                                        new Response.ErrorListener() {
                                                                                                                            @Override
                                                                                                                            public void onErrorResponse(VolleyError error) {
                                                                                                                                Toast.makeText(view.getContext(),"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                                                                                                                            }
                                                                                                                        }) ;
                                                                                                                RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                                                                                                                requestQueue.add(stringRequest);
                                                                                                            }
                                                                                                        } catch (JSONException e1) {
                                                                                                            e1.printStackTrace();
                                                                                                        }

                                                                                                    }
                                                                                                },
                                                                                                new Response.ErrorListener() {
                                                                                                    @Override
                                                                                                    public void onErrorResponse(VolleyError error) {
                                                                                                        Toast.makeText(view.getContext(),"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                                                                                                    }
                                                                                                }) ;
                                                                                        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                                                                                        requestQueue.add(stringRequest);

                                                                                    }
                                                                                } catch (JSONException e1) {
                                                                                    e1.printStackTrace();
                                                                                }

                                                                            }
                                                                        },
                                                                        new Response.ErrorListener() {
                                                                            @Override
                                                                            public void onErrorResponse(VolleyError error) {
                                                                                Toast.makeText(view.getContext(),"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                                                                            }
                                                                        }) ;
                                                                RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                                                                requestQueue.add(stringRequest);

                                                            }
                                                        } catch (JSONException e1) {
                                                            e1.printStackTrace();
                                                        }

                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(view.getContext(),"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                                                    }
                                                }) ;
                                        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                                        requestQueue.add(stringRequest);
                                    }
                                });
                                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                dialog = builder.show();
                                break;
                            case R.id.btnUnggah:
                                Intent i = new Intent(view.getContext(), UnggahActivity.class);
                                i.putExtra("kode",p.Kode);
                                view.getContext().startActivity(i);
                                break;
                        }
                        return false;

                    }
                });
                popup.show();

            }
        });


        listPembayaranViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(v.getContext(),DetailStatusPembayaran.class);
                    i.putExtra("panduanBarcode",p.Kode);
                    v.getContext().startActivity(i);
            }
        });

    }




    @Override
    public int getItemCount() {
        return PembayaranList.size();
    }

    @Override
    public listPembayaranAdapter.ListPembayaranViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_list_pembayaran, viewGroup, false);

        return new listPembayaranAdapter.ListPembayaranViewHolder(itemView);
    }

    public class ListPembayaranViewHolder extends  RecyclerView.ViewHolder {
        protected TextView tvTanggal;
        protected TextView tvKode;
        protected TextView tvHarga;
        protected ImageView buttonViewOption;
        protected LinearLayout btnDetail;



        public ListPembayaranViewHolder(View v) {
            super(v);
            tvTanggal = (TextView) v.findViewById(R.id.tvTanggal);
            tvKode = (TextView) v.findViewById(R.id.tvKodeTransaksi);
            tvHarga = (TextView) v.findViewById(R.id.tvHarga);
            btnDetail = (LinearLayout) v.findViewById(R.id.btnDetail);
            buttonViewOption = (ImageView) v.findViewById(R.id.textViewOptions);

        }






    }

    public void removeAt(int position){
        PembayaranList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,PembayaranList.size());
        StatusPembayaran.cardListPembayaran.removeViewAt(position);

    }


}