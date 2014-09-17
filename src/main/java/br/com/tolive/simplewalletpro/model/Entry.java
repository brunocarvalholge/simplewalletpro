package br.com.tolive.simplewalletpro.model;

import java.io.Serializable;

/**
 * Created by bruno.carvalho on 27/06/2014.
 */
public class Entry implements Serializable{
    public static final String ENTITY_NAME = "entry";
    public static final String ID = "id";
    public static final String DESCRIPTION = "description";
    public static final String VALUE = "value";
    public static final String TYPE = "type";
    public static final String CATEGORY = "category";
    public static final String DATE = "date";
    public static final String MONTH = "month";

    //public static final String[] ATTRIBUTES = {ID, DESCRIPTION, VALUE, TYPE, CATEGORY, DATE, MONTH};

    public static final int TYPE_GAIN = 0;
    public static final int TYPE_EXPENSE = 1;

    private Long id;
    private String description;
    private Float value;
    private int type;
    private int category;
    private String date;
    private int month;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", value=" + value +
                ", type=" + type +
                ", category='" + category + '\'' +
                ", date='" + date + '\'' +
                ", month=" + month +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;

        if (object != null && object instanceof Entry) {
            sameSame = (this.getId() == ((Entry) object).getId());
        }

        return sameSame;
    }

    //Maybe need to override hashcode metod
}
