package catering.catalog;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

@Entity
public class Ware extends Product{

    public static enum ServiceType {
        EVENTCATERING, PARTYSERVICE, RENTACOOK, MOBILEBREAKFAST;
    }

    private ServiceType type;
    private String description;

    @SuppressWarnings({ "unused", "deprecation" })
	private Ware() {}


    public Ware(String name, Money price, String description, ServiceType type){

        super(name, price);
        this.description = description;
        this.type = type;
    }


    public ServiceType getType(){
        return type;
    }

    public String getDescription(){
        return description;
    }
    

    
}


