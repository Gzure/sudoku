package sudoku;

/**
 * Created by pica8 on 2016/12/28.
 */
public class Sudoku {

    private int[][] datas = new int[9][9];
    private Grid[][] grids = new Grid[3][3];
    //private int[][] hline = new int[9][9];
    private int[][] vline = new int[9][9];

    public Sudoku(int[][] datas){
        this.datas = datas;
        resetLineAndGrid();
    }

    private void initGrids(){
        for(int m=0;m<3;m++){
            for(int n=0;n<3;n++){
                grids[m][n] = initOneGrid(m,n);
            }
        }
    }

    private Grid initOneGrid(int x,int y){
        int realX = x*3;
        int realY = y*3;
        int[][] griData = new int[3][3];
        for(int m=0;m<3;m++){
            for(int n=0;n<3;n++){
                griData[m][n] = datas[realX+m][realY+n];
            }
        }
        return new Grid(griData);
    }

    private void initVline(){
        for(int m=0;m<9;m++){
            for(int n=0;n<9;n++){
                vline[m][n] = datas[n][m];
            }
        }
    }

    private void resetLineAndGrid(){
        initVline();
        initGrids();
    }

    public int[][] compute(){
        int count = 1;
        while(count>0){
            count = updateGrid();
            System.out.println("fill nums:"+count);
        }
        if(vertify(datas))
            return datas;

        System.out.println("fill complete data:");
        printData(datas);

        exhaustive(0,datas,grids,vline);
        return datas;
    }

    private boolean exhaustive(int n,int[][] datas,Grid[][] grids,int[][] vline){
        int h = n/9;
        int l= n%9;

        if(datas[h][l] == 0) {
            int[][] tmpdatas = copyArrays(datas);
            Grid[][] tmpgrids = copyGrid(grids);
            int[][] tmpvline = copyArrays(vline);
            int[][][] mayDatas = fill(tmpdatas,tmpgrids,tmpvline);
            if(isMayDataNull(mayDatas[h][l])) return false;
            for(int i=1;i<10;i++){
                if(mayDatas[h][l][i] > 0){
                    tmpdatas[h][l] = mayDatas[h][l][i];
                    tmpgrids[h/3][l/3].setDataOne(h%3,h%3,mayDatas[h][l][i]);
                    tmpvline[l][h] = mayDatas[h][l][i];
                    if(n<80) {
                        boolean iscontinue = exhaustive(n + 1, tmpdatas, tmpgrids, tmpvline);
                        if(iscontinue) return true;
                    }else{
                        if(vertify(tmpdatas)){
                            System.out.println("all complete data:");
                            printData(tmpdatas);
                            this.datas = tmpdatas;
                            return true;
                        }
                        //printData(tmpdatas);
                        return false;
                    }
                }
            }
            return false;
        }

        if(n==80){
            if(vertify(datas)){
                printData(datas);
                this.datas = datas;
                return true;
            }
            printData(datas);
            return false;
        }

        return exhaustive(n + 1, datas, grids, vline);
    }

    private int[][] copyArrays(int[][] oo){
        int rr[][] =new int[oo.length][oo[0].length];
        for(int i=0;i<oo.length;i++){
            rr[i] = oo[i].clone();
        }
        return rr;
    }

    private Grid[][] copyGrid(Grid[][] grids){
        Grid[][] regrids = new Grid[3][3];
        for(int i=0;i<grids.length;i++){
            for(int j=0;j<grids[i].length;j++){
                Grid grid = grids[i][j].copy();
                regrids[i][j] = grid;
            }
        }
        return regrids;
     }

    private void printData(int[][] datas){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                System.out.print("  "+datas[i][j]);
            }
            System.out.print("\n");
        }
    }

    private boolean isMayDataNull(int[] array){
        for(int i=0;i<array.length;i++){
            if(array[i]>0) return false;
        }
        return true;
    }

    private int updateGrid(){
        int addNums =0;
        //add nums to mayDatas
        int[][][] mayDatas = fill(datas,grids,vline);
        addNums+=fillByOnlyOneInBlock(mayDatas,"H");
        resetLineAndGrid();
        mayDatas = fill(datas,grids,vline);
        addNums+=fillByOnlyOneInBlock(mayDatas,"G");
        resetLineAndGrid();
        mayDatas = fill(datas,grids,vline);
        addNums+=fillByOnlyOneInBlock(mayDatas,"V");
        resetLineAndGrid();
        return addNums;
    }

    private int[][][] fill(int[][] ninegrid,Grid[][] grids,int[][] vline){
        int[][][]  mayDatas = new int[9][9][10];
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                for(int num=1;num<10;num++){
                    if(ifexit(ninegrid[i],num))
                        continue;
                    if(ifexit(vline[j],num))
                        continue;
                    if(!grids[i/3][j/3].contains(num))
                        mayDatas[i][j][num] = num;
                }

            }
        }
        return mayDatas;
    }

    private int  fillByOnlyOneInBlock(int[][][] mayDatas,String type){
        int addNums=0;
        for(int i=0;i<9;i++){
            int[] ps = new int[10];
            int[] count = new int[10];
            //one block
            for(int j=0;j<9;j++){
                int[] tmp;

                if(type.equals("V"))
                    tmp=mayDatas[j][i];
                else if(type.equals("G"))
                    tmp=mayDatas[i/3*3+j/3][i%3*3+j%3];
                else
                    tmp=mayDatas[i][j];

                for(int k=1;k<tmp.length;k++){
                    if(tmp[k]>0){
                        count[k]++;
                        if(count[k]==1)
                            ps[k] = j;
                        else
                            ps[k] = 0;
                    }
                }
            }

            for(int num=1;num<10;num++){
                if(ps[num]>0){
                    if(type.equals("V"))
                        datas[ps[num]][i] = num;
                    else if(type.equals("G"))
                        datas[i/3*3+ps[num]/3][i%3*3+ps[num]%3] = num;
                    else
                        datas[i][ps[num]] = num;
                    addNums++;
                }
            }
        }
        return addNums;
    }

    private boolean ifexit(int[] nums,int num){
        for(int i=0;i<nums.length;i++){
            if(nums[i]==num) return true;
        }
        return false;
    }

    private boolean vertify(int[][] redatas){
        for(int i=0;i<9;i++){
            int[] count = new int[10];
            int[] countV = new int[10];
            int[] countG = new int[10];
            //vertify H
            for(int j=0;j<9;j++){
                if(redatas[i][j]==0 || redatas[j][i]==0 || redatas[i/3*3+j/3][i%3*3+j%3]==0) return false;
                if(++count[redatas[i][j]]>1 || ++countV[redatas[j][i]]>1 || ++countG[redatas[i/3*3+j/3][i%3*3+j%3]]>1) return false;
            }
        }
        return true;
    }

}
