package com.example.sushant.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sushant.inventoryapp.Cards.InventoryInfo;
import com.example.sushant.inventoryapp.Cards.ProductIntroAdapter;
import com.example.sushant.inventoryapp.Contracts.InventoryContracts;
import com.example.sushant.inventoryapp.Contracts.InventoryHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    TextView textNoRecords;
    TextView textTitle;
    ProductIntroAdapter productIntroAdapter;
    ListView productListView;
    ArrayList<InventoryInfo> productArrayList;
    Button productAdd;
    ImageView productThumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textTitle=(TextView)findViewById(R.id.text_title);
        textNoRecords=(TextView)findViewById(R.id.text_noRecords);
        productAdd=(Button)findViewById(R.id.button_add);
        productThumbnail=(ImageView)findViewById(R.id.product_thumbnail);
        productListView=(ListView)findViewById(R.id.product_list);
        productIntroAdapter=new ProductIntroAdapter(this,R.layout.list_item_card);

        InventoryHelper inventoryHelper=new InventoryHelper(getApplicationContext());
        SQLiteDatabase db=inventoryHelper.getWritableDatabase();

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView productName=(TextView)view.findViewById(R.id.product_name);
                String selectedProduct=productName.getText().toString();
                startProductDetails(selectedProduct);
            }
        });

        getSupportLoaderManager().initLoader(1, null, this);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/lion_king.ttf");
        textTitle.setTypeface(typeface);

    }
    public void countRecords(ListView productListView)
    {
        if(productListView.getCount()>0){
            textNoRecords.setVisibility(View.INVISIBLE);
        }
        else {
            textNoRecords.setVisibility(View.VISIBLE);
        }
    }
    public void startAdd(View v){
        Intent intent =new Intent(this,AddProductActivity.class);
        startActivity(intent);
    }

    public void startProductDetails(String selectedProduct) {

        Intent intent=new Intent(this,ProductDetailsActivity.class);
        intent.putExtra("SelectedProduct",selectedProduct);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        productListView.setAdapter(productIntroAdapter);

        return new CursorLoader(this,
                InventoryProvider.CONTENT_URI,
                new String[]{"product_name","product_quantity","product_image"}, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        int nameColumnIndex = cursor.getColumnIndex(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_NAME);
        int quantColumnIndex = cursor.getColumnIndex(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_QUANTITY);
        int imageColumnIndex = cursor.getColumnIndex(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_IMAGE);

        try {
            while (cursor.moveToNext()) {
                InventoryInfo inventoryInfo = new InventoryInfo();
                inventoryInfo.setProductName(cursor.getString(nameColumnIndex));
                inventoryInfo.setProductQuantity(Integer.parseInt(cursor.getString(quantColumnIndex)));
                inventoryInfo.setProductImage(cursor.getString(imageColumnIndex));
                productIntroAdapter.add(inventoryInfo);
            }
        }
        finally {
            cursor.close();
        }
        countRecords(productListView);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void saleClicked(View view)
    {
        getSupportLoaderManager().restartLoader(1,null,this);
    }

//    @Override
//    public void onClick(View v) {
//        int clicked=v.getId();
//        switch (clicked) {
//            case R.id.button_sale:
//            Toast.makeText(getApplicationContext(), "SALE CLICKED", Toast.LENGTH_LONG).show();
//            TextView saleView = (TextView) findViewById(R.id.product_price);
//            TextView productName = (TextView) findViewById(R.id.product_name);
//            int saleAmount = Integer.parseInt(saleView.getText().toString());
//            saleAmount -= 1;
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_PRICE, saleAmount);
//            int updateQuery = getContentResolver().update(InventoryProvider.CONTENT_URI, contentValues, "product_name=?", new String[]{productName.getText().toString()});
//            getSupportLoaderManager().restartLoader(1, null, this);
//                break;
//        }
//    }
}
