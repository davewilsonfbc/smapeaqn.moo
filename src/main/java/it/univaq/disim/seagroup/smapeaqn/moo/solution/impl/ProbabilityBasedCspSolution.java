package it.univaq.disim.seagroup.smapeaqn.moo.solution.impl;

import it.univaq.disim.seagroup.smapeaqn.moo.csp.Csp;
import it.univaq.disim.seagroup.smapeaqn.moo.csp.ProbabilityBasedCsp;
import it.univaq.disim.seagroup.smapeaqn.moo.problem.CspProblemInterface;

public class ProbabilityBasedCspSolution extends DefaultCspSolution {

	private static final long serialVersionUID = 667111139872740334L;
	
	public ProbabilityBasedCspSolution(CspProblemInterface problem) {
		super(problem);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void initializeCspVariables() {
		Csp[] csps = new Csp[problem.getNumberOfVariables()];
	    for (int i = 0 ; i < problem.getNumberOfVariables(); i++) {
	        csps[i] = new ProbabilityBasedCsp(problem.getNumberOfOutgoings());
			csps[i].generate();
	        setVariableValue(i, csps[i]) ;
	    }
	  }

}
