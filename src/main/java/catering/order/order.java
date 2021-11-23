package catering.order;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.persistence.Entity;

import org.salespointframework.catalog.Product;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.order.ChargeLine;
import org.salespointframework.order.Order;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

class order extends Order {
    
	private int guests;
    
	private int meals;

	private int tableware;

	private int deco;

    private  String date;
    private String place;
    private OptionType type;

    private int Cost = 100;
    private int SumCost;
    private int PersonalAnzahl = 100;
    private boolean PurchaseDone = false;
    private int cost1 = 100;
    private int cost2 = 200;
    private int cost3 = 300;
    private int cost4 = 500;
    
    private int waiter = 500;
    private int chefs = 100;

    public static enum OptionType{
        EVENTCATERING, PARTYSERVICE, RENTACOOK, MOBILEBREAKFAST;
    }

    public order(int guests, int meals, int tableware, int deco, String date, OptionType type){
        
        this.guests = guests;
        this.meals = meals;
        this.tableware = tableware;
        this.deco = deco;
        this.date = date;
        this.type = type;
    }
    
    public order(){
        
    }


    public int calcCost(int guestcount, int mealcount, int decocount, int tablewarecount, int option){ //option = enum? für Angebotskategorie ersetzen
        //if "Angebot" --> Gesamtkosten = "Grundpreis von Angebot" + Extras
        if (option == 1){
            SumCost = cost1 + guestcount * 50 + mealcount * 8 + decocount * 5 + tablewarecount * 10;
            return SumCost;
            }

        if (option == 2){
            SumCost = cost2 + guestcount * 50 + mealcount * 8 + decocount * 5 + tablewarecount * 10;
            return SumCost;
            }

        if (option == 3){
            SumCost = cost3 + guestcount * 50 + mealcount * 8 + decocount * 5 + tablewarecount * 10;
            return SumCost;
            }

        if (option == 4){
            SumCost = cost4 + guestcount * 50 + mealcount * 8 + decocount * 5 + tablewarecount * 10;
            return SumCost;
            }

        return 0;
        
    
    
    }
    
    public int buying(int guestcount, int mealcount, int dishcount, int decocount, int option){
        if (option == 1){                           //5 Personen = 3 Kellner, 3 Köche, 
            guestcount = guestcount / 5;
            if (guestcount == 0){
                guestcount = 1;
            }
            int chefcount = guestcount * 3;
            int waitercount = guestcount *3;

            if (chefcount <= chefs && waitercount <= waiter){               //Funktion aus Inventar für Personal benötigt
                System.out.println("Bestellung wird aufgegeben");           // return hinzufügen 
                PurchaseDone = true;
                calcCost(guestcount, mealcount, dishcount, decocount, 1);
            }
            else{
                System.out.println("Bestellung kann nicht aufgegeben werden");
                PurchaseDone = false;
            }
        }

        if (option == 2){
            guestcount = guestcount / 5;
            if (guestcount == 0){
                guestcount = 1;
            }
            int chefcount = guestcount * 3;
            int waitercount = guestcount *3;

            if (chefcount <= chefs && waitercount <= waiter){               //Funktion aus Inventar für Personal benötigt
                System.out.println("Bestellung wird aufgegeben");           // return hinzufügen 
                PurchaseDone = true;
                calcCost(guestcount, mealcount, dishcount, decocount, 1);
            }
            else{
                System.out.println("Bestellung kann nicht aufgegeben werden");
                PurchaseDone = false;
            }
        }

        if (option == 3){
            guestcount = guestcount / 5;
            if (guestcount == 0){
                guestcount = 1;
            }
            int chefcount = guestcount * 1;
            if (chefcount <= chefs){
                System.out.println("Bestellung wird aufgegeben");
                PurchaseDone = true;
                calcCost(guestcount, mealcount, dishcount, decocount, 3);
            }
            else{
                System.out.println("Bestellung kann nicht aufgegeben werden");
                PurchaseDone = false;
            }
        }

        if (option == 4){
            guestcount = guestcount / 5;
            if (guestcount == 0){
                guestcount = 1;
            }
            int waitercount = guestcount * 1;
            if (waitercount <= waiter){
                System.out.println("Bestellung wird aufgegeben");
                PurchaseDone = true;
                calcCost(guestcount, mealcount, dishcount, decocount, 4);
                }
            else{
                System.out.println("Bestellung kann nicht aufgegeben werden");
                PurchaseDone = false;
            }
        }
    
    
        return 0;
}




    public int getGuests(){
        return guests;
    }

    public int getMeals(){
        return meals;
    }

    public int getTableware(){
        return tableware;
    }

    public int getDeco(){
        return deco;
    }

    public String getDate(){
        return date;
    }

    public String getPlace(){
        return place;
    }
}

