package com.amit.datamonitorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.amit.monitorlib.MonitorUtil;

import java.text.DecimalFormat;
public class MainActivity extends AppCompatActivity {

    TextView tvTotalData;
    TextView tvTotalMemory;
    TextView tvStorageDevice;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        tvTotalData.setText(MonitorUtil.getInstance().getNetworkDataUsed(this));
        tvTotalMemory.setText(MonitorUtil.getInstance().getMemoryPercent(this) + "%");
        tvStorageDevice.setText(new DecimalFormat("##.##").format(MonitorUtil.getInstance().getTotalStorageAvailable())+ " MB");

        mHandler.postDelayed(mRunnable, 1000);

    }

    private void initViews() {
        tvTotalData = findViewById(R.id.tvTotalData);
        tvTotalMemory = findViewById(R.id.tvTotalMemory);
        tvStorageDevice = findViewById(R.id.tvStorageDevice);
    }


    private final Runnable mRunnable = new Runnable() {
        public void run() {
            tvTotalData.setText(MonitorUtil.getInstance().getNetworkDataUsed(MainActivity.this));
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

}
