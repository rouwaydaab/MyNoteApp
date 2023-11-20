package com.example.mynotepad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 2;


    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + NoteContract.TABLE_NAME + " (" +
                    NoteContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NoteContract.COLUMN_CONTENT + " TEXT NOT NULL, " +
                    NoteContract.COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    NoteContract.COLUMN_LOCKED + " INTEGER DEFAULT 0, " + // Assuming it's an integer column
                    NoteContract.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    NoteContract.COLUMN_IMG + " INTEGER)";


    public NoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Modify the schema to include the 'password' column
            db.execSQL("ALTER TABLE " + NoteContract.TABLE_NAME + " ADD COLUMN " + NoteContract.COLUMN_PASSWORD + " TEXT");
        }
    }

}