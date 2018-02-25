package com.example.user.century.Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.user.century.promosi.PromosiActivity;
import com.example.user.century.R;
import com.squareup.picasso.Picasso;

/**
 * Created by yacob on 8/31/2017.
 */

public class viewpagerAdapter extends PagerAdapter {
    Activity activity;
    String [] images;
    LayoutInflater inflater;

    public viewpagerAdapter(Activity activity, String[] images){
        this.activity=activity;
        this.images=images;
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
        inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.viewpager_promosi,container,false);
        ImageView image;
        image = (ImageView) itemView.findViewById(R.id.ivPromosi);
        DisplayMetrics dis = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dis);
        int height = dis.heightPixels;
        int width = dis.widthPixels;
        image.setMinimumHeight(height);
        image.setMinimumWidth(width);
        try{
            Picasso.with(activity.getApplicationContext())
                    .load(images[position])
                    .placeholder(R.drawable.defaultpromosi)
                    .error(R.drawable.defaultpromosi)
                    .into(image);
        }
        catch (Exception ex){

        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),PromosiActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("panduanImage",images[position]);
                v.getContext().startActivity(i);
            }
        });



        container.addView(itemView);
        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView((View) object);
    }




}
