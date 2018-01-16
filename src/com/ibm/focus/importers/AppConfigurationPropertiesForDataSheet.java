package com.ibm.focus.importers;

public class AppConfigurationPropertiesForDataSheet {

	
    public String getEndpointUrl() {
	return endpointUrl;
}

public void setEndpointUrl(String endpointUrl) {
	this.endpointUrl = endpointUrl;
}

public String getDatasheetName() {
	return datasheetName;
}

public void setDatasheetName(String datasheetName) {
	this.datasheetName = datasheetName;
}

	public String endpointUrl;
    public String datasheetName;

    public AppConfigurationPropertiesForDataSheet(String endpointUrl, String datasheetName) {
        this.endpointUrl = endpointUrl;
        this.datasheetName = datasheetName;
    }

}
