package com.example.user.century.slider;

/**
 * Created by Administrator on 11/8/2017.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.user.century.GuestLoginActivity.PetaGuest;
import com.example.user.century.LoginActivity;
import com.example.user.century.R;
import com.example.user.century.Register.PetaPertama;
import com.example.user.century.Register.RegisterActivity;
import com.example.user.century.Session.SessionCek;

/**
 * Created by yacob on 8/31/2017.
 */

public class sliderAdapter extends PagerAdapter {
   Context context;
    private Integer [] images = {R.drawable.slider1,R.drawable.slider2,R.drawable.slider3};
    LayoutInflater inflater;
    AlertDialog dialog;
    AlertDialog.Builder builder;

    SessionCek sessionCek;

    public  sliderAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.slider,null);
        ImageView image;
        image = (ImageView) itemView.findViewById(R.id.ivSlider);
        image.setImageResource(images[position]);
        Button btn = (Button) itemView.findViewById(R.id.btn);
        Button guest = (Button) itemView.findViewById(R.id.guest);
        if(position == 2){
            btn.setVisibility(View.VISIBLE);
            guest.setVisibility(View.VISIBLE);
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionCek = new SessionCek(v.getContext());
                sessionCek.createCek("login");
                Intent i = new Intent(v.getContext(),LoginActivity.class);
                v.getContext().startActivity(i);

            }
        });


        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Harap pilih lokasi Apotek Century yang Anda inginkan untuk proses pengambilan barang");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sessionCek = new SessionCek(v.getContext());
                        sessionCek.createCek("guest");
                        Intent i = new Intent(v.getContext(),PetaGuest.class);
                        v.getContext().startActivity(i);

                    }
                });
                dialog = builder.show();

            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(itemView,0);
        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView((View) object);
    }




}
