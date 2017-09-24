# Othello

![image](https://github.com/qiaofengmarco/JavaOthello/raw/master/d1.png)
![image](https://github.com/qiaofengmarco/JavaOthello/raw/master/d2.png)

## 2017.9.14更新：
 1.   新增TrainFrame 
 2.   新增alpha-beta剪枝优化的Min-max AI，参考：
    + http://www.cnblogs.com/pangxiaodong/archive/2011/05/26/2058864.html

## 2017.9.19更新:
 1.   新增NeuralNetwork，参考：
    + http://ufldl.stanford.edu/tutorial/supervised/MultiLayerNeuralNetworks/
    + https://www.zybuluo.com/hanbingtao/note/476663
    + http://shuokay.com/2016/06/11/optimization/ 
 2.   新增IOHandler

## 2017.9.21更新：
 1.   修复了之前NeuralNetwork不严谨的地方，重写了backward函数
 2.   完成了QLearningAI的代码以及初步调试
 3.   新增了很多static function作为辅助函数

## 2017.9.22更新：
 1.   再度修复之前NeuralNetwork不严谨的地方，加上了注释方便之后查验
 2.   经检查后重写了backward函数，更正了之前对minibatch-sgd的错误理解，参考：
    + http://blog.csdn.net/u014595019/article/details/52989301
 3.   调试完成了QLearningAI，对特殊情况进行了重写
 4.   在重写backward函数的同时更正了之前对minibatch的错误更新方法
 5.   修复了之前很多的小bug
 6.   修改了TrainFrame.java的名字 -> Playground.java

## 2017.9.23更新：
 1.   修复了很多AlphaBetaAI的奇怪bug...（Java函数传入的是对象的引用？好像是...）
 2.   修复了GUI和Playground的判断逻辑bug
 3.   调整了很多预设参数

## 2017.9.24更新：
 1.   初步完成基于UCT算法的AI，参考：
    + http://blog.csdn.net/Dinosoft/article/details/50893291
    + http://www.jianshu.com/p/d011baff6b64
    + http://mcts.ai/pubs/mcts-survey-master.pdf
 2.   调整了AlphaBetaAI的某些参数
 3.   修正Board.java的bug并加入了新的辅助函数
 4.   下一步：重新完善QLearningAI...？完善游戏UI...？还是就先放一放...？

**更新于2017.9.24**
