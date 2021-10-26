package gitrepo.pictureToVideoUnityVRC;

public class Bind
{
    private String NAME;
    private String GUID;
    private String BIND;
    public void setName(String in){
        NAME = in;
    }
    public void setGuid(String in){
        GUID = in;
    }
    public void setBind(String in){
        BIND = in;
    }
    public String getName(){
        return NAME;
    }
    public String getGuid(){
        return GUID;
    }
    public String getBind(){
        return BIND;
    }
}