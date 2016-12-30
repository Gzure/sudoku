package sudoku;

/**
 * Created by pica8 on 2016/12/28.
 */
public class Grid {
    private int[][] data = new int[3][3];

    public  Grid(int[][] data){
        this.data = data;
    }

    public boolean contains(int v){
        for(int m=0;m<3;m++){
            for(int n=0;n<3;n++){
                if(data[m][n]==v)
                    return true;
            }
        }
        return false;
    }

    public void setDataOne(int m,int n,int v){
        data[m][n] =v;
    }

    public Grid copy(){
        int[][] copyData = new int[3][3];
        for(int i=0;i<3;i++){
            copyData[i] = data[i].clone();
        }
        return new Grid(copyData);
    }
}
