package com.macbitsgoa.track2.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.macbitsgoa.track2.R;

public class ActivitiesViewholder extends RecyclerView.ViewHolder {
    private TextView activityTv;

    public ActivitiesViewholder(@NonNull View itemView) {
        super(itemView);
        activityTv = itemView.findViewById(R.id.tv_vh_activities);
    }

    public void setText(@NonNull final String text) {
        activityTv.setText(text);
    }

    @NonNull
    public String getText() {
        return activityTv.getText().toString();
    }
}
