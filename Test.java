public class Test{
	
	private static City[] cities;
	private static Population pop;

	public static void main(String[] args){
		
		Individual testIndividual = new Individual(cities);
		pop.add(testIndividual);

		System.out.println(pop.contains(testIndividual));
	}
}