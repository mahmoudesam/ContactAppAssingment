package comf.mahmoud_esam.contactapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by mahmoud-esam on 5/30/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int databaseVersion = 1;
    private static final String databaseName = "company";
    private static final String tableName = "contacts";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_MAIL = "mail";


    public DatabaseHandler(Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACT_TABLE = " CREATE TABLE " + tableName + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + KEY_NAME + " TEXT NOT NULL , "
                + KEY_PHONE + " TEXT NOT NULL , "
                + KEY_MAIL + " TEXT NOT NULL ) ";
        sqLiteDatabase.execSQL(CREATE_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(sqLiteDatabase);
    }

    public void addContact(Contact contact) {
        SQLiteDatabase myDb = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PHONE, contact.getPhone());
        values.put(KEY_MAIL, contact.getMail());

        myDb.insert(tableName, null, values);

        myDb.close();

    }

    public Contact getContactByID(int contactID) {
        SQLiteDatabase myDb = this.getReadableDatabase();
        Cursor cursor = myDb.query
                (tableName, new String[]{KEY_NAME, KEY_PHONE, KEY_MAIL}, KEY_ID + " = ? ", new String[]{String.valueOf(contactID)},
                        null, null, null);
        Contact contact = null;
        if (cursor.moveToFirst()) {
            contact = new Contact(contactID, cursor.getString(0), cursor.getString(1), cursor.getString(2));
        }
        myDb.close();
        return contact;

    }

    public ArrayList<Contact> getContactByName(String name) {
        SQLiteDatabase myDb = this.getReadableDatabase();
        ArrayList<Contact> allContacts = new ArrayList<Contact>();
        String selectQuery = "SELECT *  FROM " + tableName + " where " + KEY_NAME + " LIKE '%"+name+"%' ";
        Cursor cursor = myDb.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                allContacts.add(contact);
            } while (cursor.moveToNext());
        }
        myDb.close();
        return allContacts;

    }

    public ArrayList<Contact> getAllContacts() {
        SQLiteDatabase myDb = this.getReadableDatabase();
        String selectAllQuery = "SELECT * FROM " + tableName;
        ArrayList<Contact> allContacts = new ArrayList<Contact>();
        Cursor cursor = myDb.rawQuery(selectAllQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                allContacts.add(contact);
            } while (cursor.moveToNext());
        }
        myDb.close();
        return allContacts;

    }

    public int deleteContactByID(int contactId) {
        SQLiteDatabase myDb = this.getWritableDatabase();
        int count = myDb.delete(tableName, KEY_ID + " = ?", new String[]{String.valueOf(contactId)});
        myDb.close();
        return count;
    }

    public void updateContactById(int contactId, String name) {

        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
         myDb.update(tableName, values, KEY_ID + " = ?",new String[] { String.valueOf(contactId) });
        myDb.close();


    }
}

