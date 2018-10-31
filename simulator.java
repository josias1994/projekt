import java.util.Scanner;

public class Simulator{

	public static void main(String[] args){
		Scanner reader = new Scanner(System.in);

		/*
		*	Get parameters from userinput.
		*/
		System.out.println("What is your initial population?");
		int initPop = reader.nextInt();
		System.out.println("What is your maximum population?");
		int maxPop = reader.nextInt();
		System.out.println("What is the value of the parameter Omega?");
		double pOmega = reader.nextInt();
		System.out.println("What is the value of the parameter D?");
		int pDeath = reader.nextInt();
		System.out.println("What is the value of the parameter M?");
		int pMutate = reader.nextInt();
		System.out.println("What is the value of the parameter R?");
		int pRepro = reader.nextInt();



		City copenhagen=new City("Copenhagen",10,20);

		City berlin=new City("Berlin",100,200);

		System.out.println(copenhagen.name());
		System.out.println(copenhagen.x());
		System.out.println(copenhagen.y());

		System.out.println(berlin.name());
		System.out.println(berlin.x());
		System.out.println(berlin.y());





	}

}
