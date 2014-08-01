package br.com.tolive.simplewalletpro.model;

/**
 * Created by bruno.carvalho on 31/07/2014.
 */
public class Category {
    public static final String ENTITY_NAME = "category";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String COLOR = "color";

    private Long id;
    private String name;
    private int color;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
