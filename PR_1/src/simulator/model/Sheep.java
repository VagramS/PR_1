package simulator.model;

import java.util.List;

import simulator.misc.Utils;
import simulator.misc.Vector2D;

public class Sheep extends Animal {
	protected Animal _danger_source;
	protected SelectionStrategy _danger_strategy;

	public Sheep(SelectionStrategy mate_strategy, SelectionStrategy danger_strategy, Vector2D pos) {
		super("Sheep", Diet.HERBIVORE, 40.0, 35.0, mate_strategy, pos);
		this._danger_strategy = danger_strategy;
	}

	protected Sheep(Sheep p1, Animal p2) {
		super(p1, p2);
		this._danger_strategy = p1._danger_strategy;
		this._danger_strategy = null;
	}

	private Double maintain_in_range(double value, double lower_limit, double upper_limit) {
		if (value > upper_limit)
			value = upper_limit;
		else if (value < lower_limit)
			value = lower_limit;
		return value;
	}

	private void CheckIfDead() {
		if (this._energy == 0.0 || this._age > 8.0)
			this._state = State.DEAD;
	}

	private boolean isInSightRange(Animal target) {
		return _pos.distanceTo(target.get_position()) <= _sight_range;
	}

	private Animal findNewDangerSource() {
		return null;
	}

	private void handleNormalState(double dt) {
		// 1.1
		if (_pos.distanceTo(_dest) < 8.0) {
			double x = Utils.nextDouble(0, _region_mngr.get_width());
			double y = Utils.nextDouble(0, _region_mngr.get_height());
			_dest = new Vector2D(x, y);
		}

		// 1.2
		move(_speed * dt);

		// 1.3
		_age += dt;
		_energy = maintain_in_range(_energy - 10.0 * dt, 0.0, 100.0);
		_desire = maintain_in_range(_desire + 20.0 * dt, 0.0, 100.0);

		// 1.4
		if (_energy < 50.0) {
			_state = State.HUNGER;
		} else if (_desire > 65.0) {
			_state = State.MATE;
		}
	}

	private void handleDangerState(double dt) {
		// 1.
		if (_danger_source != null && (_danger_source.get_state() == State.DEAD || !isInSightRange(_danger_source)))
			_danger_source = null;

		// 2.
		if (_danger_source == null)
			handleNormalState(dt);
		else {
			// 2.1
			Vector2D escapeDirection = _pos.minus(_danger_source.get_position()).direction();
			_dest = _pos.plus(escapeDirection.scale(100));

			// 2.2
			move(2.0 * _speed * dt);

			// 2.3
			_age += dt;
			_energy = maintain_in_range(_energy - 24.0 * dt, 0.0, 100.0);
			_desire = maintain_in_range(_desire + 40.0 * dt, 0.0, 100.0);
		}

		// 3.
		if (_danger_source == null || !isInSightRange(_danger_source)) {
			// 3.1
			_danger_source = findNewDangerSource();
			if (_danger_source == null) {
				if (_desire < 65.0)
					_state = State.NORMAL;
				else
					_state = State.MATE;
			}
		}
	}

	private void handleMateState(double dt) {
		List<Animal> potentialMates = _region_mngr.get_animals_in_range(this,
				animal -> animal instanceof Sheep && !animal.equals(this));
		_mate_target = _mate_strategy.select(this, potentialMates);

		if (_mate_target != null) {
			_dest = _mate_target.get_position();
			move(_speed * dt);

			if (this.get_position().distanceTo(_mate_target.get_position()) < 8.0)
				_desire = 0.0;
		} else
			handleNormalState(dt);
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
			handleDangerState(dt);
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
