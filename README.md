# Othello

![image](https://github.com/qiaofengmarco/JavaOthello/raw/master/d1.png)
![image](https://github.com/qiaofengmarco/JavaOthello/raw/master/d2.png)

## Dependencies: 
 + Google Guava version 23.0
 + JCublas 0.8.0 (comming soon...)

## 2017.9.14 Update:
 1. Added TrainFrame class.
 2. Added alpha-beta pruning Min-max AI, referring to：
 + http://www.cnblogs.com/pangxiaodong/archive/2011/05/26/2058864.html

## 2017.9.19 Update:
 1. Added NeuralNetwork class, referring to：
 + http://ufldl.stanford.edu/tutorial/supervised/MultiLayerNeuralNetworks/
 + https://www.zybuluo.com/hanbingtao/note/476663
 + http://shuokay.com/2016/06/11/optimization/ 
 2. Added IOHandler class.

## 2017.9.21 Update:
 1. Corrected the mistakes in NeuralNetwork.java, rewrote the backward function.
 2. Finished QLearningAI.java and its debugging.
 3. Added many static functions in Board.java as helper function.

## 2017.9.22 Update:
 1. Recorrected the mistakes in NeuralNetwork.java, also attaching some comments.
 2. Rewrote backward function, corrected the previous misunderstanding of minibatch-sgd, referring to：
 + http://blog.csdn.net/u014595019/article/details/52989301
 3. Finished debugging QLearningAI and rewrote some special cases.
 4. Corrected the wrong method for updating minibatches.
 5. Fixed some small bugs.
 6. Changed name: TrainFrame.java -> Playground.java

## 2017.9.23 Update:
 1. Fixed numerous misunderstandable bugs in AlphaBetaAI... (Java function passes the reference of an object input?)
 2. Fixed bugs of state-determining logics in GUI.java and Playground.java.
 3. Adjusted some default parameters.

## 2017.9.24 Update:
 1. Finished UCT AI, referring to：
 + http://blog.csdn.net/Dinosoft/article/details/50893291
 + http://www.jianshu.com/p/d011baff6b64
 + http://mcts.ai/pubs/mcts-survey-master.pdf
 2. Adjusted some parameters in AlphaBetaAI.java.
 3. Fixed bugs in Board.java and added new static functions as helper functions.
 
## 2017.9.30 Update:
 1. Converted previous version README.md into English version. (Please forgive me for my non-native expressions...) 
 2. To do next：perfect the QLearningAI, apply GPU acceleration in NeuralNetwork.java...
 3. Important references:
 + https://aijunbai.github.io/publications/USTC07-Bai.pdf
 + http://www.jcuda.org/jcuda/jcublas/doc/index.html

**Finished on 2017.9.30.**
