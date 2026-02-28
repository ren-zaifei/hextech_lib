# 模组介绍  Mod Description 
模组的灵感来源于LOL的海克斯大乱斗，最近刚好也在做整合包，所以就写了这个支持库。
不要选择1.0.2以下的版本！

This mod was inspired by League's ARAM, and since I was working on a modpack anyway, I ended up writing this library to support it.
Please do not choose any version below 1.0.2
## 它能做什么  What does it do
就像肉鸽游戏一样，当你完成某件事时，你可以在三个BUFF中选择一项来增强自己。

Just like in a roguelike game, upon completing a certain objective, you can choose one of three buffs to empower yourself.

## 关于指令 About Command

- /hextech  
    - common
    - rare
    - epic
    - legendary
    - list

本模组内置四种奖池，代表了海克斯的稀有度与强度，但并未内置任何海克斯。

This mod includes four built-in loot pools, which represent the rarity and power level of the Hextech enhancements, but it does not include any actual Hextech abilities itself.

## 如何使用 How to use it

如果你只是一个普通玩家，请忽视。

If you are just a regular player, you can ignore this.

### 1. 导入依赖  import dependencies
` implementation "io.github.ren-zaifei:hextech_lib:${Version}" `

Vsersion:[https://repo1.maven.org/maven2/io/github/ren-zaifei/hextech_lib/]

### 2. 继承HCard基类 Extend the HCard Base Class

```java 
public class exam extends HCard {
    
}
```

### 3. 重写方法  Override the Methods
```java 
//玩家选择时调用   Called when the player selects the card
@Override
public void applyEffect(Player player) {}

//玩家触发克隆事件时调用（玩家死亡重生等） Called when a player clone event occurs (e.g., player death and respawn)
@Override
public void reload(Player oldPlayer, Player newPlayer) {}

//玩家放弃海克斯时调用  Called when the player abandons the Hextech card
@Override
public void disconnect(Player player) {}
```

### 4. 创建实例   Create an Instance
```java
private static final List<HCard> cards = new ArrayList<>();

public static void registerCard() {
  createCards();
  if (cards.isEmpty()) return;
  for (HCard card : cards) {
    HCardPool.registerHCard(card);
  }
}
private static void createCards(){
  cards.add(new exam(
          ResourceLocation.fromNamespaceAndPath(xxx.MODID,"xxx"),
          xxx.MODID+".card."+HCard.Rarity.COMMON+".xxx."+"title",
          xxx.MODID+".card."+HCard.Rarity.COMMON+".xxx."+"description",
          HCard.Rarity.COMMON,
          Items.xxx.getDefaultInstance()
  ));
}
```
### 5. 在主类注册卡片进卡池 Register Cards in Your Main Class
```java
public HextechLib(IEventBus modEventBus) {
  modEventBus.addListener(this::commonSetup);
}
private void commonSetup(FMLCommonSetupEvent event) {
  event.enqueueWork(xxx::registerCard);
}
```