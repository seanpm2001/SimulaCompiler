// JavaLine 1 ==> SourceLine 38
package simulaTestPrograms;
// Simula-Beta-0.3 Compiled at Tue Feb 19 13:53:23 CET 2019
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class Dates$Date$Plus extends BASICIO$ {
    // ProcedureDeclaration: BlockKind=Procedure, BlockLevel=3, firstLine=38, lastLine=42, hasLocalClasses=false, System=false
    public int prefixLevel() { return(0); }
    // Declare return value as attribute
    public Dates$Date RESULT$;
    public Object RESULT$() { return(RESULT$); }
    // Declare parameters as attributes
    public int p$Nx;
    // Declare locals as attributes
    // Parameter Transmission in case of Formal/Virtual Procedure Call
    private int $npar=0; // Number of actual parameters transmitted.
    public Dates$Date$Plus setPar(Object param) {
        //Util.BREAK("CALL Dates$Date$Plus.setPar: param="+param+", qual="+param.getClass().getSimpleName()+", npar="+$npar+", staticLink="+SL$);
        try {
            switch($npar++) {
                case 0: p$Nx=intValue(param); break;
                default: throw new RuntimeException("Wrong number of parameters");
            }
        }
        catch(ClassCastException e) { throw new RuntimeException("Wrong type of parameter: "+$npar+" "+param,e);}
        return(this);
    }
    // Constructor in case of Formal/Virtual Procedure Call
    public Dates$Date$Plus(RTObject$ SL$)
    { super(SL$); }
    // Normal Constructor
    public Dates$Date$Plus(RTObject$ SL$,int sp$Nx) {
        super(SL$);
        // Parameter assignment to locals
        this.p$Nx = sp$Nx;
        BBLK();
        // Declaration Code
        TRACE_BEGIN_DCL$("Plus",38);
        STM$();
    } // End of Constructor
    // Procedure Statements
    public Dates$Date$Plus STM$() {
        TRACE_BEGIN_STM$("Plus",38);
        // JavaLine 44 ==> SourceLine 41
        RESULT$=new Dates$Date(((Dates)(CUR$.SL$.SL$)),(((Dates$Date)(CUR$.SL$)).p$D+(p$Nx)),((Dates$Date)(CUR$.SL$)).p$M,((Dates$Date)(CUR$.SL$)).p$Y).STM$();
        TRACE_END_STM$("Plus",41);
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static PROGINFO$ INFO$=new PROGINFO$("dates.sim","Procedure Plus",1,38,44,41,49,42);
}
