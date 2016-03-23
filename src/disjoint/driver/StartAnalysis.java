package disjoint.driver;

import java.io.IOException;
import java.util.List;

import com.microsoft.z3.Z3Exception;

import disjoint.analysis.ValueTransfomer;
import disjoint.domain.Domain;
import disjoint.domain.reader.DomainReader;
import disjoint.solver.SolverWrapper;
import disjoint.solver.SolverWrapperZ3;
import soot.PackManager;
import soot.Scene;
import soot.Transform;
/**
 * The driver for the analysis
 * you're welcome to come up with your own.
 * @author elenasherman
 *
 */

public class StartAnalysis {
	/**
	 *  The class should have static fields for the files to write to
	 *  className_sY_domainName
	 *  where  (with all its methods)
	 *  domainName is the domain that the analysis uses
	 *  sY means using symbolic helper state and sN means not using symbolic helper state.
	 * @param args, where 
	 * args[0] - the name of class being analyzed, make sure its location in your classpath
	 * args[1] - the integer representing the order in which the method
	 *           occurs in the class file, i.e., first method, second method and so on
	 * args[2] - the path to the domain file
	 * args[3] - "sY" for adding symbolic analysis
	 */
	public static void main(String[] args) {

		//Parse arguments
		String className = args[0];
		Integer methodId = Integer.parseInt(args[1]);
		String domainName = args[2];
		String symbolicOn = args[3];
		//Print info based on the arguments
		System.out.println("Running analysis for " + domainName + "_"+ symbolicOn);
		try {
			//instantiate domain and analysis
			new StartAnalysis(className, methodId, domainName, symbolicOn.equals("sY"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public StartAnalysis(String className, int methodId, String domainFile, boolean symbolicOn) throws IOException{
		//instantiate the list of domains from a file
		DomainReader dr = new DomainReader(domainFile);
		List<Domain> domain = dr.getReadDomains();
		//show the domain encoding used in the analysis
		System.out.println("Domain provided: \n" + " " + domain);
		//"-f" "n" means for soot not to output the compiled files 
		String[] sootArgs = {"-f", "n", className};
		//add the analysis into the compiler
		PackManager.v().getPack("jtp").
		add(new Transform("jtp.disjoint", new ValueTransfomer(domain, methodId, symbolicOn)));
		//system separator
		String pathSeparator = System.getProperty("path.separator");
		//adding runtime to the path
		Scene.v().setSootClassPath(Scene.v().getSootClassPath()+pathSeparator+System.getProperty("java.class.path") 
				+ pathSeparator + System.getProperty("sun.boot.class.path"));
		//run soot with the added analysis
		soot.Main.main(sootArgs);
	}

	/*
	 * Instantiates solver based on the choice
	 * of the solver 
	 * For now just a single one that
	 * we have based on Z3
	 */
	public static SolverWrapper getSolver(){
		SolverWrapper s = null;
		try {
			s = new SolverWrapperZ3();

		} catch (Z3Exception e) {
			e.printStackTrace();
			System.out.println("Cannot instatiate the solver");
			System.exit(2);
		}
		//set the timeout if applicable
		//in milliseconds
		s.setTimeOut(10000000);
		return s;
	}

}
