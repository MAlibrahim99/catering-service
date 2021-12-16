package catering.catalog.services;

import javax.mail.Part;

public class Partyservice extends Mainservice{
    private int hamplate;
    private int cheeseplate;
    private int eggplate;
    private int fishplate;
    private int fruitplate;
    private int saladplate;
    private int sushi;
    private int pizza;
    private int seafood;
    private int alk;
    private int noalk;


    public Partyservice(int serviette, int dishes, int hamplate, int cheeseplate,
						int eggplate, int fishplate, int fruitplate, int saladplate, 
                        int sushi, int pizza, int seafood, int alk, int noalk){
        super(serviette, dishes);
        this.hamplate = hamplate;
        this.cheeseplate = cheeseplate;
        this.eggplate = eggplate;
        this.fishplate = fishplate;
        this.fruitplate = fruitplate;
        this.saladplate = saladplate;
        this.sushi = sushi;
        this.pizza = pizza;
        this.seafood = seafood;
        this.alk = alk;
        this.noalk = noalk;
    }
    
    public Partyservice(){

    }

    public int getHamplate(){
        return hamplate;
    }

    public int getCheeseplate(){
        return cheeseplate;
    }

    public int getEggplate(){
        return eggplate;
    }

    public int getFishplate(){
        return fishplate;
    }

    public int getSaladplate(){
        return saladplate;
    }

    public int getSushi(){
        return sushi;
    }

    public int getPizza(){
        return pizza;
    }

    public int getFruitplate(){
        return fruitplate;
    }

    public int getSeafood(){
        return seafood;
    }

    public void setHamplate(int hamplate){
        this.hamplate = hamplate;
    }

    public void setCheeseplate(int cheeseplate){
        this.cheeseplate = cheeseplate;
    }

    public void setEggplate(int eggplate){
        this.eggplate = eggplate;
    }

    public void setFishplate(int fishplate){
        this.fishplate = fishplate;
    }

    public void setFruitplate(int fruitplate){
        this.fruitplate = fruitplate;
    }

    public void setSaladplate(int saladplate){
        this.saladplate = saladplate;
    }

    public void setSushi(int sushi){
        this.sushi = sushi;
    }

    public void setPizza(int pizza){
        this.pizza = pizza;
    }

    public void setSeafood(int seafood){
        this.seafood = seafood;
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
