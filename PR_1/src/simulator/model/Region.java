package simulator.model;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Region implements Entity, FoodSupplier, RegionInfo
{
	protected List<Animal> animals;
	
	final void add_animal(Animal a) 
	{
		animals.add(a);
	}
	
	final void remove_animal(Animal a)
	{
		animals.remove(a);
	}
	
	final List<Animal> getAnimals()
	{
		return new ArrayList<>(animals);
	}
	
	public JSONObject as_JSON()
	{
		JSONArray animalsArray = new JSONArray();

        for (int i = 0; i < animals.size(); i++) 
            animalsArray.put(animals.get(i).as_JSON());
        
        JSONObject result = new JSONObject();
        result.put("animals", animalsArray);

        return result;
        
		//"animals": a1, a2...
	}
}
