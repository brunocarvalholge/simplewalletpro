package br.com.tolive.simplewalletpro.db;

/**
 * Created by bruno.carvalho on 27/06/2014.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import br.com.tolive.simplewalletpro.model.Category;
import br.com.tolive.simplewalletpro.model.Entry;

public class EntryDAO {
    private static EntryDAO mInstance = null;
    private SQLiteOpenHelper mHelper;

    private EntryDAO(Context context) {
        this.mHelper = new Helper(context);
    }

    public static EntryDAO getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new EntryDAO(context);
        }
        return mInstance;
    }

    public long insert(Entry entry){
        ContentValues values = new ContentValues();

        values.put(Entry.DESCRIPTION, entry.getDescription());
        values.put(Entry.VALUE, entry.getValue());
        values.put(Entry.TYPE, entry.getType());
        values.put(Entry.CATEGORY, entry.getCategory());
        values.put(Entry.DATE, entry.getDate());
        values.put(Entry.MONTH, entry.getMonth());

        return insert(values);
    }

    private synchronized long insert(ContentValues values){
        SQLiteDatabase db = mHelper.getWritableDatabase();

        long id = db.insert(Entry.ENTITY_NAME, null, values);

        db.close();

        return id;
    }

    public long delete(long id){
        String whereClause = String.format("%s=?", Entry.ID);
        String[] whereArgs = { String.valueOf(id) };

        return delete(whereClause, whereArgs);
    }

    private long delete(String whereClause, String[] whereArgs) {

        SQLiteDatabase db = mHelper.getReadableDatabase();

        long id = db.delete(Entry.ENTITY_NAME, whereClause, whereArgs);

        return id;
    }

    public ArrayList<Entry> getEntry(int month){
        String selection = String.format("SELECT * FROM %s WHERE %s = ? ", Entry.ENTITY_NAME, Entry.MONTH);
        String[] selectionArgs = { String.valueOf(month) };

        return getEntry(selection, selectionArgs);
    }

    public ArrayList<Entry> getEntry(String month, String year){
        if(year == null && month == null){
            String selection = String.format("SELECT * FROM %s", Entry.ENTITY_NAME);
            String[] selectionArgs = {};
            return getEntry(selection, selectionArgs);
        } else if(year == null){
            String selection = String.format("SELECT * FROM %s WHERE %s LIKE ?", Entry.ENTITY_NAME, Entry.DATE);
            String[] selectionArgs = {"%/" + month + "/%"};
            return getEntry(selection, selectionArgs);
        } else if(month == null){
            String selection = String.format("SELECT * FROM %s WHERE %s LIKE ?", Entry.ENTITY_NAME, Entry.DATE);
            String[] selectionArgs = {"%/" + year};
            return getEntry(selection, selectionArgs);
        } else {
            String selection = String.format("SELECT * FROM %s WHERE %s LIKE ?", Entry.ENTITY_NAME, Entry.DATE);
            String[] selectionArgs = {"%" + month + "/" + year};
            return getEntry(selection, selectionArgs);
        }
    }

    private synchronized ArrayList<Entry> getEntry(String selection, String[] selectionArgs){
        ArrayList<Entry> entries = new ArrayList<Entry>();
        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(selection, selectionArgs);

        //Cursor cursor = db.query(Entry.ENTITY_NAME, Entry.ATTRIBUTES, null, null, null, null, null);

        while(cursor.moveToNext()){
            Entry entry = new Entry();

            entry.setId(cursor.getLong(cursor.getColumnIndex(Entry.ID)));
            entry.setDescription(cursor.getString(cursor.getColumnIndex(Entry.DESCRIPTION)));
            entry.setValue(cursor.getFloat(cursor.getColumnIndex(Entry.VALUE)));
            entry.setType(cursor.getInt(cursor.getColumnIndex(Entry.TYPE)));
            entry.setCategory(cursor.getInt(cursor.getColumnIndex(Entry.CATEGORY)));
            entry.setDate(cursor.getString(cursor.getColumnIndex(Entry.DATE)));
            entry.setMonth(cursor.getInt(cursor.getColumnIndex(Entry.MONTH)));

            entries.add(entry);
        }

        cursor.close();
        db.close();

        return entries;
    }

    public long update(Entry entry) {
        ContentValues values = new ContentValues();

        values.put(Entry.DESCRIPTION, entry.getDescription());
        values.put(Entry.VALUE, entry.getValue());
        values.put(Entry.TYPE, entry.getType());
        values.put(Entry.CATEGORY, entry.getCategory());
        values.put(Entry.DATE, entry.getDate());
        values.put(Entry.MONTH, entry.getMonth());

        String whereClause = String.format("%s=?", Entry.ID);
        String[] whereArgs = { String.valueOf(entry.getId()) };

        return update(values, whereClause, whereArgs);
    }

    private synchronized long update(ContentValues values, String whereClause,
                                     String[] whereArgs) {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        long n = db.update(Entry.ENTITY_NAME, values, whereClause, whereArgs);

        db.close();

        return n;
    }

    public Float getMonthBalance(int month) {
        ArrayList<Entry> entries = getEntry(month);
        Float balance = new Float(0.0);
        for (Entry entry : entries) {
            if(entry.getType() == Entry.TYPE_GAIN){
                balance += entry.getValue();
            }else{
                balance -= entry.getValue();
            }
        }
        return balance;
    }

    public Float getGain(int month) {
        ArrayList<Entry> entries = getEntry(month);
        Float gain = new Float(0.0);
        for (Entry entry : entries) {
            if(entry.getType() == Entry.TYPE_GAIN){
                gain += entry.getValue();
            }
        }

        return gain;
    }

    public Float getExpense(int month) {
        ArrayList<Entry> entries = getEntry(month);
        Float expense = new Float(0.0);
        for (Entry entry : entries) {
            if(entry.getType() == Entry.TYPE_EXPENSE){
                expense += entry.getValue();
            }
        }

        return expense;
    }

    public ArrayList<Category> getCategories() {
        String selection = String.format("SELECT * FROM %s", Category.ENTITY_NAME);
        String[] selectionArgs = {};

        return getCategory(selection, selectionArgs);
    }

    private ArrayList<Category> getCategory(String selection, String[] selectionArgs) {
        ArrayList<Category> categories = new ArrayList<Category>();
        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(selection, selectionArgs);

        //Cursor cursor = db.query(Entry.ENTITY_NAME, Entry.ATTRIBUTES, null, null, null, null, null);

        while(cursor.moveToNext()){
            Category category = new Category();

            category.setId(cursor.getLong(cursor.getColumnIndex(Category.ID)));
            category.setName(cursor.getString(cursor.getColumnIndex(Category.NAME)));
            category.setColor(cursor.getInt(cursor.getColumnIndex(Category.COLOR)));

            categories.add(category);
        }

        cursor.close();
        db.close();

        return categories;
    }
}
