package simulaTestPrograms;
// Simula-Beta-0.3 Compiled at Sun Apr 21 21:32:50 CEST 2019
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class Docking$PBLK12$Warehouse$drawText extends BASICIO$ {
    public int prefixLevel() { return(0); }
    public TXT$ p$str;
    public double p$pos;
    public double p$line;
    private int $npar=0; // Number of actual parameters transmitted.
    public Docking$PBLK12$Warehouse$drawText setPar(Object param) {
        try {
            switch($npar++) {
                case 0: p$str=(TXT$)objectValue(param); break;
                case 1: p$pos=doubleValue(param); break;
                case 2: p$line=doubleValue(param); break;
                default: throw new RuntimeException("Wrong number of parameters");
            }
        }
        catch(ClassCastException e) { throw new RuntimeException("Wrong type of parameter: "+$npar+" "+param,e);}
        return(this);
    }
    public Docking$PBLK12$Warehouse$drawText(RTObject$ SL$)
    { super(SL$); }
    public Docking$PBLK12$Warehouse$drawText(RTObject$ SL$,TXT$ sp$str,double sp$pos,double sp$line) {
        super(SL$);
        this.p$str = sp$str;
        this.p$pos = sp$pos;
        this.p$line = sp$line;
        BBLK();
        STM$();
    }
    public Docking$PBLK12$Warehouse$drawText STM$() {
        new TextElement$(((Docking$PBLK12)(CUR$.SL$.SL$)),p$str,p$pos,p$line).STM$();
        EBLK();
        return(this);
    }
    public static PROGINFO$ INFO$=new PROGINFO$("Docking.sim","Procedure drawText",1,58,35,59,39,59);
}
