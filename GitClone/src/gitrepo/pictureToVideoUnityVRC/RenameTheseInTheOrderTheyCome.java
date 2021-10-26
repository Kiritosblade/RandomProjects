package gitrepo.pictureToVideoUnityVRC;

import java.io.*;
import java.util.*;

public class RenameTheseInTheOrderTheyCome
{
    private String ROOT_DIR = "location of original output images to use";
    private File ROOT_FOLDER;
    private File[] ROOT_FOLDER_FILES;
    private File[] tempArray;
    private File renamedFile;
    
    private String TEMP_OUT_DIR;
    private String OUT_DIR = "output folder to create the new renamed images";
    private File OUT_FILE;
    
    private String FORMAT;
    
    public void getTheseRenamedAndSorted()
    {
        try
        {
            
            ROOT_FOLDER = new File(ROOT_DIR);
            ROOT_FOLDER_FILES = ROOT_FOLDER.listFiles();
            tempArray = new File[ROOT_FOLDER_FILES.length];
            
            for(int i = 0; i < ROOT_FOLDER_FILES.length; i++)
            {
                String ext = ROOT_FOLDER_FILES[i].getName().substring(ROOT_FOLDER_FILES[i].getName().indexOf("."));
                String name0 = ROOT_FOLDER_FILES[i].getName().substring(0, ROOT_FOLDER_FILES[i].getName().indexOf("."));
                String name1 = ROOT_FOLDER_FILES[i].getName();
                if(Integer.parseInt(name0) >= 0 && Integer.parseInt(name0) < 10){//0 - 9
                    name0 = "00000" + name1.substring(name1.indexOf(".")-1);
                    renamedFile = new File(ROOT_DIR + "\\" + name0);
                    ROOT_FOLDER_FILES[i].renameTo(renamedFile);
                }else if(Integer.parseInt(name0) >= 10 && Integer.parseInt(name0) < 100){//10 - 99
                    name0 = "0000" + name1.substring(name1.indexOf(".")-2);
                    renamedFile = new File(ROOT_DIR + "\\" + name0);
                    ROOT_FOLDER_FILES[i].renameTo(renamedFile);
                }else if(Integer.parseInt(name0) >= 100 && Integer.parseInt(name0) < 1000){//100 - 999
                    name0 = "000" + name1.substring(name1.indexOf(".")-3);
                    renamedFile = new File(ROOT_DIR + "\\" + name0);
                    ROOT_FOLDER_FILES[i].renameTo(renamedFile);
                }else if(Integer.parseInt(name0) >= 1000 && Integer.parseInt(name0) < 10000){//1,000 - 9,999
                    name0 = "00" + name1.substring(name1.indexOf(".")-4);
                    renamedFile = new File(ROOT_DIR + "\\" + name0);
                    ROOT_FOLDER_FILES[i].renameTo(renamedFile);
                }else if(Integer.parseInt(name0) >= 10000 && Integer.parseInt(name0) < 100000){//10,000 - 99,999
                    name0 = "0" + name1.substring(name1.indexOf(".")-5);
                    renamedFile = new File(ROOT_DIR + "\\" + name0);
                    ROOT_FOLDER_FILES[i].renameTo(renamedFile);
                }else if(Integer.parseInt(name0) >= 100000 && Integer.parseInt(name0) < 1000000){//100,000 - 999,999
                    //name0 = name0;
                    renamedFile = new File(ROOT_DIR + "\\" + name0 + ext);
                    ROOT_FOLDER_FILES[i].renameTo(renamedFile);
                }
            }
            // Everything above renames files, appending x zeros to start of name to make it a 6 digit # so they can be sorted after
            // Line below sorts the array by numerical order
            tempArray = sortByNumber(ROOT_FOLDER_FILES);
            
            
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    public void renameTheseFiles()
    {
        ROOT_FOLDER = new File(ROOT_DIR);
        ROOT_FOLDER_FILES = ROOT_FOLDER.listFiles();
        for(int i = 1; i < ROOT_FOLDER_FILES.length; i++)
        {
            String ext = ROOT_FOLDER_FILES[i].getName().substring(ROOT_FOLDER_FILES[i].getName().indexOf("."));
            String name = "";
            if(i > 0 && i < 10)
            {
                name = "000" + i + ext;
                File renamedFile = new File(ROOT_DIR + "\\" + name);
                ROOT_FOLDER_FILES[i].renameTo(renamedFile);
            }
            else if(i > 9 && i < 100)
            {
                name = "00" + i + ext;
                File renamedFile = new File(ROOT_DIR + "\\" + name);
                ROOT_FOLDER_FILES[i].renameTo(renamedFile);
            }
            else if(i > 99 && i < 1000)
            {
                name = "0" + i + ext;
                File renamedFile = new File(ROOT_DIR + "\\" + name);
                ROOT_FOLDER_FILES[i].renameTo(renamedFile);
            }
            else if(i > 100 && i < 10000)
            {
                name = "" + i + ext;
                File renamedFile = new File(ROOT_DIR + "\\" + name);
                ROOT_FOLDER_FILES[i].renameTo(renamedFile);
            }
            
            
        }
    }
    
    
    public File[] sortByNumber(File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                int n1 = extractNumber(o1.getName());
                int n2 = extractNumber(o2.getName());
                return n1 - n2;
            }

            private int extractNumber(String name) {
                int i = 0;
                try {
                    int e = name.indexOf(".");
                    String number = name.substring(0, e);
                    i = Integer.parseInt(number);
                } catch(Exception e) {
                    i = 0; // if filename does not match the format
                    System.out.println("ERROR");     // then default to 0
                }
                return i;
            }
        });
        return files;
    }
    
    public void RUNME()
    {
        //getTheseRenamedAndSorted();
        //renameTheseFiles();
    }
    
    public static void main(String[] args){
        RenameTheseInTheOrderTheyCome c = new RenameTheseInTheOrderTheyCome();
        c.RUNME();
    }
    
}