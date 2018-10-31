import java.util.*;

public class Simulator{

	public static void main(String[] args){
		int initPop, maxPop, pDeath, pMutate, pRepro;
		double pOmega;

		Scanner reader = new Scanner(System.in);
		System.out.println("What is your initial population?");
		initPop = reader.nextInt();
		System.out.println("What is your maximum population?");
		maxPop = reader.nextInt();
		System.out.println("What is the value of the parameter Omega?");
		pOmega = reader.nextInt();
		System.out.println("What is the value of the parameter D?");
		pDeath = reader.nextInt();
		System.out.println("What is the value of the parameter M?");
		pMutate = reader.nextInt();
		System.out.println("What is the value of the parameter R?");
		pRepro = reader.nextInt();

		/*
		*	Main loop to interact with EventQueue.class and Population.class
		*/
		EventQueue queue = new EventQueue();
		Population population = new Population(pOmega);
		System.out.println("");
	}

	/*
	*	Get parameters from userinput.
	*/
	public static void GetInput(){

	}
}
