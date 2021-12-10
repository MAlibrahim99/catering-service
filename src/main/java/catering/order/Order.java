package catering.order;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.format.annotation.DateTimeFormat;

import catering.user.User;

class Order {
    
    private String time;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String location;
    
    private int cost = 100;
    private int sumCost;
    private int personalAnzahl = 100;
    private boolean purchaseDone = false;
    private int cost1 = 100;
    private int cost2 = 200;
    private int cost3 = 300;
    private int cost4 = 500;
    
    private int waiter = 500;
    private int chefs = 100;

    private int chefcount;
    private int waitercount;
    public ArrayList<User> staffList = new ArrayList<User>();

    

    public Order(LocalDate date, String time, String location){
        

        this.date = date;
        this.time = time;
        this.location = location;
    }
    
    public Order(){
        
    }


    public int calcCost(int guestcount, int mealcount, int decocount,
						int tablewarecount, int option){ //option = enum? für Angebotskategorie ersetzen
        //if "Angebot" --> Gesamtkosten = "Grundpreis von Angebot" + Extras
        if (option == 1){
            sumCost = cost1 + guestcount * 50 + mealcount * 8 + decocount * 5 + tablewarecount * 10;
            return sumCost;
            }

        if (option == 2){
            sumCost = cost2 + guestcount * 50 + mealcount * 8 + decocount * 5 + tablewarecount * 10;
            return sumCost;
            }

        if (option == 3){
            sumCost = cost3 + guestcount * 50 + mealcount * 8 + decocount * 5 + tablewarecount * 10;
            return sumCost;
            }

        if (option == 4){
            sumCost = cost4 + guestcount * 50 + mealcount * 8 + decocount * 5 + tablewarecount * 10;
            return sumCost;
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

            if (chefcount <= chefs && waitercount <= waiter){//Funktion aus Inventar für Personal benötigt
                System.out.println("Bestellung wird aufgegeben");           // return hinzufügen 
                purchaseDone = true;
                calcCost(guestcount, mealcount, dishcount, decocount, 1);
            }else{
                System.out.println("Bestellung kann nicht aufgegeben werden");
                purchaseDone = false;
            }
        }

        if (option == 2){
            guestcount = guestcount / 5;
            if (guestcount == 0){
                guestcount = 1;
            }
            int chefcount = guestcount * 3;
            int waitercount = guestcount *3;

            if (chefcount <= chefs && waitercount <= waiter){//Funktion aus Inventar für Personal benötigt
                System.out.println("Bestellung wird aufgegeben");           // return hinzufügen 
                purchaseDone = true;
                calcCost(guestcount, mealcount, dishcount, decocount, 1);
            }else{
                System.out.println("Bestellung kann nicht aufgegeben werden");
                purchaseDone = false;
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
                purchaseDone = true;
                calcCost(guestcount, mealcount, dishcount, decocount, 3);
            }else{
                System.out.println("Bestellung kann nicht aufgegeben werden");
                purchaseDone = false;
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
                purchaseDone = true;
                calcCost(guestcount, mealcount, dishcount, decocount, 4);
                }
            else{
                System.out.println("Bestellung kann nicht aufgegeben werden");
                purchaseDone = false;
            }
        }
    
    
        return 0;
}





    public LocalDate getDate(){
        return date;
    }


    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getLocation(){
        return location;
    }

    public String toString(){
        return date + " " + time + " " + location + " " + chefcount + " " + waitercount;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(LocalDate date){
        this.date = date;
    }

    public int getChefcount(){
        return chefcount;
    }

    public void setChefcount(int chefcount){
        this.chefcount = chefcount;
    }

    public int getWaitercount(){
        return waitercount;
    }

    public void setWaitercount(int waitercount){
        this.waitercount = waitercount;
    }

    public ArrayList<User> getStafflist(){
        return staffList;
    }

    public void setStafflist(ArrayList<User> staffList){
        this.staffList = staffList;
    }

    


}

