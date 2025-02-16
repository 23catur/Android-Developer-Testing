package com.example.Android_Developer_Testing.model;

import java.util.List;

public class Ability {
    private List<AbilityDetail> abilities;

    public List<AbilityDetail> getAbilities() {
        return abilities;
    }

    public static class AbilityDetail {
        private AbilityName ability;

        public AbilityName getAbility() {
            return ability;
        }
    }

    public static class AbilityName {
        private String name;

        public String getName() {
            return name;
        }
    }
}

