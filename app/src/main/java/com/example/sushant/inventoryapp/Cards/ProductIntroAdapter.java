package com.example.sushant.inventoryapp.Cards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sushant.inventoryapp.Contracts.InventoryContracts;
import com.example.sushant.inventoryapp.InventoryProvider;
import com.example.sushant.inventoryapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sushant on 10/11/16.
 */
public class ProductIntroAdapter extends ArrayAdapter<InventoryInfo> {
    private AppCompatActivity mActivity;
    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks;

    public ProductIntroAdapter(AppCompatActivity activity, int resource) {
        super(activity.getApplicationContext(), resource);
        this.mActivity = activity;
        // get LoaderCallback instance from activity
        this.mLoaderCallbacks = (LoaderManager.LoaderCallbacks<Cursor>) activity;
    }
    List<InventoryInfo> cardList = new ArrayList<InventoryInfo>();
   public static class CardViewHolder {
        ImageView ProductThumbnailView;
        TextView ProductNameView;
        TextView ProductQuantView;
        TextView ProductPriceView;
        Button ProductSaleButton;
    }

    @Override
    public void add(InventoryInfo object) {
        super.add(object);
        notifyDataSetChanged();
    }
    public void clear(){
        super.clear();
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public InventoryInfo getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row=convertView;
        final CardViewHolder cardViewHolder;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_item_card, parent, false);
            cardViewHolder = new CardViewHolder();
            cardViewHolder.ProductNameView = (TextView) row.findViewById(R.id.product_name);
            cardViewHolder.ProductThumbnailView = (ImageView) row.findViewById(R.id.product_thumbnail);
            cardViewHolder.ProductQuantView = (TextView) row.findViewById(R.id.product_quant);
            cardViewHolder.ProductPriceView = (TextView) row.findViewById(R.id.product_price);
            cardViewHolder.ProductSaleButton=(Button)row.findViewById(R.id.button_sale);
            row.setTag(cardViewHolder);
        }
        else {
            cardViewHolder = (CardViewHolder) row.getTag();
        }

        InventoryInfo inventoryInfo=getItem(position);
        cardViewHolder.ProductNameView.setText(inventoryInfo.getProductName());
        cardViewHolder.ProductQuantView.setText(String.valueOf(inventoryInfo.getProductQuantity()));
        cardViewHolder.ProductPriceView.setText(inventoryInfo.getProductPrice().toString().concat("$"));
        cardViewHolder.ProductThumbnailView.setImageBitmap(BitmapFactory.decodeFile(inventoryInfo.productImage));

        cardViewHolder.ProductSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int saleAmount = Integer.parseInt(cardViewHolder.ProductQuantView.getText().toString());
                if(saleAmount>0) {
                    saleAmount -= 1;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(InventoryContracts.InventoryDetails.COLUMN_PRODUCT_QUANTITY, saleAmount);
                    int updateQuery =getContext().getContentResolver().update(InventoryProvider.CONTENT_URI, contentValues, "product_name=?", new String[]{cardViewHolder.ProductNameView.getText().toString()});
                    if (updateQuery>0){
                        // Restart loader implemented on MainActivity
                        mActivity.getSupportLoaderManager().restartLoader(1,null, mLoaderCallbacks);
                    }
                    Toast.makeText(getContext(), "Done!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(),"Out of Stock",Toast.LENGTH_SHORT).show();
                }

            }
        });
        return row;
    }
}
