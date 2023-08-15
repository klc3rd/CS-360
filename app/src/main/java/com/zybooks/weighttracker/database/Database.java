package com.zybooks.weighttracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zybooks.weighttracker.DAO.User;
import com.zybooks.weighttracker.DAO.Weight;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "data.db";
    public static final int VERSION = 1;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static final class UserTable {
        private static final String TABLE = "users";
        private static final String COL_ID = "_id";
        public static final String COL_USERNAME = "username";
        public static final String COL_PASSWORD = "password";
        public static final String COL_GOAL = "goal";
        public static final String COL_SENDSMS = "sendsms";
        public static final String COL_PHONE = "phone";
    }

    private static final class WeightTable {
        private static final String TABLE = "weight";
        private static final String COL_ID = "_id";
        public static final String COL_USERNAME = "username";
        public static final String COL_DATE = "date";
        public static final String COL_WEIGHT = "weight";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + UserTable.TABLE + " (" +
                UserTable.COL_ID + " integer primary key autoincrement, " +
                UserTable.COL_USERNAME + " text, " +
                UserTable.COL_PASSWORD + " text, " +
                UserTable.COL_GOAL + " integer, " +
                UserTable.COL_SENDSMS + " integer, " +
                UserTable.COL_PHONE + " text)");

        db.execSQL("create table " + WeightTable.TABLE + " (" +
                WeightTable.COL_ID + " integer primary key autoincrement, " +
                WeightTable.COL_USERNAME + " text, " +
                WeightTable.COL_DATE + " text, " +
                WeightTable.COL_WEIGHT + " integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + UserTable.TABLE);
        onCreate(db);
    }

    public long addUser(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserTable.COL_USERNAME, username);
        values.put(UserTable.COL_PASSWORD, password);
        values.put(UserTable.COL_GOAL, 160);
        values.put(UserTable.COL_SENDSMS, 0);
        values.put(UserTable.COL_PHONE, "");

        long userId = db.insert(UserTable.TABLE, null, values);
        return userId;
    }

    public boolean doesUserExist(String username) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from " + UserTable.TABLE + " where username = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { username });
        if (cursor.moveToFirst()) {
            do {
                cursor.close();
                return true;
            } while (cursor.moveToNext());
        }
        cursor.close();

        return false;
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from " + UserTable.TABLE + " where username = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { username });
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String name = cursor.getString(1);
                String password = cursor.getString(2);
                int goal = cursor.getInt(3);
                int sendSMS = cursor.getInt(4);
                String phone = cursor.getString(5);
                cursor.close();

                return new User(id, name, password, goal, sendSMS, phone);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return null;
    }

    public int updateGoals(String username, int goal) {
      SQLiteDatabase db = getWritableDatabase();

      ContentValues values = new ContentValues();
      values.put(UserTable.COL_GOAL, goal);

      int rowsUpdated = db.update(UserTable.TABLE, values, "username = ?",
              new String[] { username });

      return rowsUpdated;
    }

    public long logWeight(String username, String date, int weight) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WeightTable.COL_DATE, date);
        values.put(WeightTable.COL_USERNAME, username);
        values.put(WeightTable.TABLE, weight);

        long weightId = db.insert(WeightTable.TABLE, null, values);
        return weightId;
    }

    public ArrayList<Weight> getWeights(String username) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from " + WeightTable.TABLE + " where username = ? ORDER BY _id DESC";
        ArrayList<Weight> weights = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, new String[] { username });

        if (cursor.moveToFirst()) {
            do {
                Weight newWeight = new Weight();
                newWeight.setId(cursor.getLong(0));
                newWeight.setUsername(cursor.getString(1));
                newWeight.setDate(cursor.getString(2));
                newWeight.setWeight(cursor.getInt(3));
                weights.add(newWeight);
            } while (cursor.moveToNext());
        }

        return weights;
    }

    public int getWeight(long id) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from " + WeightTable.TABLE + " where _id = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(id) });
        if (cursor.moveToFirst()) {
            do {
                int weight = cursor.getInt(3);

                return weight;

            } while (cursor.moveToNext());
        }
        cursor.close();

        return -1;
    }

    public int deleteWeight(long id) {
        SQLiteDatabase db = getWritableDatabase();

        int rowsUpdated = db.delete(WeightTable.TABLE, "_id = ?",
                new String[] { String.valueOf(id) });

        return rowsUpdated;
    }

    public int updateWeight(long id, int weight) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WeightTable.COL_WEIGHT, weight);

        int rowsUpdated = db.update(WeightTable.TABLE, values, "_id = ?",
                new String[] { String.valueOf(id) });

        return rowsUpdated;
    }
}
