package catering.catalog.services;

public class Eventcatering extends Mainservice {
    private int flowers;
    private int decoration;
    private int tablecloth;
    private int buffet;
    private int galadinner;
    private int alk;
    private int noalk;

    public Eventcatering(int serviette, int dishes, int flowers, int decoration,
						 int tablecloth, int buffet, int galadinner, int alk, int noalk){
        super(serviette, dishes);
        this.flowers = flowers;
        this.decoration = decoration;
        this.tablecloth = tablecloth;
        this.buffet = buffet;
        this.galadinner = galadinner;
        this.alk = alk;
        this.noalk = noalk;


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

    public int getAlk(){
        return alk;
    }

    public void setAlk(int alk){
        this.alk = alk;
    }

    public int getNoalk(){
        return noalk;
    }

    public void setNoalk(int noalk){
        this.noalk = noalk;
    }
}


