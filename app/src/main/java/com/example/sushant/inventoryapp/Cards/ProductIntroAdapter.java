package com.example.sushant.inventoryapp.Cards;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sushant.inventoryapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sushant on 10/11/16.
 */
public class ProductIntroAdapter extends ArrayAdapter<InventoryInfo> {
    public ProductIntroAdapter(Context context, int resource) {
        super(context, resource);
    }
    List<InventoryInfo> cardList = new ArrayList<InventoryInfo>();
    static class CardViewHolder {
        ImageView ProductThumbnailView;
        TextView ProductNameView;
        TextView ProductPriceView;
    }

    @Override
    public void add(InventoryInfo object) {
        super.add(object);
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
        CardViewHolder cardViewHolder;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_item_card, parent, false);
            cardViewHolder = new CardViewHolder();
            cardViewHolder.ProductNameView = (TextView) row.findViewById(R.id.product_name);
            cardViewHolder.ProductThumbnailView = (ImageView) row.findViewById(R.id.product_thumbnail);
            cardViewHolder.ProductPriceView = (TextView) row.findViewById(R.id.product_price);
            row.setTag(cardViewHolder);
        }
        else {
            cardViewHolder = (CardViewHolder) row.getTag();
        }

        InventoryInfo inventoryInfo=getItem(position);
        cardViewHolder.ProductNameView.setText(inventoryInfo.getProductName());
        cardViewHolder.ProductPriceView.setText(inventoryInfo.getProductPrice().concat("$"));
        cardViewHolder.ProductThumbnailView.setImageBitmap(BitmapFactory.decodeFile(inventoryInfo.productImage));
        return row;
    }
}
