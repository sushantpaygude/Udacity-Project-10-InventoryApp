package com.example.sushant.inventoryapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sushant.inventoryapp.Cards.InventoryInfo;
import com.example.sushant.inventoryapp.Contracts.InventoryContracts;

/**
 * Created by sushant on 12/11/16.
 */
public class ProductDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,View.OnClickListener {
    TextView selectedNameView;
    TextView selectedPriceView;
    TextView selectedQuantityView;
    TextView selectedSupplierNameView;
    TextView selectedSupplierContactView;
    ImageView selectedImageView;
    Button increaseQuantity;
    Button decreaseQuantity;
    Button deleteProduct;
    Button contactDealer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetails);
        selectedNameView=(TextView)findViewById(R.id.selected_productName);
        selectedPriceView=(TextView)findViewById(R.id.selected_productPrice);
        selectedQuantityView=(TextView)findViewById(R.id.selected_productQuantity);
        selectedSupplierNameView=(TextView)findViewById(R.id.selected_productSupplier);
        selectedSupplierContactView=(TextView)findViewById(R.id.selected_productContact);
        selectedImageView=(ImageView)findViewById(R.id.selected_productImage);
        increaseQuantity=(Button)findViewById(R.id.button_increaseQuant);
        decreaseQuantity=(Button)findViewById(R.id.button_decreaseQuant);
        deleteProduct=(Button)findViewById(R.id.button_delete);
        contactDealer=(Button)findViewById(R.id.button_contact);
        increaseQuantity.setOnClickListener(this);
        decreaseQuantity.setOnClickListener(this);
        deleteProduct.setOnClickListener(this);
        contactDealer.setOnClickListener(this);
        getSupportLoaderManager().initLoader(2,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String selectedProduct=null;
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null) {
            selectedProduct=bundle.getString("SelectedProduct");
        }

        return new CursorLoader(this,
                InventoryProvider.CONTENT_URI
                , new String[]{InventoryContracts.InventoryDetails.COLUMN_PRODUCT_NAME, InventoryContracts.InventoryDetails.COLUMN_PRODUCT_PRICE,InventoryContracts.InventoryDetails.COLUMN_PRODUCT_QUANTITY,
                InventoryContracts.InventoryDetails.COLUMN_PRODUCT_IMAGE, InventoryContracts.InventoryDetails.COLUMN_PRODUCT_SUPPLIER, InventoryContracts.InventoryDetails.COLUMN_PRODUCT_SUPPLIER_CONTACT},"product_name=?",  new String[]{selectedProduct}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        cursor.moveToFirst();
        int nameColumnIndex = cursor.getColumnIndex(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_QUANTITY);
        int imageColumnIndex = cursor.getColumnIndex(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_IMAGE);
        int supplierColumnIndex = cursor.getColumnIndex(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_SUPPLIER);
        int contactColumnIndex = cursor.getColumnIndex(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_SUPPLIER_CONTACT);
        try {
            selectedNameView.setText(cursor.getString(nameColumnIndex));
            selectedPriceView.setText(cursor.getString(priceColumnIndex).concat("$"));
            selectedQuantityView.setText(cursor.getString(quantityColumnIndex));
            selectedSupplierNameView.setText(cursor.getString(supplierColumnIndex));
            selectedSupplierContactView.setText(cursor.getString(contactColumnIndex));
            selectedImageView.setImageBitmap(BitmapFactory.decodeFile(cursor.getString(imageColumnIndex)));
        }
        finally {
            cursor.close();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        int currentQuantity;
        ContentValues contentValues=new ContentValues();
        switch (v.getId())
        {
            case R.id.button_increaseQuant:
                currentQuantity=Integer.parseInt(selectedQuantityView.getText().toString());
                currentQuantity+=1;
                contentValues.put(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_QUANTITY,currentQuantity);
                int updateQuery=getContentResolver().update(InventoryProvider.CONTENT_URI,contentValues,"product_name=?",new String[]{selectedNameView.getText().toString()});
                getSupportLoaderManager().restartLoader(2,null,this);
                break;

            case R.id.button_decreaseQuant:
                currentQuantity=Integer.parseInt(selectedQuantityView.getText().toString());
                if(currentQuantity>0) {
                    currentQuantity -= 1;
                    contentValues.put(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_QUANTITY, currentQuantity);
                    updateQuery = getContentResolver().update(InventoryProvider.CONTENT_URI, contentValues, "product_name=?", new String[]{selectedNameView.getText().toString()});
                    getSupportLoaderManager().restartLoader(2, null, this);
                }
                    break;

            case R.id.button_delete:
                promptDialog(this);
                break;

            case R.id.button_contact:
                Intent intentContact = new Intent(Intent.ACTION_DIAL);
                intentContact.setData(Uri.parse("tel:"+selectedSupplierContactView.getText().toString()));
                startActivity(intentContact);
                break;
        }
    }

    public void promptDialog(final Context context)
    {
        new AlertDialog.Builder(context)
                .setTitle("Delete Product")
                .setMessage("Are you sure?")
                .setIcon(R.drawable.close_blue)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int deleteQuery=getContentResolver().delete(InventoryProvider.CONTENT_URI,"product_name=?",new String[]{selectedNameView.getText().toString()});
                        Toast.makeText(context,"Product Deleted",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(context,MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}
