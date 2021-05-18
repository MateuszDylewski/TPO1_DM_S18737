package zad1;

import java.io.IOException;
import java.nio.file.*;

public class Futil extends SimpleFileVisitor {
    public static void processDir(String dirName, String resultFileName) {
        Path dirNamePath = Paths.get(dirName);
        Path resultFileNamePath = Paths.get(resultFileName);

        try{
            FileService fileServices = new FileService(resultFileNamePath);
            Files.walkFileTree(dirNamePath,fileServices);
        }catch (IOException e){
            System.out.println("-----Stack trace exception!-----");
            e.printStackTrace();
        }

    }
}
