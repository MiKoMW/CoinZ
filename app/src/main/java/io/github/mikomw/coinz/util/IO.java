package io.github.mikomw.coinz.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class IO {


    public static void writeToFile(String filePath, String fileName, String output){

        File file = new File(filePath,fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(output.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
