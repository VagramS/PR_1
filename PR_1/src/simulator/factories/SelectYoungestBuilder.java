package simulator.factories;

import org.json.JSONObject;

public class SelectYoungestBuilder<T> extends Builder<T> {

	public SelectYoungestBuilder() {
		super("youngest", "Elige el animal mas joven");
	}

	@Override
	protected T create_instance(JSONObject data) {
		return null;
	}

}
