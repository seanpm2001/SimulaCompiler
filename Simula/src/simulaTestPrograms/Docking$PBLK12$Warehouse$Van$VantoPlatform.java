// JavaLine 1 <== SourceLine 107
package simulaTestPrograms;
// Simula-Beta-0.3 Compiled at Sun May 05 10:36:39 CEST 2019
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class Docking$PBLK12$Warehouse$Van$VantoPlatform extends BASICIO$ {
    // ProcedureDeclaration: BlockKind=Procedure, BlockLevel=4, firstLine=107, lastLine=119, hasLocalClasses=false, System=false
    public int prefixLevel() { return(0); }
    // Declare parameters as attributes
    // Declare locals as attributes
    // Normal Constructor
    public Docking$PBLK12$Warehouse$Van$VantoPlatform(RTObject$ SL$) {
        super(SL$);
        // Parameter assignment to locals
        BBLK();
        // Declaration Code
        TRACE_BEGIN_DCL$("VantoPlatform",107);
        STM$();
    }
    // Procedure Statements
    public Docking$PBLK12$Warehouse$Van$VantoPlatform STM$() {
        TRACE_BEGIN_STM$("VantoPlatform",107);
        // JavaLine 23 <== SourceLine 108
        ((Docking$PBLK12$Warehouse$Van)(CUR$.SL$)).vanShape$3.setColor(((Docking$PBLK12)(CUR$.SL$.SL$.SL$)).black$1);
        // JavaLine 25 <== SourceLine 109
        ((Docking$PBLK12$Warehouse$Van)(CUR$.SL$)).vanShape$3.moveTo(((double)(((Docking$PBLK12$Warehouse)(CUR$.SL$.SL$)).drPos$2)),((double)(((Docking$PBLK12$Warehouse)(CUR$.SL$.SL$)).qLine$2)),((double)(((Docking$PBLK12$Warehouse$Van)(CUR$.SL$)).speed$3)));
        // JavaLine 27 <== SourceLine 110
        new Docking$PBLK12$Warehouse$Van$AnvanceQueue(((Docking$PBLK12$Warehouse$Van)(CUR$.SL$)));
        // JavaLine 29 <== SourceLine 111
        if(VALUE$((((Docking$PBLK12$Warehouse)(CUR$.SL$.SL$)).qLength$2>(0)))) {
            ((Docking$PBLK12$Warehouse)(CUR$.SL$.SL$)).qLength$2=(((Docking$PBLK12$Warehouse)(CUR$.SL$.SL$)).qLength$2-(1));
        }
        // JavaLine 33 <== SourceLine 113
        ((Docking$PBLK12$Warehouse$Van)(CUR$.SL$)).dLine$3=((((Docking$PBLK12$Warehouse)(CUR$.SL$.SL$)).dLine1$2+(((((Docking$PBLK12$Warehouse$Van)(CUR$.SL$)).myDock$3.p2$nr-(1))*(20))))-(5));
        // JavaLine 35 <== SourceLine 114
        ((Docking$PBLK12$Warehouse$Van)(CUR$.SL$)).vanShape$3.moveTo(((double)(((Docking$PBLK12$Warehouse)(CUR$.SL$.SL$)).drPos$2)),((double)((((Docking$PBLK12$Warehouse$Van)(CUR$.SL$)).dLine$3+(5)))),((double)(((Docking$PBLK12$Warehouse$Van)(CUR$.SL$)).speed$3)));
        // JavaLine 37 <== SourceLine 116
        ((Docking$PBLK12$Warehouse$Van)(CUR$.SL$)).dLine$3=(((Docking$PBLK12$Warehouse)(CUR$.SL$.SL$)).dLine1$2+(((((Docking$PBLK12$Warehouse$Van)(CUR$.SL$)).myDock$3.p2$nr-(1))*(20))));
        // JavaLine 39 <== SourceLine 117
        ((Docking$PBLK12$Warehouse$Van)(CUR$.SL$)).vanShape$3.moveTo(((double)(((Docking$PBLK12$Warehouse)(CUR$.SL$.SL$)).pPos$2)),((double)(((Docking$PBLK12$Warehouse$Van)(CUR$.SL$)).dLine$3)),((double)(((Docking$PBLK12$Warehouse$Van)(CUR$.SL$)).speed$3)));
        // JavaLine 41 <== SourceLine 118
        new Docking$PBLK12$trace(((Docking$PBLK12)(CUR$.SL$.SL$.SL$)),CONC(CONC(new TXT$("Van["),objectTraceIdentifier()),new TXT$("] is moved to plattform ...")));
        TRACE_END_STM$("VantoPlatform",118);
        EBLK();
        return(this);
    } // End of Procedure BODY
    public static PROGINFO$ INFO$=new PROGINFO$("Docking.sim","Procedure VantoPlatform",1,107,23,108,25,109,27,110,29,111,33,113,35,114,37,116,39,117,41,118,46,119);
} // End of Procedure
