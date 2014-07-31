package br.com.tolive.simplewalletpro.db;

import br.com.tolive.simplewalletpro.model.Entry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Helper extends SQLiteOpenHelper {

    private static String mDBName = "simple_wallet";
    private static int mVersion = 2;

    private static final String[] DB_CREATE_SCRIPT = { "CREATE TABLE "
            + Entry.ENTITY_NAME + " ( " + Entry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Entry.DESCRIPTION + " TEXT, " + Entry.VALUE + " FLOAT, " + Entry.TYPE + " INTEGER, "
            + Entry.CATEGORY + " TEXT, " + Entry.DATE + " TEXT, " + Entry.MONTH + " INTEGER );" };

    private static final String[] DB_DESTROY_SCRIPT = {"DROP TABLE IF EXISTS " + Entry.ENTITY_NAME + ";"};

    public Helper(Context context) {
        super(context, mDBName, null, mVersion);
        // this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String SQL : DB_CREATE_SCRIPT) {
            db.execSQL(SQL);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO save user data before destroy the table

        for (String destroyTable : DB_DESTROY_SCRIPT) {
            db.execSQL(destroyTable);
        }
        onCreate(db);
    }
}
