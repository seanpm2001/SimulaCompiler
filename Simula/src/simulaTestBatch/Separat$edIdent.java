package simulaTestBatch;
// Simula-Beta-0.3 Compiled at Sun Apr 21 13:18:11 CEST 2019
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class Separat$edIdent extends BASICIO$ {
    public int prefixLevel() { return(0); }
    public TXT$ RESULT$;
    public Object RESULT$() { return(RESULT$); }
    public char p$id;
    public int p$n;
    TXT$ t=null;
    private int $npar=0; // Number of actual parameters transmitted.
    public Separat$edIdent setPar(Object param) {
        try {
            switch($npar++) {
                case 0: p$id=(char)objectValue(param); break;
                case 1: p$n=intValue(param); break;
                default: throw new RuntimeException("Wrong number of parameters");
            }
        }
        catch(ClassCastException e) { throw new RuntimeException("Wrong type of parameter: "+$npar+" "+param,e);}
        return(this);
    }
    public Separat$edIdent(RTObject$ SL$)
    { super(SL$); }
    public Separat$edIdent(RTObject$ SL$,char sp$id,int sp$n) {
        super(SL$);
        this.p$id = sp$id;
        this.p$n = sp$n;
        BBLK();
        STM$();
    }
    public Separat$edIdent STM$() {
        t=copy(copy(new TXT$("(Xn)")));
        TXT$.putchar(TXT$.sub(t,2,1),p$id);
        TXT$.putchar(TXT$.sub(t,3,1),char$((rank('0')+(p$n))));
        RESULT$=t;
        EBLK();
        return(this);
    }
    public static PROGINFO$ INFO$=new PROGINFO$("Separat.sim","Procedure edIdent",1,9,12,10,37,11,39,12,41,13,45,14);
}
