import java.util.*;

/*
*	Simulates the reproduction, mutation and death of a given population and a given city.
*/
public class Simulator{
	private static int initPop, maxPop, pDeath, pMutate, pRepro;
	private static Scanner Reader;
	private static City[] cities;
	private static Population pop;
	public static double pOmega;
	public static final char DEATH='d';



	/*
	*	Initializations
	*/
	private static void init(){
		System.out.println("What is your initial population?");
		initPop = Reader.nextInt();
		System.out.println("What is your maximum population?");
		maxPop = Reader.nextInt();
		System.out.println("What is the value of the parameter Omega?");
		pOmega = Reader.nextDouble();
		System.out.println("What is the value of the parameter D?");
		pDeath = Reader.nextInt();
		System.out.println("What is the value of the parameter M?");
		pMutate = Reader.nextInt();
		System.out.println("What is the value of the parameter R?");
		pRepro = Reader.nextInt();
	}
	
	/*
	* Constructors
	*/
	CityGenerator cityGenerator = new CityGenerator();
	EventQueue eventQueue = new EventQueue();

	public static void main(String[] args){
		Reader = new Scanner(System.in);
		init();
		boolean goOn = true;
		while(goOn){
			goOn = false;
		}

		City[] cities = new CityGenerator().generate();
		Population pop = new Population(pOmega);

		int i = 0;
		while(i<initPop){
			Individual k = new Individual(cities);
			pop.add(k);
			i=i+1;
		}

		System.out.println("Population Size: " + pop.size());
		Reader.close();
	}
}
