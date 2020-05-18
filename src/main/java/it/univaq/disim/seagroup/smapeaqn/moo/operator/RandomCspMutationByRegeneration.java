package it.univaq.disim.seagroup.smapeaqn.moo.operator;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.BoundedRandomGenerator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

import it.univaq.disim.seagroup.smapeaqn.moo.solution.CspSolution;

@SuppressWarnings("serial")
public class RandomCspMutationByRegeneration implements MutationOperator<CspSolution> {
	
	private double mutationProbability;
	private RandomGenerator<Double> mutationRandomGenerator;
    private BoundedRandomGenerator<Integer> pointRandomGenerator;
	
	/** Constructor */
	public RandomCspMutationByRegeneration(double mutationProbability) {
	  this(mutationProbability,
	          () -> JMetalRandom.getInstance().nextDouble(),
	          (a, b) -> JMetalRandom.getInstance().nextInt(a, b));
	}

    /** Constructor */
    public RandomCspMutationByRegeneration(
        double mutationProbability, RandomGenerator<Double> mutationRandomGenerator) {
      this(
    	  mutationProbability,
    	  mutationRandomGenerator,
          BoundedRandomGenerator.fromDoubleToInteger(mutationRandomGenerator));
    }
	    
	/** Constructor */
    public RandomCspMutationByRegeneration(
        double mutationProbability,
        RandomGenerator<Double> mutationRandomGenerator,
        BoundedRandomGenerator<Integer> pointRandomGenerator) {
      if (mutationProbability < 0) {
        throw new JMetalException("Crossover probability is negative: " + mutationProbability);
      }
      this.mutationProbability = mutationProbability;
      this.mutationRandomGenerator = mutationRandomGenerator;
      this.pointRandomGenerator = pointRandomGenerator;
    }
	

	/* Getters */
	public double getMutationProbability() {
	  return mutationProbability;
	}

	/* Setters */
	public void setMutationProbability(double mutationProbability) {
	  this.mutationProbability = mutationProbability;
	}
	
	/** Execute() method */
	  @Override
	  public CspSolution execute(CspSolution solution) throws JMetalException {
	    if (null == solution) {
	      throw new JMetalException("Null parameter");
	    }

	    doMutation(mutationProbability, solution);

	    return solution;
	  }

	private void doMutation(double probability, CspSolution solution) {
		if (mutationRandomGenerator.getRandomValue() < probability) {
			int length = solution.getNumberOfVariables();
			//Get the mutation point i
			int mutationPoint = pointRandomGenerator.getRandomValue(0, length - 1);
			//Mutate the i-th CSP by auto-generation
			//NOTE: The CSP type is maintained. Auto-generation might be ineffective for certain CSPs 
			solution.getVariableValue(mutationPoint).generate();
		}
	}
}
