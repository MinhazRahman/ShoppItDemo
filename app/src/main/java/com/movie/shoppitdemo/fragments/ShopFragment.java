package com.movie.shoppitdemo.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.movie.shoppitdemo.R;
import com.movie.shoppitdemo.adapters.ParentRecyclerViewCategoriesAdapter;
import com.movie.shoppitdemo.models.Category;
import com.movie.shoppitdemo.models.Item;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ShopFragment extends Fragment {
    public static final String TAG = "ShopFragment";

    RecyclerView rvParentCategoryAndItems;
    ParentRecyclerViewCategoriesAdapter parentRecyclerViewCategoriesAdapter;
    List<Item> allItems;
    List<Category> allCategories;
    MenuItem menuItemSearch;

    // Required empty public constructor
    public ShopFragment() {

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
        return inflater.inflate(R.layout.fragment_shop, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar= Toolbar.class.cast(getActivity().findViewById(R.id.toolbar));
        toolbar.setTitle("ShoppItDemo");

        // Setup any handles to view objects here
        rvParentCategoryAndItems = view.findViewById(R.id.rvParentCategoryAndItems);
        rvParentCategoryAndItems.setHasFixedSize(true);

        // Initialize the lists
        allCategories = new ArrayList<>();
        allItems = new ArrayList<>();

        // Initialize the adapter
        parentRecyclerViewCategoriesAdapter = new ParentRecyclerViewCategoriesAdapter(getContext(), allCategories, allItems);

        // Initialize the layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        // Set the adapter and the linear layout manager to the RecyclerView
        rvParentCategoryAndItems.setAdapter(parentRecyclerViewCategoriesAdapter);
        rvParentCategoryAndItems.setLayoutManager(linearLayoutManager);

        // Retrieve all the categories and all the items from the Parse backend
        getAllCategories();
        getAllItems();

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // Hide the action_add menu item
        MenuItem itemAdd = menu.findItem(R.id.action_add);
        itemAdd.setVisible(false);
    }

    // Retrieve all the categories from the Parse backend
    private void getAllCategories() {
        // Define the class we would like to query
        ParseQuery<Category> categoryParseQuery = ParseQuery.getQuery(Category.class);

        // Include user key
        categoryParseQuery.include(Category.KEY_CATEGORY_NAME);
        // Execute the find asynchronously
        categoryParseQuery.findInBackground(new FindCallback<Category>() {
            @Override
            public void done(List<Category> categories, ParseException e) {
                if (e != null){
                    Log.e(TAG, "ERROR while retrieving categories", e);
                    return;
                }

                for (Category category: categories){
                    Log.i(TAG,  " Category Name: " + category.getCategoryName() + " Category Id: " + category.getObjectId());
                }

                // Add all categories to the list
                allCategories.addAll(categories);
                // Notify the adapter about the data change
                // parentRecyclerViewCategoriesItemsAdapter.notifyDataSetChanged();
            }
        });
    }

    // Retrieve all the items from the Parse backend
    private void getAllItems() {
        // Define the class we would like to query
        ParseQuery<Item> itemParseQuery = ParseQuery.getQuery(Item.class);

        // Include category key
        itemParseQuery.include(Item.KEY_ITEM_CATEGORY);

        // Execute the find asynchronously
        itemParseQuery.findInBackground(new FindCallback<Item>() {
            @Override
            public void done(List<Item> items, ParseException e) {
                if (e != null){
                    Log.e(TAG, "ERROR while retrieving posts", e);
                    return;
                }

                for (Item item: items){
                    Log.i(TAG, "Item: " + item.getItemName() + " Category Name: " + item.getCategory().getCategoryName());
                }

                // Add all categories to the list
                allItems.addAll(items);
                // Notify the adapter about the data change
                parentRecyclerViewCategoriesAdapter.notifyDataSetChanged();
            }
        });
    }
}