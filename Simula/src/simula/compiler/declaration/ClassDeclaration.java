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
import java.util.Iterator;
import java.util.Vector;

import simula.compiler.GeneratedJavaClass;
import simula.compiler.byteCodeEngineering.JavaClassInfo;
import simula.compiler.CodeLine;
import simula.compiler.parsing.Parse;
import simula.compiler.statement.InnerStatement;
import simula.compiler.statement.Statement;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.Meaning;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Type;
import simula.compiler.utilities.Util;

/**
 * Class Declaration.
 * <p>
 * Link to GitHub: <a href="https://github.com/portablesimula/SimulaCompiler/blob/master/Simula/src/simula/compiler/declaration/ClassDeclaration.java"><b>Source File</b></a>.
 * 
 * @author Øystein Myhre Andersen
 */
public class ClassDeclaration extends BlockDeclaration implements Externalizable {
	private String externalPrefixIdent;
	
	/**
	 * The parameter list. 
	 */
	Vector<Parameter> parameterList = new Vector<Parameter>();
	
	/**
	 * The virtual spec list. 
	 */
	protected Vector<VirtualSpecification> virtualSpecList = new Vector<VirtualSpecification>();
	
	/**
	 * The virtual match list. 
	 */
	protected Vector<VirtualMatch> virtualMatchList = new Vector<VirtualMatch>();
	
	/**
	 * The protected list. 
	 */
	Vector<ProtectedSpecification> protectedList = new Vector<ProtectedSpecification>();
	
	/**
	 * The hidden list. 
	 */
	Vector<HiddenSpecification> hiddenList = new Vector<HiddenSpecification>();
	
	/**
	 * Statement code before inner.
	 */
	protected Vector<CodeLine> code1; // Statement code before inner
	
	/**
	 * Statement code after inner.
	 */
	public Vector<CodeLine> code2;
	
	/**
	 * Class Prefix in case of a SubClass or Prefixed Block.
	 */
	public String prefix;

	/**
	 * Set true when attribute procedure 'detach' is used in/on this class.
	 */
	public boolean detachUsed = false;

	// ***********************************************************************************************
	// *** CONSTRUCTOR
	// ***********************************************************************************************
	/**
	 * Create a new ClassDeclaration.
	 * @param identifier the given identifier
	 */
	protected ClassDeclaration(String identifier) {
		super(identifier);
		this.declarationKind=Declaration.Kind.Class;
	}

