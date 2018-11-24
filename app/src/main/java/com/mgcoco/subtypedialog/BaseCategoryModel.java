package com.mgcoco.subtypedialog;

import java.io.Serializable;

public class BaseCategoryModel implements Serializable{

    String name;

    boolean isPick;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPick() {
        return isPick;
    }

    public void setPick(boolean pick) {
        isPick = pick;
    }
}
