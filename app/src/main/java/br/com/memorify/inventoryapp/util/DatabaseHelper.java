package br.com.memorify.inventoryapp.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import br.com.memorify.inventoryapp.model.Product;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TEMPLATE_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS %1s (%2s);";
    private static final String DATABASE_NAME = "habits.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLES[] = {
            Product.Meta.CREATE_TABLE(),
    };

    private static DatabaseHelper mInstance;

    public static DatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String CREATE_TABLE : CREATE_TABLES) {
            db.execSQL(CREATE_TABLE);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(), "Should upgrade database from version " + oldVersion + " to " + newVersion);
    }
}
