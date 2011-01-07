package ru.chapaj.util.os;

import java.io.*;

public class ProcessWrapper {
	
	public static boolean usingLog = false;

    public static int exec(String executeExpression)throws Exception {
        return exec(executeExpression, null,true, null);
    }

    public static int exec(String executeExpression, OutputStream redirect)throws Exception {
        return exec(executeExpression, redirect, true);
    }
    
    public static int exec(String executeExpression, OutputStream redirect, boolean needOutputStream)throws Exception {
        return exec(executeExpression, redirect,true, null);
    }

    public static int exec(String executeExpression, OutputStream redirect,boolean needOutputStream, File dir)throws Exception {
            Runtime rt = Runtime.getRuntime();
            if(usingLog) System.out.println("Run: " + executeExpression);
            Process proc;
            if (dir != null) {
                proc = rt.exec(executeExpression, null, dir);
            } else {
                proc = rt.exec(executeExpression);
            }

            // any error message?
            StreamGobbler errorGobbler = new StreamGobbler(proc
                    .getErrorStream(), "ERROR");

            // any output?
            StreamGobbler outputGobbler = new StreamGobbler(proc
                    .getInputStream(), "OUTPUT", redirect,needOutputStream);

            // kick them off
            errorGobbler.start();
            outputGobbler.start();

            // any error?
            int exitVal = proc.waitFor();
			if(usingLog) System.out.println("ExitValue: " + exitVal);
            // [06.03.2009] chapaj: неверный код, ибо закрывает поток раньше чем в него все записалось
//			if(redirect != null){
//				redirect.flush();
//				redirect.close();
//			}
            return exitVal;
    }
}

/**
 * from http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html?page=4
 */
class StreamGobbler extends Thread {
    InputStream is;
    String type;
    OutputStream os;
    boolean needClose;

    StreamGobbler(InputStream is, String type) {
        this(is, type, null,true);
    }

    StreamGobbler(InputStream is, String type, OutputStream redirect) {
        this(is, type, redirect, true);
    }
    
    StreamGobbler(InputStream is, String type, OutputStream redirect,boolean needCloseOutput) {
        this.is = is;
        this.type = type;
        this.os = redirect;
        this.needClose = needCloseOutput;
    }

    public void run() {
        try {
            PrintWriter pw = null;
            if (os != null)
                pw = new PrintWriter(os);

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (pw != null)
                    pw.println(line);
                if(ProcessWrapper.usingLog) System.out.println(type + ">" + line);
            }
            if (pw != null) {
                pw.flush();
                if(needClose){
                	pw.close();
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
