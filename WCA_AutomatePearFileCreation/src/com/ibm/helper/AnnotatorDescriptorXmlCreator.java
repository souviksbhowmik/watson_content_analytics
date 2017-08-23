package com.ibm.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AnnotatorDescriptorXmlCreator {

	ExcelReader excelReader = new ExcelReader();

	public void createAnnotatorDescriptorXml() {

		try {
			
			Properties prop = new Properties();
			InputStream input = new FileInputStream("resources/config.properties");
			prop.load(input);
			
			String filename = prop.getProperty("excelFilePath")+prop.getProperty("excelFileName");
			String sheetName1 = prop.getProperty("sheet1");

			List annotatorType_list = excelReader.getAnnotatorTypeList(filename, sheetName1);
			
			int annotatorType_list_size = annotatorType_list.size();
			
			File xmlFile = new File(prop.getProperty("descriptorFilePath")+prop.getProperty("descriptorFileName"));
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document doc;
			
			if (xmlFile.exists() && !xmlFile.isDirectory()){
				doc = docBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
			}else{
				
				doc = docBuilder.newDocument();
				
				//root element
				Element root = doc.createElementNS("http://uima.apache.org/resourceSpecifier", "analysisEngineDescription");
				doc.appendChild(root);
				
				//frameworkImplementation element
				Element frameworkImplementation = doc.createElement("frameworkImplementation");
				frameworkImplementation.setTextContent("org.apache.uima.java");
				root.appendChild(frameworkImplementation);
				
				//primitive element
				Element primitive = doc.createElement("primitive");
				primitive.setTextContent("true");
				root.appendChild(primitive);
				
				//annotatorImplementationName element
				Element annotatorImplementationName = doc.createElement("annotatorImplementationName");
				annotatorImplementationName.setTextContent("org.apache.uima.annotator."+prop.getProperty("annotatorImplementationName"));
				root.appendChild(annotatorImplementationName);
				
				//analysisEngineMetaData element
				Element analysisEngineMetaData = doc.createElement("analysisEngineMetaData");
				//name element
				Element analysisEngineMetaData_name = doc.createElement("name");
				analysisEngineMetaData_name.setTextContent(prop.getProperty("analysisEngineMetaDataName"));
				analysisEngineMetaData.appendChild(analysisEngineMetaData_name);
				//description element
				Element description = doc.createElement("description");
				analysisEngineMetaData.appendChild(description);
				//version element
				Element version = doc.createElement("version");
				version.setTextContent("1.0");
				analysisEngineMetaData.appendChild(version);
				//vendor element
				Element vendor = doc.createElement("vendor");
				analysisEngineMetaData.appendChild(vendor);
				//configurationParameters element
				Element configurationParameters = doc.createElement("configurationParameters");
				analysisEngineMetaData.appendChild(configurationParameters);
				//configurationParameterSettings element
				Element configurationParameterSettings = doc.createElement("configurationParameterSettings");
				analysisEngineMetaData.appendChild(configurationParameterSettings);
				//typeSystemDescription element
				Element typeSystemDescription = doc.createElement("typeSystemDescription");
				analysisEngineMetaData.appendChild(typeSystemDescription);
				//typePriorities element
				Element typePriorities = doc.createElement("typePriorities");
				analysisEngineMetaData.appendChild(typePriorities);
				//fsIndexCollection element
				Element fsIndexCollection = doc.createElement("fsIndexCollection");
				analysisEngineMetaData.appendChild(fsIndexCollection);
				//capabilities element
				Element capabilities = doc.createElement("capabilities");
					//capability element
					Element capability = doc.createElement("capability");
						//inputs, outputs and languagesSupported elements
						capability.appendChild(doc.createElement("inputs"));
						capability.appendChild(doc.createElement("outputs"));
						capability.appendChild(doc.createElement("languagesSupported"));					
				capabilities.appendChild(capability);
				//operationalProperties element
				Element operationalProperties = doc.createElement("operationalProperties");
					//modifiesCas, multipleDeploymentAllowed and outputsNewCASes elements
					Element modifiesCas = doc.createElement("modifiesCas");
					modifiesCas.setTextContent("true");
					Element multipleDeploymentAllowed = doc.createElement("multipleDeploymentAllowed");
					multipleDeploymentAllowed.setTextContent("true");
					Element outputsNewCASes = doc.createElement("outputsNewCASes");
					outputsNewCASes.setTextContent("false");					
				
				operationalProperties.appendChild(modifiesCas);
				operationalProperties.appendChild(multipleDeploymentAllowed);
				operationalProperties.appendChild(outputsNewCASes);
				
				analysisEngineMetaData.appendChild(operationalProperties);
				analysisEngineMetaData.appendChild(capabilities);
				root.appendChild(analysisEngineMetaData);
				
				
			}
			
			Node annotatorImplementationName = doc.getElementsByTagName("annotatorImplementationName").item(0);
			annotatorImplementationName.setTextContent("org.apache.uima.annotator."+prop.getProperty("annotatorImplementationName"));

			NodeList typeSystemDescriptionNodeList = doc.getElementsByTagName("typeSystemDescription");
			Node typeSystemDescription = typeSystemDescriptionNodeList.item(0);

			Element types = doc.createElement("types");
			
			//Create typeDescription elements for sheet1
			for(int i=0; i<annotatorType_list_size; i++){

				Element typeDescription = createTypeDescription(doc, annotatorType_list, i);
				types.appendChild(typeDescription);
				
			}

			typeSystemDescription.appendChild(types);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(prop.getProperty("descriptorFilePath")+prop.getProperty("descriptorFileName")));
			transformer.transform(source, result);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Element createTypeDescription(Document doc, List annotatorType_list, int i){
		
		Element typeDescription = doc.createElement("typeDescription");

		Element typeDescription_name = doc.createElement("name");
	
		typeDescription_name.appendChild(doc.createTextNode("org.apache.uima." + annotatorType_list.get(i)));
		Element description = doc.createElement("description");
		Element supertypeName = doc.createElement("supertypeName");
		supertypeName.setTextContent("uima.tcas.Annotation");

		typeDescription.appendChild(typeDescription_name);
		typeDescription.appendChild(description);
		typeDescription.appendChild(supertypeName);
		
		return typeDescription;

	}

	public static void main(String args[]) {

		AnnotatorDescriptorXmlCreator anotatorDescXmlCreator = new AnnotatorDescriptorXmlCreator();
		anotatorDescXmlCreator.createAnnotatorDescriptorXml();

	}

}
 