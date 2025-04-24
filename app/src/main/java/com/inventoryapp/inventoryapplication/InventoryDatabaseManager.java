package com.inventoryapp.inventoryapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class InventoryDatabaseManager {

    private InventoryDatabaseHelper dbHelper;

    private final Context context;

    private SQLiteDatabase database;

    public InventoryDatabaseManager(Context c) { context = c;}

    public InventoryDatabaseManager open() throws SQLException {
        dbHelper = new InventoryDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() { dbHelper.close(); }

    public void insert(String itemName, String quantity) {

        ContentValues contentValue = new ContentValues();
        contentValue.put(InventoryDatabaseHelper.ITEM_NAME, itemName);
        contentValue.put(InventoryDatabaseHelper.QUANTITY, quantity);
        database.insert(InventoryDatabaseHelper.TABLE_NAME, null, contentValue);
        if(Integer.parseInt(quantity) == 0) {
            makeNotification(context, itemName);
        }
    }

    public Cursor fetch() {
        String[] columns = new String[] {InventoryDatabaseHelper._ID, InventoryDatabaseHelper.ITEM_NAME, InventoryDatabaseHelper.QUANTITY };
        Cursor cursor = database.query(InventoryDatabaseHelper.TABLE_NAME, columns, null,null,null,null,null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public boolean checkExists(String itemName) {
        SQLiteDatabase db = database;
        String[] args = new String[] {"`" + itemName + "`"};
        String filter = String.format("%s=?",InventoryDatabaseHelper.ITEM_NAME);

        return DatabaseUtils.queryNumEntries(db, InventoryDatabaseHelper.TABLE_NAME, filter, args) > 0;
    }

    public int update(long _id, String itemName, String quantity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryDatabaseHelper.ITEM_NAME,itemName);
        contentValues.put(InventoryDatabaseHelper.QUANTITY,quantity);

        int i = database.update(InventoryDatabaseHelper.TABLE_NAME, contentValues, InventoryDatabaseHelper._ID + " = " + _id, null);
        if(Integer.parseInt(quantity) == 0) {
            makeNotification(context,itemName);
        }
        return i;
    }

    public void delete(long _id) {
        String[] pendingDeleteInformation = getInventoryItem(_id);
        database.delete(InventoryDatabaseHelper.TABLE_NAME, InventoryDatabaseHelper._ID + " = " + _id, null);
        makeNotification(context, pendingDeleteInformation[1]);
    }

    public String[] getInventoryItem(String itemName) {

        String[] itemParameters = new String[3];
        String query = "SELECT * FROM " + InventoryDatabaseHelper.TABLE_NAME + " WHERE " + InventoryDatabaseHelper.ITEM_NAME + " = \"" + itemName + "\";";

        Cursor c = database.rawQuery(query,null);


        if(c.moveToFirst()) {
            itemParameters[0] = c.getString(0);
            itemParameters[1] = c.getString(1);
            itemParameters[2] = c.getString(2);
        }
        c.close();
        return itemParameters;
    }

    public String[] getInventoryItem(long _id) {

        String[] itemParameters = new String[3];
        String query = "SELECT * FROM " + InventoryDatabaseHelper.TABLE_NAME + " WHERE " + InventoryDatabaseHelper._ID + " = " + "'" + _id + "'";

        Cursor c = database.rawQuery(query,null);


        if(c.moveToFirst()) {
            itemParameters[0] = c.getString(0);
            itemParameters[1] = c.getString(1);
            itemParameters[2] = c.getString(2);
        }
        c.close();
        return itemParameters;
    }

    public void makeNotification(Context mContext, String mItemName) {
        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext.getApplicationContext(), "notify_001");
        Intent ii = new Intent(mContext.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, ii, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        CharSequence verseurl = "Item: " + mItemName + " is out of stock.";
        bigText.bigText(verseurl);
        bigText.setBigContentTitle("Item: " + mItemName + " is out of stock.");
        bigText.setSummaryText("Item: " + mItemName + " is out of stock.");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("Item out of stock.");
        mBuilder.setContentText("Item: " + mItemName + " is out of stock.");
        mBuilder.setPriority(2);
        mBuilder.setStyle(bigText);

        mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_itemOutOfStock";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "On hand notification",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }
        mNotificationManager.notify(0, mBuilder.build());
    }


}
