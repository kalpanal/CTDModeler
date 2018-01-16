package com.ibm.focus.importers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.raml.v2.api.RamlModelBuilder;
import org.raml.v2.api.RamlModelResult;
import org.raml.v2.api.model.v08.api.Api;
import org.raml.v2.api.model.v08.resources.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.focus.importer.CartesianProductImporter;
import com.ibm.focus.importer.FreeTemplateTriggerData;
import com.ibm.focus.importer.ImporterException;
import com.ibm.focus.importer.ImporterRestrictions;
import com.ibm.focus.importer.ModelInfo;
import com.ibm.focus.importer.TriggerData;
import com.ibm.focus.importer.TriggerData.TriggerAppliance;
import com.ibm.focus.model.AttributeData;

/*****************************************************************************/
/* (c) IBM corporation (1997, 2014), ALL RIGHTS RESERVED */
/*****************************************************************************/

public class ReadRAML implements CartesianProductImporter{
	
	 @Override
	   public String getImporterDescription() {
	      return "An importer sample";
	   }

	   @Override
	   public Set<String> getWarnings() {

	      // No warnings generated during import

	      return null;
	   }

	   @Override
	   public ModelInfo initAndGetModelInfo(File[] arg0) {

	      // Default ModelInfo is good enough for this example

	      return new ModelInfo(null);
	   }

	  @Override
	  public boolean prepare(File f) {
	      // Nothing to do here in this example
	      return true;
	 }

	   @Override
	   public boolean needInput(){
		   return true;
	   }
	   @Override
	   public List<AttributeData> getAttributes() {

	      // Return two attributes - a1 (of type String), and a2 (of type Boolean).

	                 ArrayList<AttributeData> attributes = new ArrayList<AttributeData>();
		  	         String line = null;
		  	       
		  	         try{
		  	        	 System.out.println("start of attributes list of RAML");
						 //File f = new File("C:\\FOCUS\\FOCUS-2.14\\Data.txt");
		  	        	ArrayList<AppConfigurationPropertiesForDataSheet> appConfigPropertiesForDatasheets = Util.loadPropertiesForDataSheets("C:\\\\FOCUS\\\\FOCUS-2.14\\\\DocDelivery.1.raml");
							RamlModelResult ramlModelResult = new RamlModelBuilder()
									.buildApi("C:\\FOCUS\\FOCUS-2.14\\DocDelivery.1.raml");
									ramlModelResult.getApiV08();
									ramlModelResult.getSecurityScheme();
								
									Api api = (Api) ramlModelResult.getApiV08();
									// START***************get all endpoint URLs from RAML
									// file***********************/
									ArrayList<ConfigurationTO> configurationTOEndPointList = new ArrayList<ConfigurationTO>();
									
									AtomicInteger profileMappingID = new AtomicInteger();
									//ArrayList<AppConfigurationProperties> datasheetListAndEndpoints = Util.loadProperties();
									api.resources().forEach(
											(urlEndPointsNode) -> {
												System.out.println("EndPoints are ==>"
														+ urlEndPointsNode.displayName()
														+ "===>resources size====>"
														+ urlEndPointsNode.resources().size());
												System.out.println(appConfigPropertiesForDatasheets.stream().filter(p -> p.getEndpointUrl().equals(urlEndPointsNode.resourcePath())));
												
												List<AppConfigurationPropertiesForDataSheet> dataSheetsListNodeLevel = appConfigPropertiesForDatasheets.stream().filter(p -> p.getEndpointUrl().equals(urlEndPointsNode.resourcePath())).collect(Collectors.toList());
												
												System.out.println(dataSheetsListNodeLevel);
											});
		  	        	 
						 /*BufferedReader bReader = new BufferedReader ( new FileReader(f));
						 while ((line = bReader.readLine()) != null){
		  		         String[] attrStr = line.split("\\s");
		  			         for (int i = 0; i < attrStr.length; i++){
					 	  	   	  attributes.add(new AttributeData(attrStr[i], AttributeData.Type.STRING,
					 	  	               AttributeData.IOType.NEITHER));
					 	              }
								  }*/
	                 }catch (Exception e) {
	}
	      return attributes;
	}
	   
	   
	   @Override
	   public Set<String> getNegativeValues(int index) {

	      // Value "v2" of attribute a1 (index 1) is the only negative value in our
	      // model

	      if (index == 0) {
	         Set<String> ret = new HashSet<String>();
	         ret.add("v2");
	         return ret;
	      }
	      return null;
	   }

