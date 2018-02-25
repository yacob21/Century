package com.example.user.century.Pembayaran;

/**
 * Created by Administrator on 11/27/2017.
 */

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


public class DetailPembayaranAdapter extends RecyclerView.Adapter<DetailPembayaranAdapter.DetailPembayaranViewHolder> {
    public List<DetailPembayaran> detailPembayaranList;
    public int count = 0;
    public DetailPembayaranAdapter(List<DetailPembayaran> detailPembayaranList) {
        this.detailPembayaranList=detailPembayaranList;
    }


    @Override
    public void onBindViewHolder(final DetailPembayaranViewHolder detailPembayaranViewHolder, final int i) {

        final DetailPembayaran dp = detailPembayaranList.get(i);
        detailPembayaranViewHolder.tvNamaProduk.setText(dp.Nama);
        String s = (String.format("%,d", dp.Harga)).replace(',', '.');
        detailPembayaranViewHolder.tvHargaProduk.setText("Rp "+s);
        detailPembayaranViewHolder.tvQty.setText(dp.Quantity);
        Picasso.with(detailPembayaranViewHolder.itemView.getContext())
                .load(dp.Gambar)
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.defaultpromosi)
                .error(R.drawable.defaultpromosi)
                .fit()
                .centerInside()
                .into(detailPembayaranViewHolder.ivProduk);
    }


    @Override
    public int getItemCount() {
        return detailPembayaranList.size();
    }

    @Override
    public DetailPembayaranAdapter.DetailPembayaranViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_detail_pembayaran_view, viewGroup, false);

        return new DetailPembayaranAdapter.DetailPembayaranViewHolder(itemView);
    }

    public class DetailPembayaranViewHolder extends  RecyclerView.ViewHolder {
        protected ImageView ivProduk;
        protected TextView tvNamaProduk;
        protected TextView tvHargaProduk;
        protected TextView tvQty;


        public DetailPembayaranViewHolder(View v) {
            super(v);
            ivProduk = (ImageView) v.findViewById(R.id.ivProduk);
            tvNamaProduk = (TextView) v.findViewById(R.id.tvNamaProduk);
            tvHargaProduk = (TextView) v.findViewById(R.id.tvHargaProduk);
            tvQty = (TextView) v.findViewById(R.id.tvQty);

        }
    }
}