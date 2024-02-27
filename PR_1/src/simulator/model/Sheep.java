package simulator.model;
import simulator.*;
import simulator.misc.Vector2D;

public class Sheep extends Animal
{
	protected Animal _danger_source;
	protected SelectionStrategy _danger_strategy;
	
	public Sheep(SelectionStrategy mate_strategy, SelectionStrategy danger_strategy, Vector2D pos)
	{
		super("Sheep", Diet.HERBIVORE, 40.0 , 35.0, mate_strategy, pos);
		this._danger_strategy = danger_strategy;
	}

	protected Sheep(Sheep p1, Animal p2)
	{
		super(p1, p2);
		this._danger_strategy = p1._danger_strategy;
		this._danger_strategy = null;
	}
	
	public void update(double dt) 
	{
		
		
	}

	public State get_state() {
		return this._state;
	}

	public Vector2D get_position() {
		return this._pos;
	}

	public String get_genetic_code() {
		return this._genetic_code;
	}

	public Diet get_diet() {
		return this._diet;
	}

	public double get_speed() {
		return this._speed;
	}

	public double get_sight_range() {
		return this.get_sight_range();
	}

	public double get_energy() {
		return this._energy;
	}

	public double get_age() {
		return this._age;
	}

	public Vector2D get_destination() {
		return this._dest;
	}

	public boolean is_pregnant() {
		if(this._baby == null)
			return false;
		else
			return true;
	}
}