	// ***********************************************************************************************
	// *** Parsing: doParseClassDeclaration
	// ***********************************************************************************************
	/**
	 * Class Declaration.
	 * 
	 * <pre>
	 * 
	 * Syntax:
	 * 
	 * ClassDeclaration = [ Prefix ] MainPart
	 * 
	 *	Prefix = ClassIdentifier
	 *
	 *    MainPart = CLASS ClassIdentifier  ClassHead  ClassBody
	 *		ClassIdentifier = Identifier
	 *
	 *		ClassHead = [ FormalParameterPart ; [ ValuePart ] SpecificationPart ] ;
	 *				    [ ProtectionPart ; ] [ VirtualPart ]
	 *
	 *			FormalParameterPart = "(" FormalParameter { , FormalParameter } ")"
	 *				FormalParameter = Identifier
	 *
	 *			ValuePart = VALUE IdentifierList
	 *
	 *			SpecificationPart = ClassParameterSpecifier  IdentifierList ; { ClassParameterSpecifier  IdentifierList ; }
	 *				ClassParameterSpecifier = Type | [Type] ARRAY 
	 *
	 *			ProtectionPart = ProtectionSpecification { ; ProtectionSpecification }
	 *				ProtectionSpecification = HIDDEN IdentifierList | HIDDEN PROTECTED IdentifierList
	 *										| PROTECTED IdentifierList | PROTECTED HIDDEN IdentifierList
	 *
	 *			VirtualPart = VIRTUAL: virtual-specification-part
	 *				VirtualSpecificationPart = VirtualSpecification ; { VirtualSpecification ; }
	 *					VirtualSpecification = VirtualSpecifier IdentifierList
	 *						VirtualSpecifier = [ type ] PROCEDURE | LABEL | SWITCH
	 *
	 *		ClassBody = SplitBody | Statement
	 *			SplitBody = BEGIN [ { Declaration ; } ]  [ { Statement ; } ] InnerPart  [ { Statement ; } ] 
	 *				InnerPart = [ Label : ] INNER ;
	 *
	 * </pre>
	 * 
	 * @param prefix class identifier
	 * @return the resulting ClassDeclaration
	 */
	public static ClassDeclaration doParseClassDeclaration(final String prefix) {
		ClassDeclaration block = new ClassDeclaration(null);
		block.lineNumber=Parse.prevToken.lineNumber;
		block.prefix = prefix;
		block.declaredIn.hasLocalClasses = true;
		if (block.prefix == null)
			block.prefix = StandardClass.SIMULA_BLOCK.identifier;
		block.modifyIdentifier(Parse.expectIdentifier());
		if (Parse.accept(KeyWord.BEGPAR)) {
			do { // ParameterPart = Parameter ; { Parameter ; }
				new Parameter(Parse.expectIdentifier()).into(block.parameterList);
			} while (Parse.accept(KeyWord.COMMA));
			Parse.expect(KeyWord.ENDPAR);
			Parse.expect(KeyWord.SEMICOLON);
			// ModePart = ValuePart [ NamePart ] | NamePart [ ValuePart ]
			// ValuePart = VALUE IdentifierList ;
			// NamePart = NAME IdentifierList ;
			if (Parse.accept(KeyWord.VALUE)) {
				expectModeList(block, block.parameterList, Parameter.Mode.value);
				Parse.expect(KeyWord.SEMICOLON);
			}
			// ParameterPart = Parameter ; { Parameter ; }
			// Parameter = Specifier IdentifierList
			// Specifier = Type [ ARRAY | PROCEDURE ] | LABEL | SWITCH
			while (acceptClassParameterSpecifications(block, block.parameterList)) {
				Parse.expect(KeyWord.SEMICOLON);
			}
		} else
			Parse.expect(KeyWord.SEMICOLON);

		// ProtectionPart = ProtectionParameter { ; ProtectionParameter }
		// ProtectionParameter = HIDDEN IdentifierList | HIDDEN PROTECTED IdentifierList
		// | PROTECTED IdentifierList | PROTECTED HIDDEN IdentifierList
		while (true) {
			if (Parse.accept(KeyWord.HIDDEN)) {
				if (Parse.accept(KeyWord.PROTECTED))
					expectHiddenProtectedList(block, true, true);
				else
					expectHiddenProtectedList(block, true, false);
			} else if (Parse.accept(KeyWord.PROTECTED)) {
				if (Parse.accept(KeyWord.HIDDEN))
					expectHiddenProtectedList(block, true, true);
				else
					expectHiddenProtectedList(block, false, true);
			} else
				break;
		}
		// VirtualPart = VIRTUAL: virtual-specification-part
		// VirtualParameterPart = VirtualParameter ; { VirtualParameter ; }
		// VirtualParameter = VirtualSpecifier IdentifierList
		// VirtualSpecifier = [ type ] PROCEDURE | LABEL | SWITCH
		if (Parse.accept(KeyWord.VIRTUAL))
			VirtualSpecification.parseInto(block);

		if (Parse.accept(KeyWord.BEGIN))
			doParseBody(block);
		else {
			block.statements.add(Statement.doParse());
			block.statements.add(new InnerStatement(Parse.currentToken.lineNumber)); // Implicit INNER
		}
		block.lastLineNumber = Global.sourceLineNumber;
		block.type = Type.Ref(block.identifier);
		if (Option.TRACE_PARSE)	Parse.TRACE("Line "+block.lineNumber+": ClassDeclaration: "+block);
		Global.setScope(block.declaredIn);
		return (block);
	}
	
	// ***********************************************************************************************
	// *** PARSING: expectModeList
	// ***********************************************************************************************
	private static void expectModeList(final BlockDeclaration block, final Vector<Parameter> parameterList,final Parameter.Mode mode) {
		do {
			String identifier = Parse.expectIdentifier();
			Parameter parameter = null;
			for (Parameter par : parameterList)
				if (Util.equals(identifier, par.identifier)) {
					parameter = par;
					break;
				}
			if (parameter == null) {
				Util.error("Identifier " + identifier + " is not defined in this scope");
				parameter = new Parameter(identifier);
			}
			parameter.setMode(mode);
		} while (Parse.accept(KeyWord.COMMA));
	}
	
	// ***********************************************************************************************
	// *** PARSING: acceptClassParameterSpecifications
	// ***********************************************************************************************
	private static boolean acceptClassParameterSpecifications(final BlockDeclaration block,final Vector<Parameter> parameterList) {
		// ClassParameter = ClassParameterSpecifier IdentifierList
		// ClassParameterSpecifier = Type | [Type] ARRAY 
		if (Option.TRACE_PARSE)
			Parse.TRACE("Parse ParameterSpecifications");
		Type type;
		Parameter.Kind kind = Parameter.Kind.Simple;
		type = Parse.acceptType();
		if (Parse.accept(KeyWord.ARRAY)) {
			if (type == null) {
				// See Simula Standard 5.2 -
				// If no type is given the type real is understood.
				type=Type.Real;
			}
			kind = Parameter.Kind.Array;
		}
		if (type == null) return (false);
		do {
			String identifier = Parse.expectIdentifier();
			Parameter parameter = null;
			for (Parameter par : parameterList)
				if (Util.equals(identifier, par.identifier)) { parameter = par; break; }
			if (parameter == null) {
				Util.error("Identifier " + identifier + " is not defined in this scope");
				parameter = new Parameter(identifier);
			}
			parameter.setTypeAndKind(type, kind);
		} while (Parse.accept(KeyWord.COMMA));
		return (true);
	}

