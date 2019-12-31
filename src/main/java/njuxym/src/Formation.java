package njuxym.src;

public class Formation {
    public static void ChangShe(BattleField battleField,Camp<? extends Unit> camp) {
        if(camp.getLeader().toString().equals("爷爷")) {
            battleField.setUnit(camp.getLeader(), 0,0);
            for(int i = 1; i < 1+camp.getSoldiers().size(); i++)
                battleField.setUnit(camp.getSoldiers().get(i-1),i,0);
        }
        else {
            battleField.setUnit(camp.getLeader(), 0,19);
            for(int i = 1; i < 1+camp.getSoldiers().size(); i++)
                battleField.setUnit(camp.getSoldiers().get(i-1), i,19);
        }
    }

    public static void YanXing(BattleField battleField,Camp<?> camp) {
        if(camp.getLeader().toString().equals("爷爷")) {
            battleField.setUnit(camp.getLeader(), 0,0);
            for(int i = 0; i < camp.getSoldiers().size(); i++)
                battleField.setUnit(camp.getSoldiers().get(i), 7-i, i);
        }
        else {
            battleField.setUnit(camp.getLeader(), 7,15);
            battleField.setUnit(camp.getSoldiers().get(0), 7, 12);
            for(int i = 1;i<camp.getSoldiers().size();i++)
                battleField.setUnit(camp.getSoldiers().get(i), 7-i, 12+i);
        }
    }

    public static void HengE(BattleField battleField, Camp<?> camp) {
        if (camp.getLeader().toString().equals("爷爷")) {
            battleField.setUnit(camp.getLeader(), 0, 0);
            for (int i = 0; i < camp.getSoldiers().size(); i++)
                battleField.setUnit(camp.getSoldiers().get(i), 1 + i, 1 - (i % 2));
        }
        else {
            battleField.setUnit(camp.getLeader(), 0, 17);
            for (int i = 0; i < camp.getSoldiers().size(); i++)
                battleField.setUnit(camp.getSoldiers().get(i), 1 + i, 18 - (i % 2));
        }
    }

    public static void FengShi(BattleField battleField, Camp<?> camp) {
        if (camp.getLeader().toString().equals("爷爷")) {
            /*battleField.setUnit(camp.getLeader(), 0, 14);
            int tmp = -1;
            for (int i = 0; i < camp.getSoldiers().size(); i++) {
                if (i < 5) {
                    battleField.setUnit(camp.getSoldiers().get(i), 7 + tmp * ((i + 1) / 2), 10 + ((i + 1) / 2));
                    tmp = tmp * (-1);
                } else
                    battleField.setUnit(camp.getSoldiers().get(i), 7, 6 + i);
            }*/
        }
        else {
            battleField.setUnit(camp.getLeader(), 5, 19);
            battleField.setUnit(camp.getSoldiers().get(0), 5, 15);
            int tmp = -1;
            for (int i = 1; i < camp.getSoldiers().size(); i++) {
                if (i < 5) {
                    battleField.setUnit(camp.getSoldiers().get(i), 5 + tmp * ((i + 1) / 2), 15 + ((i + 1) / 2));
                    tmp = tmp * (-1);
                }
                else
                    battleField.setUnit(camp.getSoldiers().get(i), 5, 11+i);
            }
        }
    }
}
