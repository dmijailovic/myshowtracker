package com.dusan.finish;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "shows";
    private static final int DB_VERSION = 1;

    public DBHelper (Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE shows (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "rating TEXT," +
                "favourite TEXT," +
                "type TEXT," +
                "date TEXT," +
                "genre TEXT);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void insertShow(String name, String rating, String favourite, String type, String date, String genre) {

        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("rating", rating);
        cv.put("favourite", favourite);
        cv.put("type", type);
        cv.put("date", date);
        cv.put("genre", genre);

        SQLiteDatabase db = getWritableDatabase();
        db.insert("shows", null, cv);
        db.close();
    }
}
