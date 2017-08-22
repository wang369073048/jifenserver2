package org.trc.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {
	
	public static final int POOL_SIZE = 100;
	
	public static ExecutorService service = Executors.newFixedThreadPool(POOL_SIZE);
	
	public static void execute(Runnable task){
		service.execute(task);
	}
}
