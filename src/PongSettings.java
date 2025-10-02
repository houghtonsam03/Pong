
public class PongSettings extends Settings {
    public PongSettings(String filename) {
        super(filename);
        System.out.println(this.properties.keySet());
    }
    public int GameAmount() {
        return (int)this.properties.get("games");
    }
    public int BallAmount() {
        return (int)this.properties.get("balls");
    }
    public boolean LeftIsAI() {
        return (boolean)this.properties.get("LAI");
    }
    public boolean RightIsAI() {
        return (boolean)this.properties.get("RAI");
    }
}
