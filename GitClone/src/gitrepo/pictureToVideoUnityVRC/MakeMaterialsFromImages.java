package gitrepo.pictureToVideoUnityVRC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class MakeMaterialsFromImages
{
    //8,018 png files each about 450KB
    private String DIR = "the images folder where image meta files exist";
    
    private File ROOT;
    private File[] folderFiles;
    private ArrayList<File> metaFiles;
    private ArrayList<GUID> guids;
    private GUID guid;
    
    private BufferedReader br;
    private BufferedWriter bw;
    private FileWriter fr;
    private String line;
    private String[] data;
    
    //Generating 8,018 Materials which comes to 3KB each
    private File MASTER_MATERIAL;
    private String MASTER_MAT_DIR = "where to locate the master material we are going to copy";
    private String COPY_MAT_DIR = "where to create the new materials";
    private String POI = "frame ";
    private String MAT = ".mat";
    private String MASTER_MAT_NAME = "MasterMaterial.mat";
    private File COPY_MATERIAL;
    
    private String ReplaceS = "        m_Texture: {fileID: 2800000, guid: ";
    private String ReplaceE = ", type: 3}";
    
    public static void main(String[] args){
        MakeMaterialsFromImages m = new MakeMaterialsFromImages();
        m.RUNME();
    }
    
    public void RUNME()
    {
        getGuidsAndPathsIntoArrayList();
        createMatsFromMaster();
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
            for(File f : folderFiles){
                if(f.getName().contains(".meta")){
                    //Get the image files .meta into array
                    metaFiles.add(f);
                }
            }
        }    
        catch(Exception e)
        {
            e.printStackTrace();
        }
        guids = new ArrayList<GUID>();
        metaFiles.trimToSize();
        for(File f : metaFiles){
            guid = new GUID();
            guid.setPath(f.getAbsolutePath());
            try{
                br = new BufferedReader(new FileReader(f));
                while((line = br.readLine())!=null){
                    if(line.contains(("guid: "))){
                        String temp = line.substring(6);
                        guid.setGuid(temp);
                    }
                }
                
            }catch(Exception e){
                e.printStackTrace();
                System.exit(0);
            }
            guids.add(guid);
        }
        guids.trimToSize();
    }
    
    public void createMatsFromMaster(){
        
        try{
            MASTER_MATERIAL = new File(MASTER_MAT_DIR + MASTER_MAT_NAME);
            data = new String[1456];//Define length in lines of the master material
            int index = 0;
            br = new BufferedReader(new FileReader(MASTER_MATERIAL));
            while((line = br.readLine())!=null){
                data[index] = line;//add each line of the material to data[]
                index++;
            }
            int NUMBER = 1;
            for(int a = 0; a < guids.size(); a++){
                String[] dataCopy = data;//define the copy mats strings
                //dataCopy[70] = ReplaceS + guids.get(a).getGuid() + ReplaceE;//replace _ScreenTex: m_Texture: line with new line
                dataCopy[222] = ReplaceS + guids.get(a).getGuid() + ReplaceE;// depending on the amount lines in the material you use,
                                                                                //you'll have to change these values
                dataCopy[797] = "    - _MainEmissionStrength: 1.5";//change the poiyomi emission to 1.5 for each
                if(NUMBER > 0 && NUMBER < 10){
                    COPY_MATERIAL = new File(COPY_MAT_DIR + POI + "000" + NUMBER + MAT);
                    fr = new FileWriter(COPY_MATERIAL);
                    bw = new BufferedWriter(fr);
                    for(int c = 0; c < dataCopy.length-1; c++){
                        bw.write(dataCopy[c]+"\n");
                    }
                    bw.close();
                    NUMBER++;
                }else if(NUMBER > 9 && NUMBER < 100){
                    COPY_MATERIAL = new File(COPY_MAT_DIR + POI + "00" + NUMBER + MAT);
                    fr = new FileWriter(COPY_MATERIAL);
                    bw = new BufferedWriter(fr);
                    for(int c = 0; c < dataCopy.length-1; c++){
                        bw.write(dataCopy[c]+"\n");
                    }
                    bw.close();
                    NUMBER++;
                }else if(NUMBER > 99 && NUMBER < 1000){
                    COPY_MATERIAL = new File(COPY_MAT_DIR + POI + "0" + NUMBER + MAT);
                    fr = new FileWriter(COPY_MATERIAL);
                    bw = new BufferedWriter(fr);
                    for(int c = 0; c < dataCopy.length-1; c++){
                        bw.write(dataCopy[c]+"\n");
                    }
                    bw.close();
                    NUMBER++;
                }else if(NUMBER > 999){
                    COPY_MATERIAL = new File(COPY_MAT_DIR + POI + NUMBER + MAT);
                    fr = new FileWriter(COPY_MATERIAL);
                    bw = new BufferedWriter(fr);
                    for(int c = 0; c < dataCopy.length-1; c++){
                        bw.write(dataCopy[c]+"\n");
                    }
                    bw.close();
                    NUMBER++;
                }
            }
            
        }catch(Exception e){
            e.printStackTrace();
            System.exit(0);
        }
        
    }
    
}