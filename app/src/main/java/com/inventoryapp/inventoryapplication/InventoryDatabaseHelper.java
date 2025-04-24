package com.inventoryapp.inventoryapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InventoryDatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "INVENTORY";

    public static final String _ID = "_id";
    public static final String ITEM_NAME = "Item";
    public static final String QUANTITY = "Quantity";

    static final String DB_NAME = "INVENTORY.DB";
    static final int DB_VERSION = 1;

    private static final String CREATE_TABLE = "create table "  + TABLE_NAME + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ITEM_NAME + " TEXT NOT NULL, " + QUANTITY + " TEXT);";

    public InventoryDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void close() {
        super.close();
    }
}
