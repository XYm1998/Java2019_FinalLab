package njuxym.src;

import javafx.scene.image.Image;
import java.util.ArrayList;
import njuxym.sample.Controller;

public class Monster extends Unit {
    public static int num = 0;
    private String name;

    public Monster(BattleField battleField, Controller controller){
        super(battleField, controller);
        setCamp(1);
        livingProbability = 0.3;
        name = "杂兵"+String.valueOf(num);
        num++;
        try {
            Image image = new Image("/img/Monster.png");
            getView().setImage(image);
        }catch (Exception e){
            System.out.println(this+"：图片加载失败");
        }
        if((controller!=null) && !controller.history.isEmpty())
            history = new ArrayList<>(controller.history.get(this.toString()));
        else
            history = new ArrayList<>();
    }

    @Override
    public String toString() { return name; }
}
