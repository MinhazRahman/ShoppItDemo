package com.movie.shoppitdemo.application;

import android.app.Application;

import com.movie.shoppitdemo.models.Category;
import com.movie.shoppitdemo.models.Item;
import com.movie.shoppitdemo.models.ShoppingList;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register the parse model
        ParseObject.registerSubclass(Item.class);
        ParseObject.registerSubclass(Category.class);
        ParseObject.registerSubclass(ShoppingList.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("unKKRG42AIUIc75RXWpQuLYaWW5i8GF52hO5nZMe")
                .clientKey("UznRIcEnRvmwi7CHoHTg2SoAS6SANLXvBkX5lXKi")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
