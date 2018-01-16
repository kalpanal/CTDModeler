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

public class ExampleImporterWithDescriptions implements CartesianProductImporter {

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

 // @Override
   public void prepare() {
      // Nothing to do here in this example
	  
   }

   @Override
   public List<AttributeData> getAttributes() {

      // Return two attributes - a1 (of type String), and a2 (of type Boolean).

      ArrayList<AttributeData> ret = new ArrayList<AttributeData>();
      AttributeData a1 = new AttributeData("a1", AttributeData.Type.STRING,
            AttributeData.IOType.NEITHER);
      ret.add(a1);
      AttributeData a2 = new AttributeData("a2", AttributeData.Type.BOOLEAN,
            AttributeData.IOType.NEITHER);
      ret.add(a2);
      return ret;
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
      ret.addExclusionRestriction(false, "a1.equals(\"v1\") && a2",
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
      ret.add(vals2);
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

@Override
public boolean needInput() {
	// TODO Auto-generated method stub
	return false;
}

@Override
public boolean prepare(File arg0) throws ImporterException {
	// TODO Auto-generated method stub
	return false;
}

}

