/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simula.runtime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The class DEC_Lib. A set of utility procedures from DEC Handbook.
 * <p>
 * Link to GitHub: <a href="https://github.com/portablesimula/SimulaCompiler/blob/master/Simula/src/simula/runtime/_DEC_Lib.java"><b>Source File</b></a>.
 * 
 * @author Øystein Myhre Andersen
 *
 */
public class _DEC_Lib extends _SIMULA_BLOCK {

	private static _DEC_Lib DECLIB;

	// Constructor
	/**
	 * Normal Constructor
	 * @param staticLink static link
	 */
	public _DEC_Lib(final _RTObject staticLink) {
		super(staticLink);
		DECLIB = this;
		BBLK(); // Iff no prefix
	}

	@Override
	public _DEC_Lib _STM() {
		EBLK();
		return (this);
	}

	/** DEC_Lib Procedure abort.
	 * <pre>
	 * procedure abort(message); ! Stop execution. ;
	 *
	 * Abort will print the message on the error device and then call
	 * terminate_program.
	 * </pre>
	 * 
	 * @param mess a descriptive message
	 */
	public static void abort(_TXT mess) {
		_RT.println(mess.edText());
		_RTObject.terminate_program();
	}

	/** DEC_Lib Procedure change.
	 * <pre>
	 * boolean procedure change(master,oldtext,newtext);   ! Replacing Subtexts. ;
	 *
	 * Starting at master.pos, the procedure change will search for the subtext oldtext.
	 * 
	 * If not found, master remains unchanged and the procedure value is false.
	 * The position indicator is set to length+1, i.e. master.setpos(0);
	 * 
	 * If found, oldtext will be replaced by newtext and the procedure value is true.
	 * If oldtext.length >= newtext.length then master will denote a subtext of the original master text,
	 * otherwise master will denote a new text object.
	 * The position indicator is set to first character following newtext within the resulting master.
	 * 
	 * Changing all occurrences of oldtext to newtext may be done with the following procedure;
	 *
	 *          procedure changeAll(master,oldtext,newtext); name master; text master,oldtext,newtext;
	 *          begin text local;
	 *             local:- master;
	 *             while local.more do change(local,oldtext,newtext);
	 *             master:- local
	 *          end;
	 *         
	 * The position indicator 'pos' of master is set to first character after the substituted text.
	 * If not found, 'pos' is unaltered.
	 *
	 *    boolean procedure change(master,oldtext,newtext); name master; text master,oldtext,newtext; begin
	 *        text local; integer p;
	 *        local:-master;
	 *        p:=search(local,oldtext);
	 *        if p &lt;= local.length then begin
	 *            change:=true;
	 *            if oldtext.length>=newtext.length then begin
	 *                local.sub(p,newtext.length):=newtext;
	 *                if oldtext.length>newtext.length then begin
	 *                    from(local,p+newtext.length):=from(local,p+oldtext.length);
	 *                    local:-local.sub(1,local.length-oldtext.length+newtext.length)
	 *                end
	 *            end else local:-local.sub(1,p-1) &amp; newtext &amp; from(local,p+oldtext.length);
	 *            local.setpos(p+newtext.length);
	 *            master:-local
	 *        end else master.setpos(0)
	 *    end;
	 * 
	 * </pre>
	 * 
	 * @param master argument
	 * @param oldtext argument
	 * @param newtext argument
	 * @return false: no change has occurred
	 */
	public static boolean change(_NAME<_TXT> master, _TXT oldtext, _TXT newtext) {
		String oldt = oldtext.edText();
		String newt = newtext.edText();
		_TXT Master = master.get();
		String M = Master.edText();
		if (M.indexOf(oldt) < 0) {
			_TXT.setpos(Master, 0);
			return (false);
		}

		String head = M.substring(0, Master.POS + 1);
		String tail = M.substring(Master.POS + 1);

		int P = head.length() + tail.indexOf(oldt);
		String newTail = tail.replaceFirst(oldt, newt);
		String newMaster = head + newTail;

		int diff = newMaster.length() - M.length();
		if (diff > 0) {
			Master = new _TXT(newMaster);
			Master.POS = P + newt.length();
			master.put(Master);
		} else {
			_TXT subt = _TXT.sub(Master, 1, newMaster.length());
			DECLIB._ASGSTR(subt, newMaster);
			Master.POS = P + newt.length();
			Master.LENGTH = Master.LENGTH + diff; // Note: diff is negative
			master.put(Master);
		}
		return (true);
	}

	/** DEC_Lib Procedure checkextension.
	 * <pre>
	 * text procedure checkextension(fileName,defaultextension);
	 *
	 * The procedure checkextension may be used to add a default extension to
	 * file specifications not containing a dot ('.').  I.e.
	 *   fileName:- copy("myFile"); fileName:- checkextension(fileName,".txt");
	 *         will give fileName the value "myFile.txt", while
	 *   fileName:- copy("myFile.doc"); fileName:- checkextension(fileName,".txt");
	 *         will leave fileName unaltered.
	 * 
	 * </pre>
	 * 
	 * @param fileName argument
	 * @param defaultextension argument
	 * @return the resulting text
	 */
	public static _TXT checkextension(_TXT fileName, _TXT defaultextension) {
		String f = fileName.edText();
		if (f.indexOf('.') >= 0)
			return (fileName);
		return (new _TXT(f + defaultextension.edText()));
	}

	/**
	 * DEC_Lib Procedure checkint.
	 * <pre>
	 * &lt;type> procedure check&lt;type>(t); name t; text t; where &lt;type> ::= real | int | frac
	 * </pre>
	 * check&lt;type> analyses the text t from t.pos and on. If a get&lt;Number> operation
	 * from this position is legal the returned value is +1.
	 * <p>
	 * If it would give an error - then if the remaining text string is blank, the
	 * result is 0, else -1.
	 * <p>
	 * pos is placed after a legal item (+1), after the first nonblank illegal
	 * character (-1) or after the text if the rest is empty (0).
	 * <p>
	 * NOTE: The current implementation will place t.pos after a complete real item,
	 * even if it overflows. Real underflow produces +0, overflow Infinity, both of
	 * which are treated as legal results
	 * 
	 * @param t name reference to argument text
	 * @return resulting code +1:OK else error
	 */
	public static int checkint(_NAME<_TXT> t) {
		NumberChecker<Number> intChecker = DECLIB.new NumberChecker<Number>() {
			@Override
			public Integer check(_TXT T) {
				return (_TXT.getint(T));
			}
		};
		return (checkNumber(t, intChecker));
	}

