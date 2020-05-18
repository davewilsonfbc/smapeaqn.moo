package it.univaq.disim.seagroup.smapeaqn.moo.problem;

import org.uma.jmetal.problem.Problem;

import it.univaq.disim.seagroup.smapeaqn.moo.csp.Csp;

public interface CspProblemInt<T extends Csp, S> extends Problem<S> {
	
	public int getNumberOfModes();
	
	public int getNumberOfOutgoings();
	
}
