package com.example.user.century.produkKategori;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.century.R;
import com.example.user.century.Session.SessionProduk;
import com.example.user.century.brandAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;



public class produkAdapter extends RecyclerView.Adapter<produkAdapter.ProdukViewHolder> {
    private List<Produk> produkList;
    public int count = 0;
    Activity activity;
    SessionProduk sessionProduk;

    public produkAdapter(List<Produk> produkList) {
        this.produkList=produkList;
    }


    @Override
    public void onBindViewHolder(final ProdukViewHolder produkViewHolder, final int i) {
        final Produk pr = produkList.get(i);

        produkViewHolder.tvNamaProduk.setText(pr.Nama);


        if(pr.Diskon.equals("0")){

//            if(Integer.valueOf(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 2)) >= 50){
//                String s = (String.format("%,d", pr.Harga)).replace(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 2),"00").replace(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 3),String.valueOf(Integer.valueOf(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 3))+100)).replace(',', '.');
//                produkViewHolder.tvHargaProduk.setText("Rp "+s);
//            }
//            else if(Integer.valueOf(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 2)) < 50){
//                String s = (String.format("%,d", pr.Harga)).replace(String.valueOf(pr.Harga).subSequence(String.valueOf(pr.Harga).length() - 2,String.valueOf(pr.Harga).length() - 1),"00").replace(',', '.');
//                produkViewHolder.tvHargaProduk.setText("Rp "+s);
//            }
//            else{
                String s = (String.format("%,d", pr.Harga)).replace(',', '.');
                produkViewHolder.tvHargaProduk.setText("Rp "+s);
//            }


            produkViewHolder.tvDiskon.setVisibility(View.GONE);
            produkViewHolder.tvHargaDiskon.setVisibility(View.GONE);
            produkViewHolder.tvHargaProduk.setPaintFlags(produkViewHolder.tvHargaProduk.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

        }
        else{
            produkViewHolder.tvDiskon.setVisibility(View.VISIBLE);
            produkViewHolder.tvDiskon.setText(pr.Diskon+"%");
//            if(Integer.valueOf(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 2)) >= 50){
//                String s = (String.format("%,d", pr.Harga)).replace(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 2),"00").replace(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 3),String.valueOf(Integer.valueOf(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 3))+100)).replace(',', '.');
//                produkViewHolder.tvHargaProduk.setText("Rp "+s);
//            }
//            else if(Integer.valueOf(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 2)) < 50){
//                String s = (String.format("%,d", pr.Harga)).replace(String.valueOf(pr.Harga).substring(String.valueOf(pr.Harga).length() - 2),"00").replace(',', '.');
//                produkViewHolder.tvHargaProduk.setText("Rp "+s);
//            }
//            else{
                String s = (String.format("%,d", pr.Harga)).replace(',', '.');
                produkViewHolder.tvHargaProduk.setText("Rp "+s);
//            }

            produkViewHolder.tvHargaProduk.setPaintFlags( produkViewHolder.tvHargaProduk.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            produkViewHolder.tvHargaDiskon.setVisibility(View.VISIBLE);


                  int diskonz = (int) (pr.Harga*Float.valueOf(pr.Diskon)/100);
            int harga = pr.Harga-diskonz;
            String diskon = (String.format("%,d", harga));
//            if(Integer.valueOf(diskon.substring(diskon.length() - 2)) >= 50 ){
//                produkViewHolder.tvHargaDiskon.setText("Rp "+diskon.replace(diskon.substring(diskon.length()-3),String.valueOf(Integer.valueOf(diskon.substring(diskon.length()-3))+100)).replace(diskon.substring(diskon.length()-2),"00").replace(',', '.'));
//            }
//            else if(Integer.valueOf(diskon.substring(diskon.length() - 2)) < 50){
//                produkViewHolder.tvHargaDiskon.setText("Rp "+diskon.replace(diskon.substring(diskon.length()-2),"00").replace(',', '.'));
//            }
//            else{
                produkViewHolder.tvHargaDiskon.setText("Rp "+diskon.replace(',', '.'));
//            }




        }




        Picasso.with(produkViewHolder.itemView.getContext())
                    .load(pr.Gambar)
                    .config(Bitmap.Config.RGB_565)
                    .placeholder(R.drawable.defaultpromosi)
                    .error(R.drawable.defaultpromosi)
                    .fit()
                    .centerInside()
                    .into(produkViewHolder.imgProduk);

        produkViewHolder.btnProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    brandAdapter.tempBrand = null;
                    FilterActivity.min = null;
                    sessionProduk = new SessionProduk(view.getContext());
                    Intent i = new Intent(view.getContext(), DetailProduk.class);
                    sessionProduk.createProduk(pr.Id_Produk_Per_Lokasi);
                    //i.putExtra("panduanIdProduk",pr.Id_Produk);
                    view.getContext().startActivity(i);
            }
        });


    }


    @Override
    public int getItemCount() {
        return produkList.size();
    }

    @Override
    public produkAdapter.ProdukViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.produk_card_view, viewGroup, false);

        return new produkAdapter.ProdukViewHolder(itemView);
    }

    public static class ProdukViewHolder extends  RecyclerView.ViewHolder {
        protected ImageView imgProduk;
        protected TextView tvNamaProduk;
        protected TextView tvHargaProduk;

        protected TextView tvDiskon;
        protected TextView tvHargaDiskon;
        protected RelativeLayout btnProduk;
        protected CardView cardView;
        Activity context;
//        protected  View garis;

        public ProdukViewHolder(View v) {
            super(v);
//            garis = v.findViewById(R.id.garis);
            context  = (Activity) v.getContext();


            imgProduk = (ImageView) v.findViewById(R.id.imgProduk);

            tvHargaDiskon = (TextView) v.findViewById(R.id.tvHargaDiskon);
            tvDiskon = (TextView) v.findViewById(R.id.tvDiskon);
            tvNamaProduk = (TextView) v.findViewById(R.id.tvNamaProduk);
            tvHargaProduk = (TextView) v.findViewById(R.id.tvHargaProduk);
            cardView = (CardView) v.findViewById(R.id.card_view);
            btnProduk = (RelativeLayout) v.findViewById(R.id.btnProduk);

            Resources r = v.getResources();
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, r.getDisplayMetrics());
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