	/**
	 * DEC_Lib Procedure checkfrac.
	 * See <b>{@link _DEC_Lib#checkint(_NAME<_TXT>)}</b>
	 * @param t name reference to argument text
	 * @return resulting code +1:OK else error
	 */
	public static int checkfrac(_NAME<_TXT> t) {
		NumberChecker<Number> fracChecker = DECLIB.new NumberChecker<Number>() {
			@Override
			public Integer check(_TXT T) {
				return (_TXT.getfrac(T));
			}
		};
		return (checkNumber(t, fracChecker));
	}

	/**
	 * DEC_Lib Procedure checkrealc.
	 * See <b>{@link _DEC_Lib#checkint(_NAME<_TXT>)}</b>
	 * @param t name reference to argument text
	 * @return resulting code +1:OK else error
	 */
	public static int checkreal(_NAME<_TXT> t) {
		NumberChecker<Number> realChecker = DECLIB.new NumberChecker<Number>() {
			@Override
			public Double check(_TXT T) {
				return (_TXT.getreal(T));
			}
		};
		return (checkNumber(t, realChecker));
	}

	private abstract class NumberChecker<T> {
		public abstract T check(_TXT T) throws Exception;
	}

	private static int checkNumber(_NAME<_TXT> t, NumberChecker<Number> numberChecker) {
		_TXT NT = t.get();
		_TXT U = _TXT.sub(NT, NT.POS + 1, NT.LENGTH - NT.POS);
		if (U != null) {
			try {
				numberChecker.check(U);
				NT.POS = U.POS + NT.POS;
				t.put(NT);
				return (1);
			} catch (Exception e) {
			}
		}
		String s = U.edText();
		int N = s.stripLeading().length();
		if (N == 0) {
			U.POS = U.START + U.LENGTH;
			return (0);
		}
		U.POS = U.LENGTH - N;
		t.put(U);
		return (-1);
	}

	/** DEC_Lib Procedure compress.
	 * <pre>
	 * text procedure compress(t,c); text t; character c;
	 * 
	 * Removes all occurrences of the character c in the text t.
	 *
	 * The procedure compress returns notext (if t contains no other characters than
	 * a number of characters = c), or a reference to an initial subtext of t
	 * (altered) which contains all characters of t not = c. The part of t after
	 * this subtext is unchanged.
	 * 
	 * Example:  t1:-copy("AxBxCxDx");  t2:-compress(t1,'x');
	 *     gives t1="ABCDCxDx", t2==t1.sub(1,4), t2="ABCD".
	 * 
	 * </pre>
	 * 
	 * @param t argument text
	 * @param c search character
	 * @return the resulting text
	 */
	public static _TXT compress(_TXT t, char c) {
		if (t == null)
			return (null);
		String tin = t.edText();
		int tinLen = t.LENGTH;
		int pOut = 1;
		for (int p = 0; p < tinLen; p++) {
			char tmpCh = tin.charAt(p);
			if (tmpCh != c) {
				depchar(t, pOut, tmpCh);
				pOut++;
			}
		}
		if (pOut == 0)
			return (null);
		return (_TXT.sub(t, 1, pOut - 1));
	}

	/**
	 * DEC_Lib Procedure conc.
	 * <pre>
	 * text procedure conc(t1,t2); text t1,t2;
	 * text procedure conc2(t1,t2); text t1,t2;
	 * text procedure conc3(t1,t2,t3); text t1,t2,t3;
	 * text procedure conc4(t1,t2,t3,t4); text t1,t2,t3,t4;
	 * text procedure conc5(t1,t2,t3,t4,t5); text t1,t2,t3,t4,t5;
	 * 
	 * Concatenate texts. Provided for compatibility only, use the text
	 * concatenation operator &amp; instead!
	 * </pre>
	 * 
	 * @param t1 argument t1
	 * @param t2 argument t2
	 * @return the concatenated text
	 */
	public static _TXT conc(_TXT t1, _TXT t2) {
		return (new _TXT(t1.edText() + t2.edText()));
	}
	
	/**
	 * DEC_Lib Procedure conc2.
	 * See <b>{@link _DEC_Lib#conc(_TXT, _TXT)}</b>
	 * @param t1 argument t1
	 * @param t2 argument t2
	 * @return the concatenated text
	 */
	public static _TXT conc2(_TXT t1, _TXT t2) {
		return (new _TXT(t1.edText() + t2.edText()));
	}

	/**
	 * DEC_Lib Procedure conc3.
	 * See <b>{@link _DEC_Lib#conc(_TXT, _TXT)}</b>
	 * @param t1 argument t1
	 * @param t2 argument t2
	 * @param t3 argument t3
	 * @return the concatenated text
	 */
	public static _TXT conc3(_TXT t1, _TXT t2, _TXT t3) {
		return (new _TXT(t1.edText() + t2.edText() + t3.edText()));
	}

	/**
	 * DEC_Lib Procedure conc4.
	 * See <b>{@link _DEC_Lib#conc(_TXT, _TXT)}</b>
	 * @param t1 argument t1
	 * @param t2 argument t2
	 * @param t3 argument t3
	 * @param t4 argument t4
	 * @return the concatenated text
	 */
	public static _TXT conc4(_TXT t1, _TXT t2, _TXT t3, _TXT t4) {
		return (new _TXT(t1.edText() + t2.edText() + t3.edText() + t4.edText()));
	}

