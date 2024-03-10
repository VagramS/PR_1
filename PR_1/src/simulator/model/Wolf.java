package simulator.model;

import java.util.List;

import simulator.misc.Utils;
import simulator.misc.Vector2D;

public class Wolf extends Animal {
	protected Animal _hunt_target;
	protected SelectionStrategy _hunting_strategy;

	public Wolf(SelectionStrategy mate_strategy, SelectionStrategy hunting_strategy, Vector2D pos) {
		super("Wolf", Diet.CARNIVORE, 50.0, 60.0, mate_strategy, pos);
		_hunting_strategy = hunting_strategy;
	}

	protected Wolf(Wolf p1, Animal p2) {
		super(p1, p2);
		_hunting_strategy = p1._hunting_strategy;
		_hunt_target = null;
	}

	private Double maintain_in_range(double value, double lower_limit, double upper_limit) {
		if (value > upper_limit)
			value = upper_limit;
		else if (value < lower_limit)
			value = lower_limit;
		return value;
	}

	private void CheckIfDead() {
		if (this._energy == 0.0 || this._age > 14.0)
			this._state = State.DEAD;
	}

	private void handleNormalState(double dt) {
		// 1.1.
		if (_pos.distanceTo(_dest) < 8.0) {
			double newX = Utils.nextDouble(0, _region_mngr.get_width() - 1);
			double newY = Utils.nextDouble(0, _region_mngr.get_height() - 1);
			_dest = new Vector2D(newX, newY);
		}

		// 1.2.
		move(_speed * dt * Math.exp((_energy - 100.0) * 0.007));

		// 1.3.
		_age += dt;

		// 1.4.
		_energy = maintain_in_range(_energy - 18.0 * dt, 0.0, 100.0);

		// 1.5.
		_desire = maintain_in_range(_desire + 30.0 * dt, 0.0, 100.0);

		// 2.
		if (_energy < 50.0) {
			_state = State.HUNGER;
			_mate_target = null;
		} else if (_energy >= 50.0 && _desire > 65.0) {
			_state = State.MATE;
			_hunt_target = null;
		}
	}

	private void handleHungerState(double dt) {
		if (_hunt_target == null || _hunt_target.get_state() == State.DEAD
				|| _pos.distanceTo(_hunt_target.get_position()) > _sight_range) {
			List<Animal> potentialTargets = _region_mngr.get_animals_in_range(this,
					animal -> animal.get_diet() == Diet.HERBIVORE);
			_hunt_target = _hunting_strategy.select(this, potentialTargets);
		}

		if (_hunt_target == null)
			handleNormalState(dt);
		else {
			// 2.1.
			_dest = _hunt_target.get_position();

			// 2.2.
			move(3.0 * _speed * dt * Math.exp((_energy - 100.0) * 0.007));

			// 2.3.
			_age += dt;

			// 2.4.
			_energy = maintain_in_range(_energy - (18.0 * 1.2 * dt), 0.0, 100.0);

			// 2.5.
			_desire = maintain_in_range(_desire + 30.0 * dt, 0.0, 100.0);

			// 2.6.
			if (_pos.distanceTo(_hunt_target.get_position()) < 8.0) {
				// Successful hunt
				_hunt_target._state = State.DEAD;
				_hunt_target = null;
				_energy = maintain_in_range(_energy + 50.0, 0.0, 100.0);
			}
		}

		// 3.
		if (_energy > 50.0) {
			_state = (_desire < 65.0) ? State.NORMAL : State.MATE;
			_mate_target = null; // Clear mating target when leaving HUNGER state
		}
		// If energy is less than 50.0, remain in HUNGER state
	}

	private void handleMateState(double dt) {
		// 1.
		if (_mate_target != null && (_mate_target.get_state() == State.DEAD
				|| _pos.distanceTo(_mate_target.get_position()) > _sight_range))
			_mate_target = null;

		// 2.
		if (_mate_target == null) {
			List<Animal> potentialMates = _region_mngr.get_animals_in_range(this,
					animal -> animal.get_genetic_code().equals(this._genetic_code));
			_mate_target = _mate_strategy.select(this, potentialMates);

			if (_mate_target == null)
				handleNormalState(dt);
		}

		if (_mate_target != null) {
			// 2.1.
			_dest = _mate_target.get_position();

			// 2.2.
			move(3.0 * _speed * dt * Math.exp((_energy - 100.0) * 0.007));

			// 2.3.
			_age += dt;
			_energy = maintain_in_range(_energy - (18.0 * 1.2 * dt), 0.0, 100.0);

			// 2.4.
			_desire = maintain_in_range(_desire + 30.0 * dt, 0.0, 100.0);

			// 2.5.
			if (_pos.distanceTo(_mate_target.get_position()) < 8.0) {
				_desire = 0.0;
				_mate_target._desire = 0.0;

				// 2.6.
				if (_baby == null && Utils._rand.nextDouble() < 0.9)
					_baby = new Wolf(this, _mate_target);

				_energy = maintain_in_range(_energy - 10.0, 0.0, 100.0);
				_mate_target = null;
			}
		}

		// 3.
		if (_energy < 50.0)
			_state = State.HUNGER;
		else if (_desire < 65.0)
			_state = State.NORMAL;

		// Keep _state as MATE if none of the above conditions are met
	}

	private void adjustPosition() {
		double x = maintain_in_range(this._pos.getX(), 0.0, _region_mngr.get_width() - 1);
		double y = maintain_in_range(this._pos.getY(), 0.0, _region_mngr.get_height() - 1);
		this._pos = new Vector2D(x, y);
		this._state = State.NORMAL;
	}

	public void update(double dt) {
		if (this._state == State.DEAD)
			return;

		CheckIfDead();

		double food = _region_mngr.get_food(this, dt);
		this._energy = maintain_in_range(this._energy + food, 0.0, 100.0);

		switch (this._state) {
		case NORMAL:
			handleNormalState(dt);
			break;
		case HUNGER:
			handleHungerState(dt);
			break;
		case MATE:
			handleMateState(dt);
			break;
		}
		adjustPosition();
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
		if (this._baby == null)
			return false;
		else
			return true;
	}
}
