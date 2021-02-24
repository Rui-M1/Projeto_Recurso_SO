package Concorrencial.Base;

import Original.AJE;

public class BaseThread extends Thread{

    public static int threadID = -1;
    private AJE aje;
    private int[] bestPath;
    private int bestPathSum;

    @Override
    public void run()
    {
            //aje = new AJE(); //teste
            //aje.start();  //teste
            bestPathSum = aje.getBestPathSum();
            bestPath = aje.getBestPath();
    }

    public BaseThread()
    {
        threadID++;
    }

    public int[] bestPath()
    {
        return bestPath;
    }

    public int bestSumValue()
    {
        return bestPathSum;
    }

}