	/**
	 * DEC_Lib Procedure conc5.
	 * See <b>{@link _DEC_Lib#conc(_TXT, _TXT)}</b>
	 * @param t1 argument t1
	 * @param t2 argument t2
	 * @param t3 argument t3
	 * @param t4 argument t4
	 * @param t5 argument t5
	 * @return the concatenated text
	 */
	public static _TXT conc5(_TXT t1, _TXT t2, _TXT t3, _TXT t4, _TXT t5) {
		return (new _TXT(t1.edText() + t2.edText() + t3.edText() + t4.edText() + t5.edText()));
	}

	/** DEC_Lib Procedure cptime.
	 * <pre>
	 * real procedure cptime;
	 *
	 * Returns total CPU time spent since the beginning of the SIMULA program
	 * execution, expressed in seconds. Provided for compatibility, use cputime
	 * instead!
	 *
	 * real procedure cptime; begin cptime:= cputime; end;
	 * </pre>
	 *
	 * @return seconds since start of program
	 */
	public static double cptime() {
		return (cputime());
	}

	/** DEC_Lib Procedure datno-
	 * <pre>
	 * integer procedure dayno;
	 * </pre>
	 * 
	 * @return the day number in current month.
	 */
	public static int dayno() {
		return (_TXT.getint(_TXT.sub(datetime(), 9, 2)));
	}

	/** DEC_Lib Procedure daytime.
	 * <pre>
	 * text procedure daytime;
	 *
	 * Returns a reference to a new text of length 8 with
	 * contents: "hh:mm:ss"
	 * 
	 *         where hh        is hours
	 *               mm        is minutes
	 *               ss        is seconds.
	 *         at the time of the call.
	 * </pre>
	 * 
	 * @return the resulting text
	 */
	public static _TXT daytime() {
		return (_TXT.sub(datetime(), 12, 8));
	}

	/** DEC_Lib Procedure depchar.
	 * <pre>
	 * procedure depchar(t,p,c); text t; integer p; character c;
	 *
	 * Deposits the character c in the text t at position p. If p is out of range,
	 * no action will be taken;
	 * 
	 * This is a safe version of S-port standard procedure StoreChar.
	 *
	 *    procedure depchar(t,p,c); text t; integer p; character c;
	 *    if p>0 and p&lt;=t.length
	 *    then t.sub(p,1).putchar(c);
	 * 
	 * </pre>
	 * 
	 * @param t a text reference
	 * @param p the text position in which the character is stored 
	 * @param c a character
	 */
	public static void depchar(_TXT t, int p, char c) {
		if (p > 0 & p <= t.LENGTH) {
			t.POS = p - 1;
			_TXT.putchar(t, c);
		}
	}

	/** DEC_Lib Procedure enterdebug
	 * <pre>
	 * procedure enterdebug(maycontinue); boolean maycontinue; ! Enter SIMOB ;
	 *
	 * Enterdebug will activate SIMOB (if present). After SIMOB session, the
	 * execution will either continue (maycontinue true) or it will terminate
	 * (maycontinue false).
	 *
	 *    procedure enterdebug(maycontinue); boolean maycontinue;
	 *    begin  ... enter SIMOB
	 *           if not maycontinue then terminate_program;
	 *    end;
	 * </pre>
	 * 
	 * @param maycontinue true: the program may continue after SIMOB session
	 */
	public static void enterdebug(boolean maycontinue) {
//		if(SIMOB.isPresent) _RT.NOT_IMPLEMENTED("DECLIB: enterdebug");
		if (!maycontinue)
			terminate_program();
	}

	/** DEC_Lib Procedure exit.
	 * <pre>
	 * procedure exit(n); integer n;
	 *
	 *   n=0: Terminate program immediately, with no further action.
	 *        No files closed.
	 *   n=1: Terminate program as if final end was reached, i.e. all
	 *        open files are closed (and a message given for each).
	 *   n=2: Equivalent to enterdebug(true).
	 *
	 *   Other values of n are reserved for extensions.
	 *
	 *    procedure exit(n); integer n;
	 *    begin
	 *          if n=0 then ... stop execution;
	 *          if n=1 then terminate_program;
	 *          if n=2 then enterdebug(true)
	 *          else begin
	 *             outtext("Parameter to exit out of range (0,2)");
	 *             outimage;
	 *             terminate_program;
	 *          end;
	 *    end;
	 * </pre>
	 * 
	 * @param code integer value
	 */
	public static void exit(int code) {
		switch (code) {
		case 0:
			System.exit(0);
		case 1:
			terminate_program();
		case 2:
			enterdebug(true);
			terminate_program();
		default:
			_RT.println("Parameter to exit out of range (0,2)");
			terminate_program();
		}
	}

	/** DEC_Lib Procedure fetchar.
	 * <pre>
	 * character procedure fetchar(t,p); text t; integer p;
	 *
	 * Returns the p'th character from t, if p is within range, otherwise '!0!'.
	 * 
	 * This is a safe version of S-port standard procedure LoadChar.
	 *
	 *    character procedure fetchar(t,p); text t; integer p;
	 *    if p>0 and p&lt;=t.length then fetchar:= t.sub(p,1).getchar;
	 *
	 * </pre>
	 * 
	 * @param t a text reference
	 * @param p the text position in which the character is stored 
	 * @return the character at position p
	 */
	public static char fetchar(_TXT t, int p) {
		if (p > 0 & p <= t.LENGTH) {
			t.POS = p - 1;
			return (_TXT.getchar(t));
		}
		return (0);
	}

