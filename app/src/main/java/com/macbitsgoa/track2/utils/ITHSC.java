package com.macbitsgoa.track2.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.macbitsgoa.track2.models.ActivityItem;
import com.macbitsgoa.track2.viewholders.ActivitiesViewholder;

import io.realm.Realm;

public class ITHSC extends ItemTouchHelper.SimpleCallback {
    public ITHSC() {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        //delete activity
        Realm database = Realm.getDefaultInstance();
        String act = ((ActivitiesViewholder) viewHolder).getText();
        ActivityItem ai = database.where(ActivityItem.class).equalTo("activity", act).findFirst();
        if (ai == null) return;
        database.beginTransaction();
        ai.deleteFromRealm();
        database.commitTransaction();
        database.close();
    }
}
