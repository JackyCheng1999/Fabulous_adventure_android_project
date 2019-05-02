package us.mrvivacio.fabulousadventure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper for SQLite, a light-weight local database for current use.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "Login.db", null, 1);
    }


    /**
     * Initialize the database file
     * @param db the database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table user(email text primary key, password text)");
    }

    /**
     * following upgrade behavior after the implementation of onCreate.
     * @param db the database
     * @param oldVersion old version
     * @param newVersion new version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
    }

    /**
     * function that helps inserting information into the SQLite database.
     * @param email User's email to be inserted
     * @param password User's password to be inserted
     * @return a boolean value denoting whether the insertion process is successful or not.
     */
    public boolean insert(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long ins = db.insert("user", null, contentValues);
        if (ins == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * function checking whether the email already exists in the database as primary key
     * @param email email address, as String
     * @return a boolean value denoting whether the email exists in the database as a primary key.
     */
    public boolean chkmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from user where email=?", new String[]{email});
        if (cursor.getCount() > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checking whether the inputed email and password match each other.
     * @param email inputed email address, as a String
     * @param password inputed password, as a string
     * @return a boolean value denoting whether the email and password match each other.
     */
    public boolean emailpassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user where email=? and password=?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
