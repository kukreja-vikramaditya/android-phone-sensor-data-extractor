package com.macbitsgoa.track2.activities;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.macbitsgoa.track2.utils.HC;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected static final String TAG = BaseActivity.class.getSimpleName();

    private SharedPreferences sp;

    private Realm database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = Realm.getDefaultInstance();
    }

    /**
     * Call super of this function after all database actions are done.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }

    protected SharedPreferences getDefaultSharedPreferences() {
        if(sp == null) sp = HC.getSharedPreferences(this);
        return sp;
    }

    protected Realm getDatabase() {
        return database;
    }

    protected boolean checkPermissions() {
        int result;
        final List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : HC.PERMISSIONS) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat
                    .requestPermissions(this,
                            listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                            100);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull final String permissions[],
                                           @NonNull final int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
                Toast.makeText(this, "Permissions granted! Press Start!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    public void onClick(final View v) {

    }
}
