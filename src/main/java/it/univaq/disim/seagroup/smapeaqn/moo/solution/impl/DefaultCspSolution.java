package it.univaq.disim.seagroup.smapeaqn.moo.solution.impl;

import java.util.HashMap;
import java.util.Map;

import org.uma.jmetal.solution.impl.AbstractGenericSolution;

import it.univaq.disim.seagroup.smapeaqn.moo.csp.Csp;
import it.univaq.disim.seagroup.smapeaqn.moo.csp.ProbabilityBasedCsp;
import it.univaq.disim.seagroup.smapeaqn.moo.problem.CspProblemInterface;
import it.univaq.disim.seagroup.smapeaqn.moo.solution.CspSolution;

public class DefaultCspSolution extends AbstractGenericSolution<Csp, CspProblemInterface> implements CspSolution {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6186410736282347056L;

	/** Constructor */
	  public DefaultCspSolution(CspProblemInterface problem) {
	    super(problem) ;

	    initializeCspVariables();
	    initializeObjectiveValues();
	  }
	  
	  /** Copy constructor */
	  public DefaultCspSolution(DefaultCspSolution solution) {
	    super(solution.problem) ;

	    for (int i = 0; i < problem.getNumberOfVariables(); i++) {
	      setVariableValue(i, solution.getVariableValue(i));
	    }

	    for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
	      setObjective(i, solution.getObjective(i)) ;
	    }

	    attributes = new HashMap<Object, Object>(solution.attributes) ;
	  }
	  
	  
	  @Override
	  public DefaultCspSolution copy() {
	    return new DefaultCspSolution(this);
	  }
	  
	  
	  @Override
	  public int getNumberOfModes() {
	    return problem.getNumberOfModes();
	  }
	  
	  @Override
	  public int getNumberOfOutgoings() {
	    return problem.getNumberOfOutgoings();
	  }
	  
	  
	  
	  @Override
	  public String getVariableValueString(int index) {
	    return getVariableValue(index).toString() ;
	  }
	  
	  
	  
	  protected void initializeCspVariables() {
		Csp[] csps = new Csp[problem.getNumberOfVariables()];
	    for (int i = 0 ; i < problem.getNumberOfVariables(); i++) {
	        csps[i] = new ProbabilityBasedCsp(problem.getNumberOfOutgoings());
			csps[i].generate();
	        setVariableValue(i, csps[i]) ;
	    }
	  }
	  
	  @Override
		public Map<Object, Object> getAttributes() {
			return attributes;
		}
	  
	  @Override
	  public CspSolution createSolution() {
	    return new DefaultCspSolution(this)  ;
	  }

}
