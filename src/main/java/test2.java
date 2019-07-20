
public class test2{
    public static void main(String[] args) {
        Thread t1 = new Thread(new steve ("D:/downloads/audioSample1.wav"));
        t1.start();

        Thread t2 = new Thread(new steve ("D:/downloads/audioSample3.wav"));
        t2.start();
    }
}

