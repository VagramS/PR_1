package simulator.model;
import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.AnimalInfo;
import simulator.model.AnimalMapView;
import simulator.model.Diet;
import simulator.model.Entity;
import simulator.model.SelectionStrategy;
import simulator.model.State;

public abstract class Animal implements Entity, AnimalInfo
{
	protected String _genetic_code;
	protected Diet _diet;
	protected State _state;
	protected Vector2D _pos;
	protected Vector2D _dest;
	protected Double _energy;
	protected Double _speed;
	protected Double _age;
	protected Double _desire;
	protected Double _sight_range;	
	protected Animal _mate_target;
	protected Animal _baby;
	protected AnimalMapView _region_mngr;
	protected SelectionStrategy _mate_strategy;
	
	protected Animal(String genetic_code, Diet diet, double sight_range, double init_speed, SelectionStrategy mate_strategy, Vector2D pos)
	{
		if(genetic_code == null || genetic_code == "" || sight_range < 0 || init_speed < 0 || mate_strategy == null)
            throw new IllegalArgumentException("Argumentos inválidos para el constructor de Animal");
		
		this._genetic_code = genetic_code;
		this._diet = diet;
		this._sight_range = sight_range;
		this._speed = Utils.get_randomized_parameter(init_speed, 0.1);
		this._mate_strategy = mate_strategy;
		this._pos = pos;
		
		this._state = State.NORMAL;
	    this._energy = 100.0;
	    this._desire = 0.0;
	    this._dest = null;
	    this._mate_target = null;
	    this._baby = null;
	    this._region_mngr = null;
	}
	
	protected Animal(Animal p1, Animal p2)
	{
		this._dest = null;
		this._baby = null;
		this._mate_target = null;
		this._region_mngr = null;
		this._state = State.NORMAL;
		this._desire = 0.0;
		this._genetic_code = p1._genetic_code;
		this._diet = p1._diet;
		this._energy = (p1._energy + p2._energy) / 2;
		this._mate_strategy = p2._mate_strategy;
		this._pos = p1.get_position().plus(Vector2D.get_random_vector(-1,1).scale(60.0*(Utils._rand.nextGaussian()+1)));
		this._sight_range = Utils.get_randomized_parameter((p1.get_sight_range() + p2.get_sight_range())/2, 0.2);
		this._speed = Utils.get_randomized_parameter((p1.get_speed() + p2.get_speed())/2, 0.2);
	}
	
	void init(AnimalMapView reg_mngr)
	{
		this._region_mngr = reg_mngr;
		
		if(_pos == null)
		{
			double x = 0 + (double)(Math.random() * (((_region_mngr.get_width() - 1) - 0) + 1));
			double y = 0 + (double)(Math.random() * (((_region_mngr.get_height() - 1) - 0) + 1));
			Vector2D pos = new Vector2D(x, y);
			_pos = pos;
		}
		else
		{
			double x = Utils._rand.nextDouble(800);
			double y = Utils._rand.nextDouble(600);
			

			while (x >= _region_mngr.get_width()) 
				x = (x - _region_mngr.get_width());
			while (x < 0) 
				x = (x + _region_mngr.get_width());
			
			while (y >= _region_mngr.get_height()) 
				y = (y - _region_mngr.get_height());
			while (y < 0) 
				y = (y + _region_mngr.get_height());
			
		}

		double _x = Utils._rand.nextDouble(800); // cambiar valores luego
		double _y = Utils._rand.nextDouble(600);
		Vector2D dest = new Vector2D(_x, _y);
		_dest = dest;
	}
	
	Animal deliver_baby()
	{
		Animal baby = this._baby;
		this._baby  = null;
		
		return baby; 
	}
	
	protected void move(double speed)
	{
		_pos = _pos.plus(_dest.minus(_pos).direction().scale(speed));
	}
	
	public JSONObject as_JSON()
	{
		JSONObject obj = new JSONObject();
		JSONArray posArray = this._pos.asJSONArray();
		
		obj.put("pos", posArray);
		obj.put("gcode", _genetic_code);
		obj.put("diet", _diet);
		obj.put("state", _state);
		
		return obj;
		
//		"pos": [28.90696391797469,22.009772194487613],
//		"gcode": "Sheep",
//		"diet": "HERBIVORE",
//		"state": "NORMAL"
	}
}
