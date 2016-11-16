package com.example.sushant.inventoryapp.Contracts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sushant on 7/11/16.
 */
public class InventoryHelper extends SQLiteOpenHelper {


    public InventoryHelper(Context context) {
        super(context, InventoryContracts.DATABASE_NAME, null, InventoryContracts.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(InventoryContracts.InventoryDetails.CREATE_TABLE);
 }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
