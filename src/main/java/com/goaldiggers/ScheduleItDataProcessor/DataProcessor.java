package com.goaldiggers.ScheduleItDataProcessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataProcessor {

	public static void main(String[] args) throws IOException {
       formatMessages();
       deriveConclusions();
       
       		

	}
	
	
	private static void deriveConclusions() throws IOException {
		
		String currentDirectory = new File( "." ).getCanonicalPath();
		System.out.println(currentDirectory);
		FileReader vendorsData = new FileReader(new File(currentDirectory+"//src//main//resources//vendors.txt"));
		BufferedReader bufferedReader = new BufferedReader(vendorsData);
		String line = null;
		Map<String,String> vendorCategory = new HashMap<String,String>();
		while((line = bufferedReader.readLine()) != null) {
			String s[] = line.split("\\s");
			vendorCategory.put(s[0], s[1]);
		}
		bufferedReader.close();
		vendorsData.close();
		Map<String,List<String>> vendorDataMapping =  new HashMap<String,List<String>>();
		for(Map.Entry entry:vendorCategory.entrySet()) {
			String vendorName = (String)entry.getKey();
			vendorDataMapping.put(vendorName, new ArrayList<String>());
			File file = new File(currentDirectory+"//src//main//resources//"+vendorName+".txt");
			if(file.exists()) {
				FileReader  vendorFileReader = new FileReader(file);
				bufferedReader = new BufferedReader(vendorFileReader);
				while((line = bufferedReader.readLine()) != null) {
					vendorDataMapping.get(vendorName).add(line);
				}
				bufferedReader.close();
				vendorFileReader.close();
			}
		}
		
		
		String directoryName = "D:\\MailMessages\\DataFormat\\";
		File folder = new File(directoryName);
        File[] listOfFiles = folder.listFiles();
        String fileName = null;
        int i=0; 
        
        
       
        for (i = 0; i < listOfFiles.length; i++) 
        //if(i==0)	
        {
        	List<String> patterns = new ArrayList<String>();
        	int patternIndex = 0;
            //String currentPatterValue = "";
            int sequenceId = 1;
            int startPatternIndex = 0;
        	List<String> dynamicString = new ArrayList<String>();
        	if (listOfFiles[i].isFile()) {
        		//System.out.println("File " + listOfFiles[i].getName());
    		    fileName  =  listOfFiles[i].getName();
    		    FileReader fileReader = new FileReader(new File(directoryName+fileName));
                bufferedReader = new BufferedReader(fileReader);
                int count = 0;
                String userEmailId = "";
        		String vendor = "";
        		boolean isPatternFound = false;
                while((line = bufferedReader.readLine()) != null) {
                	count++;
                	if(count==4) {
                		userEmailId = line;
                	}
                	String word = line.toLowerCase();
                	if(vendorCategory.containsKey(word) && !isPatternFound) {
                		vendor = line;
                		patterns = vendorDataMapping.get(word);
                		isPatternFound = true;
                	}
                	
                	
                    if(isPatternFound) {
                    	String wordIndex[] = patterns.get(patternIndex).split("\\s");
                    	if(Integer.parseInt(wordIndex[1])==sequenceId) {
                    		if(wordIndex[0].equals(line)) {
                    			patternIndex++;
                    			sequenceId++;
                        	}else if(wordIndex[0].equals("anyString")) {
                    			dynamicString.add(line);
                    			patternIndex++;
                    			sequenceId++;
                        	}else {
                        		sequenceId = 1;
                        		patternIndex = startPatternIndex;
                        	}
                    		
                    		
                    	}else {
                    		sequenceId = 1;
                    		startPatternIndex = patternIndex;
                    	}
                    	
                    	if(patternIndex==patterns.size()) {
                        	for(String s:dynamicString) {
                        		//System.out.println(s);
                        	}
                    		break;
                    	}
                		
                	}
                	
                    
                	
                }
                
                ModelMapper mapper = new ModelMapper(vendor.toLowerCase(), vendorCategory.get(vendor.toLowerCase()));
                Map<String,String> resultSet = mapper.mappingData(dynamicString);
                
                for(Map.Entry entry: resultSet.entrySet()) {
                	System.out.println(entry.getKey()+" "+entry.getValue());
                }
                
                
                System.out.println("-----------------------------");
                bufferedReader.close();
                fileReader.close();
        	}
        	
        	
        }
		
		
		
		
		
		
		
		
		
		
		
		
		
	}


	public static void formatMessages() {
		
        String line = null;
        String directoryName = "D:\\MailMessages\\";
        File folder = new File(directoryName);
        File[] listOfFiles = folder.listFiles();
        String fileName = null;
        
        String writeDirectoryName = "D:\\MailMessages\\DataFormat\\";

        try {
            // FileReader reads text files in the default encoding.
        	
        	for (int i = 0; i < listOfFiles.length; i++) {
        		  if (listOfFiles[i].isFile()) {
        		    System.out.println("File " + listOfFiles[i].getName());
        		    fileName  =  listOfFiles[i].getName();
        		    FileReader fileReader = new FileReader(new File(directoryName+fileName));
        		    
                    BufferedReader bufferedReader = 
                        new BufferedReader(fileReader);
                    
                    FileWriter fileWriter =
                            new FileWriter(new File(writeDirectoryName+fileName));
                    BufferedWriter bufferedWriter =
                            new BufferedWriter(fileWriter);

                    while((line = bufferedReader.readLine()) != null) {
                    	String words[] = line.split("\\s+");
                    	
                    	for(int j=0;j<=words.length-1;j++) {
                    		if(!words[j].contentEquals("")) {
                    			bufferedWriter.write(words[j]);
                        		bufferedWriter.newLine();
                    		}
                    	}
                        //System.out.println(line);
                    }   

                    bufferedReader.close(); 
                    bufferedWriter.close();
        		    
        		  } else if (listOfFiles[i].isDirectory()) {
        		    System.out.println("Directory " + listOfFiles[i].getName());
        		  }
        		}
        	
        	
           

            
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + fileName);                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" + fileName);                  
        }
		
	}
	
	
	
	

}
