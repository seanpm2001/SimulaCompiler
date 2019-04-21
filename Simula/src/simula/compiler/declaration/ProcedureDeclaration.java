/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simula.compiler.declaration;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Vector;

import simula.compiler.JavaModule;
import simula.compiler.parsing.Parser;
import simula.compiler.statement.Statement;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.Meaning;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Type;
import simula.compiler.utilities.Util;

/**
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 */
public class ProcedureDeclaration extends BlockDeclaration implements Externalizable {
	public Vector<Parameter> parameterList = new Vector<Parameter>();
	public VirtualMatch myVirtual; // Set during doChecking

	// ***********************************************************************************************
	// *** CONSTRUCTORS
	// ***********************************************************************************************
	public ProcedureDeclaration(final String identifier,final BlockKind blockKind) {
		super(identifier);
		this.blockKind = blockKind;
	}

	// ***********************************************************************************************
	// *** Parsing: doParseProcedureDeclaration
	// ***********************************************************************************************
	/**
	 * Procedure Declaration.
	 * 
	 * <pre>
	 * Syntax:
	 * 
	 * ProcedureDeclaration
	 *     = [ type ] PROCEDURE ProcedureIdentifier ProcedureHead ProcedureBody
	 *     
	 * ProcedureHead
	 *     = [ FormalParameterPart ; [ ModePart ]
	 *         specification-part  ] ;
	 *         
	 * ProcedureBody = Statement
	 * ProcedureIdentifier = Identifier
	 * </pre>
	 */
	public static ProcedureDeclaration doParseProcedureDeclaration(final Type type) {
		BlockKind blockKind = (Option.standardClass) ? BlockKind.StaticMethod : BlockKind.Procedure;
		ProcedureDeclaration block = new ProcedureDeclaration(null, blockKind);
		block.type = type;
		if (Option.TRACE_PARSE)	Parser.TRACE("Parse ProcedureDeclaration, type=" + type);
		BlockParser.doParseProcedureDeclaration(block);
		block.lastLineNumber = Global.sourceLineNumber;
		if (Option.TRACE_PARSE)	Util.TRACE("END ProcedureDeclaration: " + block);
		// Debug.BREAK("END ProcedureDeclaration: ");
		Global.currentScope = block.declaredIn;
		return (block);
	}

	// ***********************************************************************************************
	// *** Utility: addParameter
	// ***********************************************************************************************
	public void addParameter(final Parameter parameter) {
		for (Parameter par : parameterList)
			if (par.identifier.equalsIgnoreCase(parameter.identifier)) {
				Util.error("Parameter already defined: " + identifier);
				return;
			}
		parameterList.add(parameter);
	}

	// ***********************************************************************************************
	// *** Checking
	// ***********************************************************************************************
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		Global.sourceLineNumber = lineNumber;
		if (blockKind == BlockKind.ContextFreeMethod) externalIdent = this.identifier;
		if (blockKind == BlockKind.StaticMethod) externalIdent = this.identifier;
		if (blockKind == BlockKind.MemberMethod) externalIdent = this.identifier;
		else if (externalIdent == null)	externalIdent = edJavaClassName();

		currentBlockLevel++;
		blockLevel = currentBlockLevel;
		// Util.BREAK("ProcedureDeclaration("+identifier+").doChecking: currentBlockLevel="+currentBlockLevel);
		// Util.BREAK("ProcedureDeclaration("+identifier+").doChecking: blockLevel="+blockLevel);
		//Util.BREAK("ProcedureDeclaration("+identifier+").doChecking: declaredIn="+declaredIn);
		Global.currentScope = this;

		int prfx = 0;// prefixLevel();
		// Util.BREAK("ProcedureDeclaration("+identifier+").doChecking: prefixLevel="+prfx);

