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
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.attribute.FileTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import simula.compiler.byteCodeEngineering.ByteCodeEngineering;
import simula.compiler.declaration.ClassDeclaration;
import simula.compiler.editor.RTOption;
import simula.compiler.parsing.Parse;
import simula.compiler.utilities.Global;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;

/**
 * The Simula Compiler
 * <p>
 * Link to GitHub: <a href="https://github.com/portablesimula/SimulaCompiler/blob/master/Simula/src/simula/compiler/SimulaCompiler.java"><b>Source File</b></a>.
 *
 * @author Øystein Myhre Andersen
 *
 */
public final class SimulaCompiler {
	final private Reader reader; // Reader in case of SimulaEditor
	
	private ProgramModule programModule;
	private File jarFile;
	private String mainEntry;
	
	/**
	 * Create a new SimulaCompiler.
	 * @param inputFileName the source file name
	 */
	public SimulaCompiler(final String inputFileName) {
		this(inputFileName,null);
	}
	
	/**
	 * Create a new SimulaCompiler.
	 * @param inputFileName the source file name
	 * @param reader Reader in case of SimulaEditor
	 */
	public SimulaCompiler(final String inputFileName,Reader reader) {
		Global.initiate();
		if(reader==null) {
			try { reader=new InputStreamReader(new FileInputStream(inputFileName),Global._CHARSET);
			} catch (IOException e) {
				Util.error("can't open " + inputFileName+", reason: "+e);
			}
		}
		this.reader=reader;
		if (!inputFileName.toLowerCase().endsWith(".sim"))
			Util.warning("Simula source file should, by convention be extended by .sim: " + inputFileName);

		File inputFile = new File(inputFileName);
		
		// Create Output File Path
		String name = inputFile.getName();
		Global.sourceFileName=name;
		Global.sourceName = name.substring(0, name.length() - 4);
		Global.sourceFileDir=inputFile.getParentFile();
		
		if(Option.TRACING) Util.println("Compiling: \""+inputFileName+"\"");
		
		if(Global.outputDir==null) {
		     Global.trySetOutputDir(new File(Global.sourceFileDir,"bin"));
		}

		// Get Temp Directory:
		Global.simulaTempDir=Global.getTempFileDir("simula/");
		deleteTempFiles(Global.simulaTempDir);

		// Create Temp .java-Files Directory:
		File javatmp=Option.keepJava;
		if(javatmp==null) javatmp=Global.simulaTempDir;
		File tmpJavaDir=new File(javatmp,"src/"+Global.packetName);
		tmpJavaDir.mkdirs();
		Global.tempJavaFileDir=tmpJavaDir;

		// Create Temp .class-Files Directory:
		File tmpClassDir=new File(Global.simulaTempDir,"classes");
		tmpClassDir.mkdirs();
		Global.tempClassFileDir=tmpClassDir;

		File desktop=new File(System.getProperty("user.home"),"Desktop");
		if(Option.verbose) {
			// https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html
			Util.println("------------  ENVIRONMENT SUMMARY  ------------");
			Util.println("Simula Properties    "+Global.simulaPropertiesFile);
			Util.println("Simula Home          "+Global.simulaHome);
			Util.println("Simula Home (prev)   "+Global.getProperty("simula.home",null));
			Util.println("Java Home            "+System.getProperty("java.home"));
			Util.println("User Home            "+System.getProperty("user.home"));
			String s=(desktop.exists())?"true ":"false";
			Util.println("Desktop Exists="+s+" "+desktop.toString());
			Util.println("Java Class Path      "+System.getProperty("java.class.path"));
			Util.println("Java Class Version   "+System.getProperty("java.class.version"));
			Util.println("Java Version         "+System.getProperty("java.version"));
			Util.println("Java VM Spec Version "+System.getProperty("java.vm.specification.version"));
			Util.println("Java Vendor          "+System.getProperty("java.vendor"));
			Util.println("OS name              "+System.getProperty("os.name"));
			Util.println("OS architecture      "+System.getProperty("os.arch"));
			Util.println("OS version           "+System.getProperty("os.version"));
			Util.println("32/64 bit JVM        "+System.getProperty("sun.arch.data.model"));
			Util.println("file.encoding        "+System.getProperty("file.encoding"));
			Util.println("defaultCharset       "+Charset.defaultCharset());

			// this will list the current system properties
		    //  System.getProperties().list(System.out);

		}
	}
	
