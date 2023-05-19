/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simula.compiler.declaration;

import java.util.Vector;

import simula.compiler.utilities.Global;
import simula.compiler.utilities.Meaning;
import simula.compiler.utilities.Util;

public abstract class DeclarationScope extends Declaration {
	
	public int sourceBlockLevel; // Set during Parsing
	protected static int currentRTBlockLevel = 0; // Runtime  Block level - Used during doChecking
	public int ctBlockLevel; // Set during doChecking
	public int rtBlockLevel; // Set during doChecking
	public boolean hasLocalClasses = false;
	public DeclarationList declarationList;// = new DeclarationList();
	public Vector<LabelDeclaration> labelList = new Vector<LabelDeclaration>();

	// ***********************************************************************************************
	// *** Constructor
	// ***********************************************************************************************
	public DeclarationScope(final String ident) {
		super(ident);
		declarationList = new DeclarationList(this.getClass().getSimpleName()+':'+ident+":Line="+Global.sourceLineNumber);
		declaredIn = Global.getCurrentScope();
		Global.setScope(this);
		if (declaredIn != null)	sourceBlockLevel = declaredIn.sourceBlockLevel + 1;
	}

	// ***********************************************************************************************
	// *** Utility: scopeID
	// ***********************************************************************************************
	public String scopeID() {
//		if(declaredIn!=null) return(declaredIn.scopeID()+'.'+identifier+"["+rtBlockLevel+"]");
		if(rtBlockLevel>1) return(declaredIn.scopeID()+'.'+identifier);
		return(identifier);
	}

	// ***********************************************************************************************
	// *** Utility: findVisibleAttributeMeaning
	// ***********************************************************************************************
	public Meaning findVisibleAttributeMeaning(final String ident) {
		Util.FATAL_ERROR("DeclarationScope.findVisibleAttributeMeaning: SHOULD BEEN REDEFINED");
		return (null);
	}
	  
    // ***********************************************************************************************
    // *** Utility: findMeaning
    // ***********************************************************************************************
    public Meaning findMeaning(final String identifier) {
    	Meaning meaning=findVisibleAttributeMeaning(identifier);
    	if(meaning==null && declaredIn!=null) meaning=declaredIn.findMeaning(identifier);
    	if(meaning==null) {
    		if(!Global.duringParsing) Util.error("Undefined variable: "+identifier);
    		meaning=new Meaning(null,null); // Error Recovery: No Meaning
    	}
    	return(meaning);
    }
	  
    // ***********************************************************************************************
    // *** Utility: findLabelMeaning
    // ***********************************************************************************************
    public Meaning findLabelMeaning(final String identifier) {
    	for(LabelDeclaration dcl:labelList) {
    		if(Util.equals(dcl.identifier, identifier)) {
				return (new Meaning(dcl, this, this, false));
    		}
    	}
    	if(declaredIn!=null) return(declaredIn.findLabelMeaning(identifier));
    	return(null);
    }

	// ***********************************************************************************************
	// *** Utility: findProcedure -- Follow Static Chain Looking for a Procedure named 'identifier'
	// ***********************************************************************************************
	public ProcedureDeclaration findProcedure(final String identifier) {
		DeclarationScope scope=this;
		while(scope!=null) {
			if(Util.equals(identifier, scope.identifier)) {
				if(scope instanceof ProcedureDeclaration proc) return(proc);
	    		return(null);
			}
			scope=scope.declaredIn;
		}
		return(null);
	}

    // ***********************************************************************************************
    // *** Coding Utility: edCTX
    // ***********************************************************************************************
    public String edCTX() {
    	if(rtBlockLevel==0) return("CTX_");
    	int curLevel=Global.getCurrentScope().rtBlockLevel;
        int ctxDiff=curLevel-rtBlockLevel;
        return(edCTX(ctxDiff));
        
    }

    // ***********************************************************************************************
    // *** Coding Utility: edCTX
    // ***********************************************************************************************
    public static String edCTX(int ctxDiff) {
        String ret="_CUR";
        while((ctxDiff--)>0) ret=ret+"._SL";
        return("("+ret+')');
    }
	  
    // ***********************************************************************************************
    // *** Print Utility: edScopeChain
    // ***********************************************************************************************
	public String edScopeChain() {
		if (declaredIn == null) return (identifier);
		String encName = declaredIn.edScopeChain();
		return (identifier + '.' + encName);
	}
	  
	// ***********************************************************************************************
	// *** Utility: edJavaClassName
	// ***********************************************************************************************
	public String edJavaClassName() {
		DeclarationScope scope=this;
		String id=null;
		while(scope!=null) {
			if((scope instanceof BlockDeclaration)
    		&& !(scope instanceof StandardClass)
    		&& !(scope instanceof StandardProcedure)) {
				if(id==null) id=scope.identifier;
				else id=scope.identifier+'_'+id;
	        }
			scope=scope.declaredIn;
		}
		return(id);
	}

}
