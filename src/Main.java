import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Main {
	
	private static boolean testing = false;
	
	private static CERobot toriGod;
	private static byte[][] genome;
	private static byte[][] topGenome;
	
	private static boolean prelim = false;
	private static byte[][] prelimGenome = 
		{{3,3,1,3,1,2,3,3,3,3,3,1,3,1,3,3,3,2,2,2},
		 {3,3,2,3,1,2,3,3,3,3,2,1,3,2,3,3,2,1,1,1}};
	
	private static final int turns = 2;
	
	public static void main(String[] args) {
		
		genome = new byte[turns][20];
		topGenome = new byte[turns][20];
		
		System.out.println("Program starting in 10 seconds.");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			System.out.print("Unable to pause thread.");
			e.printStackTrace();
		}
		
		toriGod = new CERobot();
		toriGod.newGame();
		try {
			FileWriter del = new FileWriter("genData.txt");
			del.close();
		} catch (IOException e1) {
			System.out.println("Unable to clear data.");
			e1.printStackTrace();
		}
		
		double topAdvantage = 0;
		
		if( prelim ) {
			for( byte[] seq : prelimGenome ) {
				try {
					Thread.sleep(700);
				} catch (InterruptedException e) {
					System.out.println("Unable to pause thread.");
					e.printStackTrace();
				}
				toriGod.setState(seq);
				toriGod.endTurn();
			}
			
			if( toriGod.getAdvantage() > topAdvantage ) {
				topGenome = Arrays.copyOf(genome, genome.length);
				topAdvantage = toriGod.getAdvantage();
				System.out.println("New leader: " + topAdvantage);
				for( byte[] b : topGenome )
					System.out.println(Arrays.toString(b));
			}
			toriGod.newGame();
			
		} else {
			int counter = 0;
			while( topAdvantage == 0 && counter <= 1) {
				for( int i = 1; i < 40; i++ ) {
					randomizeSequences();
					for( int t = 0; t < turns; t++ ) {
						try {
							Thread.sleep(700);
						} catch (InterruptedException e) {
							System.out.println("Unable to pause thread.");
							e.printStackTrace();
						}
						toriGod.setState(genome[t]);
						toriGod.endTurn();
					}
					if( toriGod.getAdvantage() > topAdvantage ) {
						topGenome = Arrays.copyOf(genome, genome.length);
						topAdvantage = toriGod.getAdvantage();
						System.out.println("New leader: " + topAdvantage);
						for( byte[] b : topGenome )
							System.out.println(Arrays.toString(b));
					}
					toriGod.newGame();
					if( i == 4 && testing ) System.exit(1);
				}
				counter++;
			}
		}
		try {
			recordRound(0, 0, topAdvantage);
		} catch (IOException e) {
			System.out.println("Could not write to file.");
			e.printStackTrace();
		}
		System.out.println("Search complete. Starting optimization process.");
		
		for( int s = 0; s < prelimGenome.length; s++ ) {
			for( int i = 0; i < 20; i++ ) {
				topGenome[s][i] = prelimGenome[s][i];
			}
		}
		if( topAdvantage > 0 ) {
			for( int g = 1; g <= 4; g++ ) {
				for( int i = 1; i <= 15; i++ ) {
					if( i == 4 && testing ) System.exit(1);
					genome = Arrays.copyOf(mutateSequences(g), genome.length);
					for( int t = 0; t < genome.length; t++ ) {
						try {
							Thread.sleep(700);
						} catch (InterruptedException e) {
							System.out.println("Unable to pause thread.");
							e.printStackTrace();
						}
						toriGod.setState(genome[t]);
						toriGod.endTurn();
					}
					if( toriGod.getAdvantage() > topAdvantage ) {
						byte[][] topGenome = new byte[turns][20];
						for( int s = 0; s < topGenome.length; s++ ) {
							for( int k = 0; k < 20; k++ ) {
								topGenome[s][k] = genome[s][k];
							}
						}
						
						topAdvantage = toriGod.getAdvantage();
						try {
							recordRound(g, i, toriGod.getAdvantage());
						} catch (IOException e) {
							System.out.println("Could not write to file.");
							e.printStackTrace();
						}
						System.out.println("New leader: " + topAdvantage);
						for( byte[] b : topGenome )
							System.out.println(Arrays.toString(b));
					}
					toriGod.newGame();
				}
				System.out.println("Generation " + g + " complete.");
			}
		}
	}
	
	private static byte[][] mutateSequences(int gen) {
		byte[][] tempGenome = new byte[turns][20];
		for( int s = 0; s < topGenome.length; s++ ) {
			for( int i = 0; i < 20; i++ ) {
				tempGenome[s][i] = topGenome[s][i];
			}
		}
		
		Random rand = new Random();
		for(byte[] seq : tempGenome) {
			for( int i = 0; i < 20; i++ ) {
				if( rand.nextInt(gen*6) == 1 ) {
					seq[i] = (byte) (rand.nextInt(4) + 1);
				}
			}
		}
		return tempGenome;
	}
	
	private static void randomizeSequences() {
		genome = new byte[turns][20];
		for( int s = 0; s < genome.length; s++ ) {
			for( int i = 0; i < 20; i++ ) {
				// System.out.println(i + " " + Arrays.toString(topGenome[0]));
				Random rand = new Random();
				genome[s][i] = (byte) (rand.nextInt(4) + 1);
			}
		}
	}
	
	private static void recordRound(int gen, int indiv, double advantage) throws IOException {
		FileWriter fw = new FileWriter("genData.txt", true);
		
		fw.append("Gen " + gen + " - #" + indiv);
		fw.append("\r");
		
		for( byte[] seq : genome ) {
			for( int i = 0; i < seq.length; i++ ) {
				fw.append(Byte.toString(seq[i]));
			}
			fw.append("\r");
		}
		
		fw.append("[" + Double.toString(advantage) + "]");
		fw.append("\r\r");
		
		fw.close();
	}
}
