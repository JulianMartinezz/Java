package ar.edu.utn.frc.tup.lciii;

import com.sun.jdi.Value;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.StreamSupport;

public class Queen {

    /*
     * Complete the 'queensAttack' function below.
     *
     * The function is expected to return an INTEGER.
     * The function accepts following parameters:
     *  1. INTEGER n
     *  2. INTEGER k
     *  3. INTEGER r_q
     *  4. INTEGER c_q
     *  5. 2D_INTEGER_ARRAY obstacles
     */
    public Integer queensAttack(Integer n, Integer k, Integer r_q, Integer c_q, List<List<Integer>> obstacles) {
        // Write your code here
        int contador=0;
        Integer r=r_q-1;
        Integer c=c_q-1;
        Integer max=n-1;
        int[][] matriz =new int[n][n];
        for(int i=0;i<matriz.length;i++) matriz[i][i]=0;
        if(obstacles!=null) {
            for (int i = 0; i <= k-1; i++){
                int row=obstacles.get(i).get(0);
                int col=obstacles.get(i).get(1);
                matriz[row-1][col-1]=2;
            }
        }
        matriz[r] [c]=1; //Carga de la reina
        contador+=ContarMovimientos(matriz,max,r,c,"Arriba");
        contador+=ContarMovimientos(matriz,max,r,c,"Abajo");
        contador+=ContarMovimientos(matriz,max,r,c,"Derecha");
        contador+=ContarMovimientos(matriz,max,r,c,"Izquierda");
        contador+=ContarMovimientos(matriz,max,r,c,"Diagonal derecha");
        contador+=ContarMovimientos(matriz,max,r,c,"Diagonal izquierda");
        return contador;
    }
    private int ContarMovimientos(int [][] matriz,Integer max,Integer r,Integer c,String direccion) {
        int contador=0;
        switch (direccion) {
            case "Arriba":
                for (int i = 1; i <= max; i++) {
                    if (r + i > max) break;
                    if (matriz[r + i][c] == 0) contador++;
                    else if (matriz[r + i][c] == 2) {
                        break;
                    }
                }
                break;
            case "Abajo":
                for (int i = 1; i <= max; i++) {
                    if (r - i < 0) break;
                    if (matriz[r - i][c] == 0) contador++;
                    else if (matriz[r - i][c] == 2) {
                        break;
                    }
                }
                break;
            case "Derecha":
                for (int i = 1; i <= max; i++) {
                    if (c + i > max) break;
                    if (matriz[r][c + i] == 0) contador++;
                    else if (matriz[r][c + i] == 2) {
                        break;
                    }
                }
                break;
            case "Izquierda":
                for (int i = 1; i <= max; i++) {
                    if (c - i < 0) break;
                    if (matriz[r][c - i] == 0) contador++;
                    else if (matriz[r][c - i] == 2) break;
                }
                break;
            case "Diagonal derecha":
                for (int i = 1; i <= max; i++) {
                    if (c + i > max) break;
                    if (matriz[r - i][c + i] == 0) contador++;
                    else if (matriz[r - i][c + i] == 2) break;
                }
                for (int i = 1; i <= max; i++) {
                    if (r + i > max || c + i > max) break;
                    if (matriz[r + i][c + i] == 0) contador++;
                    else if (matriz[r + i][c + i] == 2) break;
                }
                break;
            case "Diagonal izquierda":
                for (int i = 1; i <= max; i++) {
                    if (r - i < 0 || c - i < 0) break;
                    if (matriz[r - i][c - i] == 0) contador++;
                    else if (matriz[r - i][c - i] == 2) break;
                }
                for (int i = 1; i <= max; i++) {
                    if (r + i > max || c - i < 0) break;
                    if (matriz[r + i][c - i] == 0) contador++;
                    else if (matriz[r + i][c - i] == 2) break;
                }
                break;
        }
        return contador;
    }
}
