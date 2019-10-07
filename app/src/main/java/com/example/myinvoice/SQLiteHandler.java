package com.example.myinvoice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "invoices_db";

    // Logs table name
    private static final String TABLE_INVOICES = "invoices";



    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {


        String Create_Logs_table="CREATE TABLE invoices (invoiceID INTEGER PRIMARY KEY,title TEXT,invoice_Date TEXT,invoice_type TEXT, shopname TEXT,location TEXT,comment TEXT,image BLOB);";
        db.execSQL(Create_Logs_table);


        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICES);

        // Create tables again
        onCreate(db);
    }
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
    public void addInvoice(Invoice invoice){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",invoice.getTitle());
        values.put("invoice_date",invoice.getDate());
        values.put("invoice_type",invoice.getInvoiceType());
        values.put("shopname",invoice.getShopName());
        values.put("location",invoice.getLocation());
        values.put("comment",invoice.getComment());
        values.put("image",getBitmapAsByteArray(invoice.getImage()));
        long id=db.insert(TABLE_INVOICES,null,values);

    }
    public void updateInvoice(Invoice invoice){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",invoice.getTitle());
        values.put("invoice_date",invoice.getDate());
        values.put("invoice_type",invoice.getInvoiceType());
        values.put("shopname",invoice.getShopName());
        values.put("comment",invoice.getComment());
        values.put("image",getBitmapAsByteArray(invoice.getImage()));
        long id=db.update(TABLE_INVOICES,values,"invoiceID="+invoice.getInvoiceID(),null);

    }
    public boolean deleleInvoice(String invoiceID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_INVOICES, "invoiceID=" + invoiceID, null) > 0;
    }
    public ArrayList<Invoice> getRecords(){
        ArrayList<Invoice> data=new ArrayList<>();
        String query="SELECT * FROM invoices";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // Move to first row

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while(cursor.moveToNext()) {
                String invoiceID = cursor.getString(0);
                String title = cursor.getString(1);
                String date = cursor.getString(2);
                String invoiceType = cursor.getString(3);
                String shopName = cursor.getString(4);
                String location = cursor.getString(5);
                String comment = cursor.getString(6);
                byte[] image = cursor.getBlob(7);
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                Invoice invoice=new Invoice(title,date,invoiceType,shopName,location,comment,bitmap);
                invoice.setInvoiceID(invoiceID);
                data.add(invoice);
            }
        }
        return data;
    }


    /**
     * Re crate database Delete table logs and create them again
     * */
    public void deleteLogs() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_INVOICES, null, null);
        db.close();

        Log.d(TAG, "Deleted all info from logs table");
    }
}
