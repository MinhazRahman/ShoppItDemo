package com.movie.shoppitdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.movie.shoppitdemo.R;
import com.movie.shoppitdemo.models.Category;
import com.parse.ParseFile;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder>{

    Context context;
    List<Category> categories;

    public CategoriesAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category);

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardViewCategory;
        ImageView ivCategoryImage;
        TextView tvCategoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardViewCategory = itemView.findViewById(R.id.cardViewCategory);
            ivCategoryImage = itemView.findViewById(R.id.ivCategoryImage);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }

        public void bind(Category category) {
            // Bind the Category data to the view elements
            tvCategoryName.setText(category.getCategoryName());

            // Load the Category image
            ParseFile image = category.getImage();
            if (image != null){
                Glide.with(context).load(image.getUrl()).into(ivCategoryImage);
            }

        }
    }
}
