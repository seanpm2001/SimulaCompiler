package simulaTestBatch;
// Simula-Beta-0.3 Compiled at Sat Apr 13 09:06:30 CEST 2019
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class simtst98$z$virtproc extends BASICIO$ {
    public int prefixLevel() { return(0); }
    public int p$line;
    public TXT$ p$t;
    public TXT$ p$wher;
    private int $npar=0; // Number of actual parameters transmitted.
    public simtst98$z$virtproc setPar(Object param) {
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
    public simtst98$z$virtproc(RTObject$ SL$)
    { super(SL$); }
    public simtst98$z$virtproc(RTObject$ SL$,int sp$line,TXT$ sp$t,TXT$ sp$wher) {
        super(SL$);
        this.p$line = sp$line;
        this.p$t = sp$t;
        this.p$wher = sp$wher;
        BBLK();
        STM$();
    }
    public simtst98$z$virtproc STM$() {
        if(VALUE$((TXTREL$NE(p$t,new TXT$("z.virtproc"))|(((simtst98)(CUR$.SL$.SL$)).trace)))) {
            {
                if(VALUE$(TXTREL$NE(p$t,new TXT$("z.virtproc")))) {
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
                if(VALUE$(TXTREL$NE(p$t,new TXT$("z.virtproc")))) {
                    sysout().outtext(new TXT$(" - WRONG match to Z'virtproc, should be: "));
                }
                sysout().outtext(p$t);
                sysout().outimage();
            }
        }
        EBLK();
        return(this);
    }
    public static PROGINFO$ INFO$=new PROGINFO$("simtst98.sim","Procedure virtproc",1,285,35,286,37,287,42,288,47,289,51,290,54,291,56,292,59,293,66,294);
}