	// ***********************************************************************************************
	// *** PARSING: expectHiddenProtectedList
	// ***********************************************************************************************
	private static boolean expectHiddenProtectedList(final ClassDeclaration block, final boolean hidden,final boolean prtected) {
		do {
			String identifier = Parse.expectIdentifier();
			if (hidden)	block.hiddenList.add(new HiddenSpecification(block, identifier));
			if (prtected) block.protectedList.add(new ProtectedSpecification(block, identifier));
		} while (Parse.accept(KeyWord.COMMA));
		Parse.expect(KeyWord.SEMICOLON);
		return (true);
	}

	// ***********************************************************************************************
	// *** PARSING: doParseBody
	// ***********************************************************************************************
	private static void doParseBody(final BlockDeclaration block) {
		Statement stm;
		if (Option.TRACE_PARSE)	Parse.TRACE("Parse Block");
		while (Declaration.parseDeclaration(block.declarationList)) {
			Parse.accept(KeyWord.SEMICOLON);
		}
		boolean seen=false;
		Vector<Statement> stmList = block.statements;
		while (!Parse.accept(KeyWord.END)) {
			stm = Statement.doParse();
			if (stm != null) stmList.add(stm);
			if (Parse.accept(KeyWord.INNER)) {
				if (seen) Util.error("Max one INNER per Block");
				else stmList.add(new InnerStatement(Parse.currentToken.lineNumber));
				seen = true;
			}
		}
		if (!seen) stmList.add(new InnerStatement(Parse.currentToken.lineNumber)); // Implicit INNER
	}

	// ***********************************************************************************************
	// *** Utility: isSubClassOf
	// ***********************************************************************************************
	/**
	 * Consider the class definitions:
	 * 
	 * <pre>
	 *  
	 *      Class A ......;
	 *    A Class B ......;
	 *    B Class C ......;
	 * </pre>
	 * 
	 * Then Class B is a subclass of Class A, While Class C is subclass of both B
	 * and A.
	 * 
	 * @param other the other ClassDeclaration
	 * @return Boolean true iff this class is a subclass of the 'other' class.
	 */
	public boolean isSubClassOf(final ClassDeclaration other) {
		ClassDeclaration prefixClass = getPrefixClass();
		if (prefixClass != null)
			do { if (other == prefixClass) return(true);
			} while ((prefixClass = prefixClass.getPrefixClass()) != null);
		return (false);
	}

	// ***********************************************************************************************
	// *** Checking
	// ***********************************************************************************************
	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		Global.sourceLineNumber = lineNumber;
		if (externalIdent == null) externalIdent = edJavaClassName();
		currentRTBlockLevel++;
		rtBlockLevel = currentRTBlockLevel;
		Global.enterScope(this);
		ClassDeclaration prefixClass = null;
		if (!hasNoRealPrefix()) {
			prefixClass = getPrefixClass();
			if (prefixClass.declarationKind != Declaration.Kind.StandardClass) {
				if (sourceBlockLevel != prefixClass.sourceBlockLevel)
					Util.warning("Subclass on a deeper block level not allowed.");
			}
		}
		int prfx = prefixLevel();
		for (Parameter par : this.parameterList) par.setExternalIdentifier(prfx);
		for (Declaration par : new ClassParameterIterator()) par.doChecking();
		for (VirtualSpecification vrt : virtualSpecList) vrt.doChecking();
		for (Declaration dcl : declarationList)	dcl.doChecking();
		for (Statement stm : statements) stm.doChecking();
		checkProtectedList();
		checkHiddenList();
		doCheckLabelList(prefixClass);
		Global.exitScope();
		currentRTBlockLevel--;
		SET_SEMANTICS_CHECKED();
		
