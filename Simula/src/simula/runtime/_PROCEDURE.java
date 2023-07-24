/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simula.runtime;


/**
* Utility class _PROCEDURE.
* <p>
* This class is used as prefix to all java classes representing Simula Procedures.
* 
* @author Øystein Myhre Andersen
*/
public abstract class _PROCEDURE extends _BASICIO {
	
	/**
	 * Number of actual parameters left to be transmitted.
	 */
	protected int _nParLeft;

	/**
	 * Create a normal procedure.
	 * 
	 * @param SL staticLink
	 */
	public _PROCEDURE(final _RTObject SL) {
		super(SL);
	}

	/**
	 * Create a formal/virtual Procedure.
	 * @param SL staticLink
	 * @param nPar number of actual parameters
	 */
	public _PROCEDURE(final _RTObject SL, int nPar) {
		super(SL);
		this._nParLeft = nPar;
	}

	/**
	 * Sets next procedure parameter value.
	 * <p>
	 * This method is virtual and must be redefined in all procedure blocks with
	 * parameters.
	 * 
	 * @param obj Procedure's Parameter Value Object
	 * @return this object
	 */
	public _PROCEDURE setPar(final Object obj) {
		return (this);
	}

	/**
	 * Deliver the result from the Simula procedure.
	 * <p>
	 * This method is virtual and must be redefined in all type procedure blocks
	 * 
	 * @return the result from the Simula procedure.
	 */
	public Object _RESULT() {
		return (null);
	}

	/**
	 * Enter formal procedure
	 * @return this class instance
	 * @throws _SimulaRuntimeError if too few parameters
	 */
	public _PROCEDURE _ENT() {
		// Enter Formal Procedure
		if (_nParLeft != 0)
			throw new _SimulaRuntimeError("Too few parameters");
		BBLK();
		_STM();
		return (this);
	}

}