package com.ibm.focus.importers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.io.*;


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

public class SampleImporterOne implements CartesianProductImporter {
	
	 File f1 = null;
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
	   f1= arg0[0];
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
	  	        	 System.out.println("start of attributes list");
					 //File f = new File("C:\\FOCUS\\FOCUS-2.14\\Data.txt");
	  	        	 
					 BufferedReader bReader = new BufferedReader ( new FileReader(f1));
					 while ((line = bReader.readLine()) != null){
	  		         String[] attrStr = line.split("\\s");
	  			         for (int i = 0; i < attrStr.length; i++){
				 	  	   	  attributes.add(new AttributeData(attrStr[i], AttributeData.Type.STRING,
				 	  	               AttributeData.IOType.NEITHER));
				 	              }
							  }
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
    	       "v1 and v2 are equal");
      return ret;
   }

   @Override
   public List<SortedSet<String>> getValues() {

      // a1 (first attribute) has values "v1", "v2", "v3". Second attribute (a2)
      // is a boolean - no need to supply values for it

      List<SortedSet<String>> ret = new ArrayList<SortedSet<String>>();
      System.out.println("Inside get values--->");
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
      SortedSet<String> vals3 = new TreeSet<String>();
      ret.add(vals3);
      SortedSet<String> vals4 = new TreeSet<String>();
      ret.add(vals4);
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

