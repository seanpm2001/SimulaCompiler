// JavaLine 1 <== SourceLine 1041
package simuletta;
// Simula-1.0 Compiled at Sun Apr 12 14:26:02 CEST 2020
import simula.runtime.*;
@SuppressWarnings("unchecked")
public final class Common$SubBlock1030$SubBlock1041 extends BASICIO$ {
    // SubBlock: Kind=SubBlock, BlockLevel=3, firstLine=1041, lastLine=1391, hasLocalClasses=false, System=false
    // Declare local labels
    // JavaLine 9 <== SourceLine 1090
    final LABQNT$ SETem=new LABQNT$(this,1,"SETem"); // Local Label #1=SETem
    // Declare locals as attributes
    // JavaLine 12 <== SourceLine 1041
    int i=0;
    char cs=0;
    TXT$ t=null;
    TXT$ listname=null;
    TXT$ feoptions=null;
    // Normal Constructor
    public Common$SubBlock1030$SubBlock1041(RTObject$ staticLink) {
        super(staticLink);
        BBLK();
        // Declaration Code
    }
    // SubBlock Statements
    public RTObject$ STM$() {
        Common$SubBlock1030$SubBlock1041 THIS$=(Common$SubBlock1030$SubBlock1041)CUR$;
        LOOP$:while(JTX$>=0) {
            try {
                JUMPTABLE$(JTX$); // For ByteCode Engineering
                // JavaLine 30 <== SourceLine 1043
                ((Common)(CUR$.SL$.SL$)).TRC_info$1=new Head$(((Common)(CUR$.SL$.SL$))).STM$();
                ;
                ;
                // JavaLine 34 <== SourceLine 1045
                ((Common)(CUR$.SL$.SL$)).TRC_init$1=new Head$(((Common)(CUR$.SL$.SL$))).STM$();
                ;
                // JavaLine 37 <== SourceLine 1049
                ((Common)(CUR$.SL$.SL$)).modset$1=new Head$(((Common)(CUR$.SL$.SL$))).STM$();
                ;
                // JavaLine 40 <== SourceLine 1050
                ((Common)(CUR$.SL$.SL$)).qntset$1=new Head$(((Common)(CUR$.SL$.SL$))).STM$();
                ;
                // JavaLine 43 <== SourceLine 1055
                ((Common)(CUR$.SL$.SL$)).L2name$1=getTextInfo(7);
                ;
                // JavaLine 46 <== SourceLine 1056
                ((Common)(CUR$.SL$.SL$)).nscodename$1=getTextInfo(4);
                ;
                // JavaLine 49 <== SourceLine 1058
                ((Common)(CUR$.SL$.SL$)).recomp$1=(getIntInfo(22)==(1));
                ;
                // JavaLine 52 <== SourceLine 1059
                ((Common)(CUR$.SL$.SL$)).GenerateScode$1=(getIntInfo(1)==(1));
                ;
                // JavaLine 55 <== SourceLine 1061
                ((Common)(CUR$.SL$.SL$)).timestamp$1=datetime();
                ;
                // JavaLine 58 <== SourceLine 1062
                ((Common)(CUR$.SL$.SL$)).maxerrno$1=getIntInfo(4);
                ;
                // JavaLine 61 <== SourceLine 1063
                ((Common)(CUR$.SL$.SL$)).GiveNotes$1=(getIntInfo(5)==(0));
                ;
                // JavaLine 64 <== SourceLine 1064
                ((Common)(CUR$.SL$.SL$)).simob_level$1=getIntInfo(30);
                ;
                // JavaLine 67 <== SourceLine 1065
                ((Common)(CUR$.SL$.SL$)).simob_descr$1=(((Common)(CUR$.SL$.SL$)).simob_level$1>(0));
                ;
                // JavaLine 70 <== SourceLine 1066
                ((Common)(CUR$.SL$.SL$)).simob_entity$1=(((Common)(CUR$.SL$.SL$)).simob_level$1>=(2));
                ;
                ((Common)(CUR$.SL$.SL$)).simob_const$1=(((Common)(CUR$.SL$.SL$)).simob_level$1>=(3));
                ;
                // JavaLine 75 <== SourceLine 1070
                listname=getTextInfo(2);
                ;
                ((Common)(CUR$.SL$.SL$)).listlength$1=TXT$.length(sysout().image);
                ;
                // JavaLine 80 <== SourceLine 1071
                if(VALUE$(TRF_NE(listname,null))) {
                    // JavaLine 82 <== SourceLine 1072
                    {
                        t=copy(copy(listname));
                        ;
                        t=lowcase(t);
                        ;
                        // JavaLine 88 <== SourceLine 1073
                        if(VALUE$(TXTREL$EQ(t,new TXT$("sysout")))) {
                            ((Common)(CUR$.SL$.SL$)).ListFile$1=sysout();
                        } else
                        // JavaLine 92 <== SourceLine 1074
                        {
                            // BEGIN INSPECTION 
                            ((Common)(CUR$.SL$.SL$)).inspect$1074$19$1=new PrintFile$(((BASICIO$)CTX$),listname).STM$();
                            if(((Common)(CUR$.SL$.SL$)).inspect$1074$19$1!=null) // INSPECT inspect$1074$19
                            // JavaLine 97 <== SourceLine 1075
                            {
                                ((Common)(CUR$.SL$.SL$)).listlength$1=getIntInfo(7);
                                ;
                                t=blanks(((Common)(CUR$.SL$.SL$)).listlength$1);
                                ;
                                // JavaLine 103 <== SourceLine 1076
                                if(VALUE$((!(((Common)(CUR$.SL$.SL$)).inspect$1074$19$1.open(t))))) {
                                    // JavaLine 105 <== SourceLine 1077
                                    {
                                        ((Common)(CUR$.SL$.SL$)).listlength$1=0;
                                        ;
                                        // JavaLine 109 <== SourceLine 1078
                                        new Common$error1(((Common)(CUR$.SL$.SL$)),(-(366)),listname);
                                    }
                                } else
                                ((Common)(CUR$.SL$.SL$)).ListFile$1=((PrintFile$)((Common)(CUR$.SL$.SL$)).inspect$1074$19$1);
                                ;
                            }
                        }
                        ;
                    }
                }
                ;
                // JavaLine 121 <== SourceLine 1081
                ((Common)(CUR$.SL$.SL$)).listingon$1=(((Common)(CUR$.SL$.SL$)).ListFile$1!=(null));
                ;
                // JavaLine 124 <== SourceLine 1085
                t=getTextInfo(16);
                ;
                // JavaLine 127 <== SourceLine 1086
                while(TXT$.more(t)) {
                    if(VALUE$((TXT$.getchar(t)==(':')))) {
                        // JavaLine 130 <== SourceLine 1087
                        {
                            if(VALUE$(TXT$.more(t))) {
                                feoptions=copy(copy(TXT$.sub(t,TXT$.pos(t),Math.addExact(Math.subtractExact(TXT$.length(t),TXT$.pos(t)),1))));
                            }
                            ;
                            // JavaLine 136 <== SourceLine 1088
                            t=TXT$.sub(t,1,Math.subtractExact(TXT$.pos(t),2));
                            ;
                            GOTO$(SETem); // GOTO EVALUATED LABEL
                            ;
                        }
                    }
                }
                ;
                // JavaLine 145 <== SourceLine 1090
                LABEL$(1,"SETem");
                new Common$SetSelectors(((Common)(CUR$.SL$.SL$)),t,0);
                ;
                // JavaLine 149 <== SourceLine 1091
                while(TXT$.more(feoptions)) {
                    new Common$setopt(((Common)(CUR$.SL$.SL$)),TXT$.getchar(feoptions),1);
                }
                ;
                // JavaLine 154 <== SourceLine 1093
                ((Common)(CUR$.SL$.SL$)).termstatus$1=3;
                ;
                // JavaLine 157 <== SourceLine 1094
                ((Common)(CUR$.SL$.SL$)).currentpass$1=1;
                ;
                // JavaLine 160 <== SourceLine 1097
                ((Common)(CUR$.SL$.SL$)).leftintbuf$1=blanks(12);
                ;
                // JavaLine 163 <== SourceLine 1098
                ((Common)(CUR$.SL$.SL$)).leftsintbuf$1=blanks(7);
                ;
                // JavaLine 166 <== SourceLine 1100
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_NULL$1,new TXT$("NULL"));
                ;
                // JavaLine 169 <== SourceLine 1101
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_RECORD$1,new TXT$("RECORD"));
                ;
                // JavaLine 172 <== SourceLine 1102
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_LSHIFTL$1,new TXT$("LSHIFTL"));
                ;
                // JavaLine 175 <== SourceLine 1103
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_PREFIX$1,new TXT$("PREFIX"));
                ;
                // JavaLine 178 <== SourceLine 1104
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ATTR$1,new TXT$("ATTR"));
                ;
                // JavaLine 181 <== SourceLine 1105
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_LSHIFTA$1,new TXT$("LSHIFTA"));
                ;
                // JavaLine 184 <== SourceLine 1106
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_REP$1,new TXT$("REP"));
                ;
                // JavaLine 187 <== SourceLine 1107
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ALT$1,new TXT$("ALT"));
                ;
                // JavaLine 190 <== SourceLine 1108
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_FIXREP$1,new TXT$("FIXREP"));
                ;
                // JavaLine 193 <== SourceLine 1109
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ENDRECORD$1,new TXT$("ENDRECORD"));
                ;
                // JavaLine 196 <== SourceLine 1110
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_C_RECORD$1,new TXT$("C_RECORD"));
                ;
                // JavaLine 199 <== SourceLine 1111
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_TEXT$1,new TXT$("TEXT"));
                ;
                // JavaLine 202 <== SourceLine 1112
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_C_CHAR$1,new TXT$("C_CHAR"));
                ;
                // JavaLine 205 <== SourceLine 1113
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_C_INT$1,new TXT$("C_INT"));
                ;
                // JavaLine 208 <== SourceLine 1114
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_C_SIZE$1,new TXT$("C_SIZE"));
                ;
                // JavaLine 211 <== SourceLine 1116
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_C_REAL$1,new TXT$("C_REAL"));
                ;
                // JavaLine 214 <== SourceLine 1117
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_C_LREAL$1,new TXT$("C_LREAL"));
                ;
                // JavaLine 217 <== SourceLine 1118
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_C_AADDR$1,new TXT$("C_AADDR"));
                ;
                // JavaLine 220 <== SourceLine 1119
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_C_OADDR$1,new TXT$("C_OADDR"));
                ;
                // JavaLine 223 <== SourceLine 1121
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_C_GADDR$1,new TXT$("C_GADDR"));
                ;
                // JavaLine 226 <== SourceLine 1122
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_C_PADDR$1,new TXT$("C_PADDR"));
                ;
                // JavaLine 229 <== SourceLine 1123
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_C_DOT$1,new TXT$("C_DOT"));
                ;
                // JavaLine 232 <== SourceLine 1124
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_C_RADDR$1,new TXT$("C_RADDR"));
                ;
                // JavaLine 235 <== SourceLine 1126
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_NOBODY$1,new TXT$("NOBODY"));
                ;
                // JavaLine 238 <== SourceLine 1127
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ANONE$1,new TXT$("ANONE"));
                ;
                // JavaLine 241 <== SourceLine 1128
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ONONE$1,new TXT$("ONONE"));
                ;
                // JavaLine 244 <== SourceLine 1129
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_GNONE$1,new TXT$("GNONE"));
                ;
                // JavaLine 247 <== SourceLine 1131
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_NOWHERE$1,new TXT$("NOWHERE"));
                ;
                // JavaLine 250 <== SourceLine 1132
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_TRUE$1,new TXT$("TRUE"));
                ;
                // JavaLine 253 <== SourceLine 1133
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_FALSE$1,new TXT$("FALSE"));
                ;
                // JavaLine 256 <== SourceLine 1134
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_PROFILE$1,new TXT$("PROFILE"));
                ;
                // JavaLine 259 <== SourceLine 1136
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_KNOWN$1,new TXT$("KNOWN"));
                ;
                // JavaLine 262 <== SourceLine 1137
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_SYSTEM$1,new TXT$("SYSTEM"));
                ;
                // JavaLine 265 <== SourceLine 1138
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_EXTERNAL$1,new TXT$("EXTERNAL"));
                ;
                // JavaLine 268 <== SourceLine 1139
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_IMPORT$1,new TXT$("IMPORT"));
                ;
                // JavaLine 271 <== SourceLine 1141
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_EXPORT$1,new TXT$("EXPORT"));
                ;
                // JavaLine 274 <== SourceLine 1142
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_EXIT$1,new TXT$("EXIT"));
                ;
                // JavaLine 277 <== SourceLine 1143
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ENDPROFILE$1,new TXT$("ENDPROFILE"));
                ;
                // JavaLine 280 <== SourceLine 1144
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ROUTINESPEC$1,new TXT$("ROUTINESPEC"));
                ;
                // JavaLine 283 <== SourceLine 1146
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ROUTINE$1,new TXT$("ROUTINE"));
                ;
                // JavaLine 286 <== SourceLine 1147
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_LOCAL$1,new TXT$("LOCAL"));
                ;
                // JavaLine 289 <== SourceLine 1148
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ENDROUTINE$1,new TXT$("ENDROUTINE"));
                ;
                // JavaLine 292 <== SourceLine 1149
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_MODULE$1,new TXT$("MODULE"));
                ;
                // JavaLine 295 <== SourceLine 1151
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_EXISTING$1,new TXT$("EXISTING"));
                ;
                // JavaLine 298 <== SourceLine 1152
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_TAG$1,new TXT$("TAG"));
                ;
                // JavaLine 301 <== SourceLine 1153
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_BODY$1,new TXT$("BODY"));
                ;
                // JavaLine 304 <== SourceLine 1154
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ENDMODULE$1,new TXT$("ENDMODULE"));
                ;
                // JavaLine 307 <== SourceLine 1156
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_LABELSPEC$1,new TXT$("LABELSPEC"));
                ;
                // JavaLine 310 <== SourceLine 1157
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_LABEL$1,new TXT$("LABEL"));
                ;
                // JavaLine 313 <== SourceLine 1158
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_RANGE$1,new TXT$("RANGE"));
                ;
                // JavaLine 316 <== SourceLine 1159
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_GLOBAL$1,new TXT$("GLOBAL"));
                ;
                // JavaLine 319 <== SourceLine 1161
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_INIT$1,new TXT$("INIT"));
                ;
                // JavaLine 322 <== SourceLine 1162
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_CONSTSPEC$1,new TXT$("CONSTSPEC"));
                ;
                // JavaLine 325 <== SourceLine 1163
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_CONST$1,new TXT$("CONST"));
                ;
                // JavaLine 328 <== SourceLine 1164
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_DELETE$1,new TXT$("DELETE"));
                ;
                // JavaLine 331 <== SourceLine 1166
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_FDEST$1,new TXT$("FDEST"));
                ;
                // JavaLine 334 <== SourceLine 1167
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_BDEST$1,new TXT$("BDEST"));
                ;
                // JavaLine 337 <== SourceLine 1168
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_SAVE$1,new TXT$("SAVE"));
                ;
                // JavaLine 340 <== SourceLine 1169
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_RESTORE$1,new TXT$("RESTORE"));
                ;
                // JavaLine 343 <== SourceLine 1171
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_BSEG$1,new TXT$("BSEG"));
                ;
                // JavaLine 346 <== SourceLine 1172
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ESEG$1,new TXT$("ESEG"));
                ;
                // JavaLine 349 <== SourceLine 1173
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_SKIPIF$1,new TXT$("SKIPIF"));
                ;
                // JavaLine 352 <== SourceLine 1174
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ENDSKIP$1,new TXT$("ENDSKIP"));
                ;
                // JavaLine 355 <== SourceLine 1176
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_IF$1,new TXT$("IF"));
                ;
                // JavaLine 358 <== SourceLine 1177
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ELSE$1,new TXT$("ELSE"));
                ;
                // JavaLine 361 <== SourceLine 1178
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ENDIF$1,new TXT$("ENDIF"));
                ;
                // JavaLine 364 <== SourceLine 1180
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_RSHIFTL$1,new TXT$("RSHIFTL"));
                ;
                // JavaLine 367 <== SourceLine 1182
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_PRECALL$1,new TXT$("PRECALL"));
                ;
                // JavaLine 370 <== SourceLine 1183
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ASSPAR$1,new TXT$("ASSPAR"));
                ;
                // JavaLine 373 <== SourceLine 1184
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ASSREP$1,new TXT$("ASSREP"));
                ;
                // JavaLine 376 <== SourceLine 1185
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_CALL$1,new TXT$("CALL"));
                ;
                // JavaLine 379 <== SourceLine 1187
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_FETCH$1,new TXT$("FETCH"));
                ;
                // JavaLine 382 <== SourceLine 1188
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_REFER$1,new TXT$("REFER"));
                ;
                // JavaLine 385 <== SourceLine 1189
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_DEREF$1,new TXT$("DEREF"));
                ;
                // JavaLine 388 <== SourceLine 1190
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_SELECT$1,new TXT$("SELECT"));
                ;
                // JavaLine 391 <== SourceLine 1192
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_REMOTE$1,new TXT$("REMOTE"));
                ;
                // JavaLine 394 <== SourceLine 1193
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_LOCATE$1,new TXT$("LOCATE"));
                ;
                // JavaLine 397 <== SourceLine 1194
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_INDEX$1,new TXT$("INDEX"));
                ;
                // JavaLine 400 <== SourceLine 1195
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_INCO$1,new TXT$("INCO"));
                ;
                // JavaLine 403 <== SourceLine 1197
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_DECO$1,new TXT$("DECO"));
                ;
                // JavaLine 406 <== SourceLine 1198
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_PUSH$1,new TXT$("PUSH"));
                ;
                // JavaLine 409 <== SourceLine 1199
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_PUSHC$1,new TXT$("PUSHC"));
                ;
                // JavaLine 412 <== SourceLine 1200
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_PUSHLEN$1,new TXT$("PUSHLEN"));
                ;
                // JavaLine 415 <== SourceLine 1202
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_DUP$1,new TXT$("DUP"));
                ;
                // JavaLine 418 <== SourceLine 1203
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_POP$1,new TXT$("POP"));
                ;
                // JavaLine 421 <== SourceLine 1204
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_EMPTY$1,new TXT$("EMPTY"));
                ;
                // JavaLine 424 <== SourceLine 1205
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_SETOBJ$1,new TXT$("SETOBJ"));
                ;
                // JavaLine 427 <== SourceLine 1207
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_GETOBJ$1,new TXT$("GETOBJ"));
                ;
                // JavaLine 430 <== SourceLine 1208
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ACCESS$1,new TXT$("ACCESS"));
                ;
                // JavaLine 433 <== SourceLine 1209
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_FJUMP$1,new TXT$("FJUMP"));
                ;
                // JavaLine 436 <== SourceLine 1210
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_BJUMP$1,new TXT$("BJUMP"));
                ;
                // JavaLine 439 <== SourceLine 1212
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_FJUMPIF$1,new TXT$("FJUMPIF"));
                ;
                // JavaLine 442 <== SourceLine 1213
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_BJUMPIF$1,new TXT$("BJUMPIF"));
                ;
                // JavaLine 445 <== SourceLine 1214
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_SWITCH$1,new TXT$("SWITCH"));
                ;
                // JavaLine 448 <== SourceLine 1215
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_GOTO$1,new TXT$("GOTO"));
                ;
                // JavaLine 451 <== SourceLine 1217
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_T_INITO$1,new TXT$("T_INITO"));
                ;
                // JavaLine 454 <== SourceLine 1218
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_T_GETO$1,new TXT$("T_GETO"));
                ;
                // JavaLine 457 <== SourceLine 1219
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_T_SETO$1,new TXT$("T_SETO"));
                ;
                // JavaLine 460 <== SourceLine 1220
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ADD$1,new TXT$("ADD"));
                ;
                // JavaLine 463 <== SourceLine 1222
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_SUB$1,new TXT$("SUB"));
                ;
                // JavaLine 466 <== SourceLine 1223
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_MULT$1,new TXT$("MULT"));
                ;
                // JavaLine 469 <== SourceLine 1224
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_DIV$1,new TXT$("DIV"));
                ;
                // JavaLine 472 <== SourceLine 1225
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_REM$1,new TXT$("REM"));
                ;
                // JavaLine 475 <== SourceLine 1227
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_NEG$1,new TXT$("NEG"));
                ;
                // JavaLine 478 <== SourceLine 1228
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_AND$1,new TXT$("AND"));
                ;
                // JavaLine 481 <== SourceLine 1229
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_OR$1,new TXT$("OR"));
                ;
                // JavaLine 484 <== SourceLine 1230
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_XOR$1,new TXT$("XOR"));
                ;
                // JavaLine 487 <== SourceLine 1232
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_IMP$1,new TXT$("IMP"));
                ;
                // JavaLine 490 <== SourceLine 1233
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_EQV$1,new TXT$("EQV"));
                ;
                // JavaLine 493 <== SourceLine 1234
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_NOT$1,new TXT$("NOT"));
                ;
                // JavaLine 496 <== SourceLine 1235
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_DIST$1,new TXT$("DIST"));
                ;
                // JavaLine 499 <== SourceLine 1237
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ASSIGN$1,new TXT$("ASSIGN"));
                ;
                // JavaLine 502 <== SourceLine 1238
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_UPDATE$1,new TXT$("UPDATE"));
                ;
                // JavaLine 505 <== SourceLine 1239
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_CONVERT$1,new TXT$("CONVERT"));
                ;
                // JavaLine 508 <== SourceLine 1240
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_SYSINSERT$1,new TXT$("SYSINSERT"));
                ;
                // JavaLine 511 <== SourceLine 1242
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_INSERT$1,new TXT$("INSERT"));
                ;
                // JavaLine 514 <== SourceLine 1243
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ZEROAREA$1,new TXT$("ZEROAREA"));
                ;
                // JavaLine 517 <== SourceLine 1244
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_INITAREA$1,new TXT$("INITAREA"));
                ;
                // JavaLine 520 <== SourceLine 1245
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_COMPARE$1,new TXT$("COMPARE"));
                ;
                // JavaLine 523 <== SourceLine 1247
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_LT$1,new TXT$("LT"));
                ;
                // JavaLine 526 <== SourceLine 1248
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_LE$1,new TXT$("LE"));
                ;
                // JavaLine 529 <== SourceLine 1249
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_EQ$1,new TXT$("EQ"));
                ;
                // JavaLine 532 <== SourceLine 1250
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_GE$1,new TXT$("GE"));
                ;
                // JavaLine 535 <== SourceLine 1252
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_GT$1,new TXT$("GT"));
                ;
                // JavaLine 538 <== SourceLine 1253
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_NE$1,new TXT$("NE"));
                ;
                // JavaLine 541 <== SourceLine 1254
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_EVAL$1,new TXT$("EVAL"));
                ;
                // JavaLine 544 <== SourceLine 1255
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_INFO$1,new TXT$("INFO"));
                ;
                // JavaLine 547 <== SourceLine 1257
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_LINE$1,new TXT$("LINE"));
                ;
                // JavaLine 550 <== SourceLine 1258
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_SETSWITCH$1,new TXT$("SETSWITCH"));
                ;
                // JavaLine 553 <== SourceLine 1260
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_RSHIFTA$1,new TXT$("RSHIFTA"));
                ;
                // JavaLine 556 <== SourceLine 1262
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_PROGRAM$1,new TXT$("PROGRAM"));
                ;
                // JavaLine 559 <== SourceLine 1263
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_MAIN$1,new TXT$("MAIN"));
                ;
                // JavaLine 562 <== SourceLine 1265
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ENDPROGRAM$1,new TXT$("ENDPROGRAM"));
                ;
                // JavaLine 565 <== SourceLine 1266
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_DSIZE$1,new TXT$("DSIZE"));
                ;
                // JavaLine 568 <== SourceLine 1267
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_SDEST$1,new TXT$("SDEST"));
                ;
                // JavaLine 571 <== SourceLine 1268
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_RUPDATE$1,new TXT$("RUPDATE"));
                ;
                // JavaLine 574 <== SourceLine 1270
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ASSCALL$1,new TXT$("ASSCALL"));
                ;
                // JavaLine 577 <== SourceLine 1271
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_CALL_TOS$1,new TXT$("CALL_TOS"));
                ;
                // JavaLine 580 <== SourceLine 1272
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_DINITAREA$1,new TXT$("DINITAREA"));
                ;
                // JavaLine 583 <== SourceLine 1273
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_NOSIZE$1,new TXT$("NOSIZE"));
                ;
                // JavaLine 586 <== SourceLine 1275
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_POPALL$1,new TXT$("POPALL"));
                ;
                // JavaLine 589 <== SourceLine 1276
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_REPCALL$1,new TXT$("REPCALL"));
                ;
                // JavaLine 592 <== SourceLine 1277
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_INTERFACE$1,new TXT$("INTERFACE"));
                ;
                // JavaLine 595 <== SourceLine 1278
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_MACRO$1,new TXT$("MACRO"));
                ;
                // JavaLine 598 <== SourceLine 1280
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_MARK$1,new TXT$("MARK"));
                ;
                // JavaLine 601 <== SourceLine 1281
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_MPAR$1,new TXT$("MPAR"));
                ;
                // JavaLine 604 <== SourceLine 1282
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ENDMACRO$1,new TXT$("ENDMACRO"));
                ;
                // JavaLine 607 <== SourceLine 1283
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_MCALL$1,new TXT$("MCALL"));
                ;
                // JavaLine 610 <== SourceLine 1285
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_PUSHV$1,new TXT$("PUSHV"));
                ;
                // JavaLine 613 <== SourceLine 1286
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_SELECTV$1,new TXT$("SELECTV"));
                ;
                // JavaLine 616 <== SourceLine 1287
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_REMOTEV$1,new TXT$("REMOTEV"));
                ;
                // JavaLine 619 <== SourceLine 1288
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_INDEXV$1,new TXT$("INDEXV"));
                ;
                // JavaLine 622 <== SourceLine 1290
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ACCESSV$1,new TXT$("ACCESSV"));
                ;
                // JavaLine 625 <== SourceLine 1291
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_DECL$1,new TXT$("DECL"));
                ;
                // JavaLine 628 <== SourceLine 1292
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_STMT$1,new TXT$("STMT"));
                ;
                // JavaLine 631 <== SourceLine 1298
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_BEGIN$1,new TXT$("BEGIN"));
                ;
                // JavaLine 634 <== SourceLine 1299
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_END$1,new TXT$("END"));
                ;
                // JavaLine 637 <== SourceLine 1300
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_VARIANT$1,new TXT$("VARIANT"));
                ;
                // JavaLine 640 <== SourceLine 1301
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_SHORT$1,new TXT$("SHORT"));
                ;
                // JavaLine 643 <== SourceLine 1303
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_LONG$1,new TXT$("LONG"));
                ;
                // JavaLine 646 <== SourceLine 1304
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_INTEGER$1,new TXT$("INTEGER"));
                ;
                // JavaLine 649 <== SourceLine 1305
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_SIZE$1,new TXT$("SIZE"));
                ;
                // JavaLine 652 <== SourceLine 1306
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_REAL$1,new TXT$("REAL"));
                ;
                // JavaLine 655 <== SourceLine 1308
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_CHAR$1,new TXT$("CHAR"));
                ;
                // JavaLine 658 <== SourceLine 1309
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_BOOLEAN$1,new TXT$("BOOLEAN"));
                ;
                // JavaLine 661 <== SourceLine 1310
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_REF$1,new TXT$("REF"));
                ;
                // JavaLine 664 <== SourceLine 1311
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_NAME$1,new TXT$("NAME"));
                ;
                // JavaLine 667 <== SourceLine 1313
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_FIELD$1,new TXT$("FIELD"));
                ;
                // JavaLine 670 <== SourceLine 1314
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_INFIX$1,new TXT$("INFIX"));
                ;
                // JavaLine 673 <== SourceLine 1315
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ENTRY$1,new TXT$("ENTRY"));
                ;
                // JavaLine 676 <== SourceLine 1316
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_NONE$1,new TXT$("NONE"));
                ;
                // JavaLine 679 <== SourceLine 1318
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_NONAME$1,new TXT$("NONAME"));
                ;
                // JavaLine 682 <== SourceLine 1319
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_NOFIELD$1,new TXT$("NOFIELD"));
                ;
                // JavaLine 685 <== SourceLine 1320
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_THEN$1,new TXT$("THEN"));
                ;
                // JavaLine 688 <== SourceLine 1321
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ELSIF$1,new TXT$("ELSIF"));
                ;
                // JavaLine 691 <== SourceLine 1323
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_QUA$1,new TXT$("QUA"));
                ;
                // JavaLine 694 <== SourceLine 1324
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_VAR$1,new TXT$("VAR"));
                ;
                // JavaLine 697 <== SourceLine 1325
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_REPEAT$1,new TXT$("REPEAT"));
                ;
                // JavaLine 700 <== SourceLine 1326
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_WHILE$1,new TXT$("WHILE"));
                ;
                // JavaLine 703 <== SourceLine 1328
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_DO$1,new TXT$("DO"));
                ;
                // JavaLine 706 <== SourceLine 1329
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ENDREPEAT$1,new TXT$("ENDREPEAT"));
                ;
                // JavaLine 709 <== SourceLine 1330
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_SYSRUT$1,new TXT$("SYSROUTINE"));
                ;
                // JavaLine 712 <== SourceLine 1331
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_DEFINE$1,new TXT$("DEFINE"));
                ;
                // JavaLine 715 <== SourceLine 1333
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_CASE$1,new TXT$("CASE"));
                ;
                // JavaLine 718 <== SourceLine 1334
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_WHEN$1,new TXT$("WHEN"));
                ;
                // JavaLine 721 <== SourceLine 1335
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_OTHERWISE$1,new TXT$("OTHERWISE"));
                ;
                // JavaLine 724 <== SourceLine 1336
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ENDCASE$1,new TXT$("ENDCASE"));
                ;
                // JavaLine 727 <== SourceLine 1338
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_VISIBLE$1,new TXT$("VISIBLE"));
                ;
                // JavaLine 730 <== SourceLine 1339
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ASSERT$1,new TXT$("ASSERT"));
                ;
                // JavaLine 733 <== SourceLine 1340
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_SKIP$1,new TXT$("SKIP"));
                ;
                // JavaLine 736 <== SourceLine 1346
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_INTVAL$1,new TXT$("INTVAL"));
                ;
                // JavaLine 739 <== SourceLine 1347
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_REALVAL$1,new TXT$("REALVAL"));
                ;
                // JavaLine 742 <== SourceLine 1348
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_LREALVAL$1,new TXT$("LREALVAL"));
                ;
                // JavaLine 745 <== SourceLine 1349
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_STRING$1,new TXT$("STRING"));
                ;
                // JavaLine 748 <== SourceLine 1351
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_COLON$1,new TXT$("COLON"));
                ;
                // JavaLine 751 <== SourceLine 1352
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_LPAR$1,new TXT$("LPAR"));
                ;
                // JavaLine 754 <== SourceLine 1353
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_RPAR$1,new TXT$("RPAR"));
                ;
                // JavaLine 757 <== SourceLine 1354
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_COMMA$1,new TXT$("COMMA"));
                ;
                // JavaLine 760 <== SourceLine 1356
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_DOT$1,new TXT$("DOT"));
                ;
                // JavaLine 763 <== SourceLine 1357
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ADDRESS$1,new TXT$("ADDRESS"));
                ;
                // JavaLine 766 <== SourceLine 1358
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_CHARS$1,new TXT$("CHARS"));
                ;
                // JavaLine 769 <== SourceLine 1359
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_PERCENT$1,new TXT$("PERCENT"));
                ;
                // JavaLine 772 <== SourceLine 1364
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_TRACE$1,new TXT$("TRACE"));
                ;
                // JavaLine 775 <== SourceLine 1365
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_NOEXPR$1,new TXT$("NOEXPR"));
                ;
                // JavaLine 778 <== SourceLine 1366
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_VARID$1,new TXT$("VARID"));
                ;
                // JavaLine 781 <== SourceLine 1367
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_VAREXPR$1,new TXT$("VAREXPR"));
                ;
                // JavaLine 784 <== SourceLine 1369
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_VARCALL$1,new TXT$("VARCALL"));
                ;
                // JavaLine 787 <== SourceLine 1370
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ARGLIST$1,new TXT$("ARGLIST"));
                ;
                // JavaLine 790 <== SourceLine 1371
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ENDVAR$1,new TXT$("ENDVAR"));
                ;
                // JavaLine 793 <== SourceLine 1372
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_IFEXPR$1,new TXT$("IFEXPR"));
                ;
                // JavaLine 796 <== SourceLine 1374
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_REPEXPR$1,new TXT$("REPEXPR"));
                ;
                // JavaLine 799 <== SourceLine 1375
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_DEFLAB$1,new TXT$("DEFLAB"));
                ;
                // JavaLine 802 <== SourceLine 1376
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_STRUCT$1,new TXT$("STRUCT"));
                ;
                // JavaLine 805 <== SourceLine 1377
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_ENDWHEN$1,new TXT$("ENDWHEN"));
                ;
                // JavaLine 808 <== SourceLine 1379
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_SIMPLE$1,new TXT$("SIMPLE"));
                ;
                // JavaLine 811 <== SourceLine 1380
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_DOTVAR$1,new TXT$("DOTVAR"));
                ;
                // JavaLine 814 <== SourceLine 1382
                new Common$DEFKEYWORD(((Common)(CUR$.SL$.SL$)),((Common)(CUR$.SL$.SL$)).S_IDENT$1,new TXT$("IDENT"));
                ;
                // JavaLine 817 <== SourceLine 1387
                ((Common)(CUR$.SL$.SL$)).nSymb$1=((Common)(CUR$.SL$.SL$)).S_MXMX$1;
                ;
                // JavaLine 820 <== SourceLine 1389
                new Common$initTypeTable(((Common)(CUR$.SL$.SL$)));
                ;
                break LOOP$;
            }
            catch(LABQNT$ q) {
                CUR$=THIS$;
                if(q.SL$!=CUR$) {
                    if(RT.Option.GOTO_TRACING) TRACE_GOTO("SubBlock1041:NON-LOCAL",q);
                    CUR$.STATE$=OperationalState.terminated;
                    if(RT.Option.GOTO_TRACING) TRACE_GOTO("SubBlock1041:RE-THROW",q);
                    throw(q);
                }
                if(RT.Option.GOTO_TRACING) TRACE_GOTO("SubBlock1041:LOCAL",q);
                JTX$=q.index; continue LOOP$; // EG. GOTO Lx
            }
        }
        EBLK();
        return(this);
    } // End of SubBlock Statements
    public static PROGINFO$ INFO$=new PROGINFO$("COMMON.sim","SubBlock SubBlock1041",1,1041,9,1090,12,1041,30,1043,34,1045,37,1049,40,1050,43,1055,46,1056,49,1058,52,1059,55,1061,58,1062,61,1063,64,1064,67,1065,70,1066,75,1070,80,1071,82,1072,88,1073,92,1074,97,1075,103,1076,105,1077,109,1078,121,1081,124,1085,127,1086,130,1087,136,1088,145,1090,149,1091,154,1093,157,1094,160,1097,163,1098,166,1100,169,1101,172,1102,175,1103,178,1104,181,1105,184,1106,187,1107,190,1108,193,1109,196,1110,199,1111,202,1112,205,1113,208,1114,211,1116,214,1117,217,1118,220,1119,223,1121,226,1122,229,1123,232,1124,235,1126,238,1127,241,1128,244,1129,247,1131,250,1132,253,1133,256,1134,259,1136,262,1137,265,1138,268,1139,271,1141,274,1142,277,1143,280,1144,283,1146,286,1147,289,1148,292,1149,295,1151,298,1152,301,1153,304,1154,307,1156,310,1157,313,1158,316,1159,319,1161,322,1162,325,1163,328,1164,331,1166,334,1167,337,1168,340,1169,343,1171,346,1172,349,1173,352,1174,355,1176,358,1177,361,1178,364,1180,367,1182,370,1183,373,1184,376,1185,379,1187,382,1188,385,1189,388,1190,391,1192,394,1193,397,1194,400,1195,403,1197,406,1198,409,1199,412,1200,415,1202,418,1203,421,1204,424,1205,427,1207,430,1208,433,1209,436,1210,439,1212,442,1213,445,1214,448,1215,451,1217,454,1218,457,1219,460,1220,463,1222,466,1223,469,1224,472,1225,475,1227,478,1228,481,1229,484,1230,487,1232,490,1233,493,1234,496,1235,499,1237,502,1238,505,1239,508,1240,511,1242,514,1243,517,1244,520,1245,523,1247,526,1248,529,1249,532,1250,535,1252,538,1253,541,1254,544,1255,547,1257,550,1258,553,1260,556,1262,559,1263,562,1265,565,1266,568,1267,571,1268,574,1270,577,1271,580,1272,583,1273,586,1275,589,1276,592,1277,595,1278,598,1280,601,1281,604,1282,607,1283,610,1285,613,1286,616,1287,619,1288,622,1290,625,1291,628,1292,631,1298,634,1299,637,1300,640,1301,643,1303,646,1304,649,1305,652,1306,655,1308,658,1309,661,1310,664,1311,667,1313,670,1314,673,1315,676,1316,679,1318,682,1319,685,1320,688,1321,691,1323,694,1324,697,1325,700,1326,703,1328,706,1329,709,1330,712,1331,715,1333,718,1334,721,1335,724,1336,727,1338,730,1339,733,1340,736,1346,739,1347,742,1348,745,1349,748,1351,751,1352,754,1353,757,1354,760,1356,763,1357,766,1358,769,1359,772,1364,775,1365,778,1366,781,1367,784,1369,787,1370,790,1371,793,1372,796,1374,799,1375,802,1376,805,1377,808,1379,811,1380,814,1382,817,1387,820,1389,839,1391);
} // End of SubBlock
