import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            throwable.printStackTrace(); // log error
            for (Frame f : Frame.getFrames()) {
                f.dispose();              // close all windows
            }
            System.exit(1);              // exit program
        });
        System.out.println(args);
        new GameManager(Integer.parseInt(args[0]),Integer.parseInt(args[1]),(args[2]=="1"),(args[3]=="1"));
    }
}
