package catering.catalog.services;

public class Eventcatering extends Mainservice {
    private int flowers;
    private int decoration;
    private int tablecloth;
    private int buffet;
    private int galadinner;

    public Eventcatering(int serviette, int dishes, int flowers, int decoration,
						 int tablecloth, int buffet, int galadinner){
        super(serviette, dishes);
        this.flowers = flowers;
        this.decoration = decoration;
        this.tablecloth = tablecloth;
        this.buffet = buffet;
        this.galadinner = galadinner;


    }

    public Eventcatering(){

    }

    public int getFlowers(){
        return flowers;
    }

    public int getDecoration(){
        return decoration;
    }

    public int getTablecloth(){
        return tablecloth;
    }

    public int getBuffet(){
        return buffet;
    }

    public int getGaladinner(){
        return galadinner;
    }

    
    public void setFlowers(int flowers){
        this.flowers = flowers;
    }

    public void setDecoration(int decoration){
        this.decoration = decoration;
    }

    public void setTablecloth(int tablecloth){
        this.tablecloth = tablecloth;
    }

    public void setBuffet(int buffet){
        this.buffet = buffet;
    }

    public void setGaladinner(int galadinner){
        this.galadinner = galadinner;
    }
}


