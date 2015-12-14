package com.group16.medassist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TheDB";

    // Table Names
    private static final String TABLE_PRESCRIPTIONS = "prescriptions";
    private static final String TABLE_CONTACTS = "contacts";
    private static final String TABLE_DOSAGES = "dosages";
    private static final String TABLE_DOSAGE_REMINDERS = "dosage_reminders";
    private static final String TABLE_APPOINTMENTS = "appointments";

    // Common column names
    private static final String KEY_ID = "id";

    // Prescriptions Table - column names
    private static final String KEY_DRUG = "drug";
    private static final String KEY_DATE_OF_ISSUE = "date_of_issue";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_NOTES = "notes";
    private static final String KEY_CONTACT_ID = "contact_id";

    // Contacts Table - column names
    private static final String KEY_CONTACT_NAME = "contact_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ADDRESS = "address";

    // Dosages Table - column names
    private static final String KEY_PRESCRIPTION_ID = "prescription_id";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_INSTRUCTIONS = "instructions";

    // Dosage Reminders Table - column names
    private static final String KEY_DOSAGE_ID = "dosage_id";
    private static final String KEY_DAY = "day";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";

    // Appointments table - column names
    //private static final String KEY_DOCTOR="doctor";
    private static final String KEY_CONTACT_ID_A ="contact_id_a";
    private static final String KEY_START_TIME = "startTime";
    private static final String KEY_END_TIME = "endTime";
    private static final String KEY_DATE_OF_APPOINTMENT = "appointment date";

    // Prescription table create statement
    private static final String CREATE_TABLE_PRESCRIPTIONS =
            "CREATE TABLE " + TABLE_PRESCRIPTIONS + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_DRUG + " TEXT," +
            KEY_NOTES + " TEXT," +
            KEY_QUANTITY + " INTEGER," +
            KEY_DATE_OF_ISSUE + " DATETIME," +
            KEY_CONTACT_ID + " INTEGER," +
            "FOREIGN KEY(" + KEY_CONTACT_ID + ") REFERENCES " + TABLE_CONTACTS + "(" + KEY_ID + "))";

    // Contacts table create statement
    private static final String CREATE_TABLE_CONTACTS =
            "CREATE TABLE " + TABLE_CONTACTS + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_CONTACT_NAME + " TEXT," +
            KEY_EMAIL + " TEXT," +
            KEY_PHONE + " TEXT," +
            KEY_ADDRESS + " TEXT" + ")";

    // Dosages table create statement
    private static final String CREATE_TABLE_DOSAGES =
            "CREATE TABLE " + TABLE_DOSAGES + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_PRESCRIPTION_ID + " INTEGER," +
            KEY_AMOUNT + " INTEGER," +
            KEY_INSTRUCTIONS + " TEXT," +
            "FOREIGN KEY(" + KEY_PRESCRIPTION_ID + ") REFERENCES " + TABLE_PRESCRIPTIONS + "(" + KEY_ID + "))";

    // Dosage Reminders table create statement
    private static final String CREATE_TABLE_DOSAGE_REMINDERS = "CREATE TABLE " + TABLE_DOSAGE_REMINDERS + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_DOSAGE_ID + " INTEGER," +
            KEY_DAY + " INTEGER," +
            KEY_HOUR + " INTEGER," +
            KEY_MINUTE + " INTEGER," +
            "FOREIGN KEY(" + KEY_DOSAGE_ID + ") REFERENCES " + TABLE_DOSAGES + "(" + KEY_ID + "))";

    // Appointments table create statement
    private static final String CREATE_TABLE_APPOINTMENTS = "CREATE TABLE "
            + TABLE_APPOINTMENTS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CONTACT_ID_A
            + " TEXT," + KEY_START_TIME + " TEXT," + KEY_END_TIME
            + " TEXT" + KEY_DATE_OF_APPOINTMENT + " DATETIME" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTACTS);
        db.execSQL(CREATE_TABLE_PRESCRIPTIONS);
        db.execSQL(CREATE_TABLE_DOSAGES);
        db.execSQL(CREATE_TABLE_DOSAGE_REMINDERS);
        db.execSQL(CREATE_TABLE_APPOINTMENTS);

        /*for(Contact c : Arrays.asList(new Contact("Daniel Berrigan", "danielberrigan@live.ie", "0864042635", "6 Prospect, Mullingar"),
                                        new Contact("John Smith", "johnsmith@live.ie", "0861234567", "Grange Park, Mullingar"),
                                        new Contact("Omar", "omar@gmail.ie", "4935345345", "Tallaght DUBLIN"))) {
            ContentValues values = new ContentValues();
            values.put(KEY_CONTACT_NAME, c.name);
            values.put(KEY_PHONE, c.phone);
            values.put(KEY_EMAIL, c.email);
            values.put(KEY_ADDRESS, c.address);
            db.insert(TABLE_CONTACTS, null, values);
        }*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOSAGE_REMINDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRESCRIPTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public void closeDB() {
        SQLiteDatabase db = getReadableDatabase();
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
        values.put(KEY_CONTACT_ID, p.contact.id);
        values.put(KEY_NOTES, p.notes);
        return db.insert(TABLE_PRESCRIPTIONS, null, values);
    }

    public void deleteAllPrescriptions(Context context) {
        SQLiteDatabase db = getReadableDatabase();
        List<Integer> prescriptionIds = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT " + KEY_ID + " FROM " + TABLE_PRESCRIPTIONS, null);
        if(c.moveToFirst())
            do {
                prescriptionIds.add(c.getInt(0));
            } while(c.moveToNext());
        c.close();
        if(!prescriptionIds.isEmpty()) {
            System.out.println("Deleting prescriptions with ids " + prescriptionIds);
            for(Integer id : prescriptionIds)
                deletePrescription(context, id);
        }
    }

    public Prescription getPrescriptionById(int id) {
        String selectQuery = "SELECT  * FROM " +
                TABLE_PRESCRIPTIONS + " INNER JOIN " + TABLE_CONTACTS +
                " ON (" + TABLE_PRESCRIPTIONS + "." + KEY_CONTACT_ID + " = " + TABLE_CONTACTS + "." + KEY_ID + ")" +
                " WHERE " + TABLE_PRESCRIPTIONS + "." + KEY_ID + " = '" + id + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) return getPrescription(c);
        else System.out.println("No prescription found for id " + id);
        return null;
    }

    public List<Prescription> getAllPrescriptions() {
        List<Prescription> prescriptions = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " +
                TABLE_PRESCRIPTIONS + " INNER JOIN " + TABLE_CONTACTS +
                " ON (" + TABLE_PRESCRIPTIONS + "." + KEY_CONTACT_ID + " = " + TABLE_CONTACTS + "." + KEY_ID + ");";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                prescriptions.add(getPrescription(c));
            } while (c.moveToNext());
        }
        return prescriptions;
    }

    public Prescription getPrescription(Cursor c) {
        int drug_id = c.getInt(0);
        String drug_name = c.getString(c.getColumnIndex(KEY_DRUG));
        int drug_quantity = c.getInt(c.getColumnIndex(KEY_QUANTITY));
        String dateOfIssue = c.getString(c.getColumnIndex(KEY_DATE_OF_ISSUE));
        String notes = c.getString(c.getColumnIndex(KEY_NOTES));

        int contact_id = c.getInt(c.getColumnIndex(KEY_CONTACT_ID));
        String contact_name = c.getString(c.getColumnIndex(KEY_CONTACT_NAME));
        String phone = c.getString(c.getColumnIndex(KEY_PHONE));
        String email = c.getString(c.getColumnIndex(KEY_EMAIL));
        String address = c.getString(c.getColumnIndex(KEY_ADDRESS));

        Contact contact = new Contact(contact_id, contact_name, email, phone, address);
        return new Prescription(drug_id, drug_name, drug_quantity, dateOfIssue, notes, contact);
    }

    public void takeDoseFromPrescription(int id, int doseAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_PRESCRIPTIONS +
                " SET " + KEY_QUANTITY + " = " + KEY_QUANTITY + " - " + doseAmount +
                " WHERE " + KEY_ID + " = " + id;
        System.out.println("Executing take dosage: " + query);
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        c.close();
    }

    public int getNumberOfDisabledDosages(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_DOSAGES +
                " WHERE " + KEY_PRESCRIPTION_ID + " = " + id +
                " AND " + KEY_AMOUNT + " > (SELECT " + KEY_QUANTITY +
                                            " FROM " + TABLE_PRESCRIPTIONS +
                                            " WHERE " + KEY_ID + " = " + id +")";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int n = c.getInt(0);
        c.close();
        return n;
    }

    public int updatePrescription(Prescription p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DRUG, p.drug);
        values.put(KEY_QUANTITY, p.quantity);
        values.put(KEY_DATE_OF_ISSUE, p.dateOfIssue);
        return db.update(TABLE_PRESCRIPTIONS, values, KEY_ID + " = ?", new String[]{String.valueOf(p.id)});
    }

    public void refillPrescription(int id, int refillAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_PRESCRIPTIONS +
                    " SET " + KEY_QUANTITY + " = " + KEY_QUANTITY + " + " + refillAmount +
                    " WHERE " + KEY_ID + " = " + id;
        System.out.println("Executing update: " + query);
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        c.close();
    }

    public void deletePrescription(Context context, int prescriptionId) {
        System.out.println("Deleting prescription with id: " + prescriptionId);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRESCRIPTIONS, KEY_ID + " = ?", new String[]{String.valueOf(prescriptionId)});
        deleteDosages(context, prescriptionId);
    }

    // ------------------------ "prescriptions" table methods ---------------- //

    // ------------------------ "contacts" table methods ---------------- //

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                int id = c.getInt((c.getColumnIndex(KEY_ID)));
                String contact_name = c.getString(c.getColumnIndex(KEY_CONTACT_NAME));
                String phone = c.getString(c.getColumnIndex(KEY_PHONE));
                String email = c.getString(c.getColumnIndex(KEY_EMAIL));
                String address = c.getString(c.getColumnIndex(KEY_ADDRESS));
                contacts.add(new Contact(id, contact_name, email, phone, address));
            } while (c.moveToNext());
        }
        c.close();
        return contacts;
    }
    public Contact getContact(Cursor b) {

        int contact_id = b.getInt(b.getColumnIndex(KEY_ID));
        String contact_name = b.getString(b.getColumnIndex(KEY_CONTACT_NAME));
        String phone = b.getString(b.getColumnIndex(KEY_PHONE));
        String email = b.getString(b.getColumnIndex(KEY_EMAIL));
        String address = b.getString(b.getColumnIndex(KEY_ADDRESS));

        //Contact contact = new Contact(contact_id, contact_name, email, phone, address);
        return new Contact(contact_id, contact_name, email, phone, address);
    }


    public long createContact(Contact c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_CONTACT_NAME, c.name);
        values.put(KEY_PHONE, c.phone);
        values.put(KEY_EMAIL, c.email);
        values.put(KEY_ADDRESS, c.address);
        return db.insert(TABLE_CONTACTS, null, values);
    }

    public int updateContact(Contact c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CONTACT_NAME, c.name);
        values.put(KEY_PHONE, c.phone);
        values.put(KEY_EMAIL, c.email);
        values.put(KEY_ADDRESS, c.address);
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?", new String[]{String.valueOf(c.id)});
    }

    public void deleteAllContacts(Context context) {
        SQLiteDatabase db = getReadableDatabase();
        List<Integer> contactsId = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT " + KEY_ID + " FROM " + TABLE_CONTACTS, null);
        if(c.moveToFirst())
            do {
                contactsId.add(c.getInt(0));
            } while(c.moveToNext());
        c.close();
        if(!contactsId.isEmpty()) {
            System.out.println("Deleting contacts with ids " + contactsId);
            for(Integer id : contactsId)
                deleteContact(context, id);
        }
    }

    public void deleteContact(Context context, int contactsId) {
        System.out.println("Deleting contact with id: " + contactsId);
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  " + KEY_ID + " FROM " +
                TABLE_PRESCRIPTIONS + " WHERE " + KEY_CONTACT_ID + " = '" + contactsId + "';";
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                deletePrescription(context, c.getInt(0));
            } while(c.moveToNext());
        }
        c.close();
        selectQuery = "SELECT  " + KEY_ID + " FROM " +
                TABLE_APPOINTMENTS + " WHERE " + KEY_CONTACT_ID_A + " = '" + contactsId + "';";
        c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                Appointment a = new Appointment();
                a.id = c.getInt(0);
                deleteAppointment(a);
            } while(c.moveToNext());
        }
        c.close();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?", new String[]{String.valueOf(contactsId)});
    }

    public Contact getContactById(int id) {
        String selectQuery = "SELECT  * FROM " +
                TABLE_CONTACTS + " WHERE " + KEY_ID + " = '" + id + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) return getContact(c);
        else System.out.println("No contact found for id " + id);
        return null;
    }

    public int getNumberOfContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_CONTACTS, null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        return count;
    }

    // ------------------------ "contacts" table methods ---------------- //

    // ------------------------ "dosage" table methods ---------------- //

    public void deleteDosages(Context context, int prescriptionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Dosage> dosages = getDosages(prescriptionId);
        List<String> dosageIds = new ArrayList<>();
        for(Dosage dosage : dosages) {
            dosageIds.add(String.valueOf(dosage.dosageId));
            deleteDosageReminders(context, dosage.dosageId);
        }

        if(!dosageIds.isEmpty()) {
            System.out.println("Deleting dosages with ids: " + dosageIds);
            String[] args = dosageIds.toArray(new String[dosageIds.size()]);
            StringBuilder placeholders = new StringBuilder();
            for(int i = 0; i < args.length; ++i) placeholders.append(i!=0?",?":"?");
            db.delete(TABLE_DOSAGES, KEY_ID + " IN (" + placeholders.toString() + ")", args);
        }
    }

    public boolean isDosageDisabled(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + KEY_QUANTITY + "," + KEY_AMOUNT +
                " FROM " + TABLE_DOSAGES + " INNER JOIN " + TABLE_PRESCRIPTIONS +
                " ON " + TABLE_PRESCRIPTIONS + "." + KEY_ID + " = " + KEY_PRESCRIPTION_ID +
                " WHERE " + TABLE_DOSAGES + "." + KEY_ID + " = " + id;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        boolean disabled = c.getInt(c.getColumnIndex(KEY_QUANTITY)) < c.getInt(c.getColumnIndex(KEY_AMOUNT));
        c.close();
        return disabled;
    }

    public void deleteDosage(Context context, int dosageId) {
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("Deleting dosage with id: " + dosageId);
        db.delete(TABLE_DOSAGES, KEY_ID + " = ?", new String[]{String.valueOf(dosageId)});
        deleteDosageReminders(context, dosageId);
    }

    public long createDosage(int prescriptionId, int dosageAmount, String instructions) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PRESCRIPTION_ID, prescriptionId);
        values.put(KEY_AMOUNT, dosageAmount);
        values.put(KEY_INSTRUCTIONS, instructions);
        return db.insert(TABLE_DOSAGES, null, values);
    }

    public void updateDosage(int dosageId, int amount, String instructions) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, amount);
        values.put(KEY_INSTRUCTIONS, instructions);
        db.update(TABLE_DOSAGES, values, KEY_ID + " = ?", new String[]{String.valueOf(dosageId)});
    }

    public Dosage getDosageById(int id) {
        String selectQuery = "SELECT  * FROM " + TABLE_DOSAGES +
                " WHERE " + KEY_ID + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        Dosage d = null;
        if(c.moveToFirst()) {
            int amount = c.getInt(c.getColumnIndex(KEY_AMOUNT));
            String instructions = c.getString(c.getColumnIndex(KEY_INSTRUCTIONS));
            int prescription_id = c.getInt(c.getColumnIndex(KEY_PRESCRIPTION_ID));
            d = new Dosage(id, prescription_id, amount, instructions);
        }
        c.close();
        return d;
    }

    public List<Dosage> getDosages(int prescription) {
        List<Dosage> dosages = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_DOSAGES +
                " WHERE " + KEY_PRESCRIPTION_ID + " = " + prescription;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                int amount = c.getInt(c.getColumnIndex(KEY_AMOUNT));
                int id = c.getInt(c.getColumnIndex(KEY_ID));
                String instructions = c.getString(c.getColumnIndex(KEY_INSTRUCTIONS));
                dosages.add(new Dosage(id, prescription, amount, instructions));
            } while (c.moveToNext());
        }
        c.close();
        return dosages;
    }

    // ------------------------ "dosage" table methods ---------------- //

    // ------------------------ "dosage_reminder" table methods ---------------- //

    public void deleteDosageReminders(Context context, int dosageId) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> dosageReminderIds = new ArrayList<>();
        String query = "SELECT * " +
                " FROM " + TABLE_DOSAGE_REMINDERS +
                " WHERE " + KEY_DOSAGE_ID + " = " + dosageId;
        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst())
            do {
                int id = c.getInt(c.getColumnIndex(KEY_ID));
                dosageReminderIds.add(String.valueOf(id));
                cancelDosageReminder(context, id);
            } while (c.moveToNext());
        c.close();

        if(!dosageReminderIds.isEmpty()) {
            System.out.println("Deleting dosage reminders with ids: " + dosageReminderIds);
            String[] args = dosageReminderIds.toArray(new String[dosageReminderIds.size()]);
            StringBuilder placeholders = new StringBuilder();
            for(int i = 0; i < args.length; ++i) placeholders.append(i!=0?",?":"?");
            db.delete(TABLE_DOSAGE_REMINDERS, KEY_ID + " IN (" + placeholders.toString() + ")", args);
        }
    }

    public static void cancelDosageReminder(Context context, int id) {
        System.out.println("Cancelling dosage reminder " + id);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MedicationReminderReceiver.class);
        intent.putExtra("reminderID", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
    }

    public long createDosageReminder(int dosageId, int day, int hour, int minute) {
        SQLiteDatabase db = this.getWritableDatabase();

        // check if this reminder exists already
        String query = "SELECT * FROM " + TABLE_DOSAGE_REMINDERS +
                " WHERE " + KEY_DOSAGE_ID + " = " + dosageId +
                " AND " + KEY_DAY + " = " + day +
                " AND " + KEY_HOUR + " = " + hour +
                " AND " + KEY_MINUTE + " = " + minute;
        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst()) return -1;
        c.close();

        ContentValues values = new ContentValues();
        values.put(KEY_DOSAGE_ID, dosageId);
        values.put(KEY_DAY, day);
        values.put(KEY_HOUR, hour);
        values.put(KEY_MINUTE, minute);
        return db.insert(TABLE_DOSAGE_REMINDERS, null, values);
    }

    public Dosage.Reminder getDosageReminderById(int id) {
        Dosage.Reminder reminder = new Dosage.Reminder();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * " +
                    " FROM " + TABLE_DOSAGE_REMINDERS +
                    " WHERE " + KEY_ID + " = " + id;
        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst())
            do {
                reminder.hour = c.getInt(c.getColumnIndex(KEY_HOUR));
                reminder.minute = c.getInt(c.getColumnIndex(KEY_MINUTE));
                reminder.day = c.getInt(c.getColumnIndex(KEY_DAY));
            } while (c.moveToNext());
        c.close();
        return reminder;
    }

    public Map<String, String> getReminder(int reminderId) {
        SQLiteDatabase db = getReadableDatabase();
        Map<String, String> reminder = new HashMap<>();
        String query = "SELECT " + TABLE_PRESCRIPTIONS + "." + KEY_ID + "," + KEY_QUANTITY + "," + KEY_DRUG + "," + KEY_INSTRUCTIONS + "," + KEY_AMOUNT + "," + KEY_HOUR + "," + KEY_MINUTE +
                        " FROM " + TABLE_DOSAGE_REMINDERS +
                        " INNER JOIN " + TABLE_DOSAGES +
                        " ON " + KEY_DOSAGE_ID + " = " + TABLE_DOSAGES + "." + KEY_ID +
                        " INNER JOIN " + TABLE_PRESCRIPTIONS +
                        " ON " + TABLE_PRESCRIPTIONS + "." + KEY_ID + " = " + KEY_PRESCRIPTION_ID +
                        " WHERE " + TABLE_DOSAGE_REMINDERS + "." + KEY_ID + " = " + reminderId;
        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst())
            do {
                reminder.put("prescription_id", c.getString(0));
                reminder.put("prescriptionQuantity", c.getString(c.getColumnIndex(KEY_QUANTITY)));
                reminder.put("drug", c.getString(c.getColumnIndex(KEY_DRUG)));
                reminder.put("instructions", c.getString(c.getColumnIndex(KEY_INSTRUCTIONS)));
                reminder.put("amount", c.getString(c.getColumnIndex(KEY_AMOUNT)));
                reminder.put("hour", c.getString(c.getColumnIndex(KEY_HOUR)));
                reminder.put("minute", c.getString(c.getColumnIndex(KEY_MINUTE)));
            } while(c.moveToNext());
        c.close();
        return reminder;
    }

   public List<DosageReminderToPrint> getDosageReminders(int dosageId) {
       SQLiteDatabase db = getReadableDatabase();
       List<DosageReminderToPrint> list = new ArrayList<>();
       String query = "SELECT DISTINCT " + KEY_HOUR + "," + KEY_MINUTE +
                       " FROM " + TABLE_DOSAGE_REMINDERS +
                       " WHERE " + KEY_DOSAGE_ID + " = " + dosageId;
       Cursor c = db.rawQuery(query, null);
       if(c.moveToFirst())
           do {
               DosageReminderToPrint d = new DosageReminderToPrint();
               d.hour = c.getInt(c.getColumnIndex(KEY_HOUR));
               d.minute = c.getInt(c.getColumnIndex(KEY_MINUTE));
               d.dosage_id = dosageId;
               list.add(d);
           } while (c.moveToNext());
       for(DosageReminderToPrint d : list) {
           query = "SELECT " + KEY_DAY + "," + KEY_ID +
                   " FROM " + TABLE_DOSAGE_REMINDERS +
                   " WHERE " + KEY_HOUR + " = " + d.hour +
                   " AND " + KEY_MINUTE + " = " + d.minute +
                   " AND " + KEY_DOSAGE_ID + " = " + dosageId +
                   " ORDER BY " + KEY_DAY;
           c = db.rawQuery(query, null);
           if(c.moveToFirst())
               do {
                   d.reminderRowIds.add(c.getInt(c.getColumnIndex(KEY_ID)));
                   d.days.add(c.getInt(c.getColumnIndex(KEY_DAY)));
               } while (c.moveToNext());
           c.close();
       }
       return list;
    }
    // ------------------------ "dosage_reminder" table methods ---------------- //

    // ------------------------ "appointments" table methods ----------------------//
    public long createAppointment(Appointment a) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(KEY_DOCTOR, a.contact);
        values.put(KEY_CONTACT_ID_A, a.contact.id);
        values.put(KEY_START_TIME, a.startTime);
        values.put(KEY_END_TIME, a.endTime);
        return db.insert(TABLE_APPOINTMENTS, null, values);
    }

    public void deleteAllAppointments()
    {

        this.getWritableDatabase().delete(TABLE_APPOINTMENTS, null, null);
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        //String selectQuery = "SELECT  * FROM " + TABLE_APPOINTMENTS;
        String selectQuery = "SELECT  * FROM " +
                TABLE_APPOINTMENTS + " INNER JOIN " + TABLE_CONTACTS +
                " ON (" + TABLE_APPOINTMENTS + "." + KEY_CONTACT_ID_A + " = " + TABLE_CONTACTS + "." + KEY_ID + ");";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                /*Appointment a = new Appointment();
                a.id = c.getInt((c.getColumnIndex(KEY_ID)));
                a.contact = c.getString(c.getColumnIndex(KEY_CONTACT_ID_A));
                a.startTime = c.getString(c.getColumnIndex(KEY_START_TIME));
                a.endTime = c.getString(c.getColumnIndex(KEY_END_TIME));
                appointments.add(a);*/
                appointments.add(getAppointment(c));
            } while (c.moveToNext());
        }
        return appointments;
    }
    public Appointment getAppointment(Cursor c) {
        //int drug_id = c.getInt(0);
        int id = c.getInt(0/*c.getColumnIndex(TABLE_APPOINTMENTS + "." + KEY_ID)*/);
        int contact_id = c.getInt(c.getColumnIndex(KEY_CONTACT_ID_A));
        String contact_name = c.getString(c.getColumnIndex(KEY_CONTACT_NAME));
        String startTime = c.getString(c.getColumnIndex(KEY_START_TIME));
        String endTime = c.getString(c.getColumnIndex(KEY_END_TIME));
        String phone = c.getString(c.getColumnIndex(KEY_PHONE));
        String email = c.getString(c.getColumnIndex(KEY_EMAIL));
        String address = c.getString(c.getColumnIndex(KEY_ADDRESS));

        Contact contact = new Contact(contact_id, contact_name, email, phone, address);
        return new Appointment(id, contact, startTime, endTime);
    }
    public Appointment getAppointmentById(int id) {
        String selectQuery = "SELECT  * FROM " +
                TABLE_APPOINTMENTS + " WHERE " + KEY_ID + " = '" + id + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) return getAppointment(c);
        else System.out.println("No appointment found for id " + id);
        return null;
    }
    public int updateAppointment(Appointment a) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(KEY_CONTACT_ID_A, a.contact);                  //MAY NEED TO BE PUT BACK IN
        values.put(KEY_START_TIME, a.startTime);
        values.put(KEY_END_TIME, a.endTime);
        return db.update(TABLE_PRESCRIPTIONS, values, KEY_ID + " = ?", new String[] {String.valueOf(a.id)});
    }
    public void deleteAppointment(Appointment a) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_APPOINTMENTS, KEY_ID + " = ?", new String[]{String.valueOf(a.id)});
    }

    // ------------------------ "appointments" table methods ----------------------//

}