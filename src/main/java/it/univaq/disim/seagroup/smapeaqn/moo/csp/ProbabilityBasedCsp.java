package it.univaq.disim.seagroup.smapeaqn.moo.csp;

import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ProbabilityBasedCsp extends Csp {

	//private int numSmOutgoing;
	private Double[] rp;
	
	/* TEST */
	public static void main(String[] args) {
		//ProbabilityBasedCsp csp = new ProbabilityBasedCsp(9, true);
		ProbabilityBasedCsp csp = new ProbabilityBasedCsp(9);
		csp.generate();
		Double[] cspRp = csp.getRp();
		Double sum = 0.0;
		for(int i=0; i<cspRp.length; i++) {
			System.out.println("CSP[" + i + "]: " + cspRp[i]);
			sum += cspRp[i];
		}
		System.out.println("SUM: " + sum);
	}
	
	public ProbabilityBasedCsp(int numberOfSmOutgoing) {
		super(numberOfSmOutgoing);
	}
	
	public void generate() {
		Double[] probs = new Double[this.numSmOutgoing];
		Double partialSum = 0.0;
		//ub=1 for least intense workloads
		//ub=0.96 for det(1)
		//ub=0.72 for det(0.75)
		//ub=0.45 for det(0.5)
		Double ub = 1.0;
		JMetalRandom randomGenerator = JMetalRandom.getInstance();
		for(int i=0; i<probs.length; i++) {
			if(i==probs.length-1) {
				if((1-partialSum) > ub) {
					double sum=0;
					probs[i] = ub;
					double diff = 1- partialSum - ub;
					double delta = diff / i;
					for(int j=0; j<probs.length-1;j++) {
						probs[j] += delta;
						sum+=probs[j];
					}
					probs[i] = 1 - sum;
				} else
					probs[i] = 1 - partialSum;
			} else {
				probs[i] = randomGenerator.nextDouble(0.0, ub);
				while((probs[i] + partialSum) >= 1) {
					probs[i] = randomGenerator.nextDouble(0.0, ub);
				}
				partialSum += probs[i];
				//ub = 1 - partialSum;
			}
		}
		/*
		double app = probs[0];
		probs[0] = probs[probs.length-1];
		probs[probs.length-1] = app;
		*/
		this.setRp(probs);
	}
	
	public void apply(Node subParameter) {
		Node subParFirstSubPar = subParameter.getChildNodes().item(1);
		
		NodeList subParFirstSubParChildren = subParFirstSubPar.getChildNodes();
		if (subParFirstSubParChildren != null)
			if (subParFirstSubParChildren.getLength() > 0) {
				int rpCounter = 0;
				for(int i=0; i<subParFirstSubParChildren.getLength(); i++) {
					if(subParFirstSubParChildren.item(i).hasChildNodes()) {
						Node subParFirstSubParChild = subParFirstSubParChildren.item(i);
						NodeList subParFirstSubParChildChildren = subParFirstSubParChild.getChildNodes();
						for(int j=0; j<subParFirstSubParChildChildren.getLength(); j++) {
							Node subParFirstSubParChildChild = subParFirstSubParChildChildren.item(j);
							if(subParFirstSubParChildChild.hasAttributes()) {
								if(subParFirstSubParChildChild.getAttributes().getNamedItem("name").getNodeValue().equals("probability")) {
									Node valueNode = subParFirstSubParChildChild.getChildNodes().item(1).getChildNodes().item(0);
									//System.out.println("\tPROBABILITY FOUND: " + valueNode.getNodeValue());
									valueNode.setNodeValue(this.rp[rpCounter].toString());
									System.out.println("\tPROBABILITY[" + rpCounter + "]: " + valueNode.getNodeValue());
									rpCounter++;
								}
							}
							
						}
					}
				}
			}
			
	}
	
	/*
	public ProbabilityBasedCsp(int numberOfSmOutgoing) {
		this(numberOfSmOutgoing, false);
	}
	
	public ProbabilityBasedCsp(int numberOfSmOutgoing, boolean generateProbabilities) {
		numSmOutgoing = numberOfSmOutgoing;
		rp = new Double[numSmOutgoing];
		randomGenerator = JMetalRandom.getInstance();
		for(int i=0; i<rp.length; i++) {
			rp[i] = 0.0;
		}
		if(generateProbabilities) {
			rp = generateProbs(numSmOutgoing);
		}
	}*/
	
	
	/*
	public Double[] generateProbs(int numSmOutgoing) {
		Double[] probs = new Double[numSmOutgoing];
		Double partialSum = 0.0;
		Double ub = 1.0;
		for(int i=0; i<probs.length; i++) {
			if(i==probs.length-1) {
				probs[i] = 1 - partialSum;
			} else {
				probs[i] = randomGenerator.nextDouble(0.0, ub);
				partialSum += probs[i];
				ub = 1 - partialSum;
			}
		}
		return probs;
	}

	public int getNumSmOutgoing() {
		return numSmOutgoing;
	}
	public void setNumSmOutgoing(int numCtrl) {
		this.numSmOutgoing = numCtrl;
	}*/
	public Double[] getRp() {
		return rp;
	}
	public void setRp(Double[] rp) {
		this.rp = rp;
	}
	
	
	
	public String toString() {
		String res = "{";
		for(int i = 0; i < this.getRp().length; i++) {
			res += this.getRp()[i];
			if(i < this.getRp().length - 1)
				res += ", ";
		}
		res += "}";
	    return res ;
	}
	
}
