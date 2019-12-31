package njuxym.src;

import java.util.ArrayList;
import java.util.Arrays;

public class Camp<T extends Unit> {
    private ArrayList<Unit>soldiers;
    private T leader;
    private int number;

    public Camp(T leader)
    {
        this.leader = leader;
        soldiers = new ArrayList<>();
    }
    public Camp(T leader, Unit[] units)
    {
        this.leader = leader;
        soldiers = new ArrayList<>();
        soldiers.addAll(Arrays.asList(units));
    }
    public void addUnit(Unit unit) { soldiers.add(unit); }
    public ArrayList<Unit> getSoldiers() { return soldiers; }
    public T getLeader() { return leader; }
    public void setNumber(int number) { this.number = number; }
    public int getNumber() { return number; }
    public void decNumber() { number--; }
}
