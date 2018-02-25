package com.example.user.century.Cart;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.century.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    public List<Cart> cartList;
    public int count = 0;
    Activity activity;
    public CartAdapter(List<Cart> cartList) {
        this.cartList=cartList;
    }


    @Override
    public void onBindViewHolder(final CartViewHolder cartViewHolder, final int i) {

        final Cart ct = cartList.get(i);
        cartViewHolder.tvNamaProduk.setText(ct.Nama);
        String s = (String.format("%,d", ct.Harga)).replace(',', '.');
        cartViewHolder.tvHargaProduk.setText("Rp "+s);
        cartViewHolder.tvQty.setText(ct.Quantity);
        Picasso.with(cartViewHolder.itemView.getContext())
                .load(ct.Gambar)
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.defaultpromosi)
                .error(R.drawable.defaultpromosi)
                .fit()
                .centerInside()
                .into(cartViewHolder.ivProduk);


    }


    @Override
    public int getItemCount() {
        return cartList.size();
    }

    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_card_view, viewGroup, false);

        return new CartAdapter.CartViewHolder(itemView);
    }

    public class CartViewHolder extends  RecyclerView.ViewHolder {
        protected ImageView ivProduk;
        protected TextView tvNamaProduk;
        protected TextView tvHargaProduk;
        protected TextView tvQty;


        public CartViewHolder(View v) {
            super(v);
            ivProduk = (ImageView) v.findViewById(R.id.ivProduk);
            tvNamaProduk = (TextView) v.findViewById(R.id.tvNamaProduk);
            tvHargaProduk = (TextView) v.findViewById(R.id.tvHargaProduk);
            tvQty = (TextView) v.findViewById(R.id.tvQty);

        }
    }
}