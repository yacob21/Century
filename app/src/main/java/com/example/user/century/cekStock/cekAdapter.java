package com.example.user.century.cekStock;

/**
 * Created by yacob on 10/5/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.century.produkKategori.Produk;
import com.example.user.century.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by yacob on 8/31/2017.
 */


public class cekAdapter extends RecyclerView.Adapter<cekAdapter.CekViewHolder> {
    private List<Produk> cekList;
    public int count = 0;
    Activity activity;
    public cekAdapter(List<Produk> cekList) {
        this.cekList=cekList;
    }


    @Override
    public void onBindViewHolder(final CekViewHolder cekViewHolder, final int i) {
        final Produk pr = cekList.get(i);

        cekViewHolder.tvNamaProduk.setText(pr.Nama);
        Picasso.with(activity)
                .load(pr.Gambar)
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.defaultpromosi)
                .error(R.drawable.defaultpromosi)
                .fit()
                .centerInside()
                .into(cekViewHolder.imgProduk);

        cekViewHolder.btnProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sessionProduk = new SessionProduk(view.getContext());
                Intent i = new Intent(view.getContext(),DetailCekActivity.class);
                i.putExtra("panduanIdProduk",pr.Id_Produk);
                i.putExtra("panduanNama",pr.Nama);
                view.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cekList.size();
    }

    @Override
    public cekAdapter.CekViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cek_card_view, viewGroup, false);

        return new cekAdapter.CekViewHolder(itemView);
    }

    public static class CekViewHolder extends  RecyclerView.ViewHolder {
        protected ImageView imgProduk;
        protected TextView tvNamaProduk;
        protected RelativeLayout btnProduk;
        protected CardView cardView;
        protected Activity context;

        public CekViewHolder(View v) {
            super(v);
            context  = (Activity) v.getContext();
            cardView = (CardView) v.findViewById(R.id.card_view);
            imgProduk = (ImageView) v.findViewById(R.id.imgProduk);
            tvNamaProduk = (TextView) v.findViewById(R.id.tvNamaProduk);
            btnProduk = (RelativeLayout) v.findViewById(R.id.btnProduk);
            DisplayMetrics metrics = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            int width=metrics.widthPixels;
            ViewGroup.LayoutParams params2 = cardView.getLayoutParams();
            ViewGroup.LayoutParams params = btnProduk.getLayoutParams();
            params.width = (width-10)/2;
            params2.width =  (width-10)/2;
            cardView.setLayoutParams(params2);
            btnProduk.setLayoutParams(params);
        }
    }






}
