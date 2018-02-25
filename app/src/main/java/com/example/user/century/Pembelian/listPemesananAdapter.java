package com.example.user.century.Pembelian;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v4.app.FragmentActivity;
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

import com.example.user.century.Pembayaran.DetailPembayaran;
import com.example.user.century.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 11/28/2017.
 */


public class listPemesananAdapter extends RecyclerView.Adapter<listPemesananAdapter.ListPemesananViewHolder> {
    public List<Pembayaran> PemesananList;
    public int count = 0;
    Context context;

    public listPemesananAdapter(FragmentActivity activity, List<Pembayaran> PemesananList) {
        this.context=activity;
        this.PemesananList=PemesananList;
    }


    @Override
    public void onBindViewHolder(final ListPemesananViewHolder listPemesananViewHolder, final int i) {

        final Pembayaran p = PemesananList.get(i);
        listPemesananViewHolder.tvKode.setText(p.Kode);
        String s = (String.format("%,d", p.Harga)).replace(',', '.');
        listPemesananViewHolder.tvHarga.setText("Rp "+s);
        listPemesananViewHolder.tvTanggal.setText(p.Tanggal);
        listPemesananViewHolder.tvHarga.setVisibility(View.VISIBLE);

        listPemesananViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),DetailStatusPemesanan.class);
                i.putExtra("panduanBarcode",p.Kode);
                v.getContext().startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return PemesananList.size();
    }

    @Override
    public listPemesananAdapter.ListPemesananViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_list_pembayaran, viewGroup, false);

        return new listPemesananAdapter.ListPemesananViewHolder(itemView);
    }

    public class ListPemesananViewHolder extends  RecyclerView.ViewHolder {
        protected TextView tvTanggal;
        protected TextView tvKode;
        protected TextView tvHarga;
        protected ImageView buttonViewOption;
        protected LinearLayout btnDetail;



        public ListPemesananViewHolder(View v) {
            super(v);
            tvTanggal = (TextView) v.findViewById(R.id.tvTanggal);
            tvKode = (TextView) v.findViewById(R.id.tvKodeTransaksi);
            tvHarga = (TextView) v.findViewById(R.id.tvHarga);
            btnDetail = (LinearLayout) v.findViewById(R.id.btnDetail);
            buttonViewOption = (ImageView) v.findViewById(R.id.textViewOptions);

        }
    }
}