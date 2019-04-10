package com.macbitsgoa.track2.models;

import android.support.annotation.NonNull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ActivityItem extends RealmObject {
    @PrimaryKey
    private String activity;

    private long timeAdded;

    @NonNull
    public String getActivity() {
        return activity;
    }

    public long getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(long timeAdded) {
        this.timeAdded = timeAdded;
    }

    public void setActivity(@NonNull String activity) {
        this.activity = activity;
    }
}
