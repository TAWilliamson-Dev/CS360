package com.inventoryapp.inventoryapplication;

/*
    CS360 Mobile Architecture and Programming
    Module 7 - Final Project Inventory Application
    Dr. Kevin Eaton
    Travis Williamson
 */
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    private DBManager dbManager;

    final String[] from = new String[] {DBHelper._ID, DBHelper.USERNAME, DBHelper.PASSWORD };
    final int[] to = new int[] {R.id.id, R.id.editTextUserName, R.id.editTextTextPassword};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dbManager = new DBManager(this);
        try {
            dbManager.open();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Cursor cursor = dbManager.fetch();

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.activity_main, cursor, from, to);
        adapter.notifyDataSetChanged();

        Button signInButton = findViewById(R.id.button_sign_in);
        Button newUserButton = findViewById(R.id.button_new_user);

        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.SEND_SMS}, 101);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signInButton.setOnClickListener(v -> attemptSignIn());
        newUserButton.setOnClickListener(v -> createNewUser());
    }

    public void attemptSignIn() {
        TextView userName = findViewById(R.id.editTextUserName);
        TextView password = findViewById(R.id.editTextTextPassword);

        if(dbManager.checkExist(userName.getText().toString(),password.getText().toString())) {
            Intent inventory_intent = new Intent(getApplicationContext(), DisplayInventory.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(inventory_intent);
        }
        else {
            makeNotification(this);
        }
    }

    public void createNewUser() {
        TextView userName = findViewById(R.id.editTextUserName);
        TextView password = findViewById(R.id.editTextTextPassword);

        if(!dbManager.checkExist(userName.getText().toString(),password.getText().toString())) {
            dbManager.insert(userName.getText().toString(), password.getText().toString());
        }
        else {
            makeNotification(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbManager.close();
    }

    public void makeNotification(Context mContext) {
        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext.getApplicationContext(), "notify_001");
        Intent ii = new Intent(mContext.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, ii, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        CharSequence verseurl = "Username or Password is incorrect.";
        bigText.bigText(verseurl);
        bigText.setBigContentTitle("Unable to sign in.");
        bigText.setSummaryText("Username or Password is incorrect. Unable to sign in.");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("Failed sign in attempt");
        mBuilder.setContentText("Incorrect Username/Password.");
        mBuilder.setPriority(2);
        mBuilder.setStyle(bigText);

        mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_Failed_Signin";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Failed sign in attempt.",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }
        mNotificationManager.notify(1, mBuilder.build());
    }
}