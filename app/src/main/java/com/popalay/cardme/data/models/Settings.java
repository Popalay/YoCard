package com.popalay.cardme.data.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Settings extends RealmObject {

    @PrimaryKey
    private String id = "0";
    private String language;
    private String theme;
    private boolean cardBackground;

    public Settings() {}

    private Settings(Builder builder) {
        setId(builder.id);
        setLanguage(builder.language);
        setTheme(builder.theme);
        setCardBackground(builder.cardBackground);
    }

    public String getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    private void setId(String id) {
        this.id = id;
    }

    public void setCardBackground(boolean cardBackground) {
        this.cardBackground = cardBackground;
    }

    public boolean isCardBackground() {
        return cardBackground;
    }

    public static final class Builder {

        private String language;
        private String theme;
        private boolean cardBackground;
        private String id;

        public Builder() {}

        public Builder language(String val) {
            language = val;
            return this;
        }

        public Builder theme(String val) {
            theme = val;
            return this;
        }

        public Builder cardBackground(boolean val) {
            cardBackground = val;
            return this;
        }

        public Settings build() {
            return new Settings(this);
        }

        public Builder id(String val) {
            id = val;
            return this;
        }
    }
}