package gitrepo.pictureToVideoUnityVRC;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class ChangeMetasToMatch
{
    
    // This project is for renaming the meta files for each image
    // purpose of this is to generate the values without having to interact with thousands of images at once in unity
    // which btw will freeze up 
    
    private String ROOT_DIR = "the location of pictures to use";
    
    private String line24 = "  streamingMipmaps: 1";//MipmapEnabled
    private String line64 = "    maxTextureSize: 1024";//MaxTextureSize
    private String line67 = "    textureCompression: 2";//The compressions Quality Level
    private String line68 = "    compressionQuality: 50";//The compressions Quality %
    //after line 73 add this
    private String insert00 = "  - serializedVersion: 3";
    private String insert01 = "    buildTarget: Standalone";
    private String insert02 = "    maxTextureSize: 1024";
    private String insert03 = "    resizeAlgorithm: 0";
    private String insert04 = "    textureFormat: -1";
    private String insert05 = "    textureCompression: 3";
    private String insert06 = "    compressionQuality: 40";
    private String insert07 = "    crunchedCompression: 1";
    private String insert08 = "    allowsAlphaSplitting: 0";
    private String insert09 = "    overridden: 0";
    private String insert10 = "    androidETC2FallbackOverride: 0";
    private String insert11 = "    forceMaximumCompressionQuality_BC6H_BC7: 0";
   
    //everything after this is the same in both original and new fles
    
    private File FOLDER;
    private File[] folderFiles;
    
    private String fileName;
    
    private File NEW01;
    private File NEW02;
    
    private BufferedReader br;
    private BufferedWriter bw;
    
    private String[] lines, newLines;
    private String line;
    
    
    public static void main(String[] args)
    {
        ChangeMetasToMatch c = new ChangeMetasToMatch();
        //c.readFileIntoArray();
        c.RunMe();
    }
    
    private void RunMe()
    {
        
        try{
            
            FOLDER = new File(ROOT_DIR);
            folderFiles = FOLDER.listFiles();
            for(File f : folderFiles)
            {
                
                if(f.getName().contains(".meta"))
                {
                    fileName = f.getName();
                    setFileContents(f);
                    
                }
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("ERROR");
            System.exit(0);
        }
        
    }
    
    private void setFileContents(File oldFile)
    {
        
        try
        {
            br = new BufferedReader(new FileReader(oldFile));
            int lineCount = 0;
            while((line = br.readLine()) != null)
            {
                lineCount++;//counts total lines in file
            }
            br.close();
            
            int count = 1;
            br = new BufferedReader(new FileReader(oldFile));
            lines = new String[lineCount];
            
            while((line = br.readLine()) != null)
            {
                lines[count-1] = line;//sets lines String[count-1] to line being read from file
                count++;//keeps track of the line index
            }
            br.close();
            
            count = 1;
            newLines = new String[lines.length + 12];//creates new String[] newLines of size lines.length+12
            newLines = getIntoNew(lines, newLines);
            NEW02 = new File(oldFile.getAbsolutePath());
            bw = new BufferedWriter(new FileWriter(NEW02));
            for(String s : newLines)
            {
                bw.write(s + "\n");
            }
            bw.close();

        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("ERROR");
            System.exit(0);
        }
        
    }
    
    private String[] getIntoNew(String[] input, String[] inputT)
    {
        int count = 1;
        for(String s : input)
        {
            if(count < 73 && count != 24 && count != 64 && count != 67 && count != 68 && count != 73){
                inputT[count-1] = s;
            }else if(count == 24){
                inputT[count-1] = line24;
            }else if(count == 64){
                inputT[count-1] = line64;
            }else if(count == 67){
                inputT[count-1] = line67;
            }else if(count == 68){
                inputT[count-1] = line68;
            }else if(count == 73){
                inputT[count-1] = s;
                inputT[count] = insert00;
                inputT[count+1] = insert01;
                inputT[count+2] = insert02;
                inputT[count+3] = insert03;
                inputT[count+4] = insert04;
                inputT[count+5] = insert05;
                inputT[count+6] = insert06;
                inputT[count+7] = insert07;
                inputT[count+8] = insert08;
                inputT[count+9] = insert09;
                inputT[count+10] = insert10;
                inputT[count+11] = insert11;
            }else if(count > 73){
                inputT[count+11] = s;
            }
            count++;
        }
        return inputT;
    }
    
    
}