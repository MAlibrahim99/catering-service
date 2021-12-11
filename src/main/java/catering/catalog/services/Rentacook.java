package catering.catalog.services;

public class Rentacook extends Mainservice{
	private int flowers;
	private int decoration;
	private int tablecloth;

	public Rentacook(int serviette, int dishes, int flowers, int decoration,
					 int tablecloth){
		super(serviette, dishes);
		this.flowers = flowers;
		this.decoration = decoration;
		this.tablecloth = tablecloth;


	}

	public Rentacook(){

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

	public void setFlowers(int flowers){
		this.flowers = flowers;
	}

	public void setDecoration(int decoration){
		this.decoration = decoration;
	}

	public void setTablecloth(int tablecloth){
		this.tablecloth = tablecloth;
	}

}
