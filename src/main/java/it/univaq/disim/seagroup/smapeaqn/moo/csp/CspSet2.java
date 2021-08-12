package it.univaq.disim.seagroup.smapeaqn.moo.csp;

public class CspSet2 {
	
	protected int numModes;
	protected int numVariables;
	
	protected Csp[] csps;
	protected Double[] fitnessValues;
	
	public CspSet2(int numberOfModes) {
		this.numModes = numberOfModes;
		this.numVariables = 2 + (2 * numberOfModes);
		this.csps = new Csp[numVariables];
		for(int i=0; i<this.csps.length; i++) {
			this.csps[i] = new ProbabilityBasedCsp(3);
		}
	}
	
	
	public void initializeCsps() {
		for(int i=0; i<this.csps.length; i++) {
			this.csps[i].generate();
		}
	}
	public int getNumModes() {
		return numModes;
	}

	public void setNumModes(int numModes) {
		this.numModes = numModes;
	}

	public int getNumVariables() {
		return numVariables;
	}

	public void setNumVariables(int numVariables) {
		this.numVariables = numVariables;
	}

	public Csp[] getCsps() {
		return csps;
	}

	public void setCsps(Csp[] csps) {
		this.csps = csps;
	}

	public Double[] getFitnessValues() {
		return fitnessValues;
	}

	public void setFitnessValues(Double[] fitnessValues) {
		this.fitnessValues = fitnessValues;
	}
	
}
