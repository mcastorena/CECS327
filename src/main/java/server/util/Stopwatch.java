package server.util;

/**
 * TODO
 */
public class Stopwatch {

    /**
     * TODO
     */
    static long start = 0;


    /**
     * TODO
     */
    static long end = 0;

    /**
     * TODO
     */
    public static void start() {
        start = System.currentTimeMillis();
    }

    /**
     * TODO
     */
    public static void stop() {
        end = System.currentTimeMillis();
    }

    /**
     * TODO
     */
    public static void reset() {
        start = 0;
        end = 0;
    }

    /**
     * TODO
     */
    public static void time() {
        if (end == 0) stop();
        System.out.println(end - start + "ms");
        reset();
    }
}
