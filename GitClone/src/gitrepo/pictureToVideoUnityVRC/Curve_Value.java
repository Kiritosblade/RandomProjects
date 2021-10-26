package gitrepo.pictureToVideoUnityVRC;

public class Curve_Value {
    private String NAME;
    private String GUID;
    private String VALUE;
    public void setName(String in){
        NAME = in;
    }
    public void setGuid(String in){
        GUID = in;
    }
    public void setValue(String in){
        VALUE = in;
    }
    public String getName(){
        return NAME;
    }
    public String getGuid(){
        return GUID;
    }
    public String getValue(){
        return VALUE;
    }
}