package com.example.lab3databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productDB.db";
    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PRODUCTNAME = "productName";
    private static final String COLUMN_PRICE = "productPrice";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCTNAME + " TEXT, " +
                COLUMN_PRICE + " REAL" + ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    public void addProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.getProductName());
        values.put(COLUMN_PRICE, product.getProductPrice());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    public Product findProduct(String name) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{name});
        Product product = null;
        if (cursor.moveToFirst()) {
            product = new Product(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2)
            );
        }
        cursor.close();
        db.close();
        return product;
    }

    public boolean deleteProduct(String name) {
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(TABLE_PRODUCTS, COLUMN_PRODUCTNAME + " = ?", new String[]{name});
        db.close();
        return result > 0;
    }
}