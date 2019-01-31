import javafx.scene.Group;
import javafx.scene.Scene;

public class IntroScene extends Scene {
    Simulation mySimulation;

    public IntroScene(Group root, double width, double height, Simulation s){
        super(root,width,height);
        this.mySimulation = s;

    }
}
