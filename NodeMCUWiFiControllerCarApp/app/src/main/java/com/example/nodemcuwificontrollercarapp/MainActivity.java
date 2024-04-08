package com.example.nodemcuwificontrollercarapp;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import android.widget.Toast;
import android.util.Log;

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
        // Kiểm tra xem giá trị tốc độ có nằm trong khoảng hợp lệ hay không
        if (speed >= 0 && speed <= 255) {
            try {
                RequestParams params = new RequestParams();
                params.put("speed", speed);
                httpClient.post(CONTROL_URL, params, null);
            } catch (Exception e) {
                // Xử lý ngoại lệ có thể xảy ra khi gửi dữ liệu, ví dụ như ghi lại thông tin lỗi vào log
                Log.e("MainActivity", "Error sending speed data: " + e.getMessage());
            }
        } else {
            // Nếu giá trị tốc độ không hợp lệ, có thể hiển thị thông báo cho người dùng
            Toast.makeText(this, "Invalid speed value", Toast.LENGTH_SHORT).show();
        }
    }
}