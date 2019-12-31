package njuxym.src;

public class BattleField {
    private Plot[][] plots;
    private int row;
    private int col;

    public BattleField(int row, int col) {
        this.row = row;
        this.col = col;
        plots = new Plot[row][col];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++) {
                Plot temp = new Plot(i,j);
                plots[i][j] = temp;
            }
    }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public Plot[][] getPlots() { return plots; }
    public synchronized void setUnit(Unit unit, int x, int y) {
        //如果原位置单位与将要移动的单位相同，则将该位置置为空
        if (unit.getX() != -1 && plots[unit.getX()][unit.getY()].getUnit() == unit)
        { plots[unit.getX()][unit.getY()].setUnit(null); }

        plots[x][y].setUnit(unit);
        unit.set(x, y);
        unit.getView().setX(y*48);
        unit.getView().setY(x*40);
    }
}
