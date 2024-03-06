package simulator.factories;

import org.json.JSONObject;

public class SelectClosestBuilder<T> extends Builder<T> {

	public SelectClosestBuilder(String desc) {
		super("closest", desc);
	}

	@Override
	protected T create_instance(JSONObject data) {
		return null;
	}

}
