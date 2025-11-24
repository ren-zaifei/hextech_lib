# 模组介绍  Mod Description 
模组的灵感来源于LOL的海克斯大乱斗，最近刚好也在做整合包，所以就写了这个支持库。

This mod was inspired by League's ARAM, and since I was working on a modpack anyway, I ended up writing this library to support it.

## 它能做什么  What does it do
就像肉鸽游戏一样，当你完成某件事时，你可以在三个BUFF中选择一项来增强自己。

Just like in a roguelike game, upon completing a certain objective, you can choose one of three buffs to empower yourself.

## 关于指令 About Command

- /hextech  
    - common
    - rare
    - epic
    - legendary

本模组内置四种奖池，代表了海克斯的稀有度与强度，但并未内置任何海克斯。

This mod includes four built-in loot pools, which represent the rarity and power level of the Hextech enhancements, but it does not include any actual Hextech abilities itself.

## 如何使用 How to use it

如果你只是一个普通玩家，请忽视。

If you are just a regular player, you can ignore this.

### 1. 导入依赖  import dependencies
` implementation "io.github.ren-zaifei:hextech_lib:${Version}" `

Vsersion:[https://repo1.maven.org/maven2/io/github/ren-zaifei/hextech_lib/]

### 2. 创建一个海克斯 Create a Hextech

```java 
HCard card = new HCard(
    ResourceLocation.fromNamespaceAn("your_namespace","path")，//ID
    "namespace.card.title",//TitleKey 翻译时的标题键
    "namespace.card.description",//DescKey 翻译时的内容键
    HCard.Rarity.COMMON,//Rarity 可选择的品质
    Item.XXX.getDefaultInstance(),//Icon 可传入一个ItemStack作为图标
    p -> {}//Event 此处为一个Player，可进行操作
)

HexCardRegistry.register(card,rarity)//此处的rarity为HCard.Rarity的一个枚举  rarity is an enum of HCard.Rarity

```

### 3. 调用方法  Choose a Hextech
```java 
HexTriggers.selection(player,rarity)
//player -> Serverplayer
//rarity -> HCard.Rarity
```