package Original;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import Resources.PMXCrossover;

public class AJE{

    private final Random rand = new Random();

    private int pathSize;
    private int n;
    private int[][] matrix;
    private int[] bestPath;

    public AJE(){}

    /**
     * Metodo responsável pelo import dos dados do ficheiro especificado para as
     * variaveis correspondentes
     *
     * @param filename -> ficheiro com dados do exercicio
     * */
    private void importValues(String filename)
    {

        String path = "tsp_testes\\";

        File file = new File(path + filename + ".txt");

        try
        {
            Scanner scanFile = new Scanner(file);

            if(scanFile.hasNextLine())
                n = Integer.parseInt(scanFile.nextLine());

            matrix = new int[n][n];

            int i = 0;
            while (scanFile.hasNextLine() && i < n)
            {
                for(int j = 0; j < n; j++)
                {
                    int dist = scanFile.nextInt();
                    matrix[i][j] = dist;
                }
                i++;
            }
            scanFile.close();
        }
        catch (FileNotFoundException e){ e.printStackTrace(); }

    }

    /**
     * Metodo responsavel pela execução e funcionamento do algoritmo
     *
     * @param filename -> ficheiro com dados do exercicio
     * @param mutationChance -> percentagem de probabilidade de occorencia de uma mutação
     * @param pathSize -> numero de caminhos gerados
     * @param seconds  -> tempo limite de funcionamento
     * */
    public void start(String filename, int pathSize, int seconds, double mutationChance)
    {

        importValues(filename);
        this.pathSize = pathSize;
        LocalTime runTime = LocalTime.now();
        System.out.println(runTime);
        LocalTime end = runTime.plusSeconds(seconds);
        System.out.println(end);

        ArrayList<int[]> paths = new ArrayList<>();
        int[] goodPath1;
        int[] goodPath2;

        populatePaths(paths);
        printPathsArray(paths);

        do
        {
            goodPath1 = evaluatePaths(paths);
            paths.remove(goodPath1);
            goodPath2 = evaluatePaths(paths);
            paths.add(goodPath1);
            printGoodPaths(goodPath1, goodPath2);

            int[] goodPathChild1 = new int[n];
            int[] goodPathChild2 = new int[n];

            PMXCrossover pmx = new PMXCrossover();
            pmx.pmxCrossover(goodPath1, goodPath2, goodPathChild1, goodPathChild2, n, rand);

            double prob = 0 + 1 * rand.nextDouble();

            if(prob <= mutationChance)
            {
                applyMutation(goodPathChild1, goodPathChild2);
                //na função postMutationExchange vemos a inserção dos goodPathChildren
                // e a retira dos maiores caminhos
                postMutationExchange(goodPathChild1, goodPathChild2, paths);
            }

            bestPath = goodPath1;

            //sem esta linha o codigo da classe Base dá erro pois considera impossivel ir buscar a função getBestPathSum
            // pós execução da função start desta classe
            // int bestPathSum = getBestPathSum(); //teste
            getBestPathSum(); //teste
            runTime = LocalTime.now();
            System.out.println(runTime);
        }
        while(runTime.compareTo(end) < 0);
    }

    /**
     * Metodo responsavel pela população dos caminhos aleatoriamente
     *
     * @param paths -> storage de todos os caminhos gerados
     * */
    private void populatePaths(ArrayList<int[]> paths)
    {
        for(int i = 0; i < pathSize; i++)
        {
            int[] path = new int[n];

            for (int j = 0; j < n; j++)
            {
                int curr = j;

                int dist = rand.nextInt(n);

                if(j > 0 && dist == path[curr-1]) {
                    do
                    {
                        dist = rand.nextInt(n);
                    }while (path[curr-1] == dist);
                }
                path[j] = dist;
            }
            paths.add(path);
        }
    }

    /**
     * Metodo responsavel pelo calculo da distancia total de um caminho
     *
     * @param path -> storage de todos os caminhos gerados
     * */
    private int calculateDistance(int[] path)
    {
        int arraySum = 0;
        for(int i = 0; i < path.length; i++)
        {
            int curr = i;
            if(curr+1 != n-1)
                arraySum += matrix[path[curr]][path[curr++]];
            else
                arraySum += matrix[path[curr]][path[0]];
        }
        return arraySum;
    }

    /**
     * Metodo responsavel pela avaliação de todos os caminhos gerados
     *
     * @param paths -> storage de todos os caminhos gerados
     * */
    private int[] evaluatePaths(ArrayList<int[]> paths)
    {
        int[] goodPath = new int[n];
        int goodPathSum = 0;

        for (int[] path : paths)
        {
            int pathSum = calculateDistance(path);

            if(goodPathSum == 0)
            {
                goodPathSum = pathSum;
                goodPath = path;
            }
            else if(goodPathSum > pathSum)
            {
                goodPathSum = pathSum;
                goodPath = path;
            }
        }
        return goodPath;
    }

