package com.ibm.focus.importers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.raml.v2.api.model.v08.parameters.Parameter;

public class ConfigurationTO {

	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getEndPointUrl() {
		return endPointUrl;
	}
	public void setEndPointUrl(String endPointUrl) {
		this.endPointUrl = endPointUrl;
	}
	public ArrayList getHeadersList() {
		return headersList;
	}
	public void setHeadersList(ArrayList headersList) {
		this.headersList = headersList;
	}
	public String getInputSampleString() {
		return inputSampleString;
	}
	public void setInputSampleString(String inputSampleString) {
		this.inputSampleString = inputSampleString;
	}
	public ArrayList getUrlParametersList() {
		return urlParametersList;
	}
	public void setUrlParametersList(ArrayList urlParametersList) {
		this.urlParametersList = urlParametersList;
	}
	public String method;
	public HashMap<String, String> responseSchemaMap;

	public String endPointUrl;
	public ArrayList headersList;
	public String inputSampleString;
	public ArrayList urlParametersList;
	public String securedBy;
	public String schemaContentInputParam;
	List<String> dataSource;
	String dataSourcePath;
	String testPathUrl;
	String validRange;
	String ramlFileName;
	String profileMappingID;
	String appConfigPath;
	String inputTstFile;
	List<Parameter> queryParameters;
	String responseSchemaString;
	
	public String getResponseSchemaString() {
		return responseSchemaString;
	}
	public void setResponseSchemaString(String responseSchemaString) {
		this.responseSchemaString = responseSchemaString;
	}
	public List<Parameter> getQueryParameters() {
		return queryParameters;
	}
	public void setQueryParameters(List<Parameter> queryParameters) {
		this.queryParameters = queryParameters;
	}
	public String getInputTstFile() {
		return inputTstFile;
	}
	public void setInputTstFile(String inputTstFile) {
		this.inputTstFile = inputTstFile;
	}
	public String getAppConfigPath() {
		return appConfigPath;
	}
	public void setAppConfigPath(String appConfigPath) {
		this.appConfigPath = appConfigPath;
	}
	public String getProfileMappingID() {
		return profileMappingID;
	}
	public void setProfileMappingID(String profileMappingID) {
		this.profileMappingID = profileMappingID;
	}
	public String getRamlFileName() {
		return ramlFileName;
	}
	public void setRamlFileName(String ramlFileName) {
		this.ramlFileName = ramlFileName;
	}
	String authorization;
	public String getAuthorization() {
		return authorization;
	}
	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getTraceabilityId() {
		return traceabilityId;
	}
	public void setTraceabilityId(String traceabilityId) {
		this.traceabilityId = traceabilityId;
	}
	String messageId;
	String traceabilityId;
	
	public String getValidRange() {
		return validRange;
	}
	public void setValidRange(String validRange) {
		this.validRange = validRange;
	}
	public String getTestPathUrl() {
		return testPathUrl;
	}
	public void setTestPathUrl(String testPathUrl) {
		this.testPathUrl = testPathUrl;
	}
	public String getDataSourcePath() {
		return dataSourcePath;
	}
	public void setDataSourcePath(String dataSourcePath) {
		this.dataSourcePath = dataSourcePath;
	}
	public List<String> getDataSource() {
		return dataSource;
	}
	public void setDataSource(List<String> dataSource) {
		this.dataSource = dataSource;
	}
	public String getSchemaContentInputParam() {
		return schemaContentInputParam;
	}
	public void setSchemaContentInputParam(String schemaContentInputParam) {
		this.schemaContentInputParam = schemaContentInputParam;
	}
	public String getSecuredBy() {
		return securedBy;
	}
	public void setSecuredBy(String securedBy) {
		this.securedBy = securedBy;
	}
	public void setResponseSchemaMap(HashMap<String, String> responseSchemaMap) {
		this.responseSchemaMap = responseSchemaMap;
		
	}
	public HashMap<String, String> getResponseSchemaMap() {
		return responseSchemaMap;
	}
	
}
