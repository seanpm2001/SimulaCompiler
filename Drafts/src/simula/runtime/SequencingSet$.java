/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simula.runtime;

import java.util.Comparator;
import java.util.TreeSet;

public class SequencingSet$ {
	private final TreeSet<EVENT_NOTICE$> sequencingSet;

	private Comparator<EVENT_NOTICE$> compareEvents=new Comparator<EVENT_NOTICE$>() {
		public int compare(EVENT_NOTICE$ first, EVENT_NOTICE$ second) {
			if(first.KEY < second.KEY) return(-1);
			if(first.KEY > second.KEY) return(+1);
			return(0);
		}
	};

	public SequencingSet$() {
		sequencingSet=new TreeSet<EVENT_NOTICE$>(compareEvents);
	}
	
	public boolean isEmpty() { 
		return(sequencingSet.isEmpty());
	}
	
	public void add(EVENT_NOTICE$ ev,boolean prior) {
		ev.KEY=ev.EVTIME;
		boolean done=sequencingSet.add(ev);
		while(!done) {
			ev.KEY=(prior)?Math.nextDown(ev.KEY):Math.nextUp(ev.KEY);
			done=sequencingSet.add(ev);
		}
		RT.ASSERT(done,"SequencingSet$.add");
	}
	
	public void remove(EVENT_NOTICE$ ev) {
		boolean done=sequencingSet.remove(ev);
		RT.ASSERT(done,"SequencingSet$.remove");
	}
	
	public EVENT_NOTICE$ pollFirst() {
		return(sequencingSet.pollFirst());
	}
	
	public EVENT_NOTICE$ first() {
		return(sequencingSet.first());
	}
	
	public EVENT_NOTICE$ nextAfter(EVENT_NOTICE$ ev) {
		EVENT_NOTICE$ nxt=sequencingSet.higher(ev);
		return(nxt);
	}


	public String edNEW_SQS() {
		//checkSQS();
		StringBuilder s = new StringBuilder();
//		EVENT_NOTICE$ x = sequencingSet.first();
		for(EVENT_NOTICE$ e:sequencingSet) {
			s.append(' ').append(e.PROC.edObjectIdent()).append('[').append(e.EVTIME).append(']');			
		}
//		if(x==null) s.append("EMPTY");
//		while (x != null) {
//			s.append(' ').append(x.PROC.edObjectIdent()).append('[').append(x.EVTIME).append(']');
////			s.append('\n');
//			x = x.suc();
//		}
		return (s.toString());
	}

}
