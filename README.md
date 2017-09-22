# Othello

![image](https://github.com/qiaofengmarco/JavaOthello/raw/master/d1.png)
![image](https://github.com/qiaofengmarco/JavaOthello/raw/master/d2.png)

## 2017.9.14更新：
1. 新增TrainFrame
2. 新增alpha-beta剪枝优化的Min-max AI，参考：
   http://www.cnblogs.com/pangxiaodong/archive/2011/05/26/2058864.html

## 2017.9.19更新:
1. 新增NeuralNetwork，参考： 
   http://ufldl.stanford.edu/tutorial/supervised/MultiLayerNeuralNetworks/
   https://www.zybuluo.com/hanbingtao/note/476663
   http://shuokay.com/2016/06/11/optimization/
2. 新增IOHandler

## 2017.9.22更新：
1. 修复了之前NeuralNetwork不严谨的地方，重写了backward函数
2. 完成了QLearningAI的代码以及初步调试
3. 新增了很多static function作为辅助函数

## 2017.9.23更新：
1. 再度修复之前NeuralNetwork不严谨的地方，加上了注释方便之后查验
2. 调试完成了QLearningAI，对特殊情况进行了重写
3. 修复了之前很多的小bug
4. 修改了TrainFrame.java的名字 -> Playground.java
5. 下一步：编写QLearningAI的move函数，初步训练QLearningAI...

**更新于2017.9.23**
