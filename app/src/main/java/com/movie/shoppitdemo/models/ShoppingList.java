package com.movie.shoppitdemo.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.parceler.Parcel;

@ParseClassName("ShoppingList")
@Parcel(analyze={ShoppingList.class})
public class ShoppingList extends ParseObject {

    public static final String KEY_SHOPPING_LIST_NAME = "shoppingListName";

    // empty constructor needed by the Parceler library
    public ShoppingList(){
    }

    public String getShoppingListName(){
        return getString(KEY_SHOPPING_LIST_NAME);
    }

    public void setShoppingListName(String shoppingListName){
        put(KEY_SHOPPING_LIST_NAME, shoppingListName);
    }

}
