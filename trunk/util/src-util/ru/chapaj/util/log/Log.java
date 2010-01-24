package ru.chapaj.util.log;

public class Log {
	
	public static interface Out {
		void msg(String msg);
	}
	
	private static class MainLogger{
		private Out out;
		private boolean debug,info,error;
		
		MainLogger(){
			out = new Out(){

				public void msg(String msg) {
					System.out.println(msg);
				}
				
			};
			debug = true;
			info = true;
			error = true;
		}
		
		public void debug(String msg){
			if(debug) out.msg("[DEBUG] "+msg);
		}
		
		public void info(String msg){
			if(info) out.msg("[INFO] "+msg);
		}
		
		public void error(String msg){
			if(error) out.msg("[ERROR] "+msg);
		}
	};
	
	private final static MainLogger mainLogger = new MainLogger();
	
	public static void init(Out out, boolean debug,boolean info,boolean error) {
		mainLogger.out = out;
		mainLogger.debug = debug;
		mainLogger.info = info;
		mainLogger.error = error;
	}
	
	public static Log getInstance(Class informer) {
		return new Log(informer);
	}
	

	private String preffix;
	
	
	private Log(Class informer){
		preffix = informer.getName();
		int i = preffix.lastIndexOf('.');
		if(i > -1) preffix = preffix.substring(i+1);
	}
	



	public void debug(String msg){
		mainLogger.debug(createMsg(msg));
	}
	
	public void info(String msg){
		mainLogger.info(createMsg(msg));
	}
	
	public void error(String msg){
		mainLogger.error(createMsg(msg));
	}

	private String createMsg(String msg) {
		return preffix+": "+msg;
	}

	public void error(Throwable e) {
		StackTraceElement[] stackTrace = e.getStackTrace();
		if(stackTrace != null){
			//TODO ...
		}
		e.printStackTrace();
		//error(e.getMessage());
	}

}
