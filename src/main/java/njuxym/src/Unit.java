package njuxym.src;

import javafx.scene.image.ImageView;
import java.util.ArrayList;
import njuxym.sample.Controller;

abstract public class Unit implements Runnable{
    //信息传递属性
    final protected BattleField battleField;
    final protected Controller controller;
    //UI控制属性
    protected int x;
    protected int y;
    private ImageView view;
    //战斗状态属性
    protected DoA doa;
    protected BattleStatus battleStatus;
    protected int camp;
    protected double livingProbability;//判定时大于该值即死亡
    protected int paveNum;
    protected ArrayList<String> history = new ArrayList<>();

    public enum DoA{ALIVE, DEAD}
    public enum BattleStatus{BE_ATTACKED, OTHER}
    public enum Direction{LEFT,RIGHT,UP,DOWN}

    Unit(BattleField battleField, Controller controller) {
        this.battleField = battleField;
        this.controller = controller;
        this.x = -1;
        this.y = -1;
        view = new ImageView();
        view.setFitWidth(40);
        view.setFitHeight(48);
        doa = DoA.ALIVE;
        battleStatus = BattleStatus.OTHER;
        paveNum = 0;
    }

    public void set(int x,int y) {
        this.x = x;
        this.y = y;
    }
    public void setDoA(DoA x){ doa = x; }
    public DoA getDoA() { return doa; }
    public BattleStatus getBattleStatus() { return battleStatus; }
    public int getX(){ return x; }
    public int getY(){ return y; }
    public int getCamp(){return camp;}
    public void setCamp(int camp){this.camp = camp;}
    public ImageView getView(){return view;}
    public ArrayList<String> getHistory() { return history; }

    //移动回合
    public void moveProcess() {
        int dest_x = x, dest_y = y;
        ArrayList<Direction> towards = new ArrayList<>();
        towards.add(Direction.LEFT);
        towards.add(Direction.RIGHT);
        towards.add(Direction.UP);
        towards.add(Direction.DOWN);

        int tmp = (int)(Math.random()*towards.size());
        switch (towards.get(tmp)) {
            case LEFT: dest_y += Math.signum(9-y); break;
            case RIGHT: dest_y -= Math.signum(y-11); break;
            case UP: dest_x += Math.signum(3-x); break;
            case DOWN: dest_x -= Math.signum(x-7); break;
        }
        move(dest_x, dest_y);
    }

    public void executeProcess() {
        boolean state =  true;
        //防止多生物在同一位置
        synchronized (battleField) {
            for (int i = x - 1; i <= x + 1 && state; i++)
                for (int j = y - 1; j <= y + 1 && state; j++) {
                    if ((i >= 0) && (j >= 0) && (i < battleField.getRow()) && (j < battleField.getCol())
                            && !battleField.getPlots()[i][j].isEmpty()
                            && (battleField.getPlots()[i][j].getUnit().getCamp() != camp)) {
                        synchronized (battleField.getPlots()[i][j].getUnit()) {
                            if ((battleField.getPlots()[i][j].getUnit().getDoA() != DoA.DEAD)
                                    && (battleField.getPlots()[i][j].getUnit().getBattleStatus() != BattleStatus.BE_ATTACKED)) {
                                battleField.getPlots()[i][j].getUnit().beAttacked();
                                state = false;
                            }
                        }
                    }
                }
        }
    }

    public void run() {
        while (doa != DoA.DEAD) {
            if (controller.isOnGame) {
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (doa == DoA.DEAD)
                    break;
                if(battleStatus == BattleStatus.BE_ATTACKED)
                    battleStatus = BattleStatus.OTHER;
                moveProcess();
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (doa == DoA.DEAD)
                    break;
                executeProcess();
            }
            else if(controller.isOnRecord){
                //移动判定回合
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String s = history.get(paveNum);
                if(s.equals("M"))
                {
                    paveNum++;
                    recordMove();
                }
                if(paveNum >= history.size())
                    break;
                //操作判定回合
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                s = history.get(paveNum);
                if(s.equals("A"))
                    paveNum++;
                else if(s.equals("D"))
                {
                    paveNum++;
                    recordExecute();
                }
                if(paveNum >= history.size())
                    break;
            }
        }
    }

    //BasicAction
    public void move(int x, int y) {
        synchronized (battleField) {
            if (x >= battleField.getRow() || y >= battleField.getCol() || x < 0 || y < 0 || !battleField.getPlots()[x][y].isEmpty())
            {
                if (controller.isOnGame) {
                    history.add("M");
                    history.add(String.valueOf(this.x));
                    history.add(String.valueOf(this.y));
                }
                return;
            }
            battleField.getPlots()[this.x][this.y].setUnit(null);
            battleField.getPlots()[x][y].setUnit(this);
            this.set(x, y);

            if (controller.isOnGame) {
                history.add("M");
                history.add(String.valueOf(x));
                history.add(String.valueOf(y));
            }
            controller.MoveToDisplay(this, x, y);
        }
    }

    public synchronized void beAttacked() {
        //改为战斗状态，此时无法被再次攻击
        battleStatus = BattleStatus.BE_ATTACKED;
        if(Math.random() > livingProbability) {
            controller.beAttackedDisplay(this);//播放受击动画
            battleField.getPlots()[getX()][getY()].setUnit(null);
            setDoA(Unit.DoA.DEAD);
            controller.DeadDisplay(this);//播放死亡动画
            if (getCamp() == 0)
                controller.goodCamp.decNumber();
            else if (getCamp() == 1)
                controller.badCamp.decNumber();
            history.add("D");//死亡标记
        }
        history.add("A");//受击标记
        if (controller.goodCamp.getNumber() == 0)
            controller.GameOverDisplay(1);
        else if (controller.badCamp.getNumber() == 0)
            controller.GameOverDisplay(0);
    }

    //回放移动回合
    public void recordMove() {
        int x = Integer.parseInt(history.get(paveNum));
        int y = Integer.parseInt(history.get(paveNum + 1));
        paveNum += 2;
        if(x == this.x && y == this.y)
            return;
        battleField.getPlots()[this.x][this.y].setUnit(null);
        battleField.getPlots()[x][y].setUnit(this);
        this.set(x, y);
        controller.MoveToDisplay(this, x, y);
    }

    //回放操作回合
    public void recordExecute() {
        controller.beAttackedDisplay(this);
        battleField.getPlots()[this.x][this.y].setUnit(null);
        this.setDoA(DoA.DEAD);
        controller.DeadDisplay(this);//播放死亡动画

        if (getCamp() == 0)
            controller.goodCamp.decNumber();
        else if (getCamp() == 1)
            controller.badCamp.decNumber();
        if (controller.goodCamp.getNumber() == 0)
            controller.GameOverDisplay(1);
        else if (controller.badCamp.getNumber() == 0)
            controller.GameOverDisplay(0);
    }
}
