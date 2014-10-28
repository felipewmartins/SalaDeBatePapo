package Sala;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

  //public FileManager(){}
  
  public void createFile(String[] msg){
    File database = new File("file.txt");
    try( FileWriter fw = new FileWriter( database ) ){
      BufferedWriter bw = new BufferedWriter(fw);
      for (String string : msg) {
        bw.write(string);
      }
      //bw.write( msg. );
      bw.flush();
      bw.close();
    }catch(IOException ex){
      ex.printStackTrace();
    }
  }
}
