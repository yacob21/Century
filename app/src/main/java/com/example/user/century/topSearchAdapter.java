package com.example.user.century;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.century.SearchPackage.SearchActivity;
import com.example.user.century.Session.SessionKategori;
import com.example.user.century.Session.SessionManagement;
import com.example.user.century.Session.SessionSearch;
import com.example.user.century.cekStock.CekStockActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 11/22/2017.
 */



public class topSearchAdapter extends RecyclerView.Adapter<topSearchAdapter.TopSearchViewHolder> {
    private List<Search> searchList;
    public int count = 0;
    Activity activity;
    Context context;
    public static final String INSERT_SEARCH_URL = "http://fransis.rawatwajah.com/century/insertSearch.php";
    SessionManagement session;
    SessionSearch sessionSearch;
    SessionKategori sessionKategori;


    public topSearchAdapter(Context c, List<Search> searchList) {
        this.searchList=searchList;
        this.context=c;
    }


    @Override
    public void onBindViewHolder(final topSearchAdapter.TopSearchViewHolder topSearchViewHolder, final int i) {
        final Search sr = searchList.get(i);

        topSearchViewHolder.tvTop.setText(sr.Search);
        topSearchViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
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
                if(context.getClass().getSimpleName().equals("CekStockActivity")) {
                    CekStockActivity.cek="ada";
                    Intent i = new Intent(v.getContext(), CekStockActivity.class);
                    sessionSearch.createSearch(sr.Search);
                    sessionKategori.createKategori("");
                    i.putExtra("panduanSearch",sr.Search);
                    v.getContext().startActivity(i);
                }else{
                    Intent i = new Intent(v.getContext(), SearchActivity.class);
                    sessionSearch.createSearch(sr.Search);
                    sessionKategori.createKategori("");
                    i.putExtra("panduanSearch",sr.Search);
                    v.getContext().startActivity(i);
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return searchList.size();
    }

    @Override
    public topSearchAdapter.TopSearchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_top_search, viewGroup, false);

        return new topSearchAdapter.TopSearchViewHolder(itemView);
    }

    public class TopSearchViewHolder extends  RecyclerView.ViewHolder {
        protected TextView tvTop;
        protected LinearLayout btnDetail;

        public TopSearchViewHolder(View v) {
            super(v);
            btnDetail = (LinearLayout) v.findViewById(R.id.btnDetail);
            tvTop = (TextView) v.findViewById(R.id.tvTop);
        }
    }






}
