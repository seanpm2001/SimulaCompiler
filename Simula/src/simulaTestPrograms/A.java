// JavaLine 1 <== SourceLine 1
package simulaTestPrograms;
// Simula-1.0 Compiled at Wed Jun 26 13:41:14 CEST 2019
import simula.runtime.*;
@SuppressWarnings("unchecked")
public class A extends CLASS$ {
    // ClassDeclaration: BlockKind=Class, BlockLevel=1, PrefixLevel=0, firstLine=1, lastLine=10, hasLocalClasses=false, System=false, detachUsed=false
    public int prefixLevel() { return(0); }
    // Declare parameters as attributes
    public int p$p1;
    public float p$p2;
    // Declare locals as attributes
    // JavaLine 13 <== SourceLine 3
    public ARRAY$<TXT$[]>TA=null;
    // JavaLine 15 <== SourceLine 4
    public final TXT$ T=(TXT$)(new TXT$("Constant"));
    // JavaLine 17 <== SourceLine 5
    public int i=0;
    // JavaLine 19 <== SourceLine 6
    public float r=0.0f;
    // Normal Constructor
    public A(RTObject$ staticLink,int sp$p1,float sp$p2) {
        super(staticLink);
        // Parameter assignment to locals
        this.p$p1 = sp$p1;
        this.p$p2 = sp$p2;
        BBLK(); // Iff no prefix
        // Declaration Code
        TRACE_BEGIN_DCL$("A",6);
        // JavaLine 30 <== SourceLine 3
        int[] TA$LB=new int[1]; int[] TA$UB=new int[1];
        TA$LB[0]=1; TA$UB[0]=40;
        BOUND_CHECK$(TA$LB[0],TA$UB[0]);
        TA=new ARRAY$<TXT$[]>(new TXT$[TA$UB[0]-TA$LB[0]+1],TA$LB,TA$UB);
    }
    // Class Statements
    public A STM$() {
        TRACE_BEGIN_STM$("A",3);
        // Class A: Code before inner
        // JavaLine 40 <== SourceLine 7
        i=p$p1;
        // JavaLine 42 <== SourceLine 9
        // Class A: Code after inner
        r=p$p2;
        TRACE_END_STM$("A",9);
        EBLK();
        return(this);
    } // End of Class Statements
    public static PROGINFO$ INFO$=new PROGINFO$("Classes.sim","Class A",1,1,13,3,15,4,17,5,19,6,30,3,40,7,42,9,48,10);
} // End of Class
