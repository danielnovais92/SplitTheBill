package pt.novais.daniel.splitthebill;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Save implements Serializable{

    private Date date;
    private Map<String, Person> people;
    private ArrayList<Product> products;

    public Save(Date date, Map<String, Person> people, ArrayList<Product> products) {
        this.date = date;
        this.people = people;
        this.products = products;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Map<String, Person> getPeople() {
        return people;
    }

    public void setPeople(Map<String, Person> people) {
        this.people = people;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
