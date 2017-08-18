package conRdf;


public class Property {
	public int propertyId;
	
	public String propertyName;
	
	public int noOfTypes;
	public int occurances;
	public double weight;
	
	public Property(int id, String name)
	{
		//this.propertyId = id;
		
		this.propertyName = name;
		//noOfTypes is for DBpedia Dataset and occurances for the evaluation dataset
		this.noOfTypes = 0;
		this.occurances= 1;
		this.weight= 0;
	}

}
