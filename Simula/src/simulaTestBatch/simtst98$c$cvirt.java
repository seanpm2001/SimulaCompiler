package simulaTestBatch;
// Simula-Beta-0.3 Compiled at Sat Apr 13 09:06:30 CEST 2019
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst98$c$cvirt extends BASICIO$ {
    public int prefixLevel() { return(0); }
    public int p$line;
    public TXT$ p$t;
    public TXT$ p$where;
    private int $npar=0; // Number of actual parameters transmitted.
    public simtst98$c$cvirt setPar(Object param) {
        try {
            switch($npar++) {
                case 0: p$line=intValue(param); break;
                case 1: p$t=(TXT$)objectValue(param); break;
                case 2: p$where=(TXT$)objectValue(param); break;
                default: throw new RuntimeException("Wrong number of parameters");
            }
        }
        catch(ClassCastException e) { throw new RuntimeException("Wrong type of parameter: "+$npar+" "+param,e);}
        return(this);
    }
    public simtst98$c$cvirt(RTObject$ SL$)
    { super(SL$); }
    public simtst98$c$cvirt(RTObject$ SL$,int sp$line,TXT$ sp$t,TXT$ sp$where) {
        super(SL$);
        this.p$line = sp$line;
        this.p$t = sp$t;
        this.p$where = sp$where;
        BBLK();
        STM$();
    }
    public simtst98$c$cvirt STM$() {
        new simtst98$c$virtproc(((simtst98$c)(CUR$.SL$)),p$line,p$t,p$where);
        EBLK();
        return(this);
    }
    public static PROGINFO$ INFO$=new PROGINFO$("simtst98.sim","Procedure cvirt",1,153,35,154,39,154);
}
