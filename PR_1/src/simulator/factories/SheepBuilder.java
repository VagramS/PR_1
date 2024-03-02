package simulator.factories;

import org.json.JSONObject;

public class SheepBuilder<T> extends Builder<T> {

	public SheepBuilder(String desc) {
		super("sheep", desc);
	}

	@Override
	protected T create_instance(JSONObject data) {
		// TODO Auto-generated method stub
		return null;
	}

}
