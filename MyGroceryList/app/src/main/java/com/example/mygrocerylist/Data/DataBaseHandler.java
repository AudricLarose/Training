package com.example.mygrocerylist.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.mygrocerylist.Model.Grocery;
import com.example.mygrocerylist.Utils.util;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {
    private Context context;
    public DataBaseHandler(@Nullable Context context) {
        super(context, util.DBname, null, util.DBVersion);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CRREATE_TABLE="CREATE TABLE "+ util.tableName+"(" + util.KEY_ID+ "INTEGER PRIMARY KEY, " + util.KEY_GROCERY_ITEM+
                " TEXT," + util.KEY_QTY_NUMBER + " TEXT," + util.KEY_DATE_ + " LONG"+ ");";
        db.execSQL(CRREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ util.tableName);
        onCreate(db);
    }

    public void addGrocery(Grocery grocery){

    }

    public Grocery getGrocery(int id){
        return null
    }

    public void deletGrocery(Grocery grocery){

    }

    public int updateGrocery (Grocery grocery){
        return 0;
    }

    public List<Grocery> getAllGrocery(){
        SQLiteDatabase database=this.getReadableDatabase();
        List<Grocery> groceryList= new ArrayList<>();
        Cursor cursor= database.query(util.tableName,new String[] {util.KEY_ID, util.KEY_GROCERY_ITEM, util.KEY_QTY_NUMBER,util.KEY_DATE_},null,null,null,null, util.KEY_DATE_ + "DESC");
        if (cursor.moveToFirst()){
            do {
                Grocery grocery = new Grocery();
                grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(util.KEY_ID))));
                grocery.setName(cursor.getString(cursor.getColumnIndex(util.KEY_GROCERY_ITEM)));
                grocery.setQuality(cursor.getString(cursor.getColumnIndex(util.KEY_QTY_NUMBER)));

                java.text.DateFormat dateFormat= java.text.DateFormat.getDateInstance();
                String formatDate= dateFormat.format(new Date(cursor.getString(cursor.getColumnIndex(util.KEY_DATE_))).getTime());
                grocery.setDateItemAdded(formatDate);
                groceryList.add(grocery);


            } while (cursor.moveToNext());
        }
        return groceryList;
    }
}
