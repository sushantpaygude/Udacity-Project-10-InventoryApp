package com.example.sushant.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sushant.inventoryapp.Cards.InventoryInfo;
import com.example.sushant.inventoryapp.Cards.ProductIntroAdapter;
import com.example.sushant.inventoryapp.Contracts.InventoryContracts;
import com.example.sushant.inventoryapp.Contracts.InventoryHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    TextView textNoRecords;
    TextView textTitle;
    ProductIntroAdapter mAdapter;
    ListView productListView;
    ArrayList<InventoryInfo> mInventoryInfoList = new ArrayList<>();
    Button productAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textTitle=(TextView)findViewById(R.id.text_title);
        textNoRecords=(TextView)findViewById(R.id.text_noRecords);
        productAdd=(Button)findViewById(R.id.button_add);
        productListView=(ListView)findViewById(R.id.product_list);
        mAdapter =new ProductIntroAdapter(this,R.layout.list_item_card);
        productListView.setAdapter(mAdapter);
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
        return new CursorLoader(this,
                InventoryProvider.CONTENT_URI,
                new String[]{"product_name","product_price","product_quantity"}, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int nameColumnIndex = cursor.getColumnIndex(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_NAME);
        int quantColumnIndex = cursor.getColumnIndex(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_PRICE);

        try {
            mInventoryInfoList.clear();
            while (cursor.moveToNext()) {
                InventoryInfo inventoryInfo = new InventoryInfo();
                inventoryInfo.setProductName(cursor.getString(nameColumnIndex));
                inventoryInfo.setProductPrice(cursor.getString(priceColumnIndex));
                inventoryInfo.setProductQuantity(Integer.parseInt(cursor.getString(quantColumnIndex)));
                mInventoryInfoList.add(inventoryInfo);
            }
            if (mInventoryInfoList.size()>0){
                mAdapter.clear();
                mAdapter.addAll(mInventoryInfoList);
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


}
