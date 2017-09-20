package pt.novais.daniel.splitthebill;


import java.io.Serializable;
import java.util.HashMap;

class Product implements Serializable {
    // instance variables - replace the example below with your own
    private String name;
    private Double cost;
    private HashMap<String,Boolean> checks; //<person_name,bool>
    private HashMap<String,Integer> seeks; //<person_name,seek_bar_%>


    Product(String name, Double cost, HashMap<String, Boolean> checks, HashMap<String, Integer> seeks) {
        this.name = name;
        this.cost = cost;
        this.checks = checks;
        this.seeks = seeks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    HashMap<String, Boolean> getChecks() {
        return checks;
    }

    public void setChecks(HashMap<String, Boolean> checks) {
        this.checks = checks;
    }

    HashMap<String, Integer> getSeeks() {
        return seeks;
    }

    public void setSeeks(HashMap<String, Integer> seeks) {
        this.seeks = seeks;
    }

    public String getName() {
        return this.name;
    }

    Double getCost() {
        return this.cost;
    }

}
