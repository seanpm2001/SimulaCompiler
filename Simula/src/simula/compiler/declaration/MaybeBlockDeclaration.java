/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simula.compiler.declaration;

import java.util.Vector;

import simula.compiler.JavaModule;
import simula.compiler.parsing.Parser;
import simula.compiler.statement.BlockStatement;
import simula.compiler.statement.Statement;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.Meaning;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;

/**
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 */
public final class MaybeBlockDeclaration extends BlockDeclaration {

	// ***********************************************************************************************
	// *** CONSTRUCTORS
	// ***********************************************************************************************
	// Used by parseMaybeBlock, i.e. CompoundStatement or SubBlock.
	public MaybeBlockDeclaration(final String identifier) {
		super(identifier);
	}

	// ***********************************************************************************************
	// *** createMaybeBlock
	// ***********************************************************************************************
	// Used by ProgramModule
	public static MaybeBlockDeclaration createMaybeBlock() {
		MaybeBlockDeclaration module = new MaybeBlockDeclaration(Global.sourceName+'$');
		module.isMainModule = true;
		module.blockKind = BlockKind.SimulaProgram;
		module.parseMaybeBlock();
		return (module);
	}

	// ***********************************************************************************************
	// *** Parsing: parseMaybeBlock
	// ***********************************************************************************************
	/**
	 * Parse CompoundStatement, SubBlock or PrefixedBlock.
	 * 
	 * <pre>
	 * Syntax:
	 * 
	 * Block = CompoundStatement | SubBlock
	 * 
	 *	 CompoundStatement = BEGIN [ { Statement ; } ] END
	 * 	 SubBlock = BEGIN [ { Declaration ; } ]  [ { Statement ; } ] END
	 *
	 * </pre>
	 * 
	 * @param blockPrefix
	 */
	public BlockStatement parseMaybeBlock() {
		Statement stm;
		// Debug.BREAK("BEGIN Block: "+this.edScopeChain());
		if (Option.TRACE_PARSE)
			Parser.TRACE("Parse MayBeBlock");
		while (Declaration.parseDeclaration(declarationList))
			Parser.accept(KeyWord.SEMICOLON);
		while (!Parser.accept(KeyWord.END)) {
			stm = Statement.doParse();
			// Util.BREAK("BlockDeclaration.parseMaybeBlock: stm="+stm);
			if (stm != null) statements.add(stm);
		}
		if (blockKind != BlockKind.SimulaProgram) {
			if (!declarationList.isEmpty()) {
				blockKind = BlockKind.SubBlock;
				modifyIdentifier("SubBlock" + lineNumber);
			} else {
				blockKind = BlockKind.CompoundStatement;
				modifyIdentifier("CompoundStatement" + lineNumber);
				if (!labelList.isEmpty())
					moveLabelsFrom(this); // Label is also declaration
			}
		}
		this.lastLineNumber = Global.sourceLineNumber;
		// declarationMap.print("END Block: "+blockName);
		// Debug.BREAK("END Block: "+this.edScopeChain());

		Global.currentScope = declaredIn;
		return (new BlockStatement(this));
	}

	public static void moveLabelsFrom(DeclarationScope block) {
		// Special case: Labels in a CompoundStatement or ConnectionBlock.
		// Move Label Declaration to nearest enclosing Block which is not
		// a CompoundStatement or ConnectionBlock.
		DeclarationScope declaredIn = block.declaredIn;
		Vector<LabelDeclaration> labelList = block.labelList;
		// if(DEBUG) Util.BREAK("BlockDeclaration.parseMaybeBlock:
		// declaredIn="+declaredIn);
		DeclarationScope enc = declaredIn;
		while (enc.blockKind == BlockKind.CompoundStatement
				&& enc.blockKind == BlockKind.ConnectionBlock
				&& enc.declarationList.isEmpty())
			enc = enc.declaredIn;
		// if(DEBUG) Util.BREAK("BlockDeclaration.parseMaybeBlock: Labels are moved to enc="+enc);
		for (LabelDeclaration lab : labelList) enc.labelList.add(lab);
		labelList.clear();
	}