	/** DEC_Lib Procedure findtrigger.
	 * <pre>
	 * character procedure findtrigger(master,triggers); name master; text master,triggers;
	 *
	 * Starting from current master.pos, find first occurrence of any of the
	 * characters in triggers.;
	 *
	 * character procedure findtrigger(master,triggers); name master; text master,triggers;
	 * begin character c; text t;
	 *       t:- master; while t.More do
	 *       begin c:= t.getchar; triggers.setpos(1);
	 *             if scanto(triggers,c) =/= triggers then
	 *             begin comment c found in triggers; findtrigger1:= c;
	 *                   goto out;
	 *             end
	 *       end loop;
	 *  out: master.setpos(t.pos);
	 * end;
	 * </pre>
	 * 
	 * @param master argument
	 * @param triggers argument
	 * @return the resulting character
	 */
	public static char findtrigger(_NAME<_TXT> master, _TXT triggers) {
		_TXT M = master.get();
		String S = M.edText();
		String T = triggers.edText();
		for (int i = M.POS; i < M.LENGTH; i++) {
			char c = S.charAt(i);
			if (T.indexOf(c) >= 0) {
				M.POS = i + 1;
				master.put(M);
				return (c);
			}
		}
		return 0;
	}

	/** DEC_Lib Procedure from.
	 * <pre>
	 * text procedure from(t,p); text t; integer p;
	 *
	 * Returns a reference to the longest subtext of t starting at pos = p.
	 *
	 * text procedure from(t,p); text t; integer p;
	 * if p&lt;=t.Length then from :- if p&lt;=0 then t else t.Sub(p,t.Length-p+1);
	 *
	 * </pre>
	 * 
	 * @param t argument text
	 * @param p start pos
	 * @return the resulting text
	 */
	public static _TXT from(_TXT t, int p) {
		if (p < t.LENGTH) {
			if (p < 0)
				return (t);
			else
				return (_TXT.sub(t, p, t.LENGTH - p + 1));
		}
		return (null);
	}

	/** DEC_Lib Procedure front.
	 * <pre>
	 * text procedure front(t); text t;
	 *
	 * Returns a reference to the longest subtext of t before pos.
	 *
	 * text procedure front(t); text t; if t=/=notext then front:-t.sub(1,t.pos-1);
	 * 
	 * </pre>
	 * 
	 * @param t argument text
	 * @return the resulting text
	 */
	public static _TXT front(_TXT t) {
		if (t != null)
			return (_TXT.sub(t, 1, t.POS));
		return (null);
	}

	/** DEC_Lib Procedure frontcompare.
	 * <pre>
	 * boolean procedure frontcompare(string,config); text string,config;
	 *
	 * Starting at current pos, does string begin with a substring equal to config ?
	 *
	 *   boolean procedure frontcompare(string,config); text string,config;
	 *   if string.Length - string.pos+1 >= config.Length then
	 *   frontcompare:= string.Sub(string.pos,config.Length) = config;
	 * 
	 * </pre>
	 * 
	 * @param string argument
	 * @param config argument
	 * @return true: codition holds
	 */
	public static boolean frontcompare(_TXT string, _TXT config) {
		String t1 = string.edText().substring(string.POS);
		String t2 = config.edText();
		return (t1.startsWith(t2));
	}

	/** DEC_Lib Procedure frontstrip-
	 * <pre>
	 * text procedure frontstrip(t); text t;
	 *
	 * Returns a reference to the longest subtext of t starting with the first
	 * non-blank character.
	 * 
	 * </pre>
	 * 
	 * @param t argument text
	 * @return the resulting text
	 */
	public static _TXT frontstrip(_TXT t) {
		String T = t.edText().stripLeading();
		return (new _TXT(T));
	}

	/** DEC_Lib Procedure getitem.
	 * <pre>
	 * text procedure getitem(tt); name tt; text tt;
	 *
	 * Skips any blanks or tabs, starting at tt.pos. If tt.more holds then, an item
	 * is identified according to the following rules:
	 *
	 * a) If the first following character is a letter (a-z,A-Z), an identifier is
	 * found. The identifier consists of the initial letter and any following
	 * letters and/or decimal digits.
	 *
	 * b) If the first character is a digit, we have a numeric item, consisting of a
	 * string of digits with at most one decimal point "." included.
	 *
	 * c) Any other character except blank or tab forms an item on its own.
	 * 
	 * Example: "IF car.wheel_size > 13.5" will be split into the items "IF", "car",
	 * ".", "wheel", "_", "size", ">", "13.5" via successive calls to GETITEM.
	 *
	 * The value of getitem is a subtext reference to the item within tt, or notext
	 * if no item can be found starting at tt.pos. tt.pos will be placed after the
	 * item.
	 *
	 *
	 * text procedure getitem(tt); name tt; text tt; if tt =/= notext then
	 * begin character window; integer startpos; text t;
	 * 
	 *       character procedure getCharacter;
	 *       if t.more then getCharacter:= window:=t.getchar else goto out;
	 * 
	 *       boolean procedure idchar(c); character c; idchar:= letter(c) or digit(c);
	 * 
	 *       t:- tt; t.setpos(tt.pos); startpos:= t.length+1; getCharacter;
	 *       while window = ' ' or window = char(9) do getCharacter;
	 *       startpos:= t.pos-1;
	 *       if not letter(window) then
	 *       begin if digit(window) then while digit(getCharacter) do;
	 *             if window = '.' then while digit(getCharacter) do;
	 *       end else while idchar(getCharacter) do;
	 *       if t.pos > startpos + 1 then t.setpos(t.pos-1);
	 *  out: getitem1 :- t.sub(startpos,t.pos-startpos);
	 *       tt.setpos(t.pos)
	 * end;
	 *
	 * </pre>
	 * @param tt argument text
	 * @return the resulting text
	 */
	public static _TXT getitem(_NAME<_TXT> tt) {
		_TXT TXT = tt.get();
		_TXT RES = GETITEM(TXT);
		tt.put(TXT);
		return (RES);
	}

	enum State {
		NULL, NUMBER, IDENTIFIER, SYMBOL
	};

