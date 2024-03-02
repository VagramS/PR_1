package simulator.factories;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Animal;
import simulator.model.SelectionStrategy;
import simulator.model.Wolf;

public class WolfBuilder extends Builder<Wolf> {

	public WolfBuilder(String desc) {
		super("wolf", desc);
	}

	@Override
	protected Wolf create_instance(JSONObject data) 
	{
		SelectionStrategy mate_strategy = null;
		SelectionStrategy hunting_strategy = null;
		
		if(data.has("mate_strategy"))
			mate_strategy = data.getString("mate_strategy")
		else
			throw new IllegalArgumentException("mate_strategy no pertenece el archivo JSON");
		
		if(data.has("hunt_strategy"))
			hunting_strategy = data.get("hunt_strategy");
		else
			throw new IllegalArgumentException("hunt_strategy no pertenece el archivo JSON");

		JSONObject posObject = data.getJSONObject("pos");
        Vector2D pos = new Vector2D(posObject.getDouble("x"), posObject.getDouble("y"));
        
		return new Wolf(mate_strategy, hunting_strategy, pos);
	}

}
