/*
 * (CC) This work is licensed under a Creative Commons
 * Attribution 4.0 International License.
 *
 * You find a copy of the License on the following
 * page: https://creativecommons.org/licenses/by/4.0/
 */
package simula.compiler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import simula.compiler.byteCodeEngineering.ByteCodeEngineering;
import simula.compiler.parsing.Parser;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.RTOption;
import simula.compiler.utilities.Util;

/**
 * 
 * @author Øystein Myhre Andersen
 *
 */
public final class SimulaCompiler {
	String inputFileName;
	Reader reader; // Reader in case of SimulaEditor
	
	public SimulaCompiler(String inputFileName) {
		this(inputFileName,null);
	}
	
	public SimulaCompiler(String inputFileName,Reader reader) {
		Global.initiate();
		this.inputFileName = inputFileName;
		this.reader=reader;
		if (!inputFileName.toLowerCase().endsWith(".sim"))
			Util.warning("Simula source file should, by convention be extended by .sim: " + inputFileName);

		File inputFile = new File(inputFileName);
		
		// Create Output File Path
		String name = inputFile.getName();
		Global.sourceFileName=name;
		Global.sourceName = name.substring(0, name.length() - 4);
		Global.sourceFileDir=inputFile.getParent()+"/";
		
		if(Option.verbose) Util.message("Compiling: "+inputFileName);
		
		if(Option.outputDir==null)
		     Global.outputDir=Global.sourceFileDir+"bin/";	
		else Global.outputDir=Option.outputDir;
		
//		Global.simulaRtsPath=   "C:/WorkSpaces/SimulaCompiler/Simula/bin/";         // AD'HOC
//		String userDir=System.getProperty("user.dir");
//		Global.inlineRtsPath=userDir+"/bin/";         // AD'HOC

		// Get Temp Directory:
		String simtmp=Option.keepJava;
		if(simtmp==null) simtmp=Global.getTempFileDir("simula/");
		
//		// Create Temp .class-Files Directory:
//		String tmpClassDirName=simtmp+"/bin/"+Global.packetName;
//		File tmpClassDir=new File(tmpClassDirName);
//		tmpClassDir.mkdirs();
//		tmpClassDir.deleteOnExit();
//		Global.tempClassFileDir=tmpClassDir.toString()+'/';
		
		// Create Temp .java-Files Directory:
		String tmpJavaDirName=simtmp+"/src/"+Global.packetName;
		File tmpJavaDir=new File(tmpJavaDirName);
		tmpJavaDir.mkdirs();
		if(Option.deleteTempFiles) tmpJavaDir.deleteOnExit();
		Global.tempJavaFileDir=tmpJavaDir.toString()+'/';

		// Create Temp .class-Files Directory:
		File tmpClassDir=new File(Global.tempJavaFileDir+"/"+Global.sourceName+'_'+"Classes");
		tmpClassDir.mkdirs();
		if(Option.deleteTempFiles) tmpClassDir.deleteOnExit();
		Global.tempClassFileDir=tmpClassDir.toString()+'/';

		if(Option.verbose)
		{ Util.message("Package Name:    "+Global.packetName);
		  Util.message("SourceFile Name: "+Global.sourceName);
		  Util.message("SourceFile Dir:  "+Global.sourceFileDir);
		  Util.message("CurrentWorkspace "+Global.currentWorkspace);
		  Util.message("TempDir .Java:   "+Global.tempJavaFileDir);
		  Util.message("TempDir .Class:  "+Global.tempClassFileDir);
		  Util.message("SimulaRtsLib:    "+Global.simulaRtsLib);
		  Util.message("OutputDir:       "+Global.outputDir);
		  Util.message("Java version     "+System.getProperty("java.version"));
		  Util.message("Java vendor      "+System.getProperty("java.vendor"));
		  Util.message("OS name          "+System.getProperty("os.name"));
		  Util.message("OS architecture  "+System.getProperty("os.arch"));
		  Util.message("OS version       "+System.getProperty("os.version"));
		  Util.message("file.encoding    "+System.getProperty("file.encoding"));
		  Util.message("defaultCharset   "+Charset.defaultCharset());
		}
		// https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html
//		Util.println("SYSTEM: java.home="+System.getProperty("java.home"));
//		Util.println("SYSTEM: java.class.path="+System.getProperty("java.class.path"));
//		Util.println("SYSTEM: path.separator="+System.getProperty("path.separator"));
//		Util.println("SYSTEM: User working directory="+System.getProperty("user.dir"));
//		Util.println("SYSTEM: User home directory="+System.getProperty("user.home"));
		if(Option.deleteTempFiles) emptyTempClassFileDir();
	}
	
