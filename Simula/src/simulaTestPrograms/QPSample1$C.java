// JavaLine 1 ==> SourceLine 17
package simulaTestPrograms;
// Simula-Beta-0.3 Compiled at Sat Mar 16 11:15:38 CET 2019
import simula.runtime.*;
@SuppressWarnings("unchecked")
public class QPSample1$C extends CLASS$ {
    // ClassDeclaration: BlockKind=Class, BlockLevel=2, PrefixLevel=0, firstLine=17, lastLine=32, hasLocalClasses=false, System=false, detachUsed=true
    public int prefixLevel() { return(0); }
    public boolean isDetachUsed() { return(true); }
    // Declare parameters as attributes
    // Declare locals as attributes
    // Normal Constructor
    public QPSample1$C(RTObject$ staticLink) {
        super(staticLink);
        // Parameter assignment to locals
        BBLK(); // Iff no prefix
        // Declaration Code
        TRACE_BEGIN_DCL$("C",26);
        // Create Class Body
        CODE$=new ClassBody(CODE$,this,0) {
            public void STM$() {
                TRACE_BEGIN_STM$("C",26,inner);
                // JavaLine 23 ==> SourceLine 30
                new QPSample1$C$Q(((QPSample1$C)CUR$));
                if(inner!=null) {
                    inner.STM$();
                    TRACE_BEGIN_STM_AFTER_INNER$("C",30);
                }
                TRACE_END_STM$("C",30);
                EBLK(); // Iff no prefix
            }
        };
    } // End of Constructor
    // Class Statements
    public QPSample1$C STM$() { return((QPSample1$C)CODE$.EXEC$()); }
    public QPSample1$C START() { START(this); return(this); }
    public static PROGINFO$ INFO$=new PROGINFO$("QPSample1.sim","Class C",1,17,23,30,36,32);
}
