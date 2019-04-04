package server.chord;
import java.io.*;
import java.rmi.RemoteException;
import java.util.Scanner;

import static server.core.Server.dfs;
import static server.core.Server.NEXT_PORT;

public class DFSCommand
{
    //DFS dfs;
        
//    public DFSCommand(int p, int portToJoin) throws Exception {
//        dfs = new DFS(p);
//
//        if (portToJoin > 0)
//        {
//            System.out.println("Joining "+ portToJoin);
//            dfs.join("127.0.0.1", portToJoin);
//        }
    public DFSCommand(int startPort) throws Exception {

        BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));
        String line = buffer.readLine();  
        while (!line.equals("quit"))
        {
            String[] result = line.split("\\s");
            if (result[0].equals("join"))  //&& result.length > 1)
            {
                // Add a new node to the chord
                dfs = new DFS(NEXT_PORT);

                dfs.join("127.0.0.1", NEXT_PORT - 1);
                NEXT_PORT++;
                //dfs.join("127.0.0.1", Integer.parseInt(result[1]));
            }
            if (result[0].equals("print"))
            {
                dfs.print();     
            }
            if (result[0].equals("leave"))
            {
                dfs.leave();
            }

            // User interface:
            // join, ls, touch, delete, read, tail, head, append, move
            if (result[0].equals("ls"))
            {
                System.out.println(dfs.lists());
            }
            if (result[0].equals("touch"))
            {
                dfs.create(result[1]);                  // User must specify file name
            }
            if (result[0].equals("delete"))
            {
                dfs.delete(result[1]);                  // User must specify file name
            }
            if (result[0].equals("read"))
            {
                //dfs.read(result[1], Integer.parseInt(result[2]));   // User must specify file name and page number
                RemoteInputFileStream r = dfs.read(result[1], Integer.parseInt(result[2]));
                r.connect();
                Scanner scan = new Scanner(r);
                while(scan.hasNext()) {
                    String payload = scan.nextLine();
                    System.out.println("Tail:\t" + payload);
                }
            }
            if (result[0].equals("tail"))
            {
                RemoteInputFileStream r = dfs.tail(result[1]);                    // User must specify file name
                r.connect();
                Scanner scan = new Scanner(r);
                scan.useDelimiter("\\A");
                String payload = scan.next();
                System.out.println("Tail:\t"+payload);
            }
            if (result[0].equals("head"))
            {
                RemoteInputFileStream r = dfs.head(result[1]);                    // User must specify file name
                r.connect();
                Scanner scan = new Scanner(r);
                scan.useDelimiter("\\A");
                String payload = scan.next();
                System.out.println("Head:\t"+payload);
            }
            if (result[0].equals("append"))
            {
                dfs.append(result[1], new RemoteInputFileStream(result[2]));        // User must specify filename they want to append data to and filepath of the data to be appended
            }
            if (result[0].equals("move"))
            {
                dfs.move(result[1], result[2]);         // User must specify file to be edited and its new name
            }

            line=buffer.readLine();  
        }
        // If user inputs quit, exit program
        System.exit(0);
    }
    
//    static public void main(String args[]) throws Exception
//    {
//        DFSCommand dfsCommand=new DFSCommand(2008, 2000);
////        if (args.length < 1 ) {
////            throw new IllegalArgumentException("Parameter: <port> <portToJoin>");
////        }
////        if (args.length > 1 ) {
////            DFSCommand dfsCommand=new DFSCommand(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
////        }
////        else
////        {
////            DFSCommand dfsCommand=new DFSCommand( Integer.parseInt(args[0]), 0);
////        }
//     }
}
