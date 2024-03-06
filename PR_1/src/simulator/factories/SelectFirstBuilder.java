package simulator.factories;

import org.json.JSONObject;

public class SelectFirstBuilder<T> extends Builder<T>{

	public SelectFirstBuilder(String desc) {
		super("first", desc);
	}

	@Override
	protected T create_instance(JSONObject data){
		return null;
	}

}
