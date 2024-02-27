package simulator.control;

import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.model.Simulator;

public class Controller
{
	protected Simulator _sim;
		
	public Controller(Simulator sim)
	{
		this._sim = sim;
	}
		
	public void load_data(JSONObject data)
	{
		if(data.has("regions"))
		{
			JSONArray regions = data.getJSONArray("regions");
			for(int i = 0; i < regions.length(); i++)
			{
				JSONObject region = regions.getJSONObject(i);
				JSONArray rowRange = region.getJSONArray("row");
		        JSONArray colRange = region.getJSONArray("col");
		        JSONObject spec = region.getJSONObject("spec");
		        
		        for(int r = rowRange.getInt(0); r <= rowRange.getInt(1); r++)
		        {
		        	for(int c = colRange.getInt(0); c <= colRange.getInt(1); c++)
		        	{
		        		_sim.set_region(r, c, spec);
		        	}
		        }
			}
		}
		
		if(data.has("animals"))
		{
			JSONArray animals = data.getJSONArray("animals");
			for(int i = 0; i < animals.length(); i++)
			{
				JSONObject animal = animals.getJSONObject(i);
				int amount = animal.getInt("amount");
				JSONObject spec = animal.getJSONObject("spec");
				
				for(int j = 0; j < amount; j++)
					_sim.add_animal(spec);
			}
		}
	}
		
	public void run(double t, double dt, boolean sv, OutputStream out)
	{
		JSONObject init_state =_sim.as_JSON();
		while(_sim.get_time()> t)
		{
			_sim.advance(dt);

		}
		JSONObject final_state =_sim.as_JSON();
		
		JSONObject res = new JSONObject();
		res.put("in", init_state);
		res.put("out", final_state);
		
		if(sv)
		{
			try
			{
				PrintStream p = new PrintStream(out);
				p.println(res.toString(4));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
}