	private void list(final File tempClassFileDir) {
		try {
			Util.println("------------ BEGIN LIST tempClassFileDir: "+tempClassFileDir+"  ------------");
			list("",tempClassFileDir);
			Util.println("------------ ENDOF LIST tempClassFileDir: "+tempClassFileDir+"  ------------");
		} catch (Exception e) { Util.IERR("SimulaCompiler.listFiles FAILED: ", e); e.printStackTrace(); }
	}
	
	private void list(String indent,final File dir) {
		try {
			File[] elt = dir.listFiles();
			if(elt==null || elt.length==0) {
				Util.println("Empty Directory: "+dir);
				return; 
			}
			for (File f : elt) {
				Util.println(indent+"- "+f);
				if(f.isDirectory()) list(indent+"   ",f);
			}
		} catch (Exception e) { Util.IERR("SimulaCompiler.listFiles FAILED: ", e);e.printStackTrace(); }
	}
	
	private void deleteTempFiles(final File tmpClass) {
		try { File[] elt = tmpClass.listFiles();
			  if(elt==null) return; 
			  for (File f : elt) {
				  if(Option.DEBUGGING) {
					  if(f.isFile()) Util.println("Delete: "+f);
				  }
				  if(f.isDirectory()) deleteTempFiles(f);
				  f.delete();
			  }
		} catch (Exception e) { Util.IERR("SimulaCompiler.deleteFiles FAILED: ", e);	e.printStackTrace(); }
	}

