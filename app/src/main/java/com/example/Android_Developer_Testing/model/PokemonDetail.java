package com.example.Android_Developer_Testing.model;

import java.util.List;

public class PokemonDetail {
    private int id;
    private String name;
    private Sprites sprites;
    private List<AbilityWrapper> abilities;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public List<AbilityWrapper> getAbilities() {
        return abilities;
    }

    public static class Sprites {
        private String front_default;

        public String getFrontDefault() {
            return front_default;
        }
    }

    public static class AbilityWrapper {
        private Ability ability;

        public Ability getAbility() {
            return ability;
        }
    }

    public static class Ability {
        private String name;

        public String getName() {
            return name;
        }
    }
}
