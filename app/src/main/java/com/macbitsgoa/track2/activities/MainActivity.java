package com.macbitsgoa.track2.activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.macbitsgoa.track2.R;
import com.macbitsgoa.track2.models.ActivityItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends BaseActivity implements SensorEventListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int TIME_PERIOD_US = SensorManager.SENSOR_DELAY_FASTEST;//1_000_000;

    private Button startBtn;
    private RadioGroup radioGroup;

    private SensorManager sensorManager;
    private Sensor acclSensor;
    private Sensor gyroSensor;
    private Sensor heartRateSensor;
    private Sensor heartBeatSensor;
    private Sensor lAcclSensor;
    private Sensor rotationSensor;
    private Sensor _6dofSensor;
    private Sensor magnetSensor;
    private Sensor ambientSensor;
    private Sensor motionDetectSensor;
    private Sensor pressureSensor;
    private Sensor proximitySensor;
    private Sensor stationSensor;
    private Sensor stepSensor;
    private Sensor stepCountSensor;
    private Sensor sigMotionSensor;

    private boolean recording = false;

    private FileWriter writer;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = findViewById(R.id.btn_start);
        radioGroup = findViewById(R.id.rg);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acclSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        heartBeatSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_BEAT);
        lAcclSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        _6dofSensor = sensorManager.getDefaultSensor(Sensor.TYPE_POSE_6DOF);
        magnetSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        ambientSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        motionDetectSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MOTION_DETECT);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        stationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STATIONARY_DETECT);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sigMotionSensor = sensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);

        checkPermissions();
    }

    private void setupRadioButtons() {
        //remove previous views
        radioGroup.removeAllViews();

        //Get custom radio buttons from realm database
        RealmResults<ActivityItem> activities = getDatabase()
                .where(ActivityItem.class)
                .sort("timeAdded", Sort.DESCENDING)
                .findAll();
        for (final ActivityItem item : activities) {
            final RadioButton rb = new RadioButton(this);
            rb.setText(item.getActivity());
            radioGroup.addView(rb);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupRadioButtons();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (recording) {
            startBtn.setText("START");
            recording = false;
            endSession();
            sensorManager.unregisterListener(this);
        }
    }

    //TODO select option clear start
    @Override
    public void onClick(final View v) {
        final int id = v.getId();
        if (id == R.id.btn_manage) {
            if (recording) {
                Toast.makeText(this, "Manage not allowed during recording!", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(this, ManageActivity.class));
        } else if (id == R.id.btn_start) {
            if (!checkPermissions()) {
                Toast.makeText(this, "Permission error!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (recording) {
                recording = false;
                sensorManager.unregisterListener(this);
                startBtn.setText("START");
                endSession();
            } else {
                if (!startSession()) {
                    Toast.makeText(this, "Permit file storage!", Toast.LENGTH_SHORT).show();
                    return;
                }
                recording = true;
//                sensorManager.registerListener(this, heartBeatSensor, TIME_PERIOD_US);
//                sensorManager.registerListener(this, heartRateSensor, TIME_PERIOD_US);
                sensorManager.registerListener(this, acclSensor, TIME_PERIOD_US);
                sensorManager.registerListener(this, gyroSensor, TIME_PERIOD_US);
                sensorManager.registerListener(this, lAcclSensor, TIME_PERIOD_US);
//                sensorManager.registerListener(this, rotationSensor, TIME_PERIOD_US);
//                sensorManager.registerListener(this, _6dofSensor, TIME_PERIOD_US);
//                sensorManager.registerListener(this, magnetSensor, TIME_PERIOD_US);

//                sensorManager.registerListener(this, ambientSensor, TIME_PERIOD_US);
//                sensorManager.registerListener(this, motionDetectSensor, TIME_PERIOD_US);
//                sensorManager.registerListener(this, pressureSensor, TIME_PERIOD_US);
//                sensorManager.registerListener(this, proximitySensor, TIME_PERIOD_US);
//                sensorManager.registerListener(this, stationSensor, TIME_PERIOD_US);
//                sensorManager.registerListener(this, stepSensor, TIME_PERIOD_US);
//                sensorManager.registerListener(this, stepCountSensor, TIME_PERIOD_US);
//                sensorManager.registerListener(this, sigMotionSensor, TIME_PERIOD_US);
                startBtn.setText("STOP");
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!recording) return;
        StringBuilder sb = new StringBuilder();
        sb.append(Calendar.getInstance().getTime().getTime());
        sb.append(',');
        //sb.append(event.values.length);
        //sb.append(',');
        for (int i = 0; i < event.values.length; i++) {
            sb.append(event.values[i]);
            sb.append(',');
        }
        sb.append(event.sensor.getName());
        sb.append('\n');
        try {
            writer.append(sb.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Writing value error. Writer issue? or csvFile missing?");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public boolean startSession() {
        String fileName = getFileName();
        if (!checkPermissions()) return false;
        try {
            String filepath = Environment.getExternalStorageDirectory() + "/track/";
            File directory = new File(filepath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File csvFile = new File(directory, fileName);
            writer = new FileWriter(csvFile);
            writer.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getFileName() {
        final Calendar date = Calendar.getInstance();
        final StringBuilder sb = new StringBuilder();

        final int id = radioGroup.getCheckedRadioButtonId();
        String mode = "not_specified";
        if (id != -1) {
            RadioButton rb = findViewById(id);
            if(rb != null && rb.getText() != null) mode = rb.getText().toString();
        }

        final int year = date.get(Calendar.YEAR);
        final int month = 1 + date.get(Calendar.MONTH);
        final int day = date.get(Calendar.DAY_OF_MONTH);
        final int hour = date.get(Calendar.HOUR_OF_DAY);
        final int minute = date.get(Calendar.MINUTE);
        final int second = date.get(Calendar.SECOND);

        sb.append(year);
        sb.append('_');
        if (month < 10) sb.append(0);
        sb.append(month);
        sb.append('_');
        if (day < 10) sb.append(0);
        sb.append(day);
        sb.append('_');
        if (hour < 10) sb.append(0);
        sb.append(hour);
        sb.append('_');
        if (minute < 10) sb.append(0);
        sb.append(minute);
        sb.append('_');
        if (second < 10) sb.append(0);
        sb.append(second);
        sb.append('_');
        sb.append(mode);
        sb.append('_');
        sb.append("phone.csv");

        return sb.toString();
    }

    public void endSession() {
        try {
            writer.flush();
            writer.close();
            Toast.makeText(this, "Saved data to phone!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error while saving. Try again.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Session ending error. Writer issue? or csvFile missing?");
        }
    }
}
