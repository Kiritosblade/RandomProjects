package gitrepo.pictureToVideoUnityVRC;

import java.util.*;
import java.io.*;

public class GetGUIDSForMatAssigning
{
    private File ROOT, MAT;
    private File[] FOLDER;
    private String[] GUIDS;
    
    private BufferedReader br;
    private String DIR = "location of pictures";
    
    private String begin = "  - {fileID: 2100000, guid: ";
    private String end = ", type: 2}";
    
    public void listFilesAndGetGuids()
    {
        
        try
        {
            String line, temp, temp2;
            ROOT = new File(DIR);
            FOLDER = ROOT.listFiles();
            GUIDS = new String[FOLDER.length];
            for(File f : FOLDER)
            {
                
                if(f.getName().contains(".meta"))
                {
                    
                    br = new BufferedReader(new FileReader(f));
                    while((line = br.readLine()) != null)
                    {
                        
                        if(line.contains("guid: "))
                        {
                            temp = line.substring(6);
                            temp2 = begin + temp + end;
                            System.out.println(temp2);
                        }
                        
                    }
                    
                }
                
            }
            
        }
        catch(Exception e)
        {
            
        }
        
    }
    
    public static void main(String[] args)
    {
        GetGUIDSForMatAssigning g = new GetGUIDSForMatAssigning();
        g.listFilesAndGetGuids();
    }
}