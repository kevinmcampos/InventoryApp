package br.com.memorify.inventoryapp.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.memorify.inventoryapp.model.Product;

public class DatabaseManager {

    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqlite;

    private static DatabaseManager mInstance;

    public static DatabaseManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseManager(context.getApplicationContext());
        }
        return mInstance;
    }

    private DatabaseManager(Context context) {
        this.context = context;
        dbHelper = DatabaseHelper.getInstance(context);
    }

    public SQLiteDatabase getWritableDatabase() {
        if (sqlite == null || !sqlite.isOpen()) {
            sqlite = dbHelper.getWritableDatabase();
        }
        return sqlite;
    }

    public SQLiteDatabase getReadableDatabase() {
        if (sqlite == null || !sqlite.isOpen()) {
            sqlite = dbHelper.getReadableDatabase();
        }
        return sqlite;
    }

    public long insertProduct(Product product) {
        long insertId = -1;
        try {
            getWritableDatabase();
            if (sqlite != null) {
                insertId = sqlite.insert(Product.TABLE_NAME, null, product.getContentValues());
            }
        } catch (SQLException sqlerror) {
            Log.e("Insert into table error", sqlerror.getMessage());
            return insertId;
        }
        return insertId;
    }


    public int updateProduct(Product product) {
        int numberRowsAffected = 0;
        try {
            getWritableDatabase();
            ContentValues contentValues = product.getContentValues();
            if (sqlite != null) {
                String where = Product.COLUMN__ID + " = ?";
                String[] args = {Long.toString(product._id)};
                numberRowsAffected = sqlite.update(Product.TABLE_NAME, contentValues, where, args);
            }
        } catch (SQLException sqlerror) {
            Log.e("Update into table error", sqlerror.getMessage());
        }

        return numberRowsAffected;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try {
            getReadableDatabase();
            if (sqlite != null) {
                Cursor cursor = sqlite.query(Product.TABLE_NAME, null, null, null, null, null, null);
                if (cursor.getCount() > 0) {
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        Product product = Product.fromCursor(cursor);
                        products.add(product);
                    }
                }
                cursor.close();
            }
        } catch (SQLException sqlerror) {
            Log.e("Select from table error", sqlerror.getMessage());
        }
        return products;
    }

    public Product getProductById(long productId) {
        getReadableDatabase();
        Product product = null;
        if (sqlite != null) {
            String query = "SELECT * FROM " + Product.TABLE_NAME + " WHERE " + Product.COLUMN__ID + " = ?";
            Cursor cursor = sqlite.rawQuery(query, new String[]{Long.toString(productId)});
            if (cursor.moveToFirst()) {
                product = Product.fromCursor(cursor);
            }
            cursor.close();
        }
        return product;
    }

    public boolean deleteProduct(long productId) {
        try {
            getWritableDatabase();
            if (sqlite != null) {
                sqlite.delete(Product.TABLE_NAME, Product.COLUMN__ID + "=" + productId, null);
            }
        } catch (SQLException sqlerror) {
            Log.e("Delete into table error", sqlerror.getMessage());
            return false;
        }
        return true;
    }
}
