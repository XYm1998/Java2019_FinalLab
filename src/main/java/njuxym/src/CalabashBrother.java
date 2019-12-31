package njuxym.src;

import javafx.scene.image.Image;
import java.util.ArrayList;
import njuxym.sample.Controller;

public class CalabashBrother extends Unit{
    private String name;
    public CalabashBrother(BattleField battleField, Controller controller, String name) {
        super(battleField, controller);
        this.name = name;
        String s = "";
        switch (name){
            case "大娃": s = "/img/1.png";break;
            case "二娃": s = "/img/2.png";break;
            case "三娃": s = "/img/3.png";break;
            case "四娃": s = "/img/4.png";break;
            case "五娃": s = "/img/5.png";break;
            case "六娃": s = "/img/6.png";break;
            case "七娃": s = "/img/7.png";break;
        }
        try {
            Image image = new Image(s);
            getView().setImage(image);
        }catch (Exception e){
            System.out.println(this+"：图片加载失败");
        }

        setCamp(0);
        livingProbability = 0.5;
        if((controller!=null) && !controller.history.isEmpty())
            history = new ArrayList<>(controller.history.get(this.toString()));
        else
            history = new ArrayList<>();
    }

    @Override
    public String toString() { return name; }
}
