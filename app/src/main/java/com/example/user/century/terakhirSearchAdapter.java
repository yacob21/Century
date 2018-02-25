package com.example.user.century;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.century.Home.MainActivity;
import com.example.user.century.SearchPackage.SearchActivity;
import com.example.user.century.Session.SessionKategori;
import com.example.user.century.Session.SessionManagement;
import com.example.user.century.Session.SessionSearch;
import com.example.user.century.cekStock.CekStockActivity;
import com.example.user.century.produkKategori.ProdukKategoriActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 11/22/2017.
 */



public class terakhirSearchAdapter extends RecyclerView.Adapter<terakhirSearchAdapter.TerakhirSearchViewHolder> {
    public List<Search> searchList;
    public int count = 0;
    Context context;
    public static final String DELETE_SEARCH_URL = "http://fransis.rawatwajah.com/century/deletePencarian.php";
    public static final String INSERT_SEARCH_URL = "http://fransis.rawatwajah.com/century/insertSearch.php";
    SessionManagement session;
    SessionSearch sessionSearch;
    SessionKategori sessionKategori;


    public terakhirSearchAdapter(Context c, List<Search> searchList) {
        this.searchList=searchList;
        this.context=c;
    }


    @Override
    public void onBindViewHolder(final terakhirSearchAdapter.TerakhirSearchViewHolder terakhirSearchViewHolder, final int i) {
        final Search sr = searchList.get(i);

        terakhirSearchViewHolder.tvTerakhir.setText(sr.Search);

        terakhirSearchViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session = new SessionManagement(v.getContext());
                final HashMap<String, String> user = session.getUserDetails();
                JSONObject objAdd2 = new JSONObject();
                try {
                    JSONArray arrData = new JSONArray();
                    JSONObject objDetail = new JSONObject();
                    objDetail.put("hasil_search",sr.Search);
                    objDetail.put("email",user.get(SessionManagement.KEY_EMAIL));
                    arrData.put(objDetail);
                    objAdd2.put("data",arrData);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                JsonObjectRequest stringRequest2 = new JsonObjectRequest(Request.Method.POST, INSERT_SEARCH_URL, objAdd2,
                        new Response.Listener<JSONObject>() {
                            @Override

                            public void onResponse(JSONObject response) {
                                try {
                                    if(response.getString("status").equals("OK")) {

                                    }
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) ;
                RequestQueue requestQueue2 = Volley.newRequestQueue(v.getContext());
                requestQueue2.add(stringRequest2);



                sessionKategori = new SessionKategori(v.getContext());
                sessionSearch = new SessionSearch(v.getContext());
                Intent i = new Intent(v.getContext(), SearchActivity.class);
                sessionSearch.createSearch(sr.Search);
                i.putExtra("panduanSearch",sr.Search);
                sessionKategori.createKategori("");
                v.getContext().startActivity(i);
            }
        });

        terakhirSearchViewHolder.ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(searchList.size() == 1){
                    if(context.getClass().getSimpleName().equals("MainActivity")){
                        MainActivity.linearSearchTerakhirMain.setVisibility(View.GONE);
                        MainActivity.cardListTerakhirSearch.setVisibility(View.GONE);
                    }

                    if(context.getClass().getSimpleName().equals("SearchActivity")){
                        SearchActivity.linearSearchTerakhirSearch.setVisibility(View.GONE);
                        SearchActivity.cardListTerakhirSearch.setVisibility(View.GONE);
                    }

                    if(context.getClass().getSimpleName().equals("ProdukKategoriActivity")){
                        ProdukKategoriActivity.linearSearchTerakhirProduk.setVisibility(View.GONE);
                        ProdukKategoriActivity.cardListTerakhirSearch.setVisibility(View.GONE);
                    }

                    if(context.getClass().getSimpleName().equals("CekStockActivity")){
                        CekStockActivity.linearSearchTerakhirCek.setVisibility(View.GONE);
                        CekStockActivity.cardListTerakhirSearch.setVisibility(View.GONE);
                    }

                }
                SessionManagement session;
                session = new SessionManagement(v.getContext());
                final HashMap<String, String> user = session.getUserDetails();

                JSONObject objAdd2 = new JSONObject();
                try {
                    JSONArray arrData = new JSONArray();
                    JSONObject objDetail = new JSONObject();
                    objDetail.put("email",user.get(SessionManagement.KEY_EMAIL));
                    objDetail.put("id",sr.Id);
                    arrData.put(objDetail);
                    objAdd2.put("data",arrData);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                JsonObjectRequest stringRequest2 = new JsonObjectRequest(Request.Method.POST, DELETE_SEARCH_URL, objAdd2,
                        new Response.Listener<JSONObject>() {
                            @Override

                            public void onResponse(JSONObject response) {
                                try {
                                    if(response.getString("status").equals("OK")) {
                                        removeAt(i);
                                    }
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) ;
                RequestQueue requestQueue2 = Volley.newRequestQueue(v.getContext());
                requestQueue2.add(stringRequest2);

            }
        });
    }


    @Override
    public int getItemCount() {
        return searchList.size();
    }

    @Override
    public terakhirSearchAdapter.TerakhirSearchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_terakhir_search, viewGroup, false);

        return new terakhirSearchAdapter.TerakhirSearchViewHolder(itemView);
    }

    public class TerakhirSearchViewHolder extends  RecyclerView.ViewHolder {
        protected TextView tvTerakhir;
        protected ImageView ivClear;
        protected LinearLayout btnDetail;
        Activity activity;
        public TerakhirSearchViewHolder(View v) {
            super(v);
            btnDetail = (LinearLayout) v.findViewById(R.id.btnDetail);
            tvTerakhir = (TextView) v.findViewById(R.id.tvTerakhir);
            ivClear = (ImageView) v.findViewById(R.id.ivClear);
            activity = (Activity) v.getContext();

        }




    }

    public void removeAt(int position){
        searchList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,searchList.size());

        if(context.getClass().getSimpleName().equals("MainActivity")){
            MainActivity.cardListTerakhirSearch.removeViewAt(position);
        }

        if(context.getClass().getSimpleName().equals("SearchActivity")){
            SearchActivity.cardListTerakhirSearch.removeViewAt(position);
        }

        if(context.getClass().getSimpleName().equals("ProdukKategoriActivity")){
            ProdukKategoriActivity.cardListTerakhirSearch.removeViewAt(position);
        }

        if(context.getClass().getSimpleName().equals("CekStockActivity")){
            CekStockActivity.cardListTerakhirSearch.removeViewAt(position);
        }
    }







}
