package njuxym.src;

public class Plot {
    private Unit unit;
    private int x;
    private int y;

    Plot(int x, int y)//构造方法将该方块置空
    {
        this.x = x;
        this.y = y;
        unit = null;
    }
    public void setUnit(Unit unit) { this.unit = unit; }
    public Unit getUnit(){ return unit; }
    public boolean isEmpty(){ return (unit==null); }
}
