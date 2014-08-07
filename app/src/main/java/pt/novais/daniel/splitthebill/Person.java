package pt.novais.daniel.splitthebill;

import java.io.Serializable;

public class Person implements Serializable {

    private Double paid;
    private Double owes;

    public Person(Double paid, Double owes) {
        this.paid = paid;
        this.owes = owes;
    }

    public Double getPaid() {
        return this.paid;
    }

    public Double getOwes() {
        return this.owes;
    }

    public void setOwes(Double owes) {
        this.owes = owes;
    }
}
