package com.ibm.focus.importers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.jdom2.Element;
import org.json.JSONException;
import org.json.JSONObject;
import org.raml.v2.api.RamlModelBuilder;
import org.raml.v2.api.RamlModelResult;
import org.raml.v2.api.model.v08.api.Api;
import org.raml.v2.api.model.v08.bodies.Response;
import org.raml.v2.api.model.v08.methods.Method;
import org.raml.v2.api.model.v08.parameters.Parameter;
import org.raml.v2.api.model.v08.resources.Resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ibm.focus.importer.CartesianProductImporter;
import com.ibm.focus.importer.ImporterException;
import com.ibm.focus.importer.ImporterRestrictions;
import com.ibm.focus.importer.ModelInfo;
import com.ibm.focus.importer.TriggerData;
import com.ibm.focus.model.AttributeData;
import com.ibm.focus.model.Restrictions;

public class ParasoftImporter implements CartesianProductImporter {

	ArrayList<AttributeData> ret = new ArrayList<AttributeData>();
	ArrayList<AttributeData> ret1 = new ArrayList<AttributeData>();
	HashSet<String> uniqueAttributesOutput =  new HashSet<String>();
	ArrayList<String> retValues = new ArrayList<String>();
	File ramlfilename=null;
	File configFile = null;
	String lastUpdatedEndpoint = null;
	int count =0;
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
		ramlfilename =arg0[0];
		//configFile=arg0[1];
		return new ModelInfo(null);
	}
	@Override
	public boolean needInput() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean prepare(File arg0) throws ImporterException {
		// TODO Auto-generated method stub
		return true;
	}



	@Override
	public List<AttributeData> getAttributes() {
		count++;
		//if(count == 1){
		ret = new ArrayList<AttributeData>();
		//System.out.println("getsttribtes is called once");
		try {
			RamlModelResult ramlModelResult = new RamlModelBuilder().buildApi(ramlfilename);
			//ramlModelResult.getApiV08();
			//ramlModelResult.getSecurityScheme();

			Api api = (Api) ramlModelResult.getApiV08();
			
			if(lastUpdatedEndpoint == null){
				lastUpdatedEndpoint = Util.loadProperties();
			}
			try{
			System.out.println("lastUpdatedEndpoint"+ramlfilename);
			//System.out.println("ramlfilename"+api);
			}catch(Exception ec){
				System.out.println(ec.getMessage());
			}

			// START***************get all endpoint URLs from RAML
			// file**********************

			ArrayList<ConfigurationTO> configurationTOEndPointList = new ArrayList<ConfigurationTO>();
			LinkedList<String> listOfEndPoints = new LinkedList<String>();
			LOOP1:for(Resource urlEndPointsNode : api.resources()) {
				//System.out.println(urlEndPointsNode.displayName());
				AtomicInteger methodPosition = new AtomicInteger();
				List<Method> methodTypes = urlEndPointsNode.methods();
				//listOfEndPoints.add(urlEndPointsNode.displayName()+";"+urlEndPointsNode.methods().get(methodPosition.get()).method())
				LOOP2:for(Method methodName : methodTypes)  {	
					
					listOfEndPoints.add(urlEndPointsNode.displayName()+";"+urlEndPointsNode.methods().get(methodPosition.get()).method());
					methodPosition.incrementAndGet();								
				}
				LOOP3:for(Resource urlEndPointsSubNode : urlEndPointsNode.resources()){	

					List<Method> methodTypes1 = urlEndPointsSubNode.methods();
					AtomicInteger methodPosition1 = new AtomicInteger();
					LOOP4:for(Method methodName : methodTypes1)  {
						
						listOfEndPoints.add(urlEndPointsNode.displayName()+urlEndPointsSubNode.displayName()+";"+urlEndPointsSubNode.methods().get(methodPosition1.get()).method());
						methodPosition1.incrementAndGet();
					}

				}
			}
			//ArrayList<AppConfigurationProperties> datasheetListAndEndpoints = Util.loadProperties();
			String generateModelForEndPointURL = null;
			for(int k=0;k< listOfEndPoints.size();k++){
				String endPointURL = listOfEndPoints.get(k);
				//System.out.println("endPointURL----------------------->"+endPointURL+"----------"+lastUpdatedEndpoint);
				if(lastUpdatedEndpoint.equals("")){
					generateModelForEndPointURL = listOfEndPoints.get(0);
					break;
				}
				else if(lastUpdatedEndpoint.equalsIgnoreCase(endPointURL)){						
					if(k == (listOfEndPoints.size()-1)){
						//System.out.println("listOfEndPoints.get(0)++++++++++++++++++++"+listOfEndPoints.get(0));
						generateModelForEndPointURL = listOfEndPoints.get(0);
						break;
					}else{
						//System.out.println("listOfEndPoints.get(k+1)++++++++++++++++++++"+listOfEndPoints.get(k+1));
						generateModelForEndPointURL = listOfEndPoints.get(k+1);
						break;
					}
				}

			}
			
			if(generateModelForEndPointURL == null){
				generateModelForEndPointURL = listOfEndPoints.get(0);
			}

			//System.out.println("generateModelForEndPointURL=================>"+generateModelForEndPointURL);
			try{			
				LOOP1:for(Resource urlEndPointsNode : api.resources()) {
					AtomicInteger methodPosition = new AtomicInteger();
					List<Method> methodTypes = urlEndPointsNode.methods();
					LOOP2:for(Method methodName : methodTypes)  {	
						//System.out.println("pick next value inside loop2"+urlEndPointsNode.displayName()+";"+urlEndPointsNode.methods().get(methodPosition.get()).method());
						ConfigurationTO configurationTO = null;
						if((urlEndPointsNode.displayName()+";"+urlEndPointsNode.methods().get(methodPosition.get()).method()).equalsIgnoreCase(generateModelForEndPointURL)){
							try {
								configurationTO = copyRAMLDataToDTO(urlEndPointsNode, methodPosition.get());
								configurationTO.setQueryParameters(urlEndPointsNode.methods().get(methodPosition.get()).queryParameters());
								configurationTOEndPointList.add(configurationTO);
								Util.updateProperties(generateModelForEndPointURL); 
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						methodPosition.incrementAndGet();								
					}
					// if(urlEndPointsNode.resources().size() > 0){
					int lastElementOfLoopSubresource = 0;
					LOOP3:for(Resource urlEndPointsSubNode : urlEndPointsNode.resources()){		
						//System.out.println(urlEndPointsNode.displayName()+urlEndPointsSubNode.displayName());
						AtomicInteger methodPosition1 = new AtomicInteger();
						List<Method> methodTypes1 = urlEndPointsSubNode.methods();
						LOOP4:for(Method methodName : methodTypes1)  {
							ConfigurationTO configurationTOSubNode = null;

							if((urlEndPointsNode.displayName()+urlEndPointsSubNode.displayName()+";"+urlEndPointsSubNode.methods().get(methodPosition1.get()).method()).equalsIgnoreCase(generateModelForEndPointURL)){
								try {
									configurationTOSubNode = copyRAMLDataToDTO(urlEndPointsSubNode,methodPosition1.get());
									configurationTOSubNode.setQueryParameters(urlEndPointsSubNode.methods().get(methodPosition1.get()).queryParameters());
									//System.out.println("inside loop 4+++++++++++++++++++++++++"+urlEndPointsSubNode.methods().get(methodPosition1.get()).body().get(0).schema().value().toString());
									
									configurationTOEndPointList.add(configurationTOSubNode);
									Util.updateProperties(generateModelForEndPointURL);

								} catch (Exception e) {
									e.printStackTrace();
								}

							}

							methodPosition1.incrementAndGet();
						}

					}	

				}
			}catch(Exception ec){
				if(ec instanceof LastUpdatedFoundException){
					//do nothing but proceed
				}else{
					System.out.println("Error in main method"+ec.getMessage());
				}

			}
			
			
			AttributeData messageId = new AttributeData("MessageID", AttributeData.Type.STRING,
					AttributeData.IOType.INPUT);
			ret.add(messageId);
			
			AttributeData traceabilityId = new AttributeData("TraceabilityId", AttributeData.Type.STRING,
					AttributeData.IOType.INPUT);
			ret.add(traceabilityId);
			AttributeData responseCode = new AttributeData("responseCode", AttributeData.Type.STRING,
					AttributeData.IOType.INPUT);
			ret.add(responseCode);

			//System.out.println("configurationTOEndPointList should be one always------>"+configurationTOEndPointList.size());
			if(configurationTOEndPointList.size() > 0) {
				for(ConfigurationTO configurationTO : configurationTOEndPointList){
					//System.out.println("configurationTO.getQueryParameters()======================>"+configurationTO.getQueryParameters().size());
					if(configurationTO.getInputSampleString() != null){
						jsonString2Map(configurationTO.getInputSampleString(), ret, null, "INPUT");
					}
					for(Parameter param1:configurationTO.getQueryParameters()){
						ret.add(new AttributeData(param1.displayName().toString(), AttributeData.Type.STRING,
									AttributeData.IOType.INPUT));
						if(param1.example() != null){retValues.add(param1.example().toString());}
						else{
							retValues.add("Enter value");
						}
						
					}
					/*if(configurationTO.getResponseSchemaString() != null){
						jsonString2Map(configurationTO.getResponseSchemaString(), ret, null, "OUTPUT");
					}*/
					/*configurationTO.getResponseSchemaMap().entrySet().forEach(responseMap -> {
						
						if((!responseMap.getValue().equals("")) && responseMap.getValue()!=null){
							try {
								//System.out.println("responseMap.getValue()"+responseMap.getValue());
								jsonString2Map(responseMap.getValue(), ret, null, "OUTPUT");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				);*/
					
					HashMap<String, String> responseMap = configurationTO.getResponseSchemaMap();
					for (Entry<String, String> entry : responseMap.entrySet()) {
						//String responseCode = entry.getKey();
						String reponseSchemaContent = entry.getValue();						
						//Just run assertions tag creation just once not as many as response code in responseMap
						if(reponseSchemaContent!= null && (!reponseSchemaContent.equals("")) ){
							jsonString2Map(reponseSchemaContent, ret, null, "OUTPUT");
							System.out.println("ret-------->"+ret.size());
						}

					}

				}
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		
		//System.out.println("ret inside getaatributes method just beofre return statement----->"+ret.size()+"\n"+ret);
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
		//ret.addExclusionRestriction(false, "a1.equals(\"v1\") && a2",
		//		"some restrictions");
		return ret;
	}

	@Override
	public List<SortedSet<String>> getValues() {

		// a1 (first attribute) has values "v1", "v2", "v3". Second attribute (a2)
		// is a boolean - no need to supply values for it

		//System.out.println("ret values inside getValues()------>"+retValues.size());
		List<SortedSet<String>> retValue = new ArrayList<SortedSet<String>>();
		SortedSet<String> messageIdSet = new TreeSet<String>();
		for(int k=1; k<=retValues.size();k++){
			
			messageIdSet.add("TC00"+k+"_messageId");			
		}
		retValue.add(messageIdSet);
		
		
		SortedSet<String> traceabilityIdSet = new TreeSet<String>();
		for(int k=1; k<=retValues.size();k++){
			
			traceabilityIdSet.add("TC00"+k+"_traceabilityId");			
		}
		retValue.add(traceabilityIdSet);
		
		SortedSet<String> responseCodeSet = new TreeSet<String>();
		responseCodeSet.add("200");
		retValue.add(responseCodeSet);
		
		for(int r=0;r<retValues.size();r++){
			SortedSet<String> vals1 = new TreeSet<String>();
			vals1.add(retValues.get(r));
			//vals1.add("v2");
			//vals1.add("v3");
			retValue.add(vals1);
			/*SortedSet<String> vals2 = new TreeSet<String>();
		      retValue.add(vals2);*/
		}
		return retValue;
	}

	@Override
	public Map<String, Set<String>> getValuesDependencies(int arg0) {

		// No value dependencies in our example

		return null;
	}

	@Override
	public boolean isInputOutput() {

		// The model should not be an input-output model

		return true;
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
		return null;
	}

	@Override
	public List<Integer> getCoverageRequirementsInteractionLevels() {
		ArrayList<Integer> levels = new ArrayList<Integer>();
		levels.add(1);
		return levels;
	}

	@Override
	public List<List<String>> getCoverageRequirementsAttributesNames() {
		List<List<String>> attrNamesLists = new ArrayList<List<String>>();
		List<String> allAttrNames = new ArrayList<String>();
		for (AttributeData attr : getAttributes()) {
			System.out.println(attr.name+"\n");
			allAttrNames.add(attr.name);
		}
		attrNamesLists.add(allAttrNames);
		return attrNamesLists;
	}

	@Override
	public Map<String, List<Integer>> getAttributesValuesWeights() {
		return Collections.emptyMap();
	}




	public ArrayList<AttributeData> jsonString2Map(String jsonString, ArrayList<AttributeData> ret,String prependKey, String ioType) throws JSONException, IOException {
		LinkedHashMap<String, Object> keys = new LinkedHashMap<String, Object>();

		JSONObject json = new JSONObject();
		ObjectMapper mapper = new ObjectMapper();
		//System.out.print("\n Incoming json string for reference : " + jsonString);
		TypeReference<LinkedHashMap<String, Object>> typeRef = new TypeReference<LinkedHashMap<String,Object>>() {};

		LinkedHashMap<String,?> o = mapper.readValue(jsonString, typeRef); 
		List<String> sortKey = new ArrayList<String>();
		int totalSize = o.size();

		for (String key : o.keySet()){

			String actualKey=key;
			if(null!= prependKey){
				actualKey=prependKey+"_"+key;
			}
			//String key = (String) keyset.next();
			Object value = o.get(key);
			if (value instanceof LinkedHashMap) {
				//System.out.println("Incomin value is of JSONObject : "+value);
				ObjectMapper objectMapper = new ObjectMapper();
				//Set pretty printing of json
				objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
				String mapToJson = objectMapper.writeValueAsString(value);
				keys.put(key,	jsonString2Map(	mapToJson, ret, actualKey, ioType));
			} else if (value instanceof ArrayList) {
				//System.out.println("Incomin value is of JSONArray : "+value);
				ObjectMapper objectMapper = new ObjectMapper();
				//Set pretty printing of json
				objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
				String mapToJson = objectMapper.writeValueAsString(value);
				//System.out.println(mapToJson);
				if(((ArrayList) value).size() >0) {keys.put(key,	jsonArray2List(mapToJson, ret, actualKey,ioType));}
			} else {
				AttributeData a1 = null;
				//AttributeData value1 = null;
				if(ioType.equals("INPUT")){
					a1 = new AttributeData( actualKey+"", AttributeData.Type.STRING,
							AttributeData.IOType.INPUT);
					ret.add(a1);
					retValues.add(value+"");
					
					
				}else {
					if(uniqueAttributesOutput != null){
						if(!uniqueAttributesOutput.contains(actualKey)){							
							uniqueAttributesOutput.add(actualKey);
							a1 = new AttributeData( actualKey+"", AttributeData.Type.STRING,
									AttributeData.IOType.OUTPUT);
							System.out.println("actualKeyactualKeyactualKeyactualKey"+actualKey+"///"+ioType+"//"+ret.size());
							ret.add(a1);
							retValues.add(value+"");
						}
					}else{
						a1 = new AttributeData( actualKey+"", AttributeData.Type.STRING,
								AttributeData.IOType.OUTPUT);
						ret.add(a1);
						retValues.add(value+"");
						
					}
				}
				//System.out.println("retretretretretretret===>"+actualKey+"\n");				
				keys.put( key, value );
			}
		}
		return ret;
	}

	public List<Object> jsonArray2List(String arrayOFKeys, ArrayList<AttributeData> ret, String actualKey, String ioType) throws JSONException,
	IOException {
		//System.out.println("Incoming value is of JSONArray : =========");
		ObjectMapper mapper = new ObjectMapper();
		//mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		//jsonString = jsonString.replace("'", "\\\\u0027");
		//System.out.print("\n Incoming json string for reference : " + arrayOFKeys);
		TypeReference<ArrayList<?>> typeRef  = new TypeReference<ArrayList<?>>() {};        
		ArrayList<?> arrayOFKeysList = mapper.readValue(arrayOFKeys, typeRef); 


		List<Object> array2List = new ArrayList<Object>();
		int arraySize;
		if(arrayOFKeysList.size() ==1){
			arraySize = arrayOFKeysList.size();
		}else{
			arraySize = arrayOFKeysList.size()-1;
		}

		/** always send one item from Array 
		 * Not all items, therefore sending arraySize as 1
		 */
		arraySize = 1;
		for (int i = 0; i < arraySize; i++) {
			if (arrayOFKeysList.get(i) instanceof LinkedHashMap) {
				//System.out.println("Incomin value is of JSONObject : "+arrayOFKeysList.get(i).toString());
				ObjectMapper objectMapper = new ObjectMapper();
				//Set pretty printing of json
				objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
				String mapToJson = objectMapper.writeValueAsString(arrayOFKeysList.get(i));
				//System.out.println(mapToJson);
				ArrayList<AttributeData> subObj2Map = jsonString2Map( mapToJson, ret, actualKey, ioType);
				array2List.add(subObj2Map);
			} else if (arrayOFKeysList.get(i) instanceof ArrayList) {

				//System.out.println("Incomin value is of JSONObject : "+arrayOFKeysList.get(i).toString());
				ObjectMapper objectMapper = new ObjectMapper();
				//Set pretty printing of json
				objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
				String mapToJson = objectMapper.writeValueAsString(arrayOFKeysList.get(i));
				//System.out.println(mapToJson);

				List<Object> subarray2List = jsonArray2List(mapToJson, ret, actualKey, ioType);
				array2List.add(subarray2List);
			} else {
				// keyNode( arrayOFKeys.opt(i) );
				array2List.add(arrayOFKeysList.get(i));
			}
		}
		return array2List;
	}


	/*	public void main(String[] args){
		ParasoftImporter pea = new ParasoftImporter();
		System.out.println(pea.getAttributes());
	}*/

	public ConfigurationTO copyRAMLDataToDTO(Resource urlEndPointsSubNode, int index) throws Exception {

		ConfigurationTO configurationTO = new ConfigurationTO();
		// set the response schema content
		AtomicInteger j = new AtomicInteger();
		/**load input parameters - START*/
		if(urlEndPointsSubNode.methods().get(index).body().size() >0){					
			configurationTO.setInputSampleString(JsonGeneratorFromSchema.generateInputSampleString(urlEndPointsSubNode
					.methods().get(index).body().get(0).schemaContent(),Util.ramlFilePathWithOutFileName(ramlfilename.getAbsolutePath())+"\\jsd\\"
							+urlEndPointsSubNode.methods().get(index).body().get(0).schema().value().toString()+".1.schema.json" ).toString());
			/*System.out.println("output samples"+urlEndPointsSubNode
					.methods().get(index).body().get(0).example().value().toString());*/
			/*jsonString2Map(urlEndPointsSubNode
					.methods().get(index).body().get(0).example().value().toString(), ret1, null, "INPUT");*/

		}
		
		/**load input parameters - END*/
		
		/** load output parameters -START*/
		HashMap<String, String> responseSchemaMap = new HashMap<String, String>();
		//urlEndPointsSubNode.methods().get(0).
		List<Response> res1 = urlEndPointsSubNode.methods().get(index).responses();
		//System.out.println("res1.size()"+res1.size());
		
		boolean responseLoaded = false;
		for(int k=0; k<res1.size(); k++){
			//response.get(i);
			Response response = res1.get(k);
			//System.out.println("response.body() "+response.body() );
			if(response.body() != null && (!response.body().isEmpty())){
				if( (response.body().get(0).schemaContent() != null) ){
				//System.out.println(response.body().get(0).schemaContent());
				try {
					//System.out.println(response.body().get(0).schemaContent().toString());
					//configurationTO.setResponseSchemaString(JsonGeneratorFromSchema.generateInputSampleString(response.body().get(k).schemaContent().toString()).toString());
					//responseLoaded = true;
					responseSchemaMap.put(response.code().value(), JsonGeneratorFromSchema.generateInputSampleString(response.body().get(0).schemaContent().toString(), 
							Util.ramlFilePathWithOutFileName(ramlfilename.getAbsolutePath())+"\\jsd\\"+response.body().get(0).schema().value().toString()+".1.schema.json" ).toString());
					//jsonString2Map(response.body().get(0).example().value().toString(), ret1, null, "OUTPUT");
					//configurationTO.setResponseSchemaString(JsonGeneratorFromSchema.generateInputSampleString(response.body().get(0).schemaContent()).toString());
				} catch (Exception e) {
					System.out.println("Error while reading response body schema content"+e.getMessage());
					e.printStackTrace();
				}
			}
			}else{
				System.out.println(response.description().value());
				responseSchemaMap.put(response.code().value(), "");
			}

		}
		/*responseSchemaMap = (HashMap<String, String>) createMap(responseSchemaMap);
		System.out.println("responseSchemaMap->"+responseSchemaMap);*/
		configurationTO.setResponseSchemaMap(responseSchemaMap);
		/** load output parameters -END*/
		
		return configurationTO;
	}

}

