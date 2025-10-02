import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        
        GameManager gm = new GameManager();
        
        System.out.println("Press Enter to start the game...");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        sc.close();

        gm.Setup();
        gm.Start();
    }
}