	private void emptyTempClassFileDir() {
		try { // Try to Clean Global.tempClassFileDir
			  File tmpClass = new File(Global.tempClassFileDir + '/' + Global.packetName);
			  // Util.println("tmpClass: "+tmpClass);
			  File[] elt = tmpClass.listFiles();
			  // Util.println("Elements: "+elt);
			  for (File f : elt) {
				  // Util.println("Delete: "+f);
				  f.delete();
			  }
		} catch (Exception e) {	}
	}

	public void doCompile() {
		try {
			Util.nError=0;
			if(!Util.isJavaIdentifier(Global.sourceName)) {
				String sourceName=Global.sourceName; 
				Global.sourceName=Util.makeJavaIdentifier(sourceName);
				Util.warning("The source file name '"+sourceName+"' is not a legal class identifier. Modified to: "+Global.sourceName);
			}

			Global.javaModules=new Vector<JavaModule>();
			
			if(reader==null) {
				try {
					//reader = new FileReader(inputFileName);
					reader=new InputStreamReader(new FileInputStream(inputFileName),Global.CHARSET$);
				} catch (IOException e) {
					//Util.println("Error Opening File: " + fileName);
					//e.printStackTrace();
					Util.error("can't open " + inputFileName);
				}
			}
			Parser.open(reader);
			ProgramModule program = new ProgramModule();
//			createTempFiles(program);
			if (Option.verbose) {
				Util.message("END Parsing, resulting Program: "+program);
				if (Option.TRACE_PARSE && program != null) program.print("");
			}
			Parser.close(); Global.duringParsing=false;
			
			if(Util.nError>0) 
			{ Util.message("Compiler terminated after "+Util.nError+" errors during parsing");
			  return;
			  //System.exit(-Util.nError);
			}
			if (Option.verbose)	Util.message("BEGIN Semantic Checker");
			//program.print("");
			program.doChecking();
			if (Option.verbose) {
				Util.message("END Semantic Checker: "+program);
				if (Option.TRACE_CHECKER_OUTPUT && program != null) program.print("");
			}
			
			if(Util.nError>0) 
			{ Util.message("Compiler terminated after "+Util.nError+" errors during semantic checking");
			  return;
			  //System.exit(-Util.nError);
			}

			if (Option.verbose) Util.message("BEGIN Generate .java Output Code"); 
			program.doJavaCoding();
			if (Option.verbose)
			{ Util.message("END Generate .java Output Code");
			    for(JavaModule module:Global.javaModules)
			      	Util.LIST(module.javaOutputFileName);
			}
			
			if (Option.verbose) Util.message("BEGIN Possible Generate AttributeFile");
			AttributeFile.write(program);
			
			// ***************************************************************
			// *** CALL JAVAC COMPILER
			// ***************************************************************
			String classPath=Global.simulaRtsLib;
			for(String jarFileName:Global.externalJarFiles)
				  classPath=classPath+";"+jarFileName;
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			if(compiler!=null) callJavaSystemCompiler(compiler,classPath);
			else callJavacCompiler(classPath);
			
			if(Option.TRACE_JAVAC_OUTPUT)
			{ for(JavaModule module:Global.javaModules)
			  {	String classFile=module.getClassOutputFileName();
			    doListClassFile(classFile);
			  }
			}
			
			// ***************************************************************
			// *** POSSIBLE -- DO BYTE_CODE_ENGINEERING
			// ***************************************************************
			//Util.println("SimulaCompiler.doCompile: BEGIN BYTE CODE REPAIR: "+Global.tempClassFileDir);
			File classDir=new File(Global.tempClassFileDir);
			//repairClassFiles(classDir);
			ArrayList<String> files=classFiles(classDir,new ArrayList<String>());
			for(String classFileName:files) {
				//Util.println("SimulaCompiler.doCompile: Repair: "+classFileName);
				new ByteCodeEngineering().doRepairSingleByteCode(classFileName);
			}

		    //doListClassFile("C:/Users/YSTEIN~1/AppData/Local/Temp/GotoSample6Classes/testing/GotoSample6$B$1.class"); // AD'HOC
			if(Option.TRACE_BYTECODE_OUTPUT)
			{ for(JavaModule module:Global.javaModules)
			  {	String classFile=module.getClassOutputFileName();
			    doListClassFile(classFile);
			  }
			}
			

			// ***************************************************************
			// *** CRERATE .jar FILE  INLINE
			// ***************************************************************
			String jarFile=createJarFile(program);
			if(Option.TRACE_JARING)
				{
				Util.println("List .jar File: "+jarFile);
			    execute("jar.exe -tvf "+jarFile);
			}
			
			// ***************************************************************
			// *** EXECUTE .jar FILE
			// ***************************************************************
			// java -jar C:/WorkSpaces/SimulaCompiler/Simula/bin/adHoc00.jar
			if(!program.isExecutable() || Option.noExecution) { if(Option.verbose) Util.message("No Execution of .jar File: "+jarFile); }
			else
			{ if(Option.verbose) Util.message("Execute .jar File");
			
			  String opt="";
			  //if(Global.simulaRtsLib!=null) opt=opt+"-classpath "+Global.simulaRtsLib;
			  //Util.BREAK("SimulaCompiler.doCompile: EXECUTE opt="+opt);
			  
			  String arg=RTOption.getRTArguments();
			  //Util.BREAK("SimulaCompiler.doCompile: EXECUTE arg="+arg);
			  
			  String cmd="java "+opt+" -jar "+jarFile+arg;
			  
			  //Util.BREAK("SimulaCompiler.doCompile: EXECUTE cmd="+cmd);
			  int exitValue3=execute(cmd);
			  if (Option.verbose)
			     Util.message("END Execute .jar File. Exit value="+exitValue3);
			}

			if(Option.deleteTempFiles) emptyTempClassFileDir();
			
		} catch (IOException e) {
			Util.message("Compiler Error: "+e);
			e.printStackTrace();
		}
	}
	
//	// ***************************************************************
//	// *** REPAIR CLASS FILES
//	// ***************************************************************
//	public void repairClassFiles(final File folder) {
//		for (final File fileEntry : folder.listFiles()) {
//			if (fileEntry.isDirectory()) {
//				repairClassFiles(fileEntry);
//			} else {
//				String classFileName = fileEntry.getPath();
//				//Util.println(classFileName);
//				if(classFileName.endsWith(".class")) {
//					//Util.println("SimulaCompiler.doCompile: BYTE CODE REPAIR: " + classFileName);
////					if (Global.USE_OW2_ASMTOOLS)
//						 new ByteCodeEngineering().doRepairSingleByteCode(classFileName);
////					else if (Global.USE_OpenJDK_ASMTOOLS)
////						 new AsmToolEngineering().doRepairSingleByteCode(classFileName);
////					else new ByteCodeEngineering().doRepairSingleByteCode(classFileName);
//				}
//			}
//		}
//	}
	
