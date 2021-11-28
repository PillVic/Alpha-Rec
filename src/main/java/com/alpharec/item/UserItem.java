package com.alpharec.item;

import java.util.List;

public class UserItem {
    public final int userId;
    public List<String> preferTags;
    public List<String> preferGenres;

    public void setPreferTag(List<String> tags){
        this.preferTags = tags;
    }

    public void setPreferGenres(List<String> genres){
        this.preferGenres = genres;
    }
    public UserItem(int Id) {
        this.userId = Id;
    }
}
