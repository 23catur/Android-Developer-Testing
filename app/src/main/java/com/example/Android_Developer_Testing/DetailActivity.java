package com.example.Android_Developer_Testing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.Android_Developer_Testing.api.ApiService;
import com.example.Android_Developer_Testing.model.PokemonDetail;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {
    private ImageView imgPokemonDetail;
    private TextView txtPokemonNameDetail;
    private LinearLayout abilitiesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imgPokemonDetail = findViewById(R.id.imgPokemonDetail);
        txtPokemonNameDetail = findViewById(R.id.txtPokemonNameDetail);
        abilitiesContainer = findViewById(R.id.abilitiesContainer);

        int pokemonId = getIntent().getIntExtra("pokemon_id", 0);

        fetchPokemonDetail(pokemonId);
    }

    private void fetchPokemonDetail(int pokemonId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        apiService.getPokemonDetail(pokemonId).enqueue(new Callback<PokemonDetail>() {
            @Override
            public void onResponse(Call<PokemonDetail> call, Response<PokemonDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PokemonDetail pokemon = response.body();

                    txtPokemonNameDetail.setText(pokemon.getName());

                    Picasso.get()
                            .load(pokemon.getSprites().getFrontDefault())
                            .into(imgPokemonDetail);

                    abilitiesContainer.removeAllViews();
                    for (PokemonDetail.AbilityWrapper abilityWrapper : pokemon.getAbilities()) {
                        addAbilityCard(abilityWrapper.getAbility().getName());
                    }
                }
            }

            @Override
            public void onFailure(Call<PokemonDetail> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Gagal memuat data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addAbilityCard(String abilityName) {
        View view = LayoutInflater.from(this).inflate(R.layout.abilities_item, abilitiesContainer, false);
        TextView txtAbility = view.findViewById(R.id.txtAbility);
        txtAbility.setText(abilityName);
        abilitiesContainer.addView(view);
    }
}