	/**
	 * Do Compile
	 */
	public void doCompile() {
		try {
			Util.nError=0;
			if(!Util.isJavaIdentifier(Global.sourceName)) {
				String sourceName=Global.sourceName; 
				Global.sourceName=Util.makeJavaIdentifier(sourceName);
				Util.warning("The source file name '"+sourceName+"' is not a legal class identifier. Modified to: "+Global.sourceName);
			}
			// ***************************************************************
			// *** Scanning and Parsing
			// ***************************************************************
			Global.generatedJavaClass=new Vector<GeneratedJavaClass>();
			Parse.initiate(reader);
			programModule = new ProgramModule();
			if (Option.TRACING) {
				Util.println("END Parsing, resulting Program: \""+programModule+"\"");
				if (Option.TRACE_PARSE && programModule != null) programModule.print(0);
			}
			Parse.close(); Global.duringParsing=false;
			if(Util.nError>0) {
				Util.println("Compiler terminate "+Global.sourceName+" after "+Util.nError+" errors during parsing");
				return;
			}
			// ***************************************************************
			// *** Semantic Checker
			// ***************************************************************
			if (Option.TRACING)	Util.println("BEGIN Semantic Checker");
			Global.duringChecking=true;
			programModule.doChecking();
			if (Option.TRACING) {
				Util.println("END Semantic Checker: \""+programModule+"\"");
				if (Option.TRACE_CHECKER_OUTPUT && programModule != null)	programModule.print(0);
			}
			Global.duringChecking=false;
			if(Util.nError>0) {
				Util.println("Compiler terminate "+Global.sourceName+" after "+Util.nError+" errors during semantic checking");
				return;
			}
			// ***************************************************************
			// *** Generate .java intermediate code
			// ***************************************************************
			if (Option.TRACING) Util.println("BEGIN Generate .java Output Code"); 
			programModule.doJavaCoding();
			if (Option.TRACING)	{
				Util.println("END Generate .java Output Code");
			    for(GeneratedJavaClass javaClass:Global.generatedJavaClass)
			      	Util.println(javaClass.javaOutputFile.toString());
			}
			if(Util.nError>0) {
				Util.println("Compiler terminate "+Global.sourceName+" after "+Util.nError+" errors during code generation");
				return;
			}
			
			if (Option.TRACING) Util.println("BEGIN Possible Generate AttributeFile");
			AttributeFileIO.write(programModule);
			
			// ***************************************************************
			// *** CALL JAVA COMPILER
			// ***************************************************************
			String classPath=Global.simulaRtsLib.toString();			
			
			if(Option.verbose) fileSummary();
			if(Option.DEBUGGING) {
				Util.println("------------  CLASSPATH DETAILS  ------------");
				Util.println("Java PathSeparator "+System.getProperty("path.separator"));
				Util.println("Java ClassPath     "+System.getProperty("java.class.path"));
			}
			
			File rtsLib=new File(Global.simulaRtsLib,"simula/runtime");
			boolean rtsExist=rtsLib.exists();
			boolean rtsCread=rtsLib.canRead();
			if(!(rtsExist && rtsCread)) {
				Util.popUpError("Unable to access the Runtime System at:"
					     +"\n"+rtsLib
					     +"\nCheck the installation and consider"
					     +"\nto Download it again.\n");
			}
			if(Option.DEBUGGING) {
				Util.println("Simula Runtime System:    \""+rtsLib+"\", exists="+rtsExist+", canRead="+rtsCread);
				String[] list=rtsLib.list(); 
				if(list!=null) {
					Util.println("Simula Runtime System:    \""+rtsLib+"\", exists="+rtsExist+", canRead="+rtsCread+", size="+list.length);
					for(int i=0;i<list.length;i++) {
						Util.println("       "+i+": \""+list[i]+"\"");
					}
				}
			}
			String pathSeparator=System.getProperty("path.separator");
			for(File jarFile:Global.externalJarFiles) {
				if(Option.DEBUGGING) {
					boolean exist=jarFile.exists();
					boolean cread=jarFile.canRead();
					Util.println("Precompiled Library:      \""+jarFile+"\", exists="+exist+", canRead="+cread);
					listJarFile(jarFile);
				}
				classPath=classPath+pathSeparator+(jarFile.toString().trim());
			}
			int exitValue= -1;
			String msg="Commandline";
			if(Global.USE_JAVA_SYSTEM_COMPILER) {
				JavaCompiler compiler= ToolProvider.getSystemJavaCompiler();
				if(compiler!=null) {
					exitValue=callJavaSystemCompiler(compiler,classPath);
					msg="System";
					if(exitValue!=0) {
						Util.error("Java "+msg+" Compiler returns exit="+exitValue+"\n");
						msg="Commandline"; // Try use CommandLine Compiler
						exitValue=callJavacCompiler(classPath);
					}
				}
				else exitValue=callJavacCompiler(classPath);
			} else exitValue=callJavacCompiler(classPath);
			if(Option.DEBUGGING) {
				Util.println("Java "+msg+" Compiler returns exit="+exitValue+"\n");
				for (GeneratedJavaClass javaClass : Global.generatedJavaClass)
					Util.println(javaClass.getClassOutputFileName());
				list(Global.tempClassFileDir);
			}
			if(exitValue!=0) {
				Util.error("Java "+msg+" Compiler returns exit="+exitValue+"\n");
				Util.println("\nCompiler terminated after error(s) during Java Compilation");
				return;
			}

			// ***************************************************************
			// *** POSSIBLE -- DO BYTE_CODE_ENGINEERING
			// ***************************************************************
			if(Option.keepJava==null) {
				if(Option.TRACE_BYTECODE_OUTPUT) {
					Util.println("------------  LIST ByteCode Before Engineering  ------------");
					for(GeneratedJavaClass javaClass:Global.generatedJavaClass) {
						String classFile=javaClass.getClassOutputFileName();
						doListClassFile(classFile);
					}
				}
				for(GeneratedJavaClass javaClass:Global.generatedJavaClass) {
					if(javaClass.mustDoByteCodeEngineering) {
						String classFileName=javaClass.getClassOutputFileName();
						new ByteCodeEngineering().doRepairSingleByteCode(classFileName);
					}
				}
				if(Option.TRACE_BYTECODE_OUTPUT) {
					Util.println("------------  LIST ByteCode After Engineering  ------------");
					for(GeneratedJavaClass javaClass:Global.generatedJavaClass) {
						String classFile=javaClass.getClassOutputFileName();
						doListClassFile(classFile);
					}
				}
			} else {
				Util.warning("Option.keepJava set: No ByteCode Engineering is performed");
			}
			

			// ***************************************************************
			// *** CRERATE .jar FILE  INLINE
			// ***************************************************************
			String jarFile=createJarFile(programModule);
			if(Option.verbose) printSummary();
			
			
			// ***************************************************************
			// *** EXECUTE .jar FILE
			// ***************************************************************
			if(!programModule.isExecutable()) {
				if(Option.TRACING) Util.println("Separate Compilation - No Execution of .jar File: "+jarFile);
			} else if(Option.noExecution) {
					if(Option.TRACING) Util.println("Option 'noexec' ==> No Execution of .jar File: "+jarFile);
			} else {
				if(Option.verbose) Util.println("------------  EXECUTION SUMMARY  ------------");
				if(Option.TRACING) Util.println("Execute .jar File");
				Vector<String> cmds=new Vector<String>();
				cmds.add("java");
//				cmds.add("--enable-preview");
				cmds.add("-jar");
				cmds.add(jarFile);
				if(Option.RUNTIME_USER_DIR.length()>0) {
					cmds.add("-userDir"); cmds.add(Option.RUNTIME_USER_DIR);
				} else {
					cmds.add("-userDir"); cmds.add(Global.outputDir.getParentFile().getAbsolutePath());				
				}
				RTOption.addRTArguments(cmds);
				if(Option.SOURCE_FILE.length() > 0) {
					cmds.add(Option.SOURCE_FILE);
				}
				int exitValue3=execute(cmds);
				if (Option.verbose)	Util.println("END Execute .jar File. Exit value="+exitValue3);
			}
			if(Option.DEBUGGING) Util.println("------------  CLEANING UP TEMP FILES  ------------");
			deleteTempFiles(Global.simulaTempDir);
			
		} catch (IOException e) {
			Util.IERR("Compiler Error: ",e);
		}
	}
	
