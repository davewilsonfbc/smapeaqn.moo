package it.univaq.disim.seagroup.smapeaqn.moo.problem.util;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.JMetalException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CspProblemUtils {
	/**
	   * Create an instance of problem passed as argument
	   * @param problemName A full qualified problem name
	   * @return An instance of the problem
	   */
	  @SuppressWarnings("unchecked")
	  public static <S> Problem<S> loadProblem(String problemName) {
	    Problem<S> problem ;
	    try {
	    	problem = (Problem<S>)Class.forName(problemName).getConstructor().newInstance() ;
	    	//problem = (Problem<S>)Class.forName(problemName).getConstructors()[0].newInstance() ;
	    	//problem = (Problem<S>)Class.forName(problemName).getDeclaredConstructor(String.class, String.class, int.class, int.class).newInstance() ;
	    } catch (InstantiationException e) {
	      throw new JMetalException("newInstance() cannot instantiate (abstract class)", e) ;
	    } catch (IllegalAccessException e) {
	      throw new JMetalException("newInstance() is not usable (uses restriction)", e) ;
	    } catch (InvocationTargetException e) {
	      throw new JMetalException("an exception was thrown during the call of newInstance()", e) ;
	    } catch (NoSuchMethodException e) {
	      throw new JMetalException("getConstructor() was not able to find the constructor without arguments", e) ;
	    } catch (ClassNotFoundException e) {
	      throw new JMetalException("Class.forName() did not recognized the name of the class", e) ;
	    }

	    return problem ;
	  }
}
