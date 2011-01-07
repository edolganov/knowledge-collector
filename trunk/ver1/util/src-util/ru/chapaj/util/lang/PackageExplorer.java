package ru.chapaj.util.lang;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PackageExplorer {
	
	
	//test
//	public static void main(String[] args) {
//		find("ru", new Callback(){
//
//			@Override
//			public void found(Class<?> clazz) {
//				System.out.println(clazz);
//			}
//			
//		});
//	}
	
	public static interface Callback {
		void found(Class<?> clazz);
	}
	
	public static void find(String pckgname, Callback callback) {
        // Code from JWhich
        // ======
        // Translate the package name into an absolute path
        String name = new String(pckgname);
        if (!name.startsWith("/")) {
            name = "/" + name;
        }        
        name = name.replace('.','/');
        
        // Get a File object for the package
        
        URL url = Thread.currentThread().getClass().getResource(name);//Launcher.class.getResource(name);
        String externalForm = url.toExternalForm();
        if(externalForm.startsWith("jar") || externalForm.startsWith("war")){
        	String filePath = url.getFile();
        	int index = filePath.lastIndexOf('!');
        	if(index > -1){
        		filePath = filePath.substring(0, index);
        		filePath = filePath.substring(6);
        		name = name.substring(1);
				try {
					ZipFile zipFile = new ZipFile(new File(filePath));
	    			Enumeration<? extends ZipEntry> entries = zipFile.entries();
	    			while(entries.hasMoreElements()){
	    				// "org/apache/commons/httpclient/auth/MalformedChallengeException.class"
	    				String element = entries.nextElement().getName();
	    				if(element.endsWith(".class")){
	    					String classPath = element.substring(0,element.length()-6);
	    					//System.out.println(name+" "+classPath);
	    					if(classPath.startsWith(name)){
	    						classPath = classPath.replace('/','.').replace('\\', '.');
		    					//org.apache.commons.httpclient.auth.MalformedChallengeException
	    						callback.found(Class.forName(classPath));
	    					}
	    				}
						
	    			}
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}
        }
        else {
	        String filePath = url.getFile();
	        File root = new File(filePath);
	        
			//from http://forums.sun.com/thread.jspa?threadID=341935, but throws java.lang.NullPointerException
	        //directory = new File(Thread.currentThread().getContextClassLoader().getResource(filePath).getFile());
	        
			// New code
	        // ======
			LinkedList<File> dirs = new LinkedList<File>();
			dirs.addLast(root);
			while(!dirs.isEmpty()) {
				File directory = dirs.removeFirst();
				String dirPath = directory.getAbsolutePath();
				String packagePath = dirPath.replace('/','.').replace('\\', '.');
				int index = packagePath.lastIndexOf(pckgname);
				if(index < 0) {
					System.out.println("bad path:"+packagePath);
					return;
				}
				
				String currentPackage =packagePath.substring(index);
				if(currentPackage.endsWith(".")) currentPackage = currentPackage.substring(0, currentPackage.length()-1);
				//System.out.println(currentPackage);
		        if (directory.exists()) {
		            // Get the list of the files contained in the package
		            String [] files = directory.list();
		            for (int i=0;i<files.length;i++) {
		                File file = new File(dirPath+"/"+files[i]);
		                if(file.isDirectory()){
		                	dirs.addLast(file);
		                }
		                // we are only interested in .class files
		                else if (files[i].endsWith(".class")) {
		                    // removes the .class extension
		                    String classname = files[i].substring(0,files[i].length()-6);
		                    try {
		
		                        String classPath = currentPackage+"."+classname;
		                        callback.found(Class.forName(classPath));
		                        // Try to create an instance of the object
								//Object o = .newInstance();
		                        
		                    } catch (ClassNotFoundException cnfex) {
		                        System.err.println(cnfex);
		                    }
		                    catch (ExceptionInInitializerError eiie){
		                    	/* [29.09.2009] jenua.dolganov: возникает когда внутри класса
		                    	есть сложное статичное заполнение поля
		                    	
		                    	пример:
		                    	public class Actions {
		                    	 ...
		                    	 static ExtendTree tree = App.getDefault().getUI().tree;
		                    	*/
		                    	
		                    	//nothing
		                    }
		//                    } catch (InstantiationException iex) {
		//                        // We try to instantiate an interface
		//                        // or an object that does not have a 
		//                        // default constructor
		//                    	System.err.println(iex);
		//                    } catch (IllegalAccessException iaex) {
		//                        // The class is not public
		//                    	System.err.println(iaex);
		//                    }
		                }
		            }
		        }
			}
        }
	}
	
}
