package com.example.user.century.promosi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.century.produkKategori.DetailProduk;
import com.example.user.century.produkKategori.Produk;
import com.example.user.century.R;
import com.example.user.century.Session.SessionProduk;
import com.squareup.picasso.Picasso;

import java.util.List;


public class promosiAdapter extends RecyclerView.Adapter<promosiAdapter.PromosiViewHolder> {
    private List<Produk> promosiList;
    public int count = 0;
    Activity activity;
    SessionProduk sessionProduk;

    public promosiAdapter(List<Produk> promosiList) {
        this.promosiList=promosiList;
    }


    @Override
    public void onBindViewHolder(final PromosiViewHolder promosiViewHolder, final int i) {
        final Produk pr = promosiList.get(i);

        promosiViewHolder.tvNamaProduk.setText(pr.Nama);




        Picasso.with(activity)
                .load(pr.Gambar)
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.defaultpromosi)
                .error(R.drawable.defaultpromosi)
                .fit()
                .centerInside()
                .into(promosiViewHolder.imgProduk);

        promosiViewHolder.btnProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionProduk = new SessionProduk(view.getContext());
                Intent i = new Intent(view.getContext(),DetailProduk.class);
                sessionProduk.createProduk(pr.Id_Produk_Per_Lokasi  );
                //i.putExtra("panduanIdProduk",pr.Id_Produk);
                view.getContext().startActivity(i);
            }
        });

        if(pr.Diskon.equals("0") || pr.Diskon.equals(null)){
            String s = (String.format("%,d", pr.Harga)).replace(',', '.');
            promosiViewHolder.tvHargaProduk.setText("Rp "+s);
            promosiViewHolder.tvDiskon.setVisibility(View.GONE);
            promosiViewHolder.tvHargaDiskon.setVisibility(View.GONE);
            promosiViewHolder.tvHargaProduk.setPaintFlags(promosiViewHolder.tvHargaProduk.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

        }
        else{
            promosiViewHolder.tvDiskon.setVisibility(View.VISIBLE);
            promosiViewHolder.tvDiskon.setText(pr.Diskon+"%");
            String s = (String.format("%,d", pr.Harga)).replace(',', '.');
            promosiViewHolder.tvHargaProduk.setText("Rp "+s);
            promosiViewHolder.tvHargaProduk.setPaintFlags(promosiViewHolder.tvHargaProduk.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //promosiViewHolder.garis.setVisibility(View.VISIBLE);
            promosiViewHolder.tvHargaDiskon.setVisibility(View.VISIBLE);
            int diskonz = (int) (pr.Harga*Float.valueOf(pr.Diskon)/100);
            int harga = pr.Harga-diskonz;
            String diskon = (String.format("%,d", harga)).replace(',', '.');
            promosiViewHolder.tvHargaDiskon.setText("Rp "+diskon);
        }

    }


    @Override
    public int getItemCount() {
        return promosiList.size();
    }

    @Override
    public promosiAdapter.PromosiViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_promosi, viewGroup, false);

        return new promosiAdapter.PromosiViewHolder(itemView);
    }

    public static class PromosiViewHolder extends  RecyclerView.ViewHolder {
        protected ImageView imgProduk;
        protected TextView tvNamaProduk;
        protected TextView tvHargaProduk;
        protected RelativeLayout btnProduk;
        protected TextView tvDiskon;
        protected TextView tvHargaDiskon;
        protected CardView cardView;
        Activity context;
       // protected  View garis;


        public PromosiViewHolder(View v) {
            super(v);
            context  = (Activity) v.getContext();
            //garis = v.findViewById(R.id.garis);
            imgProduk = (ImageView) v.findViewById(R.id.imgProduk);
            tvHargaDiskon = (TextView) v.findViewById(R.id.tvHargaDiskon);
            tvDiskon = (TextView) v.findViewById(R.id.tvDiskon);
            tvNamaProduk = (TextView) v.findViewById(R.id.tvNamaProduk);
            tvHargaProduk = (TextView) v.findViewById(R.id.tvHargaProduk);
            btnProduk = (RelativeLayout) v.findViewById(R.id.btnProduk);
            cardView = (CardView) v.findViewById(R.id.card_view);

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
