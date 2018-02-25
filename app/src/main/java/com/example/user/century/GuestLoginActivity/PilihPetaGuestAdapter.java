package com.example.user.century.GuestLoginActivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.century.R;
import com.example.user.century.Register.LokasiPertama;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 27/07/2017.
 */

public class PilihPetaGuestAdapter extends RecyclerView.Adapter<PilihPetaGuestAdapter.PetaGuestViewHolder> {
    public List<LokasiPertama> lokasiGuestList;
    Context context;
    ArrayList<String> tempNama = new ArrayList<>();
    ArrayList<Integer> tempId = new ArrayList<>();



    public PilihPetaGuestAdapter(Context c,List<LokasiPertama> lokasiGuestList) {
        this.lokasiGuestList=lokasiGuestList;
        this.context=c;
    }



    @Override
    public void onBindViewHolder(final PetaGuestViewHolder petaGuestViewHolder, final int i) {

        final LokasiPertama lp = lokasiGuestList.get(i);

        petaGuestViewHolder.tvNamaLokasiPertama.setText(lp.Nama);
        petaGuestViewHolder.tvAlamatLokasiPertama.setText(lp.Alamat);
        petaGuestViewHolder.tvJarakLokasiPertama.setText(String.valueOf(String.format( "%.2f km", lp.Jarak )));
        petaGuestViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), String.valueOf(lp.Id), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(v.getContext(),PetaGuest.class);
                i.putExtra("panduanNama",lp.Nama);
                i.putExtra("panduanAlamat",lp.Alamat);
                i.putExtra("panduanLongitude2",lp.Longitude);
                i.putExtra("panduanLatitude2",lp.Latitude);
                i.putExtra("panduanJarak",petaGuestViewHolder.tvJarakLokasiPertama.getText().toString());
                i.putExtra("panduanId",String.valueOf(lp.Id));
                v.getContext().startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return lokasiGuestList.size();
    }

    @Override
    public PilihPetaGuestAdapter.PetaGuestViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_lokasi_pertama, viewGroup, false);

        return new PilihPetaGuestAdapter.PetaGuestViewHolder(itemView);
    }

    public static class PetaGuestViewHolder extends  RecyclerView.ViewHolder {
        protected TextView tvJarakLokasiPertama;
        protected TextView tvNamaLokasiPertama;
        protected TextView tvAlamatLokasiPertama;
        protected LinearLayout btnDetail;

        public PetaGuestViewHolder(View v) {
            super(v);
            btnDetail = (LinearLayout) v.findViewById(R.id.btnDetail);
            tvAlamatLokasiPertama = (TextView) v.findViewById(R.id.tvAlamatLokasiPertama);
            tvNamaLokasiPertama = (TextView) v.findViewById(R.id.tvNamaLokasiPertama);
            tvJarakLokasiPertama = (TextView) v.findViewById(R.id.tvJarakLokasiPertama);
        }
    }

}