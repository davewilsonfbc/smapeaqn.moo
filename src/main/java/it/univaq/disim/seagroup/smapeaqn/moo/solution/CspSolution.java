package it.univaq.disim.seagroup.smapeaqn.moo.solution;

import org.uma.jmetal.solution.Solution;

import it.univaq.disim.seagroup.smapeaqn.moo.csp.Csp;

public interface CspSolution extends Solution<Csp> {

	public int getNumberOfModes();
	
	public int getNumberOfOutgoings();

	public CspSolution createSolution();
	
}
