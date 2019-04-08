package server.util;

import server.core.MyTask;

import java.io.*;
import java.nio.file.Paths;
import java.util.Scanner;

public class CodeTester {
    public static void main(String[] args) throws Exception {
        try {
            for (int i = 0; i < 1; i++) {
//                MyTask task = new MyTask("8727380415757208375/repository/8555781317612585347");
//                Thread t = new Thread(task);
//                t.start();
//                t.join();

//                var data = task.getDatastream();
//                data.connect();


//                Scanner in = new Scanner(data);
//                in.useDelimiter("\\A");
//                System.out.println(in.next() + "\n");

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
