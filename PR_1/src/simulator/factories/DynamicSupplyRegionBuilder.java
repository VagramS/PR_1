package simulator.factories;

import org.json.JSONObject;

import simulator.model.DynamicSupplyRegion;
import simulator.model.Region;

public class DynamicSupplyRegionBuilder extends Builder<Region> {

	public DynamicSupplyRegionBuilder() {
		super("default", "El region que tiene su factor y valor de comida");
	}

	@Override
	protected Region create_instance(JSONObject data) {
		double factor = 2.0;
		double food = 1000.0;

		if (data.has("factor"))
			factor = data.getDouble("factor");

		if (data.has("food"))
			food = data.getDouble("food");

		return new DynamicSupplyRegion(food, factor);
	}
}