	   @Override
	   public ImporterRestrictions getRestrictions() {

	      // Return the only restriction in our model

	      ImporterRestrictions ret = new ImporterRestrictions();
	     // ret.addExclusionRestriction(false, "a1.equals(\"v1\") && a2",
	           // "some restrictions");
	       ret.addExclusionRestriction(false, "a1.equals(\"v1\") && a2.equals(\"v1\")",
	       "some restrictions");
	       ret.addExclusionRestriction(false, "a1.equals(\"v1\") && a2.equals(\"v4\")",
	    	       "some restrictions");
	      return ret;
	   }

	   @Override
	   public List<SortedSet<String>> getValues() {

	      // a1 (first attribute) has values "v1", "v2", "v3". Second attribute (a2)
	      // is a boolean - no need to supply values for it

	      List<SortedSet<String>> ret = new ArrayList<SortedSet<String>>();
	      SortedSet<String> vals1 = new TreeSet<String>();
	      vals1.add("v1");
	      vals1.add("v2");
	      vals1.add("v3");
	      ret.add(vals1);
	      SortedSet<String> vals2 = new TreeSet<String>();
	      vals2.add("v4");
	      vals2.add("v5");
	      vals2.add("v6");
	      ret.add(vals2);
	      
	      /*SortedSet<Boolean> vals3 = new TreeSet<Boolean>();
	      vals3.add(true);
	      ret.add(vals3);*/
	      return ret;
	   }

	   @Override
	   public Map<String, Set<String>> getValuesDependencies(int arg0) {

	      // No value dependencies in our example

	      return null;
	   }

	   @Override
	   public boolean isInputOutput() {

	      // The model should not be an input-output model

	      return false;
	   }

	   /*
	    * (non-Javadoc)
	    *
	    * @see com.ibm.focus.importer.Importer#verifyCompatibleInput(java.io.File[])
	    */
	   @Override
	   public void verifyCompatibleInput(File[] rootDirsOrFiles)
	         throws ImporterException {
	      // TODO Auto-generated method stub

	   }

	   @Override
	   public List<TriggerData> getTriggers() {
	      List<TriggerData> triggers = new ArrayList<TriggerData>();
	      triggers.add(new FreeTemplateTriggerData("t1", "a1.equals(\"v2\")", false,
	            TriggerAppliance.ALWAYS, "t1 description"));
	      triggers.add(new FreeTemplateTriggerData("t2", "a1.equals(\"v1\")", true,
	            TriggerAppliance.ALWAYS, "t2 description"));
	      triggers.add(new FreeTemplateTriggerData("t3", "a2==true", false,
	            TriggerAppliance.RANDOM_TEST, "t3 description"));
	      return triggers;
	   }

	   @Override
	   public List<Integer> getCoverageRequirementsInteractionLevels() {
	      ArrayList<Integer> levels = new ArrayList<Integer>();
	      levels.add(2);
	      return levels;
	   }

	   @Override
	   public List<List<String>> getCoverageRequirementsAttributesNames() {
	      List<List<String>> attrNamesLists = new ArrayList<List<String>>();
	      List<String> allAttrNames = new ArrayList<String>();
	      for (AttributeData attr : getAttributes()) {
	         allAttrNames.add(attr.name);
	      }
	      attrNamesLists.add(allAttrNames);
	      return attrNamesLists;
	   }

	   @Override
	   public Map<String, List<Integer>> getAttributesValuesWeights() {
	      return Collections.emptyMap();
	   }


}
