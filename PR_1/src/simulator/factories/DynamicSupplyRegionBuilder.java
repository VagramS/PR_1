package simulator.factories;

import org.json.JSONObject;

public class DynamicSupplyRegionBuilder<T> extends Builder<T> {

	public DynamicSupplyRegionBuilder(String desc) {
		super("default", desc);
	}

	@Override
	protected T create_instance(JSONObject data) {
		// TODO Auto-generated method stub
		return null;
	}

}
