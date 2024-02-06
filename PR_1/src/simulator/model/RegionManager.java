package simulator.model;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.json.JSONArray;
import org.json.JSONObject;

public class RegionManager implements AnimalMapView
{
	protected int _cols;
	protected int _rows;
	protected int _width;
	protected int _height;
	protected int _regionWidth;
	protected int _regionHeight;
	protected Region[][] _regions;
	protected Map<Animal, Region> _animal_region;
	
	public RegionManager(int cols, int rows, int width, int height)
	{
		this._cols = cols;
		this._rows = rows;
		this._width = width;
		this._height = height;
		this._regionWidth = width / cols;
		this._regionHeight = height / rows;
		
		this._regions = new Region[rows][cols];
		
		for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this._regions[i][j] = new DefaultRegion();
            }
        }
		
		this._animal_region = new HashMap<>();
	}
	
	void set_region(int row, int col, Region r)
	{
		
	}
	
	void register_animal(Animal a)
	{
		
	}
	
	void unregister_animal(Animal a)
	{
		
	}
	
	 void update_animal_region(Animal a)
	 {
		 
	 }
	 
	 public double get_food(Animal a, double dt)
	 {
		 
	 }
	 
	 void update_all_regions(double dt)
	 {
		 
	 }
	 
	 public List<Animal> get_animals_in_range(Animal a, Predicate<Animal> filter)
	 {
		 
	 }
	 
	 public JSONObject as_JSON()
	 {
		JSONArray regArray = new JSONArray();
		
		for(int i = 0; i < _rows; i++)
		{
			for(int j = 0; j < _cols; j++)
			{
				JSONObject region = new JSONObject();
				region.put("row", i);
				region.put("col", j);
				region.put("data", _regions[i][j].as_JSON());
				
				regArray.put(region);
			}
		}
		
		JSONObject res = new JSONObject();
        res.put("regiones", regArray);
        
        return res;
	 }

	public int get_cols() {
		return this._cols;
	}

	public int get_rows() {
		return this._rows;
	}

	public int get_width() {
		return this._width;
	}

	public int get_height() {
		return this._height;
	}

	public int get_region_width() {
		return this._regionWidth;
	}

	public int get_region_height() {
		return this._regionHeight;
	}
}
