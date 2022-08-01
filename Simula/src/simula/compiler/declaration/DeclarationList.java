package simula.compiler.declaration;

import java.util.Vector;

import simula.compiler.utilities.Util;

public class DeclarationList extends Vector<Declaration> {
	
	private static final long serialVersionUID = 1L;
	public String identifier;

	public DeclarationList(String identifier) {
		this.identifier=identifier;
	}
	
	public Declaration find(String identifier) {
		for(Declaration d:this) if(d.identifier.equals(identifier)) {
			return(d);
		} return(null);
	}
	
	public boolean add(Declaration dcl) {
//		for(Declaration d:this) if(d.identifier.equals(dcl.identifier)) {
//			Util.error("Multiple declarations with the same name: "+dcl+" and "+d);
//			Util.FATAL_ERROR("DeclarationList.add");
//			return(false);
//		}
		Declaration d=find(dcl.identifier);
		if(d!=null) {
			Util.warning("Multiple declarations with the same name: "+dcl.identifier);
//			Util.FATAL_ERROR("DeclarationList.add");
			return(false);			
		}
		super.addElement(dcl);
		return(true);
	}

	public void print(String title) {
		Util.println("DeclarationList: "+identifier+" -- "+title);
		for(Declaration decl:this) {
			Util.println(decl.toString());
		}
	}
}
