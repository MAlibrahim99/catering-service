package catering.catalog;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

@Entity
public class ware extends Product{

    public static enum ServiceType {
        EVENTCATERING, PARTYSERVICE, RENTACOOK, MOBILEBREAKFAST;
    }

    private String date;
    private ServiceType type;

    @SuppressWarnings({ "unused", "deprecation" })
	private ware() {}

    public ware(String name, Money price, ServiceType type){

        super(name, price);

        this.type = type;
    }


    public String getDate(){
        return date;
    }

    public ServiceType getType(){
        return type;
    }
    
}