	// ***************************************************************
	// *** CALL JAVA SYSTEM COMPILER
	// ***************************************************************
	private int callJavaSystemCompiler(final JavaCompiler compiler,final String classPath) throws IOException {
		Vector<String> arguments = new Vector<String>();
//		arguments.add("-source"); arguments.add("21");
		arguments.add("-target"); arguments.add("21");
//		arguments.add("-release"); arguments.add("21");
		if (Option.DEBUGGING) {
			arguments.add("-version");
		}
		if (Option.TRACING)	Util.println("SimulaCompiler.callJavaSystemCompiler: classPath=\"" + classPath+"\"");
		arguments.add("-classpath"); arguments.add(classPath);
		arguments.add("-d"); arguments.add(Global.tempClassFileDir.toString()); // Specifies output directory.
		if (!Option.WARNINGS) arguments.add("-nowarn");
		for (GeneratedJavaClass javaClass : Global.generatedJavaClass)
			arguments.add(javaClass.javaOutputFile.toString()); // Add .java Files
		int nArg = arguments.size();
		String[] args = new String[nArg];
		arguments.toArray(args);
		
		InputStream in=System.in;
		OutputStream out=System.out;
		OutputStream err=System.err;
		if(Global.console!=null) {
			out=Global.console.getOutputStream();
			err=Global.console.getErrorStream();
		}
		if(Option.DEBUGGING) {
			Util.println("------------  Call Java System Compiler  ------------");
			Util.println("System Compiler supports " + compiler.getSourceVersions());
			for (int i = 0; i < args.length; i++)
				Util.println("Compiler'args[" + i + "]=" + args[i]);
		}
		int exitValue = compiler.run(in, out, err, args);
		return(exitValue);
	}
	
	
	// ***************************************************************************
	// *** CALL JAVA COMMAND LINE COMPILER
	//
	// https://docs.oracle.com/javase/7/docs/technotes/tools/windows/javac.html
	// https://docs.oracle.com/javase/10/tools/tools-and-command-reference.htm
	// ***************************************************************************
	private int callJavacCompiler(final String classPath) throws IOException {
		Vector<String> cmds=new Vector<String>();
		cmds.add("javac");
//		cmds.add("-source"); cmds.add("21");
		cmds.add("-target"); cmds.add("21");
//		cmds.add("-release"); cmds.add("21");
		if (Option.DEBUGGING) {
			cmds.add("-version");
		}
		if (Option.TRACING)	Util.println("SimulaCompiler.callJavacCompiler: classPath=\"" + classPath+"\"");
		cmds.add("-classpath"); cmds.add(classPath);
		cmds.add("-d"); cmds.add(Global.tempClassFileDir.toString()); // Specifies output directory.
		if (!Option.WARNINGS) cmds.add("-nowarn");
		for (GeneratedJavaClass javaClass : Global.generatedJavaClass) {
			cmds.add(javaClass.javaOutputFile.toString()); // Add .java Files
		}
		int exitValue = execute(cmds);
		if (Option.TRACING) {
			Util.println("END Generate .class Output Code. Exit value=" + exitValue);
			for (GeneratedJavaClass javaClass : Global.generatedJavaClass)
				Util.println(javaClass.getClassOutputFileName());
		}
		return(exitValue);
	}

