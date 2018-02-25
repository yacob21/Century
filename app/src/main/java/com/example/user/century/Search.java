package com.example.user.century;

/**
 * Created by Administrator on 11/22/2017.
 */

public class Search {
    String Search;
    String Id;

    public Search(String search,String id){
        this.Search=search;
        this.Id= id;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSearch() {
        return Search;
    }

    public void setSearch(String search) {
        Search = search;
    }
}
