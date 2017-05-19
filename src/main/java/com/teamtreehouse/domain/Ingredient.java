package com.teamtreehouse.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Ingredient extends BaseEntity {
    private String name;
    private String measurement;
    private int quantity;

    public Ingredient() {
        super();
    }

    public Ingredient(String name, String measurement, int quantity) {
        this();
        this.name = name;
        this.measurement = measurement;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

/*    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ingredient that = (Ingredient) o;

        if (quantity != that.quantity) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return measurement != null ? measurement.equals(that.measurement) : that.measurement == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (measurement != null ? measurement.hashCode() : 0);
        result = 31 * result + quantity;
        return result;
    }*/
}
