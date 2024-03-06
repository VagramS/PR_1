package simulator.factories;

import org.json.JSONObject;

public class SelectYoungestBuilder<T> extends Builder<T>{

	public SelectYoungestBuilder(String desc) {
		super("youngest", desc);
	}

	@Override
	protected T create_instance(JSONObject data) {
		return null;
	}

}
