public class Agent {

    private GameManager manager;
    private float[][] states;
    private int ballAmount;
    private boolean left;
    Agent(boolean l,GameManager man,int games,int bAmount) {
        manager = man;
        states = new float[games][2+2*bAmount];
        ballAmount = bAmount;
        left = l;
    }
    public void Update(int id,float[] state) {
        states[id] = state;
        manager.Listen(id, left, Math.random()>0.5); 
    }
    
}
