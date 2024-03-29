package com.movie.shoppitdemo.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.movie.shoppitdemo.R;
import com.movie.shoppitdemo.adapters.CategoryDetailsAdapter;
import com.movie.shoppitdemo.models.Category;
import com.movie.shoppitdemo.models.Item;
import com.movie.shoppitdemo.models.OnCardViewClickListener;
import com.movie.shoppitdemo.utils.SpacesItemDecoration;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryDetailsFragment extends Fragment {

    public static final String TAG = "CategoryDetailsFragment";

    RecyclerView rvItems;
    CategoryDetailsAdapter categoryDetailsAdapter;
    List<Item> allItems;
    String categoryId;
    Category category;

    public CategoryDetailsFragment() {
        // Required empty public constructor
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
        return inflater.inflate(R.layout.fragment_category_details, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup any handles to view objects here
        rvItems = view.findViewById(R.id.rvItems);

        // Initialize the list
        allItems = new ArrayList<>();
        // Initialize the adapter
        categoryDetailsAdapter = new CategoryDetailsAdapter(getContext(), allItems);
        // Initialize the layout
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        // Decorator which adds spacing around the tiles in a Grid layout RecyclerView
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);

        // Set the adapter and the linear layout manager to the RecyclerView
        rvItems.setAdapter(categoryDetailsAdapter);
        rvItems.setLayoutManager(gridLayoutManager);
        rvItems.addItemDecoration(decoration); // Not working properly

        // Receiving the data to the fragment
        if (getArguments() != null) {
            categoryId = getArguments().getString("id");
            category = getArguments().getParcelable("categoryObj");

            Log.i(TAG, "Category id: " + categoryId);
        }

        // Change the toolbar title dynamically
        Toolbar toolbar= Toolbar.class.cast(getActivity().findViewById(R.id.toolbar));
        assert toolbar != null;
        toolbar.setTitle(category.getCategoryName());

        // set toolbar navigation icon: back arrow
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getActivity().onBackPressed();
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });


        // Get all the items for a particular category
        queryItems(category);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // Hide the action_add menu item
        MenuItem itemAdd = menu.findItem(R.id.action_add);
        itemAdd.setVisible(false);

        // ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(category.getCategoryName());
    }

    private void queryItems(Category category) {
        // Define the class we would like to query
        ParseQuery<Item> itemParseQuery = ParseQuery.getQuery(Item.class);

        // Include category key
        itemParseQuery.include(Item.KEY_ITEM_CATEGORY);
        itemParseQuery.whereEqualTo(Item.KEY_ITEM_CATEGORY, category);
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

                // Add all categories to the list
                allItems.addAll(items);
                // Notify the adapter about the data change
                categoryDetailsAdapter.notifyDataSetChanged();
            }
        });
    }

}