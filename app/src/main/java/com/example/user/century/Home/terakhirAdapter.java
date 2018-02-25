package com.example.user.century.Home;

/**
 * Created by Administrator on 11/14/2017.
 */

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

import com.example.user.century.produkKategori.DetailProduk;
import com.example.user.century.R;
import com.example.user.century.Session.SessionProduk;
import com.squareup.picasso.Picasso;
import java.util.List;


public class terakhirAdapter extends RecyclerView.Adapter<terakhirAdapter.TerakhirViewHolder> {
    private List<Terakhir> terakhirList;
    public int count = 0;
    Activity activity;
    SessionProduk sessionProduk;

    public terakhirAdapter(List<Terakhir> terakhirList) {
        this.terakhirList=terakhirList;
    }


    @Override
    public void onBindViewHolder(final TerakhirViewHolder terakhirViewHolder, final int i) {
        final Terakhir tr = terakhirList.get(i);

        terakhirViewHolder.tvNamaProduk.setText(tr.Nama);
        if(tr.Diskon.equals("0")){
//            if(Integer.valueOf(String.valueOf(tr.Harga).substring(String.valueOf(tr.Harga).length() - 2)) >= 50){
//                String s = (String.format("%,d", tr.Harga)).replace(String.valueOf(tr.Harga).substring(String.valueOf(tr.Harga).length() - 2),"00").replace(String.valueOf(tr.Harga).substring(String.valueOf(tr.Harga).length() - 3),String.valueOf(Integer.valueOf(String.valueOf(tr.Harga).substring(String.valueOf(tr.Harga).length() - 3))+100)).replace(',', '.');
//                terakhirViewHolder.tvHargaProduk.setText("Rp "+s);
//            }
//            else if(Integer.valueOf(String.valueOf(tr.Harga).substring(String.valueOf(tr.Harga).length() - 2)) < 50){
//                String s = (String.format("%,d", tr.Harga)).replace(',', '.').replace(String.valueOf(tr.Harga).substring(String.valueOf(tr.Harga).length() - 2),"00");
//                terakhirViewHolder.tvHargaProduk.setText("Rp "+s);
//            }
//            else{
                String s = (String.format("%,d", tr.Harga)).replace(',', '.');
                terakhirViewHolder.tvHargaProduk.setText("Rp "+s);
//            }
            terakhirViewHolder.tvDiskon.setVisibility(View.GONE);
            terakhirViewHolder.tvHargaDiskon.setVisibility(View.GONE);
            terakhirViewHolder.tvHargaProduk.setPaintFlags(terakhirViewHolder.tvHargaProduk.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

        }
        else{
            terakhirViewHolder.tvDiskon.setVisibility(View.VISIBLE);
            terakhirViewHolder.tvDiskon.setText(tr.Diskon+"%");
//            if(Integer.valueOf(String.valueOf(tr.Harga).substring(String.valueOf(tr.Harga).length() - 2)) >= 50){
//                String s = (String.format("%,d", tr.Harga)).replace(String.valueOf(tr.Harga).substring(String.valueOf(tr.Harga).length() - 2),"00").replace(String.valueOf(tr.Harga).substring(String.valueOf(tr.Harga).length() - 3),String.valueOf(Integer.valueOf(String.valueOf(tr.Harga).substring(String.valueOf(tr.Harga).length() - 3))+100)).replace(',', '.');
//                terakhirViewHolder.tvHargaProduk.setText("Rp "+s);
//            }
//            else if(Integer.valueOf(String.valueOf(tr.Harga).substring(String.valueOf(tr.Harga).length() - 2)) < 50){
//                String s = (String.format("%,d", tr.Harga)).replace(',', '.').replace(String.valueOf(tr.Harga).substring(String.valueOf(tr.Harga).length() - 2),"00");
//                terakhirViewHolder.tvHargaProduk.setText("Rp "+s);
//            }
//            else{
                String s = (String.format("%,d", tr.Harga)).replace(',', '.');
                terakhirViewHolder.tvHargaProduk.setText("Rp "+s);
//            }
            terakhirViewHolder.tvHargaProduk.setPaintFlags(terakhirViewHolder.tvHargaProduk.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            terakhirViewHolder.tvHargaDiskon.setVisibility(View.VISIBLE);
            int diskonz = (int) (tr.Harga*Float.valueOf(tr.Diskon)/100);
            int harga = tr.Harga-diskonz;
            String diskon = (String.format("%,d", harga));
//            if(Integer.valueOf(diskon.substring(diskon.length() - 2)) >= 50 ){
//                terakhirViewHolder.tvHargaDiskon.setText("Rp "+diskon.replace(diskon.substring(diskon.length()-3),String.valueOf(Integer.valueOf(diskon.substring(diskon.length()-3))+100)).replace(diskon.substring(diskon.length()-2),"00").replace(',', '.'));
//            }
//            else if(Integer.valueOf(diskon.substring(diskon.length() - 2)) < 50){
//                terakhirViewHolder.tvHargaDiskon.setText("Rp "+diskon.replace(diskon.substring(diskon.length()-2),"00").replace(',', '.'));
//            }
//            else{
                terakhirViewHolder.tvHargaDiskon.setText("Rp "+diskon.replace(',', '.'));
//            }
        }


        Picasso.with(terakhirViewHolder.itemView.getContext())
                .load(tr.Gambar)
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.defaultpromosi)
                .error(R.drawable.defaultpromosi)
                .fit()
                .centerInside()
                .into(terakhirViewHolder.imgProduk);

        terakhirViewHolder.btnProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionProduk = new SessionProduk(view.getContext());
                Intent i = new Intent(view.getContext(), DetailProduk.class);
                sessionProduk.createProduk(tr.Id_Produk_Per_Lokasi);
                //i.putExtra("panduanIdProduk",pr.Id_Produk);
                view.getContext().startActivity(i);
            }
        });


    }


    @Override
    public int getItemCount() {
        return terakhirList.size();
    }

    @Override
    public terakhirAdapter.TerakhirViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.terakhir_card_view, viewGroup, false);

        return new terakhirAdapter.TerakhirViewHolder(itemView);
    }

    public class TerakhirViewHolder extends  RecyclerView.ViewHolder {
        protected ImageView imgProduk;
        protected TextView tvNamaProduk;
        protected TextView tvHargaProduk;
        protected RelativeLayout btnProduk;
        protected TextView tvDiskon;
        protected TextView tvHargaDiskon;
//        protected  View garis;

        public TerakhirViewHolder(View v) {
            super(v);
//            garis = v.findViewById(R.id.garis);
            imgProduk = (ImageView) v.findViewById(R.id.imgProduk);
            tvHargaDiskon = (TextView) v.findViewById(R.id.tvHargaDiskon);
            tvDiskon = (TextView) v.findViewById(R.id.tvDiskon);
            tvNamaProduk = (TextView) v.findViewById(R.id.tvNamaProduk);
            tvHargaProduk = (TextView) v.findViewById(R.id.tvHargaProduk);
            btnProduk = (RelativeLayout) v.findViewById(R.id.btnProduk);
        }
    }






}
