package it.univaq.disim.seagroup.smapeaqn.moo.problem.impl;

import org.uma.jmetal.problem.impl.AbstractGenericProblem;

import it.univaq.disim.seagroup.smapeaqn.moo.problem.CspProblemInterface;
import it.univaq.disim.seagroup.smapeaqn.moo.solution.CspSolution;
import it.univaq.disim.seagroup.smapeaqn.moo.solution.impl.DefaultCspSolution;

public abstract class AbstractCspProblem extends AbstractGenericProblem<CspSolution> implements CspProblemInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6754773553119685560L;
	
	private String jsimgFilePath;
	private String jsimgFileName;
	
	private int numberOfModes;
	private int numberOfOutgoings;

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

	  public CspSolution createSolution() {
	    return new DefaultCspSolution(this)  ;
	  }
}
