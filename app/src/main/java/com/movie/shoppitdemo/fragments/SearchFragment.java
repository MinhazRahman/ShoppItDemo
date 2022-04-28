package com.movie.shoppitdemo.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.movie.shoppitdemo.R;
import com.movie.shoppitdemo.adapters.SearchItemsAdapter;
import com.movie.shoppitdemo.models.Item;
import com.movie.shoppitdemo.utils.SpacesItemDecoration;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    public static final String TAG = "SearchFragment";

    RecyclerView rvSearchItems;
    SearchItemsAdapter searchItemsAdapter;
    List<Item> allItems;
    MenuItem menuItemSearch;
    SearchView searchView;

    // Required empty public constructor
    public SearchFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);

        //  View view = inflater.inflate(R.layout.fragment_search, container, false);
        // setHasOptionsMenu(true);
        // return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup any handles to view objects here
        rvSearchItems = view.findViewById(R.id.rvSearchItems);

        // Initialize the list
        allItems = new ArrayList<>();
        // Initialize the adapter
        searchItemsAdapter = new SearchItemsAdapter(getContext(), allItems);
        // Initialize the layout
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        // Decorator which adds spacing around the tiles in a Grid layout RecyclerView
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);

        // Set the adapter and the linear layout manager to the RecyclerView
        rvSearchItems.setAdapter(searchItemsAdapter);
        rvSearchItems.setLayoutManager(gridLayoutManager);
        rvSearchItems.addItemDecoration(decoration); // Not working properly

        // Get all the items
        queryAllItems();

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // menu.clear(); // Remove all the old menu items
        inflater.inflate(R.menu.search_menu, menu);

        // Hide the action_add menu item
        MenuItem itemAdd = menu.findItem(R.id.action_add);
        itemAdd.setVisible(false);

        // Get the search item
        menuItemSearch = menu.findItem(R.id.action_search);
        // Get action view from the menu item and cast into SearchView
        searchView = (SearchView) menuItemSearch.getActionView();

        // Set QueryTextListener on SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, "onQueryTextSubmit: " + query);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "onQueryTextChange: " + newText);
                // searchItemsAdapter.getFilter().filter(newText);
                queryAllItems(newText);
                return false;
            }
        });
    }

    private void queryAllItems() {
        // Define the class we would like to query
        ParseQuery<Item> itemParseQuery = ParseQuery.getQuery(Item.class);

        // Include category key
        itemParseQuery.include(Item.KEY_ITEM_CATEGORY);
        itemParseQuery.orderByAscending(Item.KEY_ITEM_NAME);

        // Execute the find asynchronously
        itemParseQuery.findInBackground(new FindCallback<Item>() {
            @Override
            public void done(List<Item> items, ParseException e) {
                if (e != null){
                    Log.e(TAG, "ERROR while retrieving items", e);
                    return;
                }

                for (Item item: items){
                    Log.i(TAG, "Item: " + item.getItemName() + " Category Name: " + item.getCategory().getCategoryName());
                }

                // Add all categories to the list
                allItems.addAll(items);
                // Notify the adapter about the data change
                searchItemsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void queryAllItems(String itemName) {
        List<Item> filteredItems = new ArrayList<>();

        // Define the class we would like to query
        ParseQuery<Item> itemParseQuery = ParseQuery.getQuery(Item.class);

        // Include category key
        itemParseQuery.include(Item.KEY_ITEM_CATEGORY);
        itemParseQuery.orderByAscending(Item.KEY_ITEM_NAME);

        // Execute the find asynchronously
        itemParseQuery.findInBackground(new FindCallback<Item>() {
            @Override
            public void done(List<Item> items, ParseException e) {
                if (e != null){
                    Log.e(TAG, "ERROR while retrieving items", e);
                    return;
                }

                for (Item item: items){
                    if (item.getItemName().toLowerCase().contains(itemName.toLowerCase())){
                        filteredItems.add(item);
                        Log.i("SearchItemsAdapter", "Item name:" + item.getItemName());
                    }
                }

                allItems.clear();
                // Add all categories to the list
                allItems.addAll(filteredItems);
                // Notify the adapter about the data change
                searchItemsAdapter.notifyDataSetChanged();
            }
        });
    }
}