package splat.executor;

import java.util.Map;

import splat.parser.elements.*;

public class Executor {

	private ProgramAST progAST;
	
	private Map<String, FunctionDecl> funcMap;
	private Map<String, Value> progVarMap;
	
	public Executor(ProgramAST progAST) {
		this.progAST = progAST;
	}

	public void runProgram() throws ExecutionException {

		// This sets the maps that will be needed for executing function 
		// calls and storing the values of the program variables
		setMaps();
		
		try {
			
			// Go through and execute each of the statements
			for (Statement stmt : progAST.getStmts()) {
				stmt.execute(funcMap, progVarMap);
			}
			
		// We should never have to catch this exception here, since the
		// main program body cannot have returns
		} catch (ReturnFromCall ex) {
			System.out.println("Internal error!!! The main program body "
					+ "cannot have a return statement -- this should have "
					+ "been caught during semantic analysis!");
			
			throw new ExecutionException("Internal error -- fix your "
					+ "semantic analyzer!", -1, -1);
		}
	}
	
	private void setMaps() {
		for (Declaration decl : progAST.getDecls()) {
			if (decl instanceof FunctionDecl) {
				FunctionDecl funcDecl = (FunctionDecl) decl;
				funcMap.put(funcDecl.getLabel().toString(), funcDecl);
			} else if (decl instanceof VariableDecl) {
				VariableDecl varDecl = (VariableDecl) decl;
				progVarMap.put(varDecl.getLabel().toString(), Value.defaultValue(varDecl.getType()));
			}
		}
	}

}
