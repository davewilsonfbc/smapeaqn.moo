package it.univaq.disim.seagroup.smapeaqn.moo.operator;

import it.univaq.disim.seagroup.smapeaqn.moo.csp.Csp;
import it.univaq.disim.seagroup.smapeaqn.moo.solution.CspSolution;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.BoundedRandomGenerator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class SinglePointCspCrossover implements CrossoverOperator<CspSolution> {
	
	private double crossoverProbability;
    private RandomGenerator<Double> crossoverRandomGenerator;
    private BoundedRandomGenerator<Integer> pointRandomGenerator;

    /** Constructor */
    public SinglePointCspCrossover(double crossoverProbability) {
      this(
        crossoverProbability,
        () -> JMetalRandom.getInstance().nextDouble(),
        (a, b) -> JMetalRandom.getInstance().nextInt(a, b));
	  }
    
    /** Constructor */
    public SinglePointCspCrossover(
        double crossoverProbability, RandomGenerator<Double> randomGenerator) {
      this(
          crossoverProbability,
          randomGenerator,
          BoundedRandomGenerator.fromDoubleToInteger(randomGenerator));
    }

    /** Constructor */
    public SinglePointCspCrossover(
        double crossoverProbability,
        RandomGenerator<Double> crossoverRandomGenerator,
        BoundedRandomGenerator<Integer> pointRandomGenerator) {
      if (crossoverProbability < 0) {
        throw new JMetalException("Crossover probability is negative: " + crossoverProbability);
      }
      this.crossoverProbability = crossoverProbability;
      this.crossoverRandomGenerator = crossoverRandomGenerator;
      this.pointRandomGenerator = pointRandomGenerator;
    }

    /* Getter */
    public double getCrossoverProbability() {
      return crossoverProbability;
    }

    /* Setter */
    public void setCrossoverProbability(double crossoverProbability) {
      this.crossoverProbability = crossoverProbability;
    }
    
    @Override
    public List<CspSolution> execute(List<CspSolution> solutions) {
      //Check.isNotNull(solutions);
      //Check.that(solutions.size() == 2, "There must be two parents instead of " + solutions.size());

      return doCrossover(crossoverProbability, solutions.get(0), solutions.get(1));
    }
    
    
    
    /**
     * Perform the crossover operation.
     *
     * @param probability Crossover setProbability
     * @param parent1 The first parent
     * @param parent2 The second parent
     * @return An array containing the two offspring
     */
    public List<CspSolution> doCrossover(
      double probability, CspSolution parent1, CspSolution parent2) {
      List<CspSolution> offspring = new ArrayList<>(2);
      offspring.add((CspSolution) parent1.copy());
      offspring.add((CspSolution) parent2.copy());

      if (crossoverRandomGenerator.getRandomValue() < probability) {
        // 1. Get the total number of bits
        int length = parent1.getNumberOfVariables();

        // 2. Calculate the point to make the crossover
        int crossoverPoint = pointRandomGenerator.getRandomValue(0, length - 1);
        
        CspSolution offspring1 = (CspSolution) parent1.copy();
        CspSolution offspring2 = (CspSolution) parent2.copy();
        
        for (int i = crossoverPoint; i < offspring1.getNumberOfVariables(); i++) {
            Csp swap = offspring1.getVariableValue(i);
            offspring1.setVariableValue(i, offspring2.getVariableValue(i));
            offspring2.setVariableValue(i, swap);
        }
        
        offspring.set(0, offspring1);
        offspring.set(1, offspring2);

        // 3. Compute the variable containing the crossover bit
        /*int variable = 0;
        int bitsAccount = parent1.getVariable(variable).getBinarySetLength();
        while (bitsAccount < (crossoverPoint + 1)) {
          variable++;
          bitsAccount += parent1.getVariable(variable).getBinarySetLength();
        }

        // 4. Compute the bit into the selected variable
        int diff = bitsAccount - crossoverPoint;
        int intoVariableCrossoverPoint = parent1.getVariable(variable).getBinarySetLength() - diff;

        // 5. Apply the crossover to the variable;
        BinarySet offspring1, offspring2;
        offspring1 = (BinarySet) parent1.getVariable(variable).clone();
        offspring2 = (BinarySet) parent2.getVariable(variable).clone();

        for (int i = intoVariableCrossoverPoint; i < offspring1.getBinarySetLength(); i++) {
          boolean swap = offspring1.get(i);
          offspring1.set(i, offspring2.get(i));
          offspring2.set(i, swap);
        }

        offspring.get(0).setVariable(variable, offspring1);
        offspring.get(1).setVariable(variable, offspring2);

        // 6. Apply the crossover to the other variables
        for (int i = variable + 1; i < parent1.getNumberOfVariables(); i++) {
          offspring.get(0).setVariable(i, (BinarySet) parent2.getVariable(i).clone());
          offspring.get(1).setVariable(i, (BinarySet) parent1.getVariable(i).clone());
        }*/
      }
      return offspring;
    }

    @Override
    public int getNumberOfRequiredParents() {
      return 2;
    }

    @Override
    public int getNumberOfGeneratedChildren() {
      return 2;
    }

	
}
