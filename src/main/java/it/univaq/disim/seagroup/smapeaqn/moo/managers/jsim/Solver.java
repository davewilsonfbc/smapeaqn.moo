package it.univaq.disim.seagroup.smapeaqn.moo.managers.jsim;

import java.io.File;
import java.io.IOException;
import java.util.List;

import it.univaq.disim.seagroup.smapeaqn.moo.util.FurtherSimulationManager;

public class Solver {
	////
	public static boolean exitedBeforeTimeout = true;
	////
	/**
	 * The main method
	 * @param args arguments
	 */
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("JSIMSolver Solves a Queueing Network model using JSIM simulator");
			System.out.println();
			System.out.println("Usage: JSIMSolver modelfile [seed]");
			System.out.println("  modelfile  : name of the input XML file describing the model");
			System.out.println("  [seed]     : optional simulation seed. An integer number");
			System.exit(0);
		}
		File model = new File(args[0]);
		if (!model.isFile()) {
			System.err.print("Invalid model file: " + model.getAbsolutePath());
			System.exit(1);
		}

		Dispatcher dispatcher = new Dispatcher(model);
		/////
		dispatcher.setTerminalSimulation(true);
		/////
		// Sets simulation seed if required
		if (args.length > 1) {
			try {
				dispatcher.setSimulationSeed(Long.parseLong(args[1]));
			} catch (NumberFormatException ex) {
				System.err.println("Invalid simulation seed");
				System.exit(1);
			}
		}

		// Starts the simulation
		//dispatcher.solveModel();
		////
		exitedBeforeTimeout = dispatcher.solveModel();
		////
		File output = dispatcher.getOutputFile();
		System.out.println("Output file stored in path: " + output.getAbsolutePath());
	}
	
	/*
	/// MODIFIED (FOR ENHANCED SAS, i.e. Monitor, Analyze, Normal/Alert/Critical Plan, Normal/Alert/Critical Execute)
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("JSIMSolver Solves a Queueing Network model using JSIM simulator");
			System.out.println();
			System.out.println("Usage: JSIMSolver modelfile [seed]");
			System.out.println("  modelfile  : name of the input XML file describing the model");
			System.out.println("  [seed]     : optional simulation seed. An integer number");
			System.exit(0);
		} 
		File model = new File(args[0]);
		if (!model.isFile()) {
			System.err.print("Invalid model file: " + model.getAbsolutePath());
			System.exit(1);
		}
		
		/////////////////////////////////////////////////////////////////////////////////////////////// DA QUI
		if(args.length > 1) { //assume args[1] specifies csv file with CSPs to simulate
			File cspFile = new File(args[1]);
			if (!cspFile.isFile()) {
				System.err.print("Invalid .csv file containing CSPs to be simulated: " + cspFile.getAbsolutePath());
				System.exit(1);
			}
			//Read csv file and, while going line by line:
			// col 3 contains workload intensity
			// from col 5 to line-end there are S->M routing probabilities
			CsvManager csv_manager = new CsvManager(cspFile.getAbsolutePath());
			List<String[]> arrayList;
			try {
				arrayList = csv_manager.readCspsCsv(true);
				for (String[] listEl : arrayList) {
					for(String el : listEl) {
						System.out.print(el + "\t");
					}
					System.out.println();
		        }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		//////////////////////////////////////////////////////////////////////////////////////////////////////////

		Dispatcher dispatcher = new Dispatcher(model);
		dispatcher.setTerminalSimulation(true);
		
		// Sets simulation seed if required
		if (args.length == 3) {
			try {
				dispatcher.setSimulationSeed(Long.parseLong(args[2]));
			} catch (NumberFormatException ex) {
				System.err.println("Invalid simulation seed");
				System.exit(1);
			}
		}

		// Starts the simulation
		//dispatcher.solveModel();
		////
		exitedBeforeTimeout = dispatcher.solveModel();
		////
		File output = dispatcher.getOutputFile();
		System.out.println("Output file stored in path: " + output.getAbsolutePath());
	}*/

}