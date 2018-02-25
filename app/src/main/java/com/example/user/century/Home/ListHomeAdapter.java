package com.example.user.century.Home;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.century.R;
import com.example.user.century.Session.SessionKategori;
import com.example.user.century.produkKategori.ProdukKategoriActivity;

import java.util.List;


public class ListHomeAdapter extends RecyclerView.Adapter<ListHomeAdapter.KategoriViewHolder> {
    private List<Kategori> kategoriList;
    public int count = 0;
    SessionKategori sessionKategori;

    public ListHomeAdapter(List<Kategori> kategoriList) {
        this.kategoriList=kategoriList;
    }


    @Override
    public void onBindViewHolder(final KategoriViewHolder kategoriViewHolder, final int i) {

        final Kategori pi = kategoriList.get(i);
        kategoriViewHolder.tvKategori.setText(pi.namaKategori);

        if(pi.namaKategori.equals("Personal Care")){
            kategoriViewHolder.imgKategori.setImageResource(R.drawable.care);
        }
        else if(pi.namaKategori.equals("Supplement")){
            kategoriViewHolder.imgKategori.setImageResource(R.drawable.supplement);
        }
        else if(pi.namaKategori.equals("Men")){
            kategoriViewHolder.imgKategori.setImageResource(R.drawable.male);
        }
        else if(pi.namaKategori.equals("Women")){
            kategoriViewHolder.imgKategori.setImageResource(R.drawable.female);
        }
        else if(pi.namaKategori.equals("Misc")){
            kategoriViewHolder.imgKategori.setImageResource(R.drawable.misc);
        }
        else if(pi.namaKategori.equals("Healthcare")){
            kategoriViewHolder.imgKategori.setImageResource(R.drawable.healthcare);
        }
        else if(pi.namaKategori.equals("Medicine")){
            kategoriViewHolder.imgKategori.setImageResource(R.drawable.medicine);
        }
        else if(pi.namaKategori.equals("Cosmetic")){
            kategoriViewHolder.imgKategori.setImageResource(R.drawable.cosmetic);
        }

        kategoriViewHolder.btnKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionKategori = new SessionKategori(view.getContext());
                sessionKategori.createKategori(pi.namaKategori);
                Intent i = new Intent(view.getContext(),ProdukKategoriActivity.class);
//                i.putExtra("panduanId",pi.idKategori);
//                i.putExtra("panduanNama",pi.namaKategori);
                view.getContext().startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return kategoriList.size();
    }

    @Override
    public ListHomeAdapter.KategoriViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_card_view, viewGroup, false);
        return new ListHomeAdapter.KategoriViewHolder(itemView);
    }

    public static class KategoriViewHolder extends  RecyclerView.ViewHolder {
        protected ImageView imgKategori;
        protected TextView tvKategori;
        protected LinearLayout btnKategori;
        protected CardView cardView;
        Activity context;

        public KategoriViewHolder(View v) {
            super(v);

            cardView = (CardView) v.findViewById(R.id.card_view);
            tvKategori = (TextView) v.findViewById(R.id.tvKategori);;
            btnKategori = (LinearLayout) v.findViewById(R.id.btnKategori);
            imgKategori = (ImageView) v.findViewById(R.id.imgKategori);

            context  = (Activity) v.getContext();
            Resources r = v.getResources();
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, r.getDisplayMetrics());
            DisplayMetrics metrics = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width=metrics.widthPixels;
            int height=metrics.heightPixels;
            ViewGroup.LayoutParams params2 = cardView.getLayoutParams();
            ViewGroup.LayoutParams params = btnKategori.getLayoutParams();
            params.height = (height+100)/6;
            params.width = (width-50)/3;
            params2.height = (height+100)/6;
            params2.width =  (width-50)/3;
            btnKategori.setLayoutParams(params);
            cardView.setLayoutParams(params2);
        }

    }
}