	// ***************************************************************
	// *** CREATE CLASS FILE LIST
	// ***************************************************************
	public ArrayList<String> classFiles(final File folder,final ArrayList<String> files) {
//		ArrayList<String> files=new ArrayList<String>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				classFiles(fileEntry,files);
			} else {
				String classFileName = fileEntry.getPath();
				//Util.println(classFileName);
				if(classFileName.endsWith(".class")) {
					if(!files.contains(classFileName)) files.add(classFileName);
				}
			}
		}
		return(files);
	}
	
	// ***************************************************************
	// *** CALL JAVA SYSTEM COMPILER
	// ***************************************************************
	private void callJavaSystemCompiler(JavaCompiler compiler,String classPath) throws IOException
	{ if(Option.verbose) Util.message("Call Java System Compiler");
	  Vector<String> arguments=new Vector<String>();
	  if(Option.TRACE_JAVAC) {  arguments.add("-version"); arguments.add("-verbose"); }
	  if(Option.verbose) Util.println("SimulaCompiler.callJavaSystemCompiler: classPath="+classPath);
	  arguments.add("-classpath"); arguments.add(classPath);
	  arguments.add("-d"); arguments.add(Global.tempClassFileDir); // Specifies output directory.
	  if(Option.noJavacWarnings) arguments.add("-nowarn");
	  //arguments.add("-Xlint:unchecked");
      for(JavaModule module:Global.javaModules) arguments.add(module.javaOutputFileName); // Add .jave Files
	  
	  // java.home=C:\Program Files\Java\jre1.8.0_161
	  if(Option.TRACE_JAVAC) Util.println("Compiler="+compiler);
	  if(compiler==null) Util.error("SimulaCompiler.callJavaSystemCompiler: ToolProvider.getSystemJavaCompiler() returns null");
	  int nArg=arguments.size();
	  String[] args=new String[nArg];
			  arguments.toArray(args);
			  
	  if(Option.TRACE_JAVAC) { for(int i=0;i<args.length;i++) Util.println("Compiler'args["+i+"]="+args[i]); }

	  int exitValue = compiler.run(null, null, null, args);
	  if (Option.verbose)
	  { Util.message("END Generate .class Output Code. Exit value="+exitValue);
	    for(JavaModule module:Global.javaModules) Util.LIST(module.getClassOutputFileName());
	  }
	}
	
	// ***************************************************************
	// *** CALL JAVAC COMPILER
	//
	// https://docs.oracle.com/javase/7/docs/technotes/tools/windows/javac.html
	// https://docs.oracle.com/javase/10/tools/tools-and-command-reference.htm
	// ***************************************************************
	private void callJavacCompiler(String classPath) throws IOException
	{ if(Option.verbose) Util.message("Call Java CommandLine Compiler");
	  StringBuilder source=new StringBuilder();;
      for(JavaModule module:Global.javaModules) source.append(' ').append(module.javaOutputFileName);
      if(Option.verbose) Util.message("SimulaCompiler.doCompile: source="+source);
//	  String javac=Global.javaDir+"javac.exe";
//	  String javac=Global.javaHome+"javac.exe";
	  String javac="javac.exe";
	  //Util.println("SimulaCompiler.callJavacCompiler: classPath="+classPath);
	  classPath=" -classpath "+classPath;
	  
	  // *** TODO: Hvis tempClassFileDir ikke finnes - CREATE !
	  String classOutputDir=" -d "+Global.tempClassFileDir; 
	
	  if(Option.TRACE_JAVAC)
	  { Util.println("SimulaCompiler.doCompile JAVAC: classPath="+classPath);
	    Util.println("SimulaCompiler.doCompile JAVAC: source="+source);
	    Util.println("SimulaCompiler.doCompile JAVAC: classOutputDir="+classOutputDir);
	  }
	  String opt=(Option.TRACE_JAVAC)?" -verbose":"";
	  //opt=opt+" -Xlint:unchecked";
	  opt=opt+classOutputDir;
	  String command=javac+opt+classPath+source;
	  int exitValue=execute(command);
	  
	  if (Option.verbose)
	  { Util.message("END Generate .class Output Code. Exit value="+exitValue);
	    for(JavaModule module:Global.javaModules) Util.LIST(module.getClassOutputFileName());
	  }
	}

	
	// ***************************************************************
	// *** CREATE .jar FILE
	// ***************************************************************
	public String createJarFile(ProgramModule program) throws IOException
	{ if(Option.verbose) Util.message("BEGIN Create .jar File");
//	  String jarFileName=Global.outputDir+Global.sourceName+".jar ";
	  String jarFileName=Global.outputDir+Global.sourceName+".jar";
	  
	  if(!program.isExecutable()) {
		  //Util.BREAK("SimulaCompiler.createJarFile: program="+program);
		  jarFileName=Global.outputDir+program.getIdentifier()+".jar ";
		  Util.warning("Separate Compiled Module is written to: "+jarFileName);
	  }
	  File jarFile=new File(jarFileName);
	  jarFile.getParentFile().mkdirs();
	  Manifest manifest=new Manifest();
	  String mainEntry=Global.packetName+'/'+program.getIdentifier();
	  mainEntry=mainEntry.replace('/', '.');
	  if(Option.verbose) Util.message("Output "+jarFile+" MANIFEST'mainEntry="+mainEntry);
	  manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
	  String relativeAttributeFileName=program.getRelativeAttributeFileName();
	  if(relativeAttributeFileName!=null) manifest.getMainAttributes().putValue("SIMULA-INFO",relativeAttributeFileName);
	  else manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS,mainEntry);
	  JarOutputStream target = new JarOutputStream(new FileOutputStream(jarFile), manifest);
	  add(target,new File(Global.tempClassFileDir+Global.packetName),Global.tempClassFileDir.length());
