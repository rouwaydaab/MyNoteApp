
package com.example.mynotepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class NoteDataSource {
    private SQLiteDatabase database;
    private NoteDbHelper dbHelper;

    public NoteDataSource(Context context) {
        dbHelper = new NoteDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long createNote(String content, String password) {
        ContentValues values = new ContentValues();
        values.put(NoteContract.COLUMN_CONTENT, content);
        values.put(NoteContract.COLUMN_PASSWORD, password);
        return database.insert(NoteContract.TABLE_NAME, null, values);
    }

    public int updateNote(long noteId, String content, String password) {
        ContentValues values = new ContentValues();
        values.put(NoteContract.COLUMN_CONTENT, content);
        values.put(NoteContract.COLUMN_PASSWORD, password);
        String whereClause = NoteContract.COLUMN_ID + " = ?";
        String[] whereArgs = { String.valueOf(noteId) };
        return database.update(NoteContract.TABLE_NAME, values, whereClause, whereArgs);
    }

    public void deleteNote(long noteId) {
        String whereClause = NoteContract.COLUMN_ID + " = ?";
        String[] whereArgs = { String.valueOf(noteId) };
        database.delete(NoteContract.TABLE_NAME, whereClause, whereArgs);
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        Cursor cursor = database.query(
                NoteContract.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Note note = cursorToNote(cursor);
                    notes.add(note);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        return notes;
    }

    public Note getNoteById(long noteId) {
        Cursor cursor = null;
        Note note = null;

        try {
            String[] projection = {
                    NoteContract.COLUMN_ID,
                    NoteContract.COLUMN_CONTENT,
                    NoteContract.COLUMN_PASSWORD,
                    NoteContract.COLUMN_LOCKED,
                    NoteContract.COLUMN_TIMESTAMP,
                    NoteContract.COLUMN_IMG
            };

            String selection = NoteContract.COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(noteId) };

            cursor = database.query(
                    NoteContract.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                note = cursorToNote(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return note;
    }

    private Note cursorToNote(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(NoteContract.COLUMN_ID);
        int contentIndex = cursor.getColumnIndex(NoteContract.COLUMN_CONTENT);
        int passwordIndex = cursor.getColumnIndex(NoteContract.COLUMN_PASSWORD);
        int timestampIndex = cursor.getColumnIndex(NoteContract.COLUMN_TIMESTAMP);
        int imgIndex = cursor.getColumnIndex(NoteContract.COLUMN_IMG);
        int lockedIndex = cursor.getColumnIndex(NoteContract.COLUMN_LOCKED);

        long id = cursor.getLong(idIndex);
        String content = (contentIndex >= 0) ? cursor.getString(contentIndex) : "";
        String password = (passwordIndex >= 0) ? cursor.getString(passwordIndex) : "";
        String timestamp = (timestampIndex >= 0) ? cursor.getString(timestampIndex) : "";
        int imgResourceId = (imgIndex >= 0) ? cursor.getInt(imgIndex) : 0;
        boolean isLocked = (lockedIndex >= 0) && cursor.getInt(lockedIndex) > 0;

        return new Note(id, content, timestamp, password, isLocked, imgResourceId);
    }
}
