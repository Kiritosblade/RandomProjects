package gitrepo.beatSaber;

import java.io.*;
import java.util.ArrayList;

public class BeatSaberSongDataToUnityAnimationConversion
{
    int _lineIndex, _lineLayer;
    int _type, _cutDirection, _time;
    public ArrayList<NoteToHit> note = new ArrayList<NoteToHit>();
    NoteToHit _note;
    File file, newFile;
    FileReader fr;
    FileWriter writer;
    BufferedReader br;
    
    public BeatSaberSongDataToUnityAnimationConversion()
    {
        try
        {
            file = new File("Exported Song.txt");
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line = "";
            while((line = br.readLine())!=null)
            {
                if(line.contains("time"))
                {
                    _note = new NoteToHit();
                    _note.setTime(line);
                }
                else if(line.contains("Index"))
                {
                    _note.setLane(line);
                }
                else if(line.contains("Layer"))
                {
                    _note.setLayer(line);
                }
                else if(line.contains("type"))
                {
                    _note.setType(line);
                }
                else if(line.contains("Direction"))
                {
                    _note.setCutDirection(line);
                    note.add(_note);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void translateToJsonFormat()
    {
        try
        {
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args)
    {
        BeatSaberSongDataToUnityAnimationConversion s;
        s = new BeatSaberSongDataToUnityAnimationConversion();
        try{
            
        
            File newFile = new File("Exported Final Song.txt");
            FileWriter fw = new FileWriter(newFile);
            for(int i = 0; i < s.note.size();i++)
            {
                fw.write("key:"+s.note.get(i).getKeyFrame() + "\nlane:" + s.note.get(i).getLane() + "\nlayer:" +
                        s.note.get(i).getLayer() + "\ncut:" + s.note.get(i).getCutDirection() +
                        "\ntype:" + s.note.get(i).getType() + "\n\n");
            }
            fw.close();
        }catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("ERROR @ main");
        }
    }
}