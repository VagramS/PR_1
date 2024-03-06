package simulator.factories;
import org.json.JSONObject;
import simulator.model.DefaultRegion;

public class DefaultRegionBuilder extends Builder<DefaultRegion> {

	public DefaultRegionBuilder(String desc) {
		super("default", desc);
	}

	@Override
	protected DefaultRegion create_instance(JSONObject data) {
		return new DefaultRegion();
	}

}
