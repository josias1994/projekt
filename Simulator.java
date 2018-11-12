import java.util.*;

/*
*	Simulates the reproduction, mutation and death of a given population and a given city.
*/
public class Simulator{
	private static int initPop, maxPop, pDeath, pMutate, pRepro;
	private static Scanner Reader;
	private static double pOmega;
	private static final char DEATH='d', MUTATION='m', REPRODUCTION='r';
	private static Population pop = new Population(pOmega);
	private static EventQueue eventQueue = new EventQueue();



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

	public static void main(String[] args){
		Reader = new Scanner(System.in);
		init();

		City[] currentCities = new CityGenerator().generate();
		createInitialPopulation(currentCities);


		Reader.close();
	}

	/*
	*	Creates the Initial Population and the Events following
	*/
	private static void createInitialPopulation(City[] currentCities){
		int i = 0;
		while(i<initPop){
			Individual person = new Individual(currentCities);
			pop.add(person);
			createEvents(person);
			i=i+1;
		}
	}

	/*
	*	Adds a Mutation-, Reproduction- and Death-event to the EventQueue, for the specified Individual
	*/
	private static void createEvents(Individual person){
		double mTime = new RandomUtils().getRandomTime((1-(Math.log(pop.fitness(person)))) * pMutate);
		double rTime = new RandomUtils().getRandomTime((1-(Math.log(pop.fitness(person)))) * (initPop/maxPop) * pRepro);
		double dTime = new RandomUtils().getRandomTime((1-(Math.log(1 - pop.fitness(person)))) * pDeath);
		Event mutationEvent = new Event(MUTATION, mTime, person);
		Event reproductionEvent = new Event(REPRODUCTION, mTime, person);
		Event deathEvent = new Event(DEATH, mTime, person);
		eventQueue.add(mutationEvent);
		eventQueue.add(reproductionEvent);
		eventQueue.add(deathEvent);
	}


}
