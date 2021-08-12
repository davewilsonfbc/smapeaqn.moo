package it.univaq.disim.seagroup.smapeaqn.moo.runners;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import it.univaq.disim.seagroup.smapeaqn.moo.solution.CspSolution;
import it.univaq.disim.seagroup.smapeaqn.moo.util.CspFileOutput;
import it.univaq.disim.seagroup.smapeaqn.moo.operator.RandomCspMutationByRegeneration;
import it.univaq.disim.seagroup.smapeaqn.moo.operator.SinglePointCspCrossover;
import it.univaq.disim.seagroup.smapeaqn.moo.problem.impl.CspProblem;

public class CspSimpleNSGAIIRunner extends AbstractAlgorithmRunner {
	
	private static final String problemName = "it.univaq.disim.seagroup.smapeaqn.moo.problem.impl.CspProblem";
	private static final double DEFAULT_crossoverProbability = 0.9;
	private static final double DEFAULT_mutationProbability = 0.0625;
	private static final String DEFAULT_referenceParetoFront = "./experiments/referenceFronts/CspSimpleNSAGII.pf";
	
	public static final long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
	
	//QN model parameters
	private String jsimgFilePath;
	private String jsimgFileName;
	private int numberOfModes;
	private int numberOfOutgoings;
	
	//Algorithm data structures
	private CspProblem problem;
	private Algorithm<List<CspSolution>> algorithm;
	private CrossoverOperator<CspSolution> crossover;
	private MutationOperator<CspSolution> mutation;
	private SelectionOperator<List<CspSolution>, CspSolution> selection;
	private SolutionListEvaluator<CspSolution> evaluator;
	private NSGAIIBuilder<CspSolution> builder;
	
	//Algorithm configuration parameters
	private double crossoverProbability;
	private double mutationProbability;
	private int populationSize;
	private int maxEvaluations;
	private String referenceParetoFront;
	
	//Algorithm result
	private List<CspSolution> population;
	

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

	public int getNumberOfModes() {
		return numberOfModes;
	}

	public void setNumberOfModes(int numberOfModes) {
		this.numberOfModes = numberOfModes;
	}
	
	public int getNumberOfOutgoings() {
		return numberOfOutgoings;
	}

	public void setNumberOfOutgoings(int numberOfOutgoings) {
		this.numberOfOutgoings = numberOfOutgoings;
	}
	
	public CspProblem getProblem() {
		return problem;
	}

	public void setProblem(CspProblem problem) {
		this.problem = problem;
	}

	public Algorithm<List<CspSolution>> getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(Algorithm<List<CspSolution>> algorithm) {
		this.algorithm = algorithm;
	}

	public CrossoverOperator<CspSolution> getCrossover() {
		return crossover;
	}

	public void setCrossover(CrossoverOperator<CspSolution> crossover) {
		this.crossover = crossover;
	}

	public MutationOperator<CspSolution> getMutation() {
		return mutation;
	}

	public void setMutation(MutationOperator<CspSolution> mutation) {
		this.mutation = mutation;
	}

	public SelectionOperator<List<CspSolution>, CspSolution> getSelection() {
		return selection;
	}

	public void setSelection(SelectionOperator<List<CspSolution>, CspSolution> selection) {
		this.selection = selection;
	}

	public SolutionListEvaluator<CspSolution> getEvaluator() {
		return evaluator;
	}

	public void setEvaluator(SolutionListEvaluator<CspSolution> evaluator) {
		this.evaluator = evaluator;
	}

	public NSGAIIBuilder<CspSolution> getBuilder() {
		return builder;
	}

	public void setBuilder(NSGAIIBuilder<CspSolution> builder) {
		this.builder = builder;
	}

	public double getCrossoverProbability() {
		return crossoverProbability;
	}

	public void setCrossoverProbability(double crossoverProbability) {
		this.crossoverProbability = crossoverProbability;
	}

	public double getMutationProbability() {
		return mutationProbability;
	}

