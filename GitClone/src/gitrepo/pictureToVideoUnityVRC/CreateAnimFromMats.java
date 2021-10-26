package gitrepo.pictureToVideoUnityVRC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class CreateAnimFromMats
{
    // AUDIO IS 385 Seconds
    // TOTAL IMAGES = 8018
    //      images/seconds=20.82 so 21 per second
    //          
    
    private float TimeStampPerKey = 0.5F;
    
    private String FILE_NAME = "2fps Shrek";
    private String FPS = " 2";
    private int SECONDS = 5405;
    
    private String DIR = "materials folder";
    private String REF_ANI = "master animation location";
    private String OUT_ANI = "output location for generated animation";
    
    // Open line =  m_PPtrCurves:
    //curveStartingLine = "  - curve:";
    private String curveTime = "    - time: "; // TIME APPEND TOEND OF THIS FOR EACH - float
    private String curveValueStart = "      value: {fileID: 2100000, guid: ";
    // GUID GETS PUT IN MIDDLE OF THESE TWO FOR EACH
    private String curveValueEnd = ", type: 2}";
    //curveEndingLine = "    attribute: m_Materials.Array.data[0]";
    
    //----------------------------------------------------------------------
    
    // Open line =  m_ClipBindingConstant:
    //bindingStartingLine = "    pptrCurveMapping:";
    private String bindingCurveStart = "    - {fileID: 2100000, guid: ";
    // GUID GETS PUT IN MIDDLE OF THESE TWO FOR EACH
    private String bindingCurveEnd = ", type: 2}";
    
    
    private File REF_ANI_FILE;
    private FileWriter fr;
    private BufferedReader br;
    private BufferedWriter bw;
    private String[] OriginalAnimation;
    
    private File ROOT;
    private File[] folderFiles;
    private File[] newMetaFiles;
    private ArrayList<File> metaFiles;
    private ArrayList<GUID> guids;
    private GUID guid;
    
    private ArrayList<Curve_Value> CURVES;
    private ArrayList<Curve_Time> CURVES_TIME;
    private ArrayList<Bind> BINDS;
    
    private ArrayList<BLOCK> blocks;
    private BLOCK block;
    
    private Bind bind;
    private Curve_Time time;
    private Curve_Value value;
    
    private String line;
    
    private File finalOutput;
    
    
    public static void main(String[] args)
    {
        
        CreateAnimFromMats c = new CreateAnimFromMats();
        c.Run();
    }
    
    public void Run()
    {
        //DoThis();
        blocks = new ArrayList<BLOCK>();
        getGuidsAndPathsIntoArrayList();
        getOriginalAnimationIntoArray();
        createCurvesFromInput();
        appendAllCurvesToOriginalFile();
    }
    
    private void DoThis()
    {
        
        try{
            
            File tempRoot = new File(DIR);
            File[] tempFolder = tempRoot.listFiles();
            File tempFile;
            
            for(File f : tempFolder)
            {
                
                if(f.getName().contains(".mat"))
                {
                    String line = "";
                    int lineCount = 0;
                    br = new BufferedReader(new FileReader(f));
                    while((line = br.readLine()) != null)
                    {
                        lineCount++;
                    }
                    
                    String[] info = new String[lineCount];
                    int count = 0;
                    
                    while((line = br.readLine()) != null)
                    {
                        info[count] = line;
                        count++;
                    }
                    
                    fr = new FileWriter(f);
                    bw = new BufferedWriter(fr);
                    
                    info[797] = "    - _MainEmissionStrength: 1.5";
                    
                    for(int i = 0; i < info.length; i++)
                    {
                        
                        bw.write(info[i]);
                        
                    }
                    bw.close();
                    System.out.println(f.getName() + " - " + "has been wrote to");
                }
                
            }
            
        }catch(Exception e){
            
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
                    int s = name.indexOf('(')+1;
                    int e = name.lastIndexOf(')');
                    String number = name.substring(s, e);
                    i = Integer.parseInt(number);
                } catch(Exception e) {
                    i = 0; // if filename does not match the format
                           // then default to 0
                }
                return i;
            }
        });
        return files;
    }
    
    
    public void getGuidsAndPathsIntoArrayList()
    {
        
        try
        {
            metaFiles = new ArrayList<File>();
            ROOT = new File(DIR);
            if(ROOT == null){
                System.out.println("ERROR");
                System.exit(0);
            }
            folderFiles = ROOT.listFiles();
            if(folderFiles == null){
                System.out.println("ERROR");
                System.exit(0);
            }
            File[] tempa = new File[folderFiles.length];
            tempa = sortByNumber(folderFiles);
            folderFiles = tempa;
            for(int k = 0; k < folderFiles.length; k++){
                if(folderFiles[k].getName().contains(".meta")){
                    metaFiles.add(folderFiles[k]);
                }
            }
        }    
        catch(Exception e)
        {
            e.printStackTrace();
        }
        //SORT ME WITH NEW METHOD
        File[] temp = new File[metaFiles.size()];
        int k = 0;
        for(File f : metaFiles)
        {
            temp[k] = f;
            k++;
        }
        File[] temp2 = sortByNumber(temp);
        newMetaFiles = temp2;
        /////////////////////////////////////////////
        for(int a = 0; a < newMetaFiles.length; a++){
            try{
                br = new BufferedReader(new FileReader(newMetaFiles[a]));
                while((line = br.readLine())!=null){
                    block = new BLOCK();
                    if(line.contains(("guid: "))){
                        String tempk = line.substring(6);
                        block.GUID = tempk;
                        blocks.add(block);
                    }
                }
                
                //Arrays.sort(blocks);
            }catch(Exception e){
                e.printStackTrace();
                System.exit(0);
            }
            //System.out.println("File Name: " + f.getName() + ", Block Time: " + block.GUID);
        }
    }
        
    public void getOriginalAnimationIntoArray(){
            try{
                OriginalAnimation = new String[66];
                REF_ANI_FILE = new File(REF_ANI);
                br = new BufferedReader(new FileReader(REF_ANI_FILE));
                int i = 0;
                while((line = br.readLine())!=null){
                    String temp;
                    if(line.contains("  m_Name:"))
                    {
                        temp = line.substring(0,9);
                        temp += " " + FILE_NAME;
                        line = temp;
                        OriginalAnimation[i] = line;
                        
                    }else if(line.contains("  m_SampleRate:")){
                        temp = line.substring(0,15);
                        temp += FPS;
                        line = temp;
                        OriginalAnimation[i] = line;
                        
                    }else if(line.contains("    m_StopTime:")){
                        temp = line.substring(15);
                        temp += " " + SECONDS;
                        OriginalAnimation[i] = line;
                    }else{
                        OriginalAnimation[i] = line;
                    }
                    i++;
                }
                
            }catch(Exception e){
                e.printStackTrace();
                System.exit(0);
            }
        }
        
    public void createCurvesFromInput(){
        float i = 0F;

        for(int x = 0; x < blocks.size();x++)
        {
            blocks.get(x).BIND = bindingCurveStart + blocks.get(x).GUID + bindingCurveEnd;
            blocks.get(x).TIME  = curveTime + (i);
            blocks.get(x).VALUE = curveValueStart + blocks.get(x).GUID + curveValueEnd;
            i+=TimeStampPerKey;
        }
    }
    
    public void appendAllCurvesToOriginalFile()
    {
        
        try
        {
            OUT_ANI += FILE_NAME + ".anim";
            finalOutput = new File(OUT_ANI);
            fr = new FileWriter(finalOutput);
            bw = new BufferedWriter(fr);
            
            // First write the first 21 lines
            for(int a = 0; a < 21;a++)
            {
                bw.write(OriginalAnimation[a] + "\n");
            }
            
            // Next append the curve time followed by curve value for each curve in order
            for(int x = 0; x < blocks.size();x++)
            {
                bw.write(blocks.get(x).TIME + "\n");
                bw.write(blocks.get(x).VALUE + "\n");
            }
            
            // Next Copy second part of original after curves
            for(int b = 21; b < 40; b++)
            {
                bw.write(OriginalAnimation[b] + "\n");
            }
            
            // Now write the BINDS
            for(int z = 0; z < blocks.size();z++)
            {
                bw.write(blocks.get(z).BIND + "\n");
            }
            
            // finally add in the ending of animation file
            for(int c = 40; c < 65; c++)
            {
                bw.write(OriginalAnimation[c] + "\n");
            }
            bw.close();
        }catch(Exception e){
            e.printStackTrace();
            System.exit(0);
        }
        
    }
}