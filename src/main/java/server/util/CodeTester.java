package server.util;

import java.io.FileInputStream;
import java.util.Scanner;

/**
 * This class is used as a tester
 */
public class CodeTester {
    public static void main(String[] args) throws Exception {
        try {
            for (int i = 0; i < 1; i++) {

                var p = new FileInputStream("8727380415757208375/repository/8555781317612585347");
                Scanner in = new Scanner(p);
                in.useDelimiter("\\A");
                System.out.println(in.next());

            }
        } finally {

            System.out.println("Done.");
        }
    }
}
