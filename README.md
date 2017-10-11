# Othello

![image](https://github.com/qiaofengmarco/JavaOthello/raw/master/image/d1.png)

![image](https://github.com/qiaofengmarco/JavaOthello/raw/master/image/d2.png)

![image](https://github.com/qiaofengmarco/JavaOthello/raw/master/image/d8.png)

![image](https://github.com/qiaofengmarco/JavaOthello/raw/master/image/d9.png)

## Dependencies: 
 + Google Guava version 23.0
 + JCuda 0.8.0 (Mainly JCublas...)
 
## Important Notes:
 + I didn't deploy my QLearningAI in the game, because I had encounted problems with NeuralNetwork.
 + To be honest, I don't know what is going wrong in my NeuralNetwork.java, NetworkLayer.java and VectorDNN.java.
 + However, my UCTAI and AlphaBeta are proved correct and the whole game frame is runnable.
 + Please contact me if you find something wrong, especially in the files that I mention above, thanks!

## 2017.9.14 Update:
 1. Added TrainFrame class.
 2. Added alpha-beta pruning Min-max AI, referring to：
 + http://www.cnblogs.com/pangxiaodong/archive/2011/05/26/2058864.html

## 2017.9.19 Update:
 1. Added NeuralNetwork class, referring to：
 + http://ufldl.stanford.edu/tutorial/supervised/MultiLayerNeuralNetworks/
 + http://ufldl.stanford.edu/wiki/index.php/%E5%8F%8D%E5%90%91%E4%BC%A0%E5%AF%BC%E7%AE%97%E6%B3%95
 + https://www.zybuluo.com/hanbingtao/note/476663
 + http://shuokay.com/2016/06/11/optimization/ 
 2. Added IOHandler class.

## 2017.9.21 Update:
 1. Corrected the mistakes in NeuralNetwork.java, rewrote the backward function.
 2. Finished QLearningAI.java and its debugging.
 + http://blog.csdn.net/songrotek/article/details/50917286
 
   ![image](https://github.com/qiaofengmarco/JavaOthello/raw/master/image/d3.png)
 
   ![image](https://github.com/qiaofengmarco/JavaOthello/raw/master/image/d4.png)
 
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
 
 ![image](https://github.com/qiaofengmarco/JavaOthello/raw/master/image/d5.png)
 
## 2017.9.30 Update:
 1. Converted previous version README.md to English version. (Please forgive me for my non-native expressions...)
 
## 2017.10.8 Update:
 1. Wrote vector-based DNN VectorDNN.java. (Haven't finished yet...)
 
    ![image](https://github.com/qiaofengmarco/JavaOthello/raw/master/image/d7.png) 
    
 2. Used JCublas API for matrix-related or vector-related computations. (VectorMatrix.java)
 + http://www.jcuda.org/jcuda/jcublas/doc/index.html
 + http://blog.csdn.net/cqupt0901/article/details/26393857
 + http://www.jcuda.org/jcuda/JCuda.html#Runtime
 + http://www.jcuda.org/jcuda/jcublas/JCublas.html
 + https://devtalk.nvidia.com/default/topic/1005058/cuda-programming-and-performance/jcuda-matrices-addition/
 3. Wrapped up Activator Functions. (Activator.java)

## 2017.10.9 Update:
 1. Improved GUI. 
 
    ![image](https://github.com/qiaofengmarco/JavaOthello/raw/master/image/d6.png)
 
 2. Normalized the format of codes.
 3. Fixed bug in VectorMatrix.java.
 
## 2017.10.10 Update:
 1. Improved GUI.
 
    ![image](https://github.com/qiaofengmarco/JavaOthello/raw/master/image/d8.png) 
    
    ![image](https://github.com/qiaofengmarco/JavaOthello/raw/master/image/d9.png)
    
## 2017.10.11 Update:
 1. Organized the files.
 2. To do next：fix VectorDNN...QAQ
 3. Important references:
 + https://aijunbai.github.io/publications/USTC07-Bai.pdf

**Modified on 2017.10.11.**
