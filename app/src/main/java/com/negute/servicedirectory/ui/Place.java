package com.negute.servicedirectory.ui;

import java.io.Serializable;

public class Place implements Serializable {

    String id;
    String title;
    String tel;
    String coord;
    String addr;
    String spec;
    boolean hasGallery;

    public Place(String id, String title, String tel, String coord, String addr, String spec, boolean hasGallery) {
        this.id = id;
        this.title = title;
        this.tel = tel;
        this.coord = coord;
        this.addr = addr;
        this.spec = spec;
        this.hasGallery = hasGallery;
    }

    public boolean hasGallery() {
        return hasGallery;
    }

    public String getTitle() {
        return title;
    }

    public String getTel() {
        return tel;
    }

    public String getCoord() {
        return coord;
    }

    public String getAddr() {
        return addr;
    }



    public String getSpec() {
        return spec;
    }
}