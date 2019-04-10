package com.macbitsgoa.track2.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.macbitsgoa.track2.R;
import com.macbitsgoa.track2.models.ActivityItem;
import com.macbitsgoa.track2.viewholders.ActivitiesViewholder;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesViewholder> implements
        OrderedRealmCollectionChangeListener<RealmResults<ActivityItem>> {

    private Realm database;

    private RealmResults<ActivityItem> activities;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.database = Realm.getDefaultInstance();
        this.activities = database
                .where(ActivityItem.class)
                .sort("timeAdded", Sort.DESCENDING)
                .findAllAsync();
        this.activities.addChangeListener(this);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.activities.removeChangeListener(this);
        this.database.close();
    }

    @NonNull
    @Override
    public ActivitiesViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View v = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.vh_activities, viewGroup, false);
        return new ActivitiesViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivitiesViewholder holder, int position) {
        holder.setText(activities.get(position).getActivity());
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    @Override
    public void onChange(RealmResults<ActivityItem> activityItems,
                         OrderedCollectionChangeSet changeSet) {
        // `null`  means the async query returns the first time.
        if (changeSet == null) {
            notifyDataSetChanged();
            return;
        }
        // For deletions, the adapter has to be notified in reverse order.
        OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
        for (int i = deletions.length - 1; i >= 0; i--) {
            OrderedCollectionChangeSet.Range range = deletions[i];
            notifyItemRangeRemoved(range.startIndex, range.length);
        }

        OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
        for (OrderedCollectionChangeSet.Range range : insertions) {
            notifyItemRangeInserted(range.startIndex, range.length);
        }

        OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
        for (OrderedCollectionChangeSet.Range range : modifications) {
            notifyItemRangeChanged(range.startIndex, range.length);
        }
    }
}
