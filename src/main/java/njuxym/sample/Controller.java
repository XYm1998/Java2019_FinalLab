package njuxym.sample;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import njuxym.src.*;

public class Controller {
    private Stage primaryStage = new Stage();
    public boolean isOnGame = false;
    public boolean isOnRecord = false;
    public HashMap<String, ArrayList<String>> history = new HashMap<>();
    public Pane pane = new Pane();
    public ImageView victory = new ImageView();
    public ImageView defeat = new ImageView();
    public ExecutorService GoodCampExecutor;
    public ExecutorService BadCampExecutor;

    public BattleField battleField = new BattleField(10,20);
    public Camp<GrandPa> goodCamp;
    public Camp<Snake> badCamp;
    public ArrayList<CalabashBrother> gourds;

    private String formation;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        gameInit();
        //初始阵型
        Formation.ChangShe(battleField, goodCamp);
        Formation.ChangShe(battleField, badCamp);
        formation = "CS";
    }

    @FXML
    public void StartTriggered(ActionEvent actionEvent) {
        isOnGame = true;
        System.out.println("开始游戏");
        ExecuteThread();
    }
    @FXML
    public void ResetTriggered(ActionEvent actionEvent) {
        System.out.println("重置游戏");
        gameInit();
        //初始阵型
        Formation.ChangShe(battleField, goodCamp);
        switch (formation){
            case "CS": CSTriggered(actionEvent); break;
            case "YX": YXTriggered(actionEvent); break;
            case "HE": HETriggered(actionEvent); break;
            case "FS": FSTriggered(actionEvent); break;
        }
    }
    @FXML
    public void SaveTriggered(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("保存存档");
        File file = fileChooser.showSaveDialog(primaryStage);

        if(file != null)
            try{
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "gbk"));
                bufferedWriter.write(formation + "\n");
                bufferedWriter.write(goodCamp.getLeader().toString()+":");
                for (String s:goodCamp.getLeader().getHistory()) {
                    bufferedWriter.write(s+" ");
                }
                bufferedWriter.write("\n");
                for (Unit c : goodCamp.getSoldiers()) {
                    bufferedWriter.write(c.toString()+":");
                    for (String s:c.getHistory()) {
                        bufferedWriter.write(s+" ");
                    }
                    bufferedWriter.write("\n");
                }
                bufferedWriter.write(badCamp.getLeader().toString()+":");
                for (String s:badCamp.getLeader().getHistory()) {
                    bufferedWriter.write(s+" ");
                }
                bufferedWriter.write("\n");
                for (Unit c : badCamp.getSoldiers()) {
                    bufferedWriter.write(c.toString()+":");
                    for (String s:c.getHistory()) {
                        bufferedWriter.write(s+" ");
                    }
                    bufferedWriter.write("\n");
                }
                bufferedWriter.close();
            }catch (Exception e){
                e.printStackTrace();
            }
    }
    @FXML
    public void LoadTriggered(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("打开存档");

        File file = fileChooser.showOpenDialog(primaryStage);
        history = new HashMap<>();
        if(file != null) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gbk"));
                formation = bufferedReader.readLine();

                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    String[] splitArray = str.split(":");
                    ArrayList<String> temp = new ArrayList<>(Arrays.asList(splitArray[1].split(" ")));
                    history.put(splitArray[0], temp);
                }
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("开始回放");

            gameInit();
            isOnRecord = true;
            Formation.ChangShe(battleField, goodCamp);
            switch (formation){
                case "CS": CSTriggered(actionEvent); break;
                case "YX": YXTriggered(actionEvent); break;
                case "HE": HETriggered(actionEvent); break;
                case "FS": FSTriggered(actionEvent); break;
            }
            ExecuteThread();
        }
    }

    private void gameInit() {
        isOnGame = false;
        isOnRecord = false;
        ObservableList list = pane.getChildren();
        list.clear();
        GoodCampExecutor= Executors.newCachedThreadPool();
        BadCampExecutor= Executors.newCachedThreadPool();
        //创建对象
        gourds = new ArrayList<>();
        CalabashBrother[] cbs = {
                new CalabashBrother(battleField, this, "大娃"),
                new CalabashBrother(battleField, this, "二娃"),
                new CalabashBrother(battleField, this, "三娃"),
                new CalabashBrother(battleField, this, "四娃"),
                new CalabashBrother(battleField, this, "五娃"),
                new CalabashBrother(battleField, this, "六娃"),
                new CalabashBrother(battleField, this, "七娃")
        };
        gourds.addAll(Arrays.asList(cbs));
        GrandPa grandpa = new GrandPa(battleField, this);
        goodCamp = new Camp<>(grandpa, cbs);
        Snake snake = new Snake(battleField, this);
        badCamp = new Camp<>(snake);
        Scorpion scorpion = new Scorpion(battleField, this);
        badCamp.addUnit(scorpion);
        Monster.num = 0;
        for(int i = 0; i < 7; i++) {
            Monster temp = new Monster(battleField, this);
            badCamp.addUnit(temp);
            list.add(temp.getView());
        }
        goodCamp.setNumber(1+gourds.size());
        badCamp.setNumber(1+badCamp.getSoldiers().size());
        //将图片添加到pane中
        for (CalabashBrother cb:gourds)
            list.add(cb.getView());
        list.add(grandpa.getView());
        list.add(snake.getView());
        list.add(scorpion.getView());
        victory.setImage(new Image("/img/win.png"));
        victory.setFitWidth(300);
        victory.setFitHeight(200);
        victory.setOpacity(0);
        defeat.setImage(new Image("/img/lose.png"));
        defeat.setFitWidth(300);
        defeat.setFitHeight(200);
        defeat.setOpacity(0);
        list.add(victory);
        list.add(defeat);
    }

    private void ExecuteThread() {
        //执行线程
        for (CalabashBrother cb: gourds)
            GoodCampExecutor.execute(cb);
        GoodCampExecutor.execute(goodCamp.getLeader());

        for (Unit tmp: badCamp.getSoldiers())
            BadCampExecutor.execute(tmp);
        BadCampExecutor.execute(badCamp.getLeader());

        GoodCampExecutor.shutdown();
        BadCampExecutor.shutdown();
    }

    public synchronized void GameOverDisplay(int result) {
        if(result == 0) {
            isOnGame = false;
            goodCamp.getLeader().setDoA(Unit.DoA.DEAD);
            for (Unit c:goodCamp.getSoldiers()) {
                c.setDoA(Unit.DoA.DEAD);
            }
            System.out.println("葫芦娃胜利");
            try {
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
            FadeTransition ft = new FadeTransition(Duration.millis(500), victory);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
        }

        else if(result == 1) {
            isOnGame = false;
            badCamp.getLeader().setDoA(Unit.DoA.DEAD);
            for (Unit c : badCamp.getSoldiers()) {
                c.setDoA(Unit.DoA.DEAD);
            }
            System.out.println("妖怪胜利");
            try {
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
            FadeTransition ft = new FadeTransition(Duration.millis(500), defeat);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
        }
    }

    public synchronized void MoveToDisplay(final Unit unit, final int x, final int y) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Timeline timeline = new Timeline();
                timeline.getKeyFrames().addAll(
                        new KeyFrame(Duration.millis(400),new KeyValue(unit.getView().xProperty(), 50*y)),
                        new KeyFrame(Duration.millis(400),new KeyValue(unit.getView().yProperty(), 50*x)));
                timeline.play();
                unit.getView().setX(y * 50);
                unit.getView().setY(x * 45);
            }
        });
    }

    public synchronized void beAttackedDisplay(final Unit unit) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Timeline timeline = new Timeline();
                timeline.setCycleCount(4);
                timeline.setAutoReverse(true);
                timeline.getKeyFrames().addAll(
                        new KeyFrame(Duration.millis(60), new KeyValue(unit.getView().xProperty(), unit.getY() * 45)),
                        new KeyFrame(Duration.millis(60), new KeyValue(unit.getView().xProperty(), unit.getY() * 45)));
                timeline.play();
            }
        });
    }

    public synchronized void DeadDisplay(final Unit unit) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                RotateTransition rt = new RotateTransition(Duration.millis(300), unit.getView());
                rt.setFromAngle(0);
                if (unit.getCamp() == 1)
                    rt.setToAngle(90);
                else
                    rt.setToAngle(-90);
                rt.play();

                FadeTransition ft = new FadeTransition(Duration.millis(300), unit.getView());
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                ft.play();
            }
        });
    }
    @FXML
    public void CSTriggered(ActionEvent actionEvent) {
        Formation.ChangShe(battleField, badCamp);
        formation = "CS";
    }
    @FXML
    public void YXTriggered(ActionEvent actionEvent) {
        Formation.YanXing(battleField, badCamp);
        formation = "YX";
    }
    @FXML
    public void HETriggered(ActionEvent actionEvent) {
        Formation.HengE(battleField, badCamp);
        formation = "HE";
    }
    @FXML
    public void FSTriggered(ActionEvent actionEvent) {
        Formation.FengShi(battleField, badCamp);
        formation = "FS";
    }
    @FXML
    public void HelpTriggered(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("操作说明");
        alert.setHeaderText(null);
        alert.setContentText("鼠标操作\n文件：\t保存或读取回放\n控制：\t开始或重置游戏\n阵型：\t为妖怪侧选择初始阵型");
        alert.showAndWait();
    }
}
