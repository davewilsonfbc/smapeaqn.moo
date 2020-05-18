package it.univaq.disim.seagroup.smapeaqn.moo.csp;

import org.w3c.dom.Node;

public abstract class Csp {
	
	protected int numSmOutgoing;
	
	public Csp(int numberOfSmOutgoing) {
		this.numSmOutgoing = numberOfSmOutgoing;
	}
	
	public abstract void generate();
	
	public abstract void apply(Node subParameter);
	
	public int getNumSmOutgoing() {
		return numSmOutgoing;
	}
	public void setNumSmOutgoing(int numCtrl) {
		this.numSmOutgoing = numCtrl;
	}
	
}