	// ***********************************************************************************************
	// *** Checking
	// ***********************************************************************************************
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		Global.sourceLineNumber = lineNumber;
		if (externalIdent == null) externalIdent = edJavaClassName();

		if (blockKind != BlockKind.CompoundStatement) currentBlockLevel++;
		blockLevel = currentBlockLevel;
		// Util.BREAK("BlockDeclaration("+identifier+").doChecking: currentBlockLevel="+currentBlockLevel);
		// Util.BREAK("BlockDeclaration("+identifier+").doChecking: blockLevel="+blockLevel);
		// Util.BREAK("BlockDeclaration("+identifier+").doChecking: declaredIn="+declaredIn);
		Global.currentScope = this;

		// Util.BREAK("BlockDeclaration("+identifier+").doChecking: prefixLevel="+prfx);
		for (Declaration dcl : declarationList)	dcl.doChecking();
		for (Statement stm : statements) stm.doChecking();
		doCheckLabelList(null);
		Global.currentScope = declaredIn;
		if (blockKind != BlockKind.CompoundStatement) currentBlockLevel--;
		SET_SEMANTICS_CHECKED();
	}

	// ***********************************************************************************************
	// *** Utility: findVisibleAttributeMeaning
	// ***********************************************************************************************
	public Meaning findVisibleAttributeMeaning(final String ident) {
		// if(ident.equalsIgnoreCase("ln")) Util.BREAK("DeclarationScope("+identifier+").findVisibleAttributeMeaning("+ident+"): scope="+this);
		// if(ident.equalsIgnoreCase("ln")) Util.BREAK("DeclarationScope("+identifier+").findVisibleAttributeMeaning("+ident+"): declaredIn="+declaredIn);
		for (Declaration declaration : declarationList)
			if (ident.equalsIgnoreCase(declaration.identifier))
				return (new Meaning(declaration, this, this, false));
		for (LabelDeclaration label : labelList)
			if (ident.equalsIgnoreCase(label.identifier))
				return (new Meaning(label, this, this, false));
		return (null);
	}

	// ***********************************************************************************************
	// *** Coding: doJavaCoding
	// ***********************************************************************************************
	public void doJavaCoding() {
		// Util.BREAK("BlockDeclaration.doJavaCoding: "+identifier+", BlockDeclaration.Kind="+blockKind);
		ASSERT_SEMANTICS_CHECKED(this);
		if (this.isPreCompiled)	return;
		if (blockKind == BlockKind.CompoundStatement)
			 doCompoundStatementCoding();
		else doSubBlockCoding();
	}

	// ***********************************************************************************************
	// *** Coding: CompoundStatement as Java Subblock
	// ***********************************************************************************************
	private void doCompoundStatementCoding() {
		Global.sourceLineNumber = lineNumber;
		// Util.BREAK("BlockDeclaration.doSubBlockJavaCoding: "+identifier);
		ASSERT_SEMANTICS_CHECKED(this);
		Util.ASSERT(declarationList.isEmpty(), "Invariant");
		Util.ASSERT(labelList.isEmpty(), "Invariant");
		Global.currentScope = this;
		JavaModule.code("{");
		JavaModule.debug("TRACE_BEGIN_STM$(\"" + identifier + "\"," + Global.sourceLineNumber + ");");
		for (Statement stm : statements) stm.doJavaCoding();
		JavaModule.debug("TRACE_END_STM$(\"" + identifier + "\"," + Global.sourceLineNumber + ");");
		JavaModule.code("}");
		Global.currentScope = declaredIn;
	}

	// ***********************************************************************************************
	// *** Coding: SUBBLOCK ==> .java file
	// ***********************************************************************************************
	private void doSubBlockCoding() {
		Global.sourceLineNumber = lineNumber;
		// Util.BREAK("BlockDeclaration.doBlockJavaCoding: "+identifier);
		ASSERT_SEMANTICS_CHECKED(this);
		JavaModule javaModule = new JavaModule(this);
		Global.currentScope = this;
		JavaModule.code("@SuppressWarnings(\"unchecked\")");
		JavaModule.code("public final class " + getJavaIdentifier() + " extends BASICIO$" + " {");
		JavaModule.debug("// SubBlock: BlockKind=" + blockKind + ", BlockLevel=" + blockLevel + ", firstLine="
				+ lineNumber + ", lastLine=" + lastLineNumber + ", hasLocalClasses="
				+ ((hasLocalClasses) ? "true" : "false") + ", System=" + ((isQPSystemBlock()) ? "true" : "false"));
//		JavaModule.code("public int prefixLevel() { return(0); }");
		if (isQPSystemBlock())
			JavaModule.code("public boolean isQPSystemBlock() { return(true); }");
		if (!labelList.isEmpty()) {
			JavaModule.debug("// Declare local labels");
			for (Declaration decl : labelList) decl.doJavaCoding();
		}
		JavaModule.debug("// Declare locals as attributes");
		for (Declaration decl : declarationList) decl.doJavaCoding();
		doCodeConstructor();
		doCodeStatements();
		if (this.isMainModule) {
			JavaModule.code("");
			JavaModule.code("public static void main(String[] args) {");
			JavaModule.debug("//System.setProperty(\"file.encoding\",\"UTF-8\");");
			JavaModule.code("RT.setRuntimeOptions(args);");
			JavaModule.code("new " + getJavaIdentifier() + "(CTX$).STM$();");
			JavaModule.code("}", "End of main");
		}
		javaModule.codeProgramInfo();
		JavaModule.code("}", "End of SubBlock");
		Global.currentScope = declaredIn;
		javaModule.closeJavaOutput();
	}

	// ***********************************************************************************************
	// *** Coding Utility: doCodeConstructor
	// ***********************************************************************************************
	private void doCodeConstructor() {
		JavaModule.debug("// Normal Constructor");
		JavaModule.code("public " + getJavaIdentifier() + "(RTObject$ staticLink) {");
		JavaModule.code("super(staticLink);");
		JavaModule.code("BBLK();");
		if (blockKind == BlockKind.SimulaProgram) JavaModule.code("BPRG(\"" + identifier + "\");");
		JavaModule.debug("// Declaration Code");
		JavaModule.debug("TRACE_BEGIN_DCL$(\"" + identifier + "\"," + Global.sourceLineNumber + ");");
		for (Declaration decl : declarationList) decl.doDeclarationCoding();
		JavaModule.code("}");
	}

	// ***********************************************************************************************
	// *** Coding Utility: doCodeStatements
	// ***********************************************************************************************
	private void doCodeStatements() {
		JavaModule.debug("// " + blockKind + " Statements");
		JavaModule.code("public RTObject$ STM$() {");
		JavaModule.debug("TRACE_BEGIN_STM$(\"" + identifier + "\"," + Global.sourceLineNumber + ");");
		codeSTMBody();
		JavaModule.debug("TRACE_END_STM$(\"" + identifier + "\"," + Global.sourceLineNumber + ");");
		JavaModule.code("EBLK();");
		JavaModule.code("return(null);");
		JavaModule.code("}", "End of " + blockKind + " Statements");
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
		Util.println(s.toString());
		String beg = "begin[" + edScopeChain() + ']';
		Util.println(spc + beg);
		// if(!hiddenList.isEmpty()) Util.println(indent+" HIDDEN"+hiddenList);
		// if(!protectedList.isEmpty()) Util.println(indent+" PROTECTED"+protectedList);
		for (Declaration decl : declarationList) decl.print(indent + 1);
		for (Statement stm : statements) stm.print(indent + 1);
		Util.println(spc + "end[" + edScopeChain() + ']');
		// Util.BREAK("BlockDeclaration.print DONE");
	}

	public String toString() {
		return ("" + identifier + '[' + externalIdent + "] BlockKind=" + blockKind);
	}

}
