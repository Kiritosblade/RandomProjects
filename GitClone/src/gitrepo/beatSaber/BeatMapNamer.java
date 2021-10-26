package gitrepo.beatSaber;

import java.io.*;
import java.util.ArrayList;

public class BeatMapNamer
{
    //Variables
    private File Original, Output;
    private FileReader fileReader;
    private FileWriter Writer;
    private BufferedReader inputData;
    
    private String InputSongDatFile, InputInfoDatFile;
    private String OutputSongDatFile, OutputInfoDatFile;
    private String input = "default text";
    private ArrayList<String> outputString;
    
    public BeatMapNamer(String songDir, String outSong, String infoDir, String outInfo)
    {
        InputSongDatFile = songDir;
        InputInfoDatFile = infoDir;
        OutputSongDatFile = outSong;
        OutputInfoDatFile = outInfo;
    }
    
    public void FormatSongDatFile()
    {
        try
        {
            //More Variables
            Original = new File(InputSongDatFile);
            fileReader = new FileReader(Original);
            inputData = new BufferedReader(fileReader);
            outputString = new ArrayList<String>();
            input = inputData.readLine();
            String line = "";
            for(int i = 0; i < input.length(); i ++)
            {
                char temp = input.charAt(i);
                String s = ""+temp;
                if(s.equalsIgnoreCase(","))
                {
                    line += s;
                    if(line.contains("},"))
                    {
                        line = line.concat("\n");
                        outputString.add(line);
                        line = "";
                    }
                    else
                    {
                        outputString.add(line);
                        line = "";
                    }
                    
                    //this is each line seperated by commas
                    //once this check clears, the line gets added to the arraylist
                }
                else
                {
                    line = line.concat(s);
                    //add this char to end of line until the line is finished up above
                    if(line.contains("\"_notes\":[{"))
                    {
                        
                        outputString.add(line+"\n");
                        line = "";
                    }
                    if(line.contains("}]}"))
                    {
                        //line = line.concat("\n");
                        outputString.add(line);
                        //this is the end of the final line in file
                    }
                }
            }
            SendToSongFile(outputString);
        }
        catch(Exception e)
        {
            System.out.println("ERROR AT OLD SONG FILE READING");
            e.printStackTrace();
            System.exit(0);
        }
    }
    
    public void FormatInfoDatFile()
    {
        try
        {
            //More Variables
            Original = new File(InputInfoDatFile);
            fileReader = new FileReader(Original);
            inputData = new BufferedReader(fileReader);
            outputString = new ArrayList<String>();
            input = inputData.readLine();
            String line = "";
            for(int i = 0; i < input.length(); i ++)
            {
                char temp = input.charAt(i);
                String s = ""+temp;
                if(s.equalsIgnoreCase(","))
                {
                    line = line.concat(s);
                    outputString.add(line);
                    line = "";
                    //this is each line seperated by commas
                    //once this check clears, the line gets added to the arraylist
                }
                else
                {
                    line = line.concat(s);
                    //add this char to end of line until the line is finished up above
                    if(line.contains("}}]}]}"))
                    {
                        outputString.add(line);
                        //this is the end of the final line in file
                    }
                }
            }
            SendToInfoFile(outputString);
        }
        catch(Exception e)
        {
            System.out.println("ERROR AT OLD INFO FILE READING");
            e.printStackTrace();
            System.exit(0);
        }
    }
    
    private void SendToSongFile(ArrayList<String> s)
    {
        try
        {
            Output = new File(OutputSongDatFile);
            Writer = new FileWriter(Output);
            for(int i = 0; i < s.size();i++)
            {
                Writer.write(s.get(i) + "\n");
            }
            Writer.close();
        }
        catch(Exception e)
        {
            System.out.println("ERROR AT NEW SONG FILE CREATION");
            e.printStackTrace();
            System.exit(0);
        }
    }
    
    private void SendToInfoFile(ArrayList<String> s)
    {
        try
        {
            Output = new File(OutputInfoDatFile);
            Writer = new FileWriter(Output);
            for(int i = 0; i < s.size();i++)
            {
                Writer.write(s.get(i) + "\n");
            }
            Writer.close();
        }
        catch(Exception e)
        {
            System.out.println("ERROR AT NEW INFO FILE CREATION");
            e.printStackTrace();
            System.exit(0);
        }
    }
    public static void main(String[] args)
    {
        String OUT_DIR = "";
        String InputSongDatFile = "";
        String InputInfoDatFile = "";
        String OutputSongDatFile = "Exported Song.txt";
        String OutputInfoDatFile = "Exported Info.txt";
        BeatMapNamer m = new BeatMapNamer(InputSongDatFile, OUT_DIR+OutputSongDatFile, InputInfoDatFile, OUT_DIR+OutputInfoDatFile);
        m.FormatSongDatFile();
        m.FormatInfoDatFile();
    }
}