package com.dubinets.requisition.activity.adapter;

import java.util.HashMap;

/**
 * Created by dubinets on 29.08.2016.
 */
public class ItemTmp {
    private HashMap<String, Object> propeties = new HashMap<>();
    boolean                         isVisible;

    public ItemTmp() {
    }

    public Object get(String key) {
        return propeties.get(key);
    }

    public void put(String name, Object obj) {
        propeties.put(name, obj);
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}