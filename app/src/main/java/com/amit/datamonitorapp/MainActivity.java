package com.amit.datamonitorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amit.monitorlib.MonitorUtil;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    TextView tvDataUsed;
    TextView tvDataUnit;
    TextView tvMemory;
    ProgressBar pbMemory;
    //    TextView tvStorageDevice;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        getDataUsedAndBing();

        String mPer = MonitorUtil.getInstance().getMemoryPercent(this);

        tvMemory.setText(mPer + "%");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ObjectAnimator.ofInt(pbMemory, "progress", 79).start();

//            ObjectAnimator.ofInt(pbMemory, "progress", 1000, 0);
//            pbMemory.setProgress((int) Float.parseFloat(mPer), false);
//            pbMemory.clearAnimation();
        } else {
            ObjectAnimator.ofInt(pbMemory, "progress", 79).start();
        }


//        tvStorageDevice.setText(new DecimalFormat("##.##").format(MonitorUtil.getInstance().getTotalStorageAvailable(this)) + " MB");

        mHandler.postDelayed(mRunnable, 1000);

    }

    private void initViews() {
        tvDataUsed = findViewById(R.id.tvDataUsed);
        tvDataUnit = findViewById(R.id.tvDataUnit);
        tvMemory = findViewById(R.id.tvMemory);
        pbMemory = findViewById(R.id.pbMemory);
    }


    private final Runnable mRunnable = new Runnable() {
        public void run() {

            getDataUsedAndBing();

            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    private void getDataUsedAndBing() {

        String[] dataUsed = MonitorUtil.getInstance().getNetworkDataUsed(this).split(" ");

        tvDataUsed.setText(dataUsed[0]);
        tvDataUnit.setText(dataUsed[1]);

    }

}
