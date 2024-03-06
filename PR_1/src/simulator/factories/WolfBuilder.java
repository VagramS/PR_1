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
		SelectionStrategy hunt_strategy = null;
		
//		if(data.has("mate_strategy"))
//			mate_strategy = data.getString("mate_strategy");
//		else
//			mate_strategy = SelectionStrategy.SelectFirst();
//		
//		if(data.has("hunt_strategy"))
//			hunt_strategy = data.get("hunt_strategy");
//		else
//			hunt_strategy = SelectionStrategy.SelectFirst();

		JSONObject posObject = data.getJSONObject("pos");
        Vector2D pos = new Vector2D(posObject.getDouble("x"), posObject.getDouble("y"));
        
		return new Wolf(mate_strategy, hunt_strategy, pos);
	}

}
