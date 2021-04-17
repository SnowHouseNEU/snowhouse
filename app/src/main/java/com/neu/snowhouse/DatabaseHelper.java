package com.neu.snowhouse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Arrays;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "user_table";
    private static final String COL1 = "userName";
    private static final String COL2 = "image";


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "( " +
                COL1 + " TEXT NOT NULL, " +
                COL2 + " BLOB NOT NULL )";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addUserImage(String userName, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, userName);
        contentValues.put(COL2, image);
        db.insert(TABLE_NAME, null, contentValues);
    }

    public Cursor getUserImage(String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL1 + " = '" + userName + "'";
        return db.rawQuery(query, null);
    }

    public void updateUserImage(String userName, byte[] image) {
        deleteUserImage(userName);
        addUserImage(userName, image);
    }

    public void deleteUserImage(String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL1 + " = '" + userName + "'";
        db.execSQL(query);
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}
