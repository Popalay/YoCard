package com.popalay.cardme.data.models;

import com.github.nitrico.lastadapter.StableId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Debt extends RealmObject implements StableId {

    public static final String ID = "id";
    public static final String CREATED_AT = "createdAt";
    public static final String HOLDER_ID = "holder.id";

    @PrimaryKey
    private long id;

    private Holder holder;

    private String message;

    private long createdAt;

    public Debt() {
    }

    public Debt(Holder holder, String message) {
        this.holder = holder;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Holder getHolder() {
        return holder;
    }

    public void setHolder(Holder holder) {
        this.holder = holder;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
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

        Debt debt = (Debt) o;

        if (id != debt.id) {
            return false;
        }
        if (createdAt != debt.createdAt) {
            return false;
        }
        if (holder != null ? !holder.equals(debt.holder) : debt.holder != null) {
            return false;
        }
        return message != null ? message.equals(debt.message) : debt.message == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (holder != null ? holder.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (int) (createdAt ^ (createdAt >>> 32));
        return result;
    }
}