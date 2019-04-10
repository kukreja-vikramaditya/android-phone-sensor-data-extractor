package com.macbitsgoa.track2.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.EditText;

import com.macbitsgoa.track2.R;
import com.macbitsgoa.track2.adapters.ActivitiesAdapter;
import com.macbitsgoa.track2.models.ActivityItem;
import com.macbitsgoa.track2.utils.ITHSC;

import java.util.Calendar;

public class ManageActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ManageActivity.class.getSimpleName();

    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        //views
        et = findViewById(R.id.et_manage);
        RecyclerView rv = findViewById(R.id.rv_manage);

        //database
        ActivitiesAdapter adapter = new ActivitiesAdapter();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);

        //swipe helper
        new ItemTouchHelper(new ITHSC()).attachToRecyclerView(rv);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.btn_manage_add) {
            String activity = et.getText().toString().trim();
            if (activity.length() == 0) {
                return;
            }

            //check if values exists in db
            ActivityItem ai = getDatabase().where(ActivityItem.class).equalTo("activity", activity).findFirst();
            if (ai != null) return;

            //add to db
            getDatabase().beginTransaction();
            ai = getDatabase().createObject(ActivityItem.class, activity);
            ai.setTimeAdded(Calendar.getInstance().getTime().getTime());
            getDatabase().commitTransaction();
        }
        et.getText().clear();
    }
}
