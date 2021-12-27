package com.example.leaf_soc;

import java.io.File;
import java.io.FileWriter;

public class Log {
    public void fileWriter (String msg){
        FileWriter fWriter;
        String pathLogFileName = "/storage/emulated/0/LeafLog.txt";
        File sdCardFile = new File(pathLogFileName);
        try{
            fWriter = new FileWriter(sdCardFile, true);
            fWriter.write("\n");
            fWriter.write(msg);
            fWriter.flush();
            fWriter.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
