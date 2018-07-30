/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simula.compiler.statement;

import simula.compiler.expression.Expression;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Type;
import simula.compiler.utilities.Util;

/**
 * Goto Statement.
 * 
 * <pre>
 * 
 * Syntax:
 * 
 *  GotoStatement = GOTO DesignationalExpression
 *                | GO TO DesignationalExpression
 *
 * </pre>
 * 
 * @author Øystein Myhre Andersen
 */
public class GotoStatement extends Statement {
	private Expression label;

	public GotoStatement() {
		label = Expression.parseExpression();
	}

	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())
			return;
		label.doChecking();
		label.backLink = this; // To ensure $result from functions
		SET_SEMANTICS_CHECKED();
	}

	public void doJavaCoding(int indent) {
		Global.sourceLineNumber = lineNumber;
		//Util.BREAK("GotoStatement.doJavaCoding: label=" + label + ", qual=" + label.getClass().getSimpleName());
		ASSERT_SEMANTICS_CHECKED(this);
		if (Option.standardClass) {
			Util.code(indent,"GOTO(" + label + "); // GOTO QUASI LABEL");
			return;
		}
		Type type = label.type;
		Util.ASSERT(type == Type.Label, "Invariant");
		//Util.BREAK("GotoStatement.doJavaCoding: label="+label+", QUAL="+label.getClass().getSimpleName());
		Util.code(indent,"GOTO(" + label.toJavaCode() + "); // GOTO EVALUATED LABEL");
	}

	public String toString() {
		return ("GOTO " + label);
	}

}
