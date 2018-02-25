package com.example.user.century.Register;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.century.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 27/07/2017.
 */

public class PilihPetaPertamaAdapter extends RecyclerView.Adapter<PilihPetaPertamaAdapter.PetaPertamaViewHolder> {
    public List<LokasiPertama> lokasiPertamaList;
    Context context;
    ArrayList<String> tempNama = new ArrayList<>();
    ArrayList<Integer> tempId = new ArrayList<>();



    public PilihPetaPertamaAdapter(Context c,List<LokasiPertama> lokasiPertamaList) {
        this.lokasiPertamaList=lokasiPertamaList;
        this.context=c;
    }



    @Override
    public void onBindViewHolder(final PetaPertamaViewHolder petaPertamaViewHolder, final int i) {

        final LokasiPertama lp = lokasiPertamaList.get(i);
//        double latitude = PetaPertama.latitude;
//        double longitude = PetaPertama.longitude;
//        Location locationA = new Location("point A");
//        Location locationB = new Location("point B");
//        locationA.setLatitude(latitude);
//        locationA.setLongitude(longitude);
//        locationB.setLatitude(lp.Latitude);
//        locationB.setLongitude(lp.Longitude);
//        double jarak = locationA.distanceTo(locationB)/1000;

        petaPertamaViewHolder.tvNamaLokasiPertama.setText(lp.Nama);
        petaPertamaViewHolder.tvAlamatLokasiPertama.setText(lp.Alamat);
        petaPertamaViewHolder.tvJarakLokasiPertama.setText(String.valueOf(String.format( "%.2f km", lp.Jarak )));
        petaPertamaViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), String.valueOf(lp.Id), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(v.getContext(),PetaPertama.class);
                i.putExtra("panduanNama",lp.Nama);
                i.putExtra("panduanAlamat",lp.Alamat);
                i.putExtra("panduanLongitude2",lp.Longitude);
                i.putExtra("panduanLatitude2",lp.Latitude);
                i.putExtra("panduanJarak",petaPertamaViewHolder.tvJarakLokasiPertama.getText().toString());
                i.putExtra("panduanId",String.valueOf(lp.Id));
                v.getContext().startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return lokasiPertamaList.size();
    }

    @Override
    public PilihPetaPertamaAdapter.PetaPertamaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_lokasi_pertama, viewGroup, false);

        return new PilihPetaPertamaAdapter.PetaPertamaViewHolder(itemView);
    }

    public static class PetaPertamaViewHolder extends  RecyclerView.ViewHolder {
        protected TextView tvJarakLokasiPertama;
        protected TextView tvNamaLokasiPertama;
        protected TextView tvAlamatLokasiPertama;
        protected LinearLayout btnDetail;

        public PetaPertamaViewHolder(View v) {
            super(v);
            btnDetail = (LinearLayout) v.findViewById(R.id.btnDetail);
            tvAlamatLokasiPertama = (TextView) v.findViewById(R.id.tvAlamatLokasiPertama);
            tvNamaLokasiPertama = (TextView) v.findViewById(R.id.tvNamaLokasiPertama);
            tvJarakLokasiPertama = (TextView) v.findViewById(R.id.tvJarakLokasiPertama);
        }
    }

}