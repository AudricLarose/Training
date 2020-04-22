package Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.renderscript.Sampler;
import android.util.Log;
import android.webkit.URLUtil;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import Model.Contact;
import Utils.utils;

import static android.content.ContentValues.TAG;

public class DataBaseHandlers extends SQLiteOpenHelper {
    public DataBaseHandlers(@Nullable Context context) {
        super(context, utils.DATA_NAME, null, utils.DATEBASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL
        String CREATE_CONTACT_TABLE= "CREATE TABLE " + utils.TABLE_NAME + "("
                + utils.KEY_ID + " INTEGER PRIMARY KEY," + utils.Key_NAME + " TEXT,"
                + "phone_number" + " TEXT" + ")";
        db.execSQL(CREATE_CONTACT_TABLE);
        Log.d(TAG, "onCreate: "+ CREATE_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "  + utils.TABLE_NAME);
        onCreate(db);
    }
    public void addContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value= new ContentValues();
        value.put(utils.Key_NAME, contact.getName());
        value.put(utils.KEY_PHONE_NUMBER, contact.getPhoneNumber());
        db.insert(utils.TABLE_NAME,null, value);
        db.close();
        }
        public Contact getContact(int id){
        SQLiteDatabase db= this.getReadableDatabase();
             Cursor cursor=db.query(utils.TABLE_NAME, new String[] {utils.KEY_ID, utils.Key_NAME, utils.KEY_PHONE_NUMBER},
                     utils.KEY_ID + "=?", new String[] {String.valueOf(id)},null,null,null,null);
             if (cursor!=null)
                 cursor.moveToFirst();

                 Contact contact= new Contact(Integer.parseInt(cursor.getString(0)),
                         cursor.getString(1),cursor.getString(2));
             return contact;
         }

         public List<Contact> getAllContact() {
             SQLiteDatabase db= this.getReadableDatabase();
             List<Contact> contactList= new ArrayList<>();
             String selectAll = " SELECT * FROM " + utils.TABLE_NAME;
             Cursor cursor=db.rawQuery(selectAll,null);

             if (cursor.moveToFirst()){
                 do {
                     Contact contact= new Contact();
                     contact.setId(Integer.parseInt(cursor.getString(0)));
                     contact.setName(cursor.getString(1));
                     contact.setPhoneNumber(cursor.getString(2));
                     contactList.add(contact);
                 } while (cursor.moveToNext());

         }
             return contactList;
         }
         public int upDateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(utils.Key_NAME, contact.getName());
        values.put(utils.KEY_PHONE_NUMBER, contact.getPhoneNumber());
        return db.update(utils.TABLE_NAME,values,utils.KEY_ID + "=?",
                new String[] {String.valueOf(contact.getId())});
         }

         public void delete ( Contact contact) {
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete(utils.TABLE_NAME,utils.KEY_ID + "=?", new String[]{String.valueOf(contact.getId())});
        db.close();
         }
}
