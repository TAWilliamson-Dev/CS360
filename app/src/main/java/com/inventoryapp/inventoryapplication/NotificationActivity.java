package com.inventoryapp.inventoryapplication;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class NotificationActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        textView = findViewById(R.id.textView);
        String data = getIntent().getStringExtra("name");
        textView.setText(data);
    }
}