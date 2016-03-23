package disjoint.state;

import soot.Value;

/*
 * This interface should describe
 * all the functionality
 * that a sub-state should have
 */
public interface State {
	
	
	public State copy();

	public void initFlowVar(Value var);
	
	public void initEntryVar(Value var);
	
	public void initFlow();
	
	public void initEntry();

	public State merge(State state);

}
