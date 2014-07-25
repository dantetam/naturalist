
import processing.core.PApplet;

public class Runner extends PApplet {

	public static float width = 720;
	public static float height = 720;
	
	public static void main(String[] args)
	{
		
	}
	
	Terrain terrain;
	public void setup()
	{
		terrain = new Terrain(32,32,870);
		size(1280,720);
		background(0);
		noStroke();
	}
	
	public void draw()
	{
		background(0);
		for (double r = 0; r < terrain.terrain.length; r++)
		{
			for (double c = 0; c < terrain.terrain[0].length; c++)
			{
				fill((int)(terrain.terrain[(int)r][(int)c]*25));
				rect((int)(r/terrain.terrain.length*width),
						(int)(c/terrain.terrain[0].length*height),
						width/terrain.terrain.length + 1,
						height/terrain.terrain[0].length + 1);
			}
		}
	}
	
}
