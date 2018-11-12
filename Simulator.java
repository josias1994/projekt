import java.util.*;

/*
*	Simulates the reproduction, mutation and death of a given population, according
to the parameters below.
*/
public class Simulator{
	private static final char DEATH='d', MUTATION='m', REPRODUCTION='r', TUNITS='t', NUNITS='n', VERBOSE='v', SILENT='s';
	private static int initPop, maxPop, pDeath, pMutate, pRepro;
	private static double pOmega, simulationTime;
	private static boolean goOn = true;
	private static Scanner Reader;
	private static Population pop = new Population(pOmega);
	private static EventQueue queue = new EventQueue();

	/*
	*	Initializations
	*/
	private static void init(){
		System.out.print("Initial population: ");
		initPop = Reader.nextInt();
		System.out.print("Maximum population: ");
		maxPop = Reader.nextInt();
		System.out.print("Value of the parameter Omega: ");
		pOmega = Reader.nextDouble();
		System.out.print("Value of the parameter D: ");
		pDeath = Reader.nextInt();
		System.out.print("Value of the parameter M: ");
		pMutate = Reader.nextInt();
		System.out.print("Value of the parameter R: ");
		pRepro = Reader.nextInt();
		System.out.print("Time to run the simulation: ");
		simulationTime = Reader.nextInt();
	}

	public static void main(String[] args){
		Reader = new Scanner(System.in);
		init();
		City[] currentCities = new CityGenerator().generate();
		createInitialPopulation(currentCities);
		do{
			if(queue.hasNext()){
				System.out.println("hasNext=true;");
				Event currentEvent = queue.next();
				System.out.println(queue.next().time());
				Individual currentIndividual = currentEvent.individual();
				goOn = endCheck(currentEvent);
				epidemicCheck();
				System.out.println(currentEvent.toString());

				// Check if the individual the event describes is still alive
				if(pop.contains(currentIndividual)){
					switch (currentEvent.type()) {
						case MUTATION:
							City[] oldPath = currentIndividual.path();
							mutationEvent(currentIndividual);
							//Add next mutation for the mutated individual to EventQueue (dependency on oldPath)
							if(oldPath != currentIndividual.path()){
								addMutationEvent(currentIndividual, ((1-(Math.log(pop.fitness(currentIndividual)))) * pMutate));
							}else{
								addMutationEvent(currentIndividual, ((1-(Math.log(pop.fitness(currentIndividual)))) * pMutate/10));
							}
							break;
						case REPRODUCTION:
							reproductionEvent(currentIndividual);
							break;
						case DEATH:
							deathEvent(currentIndividual);
							break;
					}
				}
			} else {
				goOn = false;
			}
		}while(goOn);
		endSimulation();
		Reader.close();
	}

	/*
	*	Creates the Initial Population and the Events following. Assumes initPop >= 0
	*/
	private static void createInitialPopulation(City[] currentCities){
		int i = 0;
		while(i<initPop){
			Individual person = new Individual(currentCities);
			pop.add(person);
			addBirthEvents(person);
			i=i+1;
		}
	}

	/*
	*	Adds mutation-, reproduction- and death event for individual with standard values
	*/
	private static void addBirthEvents(Individual person){
		addMutationEvent(person, ((1-(Math.log(pop.fitness(person))))));
		addReproductionEvent(person, ((1-(Math.log(pop.fitness(person)))) * (initPop/maxPop) * pRepro));
		addDeathEvent(person);
	}

	private static void addMutationEvent(Individual person, double averageTimeProbability){
		double mTime = new RandomUtils().getRandomTime(averageTimeProbability);
		Event mutationEvent = new Event(MUTATION, mTime, person);
		queue.add(mutationEvent);
	}

	private static void addReproductionEvent(Individual person, double averageTimeProbability){
		double rTime = new RandomUtils().getRandomTime(averageTimeProbability);
		Event reproductionEvent = new Event(REPRODUCTION, rTime, person);
		queue.add(reproductionEvent);
	}

	private static void addDeathEvent(Individual person){
		double dTime = new RandomUtils().getRandomTime((1-(Math.log(1 - pop.fitness(person)))) * pDeath);
		Event deathEvent = new Event(DEATH, dTime, person);
		queue.add(deathEvent);
	}

	private static void mutationEvent(Individual person){
		if(new RandomUtils().getRandomEvent((1 - pop.fitness(person)) * (1 - pop.fitness(person)))){
			person.mutate();
			if(new RandomUtils().getRandomEvent(30)){
				person.mutate();
				if(new RandomUtils().getRandomEvent(15)){
					person.mutate();
				}
			}
		}
	}

	private static void reproductionEvent(Individual parent){
		Individual child = parent.reproduce();
		pop.add(child);
		addReproductionEvent(parent, ((1 - (Math.log(pop.fitness(parent)))) * (initPop/maxPop) * pRepro));
		addBirthEvents(child);
	}

	private static void deathEvent(Individual person){
		if(new RandomUtils().getRandomEvent(1 - pop.fitness(person) * pop.fitness(person))){
			pop.remove(person);
		} else {
			addDeathEvent(person);
		}
	}

	private static void epidemicCheck(){
		if(pop.size() > maxPop){
			pop.epidemic();
		}
	}

	/*
	*	Check if any of the exit-parameters have been met.
	*/
	private static boolean endCheck(Event e){
		double currentTime = e.time();
		if((currentTime >= simulationTime) || (pop.size() <= 0)){
			return false;
		} else {
			return true;
		}
	}

	private static void endSimulation(){
		System.out.println("----------------");
		double bestPathCost = new Individual(pop.bestPath()).cost();
		Individual bestIndividual = new Individual(pop.bestPath());
		System.out.println("The best path is: " + bestIndividual.path() + " (cost: " + bestPathCost + " )");
	}

}
