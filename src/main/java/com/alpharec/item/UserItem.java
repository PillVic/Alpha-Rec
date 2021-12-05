package com.alpharec.item;

import java.util.List;

/**
 * @author neovic
 * 依据用户的评分，记录用户的偏好信息
* */
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
    public UserItem(int id) {
        this.userId = id;
    }
}
