package com.example.user.century.cekStock;

import android.app.Activity;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.century.R;

import java.util.List;

/**
 * Created by yacob on 8/31/2017.
 */


public class detailCekAdapter extends RecyclerView.Adapter<detailCekAdapter.DetailCekViewHolder> {
    private List<DetailCek> detailCekList;
    public int count = 0;
    Activity activity;

    public detailCekAdapter(List<DetailCek> detailCekList) {
        this.detailCekList=detailCekList;
    }


    @Override
    public void onBindViewHolder(final DetailCekViewHolder detailCekViewHolder, final int i) {
        final DetailCek dc = detailCekList.get(i);

        detailCekViewHolder.tvAlamat.setText(dc.Alamat);
        detailCekViewHolder.tvToko.setText(dc.Nama);

        if(dc.Diskon.equals("0")){
//            if(Integer.valueOf(String.valueOf(dc.Harga).substring(String.valueOf(dc.Harga).length() - 2)) >= 50){
//                String s = (String.format("%,d", dc.Harga)).replace(String.valueOf(dc.Harga).substring(String.valueOf(dc.Harga).length() - 2),"00").replace(String.valueOf(dc.Harga).substring(String.valueOf(dc.Harga).length() - 3),String.valueOf(Integer.valueOf(String.valueOf(dc.Harga).substring(String.valueOf(dc.Harga).length() - 3))+100)).replace(',', '.');
//                detailCekViewHolder.tvHarga.setText("Rp "+s);
//            }
//            else if(Integer.valueOf(String.valueOf(dc.Harga).substring(String.valueOf(dc.Harga).length() - 2)) < 50){
//                String s = (String.format("%,d", dc.Harga)).replace(',', '.').replace(String.valueOf(dc.Harga).substring(String.valueOf(dc.Harga).length() - 2),"00");
//                detailCekViewHolder.tvHarga.setText("Rp "+s);
//            }
//            else{
                String s = (String.format("%,d", dc.Harga)).replace(',', '.');
                detailCekViewHolder.tvHarga.setText("Rp "+s);
//            }
            detailCekViewHolder.tvDiskon.setVisibility(View.GONE);
            detailCekViewHolder.tvHargaDiskon.setVisibility(View.GONE);
            detailCekViewHolder.tvHarga.setPaintFlags(detailCekViewHolder.tvHarga.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

        }
        else{
            detailCekViewHolder.tvDiskon.setVisibility(View.VISIBLE);
            detailCekViewHolder.tvDiskon.setText(dc.Diskon+"%");
//            if(Integer.valueOf(String.valueOf(dc.Harga).substring(String.valueOf(dc.Harga).length() - 2)) >= 50){
//                String s = (String.format("%,d", dc.Harga)).replace(String.valueOf(dc.Harga).substring(String.valueOf(dc.Harga).length() - 2),"00").replace(String.valueOf(dc.Harga).substring(String.valueOf(dc.Harga).length() - 3),String.valueOf(Integer.valueOf(String.valueOf(dc.Harga).substring(String.valueOf(dc.Harga).length() - 3))+100)).replace(',', '.');
//                detailCekViewHolder.tvHarga.setText("Rp "+s);
//            }
//            else if(Integer.valueOf(String.valueOf(dc.Harga).substring(String.valueOf(dc.Harga).length() - 2)) < 50){
//                String s = (String.format("%,d", dc.Harga)).replace(',', '.').replace(String.valueOf(dc.Harga).substring(String.valueOf(dc.Harga).length() - 2),"00");
//                detailCekViewHolder.tvHarga.setText("Rp "+s);
//            }
//            else{
                String s = (String.format("%,d", dc.Harga)).replace(',', '.');
                detailCekViewHolder.tvHarga.setText("Rp "+s);
//            }
            detailCekViewHolder.tvHarga.setPaintFlags( detailCekViewHolder.tvHarga.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            detailCekViewHolder.tvHargaDiskon.setVisibility(View.VISIBLE);
            int diskonz = (int) (dc.Harga*Float.valueOf(dc.Diskon)/100);
            int harga = dc.Harga-diskonz;
            String diskon = (String.format("%,d", harga));
//            if(Integer.valueOf(diskon.substring(diskon.length() - 2)) >= 50 ){
//                detailCekViewHolder.tvHargaDiskon.setText("Rp "+diskon.replace(diskon.substring(diskon.length()-3),String.valueOf(Integer.valueOf(diskon.substring(diskon.length()-3))+100)).replace(diskon.substring(diskon.length()-2),"00").replace(',', '.'));
//            }
//            else if(Integer.valueOf(diskon.substring(diskon.length() - 2)) < 50){
//                detailCekViewHolder.tvHargaDiskon.setText("Rp "+diskon.replace(diskon.substring(diskon.length()-2),"00").replace(',', '.'));
//            }
//            else{
                detailCekViewHolder.tvHargaDiskon.setText("Rp "+diskon.replace(',', '.'));
//            }
        }


        detailCekViewHolder.btnProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }


    @Override
    public int getItemCount() {
        return detailCekList.size();
    }

    @Override
    public detailCekAdapter.DetailCekViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.detail_cek_card_view, viewGroup, false);

        return new detailCekAdapter.DetailCekViewHolder(itemView);
    }

    public static class DetailCekViewHolder extends  RecyclerView.ViewHolder {
        protected TextView tvToko;
        protected TextView tvHarga;
        protected TextView tvAlamat;
        protected LinearLayout btnProduk;
        protected TextView tvDiskon;
        protected TextView tvHargaDiskon;
//        protected  View garis;


        public DetailCekViewHolder(View v) {
            super(v);
//            garis = v.findViewById(R.id.garis);
            tvHargaDiskon = (TextView) v.findViewById(R.id.tvHargaDiskon);
            tvDiskon = (TextView) v.findViewById(R.id.tvDiskon);
            tvToko = (TextView) v.findViewById(R.id.tvToko);
            tvHarga = (TextView) v.findViewById(R.id.tvHarga);
            tvAlamat = (TextView) v.findViewById(R.id.tvAlamat);
            btnProduk = (LinearLayout) v.findViewById(R.id.btnProduk);
        }
    }






}
