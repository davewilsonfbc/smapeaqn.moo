package it.univaq.disim.seagroup.smapeaqn.moo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.univaq.disim.seagroup.smapeaqn.moo.csp.CspSet2;
import it.univaq.disim.seagroup.smapeaqn.moo.csp.ProbabilityBasedCsp;
import it.univaq.disim.seagroup.smapeaqn.moo.managers.CspXmlManager2;
import it.univaq.disim.seagroup.smapeaqn.moo.managers.SimulationManager;

public class FurtherSimulationManager {
	private String basePath;
	private String inputCsvFileName;
	private String outputCsvFileName;
	private String jsimgFileName;
	private String[] headers;
	private List<String[]> data;
	private int numRows;
	private CspSet2[] cspSets;
	
	public FurtherSimulationManager(String basePath, String inputCsvFileName, String outputCsvFileName, String jsimgFileName) {
		if(new File(basePath).isDirectory()) {
			this.basePath = basePath;
			System.out.println(basePath + " is OK!");
		} else {
			System.out.println(basePath + " is not a valid directory!");
			System.exit(1);
		}
		if(new File(basePath + inputCsvFileName).isFile()) {
			this.inputCsvFileName = inputCsvFileName;
			System.out.println(basePath + inputCsvFileName + " is OK!");
		} else {
			System.out.println(basePath + inputCsvFileName + " is not a valid file path!");
			System.exit(1);
		}
		this.outputCsvFileName = outputCsvFileName;
		if(new File(basePath + jsimgFileName).isFile()) {
			this.jsimgFileName = jsimgFileName;
			System.out.println(basePath + jsimgFileName + " is OK!");
		} else {
			System.out.println(basePath + jsimgFileName + " is not a valid file path!");
			System.exit(1);
		}
		init();
	}
	
	/*
	public List<String[]> parseCspsCsv(Boolean preserveHeaders) throws IOException {
		return readCsv(this.inputCspCsvPath, preserveHeaders);
	}*/
	
	public void parseInputCsv(Boolean preserveHeaders) throws IOException {
		readCsv(getBasePath()+getInputCsvFileName(), preserveHeaders);
	}
	
	private void init() {
		String[] headers;
		List<String[]> arrayList;
		try {
			this.parseInputCsv(true);
			headers = this.getHeaders();
			for (String h : headers) {
				System.out.println("HEADER: " + h);
			}
			arrayList = this.getData();
			for (String[] listEl : arrayList) {
				System.out.println("DATA ROW: ");
				for(String el : listEl) {
					System.out.print(el + "\t");
				}
				System.out.println();
	        }
			System.out.println("TOTAL ROWS: " + arrayList.size());
			
			
			this.cspSets = new CspSet2[data.size()];
			for(int i = 0; i < this.cspSets.length; i++) {
				this.cspSets[i] = new CspSet2(3);
			}
			for(int i = 0; i < this.cspSets.length; i++) {
				String[] dataRow = this.data.get(i);
				String[] cspRow = new String[dataRow.length-4];
				System.arraycopy(dataRow, 4, cspRow, 0, dataRow.length-4);
				int j = 0;
				while(j < cspRow.length) {
					Double[] rp = new Double[3];
					int k = 0;
					while(k < rp.length) {
						rp[k] = Double.parseDouble(cspRow[j+k]);
						k++;
					}
					int cspIndex = Math.floorDiv(j, 3);
					((ProbabilityBasedCsp) this.cspSets[i].getCsps()[cspIndex]).setRp(rp);
					j = j + 3;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	//private List<String[]> readCsv(String pathToCsv, Boolean preserveHeaders) throws IOException {
	private void readCsv(String pathToCsv, Boolean preserveHeaders) throws IOException {
		List<String[]> res = new ArrayList<>();
		BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));
		String row = null;
		if(!preserveHeaders)
			csvReader.readLine();
		int i = 0;
		while ((row  = csvReader.readLine()) != null) {
		    String[] data = row.split(",");
		    if(i == 0) { //headers
		    	this.headers = data;
		    } else {
		    	res.add(data);
		    }
		    i++;
		}
		csvReader.close();
		this.numRows = i;
		this.data = res;
		//return res;
	}
	
	public String getBasePath() {
		return basePath;
	}
	
	public String getInputCsvFileName() {
		return inputCsvFileName;
	}
	
	public String getOutputCsvFileName() {
		return outputCsvFileName;
	}
	
	public String getJsimgFileName() {
		return jsimgFileName;
	}
	
	public String[] getHeaders() {
		return headers;
	}


	public List<String[]> getData() {
		return data;
	}


	public int getNumRows() {
		return numRows;
	}
	
	public CspSet2[] getCspSets() {
		return this.cspSets;
	}
	
	
	public void simulateCspSets() {
		CspSet2[] csp_sets = this.getCspSets();
		
		for(int i = 0; i < csp_sets.length; i++) {
			CspSet2 currentCspSet = csp_sets[i];
			CspXmlManager2 xml_mgr = new CspXmlManager2(currentCspSet, getBasePath(), getJsimgFileName());
			xml_mgr.applyCspToQn();
			SimulationManager.simulate(getBasePath() + getJsimgFileName());
			double[] fitnessValues = xml_mgr.getFitnessValues();
			Double[] values = new Double[fitnessValues.length];
			for(int j = 0; j < fitnessValues.length; j++) {
				Double value = Double.valueOf(fitnessValues[j]);
				values[j] = value;
			}
			currentCspSet.setFitnessValues(values);
		}
	}
	
	public void printOutputCsvFile() {
		List<String[]> list = new ArrayList<>();
		String[] header = {"NormalActuate", "AlertActuate", "CriticalActuate"};
		list.add(header);
		CspSet2[] csp_sets = this.getCspSets();
		for(int i = 0; i < csp_sets.length; i++) {
			CspSet2 currentCspSet = csp_sets[i];
			Double[] values = currentCspSet.getFitnessValues();
			String[] str = new String[values.length];
		    for(int j = 0; j < values.length; j++) {
		    	str[j] = values[j].toString();
		    }
			list.add(str);
		}
		///////////////
		try {
			FileWriter csvWriter = new FileWriter(getBasePath() + getOutputCsvFileName());
			for(String[] row : list) {
				for(int i = 0; i < row.length; i++) {
					csvWriter.append(row[i]);
					if(i < row.length - 1)
						csvWriter.append(",");
				}
				csvWriter.append("\n");
			}
			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FurtherSimulationManager further_sim_mgr = new FurtherSimulationManager("./JSS-SAAI_SpecialIssue-replication-package-REV1/further_simulations/with_duplicates/",
																				"further_simulations_input_1.0sampleseccsv", 
																				"further_simulations_output_1.0samplesec.csv", 
																				"SMAPEA-QN-emergency-handling.jsimg");
		further_sim_mgr.simulateCspSets();
		further_sim_mgr.printOutputCsvFile();
		/*
		String[] headers;
		List<String[]> arrayList;
		try {
			//arrayList = csv_manager.parseCspsCsv(true);
			csv_manager.parseCspsCsv(true);
			headers = csv_manager.getHeaders();
			for (String h : headers) {
				System.out.println("HEADER: " + h);
			}
			arrayList = csv_manager.getData();
			for (String[] listEl : arrayList) {
				System.out.println("DATA ROW: ");
				for(String el : listEl) {
					System.out.print(el + "\t");
				}
				System.out.println();
	        }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