	// ***************************************************************
	// *** CREATE .jar FILE
	// ***************************************************************
	private String createJarFile(final ProgramModule program) throws IOException {
		if (Option.TRACING)	Util.println("BEGIN Create .jar File");
		jarFile = new File(Global.outputDir,program.getIdentifier()+".jar");
		jarFile.getParentFile().mkdirs();
		if (!program.isExecutable()) {
			String id=program.module.identifier;
			String kind="Procedure ";
			if(program.module instanceof ClassDeclaration) kind="Class ";
			Util.warning("No execution - Separate Compiled "+kind+id+" is written to: \"" + jarFile+"\"");
		}
		Manifest manifest = new Manifest();
		mainEntry = Global.packetName + '/' + program.getIdentifier();
		mainEntry = mainEntry.replace('/', '.');
		if (Option.TRACING)
			Util.println("Output " + jarFile + " MANIFEST'mainEntry=\"" + mainEntry+"\"");
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
		manifest.getMainAttributes().putValue("Created-By",Global.simulaReleaseID+" (Portable Simula)");
		if(program.isExecutable()) {
			manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, mainEntry);
		} else {
			String relativeAttributeFileName = program.getRelativeAttributeFileName();
			if (relativeAttributeFileName != null)
			manifest.getMainAttributes().putValue("SIMULA-INFO", relativeAttributeFileName);
		}
		
