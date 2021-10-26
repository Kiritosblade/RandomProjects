package gitrepo.beatSaber;

public class bpmFinder
{
    public static void main(String[] args)
    {
        double lengthOfSong = 3.2;
        int bpm = 132; // the bpm of song
        int sec = 60; // 60s in 1 min
        int fps = 60; // the fps or refresh rate
        double fpm; // frames per minute
        double fpb; // frames per beat
        double kps; // keyframes per second
        
        fpm = sec*fps;
        kps = fpm/sec;
        fpb = fpm/bpm;
        double ext = fpm*lengthOfSong;
        System.out.println("Length of song in min/sec: " + lengthOfSong);
        System.out.println("Frames Total: " + ext);
        System.out.println("Frames per Min: "+fpm);
        System.out.println("Keys per Sec: "+kps);
        System.out.println("Frames per Beat: "+fpb);
    }
}