    /**
     * Metodo responsavel pela aplicação de uma Exchange Mutation
     *
     * @param goodPathChild -> descendente dos caminhos com menor distancia
     * @param goodPathChild2 -> descendente dos caminhos com menor distancia
     * */
    private void applyMutation(int[] goodPathChild, int[] goodPathChild2)
    {
        int tempPos;

        int randomPos1 = rand.nextInt(n);
        int randomPos2;
        int tempRand = rand.nextInt(n);

        while(tempRand == randomPos1)
            tempRand = rand.nextInt(n);

        randomPos2 = tempRand;

        tempPos = goodPathChild[randomPos1];
        goodPathChild[randomPos1] = goodPathChild2[randomPos1];
        goodPathChild2[randomPos1] = tempPos;

        tempPos = goodPathChild[randomPos2];
        goodPathChild[randomPos2] = goodPathChild2[randomPos2];
        goodPathChild2[randomPos2] = tempPos;
    }

    /**
     * Metodo responsavel por pos Exchange Mutation remover os 2 maiores caminhos e
     * inserir os 2 caminhos descendentes
     *
     * @param paths -> storage de todos os caminhos gerados
     * @param goodPathChild -> descendente dos caminhos com menor distancia
     * @param goodPathChild2 -> descendente dos caminhos com menor distancia
     * */
    private void postMutationExchange(int[] goodPathChild, int[] goodPathChild2, ArrayList<int[]> paths)
    {
        /*
        int goodPathChildSum = 0;
        int goodPathChildSum2 = 0;


        for(int i = 0; i < goodPathChild.length; i++)
        {
            int curr = i;
            int curr2 = i;
            if(curr+1 != n-1)
            {
                goodPathChildSum += matrix[goodPathChild[curr]][goodPathChild[curr++]];
                goodPathChildSum2 += matrix[goodPathChild2[curr2]][goodPathChild2[curr2++]];
            }
            else
                {
                    goodPathChildSum += matrix[goodPathChild[curr]][goodPathChild[0]]; 
                    goodPathChildSum2 += matrix[goodPathChild2[curr2]][goodPathChild2[0]];
                }
        }*/

        printGoodPathChildren(goodPathChild, goodPathChild2);

        getBiggestPath(paths);
        getBiggestPath(paths);

        paths.add(goodPathChild);
        paths.add(goodPathChild2);

    }

    /**
     * Metodo responsavel pelo calculo dos maiores caminhos dentro do storage
     *
     * @param paths -> storage de todos os caminhos gerados
     * */
    private void getBiggestPath(ArrayList<int[]> paths)
    {
        int[] bigPath = new int[n];
        int bigPathSum = 0;

        for (int[] path : paths)
        {
            int arraySum = calculateDistance(path);
            /*
            for(int i = 0; i < path.length; i++)
            {
                int curr = i;
                if(curr+1 != n-1)
                    arraySum += matrix[path[curr]][path[curr++]];
                else
                    arraySum += matrix[path[curr]][path[0]];
            }*/
            if(bigPathSum == 0)
            {
                bigPathSum = arraySum;
                bigPath = path;
            }
            else if(bigPathSum < arraySum)
            {
                bigPathSum = arraySum;
                bigPath = path;
            }
        }
        paths.remove(bigPath);
    }

    /**
     * Metodo responsavel por mostrar no ecra os melhores caminhos encontrados
     *
     * @param goodPath1 -> melhor caminho encontrado
     * @param goodPath2 -> segundo melhor caminho encontrado
     * */
    private void printGoodPaths(int[] goodPath1, int[] goodPath2)
    {
        System.out.println("\nGood Path 1:");
        for (int i = 0; i < n; i++)
            System.out.printf("%2d, ", goodPath1[i]);
        System.out.println();

        System.out.println("\nGood Path 2:");
        for (int i = 0; i < n; i++)
            System.out.printf("%2d, ", goodPath2[i]);
        System.out.println("\n\n");
    }

    /**
     * Metodo responsavel por mostrar no ecra os descendentes dos melhores caminhos encontrados
     *
     * @param goodPathChild -> descendente dos caminhos com menor distancia
     * @param goodPathChild2 -> descendente dos caminhos com menor distancia
     * */
    private void printGoodPathChildren(int[] goodPathChild, int[] goodPathChild2)
    {
        System.out.println("\nGood Path First Child:");
        for (int i = 0; i < n; i++)
            System.out.printf("%2d, ", goodPathChild[i]);
        System.out.println();

        System.out.println("\nGood Path Second Child:");
        for (int i = 0; i < n; i++)
            System.out.printf("%2d, ", goodPathChild2[i]);
        System.out.println();
    }

    /**
     * Metodo responsavel por mostrar no ecra todos os caminhos gerados
     *
     * @param paths -> storage de todos os caminhos gerados
     * */
    private void printPathsArray(ArrayList<int[]> paths)
    {
        int currentArray = 1;
        for (int[] path : paths) {
            System.out.println("Array " + currentArray + ":");
            for (int i = 0; i < n; i++)
                System.out.printf("%2d, ", path[i]);
            System.out.println();
            currentArray++;
        }
        System.out.println();
    }

    /**
     * Metodo responsavel por retornar o melhor caminho
     * */
    public int[] getBestPath()
    {
        return bestPath;
    }

    /**
     * Metodo responsavel por retornar a soma do melhor caminho
     * */
    public int getBestPathSum()
    {
        int bestSum = calculateDistance(bestPath);

        /*
        for(int i = 0; i < bestPath.length; i++)
        {
            int curr = i;
            if(curr+1 != n-1)
                bestSum += matrix[bestPath[curr]][bestPath[curr++]];
            else
                bestSum += matrix[bestPath[curr]][bestPath[0]];
        }*/

        return bestSum;
    }
}