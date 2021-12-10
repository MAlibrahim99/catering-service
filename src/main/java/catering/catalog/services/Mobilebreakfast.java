package catering.catalog.services;

public class Mobilebreakfast extends Mainservice {
    private int breakfast;

    public Mobilebreakfast(int serviette, int dishes, int breakfast){
        super(serviette, dishes);
        this.breakfast = breakfast;
    }

    public Mobilebreakfast(){

    }


    public int getBreakfast(){
        return breakfast;
    }


    public void setBreakfast(int breakfast){
        this.breakfast = breakfast;
    }
    
}



