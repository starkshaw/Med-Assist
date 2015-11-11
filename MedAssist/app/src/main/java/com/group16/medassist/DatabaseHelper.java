package com.group16.medassist;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TheDB";

    // Table Names
    private static final String TABLE_PRESCRIPTIONS = "prescriptions";

    // Common column names
    private static final String KEY_ID = "id";

    // Prescriptions Table - column nmaes
    private static final String KEY_DRUG = "drug";
    private static final String KEY_DATE_OF_ISSUE = "date_of_issue";
    private static final String KEY_QUANTITY = "quantity";

    // Prescription table create statement
    private static final String CREATE_TABLE_PRESCRIPTIONS = "CREATE TABLE "
            + TABLE_PRESCRIPTIONS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DRUG
            + " TEXT," + KEY_QUANTITY + " INTEGER," + KEY_DATE_OF_ISSUE
            + " DATETIME" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PRESCRIPTIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRESCRIPTIONS);
        onCreate(db);
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    // ------------------------ "prescriptions" table methods ---------------- //

    public long createPrescription(Prescription p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DRUG, p.drug);
        values.put(KEY_QUANTITY, p.quantity);
        values.put(KEY_DATE_OF_ISSUE, p.dateOfIssue);
        long prescription_id = db.insert(TABLE_PRESCRIPTIONS, null, values);
        return prescription_id;
    }

    public void deleteAllPrescriptions() {
        this.getWritableDatabase().delete(TABLE_PRESCRIPTIONS, null, null);
    }

    public List<Prescription> getAllPrescriptions() {
        List<Prescription> prescriptions = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_PRESCRIPTIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                Prescription p = new Prescription();
                p.id = c.getInt((c.getColumnIndex(KEY_ID)));
                p.drug = c.getString(c.getColumnIndex(KEY_DRUG));
                p.quantity = c.getInt(c.getColumnIndex(KEY_QUANTITY));
                p.dateOfIssue = c.getString(c.getColumnIndex(KEY_DATE_OF_ISSUE));
                prescriptions.add(p);
            } while (c.moveToNext());
        }
        return prescriptions;
    }

    public int updatePrescription(Prescription p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DRUG, p.drug);
        values.put(KEY_QUANTITY, p.quantity);
        values.put(KEY_DATE_OF_ISSUE, p.dateOfIssue);
        return db.update(TABLE_PRESCRIPTIONS, values, KEY_ID + " = ?", new String[] {String.valueOf(p.id)});
    }

    public void deletePrescription(Prescription p) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRESCRIPTIONS, KEY_ID + " = ?", new String[]{String.valueOf(p.id)});
    }

    // ------------------------ "prescriptions" table methods ---------------- //

}