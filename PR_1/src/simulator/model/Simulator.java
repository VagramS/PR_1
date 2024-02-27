package simulator.model;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.factories.Factory;

public class Simulator implements JSONable
{
	protected Factory<Animal> _animals_factory;
	protected Factory<Region> _regions_factory;
	protected RegionManager _region_mngr;
	protected List <Animal> animals;
	protected double time;
	
	public Simulator(int cols, int rows, int width, int height, Factory<Animal> animals_factory, Factory<Region> regions_factory)
	{
		this._animals_factory = animals_factory;
		this._regions_factory = regions_factory;
		this._region_mngr._cols = cols;
		this._region_mngr._rows = rows;
		this._region_mngr._width = width;
		this._region_mngr._height = height;
		time = 0.0;
	}
	
	private void set_region(int row, int col, Region r)
	{
		this._region_mngr._regions[row][col] = r;
	}
	
	public void set_region(int row, int col, JSONObject r_json) // terminar
	{
		JSONArray region_animals = r_json.getJSONArray("animals");
		Region region = new Region();
		set_region(row, col, region);
	}
	
	private void add_animal(Animal a)
	{
		this.animals.add(a);
		_region_mngr.register_animal(a);
	}
	
	public void add_animal(JSONObject a_json) // terminar
	{
		a_json.
		add_animal(A);
	}
	
	public MapInfo get_map_info()
	{
		return _region_mngr;
	}
	
	public List<? extends AnimalInfo> get_animals()
	{
		return Collections.unmodifiableList(this.animals);
	}
	
	public double get_time()
	{
		return this.time;
	}
	
	public void advance(double dt) //to check
	{
		this.time *= dt;
		
		for(int i = 0; i < animals.size(); i++)
		{
			if(animals.get(i)._state == State.DEAD)
			{
				this.animals.remove(i);
				_region_mngr.unregister_animal(animals.get(i));
			}
			animals.get(i).update(dt);
			_region_mngr.update_animal_region(animals.get(i));
			_region_mngr.update_all_regions(dt);
			
			if(animals.get(i).is_pregnant())
				add_animal(animals.get(i).deliver_baby());
		}
	}
	
	public JSONObject as_JSON()
	{
		JSONObject object = new JSONObject();
		
		object.put("time", this.time);
		object.put("state", _region_mngr.as_JSON()); // no seguro, pero debe ser asi
		
		return object;
		
//		"time": t,
//		"state": s,
	}
}
