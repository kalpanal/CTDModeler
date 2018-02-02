package com.ibm.focus.exporters;
/*****************************************************************************/
/* (c) IBM corporation (1997, 2014), ALL RIGHTS RESERVED */
/*****************************************************************************/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.ibm.focus.exporter.ExporterException;
import com.ibm.focus.exporter.TestPlanExporter;

public class ParasoftExporter implements TestPlanExporter {

   File goodPathOutputFile;
   File badPathOutputFile = null;
   BufferedWriter goodPathWriter;
   BufferedWriter badPathWriter = null;

   /**
    * Initialize the exporter - open output files and create writers
    * 
    * @param modelDirectory - the directory where the model is located
    * @param badPathTestPlanExists - true if bad path tests exist in the test
    * plan, false if don't
    * @throws ExporterException
    * @throws IOException
    */
   public void initialize(File modelDirectory, boolean badPathTestPlanExists)
         throws ExporterException, IOException {
      goodPathOutputFile = new File(modelDirectory, "goodPath");
      goodPathWriter = new BufferedWriter(new FileWriter(goodPathOutputFile));
      if (badPathTestPlanExists) {
         badPathOutputFile = new File(modelDirectory, "badPath");
         badPathWriter = new BufferedWriter(new FileWriter(badPathOutputFile));
      }
   }

   /**
    * Set the header of the test plan - write it to both good and bad path files
    * 
    * @param headerNames - the list of test plan attribute names
    * @throws ExporterException
    * @throws IOException
    */
   public void setHeader(List<String> headerNames) throws ExporterException,
         IOException {
      for (String name : headerNames) {
         goodPathWriter.write(name + " ");
      }
      goodPathWriter.write(System.getProperty("line.separator"));
      if (badPathWriter != null) {
         for (String name : headerNames) {
            badPathWriter.write(name + " ");
         }
         badPathWriter.write(System.getProperty("line.separator"));
      }
   }

   /**
    * Process single row (one test) of the test plan - write to the relevant
    * file
    * 
    * @param test - the values of the current row in the test plan
    * @param isGoodPathTest - true if the test belongs to the good path, false
    * otherwise
    * @throws ExporterException if isGoodPathTest is false but we don't have a
    * file for the bad path because of initilialize() parameters
    * @throws IOException
    */
   public void exportNextTest(List<String> test, boolean isGoodPathTest)
         throws ExporterException, IOException {
      @SuppressWarnings("resource")
      BufferedWriter writer = isGoodPathTest ? goodPathWriter : badPathWriter;
      if (writer == null) {
         throw new ExporterException(
               "Discovered bad path test in only good path tests report");
      }

      for (String t : test) {
         writer.write(t + " ");
      }
      writer.write(System.getProperty("line.separator"));
   }

   /**
    * Finalize the exporter - close the writers
    * 
    * @throws ExporterException
    * @throws IOException
    */
   public void finalize() throws ExporterException, IOException {
      goodPathWriter.close();
      if (badPathWriter != null) {
         badPathWriter.close();
      }
   }

@Override
public boolean doneExporting() throws ExporterException, IOException {
	// TODO Auto-generated method stub
	return false;
}
}