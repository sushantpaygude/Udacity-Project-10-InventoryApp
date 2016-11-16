package com.example.sushant.inventoryapp.Cards;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sushant on 10/11/16.
 */
public class LoadImageTask extends AsyncTask<String,Void,Bitmap>

    {
        ImageView ProductThumbnailview;

        public LoadImageTask(ImageView ProductThumbnailview) {
            this.ProductThumbnailview = ProductThumbnailview;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String urlThumbnail=params[0];
            Bitmap ProductThumbnail=null;
            InputStream in=null;
            try {
                in = new java.net.URL(urlThumbnail).openStream();
                ProductThumbnail = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return ProductThumbnail;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ProductThumbnailview.setImageBitmap(bitmap);
        }
    }
