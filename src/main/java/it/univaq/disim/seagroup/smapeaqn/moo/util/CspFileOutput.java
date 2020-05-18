package it.univaq.disim.seagroup.smapeaqn.moo.util;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.fileoutput.FileOutputContext;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import it.univaq.disim.seagroup.smapeaqn.moo.runners.CspSimpleNSGAIIRunner;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;


public class CspFileOutput {

	private FileOutputContext parFileContext;
	private FileOutputContext varFileContext;
	private FileOutputContext funFileContext;
	private String parFileName = "PARAMETERS";
	private String varFileName = "VARIABLES";
	private String funFileName = "FITNESS";
	private String separator = "\t";
	private AbstractAlgorithmRunner runner;
	private List<? extends Solution<?>> solutionList;
	private List<Boolean> isObjectiveToBeMinimized ;
	
	
	public CspFileOutput(AbstractAlgorithmRunner runner, List<? extends Solution<?>> solutionList) {
		parFileContext = new DefaultFileOutputContext(parFileName);
	    varFileContext = new DefaultFileOutputContext(varFileName);
	    funFileContext = new DefaultFileOutputContext(funFileName);
	    parFileContext.setSeparator(separator);
	    varFileContext.setSeparator(separator);
	    funFileContext.setSeparator(separator);
	    this.runner = runner;
	    this.solutionList = solutionList;
	    isObjectiveToBeMinimized = null ;
	}
	
	
	
	
	public CspFileOutput setParFileOutputContext(FileOutputContext fileContext) {
	    parFileContext = fileContext;

	    return this;
	  }
	
	public CspFileOutput setVarFileOutputContext(FileOutputContext fileContext) {
	    varFileContext = fileContext;

	    return this;
	  }

	  public CspFileOutput setFunFileOutputContext(FileOutputContext fileContext) {
	    funFileContext = fileContext;

	    return this;
	  }

	  public CspFileOutput setObjectiveMinimizingObjectiveList(List<Boolean> isObjectiveToBeMinimized) {
	    this.isObjectiveToBeMinimized = isObjectiveToBeMinimized ;

	    return this;
	  }

	  public CspFileOutput setSeparator(String separator) {
	    this.separator = separator;
	    varFileContext.setSeparator(this.separator);
	    funFileContext.setSeparator(this.separator);

	    return this;
	  }
	  
	  
	  public void print() {
		printAlgorithmParametersToFile(parFileContext, runner);
	    if (isObjectiveToBeMinimized == null) {
	      printObjectivesToFile(funFileContext, solutionList);
	    } else {
	      printObjectivesToFile(funFileContext, solutionList, isObjectiveToBeMinimized);
	    }
	    printVariablesToFile(varFileContext, solutionList);
	  }
	  
	  
	  
	  public void printAlgorithmParametersToFile(FileOutputContext context, AbstractAlgorithmRunner runner) {
		    BufferedWriter bufferedWriter = context.getFileWriter();

		    try {
		    	
		    	if(runner instanceof CspSimpleNSGAIIRunner) {
		    		String f = ((CspSimpleNSGAIIRunner) runner).getJsimgFileName();
		    		int m = ((CspSimpleNSGAIIRunner) runner).getNumberOfModes();
		    		int o = ((CspSimpleNSGAIIRunner) runner).getNumberOfOutgoings();
		    		double xp = ((CspSimpleNSGAIIRunner) runner).getCrossoverProbability();
		    		double mp = ((CspSimpleNSGAIIRunner) runner).getMutationProbability();
		    		int p = ((CspSimpleNSGAIIRunner) runner).getPopulationSize();
		    		int e = ((CspSimpleNSGAIIRunner) runner).getMaxEvaluations();
		    		bufferedWriter.write(f + context.getSeparator() +
		    							 m + context.getSeparator() + o + context.getSeparator() +
		    							xp + context.getSeparator() + mp + context.getSeparator() +
		    							 p + context.getSeparator() + e + context.getSeparator());
		    	}
		      bufferedWriter.close();
		    } catch (IOException e) {
		      throw new JMetalException("Error writing data ", e) ;
		    }

		  }
	  

	  public void printVariablesToFile(FileOutputContext context, List<? extends Solution<?>> solutionList) {
	    BufferedWriter bufferedWriter = context.getFileWriter();

	    try {
	      if (solutionList.size() > 0) {
	        int numberOfVariables = solutionList.get(0).getNumberOfVariables();
	        for (int i = 0; i < solutionList.size(); i++) {
	          for (int j = 0; j < numberOfVariables; j++) {
	            bufferedWriter.write(solutionList.get(i).getVariableValue(j).toString() + context.getSeparator());
	          }
	          bufferedWriter.newLine();
	        }
	      }

	      bufferedWriter.close();
	    } catch (IOException e) {
	      throw new JMetalException("Error writing data ", e) ;
	    }

	  }

	  public void printObjectivesToFile(FileOutputContext context, List<? extends Solution<?>> solutionList) {
	    BufferedWriter bufferedWriter = context.getFileWriter();

	    try {
	      if (solutionList.size() > 0) {
	        int numberOfObjectives = solutionList.get(0).getNumberOfObjectives();
	        for (int i = 0; i < solutionList.size(); i++) {
	          for (int j = 0; j < numberOfObjectives; j++) {
	            bufferedWriter.write(solutionList.get(i).getObjective(j) + context.getSeparator());
	          }
	          bufferedWriter.newLine();
	        }
	      }

	      bufferedWriter.close();
	    } catch (IOException e) {
	      throw new JMetalException("Error printing objecives to file: ", e);
	    }
	  }

	  public void printObjectivesToFile(FileOutputContext context,
	                                    List<? extends Solution<?>> solutionList,
	                                    List<Boolean> minimizeObjective) {
	    BufferedWriter bufferedWriter = context.getFileWriter();

	    try {
	      if (solutionList.size() > 0) {
	        int numberOfObjectives = solutionList.get(0).getNumberOfObjectives();
	        if (numberOfObjectives != minimizeObjective.size()) {
	          throw new JMetalException("The size of list minimizeObjective is not correct: " + minimizeObjective.size()) ;
	        }
	        for (int i = 0; i < solutionList.size(); i++) {
	          for (int j = 0; j < numberOfObjectives; j++) {
	            if (minimizeObjective.get(j)) {
	              bufferedWriter.write(solutionList.get(i).getObjective(j) + context.getSeparator());
	            } else {
	              bufferedWriter.write(-1.0 * solutionList.get(i).getObjective(j) + context.getSeparator());
	            }
	          }
	          bufferedWriter.newLine();
	        }
	      }

	      bufferedWriter.close();
	    } catch (IOException e) {
	      throw new JMetalException("Error printing objecives to file: ", e);
	    }
	  }

	  /*
	   * Wrappers for printing with default configuration
	   */
	  public void printAlgorithmParametersToFile(String fileName) throws IOException {
		  printAlgorithmParametersToFile(new DefaultFileOutputContext(fileName), runner);
		  }
	  public void printObjectivesToFile(String fileName) throws IOException {
	    printObjectivesToFile(new DefaultFileOutputContext(fileName), solutionList);
	  }

	  public void printObjectivesToFile(String fileName, List<Boolean> minimizeObjective) throws IOException {
	    printObjectivesToFile(new DefaultFileOutputContext(fileName), solutionList, minimizeObjective);
	  }

	  public void printVariablesToFile(String fileName) throws IOException {
	    printVariablesToFile(new DefaultFileOutputContext(fileName), solutionList);
	  }
	
	
}
