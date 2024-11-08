package splat.semanticanalyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import splat.parser.elements.Declaration;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.ProgramAST;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.parser.elements.VariableDecl;
import splat.parser.elements.statements.Return;

public class SemanticAnalyzer {

	private ProgramAST progAST;
	
	private Map<String, FunctionDecl> funcMap;
	private Map<String, Type> progVarMap;
	
	public SemanticAnalyzer(ProgramAST progAST) {
		this.progAST = progAST;
	}

	public void analyze() throws SemanticAnalysisException {
		funcMap = new HashMap<String, FunctionDecl>();
		progVarMap = new HashMap<String, Type>();

		// Checks to make sure we don't use the same labels more than once
		// for our program functions and variables 
		checkNoDuplicateProgLabels();

		// This sets the maps that will be needed later when we need to
		// typecheck variable references and function calls in the 
		// program body
		setProgVarAndFuncMaps();


		// Perform semantic analysis on the functions
		for (FunctionDecl funcDecl : funcMap.values()) {
			analyzeFuncDecl(funcDecl);
		}

		
		// Perform semantic analysis on the program body
		for (Statement stmt : progAST.getStmts()) {
			stmt.analyze(funcMap, progVarMap);
		}
		
	}

	private void analyzeFuncDecl(FunctionDecl funcDecl) throws SemanticAnalysisException {
		
		// Checks to make sure we don't use the same labels more than once
		// among our function parameters, local variables, and function names
		checkNoDuplicateFuncLabels(funcDecl);
		
		// Get the types of the parameters and local variables
		Map<String, Type> varAndParamMap = getVarAndParamMap(funcDecl);

		boolean hasReturnStatement = false;
		Type expectedReturnType = funcDecl.getReturnType();
		
		// Perform semantic analysis on the function body
		for (Statement stmt : funcDecl.getStmts()) {
			stmt.analyze(funcMap, varAndParamMap);

			// Check if stmt is a return statement
			if (stmt instanceof Return) {
				hasReturnStatement = true;
			}
		}

		Type res = varAndParamMap.get("1result");

		if (res != null && !res.getValue().equals("void")) {
			hasReturnStatement = true;
		}

		// If no return statement was found for a non-void function, throw an error
		if (!hasReturnStatement && !expectedReturnType.equals(Type.VOID)) {
			throw new SemanticAnalysisException("Missing return statement in function " + funcDecl.getReturnType(), funcDecl);
		}

	}
	
	
	private Map<String, Type> getVarAndParamMap(FunctionDecl funcDecl) {
		Map<String, Type> varAndParamMap = new HashMap<>();

		varAndParamMap.put("0result", funcDecl.getReturnType());

		// FIXME: Somewhat similar to setProgVarAndFuncMaps()
		for (VariableDecl param : funcDecl.getFuncParams()) {
			String paramLabel = param.getLabel().toString();
			varAndParamMap.put(paramLabel, param.getType());
		}

		for (VariableDecl localVar : funcDecl.getFuncLocalVars()) {
			String localVarLabel = localVar.getLabel().toString();
			varAndParamMap.put(localVarLabel, localVar.getType());
		}

		return varAndParamMap;
	}

	private void checkNoDuplicateFuncLabels(FunctionDecl funcDecl) 
									throws SemanticAnalysisException {
		
		// FIXME: Similar to checkNoDuplicateProgLabels()
		Set<String> labels = new HashSet<String>();

		for (VariableDecl param : funcDecl.getFuncParams()) {
			String paramLabel = param.getLabel().toString();
			if (labels.contains(paramLabel)) {
				throw new SemanticAnalysisException("Cannot have duplicate label '" + paramLabel + "' in function parameters", param);
			}

			// Check conflict with function names
			for (FunctionDecl func : funcMap.values()) {
				if (func.getLabel().toString().equals(paramLabel)) {
					throw new SemanticAnalysisException("Cannot have parameter label '" + paramLabel + "' matching a function name", param);
				}
			}

			labels.add(paramLabel);
		}

		for (VariableDecl localVar : funcDecl.getFuncLocalVars()) {
			String localVarLabel = localVar.getLabel().toString();
			if (labels.contains(localVarLabel)) {
				throw new SemanticAnalysisException("Cannot have duplicate label '" + localVarLabel + "' in function local variables", localVar);
			}

			// Check conflict with function names
			for (FunctionDecl func : funcMap.values()) {
				if (func.getLabel().toString().equals(localVarLabel)) {
					throw new SemanticAnalysisException("Cannot have parameter label '" + localVarLabel + "' matching a function name", localVar);
				}
			}
			labels.add(localVarLabel);
		}
	}
	
	private void checkNoDuplicateProgLabels() throws SemanticAnalysisException {
		
		Set<String> labels = new HashSet<String>();
		
 		for (Declaration decl : progAST.getDecls()) {
 			String label = decl.getLabel().toString();
 			
			if (labels.contains(label)) {
				throw new SemanticAnalysisException("Cannot have duplicate label '"
						+ label + "' in program", decl);
			} else {
				labels.add(label);
			}
			
		}
	}
	
	private void setProgVarAndFuncMaps() {
		for (Declaration decl : progAST.getDecls()) {

			String label = decl.getLabel().toString();

			if (decl instanceof FunctionDecl) {
				FunctionDecl funcDecl = (FunctionDecl) decl;

				funcMap.put(label, funcDecl);

			} else if (decl instanceof VariableDecl) {
				VariableDecl varDecl = (VariableDecl)decl;
				progVarMap.put(label, varDecl.getType());
			}
			else {
			}
		}
	}
}