	private static _TXT GETITEM(_TXT TXT) {
		String s = TXT.edText();
		String head = s.substring(0, TXT.POS);
		String tail = s.substring(TXT.POS);
		String rest = tail.stripLeading();
		int p = 0;
		State state = State.NULL;
		LOOP: for (p = 0; p < rest.length(); p++) {
			char c = rest.charAt(p);
			switch (state) {
			case NULL:
				if (Character.isDigit(c))
					state = State.NUMBER;
				else if (Character.isLetter(c))
					state = State.IDENTIFIER;
				else
					state = State.SYMBOL;
				break;
			case SYMBOL:
				break LOOP;
			case NUMBER:
				if (!(Character.isDigit(c) | c == '.'))
					break LOOP;
				break;
			case IDENTIFIER:
				if (!Character.isLetterOrDigit(c))
					break LOOP;
				break;
			}
		}
		int nSpace = tail.length() - rest.length();
		TXT.POS = head.length() + nSpace + p;
		return (new _TXT(rest.substring(0, p)));
	}

	/** DEC_Lib Procedure hash.
	 * <pre>
	 * integer procedure hash(t,n); text t; integer n;
	 *
	 * Procedure HASH returns a calculated hash value, in the range (0:n-1), from a
	 * given text t. The result is taken modulo n before return.
	 * 
	 * It is recommended to choose n as a prime number.
	 *
	 *    integer procedure hash(t,n); text t; integer n; begin
	 *        text tstrip; integer a;
	 *        tstrip:- frontstrip(t.Strip);
	 *        if tstrip == notext then hash:= mod(t.length,n) else begin
	 *            a:= rank(tstrip.getchar);
	 *            if tstrip.length > 3 then begin
	 *                a:= a +   8*rank(tstrip.getchar);
	 *                a:= a +  64*rank(tstrip.getchar)
	 *            end;
	 *            tstrip.setpos(tstrip.length);
	 *            a:= a + 512*rank(tstrip.getchar) + t.length;
	 *            hash:= mod(a,n);
	 *        end
	 *    end of hash;
	 *
	 * </pre>
	 * 
	 * @param t the argument text
	 * @param n the argument n
	 * @return resulting hash value, in the range (0:n-1)
	 */
	public static int hash(_TXT t, int n) {
		String S = t.edText();
		String s = S.stripLeading();
		if (s == null)
			return (mod(S.length(), n));
		int p = 0;
		int a = (int) s.charAt(p++);
		if (s.length() > 3) {
			a = a + 8 * (int) s.charAt(p++);
			a = a + 64 * (int) s.charAt(p++);
		}
		a = a + 512 * (int) s.charAt(s.length() - 1) + S.length();
		return (mod(a, n));
	}

	/** DEC_Lib Procedure initem.
	 * <pre>
	 * text procedure initem(fileref); ref(file) fileref; begin
	 *
	 * The parameter is formally ref(file), but should refer an infile, directfile,
	 * inbytefile or directbytefile (otherwise notext is returned). Same as getitem
	 * for files.
	 * 
	 * On infiles or directfiles, lastitem is called first. Notext is returned, if
	 * (a) fileref is none, (b) fileref does not point to a legal file object (see
	 * above), or (c) the remainder of the file is spaces and/or TABs. Note, that
	 * since no copying is done, a call on initem may destroy previously located
	 * items, if they are not copied by the program, since inimage may be invoked.
	 *
	 * NOTE: Initem is extended to cover bytefiles;
	 *       it will then return a single character item.
	 *
	 *   text procedure initem(fileref); ref(file) fileref; begin
	 *       text res; integer i;
	 *       inspect fileref
	 *         when infile do
	 *              if not lastitem then res:-getitem(image)
	 *         when directfile do
	 *              if not lastitem then res:-getitem(image)
	 *         when inbytefile do begin
	 *              res:-blanks(1); i:=rank('!9!');
	 *              while i=rank(' ') or i=rank('!9!') !TAB; do i:=inbyte;
	 *              res.sub(1,1).putchar(char(i));
	 *         end
	 *         when directbytefile do begin
	 *              res:-blanks(1); i:=rank('!9!');
	 *              while i=rank(' ') or i=rank('!9!') !TAB; do i:=inbyte;
	 *              res.sub(1,1).putchar(char(i));
	 *         end;
	 *         initem:-res;
	 *   end;
	 * 
	 * </pre>
	 * 
	 * @param f argument File
	 * @return the resulting text
	 */
	public static _TXT initem(_File f) {
		if (f instanceof _Infile | f instanceof _Directfile) {
			_Imagefile F = (_Imagefile) f;
			if (!F.lastitem())
				return (GETITEM(F.image));
		} else if (f instanceof _Inbytefile) {
			_Inbytefile F = (_Inbytefile) f;
			int b = 9;
			while (b == ' ' | b == 9)
				b = F.inbyte();
			return (new _TXT("" + (char) b));
		} else if (f instanceof _Directbytefile) {
			_Directbytefile F = (_Directbytefile) f;
			int b = 9;
			while (b == ' ' | b == 9)
				b = F.inbyte();
			return (new _TXT("" + (char) b));
		}
		return (null);
	}

	/** DEC_Lib Procedure insinglechar.
	 * <pre>
	 * character procedure insinglechar;
	 * 
	 * Returns next input character from sysin (after last inimage) without waiting
	 * for break character. A subsequent inimage will begin reading after the last
	 * character which has been input with insinglechar.
	 * 
	 * The use of this routine depends on runtime option USE_CONSOLE.
	 * If it is not set a runtime error will occur.
	 * 
	 * </pre>
	 * 
	 * @return single character
	 */
	public static char insinglechar() {
		try {
			return (_RT.console.read());
		} catch (Exception e) {
			throw new _SimulaRuntimeError(
					"Procedure insinglechar is undefined: re-run program with Runtime Option USE_CONSOLE=true");
		}
	}

