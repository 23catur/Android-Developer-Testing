package com.example.Android_Developer_Testing.api;

import com.example.Android_Developer_Testing.model.PokemonDetail;
import com.example.Android_Developer_Testing.model.PokemonResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("pokemon?limit=50")
    Call<PokemonResponse> getPokemonList();

    @GET("pokemon/{id}")
    Call<PokemonDetail> getPokemonDetail(@Path("id") int pokemonId);
}
