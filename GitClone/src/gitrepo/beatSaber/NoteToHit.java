package gitrepo.beatSaber;

public class NoteToHit
{
    private int _lineIndex, _lineLayer, _type, _cutDirection;
    private int _time, _keyFrame, _keyFrameEnd;
    private String temp, temp2;
    private int tempX;
    int fps = 8;
    int sec = 232;
    
    //Timing of note placement
    public void setTime(String in)
    {
        int start = in.indexOf(":");
        
        temp = in.substring(start+1);
        temp = temp.replaceAll(",", "");
        int end = temp.indexOf(".")+4;
        String fullNum = temp.substring(0,end-4);
        String rawNum = temp.substring(0,end-4);
        int second = Integer.parseInt(rawNum);
        

        
        if(temp.contains("."))
        {
            
            int st = temp.indexOf(".")+1;
            int nd = temp.indexOf(".")+4;
            String k = temp.substring(st, nd);
            int tem = Integer.parseInt(k);
            
            if(tem >= 0 && tem <= 125)
            {
                // key 0
                tempX = Integer.parseInt(fullNum);
                tempX = (tempX*fps);
                _time = tempX;
            }else if(tem >= 125 && tem <= 250)
            {
                // key 1
                tempX = Integer.parseInt(fullNum);
                tempX = (tempX*fps)+1;
                _time = tempX;
            }else if(tem >= 250 && tem <= 375)
            {
                // key 2
                tempX = Integer.parseInt(fullNum);
                tempX = (tempX*fps)+2;
                _time = tempX;
            }else if(tem >= 375 && tem <= 500)
            {
                // key 3
                tempX = Integer.parseInt(fullNum);
                tempX = (tempX*fps)+3;
                _time = tempX;
            }else if(tem >= 500 && tem <= 625)
            {
                // key 4
                tempX = Integer.parseInt(fullNum);
                tempX = (tempX*fps)+4;
                _time = tempX;
            }else if(tem >= 625 && tem <= 750)
            {
                // key 5
                tempX = Integer.parseInt(fullNum);
                tempX = (tempX*fps)+5;
                _time = tempX;
            }else if(tem >= 750 && tem <= 875)
            {
                // key 6
                tempX = Integer.parseInt(fullNum);
                tempX = (tempX*fps)+6;
                _time = tempX;
            }else if(tem >= 875 && tem < 1000)
            {
                // key 7
                tempX = Integer.parseInt(fullNum);
                tempX = (tempX*fps)+7;
                _time = tempX;
            }
        }else if(!temp.contains(".")){
            _time = Integer.parseInt(temp);
        }
        setKeyFrame();
    }
    public int getTime()
    {
        return _time;
    }
    
    //Lane: 0 = far left, 1 = mid left, 2 = mid right, 3 = far right
    public void setLane(String in)
    {
        int start = in.indexOf(":");
        temp = in.substring(start+1);
        temp = temp.replaceAll(",", "");
        _lineIndex = Integer.parseInt(temp);
    }
    public int getLane()
    {
        return _lineIndex;
    }
    
    //Height 0 = bottom, 1 = mid, 2 = top
    public void setLayer(String in)
    {
        int start = in.indexOf(":");
        temp = in.substring(start+1);
        temp = temp.replaceAll(",", "");
        _lineLayer = Integer.parseInt(temp);
    }
    public int getLayer()
    {
        return _lineLayer;
    }
    
    //Type: 0 = Left Hand(Red), 1 = Right Hand(Blue), 2 = ?, 3 = Bomb
    public void setType(String in)
    {
        int start = in.indexOf(":");
        temp = in.substring(start+1);
        temp = temp.replaceAll(",", "");
        _type = Integer.parseInt(temp);
    }
    public int getType()
    {
        return _type;
    }
    
    //Cut Direction: 0 = Up, 1 = Down, 2 = ?, 3 = ?, 4 = Up Left, 5 = Up Right, 6 = Down Left, 7 = Down Right, 8 = Dot (Any)
    public void setCutDirection(String in)
    {
        int start = in.indexOf(":");
        temp = in.substring(start+1);
        temp = temp.replaceAll(",", "");
        temp = temp.replaceAll("}","");
        _cutDirection = Integer.parseInt(temp);
    }
    public int getCutDirection()
    {
        return _cutDirection;
    }
    
    private void setKeyFrame()
    {
        _keyFrame = _time;
        setKeyFrameEnd();
    }
    public int getKeyFrame()
    {
        return _keyFrame;
    }
    
    private void setKeyFrameEnd()
    {
        _keyFrameEnd = _keyFrame + 16;
    }
    public int getKeyFrameEnd()
    {
        return _keyFrameEnd;
    }
}