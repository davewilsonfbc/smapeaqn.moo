package it.univaq.disim.seagroup.smapeaqn.moo.problem.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import it.univaq.disim.seagroup.smapeaqn.moo.csp.Csp;
import it.univaq.disim.seagroup.smapeaqn.moo.csp.CspSet;
import it.univaq.disim.seagroup.smapeaqn.moo.managers.CspXmlManager;
import it.univaq.disim.seagroup.smapeaqn.moo.managers.SimulationManager;
import it.univaq.disim.seagroup.smapeaqn.moo.runners.CspSimpleNSGAIIRunner;
import it.univaq.disim.seagroup.smapeaqn.moo.solution.CspSolution;
import it.univaq.disim.seagroup.smapeaqn.moo.solution.impl.ProbabilityBasedCspSolution;

public class CspProblem extends AbstractCspProblem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7639055131178839061L;
	
	private static int solutionsCounter = 0;
	
	////
	public static int numExitedBeforeTimeout = 0;
	////

	public CspProblem() {
		
	}
	
	public CspProblem(String jsimgFilePath, String jsimgFileName, int numberOfModes, int numberOfOutgoings) {
	  setJsimgFilePath(jsimgFilePath);
	  setJsimgFileName(jsimgFileName);
	  setNumberOfModes(numberOfModes);
	  setNumberOfOutgoings(numberOfOutgoings);
      setNumberOfVariables(2 + (2 * numberOfModes));
      setNumberOfObjectives(numberOfModes);
    }

    @Override
    public void evaluate(CspSolution solution) {
    	//Solution CSPs retrieval and application to the QN model
    	Csp[] csps = new Csp[getNumberOfVariables()];
	    for (int i = 0 ; i < getNumberOfVariables(); i++) {
	        csps[i] = solution.getVariableValue(i);
	    }
	    CspSet cspSet = new CspSet(getNumberOfModes());
	    cspSet.setCsps(csps);
	    
	    CspXmlManager cspXmlMan = null;
	    //cspXmlMan = new CspXmlManager(cspSet, getJsimgFilePath(), getJsimgFileName());
	    //cspXmlMan.applyCspToQn();
    	
    	//Simulation
	    //SimulationManager.simulate(getJsimgFilePath()+getJsimgFileName());
	    
	    try {
	    	
	    	//File run = new File(getJsimgFilePath() + "run");
	    	//if(!run.exists())
	    	//	run.mkdir();
	    	//Timestamp ts = new Timestamp(System.currentTimeMillis());
	    	//long t = ts.getTime();
	    	//File f = new File(run.getAbsolutePath() + File.separator + t);
	    	//f.mkdir();
	    	
	    	String runFolder = getJsimgFilePath() + "run_" + CspSimpleNSGAIIRunner.timestamp + File.separator + "solution_" + solutionsCounter + File.separator;
	    	File appFile = new File(runFolder);
	    	appFile.mkdir();
	    	Path src;
	    	Path trg;
	    	Path app;
	    	if(!new File(runFolder + getJsimgFileName()).exists()) {
	    		src = Paths.get(getJsimgFilePath()+getJsimgFileName());
	    		trg = Paths.get(runFolder + getJsimgFileName());
	    		app = Files.copy(src, trg, StandardCopyOption.REPLACE_EXISTING);
	    		cspXmlMan = new CspXmlManager(cspSet, runFolder, getJsimgFileName());
			    cspXmlMan.applyCspToQn();
		    	
		    	//Simulation
			    SimulationManager.simulate(runFolder + getJsimgFileName());
			    
			    ////
			    if(!SimulationManager.exitedBeforeTimeout)
			    	numExitedBeforeTimeout++;
			    ////
	    		solutionsCounter++;
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    //RTs retrieval and Fitness function setting
    	double[] f = cspXmlMan.getFitnessValues();
    	for(int i=0; i<getNumberOfObjectives(); i++) {
    		solution.setObjective(i, f[i]);
    	}
    	
    }
    
    public void deleteModelFiles(File runFolder) {
    	if (runFolder.isDirectory()) {
            File[] list = runFolder.listFiles();
            if (list != null)
                for (int i = 0; i < list.length; i++) {
                    File tmpF = list[i];
                    if (tmpF.isDirectory()) {
                    	deleteModelFiles(tmpF);
                    	tmpF.delete();
                    } else
                    	if (tmpF.isFile())
                    		if(tmpF.getAbsolutePath().endsWith(".jsimg"))
                    			tmpF.delete();
                }
        }
    }
    
    @Override
	  public CspSolution createSolution() {
	    return new ProbabilityBasedCspSolution(this)  ;
	  }
    
    public void initNumberOfVariables() {
    	setNumberOfVariables(2 + (2 * this.getNumberOfModes()));
    }
    
    public void initNumberOfObjectives() {
    	setNumberOfObjectives(getNumberOfModes());
    }
}
