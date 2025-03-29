package com.example.calendarhours;

import java.util.ArrayList;
import java.util.Objects;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "hours.db"; // название бд
    private static final int SCHEMA = 2; // версия базы данных
    public static final String TABLE = "hours"; // название таблицы в бд
    // названия столбцов
    //public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MONTH_YEAR = "month_year";
    public static final String COLUMN_HOURS = "hour";
    //public static final String COLUMN_QUANTITY_HOURS = "quantity_hour";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table hours " +
                        "(id integer primary key, month_year text, hour text)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void insertContact(String month_year, String hours) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("month_year", month_year);
        contentValues.put("hour", hours);
        //contentValues.put("quantity_hour", quantity_hour);
        db.insert("hours", null, contentValues);
    }

    public boolean updateHours(Integer id, String month_year, String hours) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("month_year", month_year);
        contentValues.put("hour", hours);
        //contentValues.put("quantity_hour", quantity_hour);
        db.update("hours", contentValues, "id = ? ", new String[]{Integer.toString(id)});

        return true;
    }

    public Integer deleteContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("hours",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<String> getAllRows() {
        ArrayList<String> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor res = db.rawQuery("select * from hours", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(String.valueOf(1))));
            res.moveToNext();
        }
        return array_list;
    }

    public boolean checkDataExistOrNot(String value) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        //String query = "SELECT * FROM " + TABLE + " WHERE " + COLUMN_MONTH_YEAR + " = " + value;
        String query = "select * from hours" + " where " + COLUMN_MONTH_YEAR + " like ?";

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{"%" + value + "%"});
        //Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;  // return false if value not exists in database
        }
        cursor.close();
        return true;  // return true if value exists in database
    }

    public String getHours(String month_year) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from hours" + " where " + COLUMN_MONTH_YEAR + " like ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + month_year + "%"});
        if (cursor.getCount() < 1) {
            cursor.close();
            return "DOES NOT EXIST";
        }
        cursor.moveToFirst();
        while (!Objects.equals(month_year, cursor.getString(1))) {
            cursor.moveToNext();
        }

        String hours = cursor.getString(cursor.getColumnIndex(COLUMN_HOURS));
        cursor.close();
        return hours;
    }
    public int GetId(String currentNote) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor getNoteId = myDB.rawQuery("select id from hours where month_year = '" + currentNote + "'", null);
        //Cursor getNoteId = myDB.rawQuery("select id from notepadData where notepad like + "'" + currentNote + "'", null);
        if (getNoteId != null && getNoteId.moveToFirst()) {
            return getNoteId.getInt(0);
        } else {
            return Integer.parseInt(null);  // because you have to return something
        }
    }
}
