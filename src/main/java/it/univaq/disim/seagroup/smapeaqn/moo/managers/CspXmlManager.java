package it.univaq.disim.seagroup.smapeaqn.moo.managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import it.univaq.disim.seagroup.smapeaqn.moo.csp.Csp;
import it.univaq.disim.seagroup.smapeaqn.moo.csp.CspSet;
import it.univaq.disim.seagroup.smapeaqn.moo.csp.ProbabilityBasedCsp;

public class CspXmlManager {

	protected CspSet cspSet;
	protected String jsimgFilePath;
	protected String jsimgFileName;
	
	public CspXmlManager(CspSet cspSet, String jsimgFilePath, String jsimgFileName) {
		this.cspSet = cspSet;
		this.jsimgFilePath = jsimgFilePath;
		this.jsimgFileName = jsimgFileName;
	}
	
	
	private boolean jsimgConformsXml() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(this.jsimgFilePath+this.jsimgFileName));
		String content = scanner.useDelimiter("\\Z").next();
		scanner.close();
		//String withoutLastCharacters = content.substring(0,content.indexOf("</archive>")+10);
		String lastString = content.substring(content.length()-10, content.length());
		if(!lastString.equals("</archive>"))
			return false;
		return true;
	}
	
	private void makeJsimgConformToXml() throws FileNotFoundException {
		File file = new File(this.jsimgFilePath+this.jsimgFileName);
		Scanner scanner = new Scanner(file);
		String content = scanner.useDelimiter("\\Z").next();
		String newContent = content.replaceAll("</archive></archive>", "</archive>");
		newContent = content.replaceAll("</archive>/archive>", "</archive>");
		newContent = content.replaceAll("</archive>archive>", "</archive>");
		newContent = content.replaceAll("</archive>rchive>", "</archive>");
		newContent = content.replaceAll("</archive>chive>", "</archive>");
		newContent = content.replaceAll("</archive>hive>", "</archive>");
		newContent = content.replaceAll("</archive>ive>", "</archive>");
		newContent = content.replaceAll("</archive>ve>", "</archive>");
		newContent = content.replaceAll("</archive>e>", "</archive>");
		newContent = content.replaceAll("</archive>>", "</archive>");
		//String lastString = newContent.substring(newContent.length()-10, newContent.length());
		scanner.close();
		
		FileOutputStream fop = null;

		try {
			fop = new FileOutputStream(file);
			// if file doesnt exists, then create it
			if (!file.exists())
				file.createNewFile();
			// get the content in bytes
			byte[] contentInBytes = newContent.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//FileWriter fstream = new FileWriter(new File(this.jsimgFilePath+this.jsimgFileName),false);
        //BufferedWriter out = new BufferedWriter(fstream);
        //out.write(s);
        //out.close();
		
		
		
	}
	

	public void applyCspToQn() {
		// TODO Auto-generated method stub
		try {
			if(!jsimgConformsXml())
				makeJsimgConformToXml();
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		    Document doc = docBuilder.parse(this.jsimgFilePath+this.jsimgFileName);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("//node[@name=\"S->M\"]/section[@className=\"Router\"]/parameter");
			Node parameter = ((NodeList) expr.evaluate(doc, XPathConstants.NODESET)).item(0);
			NodeList parameterChildren = parameter.getChildNodes();
			if (parameterChildren != null)
				if (parameterChildren.getLength() > 0) {
					int cspIndex=0;
					for(int i=0; i<parameterChildren.getLength(); i++) {
						if(parameterChildren.item(i).hasChildNodes()) {
							String refClass = parameterChildren.item(i).getFirstChild().getNodeValue().toString();
							if(refClass.endsWith("Monitor") || refClass.endsWith("Analyze") || refClass.endsWith("Plan") || refClass.endsWith("Execute")) {
								System.out.println("MAPE CLASS " + refClass + ":");
								Csp csp = this.cspSet.getCsps()[cspIndex];
								csp.apply(parameterChildren.item(i+2));
								cspIndex++;
								System.out.println("------------------------------");
							}
						}
					}
				}
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			// StreamResult result = new StreamResult(new File(jmvaFilePath.substring(0,
			// jmvaFilePath.length() - 5) + ".xmi"));
			StreamResult result = new StreamResult(new File(this.jsimgFilePath)+File.separator+this.jsimgFileName);
			transformer.transform(source, result);
			if(!jsimgConformsXml())
				makeJsimgConformToXml();
			System.out.println("Done");
			
		} catch (DOMException dome) {
			dome.printStackTrace();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		} catch (XPathExpressionException xpee) {
			// TODO Auto-generated catch block
			xpee.printStackTrace();
		} catch (TransformerConfigurationException tce) {
			// TODO Auto-generated catch block
			tce.printStackTrace();
		} catch (TransformerException te) {
			// TODO Auto-generated catch block
			te.printStackTrace();
		}
	}
	
	
	
	public double[] getFitnessValues() {
		ArrayList<Double> fitnessValues = new ArrayList<Double>();
		double[] res = null;
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(getJsimgFilePath() +  "res_sim_" + getJsimgFileName());
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("//solutions[@modelName=\"" + getJsimgFileName() 
									+ "\"]/measure[@measureType=\"Response Time per Sink\" and "
									+ "substring(@class, string-length(@class)- string-length(\"Actuate\") +1)]");
											//	+ "ends-with(@class, \"Actuate\"]");
			NodeList measures = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for(int i=0; i<measures.getLength(); i++) {
				NamedNodeMap measureAttrs = measures.item(i).getAttributes();
				if (measureAttrs.getNamedItem("meanValue") != null) 
					fitnessValues.add(Double.parseDouble(measureAttrs.getNamedItem("meanValue").getNodeValue()));
			}
			
			res = new double[fitnessValues.size()];
			for(int i=0; i<res.length; i++) {
				res[i] = BigDecimal.valueOf(fitnessValues.get(i).doubleValue())
					    .setScale(3, RoundingMode.HALF_UP)
					    .doubleValue();
				//res[i] = fitnessValues.get(i).doubleValue();
			}
			
			
		} catch (ParserConfigurationException pce) {
			// TODO Auto-generated catch block
			pce.printStackTrace();
		} catch (SAXException saxe) {
			// TODO Auto-generated catch block
			saxe.printStackTrace();
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		} catch (XPathExpressionException xpee) {
			// TODO Auto-generated catch block
			xpee.printStackTrace();
		}
		
		
		return res;
	}
	
	
	public CspSet getCspSet() {
		return cspSet;
	}

	public void setCspSet(CspSet cspSet) {
		this.cspSet = cspSet;
	}

	public String getJsimgFilePath() {
		return jsimgFilePath;
	}

	public void setJsimgFilePath(String jsimgFilePath) {
		this.jsimgFilePath = jsimgFilePath;
	}
	
	
	
	
	public String getJsimgFileName() {
		return jsimgFileName;
	}

	public void setJsimgFileName(String jsimgFileName) {
		this.jsimgFileName = jsimgFileName;
	}

	/* TEST */
	public static void main(String[] args) {
		String jsimgFilePath = "./resources/";
		String jsimgFileName = "SMAPEA-QN-emergency-handling.jsimg";
		CspSet cspSet = new CspSet(2);
		Csp[] csps = new Csp[cspSet.getNumVariables()];
		for(int i=0; i<csps.length; i++) {
			csps[i] = new ProbabilityBasedCsp(3);
			csps[i].generate();
		}
		cspSet.setCsps(csps);
		//cspSet.initializeCsps();
		
		CspXmlManager man = new CspXmlManager(cspSet, jsimgFilePath, jsimgFileName);
		man.applyCspToQn();
	}

}
