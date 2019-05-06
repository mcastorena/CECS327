package server.chord;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.core.Server;
import server.util.MusicJsonSplitter;

import java.io.*;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static server.core.Server.*;

public class DFSCommand
{
    public DFSCommand() throws Exception {

        BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));
        String line = buffer.readLine();  
        while (!line.equals("quit"))
        {
            String[] result = line.split("\\s");
            switch(result[0]) {
                case "join":
                    // Add a new node to the chord
                    if (result.length == 1){
                        try {
                            dfs = new DFS(NEXT_PORT);

                            dfs.join("127.0.0.1", NEXT_PORT - 1);
                            NEXT_PORT++;
                        } catch (NumberFormatException e) {
                            System.out.println("Error - Second argument must be a port number.");
                        }
                    }
                case "print":
                    dfs.print();
                    break;

                case "leave":
                    dfs.leave();
                    break;

                case "ls":
                    System.out.println(dfs.lists());
                    break;

                case "touch":
                    if (result.length == 2)
                        dfs.create(result[1]);                  // User must specify a fileList name
                    break;

                case "delete":
                    if (result.length == 2)
                        dfs.delete(result[1]);                  // User must specify fileList name
                    break;

                case "read":
                    if (result.length == 3) {
                        try {
                            dfs.read(result[1], Integer.parseInt(result[2]));   // User must specify fileList name and page number
                        } catch (NumberFormatException e) {
                            System.out.println("Error - Second argument must be a page number.");
                        }
                    }
                    break;

                case "tail":
                    if (result.length == 2) {
                        dfs.tail(result[1]);                    // User must specify fileList name
                    }
                    break;

                case "head":
                    if (result.length == 2) {
                        dfs.head(result[1]);                    // User must specify fileList name
                    }
                    break;

                case "append":
                    if (result.length == 3) {
                        // Append text by enclosing in single quotes [']
                        if (result[2].matches("^'(.+?)'$")) {
                            Pattern pattern = Pattern.compile("^'(.+?)'$");
                            Matcher matcher = pattern.matcher(result[2]);
                            matcher.find();
                            String text = matcher.group(1);

                            dfs.append(result[1], text); // Appends text
                        }
                        else if (result[1].contains("music"))
                        {

                            Gson gson = new GsonBuilder().setPrettyPrinting().create();

                            var chunks =
                                    MusicJsonSplitter.getMusicJsonChunks(result[2], 100);
//                                DFSCommand.class.getResource("/server/music.json").getPath(),
//                                100);

                            System.out.println("Adding pages to music.json...");
                            int i = 0;
                            for (var chunk : chunks) {
                                String jsonStr = null;
                                try {
                                    jsonStr = gson.toJson(chunk);
                                    dfs.append(result[1], jsonStr);

                                    System.out.println(String.format("Creating page [%d/%d]", ++i, chunks.size()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            //d.updateMusicOnFileAdd();
                            //Server.updateSongList();

                            System.out.println("Done");
                        }
                        else
                            dfs.append(result[1], new RemoteInputFileStream(result[2]));        // User must specify filename they want to append data to and filepath of the data to be appended
                    }
                    break;

                case "move":
                    if (result.length == 3) {
                        dfs.move(result[1], result[2]);         // User must specify fileList to be edited and its new name
                    }
                    break;

                case "runmapreduce":
                    if (result.length == 2)
                        dfs.runMapReduce(result[1], "songInvertedIndex");
                    break;

                default:
                    System.out.println("Error - Not a valid command.");
                    break;
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
