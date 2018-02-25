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


public class rekomendasiAdapter extends RecyclerView.Adapter<rekomendasiAdapter.RekomendasiViewHolder> {
    private List<Rekomendasi> rekomendasiList;
    public int count = 0;
    Activity activity;
    SessionProduk sessionProduk;

    public rekomendasiAdapter(List<Rekomendasi> rekomendasiList) {
        this.rekomendasiList=rekomendasiList;
    }


    @Override
    public void onBindViewHolder(final RekomendasiViewHolder rekomendasiViewHolder, final int i) {
        final Rekomendasi re = rekomendasiList.get(i);

        rekomendasiViewHolder.tvNamaProduk.setText(re.Nama);

        if(re.Diskon.equals("0")){
//            if(Integer.valueOf(String.valueOf(re.Harga).substring(String.valueOf(re.Harga).length() - 2)) >= 50){
//                String s = (String.format("%,d", re.Harga)).replace(String.valueOf(re.Harga).substring(String.valueOf(re.Harga).length() - 2),"00").replace(String.valueOf(re.Harga).substring(String.valueOf(re.Harga).length() - 3),String.valueOf(Integer.valueOf(String.valueOf(re.Harga).substring(String.valueOf(re.Harga).length() - 3))+100)).replace(',', '.');
//                rekomendasiViewHolder.tvHargaProduk.setText("Rp "+s);
//            }
//            else if(Integer.valueOf(String.valueOf(re.Harga).substring(String.valueOf(re.Harga).length() - 2)) < 50){
//                String s = (String.format("%,d", re.Harga)).replace(',', '.').replace(String.valueOf(re.Harga).substring(String.valueOf(re.Harga).length() - 2),"00");
//                rekomendasiViewHolder.tvHargaProduk.setText("Rp "+s);
//            }
//            else{
                String s = (String.format("%,d", re.Harga)).replace(',', '.');
                rekomendasiViewHolder.tvHargaProduk.setText("Rp "+s);
//            }

            rekomendasiViewHolder.tvDiskon.setVisibility(View.GONE);
            rekomendasiViewHolder.tvHargaDiskon.setVisibility(View.GONE);
            rekomendasiViewHolder.tvHargaProduk.setPaintFlags(rekomendasiViewHolder.tvHargaProduk.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

        }
        else{
            rekomendasiViewHolder.tvDiskon.setVisibility(View.VISIBLE);
            rekomendasiViewHolder.tvDiskon.setText(re.Diskon+"%");
//            if(Integer.valueOf(String.valueOf(re.Harga).substring(String.valueOf(re.Harga).length() - 2)) >= 50){
//                String s = (String.format("%,d", re.Harga)).replace(String.valueOf(re.Harga).substring(String.valueOf(re.Harga).length() - 2),"00").replace(String.valueOf(re.Harga).substring(String.valueOf(re.Harga).length() - 3),String.valueOf(Integer.valueOf(String.valueOf(re.Harga).substring(String.valueOf(re.Harga).length() - 3))+100)).replace(',', '.');
//                rekomendasiViewHolder.tvHargaProduk.setText("Rp "+s);
//            }
//            else if(Integer.valueOf(String.valueOf(re.Harga).substring(String.valueOf(re.Harga).length() - 2)) < 50){
//                String s = (String.format("%,d", re.Harga)).replace(',', '.').replace(String.valueOf(re.Harga).substring(String.valueOf(re.Harga).length() - 2),"00");
//                rekomendasiViewHolder.tvHargaProduk.setText("Rp "+s);
//            }
//            else{
                String s = (String.format("%,d", re.Harga)).replace(',', '.');
                rekomendasiViewHolder.tvHargaProduk.setText("Rp "+s);
//            }
            rekomendasiViewHolder.tvHargaProduk.setPaintFlags(rekomendasiViewHolder.tvHargaProduk.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            rekomendasiViewHolder.tvHargaDiskon.setVisibility(View.VISIBLE);
            int diskonz = (int) (re.Harga*Float.valueOf(re.Diskon)/100);
            int harga = re.Harga-diskonz;
            String diskon = (String.format("%,d", harga));
//            if(Integer.valueOf(diskon.substring(diskon.length() - 2)) >= 50 ){
//                rekomendasiViewHolder.tvHargaDiskon.setText("Rp "+diskon.replace(diskon.substring(diskon.length()-3),String.valueOf(Integer.valueOf(diskon.substring(diskon.length()-3))+100)).replace(diskon.substring(diskon.length()-2),"00").replace(',', '.'));
//            }
//            else if(Integer.valueOf(diskon.substring(diskon.length() - 2)) < 50){
//                rekomendasiViewHolder.tvHargaDiskon.setText("Rp "+diskon.replace(diskon.substring(diskon.length()-2),"00").replace(',', '.'));
//            }
//            else{
                rekomendasiViewHolder.tvHargaDiskon.setText("Rp "+diskon.replace(',', '.'));
//            }

        }




        Picasso.with(rekomendasiViewHolder.itemView.getContext())
                .load(re.Gambar)
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.defaultpromosi)
                .error(R.drawable.defaultpromosi)
                .fit()
                .centerInside()
                .into(rekomendasiViewHolder.imgProduk);

        rekomendasiViewHolder.btnProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionProduk = new SessionProduk(view.getContext());
                Intent i = new Intent(view.getContext(), DetailProduk.class);
                sessionProduk.createProduk(re.Id_Produk_Per_Lokasi);
                //i.putExtra("panduanIdProduk",pr.Id_Produk);
                view.getContext().startActivity(i);
            }
        });


    }


    @Override
    public int getItemCount() {
        return rekomendasiList.size();
    }

    @Override
    public rekomendasiAdapter.RekomendasiViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rekomendasi_card_view, viewGroup, false);

        return new rekomendasiAdapter.RekomendasiViewHolder(itemView);
    }

    public class RekomendasiViewHolder extends  RecyclerView.ViewHolder {
        protected ImageView imgProduk;
        protected TextView tvNamaProduk;
        protected TextView tvHargaProduk;
        protected RelativeLayout btnProduk;
        protected TextView tvDiskon;
        protected TextView tvHargaDiskon;
//        protected  View garis;

        public RekomendasiViewHolder(View v) {
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
