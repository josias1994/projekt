import java.util.*;

/*
*	Simulates the reproduction, mutation and death of a given population and a given city.
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

		/*
		*	Main Loop
		*/
		do{
			if(queue.hasNext()){
				Event currentEvent = queue.next();
				// Check if the individual which the event describes is still alive
				if(pop.contains(currentEvent.individual())){
					goOn = checkTime(currentEvent.time());
					switch (currentEvent.type()) {
						case 'm':
							String oldPath = currentEvent.individual().path();
							currentEvent.individual().mutate();
							//Add next mutation for the mutated individual to EventQueue
							if(oldPath != currentEvent.individual.path()){
								addMutationEvent(currentEvent.individual(), ((1-(Math.log(pop.fitness(currentEvent.individual())))) * pMutate));
							}else{
								addMutationEvent(currentEvent.individual(), ((1-(Math.log(pop.fitness(currentEvent.individual())))) * pMutate/10));
							}
							break;
						case 'r':
							reproductionEvent(currentEvent.individual(), double averageTimeProbability);
							break;
						case 'd':

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
		double dTime = new RandomUtils().getRandomTime((1-(Math.log(1 - pop.fitness(person)))) * pDeath);

		Event deathEvent = new Event(DEATH, dTime, person);

		queue.add(deathEvent);
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

	/*
	*	Creates child which is mutation of parent and adds new ReproductionEvent & BirthEvents
	*/
	private static void reproductionEvent(Individual parent){
		pop.add(parent.reproduce());
		addReproductionEvent(parent, ((1 - (Math.log(pop.fitness(parent)))) * (initPop/maxPop) * pRepro));
		addBirthEvents(child);
	}

	private static deathEvent(){
		
	}

	/*
	*	Checks currentTime to see if simulation should continue
	*/
	private static boolean checkTime(double currentTime){
		if(currentTime >= simulationTime){
			return false;
		} else {
			return true;
		}
	}

	/*
	*	Ends simulation by giving results and a conclusion
	*/
	private static void endSimulation(){
		System.out.println("----------------");
		System.out.println("Simulation ended.");
	}

}
