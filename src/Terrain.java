import java.util.Random;


public class Terrain {

	public double[][] terrain;
	public Random random;
	
	/*public Terrain(int rows, int cols)
	{
		random = new Random(System.currentTimeMillis());
		double[][] temp = scaledTables(32,32,0.75,100,5);
		printTable(temp);
		System.out.println("--------------------");
		printTable(iNoise(temp,2));
		//printTable();
		terrain = posTable(iNoise(temp,2));
	}*/

	public Terrain(int rows, int cols, double seed)
	{
		random = new Random((long)seed);
		double[][] temp = scaledTables(32,32,0.75,100,5);
		printTable(temp);
		System.out.println("--------------------");
		printTable(iNoise(temp,4));
		//printTable();
		terrain = posTable(iNoise(temp,4));
	}
	
	/** Prints a table
	 */
	public void printTable(double[][] t)
	{
		for (int i = 0; i < t.length; i++)
		{
			for (int j = 0; j < t[0].length; j++)
			{
				System.out.print((int)t[i][j] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * Returns a set of not interpolated 3d noise with precision
	 * @param squareSize Level of precision
	 * @param amp Maximum amplitude
	 */
	private double[][] noise(int rows, int cols, int squareSize, double amp)
	{
		double[][] temp = new double[rows][cols];
		for (int i = 0; i < rows; i += squareSize)
		{
			for (int j = 0; j < cols; j += squareSize)
			{
				int height = (int)(random.nextDouble()*amp);
				for (int k = 0; k < squareSize; k++)
				{
					for (int l = 0; l < squareSize; l++)
					{
						temp[i+k][j+l] = height;
					}
				}
			}
		}
		return temp;
	}
	
	/**
	 * Returns positive noise
	 */
	private double[][] posTable(double[][] t)
	{
		double[][] temp = new double[t.length][t[0].length];
		for (int r = 0; r < t.length; r++)
		{
			for (int c = 0; c < t[0].length; c++)
			{
				temp[r][c] = Math.abs(t[r][c]);
			}
		}
		return temp;
	}
	
	/**
	 * Returns interpolated noise
	 */
	private double[][] iNoise(double[][] noise, double expand)
	{
		double[][] temp = new double[(int)(noise.length*expand)][(int)(noise[0].length*expand)];
		BicubicInterpolator b = new BicubicInterpolator();
		/*for (int r = 0; r < noise.length; r++)
		{
			for (int c = 0; c < noise[0].length; c++)
			{
				temp[r][c] = b.getValue(noise, r, c);
			}
		}*/
		for (int r = 0; r < temp.length; r++)
		{
			for (int c = 0; c < temp[0].length; c++)
			{
				temp[r][c] = b.getValue(noise, r/expand, c/expand);
			}
		}
		return temp;
	}
	
	/**
	 * Returns an averaging of a list of tables that get progressively more fine and dull
	 */
	private double[][] scaledTables(int rows, int cols, double persi, double amp, int times)
	{
		double[][][] temp = new double[times][rows][cols];
		for (int i = 0; i < times; i++)
		{
			temp[i] = noise(rows,cols,(int)(rows/Math.pow(2,i+1)),amp*Math.pow(persi,i));
		}
		return averageTables(temp);
	}
	
	/**
	 * Averages tables
	 */
	private double[][] averageTables(double[][][] tables)
	{
		double[][] temp = new double[tables[0].length][tables[0][0].length];
		for (int i = 0; i < tables.length; i++)
		{
			for (int r = 0; r < tables[0].length; r++)
			{
				for (int c = 0; c < tables[0][0].length; c++)
				{
					temp[r][c] = Math.abs(tables[i][r][c]/tables.length);
				}
			}
		}
		return temp;
	}
	
	public class CubicInterpolator {
		public double getValue(double[] p, double x) {
			int xi = (int) x;
			x -= xi;
			double p0 = p[Math.max(0, xi - 1)];
			double p1 = p[xi];
			double p2 = p[Math.min(p.length - 1,xi + 1)];
			double p3 = p[Math.min(p.length - 1, xi + 2)];
			return p1 + 0.5 * x * (p2 - p0 + x * (2.0 * p0 - 5.0 * p1 + 4.0 * p2 - p3 + x * (3.0 * (p1 - p2) + p3 - p0)));
		}
	}

	public class BicubicInterpolator extends CubicInterpolator {
		private double[] arr = new double[4];

		public double getValue(double[][] p, double x, double y) {
			int xi = (int) x;
			x -= xi;
			arr[0] = getValue(p[Math.max(0, xi - 1)], y);
			arr[1] = getValue(p[xi], y);
			arr[2] = getValue(p[Math.min(p.length - 1,xi + 1)], y);
			arr[3] = getValue(p[Math.min(p.length - 1, xi + 2)], y);
			return getValue(arr, x+ 1);
		}
	}

}
