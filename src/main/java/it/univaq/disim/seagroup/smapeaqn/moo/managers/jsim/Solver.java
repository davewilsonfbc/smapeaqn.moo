package it.univaq.disim.seagroup.smapeaqn.moo.managers.jsim;

import java.io.File;

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
		dispatcher.setTerminalSimulation(true);
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

}