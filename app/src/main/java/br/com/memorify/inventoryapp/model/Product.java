package br.com.memorify.inventoryapp.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;

import java.util.Date;
import java.util.Random;

import br.com.memorify.inventoryapp.util.DatabaseHelper;

public class Product {

    public static final String TABLE_NAME = "Product";

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_STOCK, stock);
        values.put(COLUMN_PRICE, price);
//            values.put(COLUMN_PICTURE, nextReminder.getTime());
        values.put(COLUMN_SUPPLIER_CONTACT, supplierContact);
        return values;
    }

    public static Product fromCursor(Cursor cursor) {
        Product product = new Product();
        int columnIndex = cursor.getColumnIndex(COLUMN__ID);
        if (columnIndex != -1) {
            product._id = cursor.getLong(columnIndex);
        }

        columnIndex = cursor.getColumnIndex(COLUMN_NAME);
        if (columnIndex != -1) {
            product.name = cursor.getString(columnIndex);
        }

        columnIndex = cursor.getColumnIndex(COLUMN_STOCK);
        if (columnIndex != -1) {
            product.stock = cursor.getLong(columnIndex);
        }

        columnIndex = cursor.getColumnIndex(COLUMN_PRICE);
        if (columnIndex != -1) {
            product.price = cursor.getDouble(columnIndex);
        }

//        columnIndex = cursor.getColumnIndex(COLUMN_PICTURE);
//        if (columnIndex != -1 && !cursor.isNull(columnIndex)) {
//            product.nextReminder = new Date(cursor.getLong(columnIndex));
//        }

        columnIndex = cursor.getColumnIndex(COLUMN_SUPPLIER_CONTACT);
        if (columnIndex != -1) {
            product.supplierContact = cursor.getString(columnIndex);
        }
        return product;
    }

    /**
     * At this point, the app use a complex math to know what supplier you was thinking without no
     * need to type it on an EditText. Or maybe the dialog was too small to put another field.
     */
    public static String getRandomSupplier() {
        String[] suppliers = new String[]{"supplies@walmart.com", "games@steam.com", "answers@stackoverflow.com"};
        int complexIndex = new Random().nextInt(suppliers.length - 1);
        return suppliers[complexIndex];
    }

    public static class Meta {
        public static String CREATE_TABLE() {
            return CREATE_TABLE(TABLE_NAME);
        }
        public static String CREATE_TABLE(String tableName) {
            return String.format(DatabaseHelper.TEMPLATE_CREATE_TABLE, tableName,
                    COLUMN__ID + " integer primary key, "
                            + COLUMN_NAME + " text, "
                            + COLUMN_STOCK + " long, "
                            + COLUMN_PRICE + " double, "
                            + COLUMN_PICTURE + " blob, "
                            + COLUMN_SUPPLIER_CONTACT + " text ");
        }
    }

    public static final String COLUMN__ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_STOCK = "stock";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_PICTURE = "picture";
    public static final String COLUMN_SUPPLIER_CONTACT = "supplier";

    public long _id;
    public String name;
    public long stock;
    public double price;
    public Bitmap picture;
    public String supplierContact;

}
