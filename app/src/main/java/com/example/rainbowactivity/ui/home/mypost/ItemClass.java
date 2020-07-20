package com.example.rainbowactivity.ui.home.mypost;

public class ItemClass {

    private int type;
    private Object object;

    public ItemClass(int type, Object object) {
        this.type = type;
        this.object = object;
    }

    public int getType() {
        return type;
    }

    public Object getObject() {
        return object;
    }
}
