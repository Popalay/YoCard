package com.popalay.yocard.data.models;

import com.github.nitrico.lastadapter.StableId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Holder extends RealmObject implements StableId {

    public static final String ID = "id";

    @PrimaryKey private long id;

    private String name;

    public Holder() {
    }

    public Holder(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public long getStableId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Holder holder = (Holder) o;

        if (id != holder.id) {
            return false;
        }
        return name != null ? name.equals(holder.name) : holder.name == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}