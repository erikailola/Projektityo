package com.example.kuntakatselu2.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuntakatselu2.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Favourite list part doesn't work in the app --> We couldn't figure out why as time run out but left this here

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private List<String> items;
    private Set<String> favorites;
    private OnFavoriteClickListener listener;
    private SharedPreferences sharedPreferences;

    public FavoriteAdapter(Context context, OnFavoriteClickListener listener) {
        this.items = new ArrayList<>();
        this.listener = listener;
        this.sharedPreferences = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        this.favorites = new HashSet<>(sharedPreferences.getStringSet("favorites", new HashSet<>()));
        loadRecentSearches();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        String item = items.get(position);
        holder.favoriteItemText.setText(item);
        holder.favoriteIcon.setImageResource(R.drawable.ic_favorite);

        if (favorites.contains(item)) {
            holder.favoriteIcon.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.accentColor));
        } else {
            holder.favoriteIcon.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.textSecondary));
        }

        holder.itemView.setOnClickListener(v -> listener.onFavoriteClick(item));
        holder.favoriteIcon.setOnClickListener(v -> toggleFavorite(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addSearch(String search) {
        items.remove(search);
        items.add(0, search);
        if (items.size() > 3) {
            items.remove(3);
        }
        saveRecentSearches();
        notifyDataSetChanged();
    }

    public boolean toggleFavorite(String item) {
        boolean isFavorite = favorites.contains(item);
        if (isFavorite) {
            favorites.remove(item);
        } else {
            favorites.add(item);
        }
        sharedPreferences.edit().putStringSet("favorites", favorites).apply();
        updateItems();
        return !isFavorite;
    }

    private void updateItems() {
        items.clear();
        items.addAll(favorites);
        loadRecentSearches();
        notifyDataSetChanged();
    }

    private void loadRecentSearches() {
        Set<String> recentSearches = sharedPreferences.getStringSet("recentSearches", new HashSet<>());
        for (String search : recentSearches) {
            if (!items.contains(search) && items.size() < 3) {
                items.add(search);
            }
        }
    }

    private void saveRecentSearches() {
        Set<String> recentSearches = new HashSet<>(items.subList(0, Math.min(items.size(), 3)));
        sharedPreferences.edit().putStringSet("recentSearches", recentSearches).apply();
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView favoriteItemText;
        ImageView favoriteIcon;

        FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteItemText = itemView.findViewById(R.id.favoriteItemText);
            favoriteIcon = itemView.findViewById(R.id.favoriteIcon);
        }
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(String favorite);
    }
}