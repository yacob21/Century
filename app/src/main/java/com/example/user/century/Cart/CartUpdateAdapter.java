package com.example.user.century.Cart;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.century.CONFIG;
import com.example.user.century.DatabaseHandler;
import com.example.user.century.GuestLoginActivity.SessionLokasi;
import com.example.user.century.ProdukTemp;
import com.example.user.century.R;
import com.example.user.century.Session.SessionManagement;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CartUpdateAdapter extends RecyclerView.Adapter<CartUpdateAdapter.CartUpdateViewHolder> {
    public List<Cart> cartUpdateList;
    public int count = 0;
    Activity activity;
    ArrayList<String> tempId = new ArrayList<>();
    ArrayList<String> produk = new ArrayList<>();
    ArrayList<String> jumlah = new ArrayList<>();
    String cek;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    RequestQueue queue;
    SessionManagement session;
    SessionLokasi sessionLokasi;

    public CartUpdateAdapter(List<Cart> cartUpdateList) {
        this.cartUpdateList=cartUpdateList;
    }


    @Override
    public void onBindViewHolder(final CartUpdateViewHolder cartUpdateViewHolder, final int i) {

        final Cart cu = cartUpdateList.get(i);
        cartUpdateViewHolder.tvNamaProduk.setText(cu.Nama);

        String s = (String.format("%,d", cu.Harga)).replace(',', '.');
        cartUpdateViewHolder.tvHargaProduk.setText("Rp "+s);



        cartUpdateViewHolder.etQty.setText(cu.Quantity);
        cartUpdateViewHolder.etQty.setEnabled(false);
        cartUpdateViewHolder.etQty.setTextColor(Color.parseColor("#000000"));
        Picasso.with(cartUpdateViewHolder.itemView.getContext())
                .load(cu.Gambar)
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.defaultpromosi)
                .error(R.drawable.defaultpromosi)
                .fit()
                .centerInside()
                .into(cartUpdateViewHolder.ivProduk);



        cartUpdateViewHolder.ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(CONFIG.CekGuest.equals("tidak")){
                    builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Anda yakin ingin menghapus "+cartUpdateViewHolder.tvNamaProduk.getText().toString()+"?");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tempId.add(cu.Id);
                            produk.add(cu.Nama);
                            jumlah.add(cu.Quantity);
                            removeAt(i);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog = builder.show();
                }
                else if (CONFIG.CekGuest.equals("ya")) {
                final DatabaseHandler db = new DatabaseHandler(cartUpdateViewHolder.itemView.getContext());
                final List<ProdukTemp> produkTemp = db.getAllProdukTemp();
                        builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("Anda yakin ingin menghapus " + cartUpdateViewHolder.tvNamaProduk.getText().toString() + "?");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeAt(i);
                                db.deleteProdukTemp(produkTemp.get(i));
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog = builder.show();
                    }
            }
        });


        cartUpdateViewHolder.ivTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                queue = Volley.newRequestQueue(v.getContext());
                session = new SessionManagement(v.getContext());
                sessionLokasi = new SessionLokasi(v.getContext());
                final HashMap<String, String> user = session.getUserDetails();
                final HashMap<String, String> lokasi = sessionLokasi.getLokasiDetails();

        if(CONFIG.CekGuest.equals("tidak")){

            String url1 = "http://fransis.rawatwajah.com/century/selectCustomer.php?email="+user.get(SessionManagement.KEY_EMAIL);
            JsonObjectRequest req = new JsonObjectRequest(url1, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray users = null;
                            try {
                                users = response.getJSONArray("result");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            for (int i = 0; i < users.length(); i++) {
                                try {
                                    JSONObject obj = users.getJSONObject(i);
                                    String url1 = "http://fransis.rawatwajah.com/century/getDisplayStok.php?id_produk_per_lokasi="+cu.Id_Produk_Per_Lokasi+"&id_lokasi="+obj.getString("Id_Lokasi");
                                    JsonObjectRequest req = new JsonObjectRequest(url1, null,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    JSONArray users = null;
                                                    try {
                                                        users = response.getJSONArray("result");
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    for (int i = 0; i < users.length(); i++) {
                                                        try {
                                                            JSONObject obj = users.getJSONObject(i);
                                                            int qty = Integer.valueOf(cartUpdateViewHolder.etQty.getText().toString());
                                                            if (!cartUpdateViewHolder.etQty.getText().toString().equals("99")  && !cartUpdateViewHolder.etQty.getText().toString().equals(obj.getString("Display_Stock"))) {
                                                                cartUpdateViewHolder.etQty.setText(String.valueOf(qty+=1));
                                                            }
                                                            if(cartUpdateViewHolder.etQty.getText().toString().equals(obj.getString("Display_Stock")) || obj.getString("Display_Stock").equals("0")){
                                                                cartUpdateViewHolder.ivTambah.setEnabled(false);
                                                                builder = new AlertDialog.Builder(v.getContext());
                                                                builder.setMessage("Stok Tidak Mencukupi");
                                                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        cartUpdateViewHolder.ivTambah.setEnabled(true);
                                                                    }
                                                                });
                                                                dialog = builder.show();
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }

                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(v.getContext(),"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                                        }
                                    });

                                    queue.add(req);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                }
            });

            queue.add(req);
        }
        else if(CONFIG.CekGuest.equals("ya")){
            final DatabaseHandler db = new DatabaseHandler(cartUpdateViewHolder.itemView.getContext());
            final List<ProdukTemp> produkTemp = db.getAllProdukTemp();
            String url1 = "http://fransis.rawatwajah.com/century/getDisplayStok.php?id_produk_per_lokasi="+produkTemp.get(i).getId_produk_per_lokasi()+"&id_lokasi="+lokasi.get(SessionLokasi.KEY_ID_LOKASI);
            JsonObjectRequest req = new JsonObjectRequest(url1, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray users = null;
                            try {
                                users = response.getJSONArray("result");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            for (int i = 0; i < users.length(); i++) {
                                try {
                                    JSONObject obj = users.getJSONObject(i);
                                    int qty = Integer.valueOf(cartUpdateViewHolder.etQty.getText().toString());
                                    if (!cartUpdateViewHolder.etQty.getText().toString().equals("99")  && !cartUpdateViewHolder.etQty.getText().toString().equals(obj.getString("Display_Stock"))) {
                                        cartUpdateViewHolder.etQty.setText(String.valueOf(qty+=1));
                                        db.updateProdukTemp(new ProdukTemp(produkTemp.get(i).getId_produk_per_lokasi(),Integer.valueOf(cartUpdateViewHolder.etQty.getText().toString())));
                                    }
                                    if(cartUpdateViewHolder.etQty.getText().toString().equals(obj.getString("Display_Stock")) || obj.getString("Display_Stock").equals("0")){
                                        cartUpdateViewHolder.ivTambah.setEnabled(false);
                                        builder = new AlertDialog.Builder(v.getContext());
                                        builder.setMessage("Stok Tidak Mencukupi");
                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                cartUpdateViewHolder.ivTambah.setEnabled(true);
                                            }
                                        });
                                        dialog = builder.show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(v.getContext(),"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
                }
            });

            queue.add(req);
        }


            }
        });
        cartUpdateViewHolder.ivKurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CONFIG.CekGuest.equals("tidak")) {
                    if (!cartUpdateViewHolder.etQty.getText().toString().equals("1")) {
                        int qty = Integer.valueOf(cartUpdateViewHolder.etQty.getText().toString());
                        cartUpdateViewHolder.etQty.setText(String.valueOf(qty -= 1));
                    }
                }
                else if(CONFIG.CekGuest.equals("ya")){
                    final DatabaseHandler db = new DatabaseHandler(cartUpdateViewHolder.itemView.getContext());
                    final List<ProdukTemp> produkTemp = db.getAllProdukTemp();
                    if (!cartUpdateViewHolder.etQty.getText().toString().equals("1")) {
                        int qty = Integer.valueOf(cartUpdateViewHolder.etQty.getText().toString());
                        cartUpdateViewHolder.etQty.setText(String.valueOf(qty -= 1));
                        db.updateProdukTemp(new ProdukTemp(produkTemp.get(i).getId_produk_per_lokasi(),Integer.valueOf(cartUpdateViewHolder.etQty.getText().toString())));
                    }
                }
            }
        });



        cartUpdateViewHolder.etQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                cu.setQuantity(cartUpdateViewHolder.etQty.getText().toString());
                //notifyItemChanged(i);

            }
        });



    }


    @Override
    public int getItemCount() {
        return cartUpdateList.size();
    }

    @Override
    public CartUpdateAdapter.CartUpdateViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_card_update_view, viewGroup, false);

        return new CartUpdateAdapter.CartUpdateViewHolder(itemView);
    }

    public class CartUpdateViewHolder extends  RecyclerView.ViewHolder {
        protected ImageView ivClear;
        protected ImageView ivProduk;
        protected TextView tvNamaProduk;
        protected TextView tvHargaProduk;
        protected EditText etQty;
        protected ImageView ivKurang,ivTambah;


        public CartUpdateViewHolder(View v) {
            super(v);
            ivClear = (ImageView) v.findViewById(R.id.ivClear);
            ivProduk = (ImageView) v.findViewById(R.id.ivProduk);
            ivKurang = (ImageView) v.findViewById(R.id.ivKurang);
            ivTambah = (ImageView) v.findViewById(R.id.ivTambah);
            tvNamaProduk = (TextView) v.findViewById(R.id.tvNamaProduk);
            tvHargaProduk = (TextView) v.findViewById(R.id.tvHargaProduk);
            etQty = (EditText) v.findViewById(R.id.etQty);
        }

    }



    public void removeAt(int position){
        cartUpdateList.remove(position);
        notifyDataSetChanged();
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position,cartUpdateList.size());
}

}
