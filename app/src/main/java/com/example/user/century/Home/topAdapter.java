package com.example.user.century.Home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.century.R;
import com.example.user.century.Session.SessionProduk;
import com.example.user.century.produkKategori.DetailProduk;
import com.squareup.picasso.Picasso;

import java.util.List;


public class topAdapter extends RecyclerView.Adapter<topAdapter.TopViewHolder> {
    private List<Top> topList;
    public int count = 0;
    Activity activity;
    SessionProduk sessionProduk;

    public topAdapter(List< Top> topList) {
        this.topList=topList;
    }


    @Override
    public void onBindViewHolder(final TopViewHolder topViewHolder, final int i) {
        final Top to = topList.get(i);

        topViewHolder.tvNamaProduk.setText(to.Nama);

        if(to.Diskon.equals("0")){
            String s = (String.format("%,d", to.Harga)).replace(',', '.');
            topViewHolder.tvHargaProduk.setText("Rp "+s);
            topViewHolder.tvDiskon.setVisibility(View.GONE);
            topViewHolder.tvHargaDiskon.setVisibility(View.GONE);
            topViewHolder.tvHargaProduk.setPaintFlags(topViewHolder.tvHargaProduk.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

        }
        else{
            topViewHolder.tvDiskon.setVisibility(View.VISIBLE);
            topViewHolder.tvDiskon.setText(to.Diskon+"%");
            String s = (String.format("%,d", to.Harga)).replace(',', '.');
            topViewHolder.tvHargaProduk.setText("Rp "+s);
            topViewHolder.tvHargaProduk.setPaintFlags( topViewHolder.tvHargaProduk.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            topViewHolder.tvHargaDiskon.setVisibility(View.VISIBLE);
            int diskonz = (int) (to.Harga*Float.valueOf(to.Diskon)/100);
            int harga = to.Harga-diskonz;
            String diskon = (String.format("%,d", harga));
            topViewHolder.tvHargaDiskon.setText("Rp "+diskon.replace(',', '.'));
        }




        Picasso.with( topViewHolder.itemView.getContext())
                .load(to.Gambar)
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.defaultpromosi)
                .error(R.drawable.defaultpromosi)
                .fit()
                .centerInside()
                .into( topViewHolder.imgProduk);

        topViewHolder.btnProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionProduk = new SessionProduk(view.getContext());
                Intent i = new Intent(view.getContext(), DetailProduk.class);
                sessionProduk.createProduk(to.Id_Produk_Per_Lokasi);
                view.getContext().startActivity(i);
            }
        });


    }


    @Override
    public int getItemCount() {
        return topList.size();
    }

    @Override
    public topAdapter.TopViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rekomendasi_card_view, viewGroup, false);

        return new topAdapter.TopViewHolder(itemView);
    }

    public class TopViewHolder extends  RecyclerView.ViewHolder {
        protected ImageView imgProduk;
        protected TextView tvNamaProduk;
        protected TextView tvHargaProduk;
        protected RelativeLayout btnProduk;
        protected TextView tvDiskon;
        protected TextView tvHargaDiskon;

        public TopViewHolder(View v) {
            super(v);
            imgProduk = (ImageView) v.findViewById(R.id.imgProduk);
            tvHargaDiskon = (TextView) v.findViewById(R.id.tvHargaDiskon);
            tvDiskon = (TextView) v.findViewById(R.id.tvDiskon);
            tvNamaProduk = (TextView) v.findViewById(R.id.tvNamaProduk);
            tvHargaProduk = (TextView) v.findViewById(R.id.tvHargaProduk);
            btnProduk = (RelativeLayout) v.findViewById(R.id.btnProduk);
        }
    }






}
