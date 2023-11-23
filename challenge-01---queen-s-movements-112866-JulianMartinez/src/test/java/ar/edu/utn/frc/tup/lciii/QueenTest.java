package ar.edu.utn.frc.tup.lciii;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class QueenTest {

    private Queen queen = new Queen();

    @Test
    public void testCase1() {
        Integer n = 4;
        Integer k = 0;
        Integer r_q = 4;
        Integer c_q = 4;
        Integer expected = 9;
        Integer result = queen.queensAttack(n, k, r_q, c_q, null);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCase2() {
        Integer n = 5;
        Integer k = 3;
        Integer r_q = 4;
        Integer c_q = 3;
        List<List<Integer>> obstacles = Arrays.asList(Arrays.asList(5,5), Arrays.asList(4,2), Arrays.asList(2,3));
        Integer expected = 10;
        Integer result = queen.queensAttack(n, k, r_q, c_q, obstacles);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCase3() {
        Integer n = 1;
        Integer k = 0;
        Integer r_q = 1;
        Integer c_q = 1;
        Integer expected = 0;
        Integer result = queen.queensAttack(n, k, r_q, c_q, null);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCase4() {
        Integer n = 8;
        Integer k = 4;
        Integer r_q = 6;
        Integer c_q = 3;
        List<List<Integer>> obstacles = Arrays.asList(Arrays.asList(5,2), Arrays.asList(2,3), Arrays.asList(1,8), Arrays.asList(7,7));
        Integer expected = 20;
        Integer result = queen.queensAttack(n, k, r_q, c_q, obstacles);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCase5() {
        Integer n = 8;
        Integer k = 8;
        Integer r_q = 4;
        Integer c_q = 4;
        List<List<Integer>> obstacles = Arrays.asList(Arrays.asList(1,1), Arrays.asList(4,1), Arrays.asList(7,1), Arrays.asList(8,4),
                Arrays.asList(1,4), Arrays.asList(1,7), Arrays.asList(4,8), Arrays.asList(8,8));
        Integer expected = 19;
        Integer result = queen.queensAttack(n, k, r_q, c_q, obstacles);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCase6() {
        Integer n = 5;
        Integer k = 8;
        Integer r_q = 3;
        Integer c_q = 3;
        List<List<Integer>> obstacles = Arrays.asList(Arrays.asList(2,2), Arrays.asList(3,2), Arrays.asList(4,2), Arrays.asList(2,3),
                Arrays.asList(4,3), Arrays.asList(2,4), Arrays.asList(3,4), Arrays.asList(4,4));
        Integer expected = 0;
        Integer result = queen.queensAttack(n, k, r_q, c_q, obstacles);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCase7() {
        Integer n = 6;
        Integer k = 5;
        Integer r_q = 4;
        Integer c_q = 3;
        List<List<Integer>> obstacles = Arrays.asList(Arrays.asList(3,1), Arrays.asList(5,1), Arrays.asList(2,4), Arrays.asList(6,4),
                Arrays.asList(5,6));
        Integer expected = 19;
        Integer result = queen.queensAttack(n, k, r_q, c_q, obstacles);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testCase8() {
        Integer n = 6;
        Integer k = 0;
        Integer r_q = 4;
        Integer c_q = 3;
        Integer expected = 19;
        Integer result = queen.queensAttack(n, k, r_q, c_q, null);
        Assertions.assertEquals(expected, result);
    }
}
