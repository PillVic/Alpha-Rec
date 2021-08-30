package com.alpharec.data;

import com.alpharec.pojo.Link;
import com.alpharec.pojo.Rating;
import com.alpharec.pojo.Tag;


public interface DbWriter {
    void insertLink(Link link);
    void insertTag(Tag tag);
    void insertRating(Rating rating);
}
