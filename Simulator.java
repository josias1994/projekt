import java.util.*;

/*
*	Simulates the reproduction, mutation and death of a given population, according
* to the parameters below. Population declared as doubles to allow for easier calculations,
* but input taken as Ints as that's what makes sense logically.
*/
public class Simulator{
	private static final char DEATH='d', MUTATION='m', REPRODUCTION='r';
	private static int simulationMode, initInterval, countEvents, countInterval;
	private static double targetTime, initPop, maxPop, pOmega, pDeath, pMutate, pRepro, currentTime;
	private static boolean goOn = true;
	private static boolean tUnits, nUnits, verbose, silent = false;
	private static Scanner Reader;
	private static Population pop = new Population(pOmega);
	private static EventQueue queue = new EventQueue();

	/*
	*	Initializations.
	*/
	private static void init(){
		System.out.print("Initial population: ");
		initPop = Reader.nextInt();
		System.out.print("Maximum population: ");
		maxPop = Reader.nextInt();
		System.out.print("Value of the parameter Omega: ");
		pOmega = Reader.nextDouble();
		System.out.print("Value of the parameter D: ");
		pDeath = Reader.nextDouble();
		System.out.print("Value of the parameter M: ");
		pMutate = Reader.nextDouble();
		System.out.print("Value of the parameter R: ");
		pRepro = Reader.nextDouble();
		System.out.print("Time to run the simulation: ");
		targetTime = Reader.nextDouble();
		System.out.print("Simulation mode (1: every t units; 2: every n events; 3: verbose; 4: silent): ");
		simulationMode = Reader.nextInt();
		if((simulationMode == 1) || (simulationMode == 2)){
			System.out.print("Interval between observations: ");
			initInterval = Reader.nextInt();
			countInterval = initInterval;
			countEvents = 0;
		}
	}

	public static void main(String[] args){
		Reader = new Scanner(System.in);
		init();
		City[] currentCities = new CityGenerator().generate();
		createInitialPopulation(currentCities);
		do{
			epidemicCheck();
			if(queue.hasNext()){
				Event currentEvent = queue.next();
				currentTime = addTime(currentEvent);
				if(checkTime(currentTime)){
					Individual currentIndividual = currentEvent.individual();
					simulateEvent(currentEvent, currentIndividual);
					countEvents += 1;
					displaySimulation(currentEvent);
				}else{
					goOn = false;
				}
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
	*	Switches and simulates according to Event.type()
	*/
	private static void simulateEvent(Event currentEvent, Individual currentIndividual){
		// Check if the individual the event describes is still alive
		if(pop.contains(currentIndividual)){
			switch (currentEvent.type()) {
				case 'm':
					City[] oldPath = currentIndividual.path();
					mutationEvent(currentIndividual);
					if(oldPath != currentIndividual.path()){
						addMutationEvent(currentIndividual, ((1-(Math.log(pop.fitness(currentIndividual)))) * pMutate));
					}else{
						addMutationEvent(currentIndividual, ((1-(Math.log(pop.fitness(currentIndividual)))) * pMutate/10));
					}
					break;
				case 'r':
					reproductionEvent(currentIndividual);
					break;
				case 'd':
					deathEvent(currentIndividual);
					break;
			}
		}
	}

	/*
	*	Creates 3 standard Events when a new Individual is created
	*/
	private static void addBirthEvents(Individual person){
		addMutationEvent(person, ((1-(Math.log(pop.fitness(person))))));
		addReproductionEvent(person, ((1-(Math.log(pop.fitness(person)))) * (initPop/maxPop) * pRepro));
		addDeathEvent(person, (1-(Math.log(1 - pop.fitness(person)))) * pDeath);
	}

	private static void addMutationEvent(Individual person, double averageTimeProbability){
		double mTime = new RandomUtils().getRandomTime(averageTimeProbability);
		Event mutationEvent = new Event(MUTATION, (currentTime + mTime), person);
		queue.add(mutationEvent);
	}

	private static void addReproductionEvent(Individual person, double averageTimeProbability){
		double rTime = new RandomUtils().getRandomTime(averageTimeProbability);
		Event reproductionEvent = new Event(REPRODUCTION, (currentTime + rTime), person);
		queue.add(reproductionEvent);
	}

	private static void addDeathEvent(Individual person, double averageTimeProbability){
		double dTime = new RandomUtils().getRandomTime(averageTimeProbability);
		Event deathEvent = new Event(DEATH, (currentTime + dTime), person);
		queue.add(deathEvent);
	}

	private static void mutationEvent(Individual person){
		if(new RandomUtils().getRandomEvent((1.0 - pop.fitness(person)) * (1.0 - pop.fitness(person)))){
			person.mutate();
			//add timecost
			if(new RandomUtils().getRandomEvent(30.0)){
				person.mutate();
				if(new RandomUtils().getRandomEvent(15.0)){
					person.mutate();
				}
			}
		}
	}

	/*
	*	Assumes maxPop > 0
	*/
	private static void reproductionEvent(Individual parent){
		Individual child = parent.reproduce();
		pop.add(child);

		addReproductionEvent(parent, ((1.00 - Math.log(pop.fitness(parent))) * (initPop/maxPop) * pRepro));
		addBirthEvents(child);
	}

	private static void deathEvent(Individual person){
		if(new RandomUtils().getRandomEvent(1.00 - pop.fitness(person) * pop.fitness(person))){
			pop.remove(person);
		} else {
			addDeathEvent(person, (1.00 - (Math.log(1.00 - pop.fitness(person)))) * pDeath);
		}
	}

	private static void epidemicCheck(){
		if(pop.size() > maxPop){
			pop.epidemic();
		}
	}

	/*
	*	Adds up the time the simulation has run for
	*/
	private static double addTime(Event e){
		double nowTime = 0;
		return(nowTime += e.time());
	}

	private static boolean checkTime(double now){
		if(now >= targetTime){
			return false;
		} else {
			return true;
		}
	}

	private static void displaySimulation(Event currentEvent){
		switch(simulationMode) {
			case 1:
				if(countInterval + 0.01  >  currentTime && countInterval < currentTime && ((int) currentTime % initInterval) == 0){
					displayObservation();
					countInterval += initInterval;
				}
				break;
			case 2:
				if((countEvents % initInterval) == 0){
					displayObservation();
					countInterval += initInterval;
				}
				break;
			case 3:
				System.out.println("Simulating: " + currentEvent.toString());
				break;
			case 4:
				break;
		}
	}

	private static void displayObservation(){
		System.out.println("\nObservation");
		System.out.println("-----------");
		System.out.println("Current time: " + currentTime);
		System.out.println("Events Simulated: " + countEvents);
		System.out.println("Population size: " + pop.size());
		System.out.println("The best path is: " + bestPathString(pop.bestPath()) + " (cost: " + pathCost(new Individual(pop.bestPath())) + ")");
	}

	private static void endSimulation(){
		System.out.println("The best path is: " + bestPathString(pop.bestPath()) + " (cost: " + pathCost(new Individual(pop.bestPath())) + ")");
	}

	/*
	*	Returns the names of every City in City[] as a String in the correct format
	*/
	private static String bestPathString(City[] listOfCities){
		String bestPath = "";
		for(City city : listOfCities){
    	bestPath += (city.name() + "; ");
		}
		return bestPath;
	}

	private static double pathCost(Individual bestIndividual){
		return(bestIndividual.cost());
	}
}
