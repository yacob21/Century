package com.example.user.century.Pembelian;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.century.R;
import com.example.user.century.Session.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class StatusPemesanan extends Fragment {

    public StatusPemesanan() {
        // Required empty public constructor
    }
    SessionManagement session;
    RecyclerView cardListPemesanan;
    List<Pembayaran> result = new ArrayList<>();
    listPemesananAdapter lpa;
    LinearLayout linearBelum;
    RequestQueue queue;
    String tanggal;
    String kode;
    int harga;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(getActivity());
        session = new SessionManagement(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_status_pemesanan, container, false);
        cardListPemesanan = (RecyclerView) view.findViewById(R.id.cardListPemesanan);
        cardListPemesanan.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        cardListPemesanan.setLayoutManager(llm);
        linearBelum = (LinearLayout) view.findViewById(R.id.linearBelum);

        final HashMap<String, String> user = session.getUserDetails();
        String urlDetail = "http://fransis.rawatwajah.com/century/selectPemesanan.php?email="+user.get(SessionManagement.KEY_EMAIL);
        JsonObjectRequest reqDetail = new JsonObjectRequest(urlDetail, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray users = null;
                        try {
                            users = response.getJSONArray("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(users.length() == 0){
                            linearBelum.setVisibility(View.VISIBLE);
                            PembelianActivity.pb.setVisibility(View.GONE);
                        }
                        for (int i = 0; i < users.length(); i++) {
                            try {
                                JSONObject obj = users.getJSONObject(i);
                                tanggal =obj.getString("Tanggal_Transaksi1");
                                kode = obj.getString("Kode_Transaksi");
                                harga = obj.getInt("Total_Harga")+obj.getInt("Kode_Unik");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            result.add(new Pembayaran(tanggal,kode,harga));
                            lpa = new listPemesananAdapter(getActivity(), result);
                            cardListPemesanan.setAdapter(lpa);
                            PembelianActivity.pb.setVisibility(View.GONE);
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(ListPasien.this,"Terjadi Kendala Koneksi",Toast.LENGTH_LONG ).show();
            }
        });
        queue.add(reqDetail);


        return view;
    }


}
