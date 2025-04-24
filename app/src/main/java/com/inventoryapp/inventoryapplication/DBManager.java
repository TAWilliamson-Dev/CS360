package com.inventoryapp.inventoryapplication;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;

public class DBManager {

    private DBHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    protected DBManager open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    protected void close() {
        dbHelper.close();
    }

    protected void insert(String username, String password) {
        Log.d(TAG, "YEP!");
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.USERNAME, username);
        contentValue.put(DBHelper.PASSWORD, password);
        database.insert(DBHelper.TABLE_NAME, null, contentValue);
    }

    /* Return Cursor of all users in the database */
    protected Cursor fetch() {
        String[] columns = new String[] { DBHelper._ID, DBHelper.USERNAME, DBHelper.PASSWORD };
        Cursor cursor = database.query(DBHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
           cursor.moveToFirst();
        }
        return cursor;
    }

    /* True if the username / password combination exists in the database */
    protected boolean checkExist(String username, String password) {
        SQLiteDatabase db = database;

        String[] args = new String[] {username, password};

        String filter = String.format("%s=? AND %s=?", DBHelper.USERNAME, DBHelper.PASSWORD);

        return DatabaseUtils.queryNumEntries(db, dbHelper.TABLE_NAME, filter, args) > 0;
    }

    /* Update existing users password */
    protected int update(long _id, String username, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.USERNAME, username);
        contentValues.put(DBHelper.PASSWORD, password);

        int i = database.update(DBHelper.TABLE_NAME, contentValues, DBHelper._ID + " = " + _id, null);
        return i;
    }

    /* Delete user from database - No current Use */
    private void delete(long _id) {
        database.delete(DBHelper.TABLE_NAME, DBHelper._ID + "=" + _id, null);
    }
}
