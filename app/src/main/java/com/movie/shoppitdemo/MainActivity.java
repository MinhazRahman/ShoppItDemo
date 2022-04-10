package com.movie.shoppitdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.movie.shoppitdemo.models.Item;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get items
        queryItem();
    }

    private void queryItem() {
        // Define the class we would like to query
        ParseQuery<Item> itemParseQuery = ParseQuery.getQuery(Item.class);

        // Include user key
        itemParseQuery.include(Item.KEY_ITEM_CATEGORY);
        // Execute the find asynchronously
        itemParseQuery.findInBackground(new FindCallback<Item>() {
            @Override
            public void done(List<Item> items, ParseException e) {
                if (e != null){
                    Log.e("HomeActivity", "ERROR while retrieving posts", e);
                    return;
                }

                for (Item item: items){
                    Log.i("HomeActivity", "Item: " + item.getItemName() + " Category Name: " + item.getCategory().getCategoryName());
                }
            }
        });
    }
}