		JarOutputStream target = new JarOutputStream(new FileOutputStream(jarFile), manifest);
		add(target, new File(Global.tempClassFileDir,Global.packetName), Global.tempClassFileDir.toString().length());
//		if(programModule.isExecutable() && !Option.noExecution && Global.INCLUDE_RUNTIME_SYSTEM_IN_JAR) {
		if(programModule.isExecutable() && Global.INCLUDE_RUNTIME_SYSTEM_IN_JAR) {
			File rtsHome= new File(Global.simulaRtsLib,"simula/runtime");
			add(target,rtsHome, Global.simulaRtsLib.toString().length());
		}
		target.close();
		if (Option.TRACING)	Util.println("END Create .jar File: " + jarFile);
		if(Option.DEBUGGING) {
			Util.println("SimulaCompiler.createJarFile: BEGIN LIST GENERATED .jar FILE  ========================================================");
			listJarFile(jarFile);
			Util.println("SimulaCompiler.createJarFile: ENDOF LIST GENERATED .jar FILE  ========================================================");
		}
		return (jarFile.toString());
	}

	private void add(final JarOutputStream target,final File source,final int pathSize) throws IOException {
		BufferedInputStream inpt = null;
		try {
			if (source.isDirectory()) {
				String name = source.getPath().replace("\\", "/");
				if (!name.isEmpty()) {
					if (!name.endsWith("/")) name += "/";
					name=name.substring(pathSize);
					if (name.startsWith("/")) name=name.substring(1);
					JarEntry entry = new JarEntry(name);
					entry.setTime(source.lastModified());
					target.putNextEntry(entry);
					target.closeEntry();
				}
				for (File nestedFile : source.listFiles())
					add(target, nestedFile, pathSize);
				return;
			}
			String entryName = source.getPath().replace("\\", "/");
			if (!entryName.endsWith(".jasm")) {
				entryName=entryName.substring(pathSize);
				if (entryName.startsWith("/")) entryName=entryName.substring(1);
				JarEntry entry = new JarEntry(entryName);
				entry.setTime(source.lastModified());
				target.putNextEntry(entry);
				inpt = new BufferedInputStream(new FileInputStream(source));
				byte[] buffer = new byte[1024];
				while (true) {
					int count = inpt.read(buffer);
					if (count == -1) break; // EOF
					target.write(buffer, 0, count);
				}
				target.closeEntry();
			}
		} finally {
			if (inpt != null) inpt.close();
		}
	}
	
	// ***************************************************************
	// *** FILE SUMMARY
	// ***************************************************************
	private void fileSummary() {
		Util.println("------------  FILE SUMMARY  ------------");
		Util.println("Package Name:    \""+Global.packetName+"\"");
		Util.println("SourceFile Name: \""+Global.sourceName+"\"");
		Util.println("SourceFile Dir:  \""+Global.sourceFileDir.getAbsolutePath()+"\"");
		if(Global.currentWorkspace!=null) Util.println("CurrentWorkspace \""+Global.currentWorkspace.getAbsolutePath()+"\"");
		Util.println("TempDir .java:   \""+Global.tempJavaFileDir.getAbsolutePath()+"\"");
		Util.println("TempDir .class:  \""+Global.tempClassFileDir.getAbsolutePath()+"\"");
		Util.println("SimulaRtsLib:    \""+Global.simulaRtsLib.getAbsolutePath()+"\"");
		Util.println("OutputDir:       \""+Global.outputDir.getAbsolutePath()+"\"");
	}
	
	// ***************************************************************
	// *** PRINT SUMMARY
	// ***************************************************************
	private void printSummary() {
		Util.println("------------  COMPILATION SUMMARY  ------------");
		if (!programModule.isExecutable()) {
			Util.println("Separate Compiled "+programModule.module.declarationKind+" is written to: \"" + jarFile+"\"");
			Util.println("Rel Attr.File:   \""+programModule.getRelativeAttributeFileName()+"\"");
		} else {
			Util.println("Resulting File:  \"" + jarFile.getAbsolutePath() +"\"");
			Util.println("Main Entry:      \"" + mainEntry+"\"");
		}
		if(Option.DEBUGGING) listJarFile(jarFile);
	}

	// ***************************************************************
	// *** LIST .jar file
	// ***************************************************************
	private static void listJarFile(final File file) {
		Util.println("---------  LIST .jar File: "+file+"  ---------");
		if(!(file.exists() && file.canRead())) {
			Util.error("Can't read .jar file: "+file);	return;
	    }
		JarFile jarFile=null;
	    try { jarFile=new JarFile(file);
	        Manifest manifest=jarFile.getManifest();
	        Attributes mainAttributes=manifest.getMainAttributes();
	        Set<Object> keys=mainAttributes.keySet();
	        for(Object key:keys) {
		        String val=mainAttributes.getValue(key.toString());
		        Util.println(key.toString()+"=\""+val+"\"");
	        }
	        
	        Enumeration<JarEntry> entries=jarFile.entries();
	        while(entries.hasMoreElements()) {
	        	JarEntry entry=entries.nextElement();
	        	String size=""+entry.getSize();
	        	while(size.length()<6) size=" "+size;
	        	FileTime fileTime=entry.getLastModifiedTime();
	        	String date = DateTimeFormatter.ofPattern("uuuu-MMM-dd HH:mm:ss", Locale.getDefault())
	        			    .withZone(ZoneId.systemDefault()).format(fileTime.toInstant());
	        	Util.println("Jar-Entry: "+size+"  "+date+"  \""+entry+"\"");
	        }
	    } catch(IOException e) {
	    	Util.IERR("Caused by:",e);
        } finally {
        	if(jarFile!=null)
				try { jarFile.close(); } catch (IOException e) { e.printStackTrace(); }
	    }
	}
	
	// ***************************************************************
	// *** LIST .class file
	// ***************************************************************
	/**
	 * Print a .class file listing.
	 * 
	 * @param classFileName the .class file name
	 */
	public static void doListClassFile(final String classFileName) {
		try { execute("javap","-c","-l","-p","-s","-verbose",classFileName);
		} catch (IOException e) { Util.IERR("Impossible",e); }
	}

	// ***************************************************************
	// *** EXECUTE OS COMMAND
	// ***************************************************************
	private static int execute(final Vector<String> cmdarray) throws IOException {
		String[] cmds=new String[cmdarray.size()];
		cmdarray.copyInto(cmds);
		return(execute(cmds));
	}
	
	/**
	 * Execute an OS command
	 * @param cmdarray command array
	 * @return exit value
	 * @throws IOException if an I/O error occurs
	 */
	public static int execute(final String... cmdarray) throws IOException {
		Runtime runtime = Runtime.getRuntime();
		if (Option.verbose) {
			String line="";
			for(int i=0;i<cmdarray.length;i++) line=line+" "+cmdarray[i];
			Util.println("Execute: " + line);
		}
		Process process = runtime.exec(cmdarray);
		InputStream err = process.getErrorStream();
		InputStream inp = process.getInputStream();
		StringBuilder error=new StringBuilder();
		while (process.isAlive()) {
			while (err.available() > 0) {
				char c=(char)err.read();
				System.err.append(c);
				error.append(c);
			}
			while (inp.available() > 0) {
				if(Global.console != null) Global.console.write(""+(char) inp.read());
				else System.out.append((char) inp.read());
			}
		}
		if(error.length()>0) Util.error(error.toString());
		return (process.exitValue());
	}

}