		if (blockKind == BlockKind.Procedure)
			for (Parameter par : this.parameterList) par.setExternalIdentifier(prfx);
		for (Declaration par : this.parameterList) par.doChecking();
		for (Declaration dcl : declarationList)	dcl.doChecking();
		for (Statement stm : statements) stm.doChecking();
		doCheckLabelList(null);
		VirtualSpecification virtualSpec = VirtualSpecification.getVirtualSpecification(this);
		if (virtualSpec != null) {
			//Util.BREAK("ProcedureDeclaration("+identifier+").doChecking: virtualSpec="+virtualSpec);
			// This Procedure is a Virtual Match
			myVirtual = new VirtualMatch(virtualSpec, this);
			ClassDeclaration decl = (ClassDeclaration) declaredIn;
			decl.virtualMatchList.add(myVirtual);
//			if (decl == virtualSpec.specifiedIn) virtualSpec.hasDefaultMatch = true;
			if (decl == virtualSpec.declaredIn) virtualSpec.hasDefaultMatch = true;
		}
		Global.currentScope = declaredIn;
		currentBlockLevel--;
		SET_SEMANTICS_CHECKED();
	}

	// ***********************************************************************************************
	// *** Utility: findVisibleAttributeMeaning
	// ***********************************************************************************************
	public Meaning findVisibleAttributeMeaning(final String ident) {
		// if (ident.equalsIgnoreCase("P")) Util.BREAK("ProcedureDeclaration("+identifier+").findVisibleAttributeMeaning("+ident+"): scope="+this);
		// if (ident.equalsIgnoreCase("P")) Util.BREAK("ProcedureDeclaration("+identifier+").findVisibleAttributeMeaning("+ident+"): declaredIn="+declaredIn);
		for (Declaration declaration : declarationList)
			if (ident.equalsIgnoreCase(declaration.identifier))
				return (new Meaning(declaration, this, this, false));
		for (Parameter parameter : parameterList)
			if (ident.equalsIgnoreCase(parameter.identifier))
				return (new Meaning(parameter, this, this, false));
		for (LabelDeclaration label : labelList)
			if (ident.equalsIgnoreCase(label.identifier))
				return (new Meaning(label, this, this, false));
		return (null);
	}

	// ***********************************************************************************************
	// *** Coding: doJavaCoding
	// ***********************************************************************************************
	public void doJavaCoding() {
		// Util.BREAK("ProcedureDeclaration.doJavaCoding: " + identifier + ", BlockKind=" + blockKind);
		ASSERT_SEMANTICS_CHECKED(this);
		if (this.isPreCompiled)	return;
		switch (blockKind) {
		case ContextFreeMethod:	doMethodJavaCoding("static ", false); break;
		case MemberMethod: doMethodJavaCoding("", true); break;
		case StaticMethod: doMethodJavaCoding("static ", true); break;
		case Procedure:	doProcedureCoding(); break;
		default: Util.FATAL_ERROR("Impossible Situation !");
		}
	}

	// ***********************************************************************************************
	// *** Coding: METHOD -- Generate Inline Method code for Procedure.
	// ***********************************************************************************************
	// Generate Inline Method code for Procedure.
	private void doMethodJavaCoding(final String modifier,final boolean addStaticLink) {
		Global.sourceLineNumber = lineNumber;
		Util.BREAK("ProcedureDeclaration.doMethodJavaCoding: " + identifier);
		ASSERT_SEMANTICS_CHECKED(this);
		Global.currentScope = this;
		String line = "public " + modifier + ((type == null) ? "void" : type.toJavaType());
		line = line + ' ' + getJavaIdentifier() + ' ' + edFormalParameterList(true, addStaticLink);
		JavaModule.code(line);
		if (type != null) {
			JavaModule.debug("// Declare return value as variable");
			JavaModule.code(type.toJavaType() + ' ' + "RESULT$" + '=' + type.edDefaultValue() + ';');
		}
		for (Declaration decl : labelList) decl.doJavaCoding();
		for (Declaration decl : declarationList) decl.doJavaCoding();
		for (Statement stm : statements) stm.doJavaCoding();
		if (type != null) JavaModule.code("return(RESULT$);");
		JavaModule.code("}");
		Global.currentScope = declaredIn;
	}

	// ***********************************************************************************************
	// *** Coding Utility: edFormalParameterList
	// ***********************************************************************************************
	// Also used by subclass StandardProcedure
	public String edFormalParameterList(final boolean isInlineMethod,final boolean addStaticLink) {
		// Accumulates through prefix-chain when class
		StringBuilder s = new StringBuilder();
		s.append('(');
		boolean withparams = false;
		if (addStaticLink) {
			String SL = "RTObject$";
			// String SL=this.declaredIn.externalIdent;
			s.append(SL + " SL$");
			withparams = true;
		}
		for (Declaration par : this.parameterList) {
			if (withparams)	s.append(',');
			withparams = true;
			s.append(((Parameter) par).toJavaType()).append(' ');
			if (isInlineMethod)
				 s.append(par.identifier);
			else s.append('s').append(par.externalIdent); // s to indicate Specified Parameter
		}
		s.append(") {");
		return (s.toString());
	}

	// ***********************************************************************************************
	// *** Coding: PROCEDURE
	// ***********************************************************************************************
	private void doProcedureCoding() {
		Global.sourceLineNumber = lineNumber;
		// Util.BREAK("ProcedureDeclaration.doProcedureCoding: "+identifier);
		ASSERT_SEMANTICS_CHECKED(this);
		if (this.isPreCompiled)	return;
		JavaModule javaModule = new JavaModule(this);
		Global.currentScope = this;
		JavaModule.code("@SuppressWarnings(\"unchecked\")");
		JavaModule.code("public final class " + getJavaIdentifier() + " extends BASICIO$ {");
		JavaModule.debug("// ProcedureDeclaration: BlockKind=" + blockKind + ", BlockLevel=" + blockLevel
					+ ", firstLine=" + lineNumber + ", lastLine=" + lastLineNumber + ", hasLocalClasses="
					+ ((hasLocalClasses) ? "true" : "false") + ", System=" + ((isQPSystemBlock()) ? "true" : "false"));
		JavaModule.code("public int prefixLevel() { return(0); }");
		if (isQPSystemBlock())
			JavaModule.code("public boolean isQPSystemBlock() { return(true); }");
		if (blockKind == BlockKind.Procedure && type != null) {
			JavaModule.debug("// Declare return value as attribute");
			JavaModule.code("public " + type.toJavaType() + ' ' + "RESULT$;");
			JavaModule.code("public Object RESULT$() { return(RESULT$); }");
		}
		JavaModule.debug("// Declare parameters as attributes");
		boolean hasParameter = false;
		for (Parameter par : parameterList) {
			String tp = par.toJavaType();
			hasParameter = true;
			JavaModule.code("public " + tp + ' ' + par.externalIdent + ';');
		}
		if (!labelList.isEmpty()) {
			JavaModule.debug("// Declare local labels");
			for (Declaration decl : labelList) decl.doJavaCoding();
		}
		JavaModule.debug("// Declare locals as attributes");
		for (Declaration decl : declarationList) decl.doJavaCoding();
		if (blockKind == BlockKind.Procedure && hasParameter) doCodePrepareFormal();
		doCodeConstructor();
		codeProcedureBody();
		javaModule.codeProgramInfo();
		JavaModule.code("}", "End of Procedure");
		Global.currentScope = declaredIn;
		javaModule.closeJavaOutput();
	}

	// ***********************************************************************************************
	// *** Coding Utility: doCodeConstructor
	// ***********************************************************************************************
	private void doCodeConstructor() {
		JavaModule.debug("// Normal Constructor");
		JavaModule.code("public " + getJavaIdentifier() + edFormalParameterList(false, true));
		JavaModule.code("super(SL$);");
		JavaModule.debug("// Parameter assignment to locals");
		for (Parameter par : parameterList)
			JavaModule.code("this." + par.externalIdent + " = s" + par.externalIdent + ';');
		JavaModule.code("BBLK();");
		JavaModule.debug("// Declaration Code");
		JavaModule.debug("TRACE_BEGIN_DCL$(\"" + identifier + "\"," + Global.sourceLineNumber + ");");
		for (Declaration decl : declarationList) decl.doDeclarationCoding();
		JavaModule.code("STM$();");
		JavaModule.code("}");
	}

	// ***********************************************************************************************
	// *** Coding Utility: doCodePrepareFormal
	// ***********************************************************************************************
	private void doCodePrepareFormal() {
		JavaModule.debug("// Parameter Transmission in case of Formal/Virtual Procedure Call");
		JavaModule.code("private int $npar=0; // Number of actual parameters transmitted.");
		JavaModule.code("public " + getJavaIdentifier() + " setPar(Object param) {");
		JavaModule.debug("//Util.BREAK(\"CALL " + getJavaIdentifier()
				+ ".setPar: param=\"+param+\", qual=\"+param.getClass().getSimpleName()+\", npar=\"+$npar+\", staticLink=\"+SL$);");
		JavaModule.code("try {");
		JavaModule.code("switch($npar++) {");
		int npar = 0;
		for (Parameter par : parameterList) {
			String tp = par.toJavaType();
			String typeValue;
			if (par.mode == Parameter.Mode.name) typeValue = ("(" + tp + ")param");
			else if (par.kind == Parameter.Kind.Array) {
				typeValue = ("arrayValue(param)");
				if (par.mode == Parameter.Mode.value) typeValue = typeValue + ".COPY()";
			} else if (par.kind == Parameter.Kind.Procedure) typeValue = ("procValue(param)");
			else if (par.kind != Parameter.Kind.Simple)	typeValue = ("(" + tp + ")param");
			else if (par.type.isArithmeticType()) typeValue = (tp + "Value(param)");
			else typeValue = ("(" + tp + ")objectValue(param)");
			JavaModule.code("case " + (npar++) + ": " + par.externalIdent + "=" + typeValue + "; break;");
		}
		JavaModule.code("default: throw new RuntimeException(\"Wrong number of parameters\");");
		JavaModule.code("}");
		JavaModule.code("}");
		JavaModule.code("catch(ClassCastException e) { throw new RuntimeException(\"Wrong type of parameter: \"+$npar+\" \"+param,e);}");
		JavaModule.code("return(this);");
		JavaModule.code("}");
		JavaModule.debug("// Constructor in case of Formal/Virtual Procedure Call");
		JavaModule.code("public " + getJavaIdentifier() + "(RTObject$ SL$)");
		JavaModule.code("{ super(SL$); }");
	}

	// ***********************************************************************************************
	// *** Coding Utility: codeProcedureBody -- Redefined in SwitchDeclaration
	// ***********************************************************************************************
	public void codeProcedureBody() {
		JavaModule.debug("// Procedure Statements");
		JavaModule.code("public " + getJavaIdentifier() + " STM$() {");
		JavaModule.debug("TRACE_BEGIN_STM$(\"" + identifier + "\"," + Global.sourceLineNumber + ");");
		codeSTMBody();
		JavaModule.debug("TRACE_END_STM$(\"" + identifier + "\"," + Global.sourceLineNumber + ");");
		JavaModule.code("EBLK();");
		JavaModule.code("return(this);");
		JavaModule.code("}", "End of Procedure BODY");
	}

	// ***********************************************************************************************
	// *** Printing Utility: print
	// ***********************************************************************************************
	public void print(final int indent) {
    	String spc=edIndent(indent);
		StringBuilder s = new StringBuilder(spc);
		s.append('[').append(sourceBlockLevel).append(':').append(blockLevel).append("] ");
		s.append(blockKind).append(' ').append(identifier);
		s.append('[').append(externalIdent).append("] ");
		s.append(editParameterList());
		Util.println(s.toString());
		String beg = "begin[" + edScopeChain() + ']';
		Util.println(spc + beg);
		// if(!hiddenList.isEmpty()) Util.println(indent+" HIDDEN"+hiddenList);
		// if(!protectedList.isEmpty()) Util.println(indent+" PROTECTED"+protectedList);
		for (Declaration decl : declarationList) decl.print(indent + 1);
		for (Statement stm : statements) stm.print(indent + 1);
		Util.println(spc + "end[" + edScopeChain() + ']');
		// Util.BREAK("ProcedureDeclaration.print DONE");
	}

	// ***********************************************************************************************
	// *** Printing Utility: editParameterList
	// ***********************************************************************************************
	private String editParameterList() {
		StringBuilder s = new StringBuilder();
		s.append('(');
		boolean first = true;
		for (Parameter par : parameterList) {
			if (!first)	s.append(',');
			s.append(par);
			first = false;
		}
		s.append(')');
		s.append(';');
		return (s.toString());
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(identifier).append('[').append(externalIdent).append("] BlockKind=").append(blockKind);
		if (isProtected != null) {
			s.append(", Protected by ").append(isProtected.identifier);
			s.append(" defined in ");
			s.append((isProtected.definedIn != null) ? isProtected.definedIn.identifier : "MISSING");
		}
		return (s.toString());
	}

	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************
	public ProcedureDeclaration() {	super(null); }

	@Override
	public void writeExternal(ObjectOutput oupt) throws IOException {
		Util.TRACE_OUTPUT("BEGIN Write ProcedureDeclaration: "+identifier);
		oupt.writeObject(identifier);
		oupt.writeObject(externalIdent);
		oupt.writeObject(type);
		oupt.writeInt(blockLevel);
		oupt.writeBoolean(hasLocalClasses);

		oupt.writeObject(parameterList);
		oupt.writeObject(labelList);
		oupt.writeObject(declarationList);
		Util.TRACE_OUTPUT("END Write ProcedureDeclaration: "+identifier);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void readExternal(ObjectInput inpt) throws IOException, ClassNotFoundException {
		blockKind=BlockKind.Procedure;
		Util.TRACE_INPUT("BEGIN Read ProcedureDeclaration: "+identifier+", Declared in: "+this.declaredIn);
		identifier=(String)inpt.readObject();
		externalIdent=(String)inpt.readObject();
		type=Type.inType(inpt);
		blockLevel=inpt.readInt();
		hasLocalClasses=inpt.readBoolean();
		
		parameterList=(Vector<Parameter>) inpt.readObject();
		labelList=(Vector<LabelDeclaration>) inpt.readObject();
		declarationList=(Vector<Declaration>) inpt.readObject();
		Util.TRACE_INPUT("END Read ProcedureDeclaration: "+identifier+", Declared in: "+this.declaredIn);
		Global.currentScope = this.declaredIn;
	}

}