	public void setMutationProbability(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public int getMaxEvaluations() {
		return maxEvaluations;
	}

	public void setMaxEvaluations(int maxEvaluations) {
		this.maxEvaluations = maxEvaluations;
	}
	
	
	public String getReferenceParetoFront() {
		return referenceParetoFront;
	}

	public void setReferenceParetoFront(String referenceParetoFront) {
		this.referenceParetoFront = referenceParetoFront;
	}

	public List<CspSolution> getPopulation() {
		return population;
	}

	public void setPopulation(List<CspSolution> population) {
		this.population = population;
	}

	public CspSimpleNSGAIIRunner( String jsimgFilePath, 
									String jsimgFileName, 
									int numberOfModes, 
									int numberOfOutgoings,
									double crossoverProbability, 
									double mutationProbability, 
									int populationSize, 
									int maxEvaluations, 
									String referenceParetoFront) {
		//Primitive attributes
		this.jsimgFilePath = jsimgFilePath;
		this.jsimgFileName = jsimgFileName;
		this.numberOfModes = numberOfModes;
		this.numberOfOutgoings = numberOfOutgoings;
		this.crossoverProbability = crossoverProbability;
		this.mutationProbability = mutationProbability;
		this.populationSize = populationSize;
		this.maxEvaluations = maxEvaluations;
		if(referenceParetoFront == null)
			this.referenceParetoFront = DEFAULT_referenceParetoFront;
		else
			this.referenceParetoFront = referenceParetoFront;
		//Data structures
		this.problem = (CspProblem) ProblemUtils.<CspSolution>loadProblem(problemName);
		this.problem.setJsimgFilePath(this.jsimgFilePath);
		this.problem.setJsimgFileName(this.jsimgFileName);
		this.problem.setNumberOfModes(this.numberOfModes);
		this.problem.setNumberOfOutgoings(this.numberOfOutgoings);
		this.problem.initNumberOfVariables();
		this.problem.initNumberOfObjectives();
		this.crossover = new SinglePointCspCrossover(this.crossoverProbability);
	    this.mutation = new RandomCspMutationByRegeneration(this.mutationProbability);
	    this.selection = new BinaryTournamentSelection<CspSolution>(
	    		new RankingAndCrowdingDistanceComparator<CspSolution>());
		//this.evaluator = new MultithreadedSolutionListEvaluator<CspSolution>(1, problem);
		this.builder = 
				new NSGAIIBuilder<CspSolution>(this.problem, this.crossover, this.mutation, this.populationSize)
	            													.setSelectionOperator(this.selection)
	            													.setMaxEvaluations(this.maxEvaluations);
	            													//.setSolutionListEvaluator(this.evaluator);
		this.algorithm = builder.build();
	}
	
	
	
	public static void runCspSimpleNSGAII(String jsimgFilePath, 
											String jsimgFileName, 
											int numberOfModes, 
											int numberOfOutgoings,
											double crossoverProbability, 
											double mutationProbability, 
											int populationSize, 
											int maxEvaluations, 
											String referenceParetoFront	) throws IOException {
		CspSimpleNSGAIIRunner runner = new CspSimpleNSGAIIRunner(jsimgFilePath, 
																	 jsimgFileName, 
																	 numberOfModes, 
																	 numberOfOutgoings, 
																	 crossoverProbability, 
																	 mutationProbability, 
																	 populationSize, 
																	 maxEvaluations, 
																	 referenceParetoFront);
		
		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(runner.getAlgorithm()).execute();
		//runner.getBuilder().getSolutionListEvaluator().shutdown();
	    runner.setPopulation(runner.getAlgorithm().getResult());
	    long computingTime = algorithmRunner.getComputingTime();
	    //runner.getEvaluator().shutdown();

	    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
	    runner.printCspOutput(runner.getPopulation());/*
	    if (!runner.getReferenceParetoFront().equals("")) {
	    	File f = new File(runner.getReferenceParetoFront());
	    	f.getParentFile().mkdirs(); 
	    	f.createNewFile();
	      printQualityIndicators(runner.getPopulation(), f.getAbsolutePath());
	      //printQualityIndicators(runner.getPopulation(), runner.getReferenceParetoFront());
	    }*/
	    
	    ////
	    Integer n = CspProblem.numExitedBeforeTimeout;
	    Integer e = runner.getMaxEvaluations();
	    double percentage = (n.doubleValue()/e.doubleValue())*100;
	    System.out.println("Number of simulations exited by timeout: " +
	    					CspProblem.numExitedBeforeTimeout + " out of " + runner.getMaxEvaluations() +
	    					" (" + percentage + "%)");
	    ////
	    
	    runner.getProblem().deleteModelFiles(new File(jsimgFilePath + "run_" + CspSimpleNSGAIIRunner.timestamp));
	    Toolkit.getDefaultToolkit().beep();
	    
	}
	
	
	/**
	   * Write the population into two files and prints some data on screen
	   * @param population
	   */
	  public void printCspOutput(List<? extends Solution<?>> population) {
/*
	    new SolutionListOutput(population)
	        .setSeparator("\t")
	        .setVarFileOutputContext(new DefaultFileOutputContext(getJsimgFilePath() + "run_" + 
	        								CspSimpleNSGAIIRunner.timestamp + "/VAR.tsv"))
	        .setFunFileOutputContext(new DefaultFileOutputContext(getJsimgFilePath() + "run_" +
											CspSimpleNSGAIIRunner.timestamp + "/FUN.tsv"))
	        .print();
*/
		  new CspFileOutput(this, population)
	        .setSeparator("\t")
	        .setParFileOutputContext(new DefaultFileOutputContext(getJsimgFilePath() + "run_" + 
	        								CspSimpleNSGAIIRunner.timestamp + "/PARAMETERS.tsv"))
	        .setVarFileOutputContext(new DefaultFileOutputContext(getJsimgFilePath() + "run_" + 
	        								CspSimpleNSGAIIRunner.timestamp + "/VARIABLES.tsv"))
	        .setFunFileOutputContext(new DefaultFileOutputContext(getJsimgFilePath() + "run_" +
											CspSimpleNSGAIIRunner.timestamp + "/FITNESS.tsv"))
	        .print();
		  
	    JMetalLogger.logger.info("Random seed: " + JMetalRandom.getInstance().getSeed());
	    JMetalLogger.logger.info("Parameters values have been written to file PARAMETERS.tsv");
	    JMetalLogger.logger.info("Objectives values have been written to file FITNESS.tsv");
	    JMetalLogger.logger.info("Variables values have been written to file VARIABLES.tsv");
	  }
	
	  

	/**
   * @param args Expected command line arguments: 
   * 			String jsimgFilePath, String jsimgFileName, int numberOfModes, int numberOfOutgoings, 
   * 			double crossoverProbability, double mutationProbability, int populationSize, int maxEvaluations, 
   * 			String referenceParetoFront
	 * @throws IOException 
   * @throws SecurityException Invoking command: java
   *     org.uma.jmetal.runner.multiobjective.nsgaii.SimpleCspNSGAIIRunner 
   */
  public static void main(String[] args) throws JMetalException, IOException {
	String jsimgFilePath = args[0];
	String jsimgFileName = args[1];
	int numberOfModes = Integer.parseInt(args[2]);
	int numberOfOutgoings = Integer.parseInt(args[3]);
	double crossoverProbability = Double.parseDouble(args[4]);
	double mutationProbability = Double.parseDouble(args[5]);
	int populationSize = Integer.parseInt(args[6]);
	int maxEvaluations = Integer.parseInt(args[7]);
	String referenceParetoFront = null;
	if(args.length == 9)
		referenceParetoFront = args[8];
	
	File run = new File(jsimgFilePath + "run_" + CspSimpleNSGAIIRunner.timestamp);
	if(!run.exists())
		run.mkdir();
	//Timestamp ts = new Timestamp(System.currentTimeMillis());
	//long t = ts.getTime();
	File f = new File(run.getAbsolutePath() + File.separator);
	f.mkdir();
	
	CspSimpleNSGAIIRunner.runCspSimpleNSGAII(jsimgFilePath, jsimgFileName, 
												 numberOfModes, numberOfOutgoings, 
												 crossoverProbability, mutationProbability, 
												 populationSize, maxEvaluations, referenceParetoFront);
  }
}
