package njuxym.src;

import javafx.scene.image.Image;
import java.util.ArrayList;
import njuxym.sample.Controller;

public class GrandPa extends Unit{
    public GrandPa(BattleField battleField, Controller controller) {
        super(battleField, controller);
        try {
            Image image = new Image("/img/GrandPa.png");
            getView().setImage(image);
        }catch (Exception e){
            System.out.println(this+"：图片加载失败");
        }
        setCamp(0);
        livingProbability = 0.1;
        if((controller!=null) && !controller.history.isEmpty())
            history = new ArrayList<>(controller.history.get(this.toString()));
        else
            history = new ArrayList<>();
    }

    @Override
    public String toString(){ return "爷爷"; }
}
