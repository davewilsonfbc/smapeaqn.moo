package it.univaq.disim.seagroup.smapeaqn.moo.managers;

import it.univaq.disim.seagroup.smapeaqn.moo.managers.jsim.Solver;

public class SimulationManager {
	/* TO COMPLETE WITH XML WRITING */
	
	private String seed;
	private String maxTime;
	private String maxSimulated;
	private String maxSamples;
	private boolean disableStaticStop;
	
	private String alpha;
	private String precision;
	
	public static String DEFAULT_SEED = "23000";
	public static String DEFAULT_MAXTIME = "180.0";
	public static String DEFAULT_MAXSIMULATED = "600.0";
	public String DEFAULT_MAXSAMPLES = "500000";
	public boolean DEFAULT_DISABLESTATICSTOP = false;
	
	public static String DEFAULT_ALPHA = "0.05";
	public String DEFAULT_PRECISION = "0.05";
	
	////
	public static boolean exitedBeforeTimeout = true;
	////

	public static void main(String[] args) {
		//String jsimgFilePath = "./resources/SMAPEA-QN-emergency-handling.jsimg";
		simulate(args[0]);
	}
	
	
	public static void simulate(String jsimgFilePath) {
		try {
			System.out.println("SIMULATING "+ jsimgFilePath + "......");
			Solver.main(new String[] { jsimgFilePath });
			////
			exitedBeforeTimeout = Solver.exitedBeforeTimeout;
			////
			//System.out.println("SIMULATION DONE.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public SimulationManager(String seed, String maxTime, String maxSimulated, String maxSamples, boolean disableStaticStop, String confidenceInterval, String maxRelativeError, boolean useDefaults) {
		if(seed != null)
			this.seed = seed;
		else
			if(useDefaults)
				this.seed = DEFAULT_SEED;
		if(maxTime != null)
			this.maxTime = maxTime;
		else
			if(useDefaults)
				this.maxTime = DEFAULT_MAXTIME;
		if(maxSimulated != null)
			this.maxSimulated = maxSimulated;
		else
			if(useDefaults)
				this.maxSimulated = DEFAULT_MAXSIMULATED;
		if(maxSamples != null)
			this.maxSamples = maxSamples;
		else
			if(useDefaults)
				this.maxSamples = DEFAULT_MAXSAMPLES;
		this.disableStaticStop = disableStaticStop;
		
		if(confidenceInterval != null) {
			Double a = 1 - Double.parseDouble(confidenceInterval);
			this.alpha = a.toString();
		}
		else
			if(useDefaults)
				this.alpha = DEFAULT_ALPHA;
		if(maxRelativeError != null)
			this.precision = maxRelativeError;
		else
			if(useDefaults)
				this.precision = DEFAULT_PRECISION;
	}
	
	public SimulationManager() {
		this.seed = DEFAULT_SEED;
		this.maxTime = DEFAULT_MAXTIME;
		this.maxSimulated = DEFAULT_MAXSIMULATED;
		this.maxSamples = DEFAULT_MAXSAMPLES;
		this.disableStaticStop = DEFAULT_DISABLESTATICSTOP;
		
		this.alpha = DEFAULT_ALPHA;
		this.precision = DEFAULT_PRECISION;
	}
	
	
	
	public String getSeed() {
		return seed;
	}
	public void setSeed(String seed) {
		this.seed = seed;
	}
	public String getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(String maxTime) {
		this.maxTime = maxTime;
	}
	public String getMaxSimulated() {
		return maxSimulated;
	}
	public void setMaxSimulated(String maxSimulated) {
		this.maxSimulated = maxSimulated;
	}
	public String getMaxSamples() {
		return maxSamples;
	}
	public boolean isDisableStaticStop() {
		return disableStaticStop;
	}

	public void setDisableStaticStop(boolean disableStaticStop) {
		this.disableStaticStop = disableStaticStop;
	}

	public void setMaxSamples(String maxSamples) {
		this.maxSamples = maxSamples;
	}

	public String getAlpha() {
		return alpha;
	}

	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}
	
	
	
	
}
