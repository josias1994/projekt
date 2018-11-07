public class Test{
	
	private static City[] cities;
	private static Population pop;
	public static double pOmega=0.01;
	public static Event e;
	public static final char DEATH='d';
	public static double getRandomTime();
    //public static boolean getRandomEvent(double probability)
	public static void main(String[] args){



		City[] cities = new CityGenerator().generate();
		Population pop = new Population(pOmega); 
		Individual test = new Individual(cities);
		RandomUtils random = new RandomUtils();
		pop.add(test);

		System.out.println(pop.contains(test));

			EventQueue eventQueue = new EventQueue();
			Event e = new Event(DEATH, 1.0, test);

			eventQueue.add(e);
		System.out.println(e.toString());
		System.out.println(e.type());



	}
}