//	  // Add External Jar Files
//	  for(ExternalJarFile jf:ExternalJarFile.ExternalJarFiles)
//		  addExternalJar(target,jf.jarFileName);
	  
	  if(Global.INCLUDE_RUNTIME_SYSTEM_IN_JAR)
	  { add(target,new File(Global.simulaRtsLib+"simula/runtime"),Global.simulaRtsLib.length());
//	    add(target,new File(Global.simulaRtsLib+"simula/compiler/utilities/Util.class"),Global.simulaRtsLib.length());
//	    add(target,new File(Global.simulaRtsLib+"simula/compiler/utilities/Option.class"),Global.simulaRtsLib.length());
	  }
	  target.close();
	  if(Option.verbose) Util.message("END Create .jar File: "+jarFile);
	  return(jarFile.toString());
	}

	private void add(JarOutputStream target,File source,int pathSize ) throws IOException
	{ BufferedInputStream inpt=null;
	  try
	  { if(source.isDirectory())
	    { String name = source.getPath().replace("\\", "/");
	      if(!name.isEmpty())
	      { if (!name.endsWith("/")) name += "/";
	        JarEntry entry = new JarEntry(name.substring(pathSize));
	        entry.setTime(source.lastModified());
	        target.putNextEntry(entry);
	        target.closeEntry();
	      }
	      for(File nestedFile:source.listFiles())
	          add(target,nestedFile,pathSize);
	      return;
	    }
        String entryName=source.getPath().replace("\\", "/");
        //Util.println("SimulaCompiler.add: "+entryName);
        if(!entryName.endsWith(".jasm")) {
        	JarEntry entry = new JarEntry(entryName.substring(pathSize));
        	entry.setTime(source.lastModified());
        	target.putNextEntry(entry);
        	inpt = new BufferedInputStream(new FileInputStream(source));
        	byte[] buffer=new byte[1024];
        	while(true)	{
        		int count=inpt.read(buffer);
        		if(count == -1) break; // EOF
        		target.write(buffer,0,count);
        	}
        	target.closeEntry();
        }
	  } finally { if(inpt!=null) inpt.close(); }
	}

	
	// ***************************************************************
	// *** LIST .class file
	// ***************************************************************
	public static void doListClassFile(String classFileName) {
//		String command="javap -c "+fileName;
		String command="javap -c -l -p -s -verbose "+classFileName;
		try { execute(command); } catch(IOException e) { e.printStackTrace();
		}
	}

	// ***************************************************************
	// *** EXECUTE OS COMMAND
	// ***************************************************************
	public static int execute(String command) throws IOException
	{	Runtime runtime = Runtime.getRuntime();
	    if(Option.verbose) Util.message("Execute: "+command);
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
