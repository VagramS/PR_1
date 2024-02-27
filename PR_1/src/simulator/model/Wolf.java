package simulator.model;
import simulator.*;
import simulator.misc.Utils;
import simulator.misc.Vector2D;

public class Wolf extends Animal
{
	protected Animal _hunt_target;
	protected SelectionStrategy _hunting_strategy;
	
	public Wolf(SelectionStrategy mate_strategy, SelectionStrategy hunting_strategy, Vector2D pos)
	{
		super("Wolf", Diet.CARNIVORE, 50.0, 60.0, mate_strategy, pos);
		_hunting_strategy = hunting_strategy;
	}
	
	protected Wolf(Wolf p1, Animal p2)
	{
		super(p1,p2);
		_hunting_strategy = p1._hunting_strategy;
		_hunt_target = null;
	}
	
	public void update(double dt) 
	{
		if(this.get_state() != State.DEAD)
		{
			if(this._pos.getX() > this._region_mngr.get_width() || this._pos.getX() < 0 || this._pos.getY() > this._region_mngr.get_height() || this._pos.getY() < 0)
			{
				 // ajustar la pos
				this._state = State.NORMAL;
			}
			else if(this.get_energy() == 0 || this.get_age() > 14.0)
				this._state = State.DEAD;
			
			this._energy +=	_region_mngr.get_food(this, dt);
			if(this._energy > 100.0)
				this._energy = 100.0;
			else if(this._energy < 0.0)
				this._energy = 0.0;
			
			
			if(this._state == State.NORMAL)
			{
				if(this._pos.distanceTo(_dest) < 8.0)
				{
					double x = Utils._rand.nextDouble(800);
					double y = Utils._rand.nextDouble(600);
					Vector2D dest = new Vector2D(x, y);
					this._dest = dest;
				}
				
				this.move(this._speed * dt * Math.exp((this._energy - 100.0) * 0.007));
				this._age += dt;
				this._energy -= 18.0 * dt;
				
				if(this._energy > 100.0)
					this._energy = 100.0;
				else if(this._energy < 0.0)
					this._energy = 0.0;
				
				this._desire += 30.0 * dt;
				
				if(this._desire > 100.0)
					this._desire = 100.0;
				else if(this._desire < 0.0)
					this._desire = 0.0;
				
				if(this._energy < 50.0)
					this._state = State.HUNGER;
				else if(this._energy >= 50.0 && this._desire > 65.0)
					this._state = State.MATE;
				
			}
			else if(this._state == State.HUNGER)
			{
//				Si _hunt_target es null, o no es null pero su estado es DEAD o está fuera del campo visual, buscar
//				otro animal para cazarlo
				
				
			}
			else if(this._state == State.MATE)
			{
				
			}
		}
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
