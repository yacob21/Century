package com.example.user.century;

/**
 * Created by Administrator on 11/21/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

/**
 * Created by yacob on 8/31/2017.
 */


public class brandAdapter extends RecyclerView.Adapter<brandAdapter.BrandViewHolder> {
    private List<Brand> brandList;
    public int count = 0;
    Activity activity;
    Context context;
    public static String tempBrand = null;
    public brandAdapter(Context c, List<Brand> brandList) {
        this.brandList=brandList;
        this.context=c;
    }


    @Override
    public void onBindViewHolder(final BrandViewHolder brandViewHolder, final int i) {
        final Brand br = brandList.get(i);

        brandViewHolder.cbBrand.setText(br.Nama);
        brandViewHolder.cbBrand.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tempBrand = tempBrand+", "+br.Nama;
                }
                else{
                    tempBrand = tempBrand.replace(", "+br.Nama,"");
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return brandList.size();
    }

    @Override
    public brandAdapter.BrandViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_brand, viewGroup, false);

        return new brandAdapter.BrandViewHolder(itemView);
    }

    public static class BrandViewHolder extends  RecyclerView.ViewHolder {
        protected CheckBox cbBrand;

        public BrandViewHolder(View v) {
            super(v);
            cbBrand = (CheckBox) v.findViewById(R.id.cbBrand);
        }
    }






}
