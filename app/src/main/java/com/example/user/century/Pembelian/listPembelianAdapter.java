package com.example.user.century.Pembelian;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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


public class listPembelianAdapter extends RecyclerView.Adapter<listPembelianAdapter.ListPembelianViewHolder> {
    public List<Pembelian> PembelianList;
    public int count = 0;
    Context context;

    public listPembelianAdapter(FragmentActivity activity, List<Pembelian> PembelianList) {
        this.context=activity;
        this.PembelianList=PembelianList;
    }


    @Override
    public void onBindViewHolder(final ListPembelianViewHolder listPembelianViewHolder, final int i) {

        final Pembelian p = PembelianList.get(i);
        listPembelianViewHolder.tvKode.setText(p.Kode);
        listPembelianViewHolder.tvStatus.setVisibility(View.VISIBLE);
        listPembelianViewHolder.tvStatus.setText("Transaksi "+p.Status);
        listPembelianViewHolder.tvTanggal.setText(p.Tanggal);

        listPembelianViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),DetailDaftarPembelian.class);
                i.putExtra("panduanBarcode",p.Kode);
                v.getContext().startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return PembelianList.size();
    }

    @Override
    public listPembelianAdapter.ListPembelianViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_list_pembayaran, viewGroup, false);

        return new listPembelianAdapter.ListPembelianViewHolder(itemView);
    }

    public class ListPembelianViewHolder extends  RecyclerView.ViewHolder {
        protected TextView tvTanggal;
        protected TextView tvKode;
        protected TextView tvStatus;
        protected LinearLayout btnDetail;



        public ListPembelianViewHolder(View v) {
            super(v);
            tvTanggal = (TextView) v.findViewById(R.id.tvTanggal);
            tvKode = (TextView) v.findViewById(R.id.tvKodeTransaksi);
            tvStatus = (TextView) v.findViewById(R.id.tvStatus);
            btnDetail = (LinearLayout) v.findViewById(R.id.btnDetail);


        }
    }
}