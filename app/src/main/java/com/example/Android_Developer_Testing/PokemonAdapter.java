package com.example.Android_Developer_Testing;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Android_Developer_Testing.model.Pokemon;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.ViewHolder> {
    private List<Pokemon> pokemonList;
    private List<Pokemon> filteredPokemonList;
    private Context context;

    public PokemonAdapter(List<Pokemon> pokemonList, Context context) {
        this.context = context;
        this.pokemonList = (pokemonList != null) ? pokemonList : new ArrayList<>();
        this.filteredPokemonList = new ArrayList<>(this.pokemonList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_pokemon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (filteredPokemonList == null || filteredPokemonList.isEmpty() || position >= filteredPokemonList.size()) {
            return;
        }

        Pokemon pokemon = filteredPokemonList.get(position);
        holder.txtPokemonName.setText(pokemon.getName());

        int pokemonId = extractPokemonId(pokemon.getUrl());

        String imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + pokemonId + ".png";
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.pokemon_logo)
                .into(holder.imgPokemon, new Callback() {
                    @Override
                    public void onSuccess() {}

                    @Override
                    public void onError(Exception e) {
                        Log.e("PICASSO", "Gagal memuat gambar untuk " + pokemon.getName(), e);
                    }
                });

        int colorRes;
        if (position % 2 == 0) {
            colorRes = R.color.light_pink;
        } else {
            colorRes = R.color.light_purple;
        }

        holder.itemView.setBackgroundResource(colorRes);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("pokemon_name", pokemon.getName());
            intent.putExtra("pokemon_id", pokemonId);
            context.startActivity(intent);
        });

        Log.e("ADAPTER", "Menampilkan: " + pokemon.getName());
    }

    @Override
    public int getItemCount() {
        return (filteredPokemonList != null) ? filteredPokemonList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPokemon;
        TextView txtPokemonName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPokemon = itemView.findViewById(R.id.imgPokemon);
            txtPokemonName = itemView.findViewById(R.id.txtPokemonName);
        }
    }

    private int extractPokemonId(String url) {
        try {
            String[] parts = url.split("/");
            return Integer.parseInt(parts[parts.length - 1]);
        } catch (Exception e) {
            Log.e("EXTRACT_ID", "Gagal mengekstrak ID dari URL: " + url, e);
            return 0;
        }
    }

    public void updateData(List<Pokemon> newPokemonList) {
        this.pokemonList.clear();
        this.pokemonList.addAll(newPokemonList);
        this.filteredPokemonList = new ArrayList<>(pokemonList);
        notifyDataSetChanged();
        Log.e("ADAPTER", "Data diperbarui: " + pokemonList.size() + " item");
    }

    public void filter(String query) {
        filteredPokemonList.clear();
        if (query.isEmpty()) {
            filteredPokemonList.addAll(pokemonList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Pokemon pokemon : pokemonList) {
                if (pokemon.getName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredPokemonList.add(pokemon);
                }
            }
        }
        notifyDataSetChanged();
    }
}