		JavaClassInfo info=new JavaClassInfo();
		info.externalIdent=this.getJavaIdentifier();
		if(prefixClass!=null) {
			info.prefixIdent=externalPrefixIdent=prefixClass.getJavaIdentifier();
		}
		JavaClassInfo.put(info.externalIdent,info);
	}

	// ***********************************************************************************************
	// *** Utility: checkHiddenList
	// ***********************************************************************************************
	private void checkHiddenList() {
		for (HiddenSpecification hdn : hiddenList)
			hdn.doChecking();
	}

	// ***********************************************************************************************
	// *** Utility: checkProtectedList
	// ***********************************************************************************************
	private void checkProtectedList() {
		for (ProtectedSpecification pct : protectedList) {
			pct.doChecking();
		}
	}

	// ***********************************************************************************************
	// *** Utility: searchVirtualSpecList -- - Search VirtualSpec-list for 'ident'
	// ***********************************************************************************************
	/**
	 * Utility: Search VirtualSpec-list for 'ident'
	 * @param ident argument
	 * @return a VirtualSpecification when it was found, otherwise null
	 */
	VirtualSpecification searchVirtualSpecList(final String ident) {
		for (VirtualSpecification virtual : virtualSpecList) {
			if (Util.equals(ident, virtual.identifier)) return (virtual);
		} return (null);
	}

	// ***********************************************************************************************
	// *** Utility: prefixLevel
	// ***********************************************************************************************
	/**
	 * Returns the prefix level.
	 * @return the prefix level
	 */
	public int prefixLevel() {
		if (hasNoRealPrefix()) return (0);
		ClassDeclaration prfx = getPrefixClass();
		if (prfx != null)
			return (prfx.prefixLevel() + 1);
		return (-1);
	}

	// ***********************************************************************************************
	// *** Utility: getNlabels
	// ***********************************************************************************************
	/**
	 * Returns the number of labels in this class.
	 * @return the number of labels in this class
	 */
	int getNlabels() {
		if (hasNoRealPrefix())
			return (labelList.size());
		return (labelList.size() + getPrefixClass().getNlabels());
	}

	// ***********************************************************************************************
	// *** Utility: findLocalAttribute
	// ***********************************************************************************************
	/**
	 * Utility: Search for an attribute named 'ident'
	 * @param ident argument
	 * @return a ProcedureDeclaration when it was found, otherwise null
	 */
	Declaration findLocalAttribute(final String ident) {
		if(Option.TRACE_FIND_MEANING>0) Util.println("BEGIN Checking Class for "+ident+" ================================== "+identifier+" ==================================");
		for (Parameter parameter : parameterList) {
			if(Option.TRACE_FIND_MEANING>1) Util.println("Checking Parameter "+parameter);
			if (Util.equals(ident, parameter.identifier)) return (parameter);
		}
		for (Declaration declaration : declarationList) {
			if(Option.TRACE_FIND_MEANING>1) Util.println("Checking Local "+declaration);
			if (Util.equals(ident, declaration.identifier))	return (declaration);
		}
		for (LabelDeclaration label : labelList) {
			if(Option.TRACE_FIND_MEANING>1) Util.println("Checking Label "+label);
			if (Util.equals(ident, label.identifier)) return (label);
		}
		for (VirtualMatch match : virtualMatchList) {
			if(Option.TRACE_FIND_MEANING>1) Util.println("Checking Match "+match);
			if (Util.equals(ident, match.identifier)) return (match);
		}
		for (VirtualSpecification virtual : virtualSpecList) {
			if(Option.TRACE_FIND_MEANING>1) Util.println("Checking Virtual "+virtual);
			if (Util.equals(ident, virtual.identifier))	return (virtual);
		}
		if(Option.TRACE_FIND_MEANING>0) Util.println("ENDOF Checking Class for "+ident+" ================================== "+identifier+" ==================================");
		return (null);
	}

	// ***********************************************************************************************
	// *** Utility: findLocalProcedure
	// ***********************************************************************************************
	/**
	 * Utility: Search Declaration-list for a procedure named 'ident'
	 * @param ident argument
	 * @return a ProcedureDeclaration when it was found, otherwise null
	 */
	ProcedureDeclaration findLocalProcedure(final String ident) {
		for (Declaration decl : declarationList)
			if (Util.equals(ident, decl.identifier)) {
				if (decl instanceof ProcedureDeclaration proc) return (proc);
				else return (null);
			}
		return (null);
	}

	// ***********************************************************************************************
	// *** Utility: findRemoteAttributeMeaning
	// ***********************************************************************************************
	/**
	 * Find Remote Attribute's Meaning.
	 * 
	 * @param ident attribute identifier
	 * @return the resulting Meaning
	 */
	public Meaning findRemoteAttributeMeaning(final String ident) {
		boolean behindProtected = false;
		ClassDeclaration scope = this;
		Declaration decl = scope.findLocalAttribute(ident);
		if (decl != null) {
			boolean prtected = decl.isProtected != null;
			VirtualSpecification virtSpec = VirtualSpecification.getVirtualSpecification(decl);
			if (virtSpec != null && virtSpec.isProtected != null) prtected = true;
			if (!prtected) return(new Meaning(decl, this, scope, behindProtected));
		}
SEARCH: while (scope != null) {
			HiddenSpecification hdn = scope.searchHiddenList(ident);
			if (hdn != null) {
				scope = hdn.getScopeBehindHidden();
				behindProtected = true;
				continue SEARCH;
			}
			Declaration decl2 = scope.findLocalAttribute(ident);
			if (decl2 != null) {
				boolean prtected = decl2.isProtected != null;
				if (withinScope(scope)) prtected = false;
				if (!prtected) return(new Meaning(decl2, this, scope, behindProtected));
				behindProtected = true;
			}
			scope = scope.getPrefixClass();
		}
		return (null);
	}

	// ***********************************************************************************************
	// *** Utility: searchProtectedList - Search Protected-list for 'ident'
	// ***********************************************************************************************
	/**
	 * Utility: Search Protected-list for 'ident'
	 * @param ident argument
	 * @return a ProtectedSpecification when it was found, otherwise null
	 */
	ProtectedSpecification searchProtectedList(final String ident) {
		for (ProtectedSpecification pct : protectedList)
			if (Util.equals(ident, pct.identifier)) return (pct);
		return (null);
	}

	// ***********************************************************************************************
	// *** Utility: withinScope -- Used by findRemoteAttributeMeaning
	// ***********************************************************************************************
	private static boolean withinScope(final DeclarationScope otherScope) {
		DeclarationScope scope = Global.getCurrentScope();
		while (scope != null) {
			if (scope == otherScope) return (true);
			if (scope instanceof ClassDeclaration cls) {
				ClassDeclaration prfx = cls.getPrefixClass();
				while (prfx != null) {
					if (prfx == otherScope)	return (true);
					prfx = prfx.getPrefixClass();
				}
			}
			scope = scope.declaredIn;
		}
		return (false);
	}

	// ***********************************************************************************************
	// *** Utility: findVisibleAttributeMeaning
	// ***********************************************************************************************
	@Override
	public Meaning findVisibleAttributeMeaning(final String ident) {
		if(Option.TRACE_FIND_MEANING>0) Util.println("BEGIN Checking Class for "+ident+" ================================== "+identifier+" ==================================");
		boolean searchBehindHidden = false;
		ClassDeclaration scope = this;
		Declaration decl = scope.findLocalAttribute(ident);
		if (decl != null) {
			Meaning meaning = new Meaning(decl, this, scope, searchBehindHidden);
			return (meaning);
		}
		scope = scope.getPrefixClass();
SEARCH: while (scope != null) {
			HiddenSpecification hdn = scope.searchHiddenList(ident);
			if (hdn != null) {
				scope = hdn.getScopeBehindHidden();
				searchBehindHidden = true;
				continue SEARCH;
			}
			decl = scope.findLocalAttribute(ident);
			if (decl != null) {
				Meaning meaning = new Meaning(decl, this, scope, searchBehindHidden);
				return (meaning);
			}
			scope = scope.getPrefixClass();
		}
		if(Option.TRACE_FIND_MEANING>0) Util.println("ENDOF Checking Class for "+ident+" ================================== "+identifier+" ==================================");
		return (null);
	}

	// ***********************************************************************************************
	// *** Utility: searchHiddenList -- Search Hidden-list for 'ident'
	// ***********************************************************************************************
	/**
	 * Utility: Search Hidden-list for 'ident'
	 * @param ident argument
	 * @return a HiddenSpecification when it was found, otherwise null
	 */
	HiddenSpecification searchHiddenList(final String ident) {
		for (HiddenSpecification hdn : hiddenList)
			if (Util.equals(ident, hdn.identifier)) return (hdn);
		return (null);
	}

	// ***********************************************************************************************
	// *** Utility: getPrefixClass
	// ***********************************************************************************************
	/**
	 * Returns the prefix ClassDeclaration or null.
	 * @return the prefix ClassDeclaration or null
	 */
	public ClassDeclaration getPrefixClass() {
		if (prefix == null)	return (null);
		Meaning meaning = declaredIn.findMeaning(prefix);
		if (meaning == null) Util.error("Undefined prefix: " + prefix);
		Declaration decl = meaning.declaredAs;
		if (decl == this) {
			Util.error("Class prefix chain loops: "+identifier);
		}
		if (decl instanceof ClassDeclaration cls) return (cls);
		if (decl instanceof StandardClass scl) return (scl);
		Util.error("Prefix " + prefix + " is not a Class");
		return (null);
	}

	// ***********************************************************************************************
	// *** Coding Utility: hasNoRealPrefix
	// ***********************************************************************************************
	private boolean hasNoRealPrefix() {
		ClassDeclaration prfx = getPrefixClass();
		boolean noPrefix = true;
		if (prfx != null) {
			noPrefix = false;
			String prfxString = prfx.identifier;
			if (Util.equals(prfxString, "SIMULA_BLOCK")) noPrefix = true;
		}
		return (noPrefix);
	}

	// ***********************************************************************************************
	// *** Coding: isDetachUsed -- If the 'detach' attribute is used
	// ***********************************************************************************************
	//
	// COMMENT FROM Stein: Ta utgangspunkt i hvilke klasser man har kalt "detach"
	// i, altså kvalifikasjonen av de X som er brukt i "X.detach". Men da må man jo
	// også holde greie på hvilke slike som har forekommet i eksterne "moduler" (som
	// f.eks. SIMULATION), uten at det burde være problematisk.
	// ***********************************************************************************************
	/**
	 * Returns true if detach is called in/on this class.
	 * @return true if detach is called in/on this class
	 */
	public boolean isDetachUsed() {
		// TRAVERSER PREFIX LOOKING FOR (detachUsed==true)
		if (this.detachUsed) return (true);
		if (this instanceof ClassDeclaration) {
			ClassDeclaration prfx = ((ClassDeclaration) this).getPrefixClass();
			if (prfx != null) return (prfx.isDetachUsed());
		}
		return (false);
	}

	// ***********************************************************************************************
	// *** Utility: ClassParameterIterator - // Iterates through prefix-chain
	// ***********************************************************************************************
	/**
	 * Utility: ClassParameterIterator - Iterates through prefix-chain.
	 *
	 */
	public final class ClassParameterIterator implements Iterator<Parameter>, Iterable<Parameter> {
		private Iterator<Parameter> prefixIterator;
		private Iterator<Parameter> localIterator;

		/**
		 * Constructor
		 */
		public ClassParameterIterator() {
			ClassDeclaration prefix = getPrefixClass();
			if (prefix != null)	prefixIterator = prefix.parameterIterator();
			localIterator = parameterList.iterator();
		}

		@Override
		public boolean hasNext() {
			if (prefixIterator != null) {
				if (prefixIterator.hasNext()) return (true);
				prefixIterator = null;
			}
			return (localIterator.hasNext());
		}

		@Override
		public Parameter next() {
			if (!hasNext())	return (null);
			if (prefixIterator != null)	return (prefixIterator.next());
			return (localIterator.next());
		}

		@Override
		public Iterator<Parameter> iterator() {
			return (new ClassParameterIterator());
		}
	}

	/**
	 * Iterates through all class parameters.
	 * @return a ClassParameterIterator
	 */
	public Iterator<Parameter> parameterIterator() {
		return (new ClassParameterIterator());
	}

	// ***********************************************************************************************
	// *** Coding: doJavaCoding
	// ***********************************************************************************************
	@Override
	public void doJavaCoding() {
		ASSERT_SEMANTICS_CHECKED();
		if (this.isPreCompiled)	return;
		Global.sourceLineNumber = lineNumber;
		GeneratedJavaClass javaModule = new GeneratedJavaClass(this);
		Global.enterScope(this);
		GeneratedJavaClass.code("@SuppressWarnings(\"unchecked\")");
		String line = "public class " + getJavaIdentifier();
//		if (prefix != null)
			 line = line + " extends " + getPrefixClass().getJavaIdentifier();
//		else line = line + " extends _BASICIO";
		GeneratedJavaClass.code(line + " {");
		GeneratedJavaClass.debug("// ClassDeclaration: Kind=" + declarationKind + ", BlockLevel=" + rtBlockLevel + ", PrefixLevel="
					+ prefixLevel() + ", firstLine=" + lineNumber + ", lastLine=" + lastLineNumber + ", hasLocalClasses="
					+ ((hasLocalClasses) ? "true" : "false") + ", System=" + ((isQPSystemBlock()) ? "true" : "false")
					+ ", detachUsed=" + ((detachUsed) ? "true" : "false"));
		if (isQPSystemBlock())
			GeneratedJavaClass.code("public boolean isQPSystemBlock() { return(true); }");
		if (isDetachUsed())
			GeneratedJavaClass.code("public boolean isDetachUsed() { return(true); }");
		GeneratedJavaClass.debug("// Declare parameters as attributes");
		for (Parameter par : parameterList) {
			String tp = par.toJavaType();
			GeneratedJavaClass.code("public " + tp + ' ' + par.externalIdent + ';');
		}
		if (!labelList.isEmpty()) {
			GeneratedJavaClass.debug("// Declare local labels");
			for (Declaration decl : labelList) decl.doJavaCoding();
		}
		GeneratedJavaClass.debug("// Declare locals as attributes");
		for (Declaration decl : declarationList) decl.doJavaCoding();

		for (VirtualSpecification virtual : virtualSpecList) {
			if (!virtual.hasDefaultMatch) virtual.doJavaCoding();
		}
		for (VirtualMatch match : virtualMatchList)	match.doJavaCoding();
		doCodeConstructor();
		codeClassStatements();
		javaModule.codeProgramInfo();
		GeneratedJavaClass.code("}","End of Class");
		Global.exitScope();
		javaModule.closeJavaOutput();
	}

	// ***********************************************************************************************
	// *** Coding Utility: doCodeConstructor
	// ***********************************************************************************************
	/**
	 * Coding utility: Code the constructor.
	 */
	private void doCodeConstructor() {
		GeneratedJavaClass.debug("// Normal Constructor");
		GeneratedJavaClass.code("public " + getJavaIdentifier() + edFormalParameterList());
		if (prefix != null) {
			ClassDeclaration prefixClass = this.getPrefixClass();
			GeneratedJavaClass.code("super" + prefixClass.edCompleteParameterList());
		} else
			GeneratedJavaClass.code("super(staticLink);");
		GeneratedJavaClass.debug("// Parameter assignment to locals");
		for (Parameter par : parameterList)
			GeneratedJavaClass.code("this." + par.externalIdent + " = s" + par.externalIdent + ';');

		if (hasNoRealPrefix()) GeneratedJavaClass.code("BBLK(); // Iff no prefix");

		GeneratedJavaClass.debug("// Declaration Code");
		for (Declaration decl : declarationList) decl.doDeclarationCoding();
		GeneratedJavaClass.code("}");
	}

	// ***********************************************************************************************
	// *** Coding Utility: edFormalParameterList
	// ***********************************************************************************************
	/**
	 * Edit the formal parameter list
	 * <p>
	 * Also used by subclass StandardProcedure.
	 * @return the resulting Java code
	 */
	protected String edFormalParameterList() {
		// Accumulates through prefix-chain when class
		StringBuilder s = new StringBuilder();
		s.append('(');
		s.append("_RTObject staticLink");
		for (Declaration par : new ClassParameterIterator()) {
			// Iterates through prefix-chain
			s.append(',');
			s.append(((Parameter) par).toJavaType());
			s.append(' ');
			s.append('s').append(par.externalIdent); // s to indicate Specified Parameter
		}
		s.append(") {");
		return (s.toString());
	}

	// ***********************************************************************************************
	// *** Coding Utility: hasLabel
	// ***********************************************************************************************
	@Override
	protected boolean hasLabel() {
		if (!labelList.isEmpty()) return (true);
		if (!this.hasNoRealPrefix()) {
			ClassDeclaration prfx = this.getPrefixClass();
			if (prfx != null) return (prfx.hasLabel());
		}
		return (false);
	}

	// ***********************************************************************************************
	// *** Coding Utility: saveClassStms
	// ***********************************************************************************************
	private void saveClassStms() {
		if (code1 == null) {
			code1 = new Vector<CodeLine>();
			Global.currentJavaModule.saveCode = code1;
			for (Statement stm : statements) stm.doJavaCoding();
			Global.currentJavaModule.saveCode = null;
		}
	}

	// ***********************************************************************************************
	// *** Coding Utility: codeStatements
	// ***********************************************************************************************
	@Override
	public void codeStatements() {
		writeCode1(); // Write code before inner
		writeCode2(); // Write code after inner
		// listSavedCode();
	}

	// ***********************************************************************************************
	// *** Coding Utility: writeCode1 -- Write code before inner
	// ***********************************************************************************************
	/**
	 * Coding utility: writeCode1 -- Write code before inner
	 */
	protected void writeCode1() {
		if (!this.hasNoRealPrefix()) {
			ClassDeclaration prfx = this.getPrefixClass();
			if (prfx != null) prfx.writeCode1();
		}
		saveClassStms();
		String comment=(code2 != null && code2.size()>0)?"Code before inner":"Code";
		GeneratedJavaClass.debug("// Class " + this.identifier + ": "+comment);
		for (CodeLine c : code1) GeneratedJavaClass.code(c);
	}

	// ***********************************************************************************************
	// *** Coding Utility: writeCode2 -- Write code after inner
	// ***********************************************************************************************
	/**
	 * Coding utility: writeCode2 -- Write code after inner
	 */
	protected void writeCode2() {
		if (code2 != null && code2.size()>0) {
			GeneratedJavaClass.debug("// Class " + this.identifier + ": Code after inner");
			for (CodeLine c : code2) GeneratedJavaClass.code(c);
		}
		if (!this.hasNoRealPrefix()) {
			ClassDeclaration prfx = this.getPrefixClass();
			if (prfx != null) prfx.writeCode2();
		}
	}


	// ***********************************************************************************************
	// *** Coding Utility: listSavedCode
	// ***********************************************************************************************
