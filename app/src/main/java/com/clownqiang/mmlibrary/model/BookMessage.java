package com.clownqiang.mmlibrary.model;

import java.io.Serializable;

/**
 * Created by clownqiang on 14/11/30.
 */
public class BookMessage implements Serializable{
    String book_id;
    String img_url;
    String title;
    String author;
    String borrowed_date;
    String return_date;
    String lib_location;
    String attachment;
    String description;

    public String getBook_id() {
        return book_id;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getBorrowed_date() {
        return borrowed_date;
    }

    public String getReturn_date() {
        return return_date;
    }

    public String getLib_location() {
        return lib_location;
    }

    public String getAttachment() {
        return attachment;
    }

    public String getDescription() {
        return description;
    }
}
