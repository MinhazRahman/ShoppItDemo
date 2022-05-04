package com.movie.shoppitdemo.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.movie.shoppitdemo.R;
import com.movie.shoppitdemo.activities.MainActivity;
import com.movie.shoppitdemo.adapters.ShoppingListsAdapter;
import com.movie.shoppitdemo.models.Category;
import com.movie.shoppitdemo.models.Item;
import com.movie.shoppitdemo.models.ShoppingList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    // variables
    TextView tvText;
    ImageView ivListIcon;
    RecyclerView rvShoppingLists;
    List<ShoppingList> allShoppingLists;
    String shoppingListName;
    ShoppingListsAdapter shoppingListsAdapter;

    // Required empty public constructor
    public HomeFragment() {

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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar= Toolbar.class.cast(getActivity().findViewById(R.id.toolbar));
        toolbar.setTitle("ShoppItDemo");

        // Initialize the widgets
        tvText = view.findViewById(R.id.tvText);
        ivListIcon = view.findViewById(R.id.ivListIcon);
        rvShoppingLists = view.findViewById(R.id.rvShoppingLists);

        // Initialize list
        allShoppingLists = new ArrayList<>();

        // Initialize the adapter
        shoppingListsAdapter = new ShoppingListsAdapter(getContext(), allShoppingLists);

        // Initialize the layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        // Decorate the RecyclerView
        // This decorator displays dividers between each item within the list
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);

        // Set the Adapter for the RecyclerView
        rvShoppingLists.setAdapter(shoppingListsAdapter);

        // Set the LayoutManager for the RecyclerView
        rvShoppingLists.setLayoutManager(linearLayoutManager);
        rvShoppingLists.addItemDecoration(itemDecoration);

        // Receiving the data to the fragment
        if (getArguments() != null) {
            shoppingListName = getArguments().getString("shoppingListName");

            Log.i(TAG, "Shopping List Name: " + shoppingListName);
        }

        // Populate shopping lists
        getAllShoppingLists();

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    // Show a message to the user if the shopping list is empty
    private void showMessageOnEmptyShoppingList(){
        rvShoppingLists.setVisibility(View.GONE);
        ivListIcon.setVisibility(View.VISIBLE);
        tvText.setVisibility(View.VISIBLE);
    }

    // Retrieve all the categories from the Parse backend
    private void getAllShoppingLists() {
        // Define the class we would like to query
        ParseQuery<ShoppingList> shoppingListParseQuery = ParseQuery.getQuery(ShoppingList.class);

        // Include user key
        shoppingListParseQuery.include(ShoppingList.KEY_SHOPPING_LIST_NAME);
        // Execute the find asynchronously
        shoppingListParseQuery.findInBackground(new FindCallback<ShoppingList>() {
            @Override
            public void done(List<ShoppingList> shoppingLists, ParseException e) {
                if (e != null){
                    Log.e(TAG, "ERROR while retrieving shopping lists", e);
                    return;
                }

                for (ShoppingList shoppingList: shoppingLists){
                    Log.i(TAG,  " Shopping List Name: " + shoppingList.getShoppingListName() + " Shopping List Id: " + shoppingList.getObjectId());
                }

                // Add all categories to the list
                allShoppingLists.addAll(shoppingLists);
                // Notify the adapter about the data change
                shoppingListsAdapter.notifyDataSetChanged();

                // Show message on empty shopping list
                if (allShoppingLists.isEmpty()){
                    showMessageOnEmptyShoppingList();
                }
            }
        });
    }

}