	/** DEC_Lib Procedure linecount.
	 * <pre>
	 * integer procedure linecount(pfil); ref(PrintFile) pfil; begin
	 *     if pfil==none then linecount:=-1
	 *     else if not pfil.isOpen then linecount:=-2
	 *     else begin
	 *         --  return current linesperpage setting
	 *         integer lpp; lpp:=linesperpage(0); linesperpage(lpp);
	 *         linecount:=lpp;
	 *     end;
	 * end;
	 * 
	 * </pre>
	 * 
	 * @param pf argument Printfile
	 * @return resulting lpp or code
	 */
	public static int linecount(_Printfile pf) {
		if (pf == null)
			return (-1);
		if (!pf.isopen())
			return (-2);
		return (pf._LINES_PER_PAGE);
	}

	/** DEC_Lib Procedure lowc.
	 * <pre>
	 * character procedure lowc(c); character c;
	 *
	 * Returns uppercase character as lowercase equivalent, other characters
	 * unchanged.
	 *
	 *   character procedure lowc(c); character c;
	 *   lowc:= if not Letter(c) then c else
	 *          if c > 'z' then
	 *              char(rank(c)-rank('A')+rank('a')) else c;
	 * </pre>
	 * @param c argument character
	 * @return the lower case versjon
	 */
	public static char lowc(char c) {
		return (Character.toLowerCase(c));
	}

	/** DEC_Lib Procedure maketext.
	 * <pre>
	 * text procedure maketext(ch,n); character ch; integer n;
	 *
	 * Return a text object of length n, filled with character ch.
	 *
	 * text procedure maketext(ch,n); character ch; integer n; if n > 0 then begin
	 * text t; t:-blanks(n); while t.more do t.putchar(ch); t.setpos(1);
	 * maketext:-t; end;
	 *
	 * </pre>
	 * 
	 * @param c argument character
	 * @param n text length
	 * @return resulting text value
	 */
	public static _TXT maketext(final char c, final int n) {
		if (n <= 0)
			return (NOTEXT);
		_TXT textRef = new _TXT();
		_TEXTOBJ textObj = new _TEXTOBJ(n, false);
		textObj.fill(c);
		textRef.START = 0; // Note: Counting from zero in this implementation
		textRef.LENGTH = n;
		textRef.POS = 0; // Note: Counting from zero in this implementation
		textRef.OBJ = textObj;
		return (textRef);
	}

	/** DEC_Lib Procedure puttext.
	 * <pre>
	 * boolean procedure puttext(oldstring,newstring); name oldstring; text
	 * oldstring,newstring;
	 *
	 * Puts the (short) text newstring into the (longer) text oldstring. Newstring
	 * is put at POS in oldstring, and POS of oldstring is moved after the end of
	 * the new substring.
	 * 
	 * Returns false if there was not room for the new string, or if trying to
	 * modify a Constant text.
	 *
	 *  boolean procedure puttext(oldstring,newstring); name oldstring; text oldstring,newstring;
	 *  begin text s;
	 *      s:-oldstring;
	 *      if s.pos+newstring.length-1 &lt;= s.length 
	 *         and then not s.constant
	 *      then begin
	 *          puttext:=true;
	 *          s.sub(s.pos,newstring.length):=newstring;
	 *          s.setpos(s.pos+newstring.length)
	 *          oldstring.setpos(s.pos)
	 *      end;
	 *  end;
	 * 
	 * </pre>
	 * @param oldstring argument old string
	 * @param newstring argument new string
	 * @return tthe resulting text
	 */
	public static boolean puttext(_NAME<_TXT> oldstring, _TXT newstring) {
		_TXT s = oldstring.get();
		if (s.POS + newstring.LENGTH <= s.LENGTH & !_TXT.constant(s)) {
			_ASGTXT(_TXT.sub(s, s.POS + 1, newstring.LENGTH), newstring);
			_TXT.setpos(s, s.POS + 1 + newstring.LENGTH);
			oldstring.put(s);
			return (true);
		}
		return (false);
	}

	/** DEC_Lib Procedure rest.
	 * <pre>
	 * text procedure rest(t); text t;
	 *
	 * Returns a subtext reference of a text starting at pos.
	 *
	 * text procedure rest(t); text t; rest :- t.sub(t.pos,t.length+1-t.pos);
	 *
	 * </pre>
	 * 
	 * @param t argument text
	 * @return resulting text
	 */
	public static _TXT rest(_TXT t) {
		int pos = t.POS + 1;
		return (_TXT.sub(t, pos, t.LENGTH - pos + 1));
	}

	/** DEC_Lib Procedure scanchar.
	 * <pre>
	 * character procedure scanchar(t); name t; text t;
	 *
	 * Safe version of GETCHAR. Will not give RTS error, just returns char(0) if not
	 * t.more.
	 *
	 * character procedure scanchar(t); name t; text t;
	 * scanchar:= if t.more then t.getchar else char(0);
	 * 
	 * </pre>
	 * 
	 * @param t argument text
	 * @return resulting character value
	 */
	public static char scanchar(_NAME<_TXT> t) {
		_TXT T = t.get();
		if (!_TXT.more(T))
			return (0);
		char res = _TXT.getchar(T);
		t.put(T);
		return (res);
	}

	/** DEC_Lib Procedure scanfrac.
	 * <pre>
	 * integer procedure scanfrac(t); name t; text t;
	 * 
	 * Scanfrac is similar to getfrac, but differs in two principal ways:
	 * 
	 * 1) It looks for an item starting at t.pos
	 * 
	 * 2) The handling of error conditions due to bad data is different. Scanfrac
	 * returns the value of the next grouped item if any. If no such item is found,
	 * 'minint' is returned.
	 * 
	 * t.pos will only be moved if de-editing was successful.
	 * 
	 * </pre>
	 * @param t argument text
	 * @return the resulting integer value
	 */
	public static int scanfrac(_NAME<_TXT> t) {
		_TXT T = t.get();
		if (T != null) {
			_TXT TAIL = rest(T);
			try {
				int res = _TXT.getfrac(TAIL);
				T.POS = T.POS + TAIL.POS;
				t.put(T);
				return (res);
			} catch (Exception e) {}
		}
		return (minint);
	}

