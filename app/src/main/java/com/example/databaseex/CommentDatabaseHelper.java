package com.example.databaseex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CommentDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "comments.db";
    private static final int DATABASE_VERSION = 2;

    public CommentDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE comments (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, text TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS comments");
        onCreate(db);
    }

    public void addComment(String name, String text) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("text", text);
        db.insert("comments", null, values);
        db.close();
    }

    public Cursor getComments() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id, name FROM comments", null);
    }

    public Cursor getCommentById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id, name, text FROM comments WHERE id = ?", new String[]{String.valueOf(id)});
    }

    public void deleteComment(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("comments", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
