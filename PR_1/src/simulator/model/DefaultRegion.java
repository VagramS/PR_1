package simulator.model;

import java.util.List;

public class DefaultRegion extends Region
{
	public DefaultRegion() {
		super();
	}

	public double get_food(Animal a, double dt) 
	{
		double _food = 0.0;
		int n = 0;
		
		for(Animal animal: _animals)
		{
			if(animal._diet == Diet.HERBIVORE)
				n++;
		}
		
		if(a._diet == Diet.HERBIVORE)
			_food = 60.0 * Math.exp (-Math.max(0, n - 5.0) * 2.0) * dt; // n es el número de animales herbívoros en la región
		else
			_food = 0.0;
		
		return _food;
	}

	public void update(double dt) {}
	
}
