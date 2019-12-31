package njuxym;

import static org.junit.Assert.assertEquals;

import njuxym.src.BattleField;
import njuxym.src.CalabashBrother;
import org.junit.Test;


public class MyTest
{
    private CalabashBrother[] cbs;
    private String[] CBs = new String[] {"大娃", "二娃", "三娃", "四娃", "五娃", "六娃", "七娃"};

    public MyTest() {
        cbs = new CalabashBrother[7];
        for(int i = 0;i<7;i++)
            cbs[i] = new CalabashBrother(null,null, CBs[i]);
    }

    @Test
    public void MonsterTest()throws Exception{
        for(int i = 0; i< cbs.length; i++)
            assertEquals(cbs[i].toString(), CBs[i]);
    }
}
