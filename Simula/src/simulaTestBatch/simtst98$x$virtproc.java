package simulaTestBatch;
// Simula-Beta-0.3 Compiled at Sat Apr 13 09:06:30 CEST 2019
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst98$x$virtproc extends BASICIO$ {
    public int prefixLevel() { return(0); }
    public int p$line;
    public TXT$ p$t;
    public TXT$ p$wher;
    private int $npar=0; // Number of actual parameters transmitted.
    public simtst98$x$virtproc setPar(Object param) {
        try {
            switch($npar++) {
                case 0: p$line=intValue(param); break;
                case 1: p$t=(TXT$)objectValue(param); break;
                case 2: p$wher=(TXT$)objectValue(param); break;
                default: throw new RuntimeException("Wrong number of parameters");
            }
        }
        catch(ClassCastException e) { throw new RuntimeException("Wrong type of parameter: "+$npar+" "+param,e);}
        return(this);
    }
    public simtst98$x$virtproc(RTObject$ SL$)
    { super(SL$); }
    public simtst98$x$virtproc(RTObject$ SL$,int sp$line,TXT$ sp$t,TXT$ sp$wher) {
        super(SL$);
        this.p$line = sp$line;
        this.p$t = sp$t;
        this.p$wher = sp$wher;
        BBLK();
        STM$();
    }
    public simtst98$x$virtproc STM$() {
        if(VALUE$((TXTREL$NE(p$t,new TXT$("x.virtproc"))|(((simtst98)(CUR$.SL$.SL$)).trace)))) {
            {
                if(VALUE$(TXTREL$NE(p$t,new TXT$("x.virtproc")))) {
                    {
                        ((simtst98)(CUR$.SL$.SL$)).errorFound=true;
                        sysout().outtext(((simtst98)(CUR$.SL$.SL$)).programpoint);
                        sysout().outimage();
                    }
                }
                sysout().outtext(new TXT$("- l."));
                sysout().outint(p$line,4);
                sysout().outtext(new TXT$(": "));
                sysout().outtext(p$wher);
                sysout().outimage();
                if(VALUE$(TXTREL$NE(p$t,new TXT$("x.virtproc")))) {
                    sysout().outtext(new TXT$(" - WRONG match to X'virtproc, should be: "));
                }
                sysout().outtext(p$t);
                sysout().outimage();
            }
        }
        EBLK();
        return(this);
    }
    public static PROGINFO$ INFO$=new PROGINFO$("simtst98.sim","Procedure virtproc",1,226,35,227,37,228,42,229,47,230,51,231,54,232,56,233,59,234,66,235);
}
