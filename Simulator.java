import java.util.*;

/*
*	Simulates the reproduction, mutation and death of a given population and a given city.
*/
public class Simulator{
	private static int initPop, maxPop, pDeath, pMutate, pRepro;
	private static double pOmega;
	private static Scanner Reader;

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
		boolean goOn = true;
		while(goOn){
			goOn = false;
		}


		Reader.close();
	}
}
