package com.example.sushant.inventoryapp.Contracts;

import android.provider.BaseColumns;

/**
 * Created by sushant on 7/11/16.
 */
public class InventoryContracts {

    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="inventorydb.db";
    public static final String TEXT_TYPE=" TEXT";
    public static final String IMAGE_TYPE=" TEXT";
    public static final String COMMA=",";
    public static final String INT_TYPE=" INTEGER";

    public static abstract class InventoryDetails implements BaseColumns{

        public static final String TABLE_NAME="Inventory";
        public static final String TABLE_ID="ID";
        public static final String COLUMN_PRODUCT_NAME="product_name";
        public static final String COLUMN_PRODUCT_PRICE="product_price";
        public static final String COLUMN_PRODUCT_QUANTITY="product_quantity";
        public static final String COLUMN_PRODUCT_IMAGE="product_image";
        public static final String COLUMN_PRODUCT_SUPPLIER="product_supplier";
        public static final String COLUMN_PRODUCT_SUPPLIER_CONTACT="supplier_contact";

        public static final String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("+
            COLUMN_PRODUCT_NAME+TEXT_TYPE+" PRIMARY KEY "+COMMA+
            COLUMN_PRODUCT_PRICE+TEXT_TYPE+COMMA+
            COLUMN_PRODUCT_QUANTITY+INT_TYPE+COMMA+
            COLUMN_PRODUCT_IMAGE+IMAGE_TYPE+COMMA+
            COLUMN_PRODUCT_SUPPLIER+TEXT_TYPE+COMMA+
            COLUMN_PRODUCT_SUPPLIER_CONTACT+TEXT_TYPE+");";

    }
}
