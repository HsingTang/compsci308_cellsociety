import javafx.scene.Group;
import javafx.scene.Scene;

public class UI extends Scene {
    Simulation mySimulation;

    public UI(Group root, double width, double height, Simulation s){
        super(root,width,height);
        this.mySimulation = s;

    }


}
