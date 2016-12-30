package sudoku;

/**
 * Created by pica8 on 2016/12/30.
 */
public class Test {
    public static void main(String[] args) {
		Test test = new Test();
		test.computeByNew();
    }

    public void computeByNew(){
        int[][] datas ={{0,0,0,0,0,6,0,7,8},
                {4,0,0,0,7,0,2,5,0},
                {0,2,8,1,0,0,0,0,0},
                {2,4,0,5,8,0,3,0,0},
                {0,0,0,6,0,3,0,0,0},
                {0,0,3,0,4,2,0,6,5},
                {0,0,0,0,0,1,5,8,0},
                {0,8,1,0,5,0,0,0,6},
                {6,5,0,8,0,0,0,0,0}};
        Sudoku sudoku = new Sudoku(datas);
        int[][] redatas = sudoku.compute();
    }

}
