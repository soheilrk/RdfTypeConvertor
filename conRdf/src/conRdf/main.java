package conRdf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import conRdf.Instance;
import conRdf.Partition;

public class main {
	public static Partition partition=new Partition();
    public static void main(String[] args) throws IOException {
FileExits();
//System.out.println(partition.listObjets.get(0).instanceIds);
//createRdfN4("./EnumeratedData.txt");
//createTypesFile("./types.txt");
//createTypeIds("./Ids.txt");
//createIdUri("./Idsuris.txt");
createPropertyFile("./PropertyNames_NumberOfTypes.txt");
//createPropertiesPerInstance("./PropertyPerInstance.txt");
//System.out.println(partition.listObjets);
System.out.println("Done!");
//"C:\Users\rosha\OneDrive\Documents\db\dc-2010-complete"
//"C:\Users\rosha\OneDrive\Documents\db\largeDbKenza"
//"C:\Users\rosha\OneDrive\Documents\db\museum""
}
    
    
    //Read the N3Dataset file, then filter all the triples if the have <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> or not
    //afterwards It saves all the Instances in partition.Objects and all the types in listTypes
public static void readDataSet(String N3DataSet) throws IOException {
	
	int id =0 ;
	int maxId = -1;
	int typeId = 0;
	int propertyId = 0;
	int countLines = 0;
	int countTypes=0;
	ArrayList<Instance> instances = new ArrayList<Instance>();
	ArrayList<ConceptType> listTypes = new ArrayList<ConceptType>();
	ArrayList<Property> listProperties = new ArrayList<Property>();

	String[] data = readLines(N3DataSet);
	
	for (String line : data){
		String[] s = line.split(" ");
		
		if (s.length<3) continue;
		
		id = -1;
		int index = -1;
		for(Instance tempInstance : instances)
		{
			if (tempInstance.instanceNames.trim().contains(s[0].trim()))
			{
				id = tempInstance.instanceIds;
				index = instances.indexOf(tempInstance);

				break;
			}
		}
		
		boolean isType = s[1].contains("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>");
		
		//if (!s[1].contains("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>")) continue;

		int typeIndex = -1;
		int propIndex = -1;

		if (isType)
		{
			countTypes++;

			for (ConceptType type : listTypes)
			{
				if (type.typeName.trim().contains(s[2].trim()))
				{
					typeIndex = listTypes.indexOf(type);
	
					break;
				}
			}
			
			if (typeIndex == -1) 
			{
				ConceptType newType =new ConceptType(++typeId, s[2]); 
				listTypes.add(newType);
				typeIndex = listTypes.indexOf(newType);
				
			}

		}
		else
		{
			
			for (Property prop : listProperties)
			{
				if (prop.propertyName.trim().contains(s[1].trim()))
				{
					propIndex = listProperties.indexOf(prop);
					
					prop.occurances ++;
	
					break;
				}
			}
			
			if (propIndex == -1) 
			{
				Property newProperty =new Property(++propertyId, s[1]); 
				listProperties.add(newProperty);
				propIndex = listProperties.indexOf(newProperty);
				
			}		
		}

		if (id>=0)
		{				

		Instance tempInstance = instances.get(index);
		
		if (isType)
		{
			if (s[2]!=null) tempInstance.typeNames.add(s[2]);
		}
		else
			{
				boolean propFound = false;
				for(String prop : tempInstance.propertyNames)
				{
					if (prop.trim().contains(s[1].trim()))
					{
						propFound = true;
						break;
					}
				}
				
				if (!propFound && s[1]!= null)tempInstance.propertyNames.add(s[1]);
			}
		
		instances.set(index, tempInstance);
		}
		else if (id ==-1 ) 
		{	

			id = ++maxId;
			instances.add(new Instance(id, s[0], isType?s[2]:"", !isType?s[1]:""));


		}
		
		if (isType)
		{
		    ConceptType tempType = listTypes.get(typeIndex);
		    
		    tempType.instanceIds.add(id);
		    
		    listTypes.set(typeIndex, tempType);
		}
		
		//the triple is parsed
		++countLines;

	}
	
	int propIndex = -1;
	for(Property prop : listProperties)
	{
		for (Instance instance : instances)
		{
			for (String instProp : instance.propertyNames)
			{
				if (instProp.trim().contains(prop.propertyName.trim()))
				{
					prop.noOfTypes += instance.typeNames.size();
					break;
				}
			}
		}
	}
	//create the loop to clear the list that consume memory 
	
	partition.listObjets = instances;
	
	partition.listTypes = listTypes;
	
	partition.listProperties = listProperties;
	System.out.println(countLines);
	System.out.println(countTypes);
}
    

//Take the list of Object that we have from previous step and Enumurate and write them in EnumeratedData.txt
public static void createRdfN4(String path) throws IOException {
        
        
	  FileWriter fw = new FileWriter(path, true);
	    try (BufferedWriter output = new BufferedWriter(fw)) {
	    		    	

	    	
	    	
	    	String ligne = "";
	    	       
	    	for(Instance instance : partition.listObjets)
	    	{
	    		for(String type : instance.typeNames)
	    		{
		    		int i = partition.listObjets.indexOf(instance);
		    		int j = instance.typeNames.indexOf(type);
		    		ligne =  (i>0 || j>0?"\n":"")+ instance.instanceIds+" "+instance.instanceNames;
		    		ligne += " " +"<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>" + " " + type ;
		            output.write(ligne, 0, ligne.length());

	    		}
	    	}
	          
	        output.flush();
	    }
	       
	   
	   
	   }

public static void createIdUri(String path) throws IOException {
    
    
	  FileWriter fw = new FileWriter(path, true);
	    try (BufferedWriter output = new BufferedWriter(fw)) {
	    		    	

	    	
	    	
	    	String ligne = "";
	    	       
	    	for(Instance instance : partition.listObjets)
	    	{
		    		int i = partition.listObjets.indexOf(instance);
		    		ligne =  (i>0?"\n":"")+ instance.instanceIds+" "+instance.instanceNames;
		            output.write(ligne, 0, ligne.length());

	    		
	    	}
	          
	        output.flush();
	    }
	       
	   
	   
	   }


//The Name of the Types exist in our Dataset. and Write them in Types.txt
public static void createTypesFile(String typesFile) throws IOException {
    
    
	  FileWriter fw = new FileWriter(typesFile, true);
	    try (BufferedWriter output = new BufferedWriter(fw)) {
	    		    		    	
	    	
	    	String ligne = "";
	    	       
	    	for(ConceptType type : partition.listTypes)
	    	{
		    		int i = partition.listTypes.indexOf(type);
		    		ligne =  (i>0?"\n":"")+ type.typeName;
		            output.write(ligne, 0, ligne.length());

	    		
	    	}
	          
	        output.flush();
	    }
	       
   
	   
   }


//Method to Group By the Instance Ids  and write them in Ids.txt
public static void createTypeIds(String instanceIds ) throws IOException {
    	       
		  FileWriter fwIds = new FileWriter(instanceIds, true);
		    try (BufferedWriter output = new BufferedWriter(fwIds)) {
		    		    		    	
		    	
		    	String ligne = "";
		    	       
		    	for(ConceptType type : partition.listTypes)
		    	{
			    		int i = partition.listTypes.indexOf(type);
			    		String ids = "";
			    		for(Integer id : type.instanceIds)
			    		{
			    			int idIndex = type.instanceIds.indexOf(id);
			    			ids += (idIndex == 0? "":" ") + id;
			    		}
			    		ligne =  (i>0?"\n":"")+ ids;
			            output.write(ligne, 0, ligne.length());

		    		
		    	}
		          
		        output.flush();
		    }	   
	   
 }

//creating propetyfiles which is including propertynames and the number of types per property. 

public static void createPropertyFile(String propertyFile) throws IOException {
    
    
	  FileWriter fw = new FileWriter(propertyFile, true);
	    try (BufferedWriter output = new BufferedWriter(fw)) {
	    		    		    	
	    	
	    	String ligne = "";
	    	       
	    	for(Property getPropertyName : partition.listProperties)
	    	{
		    		int i = partition.listProperties.indexOf(getPropertyName);
		    		ligne =  (i>0?"\n":"")+ getPropertyName.propertyName +" "+ getPropertyName.noOfTypes+" "+getPropertyName.occurances;
		            output.write(ligne, 0, ligne.length());

	    		    	
	    	 }
	        output.flush();
	    }
	       
        

	   
 }

//creating instances property file
public static void createPropertiesPerInstance(String path) throws IOException {
    
    
	  FileWriter fw = new FileWriter(path, true);
	    try (BufferedWriter output = new BufferedWriter(fw)) {
	    		    	

	    	
	    	
	    	String ligne = "";
	    	       
	    	for(Instance instance : partition.listObjets)
	    	{
		    		int i = partition.listObjets.indexOf(instance);
		    		ligne =  (i>0?"\n":"")+ instance.instanceIds+" "+instance.instanceNames +" "+ instance.propertyNames+ " " + instance.typeNames.size();
		            output.write(ligne, 0, ligne.length());

	    		
	    	}
	    	
	        output.flush();
	    }
	       
	   
	   
	   }


public static String[] readLines(String filename) throws IOException {
    FileReader fileReader = new FileReader(filename);
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    List<String> lines = new ArrayList<String>();
    String line = null;
    while ((line = bufferedReader.readLine()) != null) {
        lines.add(line);
    }
    bufferedReader.close();
    return lines.toArray(new String[lines.size()]);
}



//Method to read the path of our input Dataset and also check if exits any output results from previous execution, it delete those files. 
public static void FileExits() throws IOException {
    
    
	String fileToDeleteEnumeratedData = "./EnumeratedData.txt";
	String fileToDeletetypes = "./types.txt";
	String fileToDeleteIds = "./Ids.txt";
	String fileToDeleteIdsuris = "./Idsuris.txt";
	String fileToDeletePropertyName = "./PropertyNames_NumberOfTypes.txt";
	String fileToDeletePropertyPerInstance = "./PropertyPerInstance.txt";

	File fileEnumeratedData=new File(fileToDeleteEnumeratedData);
	File filetypes=new File(fileToDeletetypes);
	File fileIds=new File(fileToDeleteIds);
	File fileIdsuris=new File(fileToDeleteIdsuris);
	File filePropertyName=new File(fileToDeletePropertyName);
	File filePropertyPerInstance=new File(fileToDeletePropertyPerInstance);


	if (fileEnumeratedData.exists()){
   fileEnumeratedData.delete();}
	
	if (filetypes.exists()){
		filetypes.delete();}
	
	if (fileIds.exists()){
		fileIds.delete();}
	
	if (fileIdsuris.exists()){
		fileIdsuris.delete();}
	
	if (filePropertyName.exists()){
		filePropertyName.delete();}
	
	if (filePropertyPerInstance.exists()){
		filePropertyPerInstance.delete();}
	
	
	//Reading the N3 Dataset Path
	    		 BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    	    System.out.print("Enter the PATH of your Dataset: ");
	    	        String dataPath = br.readLine();    	
	    	readDataSet(dataPath);
	    	
	    }
	   

}
	
