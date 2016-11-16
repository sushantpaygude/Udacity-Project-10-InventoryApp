package com.example.sushant.inventoryapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.sushant.inventoryapp.Contracts.InventoryContracts;
import com.example.sushant.inventoryapp.Contracts.InventoryHelper;

import java.util.HashMap;

/**
 * Created by sushant on 8/11/16.
 */
public class InventoryProvider extends ContentProvider {
    static final String PROVIDER_NAME="com.example.sushant.inventoryapp.InventoryProvider";
    static final String URL="content://"+PROVIDER_NAME+"/Inventory";
    static final Uri CONTENT_URI=Uri.parse(URL);
    private SQLiteDatabase db;
    static UriMatcher uriMatcher = null;
    static final int URI_CODE=1;
    private static HashMap<String, String> values;

    static {
    uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    uriMatcher.addURI(PROVIDER_NAME,"Inventory",URI_CODE);
    uriMatcher.addURI(PROVIDER_NAME,"Inventory/*",URI_CODE);
    }

    @Override
    public boolean onCreate() {
        InventoryHelper inventoryHelper=new InventoryHelper(getContext());
        db=inventoryHelper.getWritableDatabase();

        if(db!=null) {
            return true;
        }
        else {
            return false;
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqLiteQueryBuilder=new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(InventoryContracts.InventoryDetails.TABLE_NAME);

        switch (uriMatcher.match(uri))
        {
            case URI_CODE:
                sqLiteQueryBuilder.setProjectionMap(values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder == "") {
            sortOrder = InventoryContracts.InventoryDetails.COLUMN_PRODUCT_NAME;
        }
        Cursor cursor=sqLiteQueryBuilder.query(db,projection,selection,selectionArgs,null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_CODE:
                return "vnd.android.cursor.dir/Inventory";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID=db.insert(InventoryContracts.InventoryDetails.TABLE_NAME,"",values);
        if(rowID>0)
        {
            Uri _uri= ContentUris.withAppendedId(CONTENT_URI,rowID);
            getContext().getContentResolver().notifyChange(_uri,null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count=0;
        switch (uriMatcher.match(uri))
        {
            case URI_CODE:
                count=db.delete(InventoryContracts.InventoryDetails.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count=0;
        switch (uriMatcher.match(uri))
        {
            case URI_CODE:
               count=db.update(InventoryContracts.InventoryDetails.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);

        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }
}
