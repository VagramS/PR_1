package simulator.factories;

import org.json.JSONObject;

public class DefaultRegionBuilder<T> extends Builder<T> {

	public DefaultRegionBuilder(String desc) {
		super("default", desc);
	}

	@Override
	protected T create_instance(JSONObject data) {
		// TODO Auto-generated method stub
		return null;
	}

}
