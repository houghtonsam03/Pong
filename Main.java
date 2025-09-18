import java.awt.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            throwable.printStackTrace(); // log error
            for (Frame f : Frame.getFrames()) {
                f.dispose();              // close all windows
            }
            System.exit(1);              // exit program
        });
        GameManager gm = new GameManager(Integer.parseInt(args[0]),Integer.parseInt(args[1]),(args[2].equals("1")),(args[3].equals("1")));
        Thread.sleep(3000);
        gm.Start();
    }
}
