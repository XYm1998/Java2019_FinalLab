# “葫芦娃大战妖精”代码说明

## 运行方法
* 1、主目录下已有jar包。或使用mvn clean test packge在target目录生成jar包
* 2、replay文件在replay目录

## 设计思路
* 1、BattleField类构建战场，私有成员的类型为Plot[][]，每一个Plot对象表示场地上的一块区域。提供setUnit方法，将某生物体放置在某座标上<br>
* 2、Unit实现包括移动、攻击、记录在内的所有行为。GrandPa、CalabashBrother、Snake、Scorpion、Monster继承基类Unit<br>
* 3、GrandPa、CalabashBrother、Snake、Scorpion、Monster重写toString方法，使标识和获取对象名称更便捷<br>
* 4、Camp类的对象用于存储单个阵营的所有对象。在Camp类中使用泛型，以配合不同的leader成员。
```Java
public class Camp<T extends Unit> {
	private ArrayList<Unit> soldiers;
	private T leader;
	...
}
```
* 5、阵型变换统一交给Formation类处理，相关的成员函数均为public static void。<br>
* 6、Controller类实现用户交互、动画等方面的功能

## Class Diagram
![class_diagram](https://github.com/XYm1998/java-2019-homeworks/blob/master/4-Types/徐翊萌-161220150/img/Class_Diagram.png)

## 战斗过程介绍

|Unit|GrandPa|CalabashBrother|Snake|Scorpion|Monster|
|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|
|livingProbability|0.1|0.5|0.3|0.65|0.65|
* 1、使用鼠标进行操作<br>
* 2、开始前可选择打开存档，或为妖怪侧选择阵型<br>
* 3、结束后可通过“重置”将游戏画面还原至开始前。战斗记录若未手动保存，则在重置或关闭后清除<br>
* 4、战斗开始后，每个Unit各自移动或攻击，单次移动范围为1格，攻击范围为以自身为中心的3×3区域。每个Unit在被攻击时进行一次判定，若判定值大于存活率，则该Unit死亡<br>
* 5、Unit在发生移动、死亡等事件时均有动画展示。如死亡时会抖动、向后倒下（即向后旋转90°）并逐渐变透明。动画效果使战斗过程不突兀。

## 关于存档
* 使用txt格式存储，第一行为妖怪侧阵型，以下每行为1个Unit的行动记录。移动过程为“M + 目的坐标”，受攻击为“A”，被判定死亡为“D”

## 利用的机制与好处
* 封装：将不同的内容封装为多个类，隐藏内部实现，提高代码安全性、可读性。<br>
* 继承：GrandPa、CalabashBrother、Snake、Scorpion、Monster继承基类Unit，提高代码复用程度。使多个类之间产生联系，提高代码可读性和可维护性<br>
* 多态：Unit的子类重写了父类的方法，提高可扩充性和可维护性。<br>
* 多线程：每个Unit为一个线程，实现Runnable接口，每个阵营通过一个线程池进行管理.对于Unit的移动、攻击等共享数据的方法，使用synchronized加锁
```Java
	GoodCampExecutor= Executors.newCachedThreadPool();
    BadCampExecutor= Executors.newCachedThreadPool();
	
	public synchronized void beAttacked() {...}
```
* 异常处理：使用异常处理机制，保护代码的执行，且可捕获异常、定位错误的发生处
```Java
	try {
        Image image = new Image(s);
        getView().setImage(image);
    }catch (Exception e){
        System.out.println(this+"图片加载失败");
    }
```
* 集合类型：多处使用ArrayList、HashMap以便于编程
```Java
	public HashMap<String, ArrayList<String>> history = new HashMap<>();//全部Unit的历史记录
	protected ArrayList<String> history = new ArrayList<>();//单个Unit的历史记录
```
* 泛型：此项已在上文说明<br>
* 输入输出：使用装饰器模式
```Java
	BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "gbk"));//由于存档带有中文，故使用gbk编码
```
* 注解
** @FXML：标识与fxml控件相关联的方法<br>
** @Override：告知编译器，需要覆写父类的该方法<br>
** @Test：标识单元测试方法

## 结果展示
![res1](https://github.com/XYm1998/java-2019-homeworks/blob/master/3-OOPAdvanced/徐翊萌-161220150/img/res1.png)