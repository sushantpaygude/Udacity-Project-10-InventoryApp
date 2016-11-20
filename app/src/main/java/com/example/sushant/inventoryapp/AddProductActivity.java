package com.example.sushant.inventoryapp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sushant.inventoryapp.Contracts.InventoryContracts;

/**
 * Created by sushant on 11/11/16.
 */
public class AddProductActivity extends AppCompatActivity implements View.OnClickListener {

    TextView productNameView;
    TextView productPriceView;
    TextView productQuantityView;
    TextView productSupplierNameView;
    TextView productSupplierContactView;
    ImageView productImageView;
    Button productImageSelect;
    Button productAdd;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString=null;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);

        productNameView=(TextView)findViewById(R.id.newproduct_name);
        productQuantityView=(TextView)findViewById(R.id.newproduct_quantity);
        productPriceView=(TextView)findViewById(R.id.newproduct_price);
        productSupplierNameView=(TextView)findViewById(R.id.newproduct_supplier_name);
        productSupplierContactView=(TextView)findViewById(R.id.newproduct_supplier_contact);
        productImageView=(ImageView)findViewById(R.id.newproduct_image);
        productImageSelect=(Button)findViewById(R.id.button_image);
        productAdd=(Button)findViewById(R.id.button_add);
        productImageSelect.setOnClickListener(this);
        productAdd.setOnClickListener(this);
        verifyStoragePermissions(this);
    }

    @Override
    public void onClick(View v) {
        int select=v.getId();
        switch (select)
        {
            case R.id.button_image:
                loadImagefromGallery();
                break;

            case R.id.button_add:
                if(inputValidator()) {
                    addNewProduct();
                }
                else {
                    new AlertDialog.Builder(this)
                            .setTitle("Invalid Input")
                            .setMessage("Please enter correct info.")
                            .setIcon(R.drawable.close_blue)
                            .setNeutralButton("Sure", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
                break;
        }
    }

    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    public void addNewProduct()
    {
        try {
            ContentValues newProductValues = new ContentValues();
            newProductValues.put(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_NAME, productNameView.getText().toString());
            newProductValues.put(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_PRICE, productPriceView.getText().toString());
            newProductValues.put(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_QUANTITY, Integer.parseInt(productQuantityView.getText().toString()));
            newProductValues.put(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_SUPPLIER, productSupplierNameView.getText().toString());
            newProductValues.put(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_SUPPLIER_CONTACT, productSupplierContactView.getText().toString());
              if (imgDecodableString != null) {
                newProductValues.put(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_IMAGE, imgDecodableString);
            }
            Uri uri = getContentResolver().insert(InventoryProvider.CONTENT_URI, newProductValues);
            Toast.makeText(getBaseContext(), "New record inserted", Toast.LENGTH_SHORT)
                    .show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean inputValidator()
    {
        boolean validator=false;
        try {
            if (productNameView.getText().toString().isEmpty() || productPriceView.getText().toString().isEmpty() || productQuantityView.getText().toString().isEmpty() ||
                    productSupplierNameView.getText().toString().isEmpty() || productSupplierContactView.getText().toString().isEmpty()) {
                validator = false;
            } else {
                validator = true;
            }

            if (productPriceView.getText().toString().matches(".*[a-zA-Z]+.*") || productQuantityView.getText().toString().matches(".*[a-zA-Z]+.*") || productSupplierContactView.getText().toString().matches(".*[a-zA-Z]+.*")
                    || productSupplierContactView.getText().toString().length()!=10 || productImageView.getDrawable()==null) {
                validator = false;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return validator;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.newproduct_image);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
