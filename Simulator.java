import java.util.*;

/*
*	Simulates the reproduction, mutation and death of a given population and a given city.
*/
public class Simulator{

	public static void main(String[] args){
		int initPop, maxPop, pDeath, pMutate, pRepro;
		double pOmega;
		// Import cities[]




		/*
		*	Get userinput for parameters
		*/
		Scanner reader = new Scanner(System.in);
		System.out.println("What is your initial population?");
		initPop = reader.nextInt();
		System.out.println("What is your maximum population?");
		maxPop = reader.nextInt();
		System.out.println("What is the value of the parameter Omega?");
		pOmega = reader.nextDouble();
		System.out.println("What is the value of the parameter D?");
		pDeath = reader.nextInt();
		System.out.println("What is the value of the parameter M?");
		pMutate = reader.nextInt();
		System.out.println("What is the value of the parameter R?");
		pRepro = reader.nextInt();

		/*
		*	Main loop to interact with EventQueue & Population class
		*/
		EventQueue Queue = new EventQueue();
		Population CurrentPopulation = new Population(0.001); //takes pOmega

		/*
		*	Add an individual with a random list of cities to the population.
		* Do this for all individual from initial population.
		*/
		CityGenerator Generator = new CityGenerator();
		for(int i=0; i<initPop; i++){
			Individual CurrentPerson = new Individual();

		}

		// CurrentPopulation.add(CurrentPerson(Generator.generate()));



	}
}
