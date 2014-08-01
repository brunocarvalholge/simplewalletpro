package br.com.tolive.simplewalletpro.db;

import br.com.tolive.simplewalletpro.model.Category;
import br.com.tolive.simplewalletpro.model.Entry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Helper extends SQLiteOpenHelper {

    private static String mDBName = "simple_wallet";
    private static int mVersion = 1;

    private static final String[] DB_CREATE_SCRIPT = {
            "CREATE TABLE "
            + Entry.ENTITY_NAME + " ( " + Entry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Entry.DESCRIPTION + " TEXT, " + Entry.VALUE + " FLOAT, " + Entry.TYPE + " INTEGER, "
            + Entry.CATEGORY + " INTEGER, " + Entry.DATE + " TEXT, " + Entry.MONTH + " INTEGER, FOREIGN KEY ("
            + Entry.CATEGORY + ") REFERENCES " + Category.ENTITY_NAME + "(" + Category.ID + "));",
            "CREATE TABLE "
            + Category.ENTITY_NAME + " ( " + Category.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Category.NAME + " TEXT, " + Category.COLOR + " INTEGER );"};

    private static final String[] DB_INSERT_SCRIPT = {
            "INSERT INTO " + Category.ENTITY_NAME + " VALUES ( 0, 'Vermelho', 0 );",
            "INSERT INTO " + Category.ENTITY_NAME + " VALUES ( 1, 'Rosa', 1 );",
            "INSERT INTO " + Category.ENTITY_NAME + " VALUES ( 2, 'Roxo', 2 );",
            "INSERT INTO " + Category.ENTITY_NAME + " VALUES ( 3, 'Indigo', 3 );",
            "INSERT INTO " + Category.ENTITY_NAME + " VALUES ( 4, 'Cyan', 4 );",
    };


    private static final String[] DB_DESTROY_SCRIPT = {"DROP TABLE IF EXISTS " + Entry.ENTITY_NAME + ";", "DROP TABLE IF EXISTS " + Category.ENTITY_NAME + ";"};

    public Helper(Context context) {
        super(context, mDBName, null, mVersion);
        // this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String SQL : DB_CREATE_SCRIPT) {
            db.execSQL(SQL);
        }
        for (String SQL : DB_INSERT_SCRIPT) {
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
