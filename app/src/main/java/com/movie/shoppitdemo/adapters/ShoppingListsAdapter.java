package com.movie.shoppitdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.movie.shoppitdemo.R;
import com.movie.shoppitdemo.models.ShoppingList;

import java.util.List;

public class ShoppingListsAdapter extends RecyclerView.Adapter<ShoppingListsAdapter.ViewHolder>{

    Context context;
    List<ShoppingList> shoppingLists;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param shoppingLists containing the data to populate views to be used
     * by RecyclerView.
     */
    public ShoppingListsAdapter(Context context, List<ShoppingList> shoppingLists) {
        this.context = context;
        this.shoppingLists = shoppingLists;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(context).inflate(R.layout.row_shopping_list, parent, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get element from your dataset at this position
        ShoppingList shoppingList = shoppingLists.get(position);

        // Replace the contents of the view with that element
        holder.bind(shoppingList);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return shoppingLists.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        // Widgets
        TextView tvShoppingList;
        TextView tvShoppingListActions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the widgets
            tvShoppingList = itemView.findViewById(R.id.tvShoppingList);
            tvShoppingListActions = itemView.findViewById(R.id.tvShoppingListActions);
        }

        public void bind(ShoppingList shoppingList) {
            tvShoppingList.setText(shoppingList.getShoppingListName());
        }
    }

}
