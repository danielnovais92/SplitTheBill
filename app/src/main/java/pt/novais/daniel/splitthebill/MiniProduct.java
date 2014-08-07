package pt.novais.daniel.splitthebill;

import java.io.Serializable;

public class MiniProduct implements Serializable {

    private String name;
    private Double cost;
    private Double quantity;

    public MiniProduct(String name, Double cost, Double quantity) {
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCost() {
        return cost;
    }

    public Double getQuantity() {
        return quantity;
    }
}
