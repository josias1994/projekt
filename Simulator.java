import java.util.*;

/*
*	Simulates the reproduction, mutation and death of a given population, according
* to the parameters below. Population declared as doubles to allow for easier calculations,
* but input taken as Ints as because that's what makes sense logically.
*/
public class Simulator{
	private static final char DEATH='d', MUTATION='m', REPRODUCTION='r', TUNITS='t', NUNITS='n', VERBOSE='v', SILENT='s';
	private static double targetTime, initPop, maxPop, pOmega, pDeath, pMutate, pRepro, currentTime;
	private static boolean goOn = true;
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
					System.out.println(currentEvent.toString());
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
	*	Adds mutation-, reproduction- and death event for individual with standard values
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

	/*
	*	Check if any of the exit-parameters have been met.
	*/
	private static boolean checkTime(double now){
		if(now >= targetTime){
			return false;
		} else {
			return true;
		}
	}

	private static void endSimulation(){
		System.out.println("----------------");
		Individual optimalIndividual = new Individual(pop.bestPath());
		double bestPathCost = optimalIndividual.cost();
		City[] bestPathList = optimalIndividual.path();
		System.out.println("The best path is: " + bestPath(bestPathList) + " (cost: " + bestPathCost + ")");


		System.out.println(bestPathList[0].name());
	}

	/*
	*	Returns the City[] in the correct format
	*/
	private static String bestPath(City[] listOfCities){
		String bestPath = "";
		for(City city : listOfCities){
    	bestPath += (city.name() + "; ");
		}
		return bestPath;
	}
}
