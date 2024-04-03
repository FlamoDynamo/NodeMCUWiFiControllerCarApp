package com.example.nodemcuwificontrollercarapp;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nodemcuwificontrollercarapp.views.JoystickView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

public class MainActivity extends AppCompatActivity {
    private static final String NODEMCU_IP_ADDRESS = "192.168.4.1";
    private static final String CONTROL_URL = "http://" + NODEMCU_IP_ADDRESS + "/control";

    private AsyncHttpClient httpClient;
    private TextView speedValueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        httpClient = new AsyncHttpClient();
        SeekBar speedSeekBar = findViewById(R.id.speedSeekBar);
        speedValueTextView = findViewById(R.id.speedValueTextView);
        JoystickView joystickView = findViewById(R.id.JoystickView);

        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedValueTextView.setText(String.valueOf(progress));
                sendSpeedData(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Không cần xử lý
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Không cần xử lý
            }
        });

        joystickView.setOnJoystickMovedListener((xPercent, yPercent) -> {
            // Thực hiện hành động khi joystick di chuyển
        });
    }

    private void sendSpeedData(int speed) {
        RequestParams params = new RequestParams();
        params.put("speed", speed);

        httpClient.post(CONTROL_URL, params, null);
    }
}