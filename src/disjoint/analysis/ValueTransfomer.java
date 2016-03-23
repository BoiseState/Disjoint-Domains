package disjoint.analysis;

import java.util.List;
import java.util.Map;

import disjoint.domain.Domain;

import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.SootClass;
import soot.toolkits.graph.ExceptionalUnitGraph;

public class ValueTransfomer extends BodyTransformer {
	
	List<Domain> domains;
	boolean symbolicOn;
	int methodId = 16;
	
	//later can pass the domain info there is no
	//need for it to be initialized here
	
	public ValueTransfomer(List<Domain> setDomains, int methodId, boolean symbolicOn){
		super();
		domains=setDomains;
		this.symbolicOn = symbolicOn;
		this.methodId = methodId;
	}

	@Override
	protected void internalTransform(Body b, String phaseName, Map options) {
		String methodName = b.getMethod().getName();
		if(b.getMethod().getDeclaringClass().getMethods().get(methodId).equals(b.getMethod())){
		//System.out.println("method " + methodName);
		//if method's does not have a single local int variable
		//the skip it
		boolean hasIntLocals = false;
		for(Local l : b.getLocals()){
			if(ValueAnalysis.isAnyIntType(l)){
				hasIntLocals = true;
				break;//at least one local var is an int
			}
		}
		if(!methodName.equals("<clinit>") && hasIntLocals){
			//System.out.println(b.getMethod().getDeclaringClass() + "\t" + b.getMethod().getDeclaringClass().getMethods().indexOf(b.getMethod()) + "\t" + b.getMethod());
			System.out.println("analyzing " + b.getMethod().getSignature());
			System.gc();
			ValueAnalysis va = new ValueAnalysis(new ExceptionalUnitGraph(b), domains, symbolicOn);
		}

	}
	}
}
