package ar.edu.utn.frc.tup.lciii;

import javax.xml.transform.Result;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Hello Queen's Attack!
 *
 */
public class App 
{

    /**
     * This is the main program
     * 
     */
    public static void main( String[] args ) {
        Queen queen = new Queen();
        /* IMPORTANT: Do not change this code */
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Queen's Attack!");
        System.out.println("Chessboard size:");
        Integer n = scanner.nextInt();
        System.out.println("Number of obstacles:");
        Integer k = scanner.nextInt();
        System.out.println("Queen start position:");
        System.out.println("Row:");
        Integer r_q = scanner.nextInt();
        System.out.println("Column:");
        Integer c_q = scanner.nextInt();

        List<List<Integer>> obstacles = new ArrayList<>();
        for(int i = 0; i < k; i++) {
            List<Integer> point = new ArrayList<>();
            System.out.println("Obstacle " + (i+1) + ":");
            System.out.println("Row:");
            point.add(scanner.nextInt());
            System.out.println("Column:");
            point.add(scanner.nextInt());
            obstacles.add(point);
        }
        System.out.println("Result:");
        queen.queensAttack(n, k, r_q, c_q, obstacles);
    }
}
