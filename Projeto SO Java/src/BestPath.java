public class BestPath {

    private int bestPathSum;
    private int[] bestPath;

    public BestPath(){}

    public int getBestPathSum()
    {
        return bestPathSum;
    }

    public int[] getBestPath()
    {
        return bestPath;
    }

    public void setBestPathSum(int bestPathSum)
    {
        this.bestPathSum = bestPathSum;
    }

    public void setBestPath(int[] bestPath)
    {
        this.bestPath = bestPath;
    }

    public void printBestPath()
    {
        for (int i = 0; i < bestPath.length; i++)
            System.out.printf("%2d, ", bestPath[i]);
    }
}
