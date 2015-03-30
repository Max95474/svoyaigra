package model;

import java.util.ArrayList;

public class Package {
    private String name;
    private ArrayList<Theme> themes;

    public Package(String name, ArrayList<Theme> themes) {
        this.name = name;
        this.themes = themes;
    }

    public ArrayList<Theme> getThemes() {
        return themes;
    }

    public String getName() {
        return name;
    }

    public int size() {
        return themes.size();
    }
}
