package cn.mz.live.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gavin
 * Time 2017/10/27  14:47
 * Email:molu_clown@163.com
 */

public class ThreadPoolUtils {

    private ThreadPoolUtils() {
    }

    private static final ExecutorService threadPool = Executors.newCachedThreadPool();

    public static ExecutorService getThreadPool() {
        return threadPool;
    }
}
