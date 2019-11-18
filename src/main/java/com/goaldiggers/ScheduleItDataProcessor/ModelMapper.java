package com.goaldiggers.ScheduleItDataProcessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ModelMapper {
	
	String categoryName;
	String vendorName;
	
	/**
	 * 
	 * @param vendorName, vendorName defined in vendors.txt
	 * @param categoryName, corresponding category name for vendor name exist in vendors.txt
	 */
	ModelMapper(String vendorName,String categoryName) {
		this.vendorName = vendorName;
		this.categoryName = categoryName;
	}
	
	/**
	 * 
	 * @param dynamicStrings, List of dynamic strings values like string0 value,string1 value
	 * @return mapping model data with key as values present in {categoryName}category.txt file 
	 *                             with value as conclusions data 
	 * @throws IOException
	 */
	public Map<String,String> mappingData(List<String> dynamicStrings) throws IOException {
		Map<String,String> conclusionMap = new LinkedHashMap<String,String>();
		List<String> modelData = readModelCategoryData();
		List<String> conclusionData = readModelConclusionData(dynamicStrings);
		for(int i=0;i<=conclusionData.size()-1;i++) {
			conclusionMap.put(modelData.get(i), conclusionData.get(i));
		}
		return conclusionMap;
	}
	
	/**
	 * Read categories like hotel,flight,carrental data through text file with a pattern of {categoryName}category.txt
	 * @return
	 * @throws IOException
	 */
	private List<String> readModelCategoryData() throws IOException {
		String currentDirectory = new File( "." ).getCanonicalPath();
		//System.out.println(currentDirectory);
		FileReader modelCategoryData = new FileReader(new File(currentDirectory+"//src//main//resources//"+this.categoryName+"category.txt"));
		BufferedReader bufferedReader = new BufferedReader(modelCategoryData);
		List<String> ls = new ArrayList<String>();
		String line = null;
		while((line = bufferedReader.readLine()) != null) {
			//System.out.println(line);
			ls.add(line);
		}
		bufferedReader.close();
		modelCategoryData.close();
		return ls;
	}
	
	/**
	 * Read conclusion like airbnb,spirit,exploretrip,hotwire data through text file with a pattern of {vendorName}conclusion.txt
	 * @return
	 * @throws IOException
	 */
	private List<String> readModelConclusionData(List<String> dynamicStrings) throws IOException {
		Map<String,String> stringMappping = new HashMap<String,String>();
		int k=0;
		for(String s:dynamicStrings) {
    		stringMappping.put("string"+k, s);
    		k++;
    		//System.out.println(s);
    	}
		
		String currentDirectory = new File( "." ).getCanonicalPath();
		System.out.println(currentDirectory);
		FileReader modelConclusionData = new FileReader(new File(currentDirectory+"//src//main//resources//"+this.vendorName+"conclusion.txt"));
		BufferedReader bufferedReader = new BufferedReader(modelConclusionData);
		List<String> ls = new ArrayList<String>();
		String line = null;
		while((line = bufferedReader.readLine()) != null) {
			String[] words = line.split("\\s");
			String val = "";
			for(int i=0;i<=words.length-1;i++) {
				//System.out.println(words[i]);
				if(stringMappping.containsKey(words[i])) {
					val = val + stringMappping.get(words[i]);
				}else {
					val = val + words[i];
				}
				if(i!=words.length-1) {
					val = val+" ";
				}
				
			}
			ls.add(val);
		}
		bufferedReader.close();
		modelConclusionData.close();
		return ls;
	}
	
	
	
	
	
	

}
