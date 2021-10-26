using UnityEngine;
public class Block
{
    private int LAYER, LANE, TYPE, CUT;
    private float TIME_START, TIME_END;
    private string PATH;
    private int COUNT;

    private Keyframe blockPosX, blockPosY, blockPosZ;
    private Keyframe blockRotX, blockRotY, blockRotZ;
    private Keyframe keyStartTime, keyEndTime;

    private static bool generated;


    public void setBlockPos(Keyframe x, Keyframe y, Keyframe z)
    {
        blockPosX = x;
        blockPosY = y;
        blockPosZ = z;
    }
    public void setBlockRot(Keyframe x, Keyframe y, Keyframe z)
    {
        blockRotX = x;
        blockRotY = y;
        blockRotZ = z;
    }
    public void setKeyFrameTimes(Keyframe start, Keyframe end)
    {
        keyStartTime = start;
        keyEndTime = end;
    }

    public Keyframe getBlockPosX()
    {
        return blockPosX;
    }
    public Keyframe getBlockPosY()
    {
        return blockPosY;
    }
    public Keyframe getBlockPosZ()
    {
        return blockPosZ;
    }

    public Keyframe getBlockRotX()
    {
        return blockRotX;
    }
    public Keyframe getBlockRotY()
    {
        return blockRotY;
    }
    public Keyframe getBlockRotZ()
    {
        return blockRotZ;
    }

    public Keyframe getKeyStartTime()
    {
        return keyStartTime;
    }
    public Keyframe getKeyEndTime()
    {
        return keyEndTime;
    }


    public void setCOUNT(int input)
    {
        COUNT = input;
    }
    public int getCOUNT()
    {
        return COUNT;
    }
    public void setEndTime(float timeEnd)
    {
        TIME_END = timeEnd;
    }
    public float getEndTime()
    {
        return TIME_END;
    }
    public void setStartTime(float timeStart)
    {
        TIME_START = timeStart;
    }
    public float getStartTime()
    {
        return TIME_START;
    }
    public void setLane(float lane)
    {
        LANE = (int)lane;
    }
    public int getLane()
    {
        return LANE;
    }
    public void setLayer(float layer)
    {
        LAYER = (int)layer;
    }
    public int getLayer()
    {
        return LAYER;
    }
    public void setCut(float cut)
    {
        CUT = (int)cut;
    }
    public int getCut()
    {
        return CUT;
    }
    public void setType(float type)
    {
        TYPE = (int)type;
    }
    public int getType()
    {
        return TYPE;
    }
    public void setPath(string path)
    {
        PATH = path;
    }
    public string getPath()
    {
        return PATH;
    }
}