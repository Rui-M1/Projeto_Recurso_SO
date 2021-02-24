import Concorrencial.Base.Base;
import Original.AJE;

import java.util.Scanner;

public class AppStart {

    public static void main(String[] args)
    {
        String res;
        int continuarCorrer = 0;
        int acabar = 0;

        BestPath bestPath = new BestPath();

        do
        {
            System.out.println("Que algoritmo deseja correr?\n 1- AJE++\n 2- Concorrencial Base\n 3- Concorrencial Avançado\n 0 - Terminar Programa");
            Scanner scan = new Scanner(System.in);
            int mode = scan.nextInt();

            switch(mode)
            {
                case 1:
                System.out.println("Introduza os parametros necessarios para correr o algoritmo:\n " +
                        "filename,numero de caminhos,tempo limite,chance de mutação");

                String[] parameters = scan.next().split(",");
                String filename = parameters[0];
                int pathSize = Integer.parseInt(parameters[1]);
                int time = Integer.parseInt(parameters[2]);
                double chance = Double.parseDouble(parameters[3]);

                AJE aje = new AJE();
                aje.start(filename, pathSize, time, chance);
                bestPath.setBestPath(aje.getBestPath());
                bestPath.setBestPathSum(aje.getBestPathSum());
                System.out.println("\n\nO melhor caminho encontrado foi:");
                bestPath.printBestPath();
                System.out.println("\n\nCom uma distancia de: " + bestPath.getBestPathSum() + "\n");
                
                break;

                case 2:
            
                System.out.println("Introduza os parametros necessarios para correr o algoritmo:\n " +
                        "filename,numero de threads,numero de caminhos,tempo limite,chance de mutação");

                String[] parameters2 = scan.next().split(",");
                String filename2 = parameters2[0];
                int threadNum = Integer.parseInt(parameters2[1]);
                int pathSize2 = Integer.parseInt(parameters2[2]);
                int time2 = Integer.parseInt(parameters2[3]);
                double chance2 = Double.parseDouble(parameters2[4]);

                //int[] resultList = new int[threadNum];

                //Base.Storage storage = new Base.Storage(threadNum);

                Base[] threads = new Base[threadNum];

                /*for(int i = 1; i < threadNum+1; i++)
                {
                    Base thread = new Base(i, filename2, pathSize2, time2, chance2, threadNum);
                    thread.start();
                }*/

                populateThreads(threadNum, filename2, pathSize2, time2, chance2, threads);
                runThreads(threads);

                Base.Storage storage = new Base.Storage(threadNum);
                int[] resultList;

                resultList = storage.get();
                bestPathSolution(resultList);

                break;

                case 3:
                System.out.println("Programa em Desenvolvimento!");
                break;

                case 0:
                System.out.println("Programa Terminado!");
                System.exit(0);
                scan.close();

                break;

                default :
                System.out.println("Selecione 1, 2, 3, 0");
                continue;
            }

                do
                {System.out.print("Deseja continuar? (S/N) \n");
                res = scan.next();
            
                switch(res){

                    case "s":
                    case "S":
                    acabar = 1;
                    break;
                                    
                    case "N":
                    case "n":
                    System.out.println("Programa Terminado!");
                    acabar = 2;
                    System.exit(0);
                    break;
                }
                }
                while(acabar == 0) ;
            
        }
        while(continuarCorrer == 0);
    }

    private static void bestPathSolution(int[] resultList) {
        int bestSolution = 0;

        for (int i = 0; i < resultList.length; i++) {
            int curr = i;
            if (resultList[curr] < resultList[curr + 1]) {
                bestSolution = resultList[i];
            }
        }

        System.out.println("The best solution found is: " + bestSolution);
    }

    private static void populateThreads(int threadNum, String filename2, int pathSize2, int time2, double chance2, Base[] threads)
    {
        for(int i = 0; i < threads.length; i++)
        {
            Base thread = new Base(i, filename2, pathSize2, time2, chance2, threadNum);
            threads[i] = thread;
        }
    }

    private static void runThreads(Base[] threads)
    {
        for (int i = 0; i < threads.length; i++)
        {
            threads[i].start();
        }
    }
}
