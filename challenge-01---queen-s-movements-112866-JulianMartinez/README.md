[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/ybN7Uo3I)
# Challenge "Queen's Movements"

You will be given a square chess board with one queen and a number of 
obstacles placed on it. Determine how many squares the queen can attack.

A [queen](https://en.wikipedia.org/wiki/Queen_%28chess%29) is standing on 
an **_n x n_** [chessboard](https://en.wikipedia.org/wiki/Chess). 
The chess board's rows are numbered from **_1_** to **_n_**, going from 
bottom to top. Its columns are numbered from **_1_** to **_n_**, going from 
left to right. Each square is referenced by a tuple **_(r, c)_**, describing 
the row **_r_** and column **_c_**, where the square is located.

The queen is standing at position **_(r<sub>q</sub>, c<sub>q</sub>)_**. 
In a single move, she can attack any square in any of the eight directions 
(left, right, up, down, and the four diagonals). In the diagram below, 
the green circles denote all the cells the queen can attack from **_(4, 4)_**:

![img](/docs/_images/img.png)

There are obstacles on the chessboard, each preventing the queen from 
attacking any square beyond it on that path. For example, an obstacle at 
location **_(3, 5)_** in the diagram above prevents the queen from attacking 
cells **_(3, 5)_**, **_(2, 6)_** and **_(1, 7)_**:

![img](/docs/_images/img_1.png)

Given the queen's position and the locations of all the obstacles, find and 
print the number of squares the queen can attack from her position 
at **_(r<sub>q</sub>, c<sub>q</sub>)_**. In the board above, 
there are **_24_** such squares.

## Funtion Description

Complete the queensAttack function in the editor below.

queensAttack has the following parameters:
- int **_n_**: the number of rows and columns in the board
- nt **_k_**: the number of obstacles on the board
- int **_r_q_**: the row number of the queen's position
- int **_c_q_**: the column number of the queen's position
- int obstacles **_[k][2]_**: each element is an array of **_2_** integers, the row and column of an obstacle

## Returns

- int: the number of squares the queen can attack

## Input Format

The first line contains two space-separated integers **_n_** and **_k_**, 
the length of the board's sides and the number of obstacles.
The next line contains two space-separated integers **_r<sub>q</sub>_** and **_c<sub>q</sub>_**, 
the queen's row and column position.
Each of the next **_k_** lines contains two space-separated integers **_r[i]_** and **_c[i]_**, 
the row and column position of **_obstacle[i]_**.

## Constrains

![img](/docs/_images/img_2.png)

## Subtasks

![img](/docs/_images/img_3.png)

## Sample Input 0

```
Welcome to Queens Attack!
Chessboard size:
4
Number of obstacles:
0
Queen start position:
Row:
4
Column:
4
```

## Sample Output 0

```
Result:
9
```

## Explanation 0

The queen is standing at position **_(4, 4)_** on a **_4 x 4_** chessboard with no obstacles:

![img](/docs/_images/img_4.png)

The number of squares she can attack from that position is **_9_**.

## Sample Input 1

```
Welcome to Queen's Attack!
Chessboard size:
5
Number of obstacles:
3
Queen start position:
Row:
4
Column:
3
Obstacle 1:
Row:
5
Column:
5
Obstacle 2:
Row:
4
Column:
2
Obstacle 3:
Row:
2
Column:
3
```

## Sample Output 1

```
Result:
10
```

## Explanation 1

The queen is standing at position **_(4, 3)_** on a **_5 x 5_** chessboard with **_k = 3_** obstacles:

![img](/docs/_images/img_5.png)

The number of squares she can attack from that position is **_10_**.

## Sample Input 2

```
Welcome to Queen's Attack!
Chessboard size:
1
Number of obstacles:
0
Queen start position:
Row:
1
Column:
1
```

## Sample Output 2

```
Result:
0
```

## Explanation 2

Since there is only one square, and the queen is on it, the queen can move **_0_** squares.