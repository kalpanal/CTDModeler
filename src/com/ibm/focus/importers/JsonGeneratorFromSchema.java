package com.ibm.focus.importers;

import io.apptik.json.JsonElement; 
import io.apptik.json.JsonObject;
import io.apptik.json.exception.JsonException;
import io.apptik.json.generator.JsonGenerator;
import io.apptik.json.generator.JsonGeneratorConfig;
import io.apptik.json.modelgen.*;
import io.apptik.json.schema.Schema;
import io.apptik.json.schema.SchemaV4;
 



import io.apptik.json.schema.fetch.SchemaResourceFetcher;
import io.apptik.json.schema.fetch.SchemaUriFetcher;

import java.io.File;
import java.io.IOException; 
import java.net.URI;
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import net.sf.json.JSONSerializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

 
public class JsonGeneratorFromSchema { 
    SchemaV4 schema1; 
 
 /*   public static void main(String[] args) { 
        JsonGeneratorFromSchema generator = new JsonGeneratorFromSchema(); 
        
        System.out.println(generator.generateNoSettings(generator.schema1).toString()); 
        System.out.println("generation with settings"); 
        System.out.println(generator.generateWithSettings(generator.schema1).toString()); 
 
    } */
 
    public JsonGeneratorFromSchema() { 
        try { 
            schema1 =  new SchemaV4().wrap( (JsonObject) JsonElement.readFrom( 
                    "{" + 
                            "\"type\" : \"object\"," + 
                            "\"oneOf\" :  [" + 
                            "{" + 
                            "\"type\" : \"object\"," + 
                            "\"oneOf\" :  [" + 
                            "{\n" + 
 
 
                            "\"type\" : \"object\"," + 
                            "\"properties\" : {" + 
                            "\"one\" : {\"type\" : \"number\"  } ," + 
                            "\"two\" : {\"type\" : \"string\" }," + 
                            "\"three\" : " + "{" + 
                            "\"type\" : \"object\"," + 
                            "\"properties\" : {" + 
                            "\"one\" : {\"type\" : \"number\"  } ," + 
                            "\"two\" : {\"type\" : \"string\" }" + 
                            "}" + 
                            "},"+ 
                            "\"four\" : {\"type\" : \"boolean\"  }," + 
                            "\"five\" : {\"type\" : \"integer\", \"minimum\": 200, \"maximum\":5000 }," + 
                            "\"five1\" : {\"type\" : \"integer\", \"minimum\": 200, \"maximum\":5000 }," + 
                            "\"five2\" : {\"type\" : \"integer\"}," + 
                            "\"six\" : {\"enum\" : [\"one\", 2, 3.5, true, [\"almost empty aray\"], {\"one-item\":\"object\"}, null]  }, " + 
                            "\"seven\" : {\"type\" : \"string\", \"format\": \"uri\" }" + 
                            "}" + 
                            "}]" + 
                            "}]" + 
                            "}")); 
            
           // System.out.println("generation without settings"+new JsonGenerator(schema1, null).generate()); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
    } 
 
/*    private JsonElement generateNoSettings(Schema schema) { 
        return  new Generator(schema, new GeneratorConfig()).generate(); 
    } 
 
    private JsonElement generateWithSettings(Schema schema) { 
        GeneratorConfig gConf = new GeneratorConfig(); 
        ArrayList<String> images =  new ArrayList<String>(); 
        images.add("/photos/image.jpg"); 
        images.add("/photos/image.jpg"); 
 
        gConf.uriPaths.put("seven", images); 
 
        gConf.globalArrayItemsMax = 7; 
 
        gConf.globalIntegerMin = 0; 
        gConf.globalIntegerMax = 100; 
        //can still limit numbers as long as its still valid according to the schema 
        gConf.integerMin.put("five1", 300); 
        gConf.integerMax.put("five1", 400); 
        gConf.skipObjectProperties.add("two"); 
 
        return  new Generator(schema, gConf).generate(); 
    } */
    
	
	public static JsonElement generateInputSampleString(String incomingJsonInputSchema, String schemaFilePath) throws JsonException, IOException, ParseException{
		
		/*SchemaResourceFetcher srf = new SchemaResourceFetcher(); 
		File myFile=new File("C:/Kalpana/TD Bank/RAML files/Doc-Delivery/jsd/AddFTPDeliveryRq.1.schema.json");
        Schema schema = srf.fetch(myFile.toURI(), null,null); */
       // assertEquals(schemaSimple.getJson(), schema.getJson()); 
/*		Gson gson = new GsonBuilder().create();

		Object job = gson.fromJson(incomingJsonInputSchema, Object.class);
		
		JSONObject resobj = new JSONObject(incomingJsonInputSchema);
		Iterator<?> keys = resobj.keys();
		while(keys.hasNext() ) {
		    String key = (String)keys.next();
		    if ( resobj.get(key) instanceof JSONObject ) {
		        JSONObject xx = new JSONObject(resobj.get(key).toString());
		        System.out.println("res1"+xx.getString("required"));
		        //Log.d("res2",xx.getString("something2"));
		    }
		}*/
		/*JsonElement entry=job.getJsonObject("required").getJsonObject("map").getJsonArray("entry");
		String str = entry.toString();
		System.out.println(str);*/
		//URI url = new File("C:/Focus tool/FOCUS-2.14/lib/RAMLfiles/Doc-Delivery/jsd/AddPrintDeliveryWorkstationTerminalRs.1.schema.json").toURI();
		
		
		SchemaV4 schema =  new SchemaV4().wrap((JsonObject) JsonElement.readFrom( incomingJsonInputSchema
			)); 
		File f1 = new File(schemaFilePath);
		System.out.println("schemaFilePath kalpana outside if condition-------->"+schemaFilePath);
		if(f1.exists()){
			System.out.println("schemaFilePath kalpana please make it work -------->"+schemaFilePath);
			URI url = new File(schemaFilePath).toURI();
			schema.setOrigSrc(url);
			//System.out.println("schemaFilePath"+schemaFilePath);
		}/*else{
			schemaFilePath =schemaFilePath.replace("\\jsd", "");	
		}*/
		
		
		
		//System.out.println(schema1.getRequired());
		return generateWithSettings(schema);
		
	}  
	
	 private static JsonElement generateWithSettings(SchemaV4 schema) throws ParseException { 
		 
/*		 URI  schemaUri = URI.create("http://json-schema.org/geo");
		 SchemaUriFetcher suf = new SchemaUriFetcher();

	        System.out.println(schemaUri.getPath());

	        Schema schema2 = suf.fetch(schemaUri, null, null);

	        System.out.println(schema2.toString());*/
	        
		 JsonGeneratorConfig gConf = new JsonGeneratorConfig(); 
	        ArrayList<String> images =  new ArrayList<String>(); 
	       images.add("/photos/image.jpg"); 
	       images.add("/photos/image.jpg"); 
	 
	       gConf.uriPaths.put("seven", images);
	       
	 
	        gConf.globalArrayItemsMax = 2; 
	 
	        gConf.globalIntegerMin = 0; 
	        gConf.globalIntegerMax = 100; 
	        //can still limit numbers as long as its still valid according to the schema 
	        gConf.integerMin.put("five1", 3); 
	        gConf.integerMax.put("five1", 3); 
	        gConf.arrayItemsMin.put("min",1);
	        gConf.arrayItemsMax.put("max", 1);
	        gConf.skipObjectProperties.add("two"); 
	        //schema.getJson()gConf.
	 
	        SchemaResourceFetcher srf = new SchemaResourceFetcher();
	       // Schema schema1 = srf.fetch(URI.create("fetch/simple.json"), null,null);
	        
	       /* final File jsonSchemaFile = new File("C:/KalpanaTAEWorkspace/Kalpana-raml/target/classes/AddPrintDeliveryRq.1.schema.json");
	        final URI uri = jsonSchemaFile.toURI();*/
	        
	       // System.out.println("URIIIIIIIIIIIII-------->"+uri);
	      // new JsonGenerator(schema, gConf).
	       // System.out.println(new JsonGenerator(schema, gConf).generate());
	       // System.out.println(schema.mergeAllRefs().getItems());
	       // schema.setOrigSrc(URI("file:///./jsd/"));
	        JSONObject myjson = new JSONObject(new JsonGenerator(schema, gConf).generate());
	       // parseProfilesJson(new JsonGenerator(schema, gConf).generate().toString());
	        
	        //loopThroughJson(myjson);
	        return  new JsonGenerator(schema, gConf).generate(); 
	    }
	 
	
/*	 public static void printJsonObject(JSONObject jsonObj) {
		// My stored keys and values from the json object
		 
		 


		}
	 static HashMap<String,String> myKeyValues = new HashMap<String,String>();

	 // Used for constructing the path to the key in the json object
	 static Stack<String> key_path = new Stack<String>(); 
	 // Recursive function that goes through a json object and stores 
	 // its key and values in the hashmap 
	 private static void loadJson(JSONObject resobj){
		// JSONObject resobj = new JSONObject(jsonstring);
		 Iterator<?> keys = resobj.keys();
		 while(keys.hasNext() ) {
		     String key = (String)keys.next();
		     if ( resobj.get(key) instanceof JSONObject ) {
		         JSONObject xx = new JSONObject(resobj.get(key).toString());
		         System.out.println("res1"+xx.);
		         System.out.println("res2"+xx.getString("something2"));
		     }
		 }
	 }*/
	 
	   public static void loopThroughJson(Object input) throws JSONException {
	        if (input instanceof JSONObject) {
	            Iterator<?> keys = ((JSONObject) input).keys();
	            while (keys.hasNext()) {
	                String key = (String) keys.next();
	                if (!(((JSONObject) input).get(key) instanceof JSONArray))
	                    System.out.println(key + "=" + ((JSONObject) input).get(key));
	                else
	                    loopThroughJson(new JSONArray(((JSONObject) input).get(key).toString()));
	            }
	        }
	        if (input instanceof JSONArray) {
	            for (int i = 0; i < ((JSONArray) input).length(); i++) {
	                JSONObject a = ((JSONArray) input).getJSONObject(i);
	                Object key = a.keys().next().toString();
	                if (!(a.opt(key.toString()) instanceof JSONArray))
	                    System.out.println(key + "=" + a.opt(key.toString()));
	                else
	                    loopThroughJson(a.opt(key.toString()));
	            }
	        }

	    }
	 public static void parseProfilesJson(String the_json) throws ParseException{
		 
/*		 
		 JSONArray nameArray = (JSONArray) JSONSerializer.toJSON(the_json);
		 System.out.println(((List<JsonElement>) nameArray).size());
		 for(Object js : nameArray)
		 {
		     JSONObject json = (JSONObject) js;
		     System.out.println("File_Name :" +json.get("file_name"));                                         
		 }
	       */try {

	           JSONObject myjson = new JSONObject(the_json);
	           	
	            JSONArray nameArray = myjson.names();
	           //System.out.println( myjson.toMap());
	       /*     JSONArray valArray = myjson.toJSONArray(nameArray);
	            for(int i=0;i<valArray.length();i++)
	            {
	                String p = nameArray.getString(i) + "," + valArray.getString(i);
	               System.out.println("p"+p);
	            } */  
	            
	            
	          /*  Iterator<?> keys = myjson.keys();

	            while( keys.hasNext() ) {
	                String key = (String)keys.next();
	                if ( jObject.get(key) instanceof JSONObject ) {

	                }
	            }
*/

	        } catch (JSONException e) {
	                e.printStackTrace();
	        }
	    }

}