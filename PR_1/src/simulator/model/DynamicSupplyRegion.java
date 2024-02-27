package simulator.model;

import java.util.Random;

public class DynamicSupplyRegion extends Region
{
	protected double _food;
	protected double _factor;
	protected Random rand = new Random();
	
	public DynamicSupplyRegion(double food, double factor)
	{
		this._food = food;
		this._factor = factor;
	}

	public void update(double dt) 
	{
		if (rand.nextDouble() < 0.5) 
            _food += dt * _factor;
	}

	public double get_food(Animal a, double dt) 
	{
		int n = 0;
		
		for(Animal animal: _animals)
			if(animal._diet == Diet.HERBIVORE)
				n++;
		
		if(a._diet == Diet.HERBIVORE)
			this._food = Math.min(_food, 60.0 * Math.exp(-Math.max(0, n - 5.0) * 2.0) * dt);
		else
			this._food = 0.0;
		
		return this._food;
	}
}
