package catering.catalog.services;

public class Mobilebreakfast extends Mainservice {
    private int breakfast;
    private int noalk;

    public Mobilebreakfast(int serviette, int dishes, int breakfast, int noalk){
        super(serviette, dishes);
        this.breakfast = breakfast;
        this.noalk = noalk;
    }

    public Mobilebreakfast(){

    }


    public int getBreakfast(){
        return breakfast;
    }


    public void setBreakfast(int breakfast){
        this.breakfast = breakfast;
    }



    public int getNoalk(){
        return noalk;
    }

    public void setNoalk(int noalk){
        this.noalk = noalk;
    }
    
}



