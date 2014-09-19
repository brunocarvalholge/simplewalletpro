package br.com.tolive.simplewalletpro.model;

/**
 * Created by bruno.carvalho on 31/07/2014.
 */
public class Category {
    public static final String ENTITY_NAME = "category";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String COLOR = "color";

    public static final int TYPE_GAIN = 0;
    public static final int TYPE_EXPENSE = 1;

    //TODO : CREATE COLOR NAME CONSTANTES

    private Long id;
    private String name;
    private int type;
    private int color;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", color=" + color +
                '}';
    }
}
