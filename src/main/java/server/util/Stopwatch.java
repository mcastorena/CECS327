package server.util;

public class Stopwatch {
    static long start = 0;
    static long end = 0;

    public static void start() {
        start = System.currentTimeMillis();
    }

    public static void stop() {
        end = System.currentTimeMillis();
    }

    public static void reset() {
        start = 0;
        end = 0;
    }

    public static void time() {
        if (end == 0) stop();
        System.out.println(end-start + "ms");
        reset();
    }
}
