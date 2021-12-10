package catering.catalog.services;

public class Mainservice {
    private int serviette;
    private int dishes;


    public Mainservice(int serviette, int dishes){
        this.serviette = serviette;
        this.dishes = dishes;
    }

    public Mainservice(){
        
    }

    public int getServiette(){
        return serviette;
    }

    public int getDishes(){
        return dishes;
    }

    public void setServiette(int serviette){
        this.serviette = serviette;
    }

    public void setDishes(int dishes){
        this.dishes = dishes;
    }

}
