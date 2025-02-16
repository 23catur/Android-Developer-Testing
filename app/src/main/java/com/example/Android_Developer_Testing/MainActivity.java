package com.example.Android_Developer_Testing;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Android_Developer_Testing.api.ApiService;
import com.example.Android_Developer_Testing.model.Pokemon;
import com.example.Android_Developer_Testing.model.PokemonResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PokemonAdapter adapter;
    private SearchView searchView;
    private Spinner spinnerSort;
    private List<Pokemon> pokemonList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        spinnerSort = findViewById(R.id.spinnerSort);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PokemonAdapter(pokemonList, this);
        recyclerView.setAdapter(adapter);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.sort_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(spinnerAdapter);

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortPokemonList(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        fetchPokemonList();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
    }

    private void fetchPokemonList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        apiService.getPokemonList().enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Pokemon> fetchedPokemon = response.body().getResults();

                    if (fetchedPokemon != null && !fetchedPokemon.isEmpty()) {
                        pokemonList.clear();
                        pokemonList.addAll(fetchedPokemon);

                        adapter = new PokemonAdapter(pokemonList, MainActivity.this);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        Log.e("API SUCCESS", "Data berhasil dimuat: " + pokemonList.size() + " item");
                    } else {
                        Log.e("API ERROR", "Response sukses tetapi data kosong");
                    }
                } else {
                    Log.e("API ERROR", "Response gagal: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                Log.e("API FAILURE", "Gagal mengambil data: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sortPokemonList(int position) {
        if (pokemonList == null || pokemonList.isEmpty()) return;

        switch (position) {
            case 1:
                Collections.sort(pokemonList, (p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
                break;
            case 2:
                Collections.sort(pokemonList, (p1, p2) -> p2.getName().compareToIgnoreCase(p1.getName()));
                break;
            default:
                fetchPokemonList();
                return;
        }

        adapter = new PokemonAdapter(pokemonList, MainActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
