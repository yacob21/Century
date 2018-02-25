package com.example.user.century.SearchPackage;

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

import com.example.user.century.R;
import com.example.user.century.Session.SessionProduk;
import com.example.user.century.Session.SessionSearch;
import com.example.user.century.produkKategori.DetailProduk;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yacob on 8/31/2017.
 */


public class searchAdapter extends RecyclerView.Adapter<com.example.user.century.SearchPackage.searchAdapter.SearchViewHolder> {
    private List<Produk> searchList;
    public int count = 0;
    Activity activity;
    SessionProduk sessionProduk;
    SessionSearch sessionSearch;


    public searchAdapter(List<Produk> searchList) {
        this.searchList=searchList;
    }


    @Override
    public void onBindViewHolder(final SearchViewHolder searchViewHolder, final int i) {
        final Produk pr = searchList.get(i);

        searchViewHolder.tvNamaProduk.setText(pr.Nama);

        if(pr.Diskon.equals("0")){
//            if(Integer.valueOf(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 2)) >= 50){
//                String s = (String.format("%,d", pr.Harga)).replace(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 2),"00").replace(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 3),String.valueOf(Integer.valueOf(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 3))+100)).replace(',', '.');
//                searchViewHolder.tvHargaProduk.setText("Rp "+s);
//            }
//            else if(Integer.valueOf(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 2)) < 50){
//                String s = (String.format("%,d", pr.Harga)).replace(',', '.').replace(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 2),"00");
//                searchViewHolder.tvHargaProduk.setText("Rp "+s);
//            }
//            else{
                String s = (String.format("%,d", pr.Harga)).replace(',', '.');
                searchViewHolder.tvHargaProduk.setText("Rp "+s);
//            }
            searchViewHolder.tvDiskon.setVisibility(View.GONE);
            searchViewHolder.tvHargaDiskon.setVisibility(View.GONE);
            searchViewHolder.tvHargaProduk.setPaintFlags(searchViewHolder.tvHargaProduk.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

        }
        else{
            searchViewHolder.tvDiskon.setVisibility(View.VISIBLE);
            searchViewHolder.tvDiskon.setText(pr.Diskon+"%");
//            if(Integer.valueOf(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 2)) >= 50){
//                String s = (String.format("%,d", pr.Harga)).replace(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 2),"00").replace(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 3),String.valueOf(Integer.valueOf(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 3))+100)).replace(',', '.');
//                searchViewHolder.tvHargaProduk.setText("Rp "+s);
//            }
//            else if(Integer.valueOf(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 2)) < 50){
//                String s = (String.format("%,d", pr.Harga)).replace(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 2),"00").replace(',', '.');
//                searchViewHolder.tvHargaProduk.setText("Rp "+s);
//            }
//            else{
                String s = (String.format("%,d", pr.Harga)).replace(',', '.');
                searchViewHolder.tvHargaProduk.setText("Rp "+s);
//            }
            searchViewHolder.tvHargaProduk.setPaintFlags( searchViewHolder.tvHargaProduk.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            searchViewHolder.tvHargaDiskon.setVisibility(View.VISIBLE);
            int diskonz = (int) (pr.Harga*Float.valueOf(pr.Diskon)/100);
            int harga = pr.Harga-diskonz;
            String diskon = (String.format("%,d", harga));
//            if(Integer.valueOf(diskon.substring(diskon.length() - 2)) >= 50 ){
//                searchViewHolder.tvHargaDiskon.setText("Rp "+diskon.replace(diskon.substring(diskon.length()-3),String.valueOf(Integer.valueOf(diskon.substring(diskon.length()-3))+100)).replace(diskon.substring(diskon.length()-2),"00").replace(',', '.'));
//            }
//            else if(Integer.valueOf(diskon.substring(diskon.length() - 2)) < 50){
//                searchViewHolder.tvHargaDiskon.setText("Rp "+diskon.replace(diskon.substring(diskon.length()-2),"00").replace(',', '.'));
//            }
//            else{
                searchViewHolder.tvHargaDiskon.setText("Rp "+diskon.replace(',', '.'));
//            }
        }


        Picasso.with(searchViewHolder.itemView.getContext())
                .load(pr.Gambar)
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.defaultpromosi)
                .error(R.drawable.defaultpromosi)
                .fit()
                .centerInside()
                .into(searchViewHolder.imgProduk);

        searchViewHolder.btnProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterSearchActivity.minSearch = null;
                sessionProduk = new SessionProduk(view.getContext());
                sessionSearch = new SessionSearch(view.getContext());
                HashMap<String, String> search = sessionSearch.getSearchDetails();
                Intent i = new Intent(view.getContext(),DetailProduk.class);
                sessionProduk.createProduk(pr.Id_Produk_Per_Lokasi);
                i.putExtra("riwayatsearch",search.get(sessionSearch.KEY_SEARCH));
                view.getContext().startActivity(i);

            }
        });


    }


    @Override
    public int getItemCount() {
        return searchList.size();
    }

    @Override
    public com.example.user.century.SearchPackage.searchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_card_view, viewGroup, false);

        return new com.example.user.century.SearchPackage.searchAdapter.SearchViewHolder(itemView);
    }

    public static class SearchViewHolder extends  RecyclerView.ViewHolder {
        protected ImageView imgProduk;
        protected TextView tvNamaProduk;
        protected TextView tvHargaProduk;
        protected TextView tvDiskon;
        protected TextView tvHargaDiskon;
        protected RelativeLayout btnProduk;
        protected CardView cardView;
        Activity context;
//        protected  View garis;


        public SearchViewHolder(View v) {
            super(v);
//            garis = v.findViewById(R.id.garis);
            context  = (Activity) v.getContext();
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
