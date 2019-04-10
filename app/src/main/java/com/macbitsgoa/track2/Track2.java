package com.macbitsgoa.track2;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Track2 extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        final RealmConfiguration config = new RealmConfiguration.Builder()
                .name("activities")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
