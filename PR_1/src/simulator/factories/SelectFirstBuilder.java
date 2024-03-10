package simulator.factories;

import org.json.JSONObject;
import simulator.model.SelectionStrategy;

public class SelectFirstBuilder extends Builder<SelectionStrategy> {

	public SelectFirstBuilder() {
		super("first", "Elige el primer animal de la region");
	}

	@Override
	protected SelectionStrategy create_instance(JSONObject data) {
		return null;
	}

}