	/** DEC_Lib Procedure scanint.
	 * <pre>
	 * integer procedure scanint(t); name t; text t;
	 *
	 * Scanint is similar to GETINT, but differs in two principal ways:
	 * 
	 * 1) It looks for an item starting at T.pos
	 * 
	 * 2) The handling of error conditions due to bad data is different. Scanint
	 * returns the value of the next integer item if any. If no such item is found,
	 * 'minint' is returned.
	 * 
	 * t.pos will only be moved if de-editing was successful.
	 *
	 *    integer procedure scanint(t); name t; text t;
	 *    begin
	 *      EXTERNAL text procedure rest;
	 *      EXTERNAL integer procedure checkint,maxint;
	 *      text s;  integer i;
	 *      s:-rest(t);
	 *      i:=checkint(s);
	 *      if i=1 then begin
	 *        scanint:=s.Getint;
	 *        t.setpos(t.pos+s.pos-1)
	 *      end else scanint:=Minint
	 *    end;
	 *
	 * </pre>
	 * @param t argument text
	 * @return the resulting integer value
	 */
	public static int scanint(_NAME<_TXT> t) {
		_TXT T = t.get();
		if (T != null) {
			_TXT TAIL = rest(T);
			try {
				int res = _TXT.getint(TAIL);
				T.POS = T.POS + TAIL.POS;
				t.put(T);
				return (res);
			} catch (Exception e) {}
		}
		return (minint);
	}

	/** DEC_Lib Procedure scanreal.
	 * <pre>
	 * long real procedure scanreal(t); name t; text t;
	 *
	 * Scanreal is similar to GETREAL, but differs in two principal ways:
	 * 
	 * 1) It looks for an item starting at T.pos
	 * 
	 * 2) The handling of error conditions due to bad data is different. Scanreal
	 * returns the value of the next real item if any. If no such item is found,
	 * 'minreal' is returned.
	 * 
	 * t.pos will only be moved if de-editing was successful.
	 *
	 *    comment -1&amp;33 should be the most negative real value;
	 *    long real procedure scanreal(t);  name t;  text t;
	 *    begin
	 *      EXTERNAL text procedure rest;
	 *      EXTERNAL integer procedure checkreal;
	 *      text s;  integer i;
	 *      s:-rest(t);
	 *      i:=checkreal(s);
	 *      if i=1 then begin scanreal:=s.Getreal;
	 *                        t.setpos(t.pos+s.pos-1)
	 *             end else scanreal:=Minreal;
	 *    end;
	 * 
	 * </pre>
	 * 
	 * @param t argument text
	 * @return resulting long real value
	 */
	public static double scanreal(_NAME<_TXT> t) {
		_TXT T = t.get();
		if (T != null) {
			_TXT TAIL = rest(T);
			try {
				double res = _TXT.getreal(TAIL);
				T.POS = T.POS + TAIL.POS;
				t.put(T);
				return (res);
			} catch (Exception e) {}
		}
		return minreal;
	}

	/** DEC_Lib Procedure scanto.
	 * <pre>
	 * text procedure scanto(tt,c); name tt; text tt; character c;
	 *
	 * Scans from tt.pos until the next occurrence of the character c. Places tt.pos
	 * after the character found.
	 * 
	 * The function value of scan is the text starting at the initial tt.pos and not
	 * including the character c found.
	 * 
	 * If no character c is found, tt.pos will be = tt.length+1, and scan denotes
	 * the rest of tt starting at the initial tt.pos.
	 *
	 *  text procedure scanto(tt,c); name tt; text tt; character c; begin
	 *      text t; integer p;
	 *      t:- tt; p:= t.pos;
	 *      while t.more do
	 *        if t.getchar = c then begin
	 *            scanto:- t.sub(p,t.pos-p-1);
	 *            goto out;
	 *        end;
	 *      scanto:- from(t,p);
	 *      out: tt.setpos(t.pos);
	 *    end of scanto;
	 * 
	 * </pre>
	 * 
	 * @param t argument text
	 * @param c stop character c
	 * @return the resulting text
	 */
	public static _TXT scanto(_NAME<_TXT> t, char c) {
		_TXT TXT = t.get();
		String s = TXT.edText();
		String tail = s.substring(TXT.POS);
		int pos = TXT.POS + 1;
		int index = tail.indexOf(c); // returns: the index of the first occurrence of the specified substring, or -1
		if (index >= 0) { // Character found at index in tail
			TXT.POS = pos + index - 1;
			t.put(TXT);
			return (_TXT.sub(TXT, pos, index));
		} else { // No character c is found
			TXT.POS = TXT.START + TXT.LENGTH;
			t.put(TXT);
			return (_TXT.sub(TXT, pos, _TXT.length(TXT) - pos + 1));
		}
	}

	/**
	 * DEC_Lib Procedure search.
	 * <pre>
	 * integer procedure search(t1,t2); text t1,t2;
	 *
	 * Search: find first subtext = t2 from t1.pos;
	 * Return: The pos indicator of first character of subtext within t1.
	 *         If not found return t1.length+1
	 * </pre>
	 * 
	 * @param t1 master text
	 * @param t2 text to be searched for
	 * @return resulting pos
	 */
	public static int search(_TXT t1, _TXT t2) {
		if (t1 == null | t2 == null)
			return (0);
		String s1 = t1.edText();
		String s2 = t2.edText();
		int idx = s1.indexOf(s2, t1.POS);
		return ((idx < 0) ? t1.LENGTH + 1 : idx + 1);
	}

