package javap;

import java.io.IOException;
import java.io.InputStream;

public class RunJavap {

	public static void main(String[] args) {
//		String fileName="C:/GitHub/SimulaCompiler/Simula/bin/simulaTestPrograms/Sudoku$PBLK1$SQ$legit.class";
//		String fileName="C:/GitHub/SimulaCompiler/Simula/src/simulaTestPrograms/tmp/simulaTestPrograms/adHoc00.class";
//		String fileName="C:/GitHub/SimulaCompiler/Simula/src/simulaTestBatch/tmp/simulaTestBatch/simtst26$SubBlock37.class";
//		String fileName="C:/GitHub/SimulaCompiler/Simula/src/simulaTestPrograms/tmp/simulaTestPrograms/adHoc26$q.class";
		
//		String fileName="C:\\GitHub\\SimulaCompiler\\Simula\\src\\simulaTestPrograms\\tmp/simulaTestPrograms/adHoc68$Writer$1$4.class";
		String fileName="C:/GitHub/SimulaCompiler/Simula/src/simulaTestPrograms/tmp/simulaTestPrograms/adHoc68$Writer$1.class";
		listClassFile(fileName);
	}
	
	public static void listClassFile(String classFileName) {
//		String command="javap -c "+fileName;
		String command="javap -c -l -p -s -verbose "+classFileName;
		try { execute(command); } catch(IOException e) { e.printStackTrace();
		}
	}


	// ***************************************************************
	// *** EXECUTE OS COMMAND
	// ***************************************************************
	private static int execute(String command) throws IOException {
		Runtime runtime = Runtime.getRuntime();
        System.out.println("MakeCompiler.execute: command="+command);
	    String cmd=command.trim()+'\n';
		Process process = runtime.exec(cmd);
		//try
		{ InputStream err=process.getErrorStream();
		  InputStream inp=process.getInputStream();
		  while(process.isAlive())
		  { while(err.available()>0) System.err.append((char)err.read());
		    while(inp.available()>0) System.out.append((char)inp.read());
			
		  }
		  // process.waitFor();
		} //catch(InterruptedException e) { e.printStackTrace(); }
		return(process.exitValue());
	}

}