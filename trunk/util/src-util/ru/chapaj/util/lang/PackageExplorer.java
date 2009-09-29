package ru.chapaj.util.lang;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;

import ru.chapaj.util.swing.tree.ExtendTree;
import ru.dolganov.tool.knowledge.collector.App;
import sun.misc.Launcher;

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
        URL url = Launcher.class.getResource(name);
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
			int index = dirPath.lastIndexOf(pckgname);
			if(index < 0) {
				System.out.println("bad");
				return;
			}
			
			String currentPackage =dirPath.substring(index).replace('/','.').replace('\\', '.');
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