	/**
	 * DEC_Lib Procedure skip.
	 * <pre>
	 * text procedure skip(tt,c); name tt; text tt; character c; begin
	 *
	 * If, starting at tt.pos, a sequence of characters = c is found, then tt.pos is
	 * moved past these characters, otherwise tt.pos is unaltered.
	 * 
	 * The function value of skip is rest(tt) (after changing tt.pos).
	 *
	 *   text procedure skip(tt,c); name tt; text tt; character c; begin
	 *       text t; t:- tt;
	 *       while t.more do if t.getchar ne c then begin
	 *           t.setpos(t.pos-1);
	 *           skip1:- t.sub(t.pos,t.length-t.pos+1);
	 *           goto out
	 *       end;
	 *       out: tt.setpos(t.pos)
	 *   end of skip;
	 *
	 * </pre>
	 * 
	 * @param t argument text
	 * @param c argument character
	 * @return the resulting text
	 */	
	public static _TXT skip(_NAME<_TXT> t, char c) {
		_TXT TXT = t.get();
		String s = TXT.edText();
		int pos = TXT.POS;
		int lp = TXT.LENGTH - TXT.START;
		_TEXTOBJ obj = TXT.OBJ;
		while (pos < lp) {
			if (obj.MAIN[pos] != c) {
				TXT.POS = pos;
				TXT.LENGTH = TXT.LENGTH - pos;
				t.put(TXT);
				return (new _TXT(s.substring(pos)));
			}
			pos++;
		}
		TXT.POS = pos;
		t.put(TXT);
		return null;
	}

	/**
	 * DEC_Lib Procedure startpos.
	 * <pre>
	 * integer procedure startpos(t); text t;
	 *
	 * startpos returns the starting position of t within t.main. Provided for
	 * compatibility only;
	 *
	 * integer procedure startpos(t); text t; begin startpos:=t.start; end;
	 *
	 * </pre>
	 * 
	 * @param t argument text
	 * @return starting position of t within t.main
	 */
	public static int startpos(_TXT t) {
		return (t.START + 1);
	}

	/**
	 * DEC_Lib Procedure today.
	 * <pre>
	 * text procedure today;
	 *
	 * Returns a reference to a text object of length 10 with contents "yyyy-mm-nn"
	 * where yyyy is year, mm is month(in digits 01-12), nn is day (in digits
	 * 01-31). This is the internationally standardized format for dates.
	 *
	 * text procedure today; today:-datetime.sub(1,10);
	 * </pre>
	 * 
	 * @return the resulting text
	 */
	public static _TXT today() {
		DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String dat = LocalDateTime.now().format(form);
		return (new _TXT(dat));
	}

	/**
	 * DEC_Lib Procedure tsub.
	 * <pre>
	 * text procedure tsub(t,p,l); text t; integer p,l;
	 *
	 * tsub is a safe variant of sub. Parameters which give an error in sub will
	 * return notext instead.
	 *
	 *   text procedure tsub(t,p,l); text t; integer p,l;
	 *   if p >= 1 and l >= 0 and p+l &lt;= t.length + 1 then
	 *       tsub:- t.sub(p,l);
	 *
	 * </pre>
	 * @param t argument text
	 * @param p argument pos
	 * @param l argument length
	 * @return the resulting text
	 */
	public static _TXT tsub(_TXT t, int p, int l) {
		try {
			return (_TXT.sub(t, p, l));
		} catch (Exception e) {
			return (null);
		}
	}

	/** DEC_Lib Procedure upc.
	 * <pre>
	 * character procedure upc(c); character c;
	 *
	 * Returns lowercase character as uppercase equivalent, other characters
	 * unchanged.
	 *
	 *    character procedure upc(c); character c;
	 *       upc:= if not Letter(c) then c else
	 *             if c > 'Z' then
	 *                char(rank(c)-rank('a')+rank('A')) else c;
	 *
	 * </pre>
	 * 
	 * @param c argument character
	 * @return the upper case versjon
	 */
	public static char upc(char c) {
		return (Character.toUpperCase(c));
	}

	/**
	 * DEC_Lib Procedure upcompare.
	 * <pre>
	 * boolean procedure upcompare(master,test);
	 *
	 *
	 *         Upcompare returns true if the contents
	 *         of TEST is equal to the next TEST.length characters of MASTER,
	 *         counted from current MASTER.pos.
	 *         The MASTER characters will be converted to upper case
	 *         before comparison (without changing the MASTER text).
	 *         Note that the TEST text will not be converted.
	 *         Thus
	 *
	 *         MASTER          TEST            UPCOMPARE
	 *
	 *         BEGIN           BEG             true
	 *         BEGIN           beg             false
	 *         begin           BEG             true
	 *         begin           beg             false
	 *         xxxxx           BEG             false
	 *
	 *         assuming that MASTER.pos = 1.
	 *         If TEST == notext the result will always be true.
	 *
	 *
	 *    boolean procedure upcompare(master,test); text master,test;
	 *    if master.pos + test.length &lt;= master.length + 1 then begin
	 *        character cmaster,ctest;
	 *        integer shift;
	 *        shift:= rank('a') - rank('A');
	 *        while master.more and test.more and cmaster = ctest do begin
	 *            cmaster:= master.getchar;
	 *            ctest:= test.getchar;
	 *            if (if cmaster > 'Z' then Letter(cmaster) else FALSE)
	 *                then cmaster:= char(rank(cmaster) - shift);
	 *        end loop;
	 *    Out:upcompare:= cmaster = ctest;
	 *    end;
	 *
	 * </pre>
	 * 
	 * @param master argument master
	 * @param test argument test
	 * @return true if condition holds
	 */
	public static boolean upcompare(_TXT master, _TXT test) {
		String mst = master.edText().toUpperCase().substring(_TXT.pos(master) - 1);
		String tst = test.edText();
		return (mst.startsWith(tst));
	}

	/**
	 * DEC_Lib Procedure upto.
	 * <pre>
	 * text procedure upto(t,p); text t; integer p;
	 *
	 * Returns a reference to the longest subtext of T before pos = p.
	 *
	 *   text procedure upto(t,p); text t; integer p;
	 *   if i>0 then upto :- if p>t.length then t else t.sub(1,p-1);
	 *
	 * </pre>
	 * @param t argument text
	 * @param p pos limit
	 * @return the resulting text
	 */
	public static _TXT upto(_TXT t, int p) {
		if (p <= 0)
			return (null);
		if (p > t.LENGTH)
			return (t);
		return (_TXT.sub(t, 1, p - 1));
	}

}