//	protected void listSavedCode() {
//		System.out.println("ClassDeclaration.listSavedCode: Class " + identifier);
//		for (CodeLine c : code1) {
//			System.out.println("Line: " + c.sourceLineNumber + " " + c.codeLine);
//		}
//		if (code2 == null)
//			System.out.println("ClassDeclaration.listSavedCode: Class " + identifier + " NO CODE After inner");
//		else {
//			System.out.println("ClassDeclaration.listSavedCode: Class " + identifier + " After inner");
//			for (CodeLine c : code2) {
//				System.out.println("Line: " + c.sourceLineNumber + " " + c.codeLine);
//			}
//		}
//	}

	// ***********************************************************************************************
	// *** Coding Utility: codeClassStatements
	// ***********************************************************************************************
	/**
	 * Coding utility: Code class statements.
	 */
	protected void codeClassStatements() {
		boolean duringSTM_Coding=Global.duringSTM_Coding;
		Global.duringSTM_Coding=true;
		GeneratedJavaClass.debug("// Class Statements");
		GeneratedJavaClass.code("public "+getJavaIdentifier()+" _STM() {");
		codeSTMBody();
		GeneratedJavaClass.code("EBLK();");
		GeneratedJavaClass.code("return(this);");
		GeneratedJavaClass.code("}","End of Class Statements");
		Global.duringSTM_Coding=duringSTM_Coding;
	}

	// ***********************************************************************************************
	// *** Coding Utility: edCompleteParameterList
	// ***********************************************************************************************
	/**
	 * Coding Utility: Edit the complete parameter list including all prefixes.
	 * @return the resulting Java code
	 */
	protected String edCompleteParameterList() {
		StringBuilder s = new StringBuilder();
		s.append("(staticLink");
		for (Parameter par : new ClassParameterIterator()) // Iterates through prefix-chain
			s.append(",s").append(par.externalIdent); // s to indicate Specified Parameter
		s.append(");");
		return (s.toString());
	}

	// ***********************************************************************************************
	// *** Printing Utility: print
	// ***********************************************************************************************
	@Override
	public void print(final int indent) {
    	String spc=edIndent(indent);
		StringBuilder s = new StringBuilder(spc);
		s.append('[').append(sourceBlockLevel).append(':').append(rtBlockLevel).append("] ");
		if (prefix != null)	s.append(prefix).append(' ');
		s.append(declarationKind).append(' ').append(identifier);
		s.append('[').append(externalIdent).append("] ");
		s.append(Parameter.editParameterList(parameterList));
		Util.println(s.toString());
		if (!virtualSpecList.isEmpty())	Util.println(spc + "    VIRTUAL-SPEC" + virtualSpecList);
		if (!virtualMatchList.isEmpty()) Util.println(spc + "    VIRTUAL-MATCH" + virtualMatchList);
		if (!hiddenList.isEmpty()) Util.println(spc + "    HIDDEN" + hiddenList);
		if (!protectedList.isEmpty()) Util.println(spc + "    PROTECTED" + protectedList);
		String beg = "begin[" + edScopeChain() + ']';
		Util.println(spc + beg);
		for (Declaration decl : declarationList) decl.print(indent + 1);
		for (Statement stm : statements) stm.print(indent + 1);
		Util.println(spc + "end[" + edScopeChain() + ']');
	}

	@Override
	public String toString() {
		return ("" + identifier + '[' + externalIdent + "] DeclarationKind=" + declarationKind);
	}


	// ***********************************************************************************************
	// *** Externalization
	// ***********************************************************************************************
	/**
	 * Default constructor used by Externalization.
	 */
	public ClassDeclaration() {
		super(null);
	}

	@Override
	public void writeExternal(ObjectOutput oupt) throws IOException {
		Util.TRACE_OUTPUT("BEGIN Write ClassDeclaration: "+identifier);
		oupt.writeObject(identifier);
		oupt.writeObject(externalIdent);
		oupt.writeObject(type);
		oupt.writeInt(rtBlockLevel);
		oupt.writeObject(prefix);
		oupt.writeBoolean(hasLocalClasses);
		oupt.writeBoolean(detachUsed);
		
		oupt.writeObject(parameterList);
		oupt.writeObject(virtualSpecList);
		oupt.writeObject(hiddenList);
		oupt.writeObject(protectedList);
		oupt.writeObject(labelList);
		oupt.writeObject(declarationList);
//		oupt.writeObject(virtualMatchList);
		oupt.writeObject(code1);
		oupt.writeObject(code2);
		oupt.writeObject(externalPrefixIdent);

		Util.TRACE_OUTPUT("END Write ClassDeclaration: "+identifier);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void readExternal(ObjectInput inpt) throws IOException, ClassNotFoundException {
		declarationKind=Declaration.Kind.Class;
		Util.TRACE_INPUT("BEGIN Read ClassDeclaration: "+identifier+", Declared in: "+this.declaredIn);
		identifier=(String)inpt.readObject();
		externalIdent=(String)inpt.readObject();
		type=Type.inType(inpt);
		rtBlockLevel=inpt.readInt();
		prefix=(String)inpt.readObject();
		hasLocalClasses=inpt.readBoolean();
		detachUsed=inpt.readBoolean();
		
		parameterList=(Vector<Parameter>) inpt.readObject();
		virtualSpecList=(Vector<VirtualSpecification>) inpt.readObject();
		hiddenList=(Vector<HiddenSpecification>) inpt.readObject();
		protectedList=(Vector<ProtectedSpecification>) inpt.readObject();
		labelList=(Vector<LabelDeclaration>) inpt.readObject();
		declarationList=(DeclarationList) inpt.readObject();
//		virtualMatchList=(Vector<VirtualMatch>) inpt.readObject();
		code1=(Vector<CodeLine>) inpt.readObject();
		code2=(Vector<CodeLine>) inpt.readObject();
		externalPrefixIdent=(String) inpt.readObject();
		Util.TRACE_INPUT("END Read ClassDeclaration: "+identifier+", Declared in: "+this.declaredIn);
		Global.setScope(this.declaredIn);
		
		JavaClassInfo info=new JavaClassInfo();
		info.externalIdent=this.getJavaIdentifier();
		info.prefixIdent=externalPrefixIdent;
		JavaClassInfo.put(info.externalIdent,info);
	}

}
