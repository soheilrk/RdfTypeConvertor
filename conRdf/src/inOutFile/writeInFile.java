package inOutFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class writeInFile {
	public void writeListe(String path, ArrayList list)
	{
		
		String adressedufichier = path ;
	
		
		try
		{
			
                           
			FileWriter fw = new FileWriter(adressedufichier, true);
			
			
			BufferedWriter output = new BufferedWriter(fw);
			
			
			
                             output.write(list.get(0).toString(), 0, list.get(0).toString().length());
                             
                            for (int i = 1; i < list.size(); i++) {
                            
                                output.write("\n" + list.get(i).toString(), 0, list.get(i).toString().length()+1 );
                                
                        }
                            
			
			
			output.flush();
			
			
			output.close();
			
			
		}
		catch(IOException ioe){
			System.out.print("Erreur : ");
			ioe.printStackTrace();
			}

	}

	public void writePartition2(String path,String N3DataSet) throws IOException {
        
        
		  FileWriter fw = new FileWriter(path, true);
		    try (BufferedWriter output = new BufferedWriter(fw)) {
		    	ArrayList<String> InstanceNames = new ArrayList<String>();
		    	ArrayList<String> rdfType = new ArrayList<String>();
		    	ArrayList<String> typeName = new ArrayList<String>();
		    	
		    	String[] data = readLines(N3DataSet);
		    	
		    	for (String line : data){
		    		String[] s = line.split("\t");
		    		InstanceNames.add(s[0]);
		    		rdfType.add(s[1]);
		    		typeName.add(s[2]);
		    	}
		    	
		    	
		    	String ligne = "";
		    	
		    	if (rdfType.contains("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>")){
		    		 
		    		ligne = InstanceNames.get(0);
		    		ligne += "\t" +"<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>" + "\t" + typeName.get(0) ;
		    	
		    			    		
		            output.write(ligne, 0, ligne.length());
		    	}
		        
		        
		        for (int i = 1; i < 300; i++) {
		        	if (rdfType.contains("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>")){
			    		 
			    		ligne = InstanceNames.get(i);
			    		ligne += "\t" +"<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>" + "\t" + typeName.get(i) ;
			    	
			    			    		
			            output.write(ligne, 0, ligne.length());
			    	}
		            
		            
		        }
		        
		          
		        output.flush();
		    }
		       
		   
		   
		   }


	public String[] readLines(String filename) throws IOException {
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




}
