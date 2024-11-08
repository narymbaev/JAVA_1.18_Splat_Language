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

		System.out.println(funcMap + " " + progVarMap);
		
		// Checks to make sure we don't use the same labels more than once
		// for our program functions and variables 
		checkNoDuplicateProgLabels();

		System.out.println("I M RUNNING");


		// This sets the maps that will be needed later when we need to
		// typecheck variable references and function calls in the 
		// program body
		setProgVarAndFuncMaps();

		System.out.println("I DONE");
		// Perform semantic analysis on the functions
		for (FunctionDecl funcDecl : funcMap.values()) {
			System.out.println("Func iteration");
			analyzeFuncDecl(funcDecl);
		}
		System.out.println("I DONE 2");

		
		// Perform semantic analysis on the program body
		for (Statement stmt : progAST.getStmts()) {
			System.out.println("Stmt iteration " + stmt.toString());
			stmt.analyze(funcMap, progVarMap);
			System.out.println("I DONE 3");
		}
		
	}

	private void analyzeFuncDecl(FunctionDecl funcDecl) throws SemanticAnalysisException {
		
		// Checks to make sure we don't use the same labels more than once
		// among our function parameters, local variables, and function names
		checkNoDuplicateFuncLabels(funcDecl);
		
		// Get the types of the parameters and local variables
		Map<String, Type> varAndParamMap = getVarAndParamMap(funcDecl);
		
		// Perform semantic analysis on the function body
		for (Statement stmt : funcDecl.getStmts()) {
			System.out.println(stmt.toString());
			stmt.analyze(funcMap, varAndParamMap);
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
			labels.add(paramLabel);
		}

		for (VariableDecl localVar : funcDecl.getFuncLocalVars()) {
			String localVarLabel = localVar.getLabel().toString();
			if (labels.contains(localVarLabel)) {
				throw new SemanticAnalysisException("Cannot have duplicate label '" + localVarLabel + "' in function local variables", localVar);
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

				System.out.println("Label: " + label);
				System.out.println("Function Declaration: " + funcDecl);
				System.out.println("Function Declaration: " + decl);

				funcMap.put(label, funcDecl);

			} else if (decl instanceof VariableDecl) {
				VariableDecl varDecl = (VariableDecl)decl;
				progVarMap.put(label, varDecl.getType());
			}
			else {
				System.out.println("Unknown declaration: " + decl);
			}
		}
		System.out.println("For done");
	}
}
