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
		this._danger_source = null;
	}

	private Double maintain_in_range(double value, double lower_limit, double upper_limit) {
		if (value > upper_limit)
			value = upper_limit;
		else if (value < lower_limit)
			value = lower_limit;
		return value;
	}

	private boolean isInSightRange(Animal target) {
		return _pos.distanceTo(target.get_position()) <= _sight_range;
	}

	private Animal findNewDangerSource() {
		List<Animal> dangers = _region_mngr.get_animals_in_range(this, animal -> animal.get_diet() == Diet.CARNIVORE);
		return _danger_source = _danger_strategy.select(this, dangers);
	}

	private void handleNormalState(double dt) {
		if (_pos.distanceTo(_dest) < 8.0) {
			double x = Utils._rand.nextDouble(0, _region_mngr.get_width());
			double y = Utils._rand.nextDouble(0, _region_mngr.get_height());
			_dest = new Vector2D(x, y);
		}

		move(_speed * dt * Math.exp((_energy - 100.0) * 0.007));

		_age += dt;
		_energy = maintain_in_range(_energy - 20.0 * dt, 0.0, 100.0);
		_desire = maintain_in_range(_desire + 40.0 * dt, 0.0, 100.0);

		if (this._danger_source == null)
			findNewDangerSource();

		if (this._danger_source == null && this._desire > 65.0)
			_state = State.MATE;
		else if (this._danger_source != null)
			_state = State.DANGER;
	}

	private void handleDangerState(double dt) {
		if (_danger_source != null && (_danger_source.get_state() == State.DEAD || !isInSightRange(_danger_source)))
			_danger_source = null;

		if (_danger_source == null)
			handleNormalState(dt);
		else {
			Vector2D escapeDirection = _pos.minus(_danger_source.get_position()).direction();
			_dest = _pos.plus(escapeDirection.scale(100));

			move(2.0 * _speed * dt * Math.exp((_energy - 100.0) * 0.007));

			_age += dt;
			_energy = maintain_in_range(_energy - 20.0 * dt, 0.0, 100.0);
			_desire = maintain_in_range(_desire + 40.0 * dt, 0.0, 100.0);
		}

		if (_danger_source == null || !isInSightRange(_danger_source)) {
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
		if (_mate_target != null && (_mate_target.get_state() == State.DEAD
				|| _pos.distanceTo(_mate_target.get_position()) > _sight_range))
			_mate_target = null;

		if (_mate_target == null) {
			List<Animal> potentialMates = _region_mngr.get_animals_in_range(this,
					animal -> animal.get_genetic_code().equals(this._genetic_code));
			_mate_target = _mate_strategy.select(this, potentialMates);

			if (_mate_target == null)
				handleNormalState(dt);
		}
		if (_mate_target != null) {
			_dest = _mate_target.get_position();

			move(2.0 * _speed * dt * Math.exp((_energy - 100.0) * 0.007));

			_age += dt;
			_energy = maintain_in_range(_energy - (20.0 * 1.2 * dt), 0.0, 100.0);
			_desire = maintain_in_range(_desire + 40.0 * dt, 0.0, 100.0);

			if (_pos.distanceTo(_mate_target.get_position()) < 8.0) {
				_desire = 0.0;
				_mate_target._desire = 0.0;

				if (_baby == null && Utils._rand.nextDouble() < 0.9)
					this._baby = new Sheep(this, _mate_target);
				_mate_target = null;
			}
		}

		if (this._danger_source == null)
			findNewDangerSource();

		if (_danger_source != null)
			_state = State.DANGER;
		else if (this._danger_source == null && _desire < 65.0)
			_state = State.NORMAL;
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

		if (this._energy == 0.0 || this._age > 8.0)
			this._state = State.DEAD;
		else {
			double food = _region_mngr.get_food(this, dt);
			this._energy = maintain_in_range(this._energy + food, 0.0, 100.0);

			switch (this._state) {
			case NORMAL:
				handleNormalState(dt);
				break;
			case DANGER:
				handleDangerState(dt);
				break;
			case MATE:
				handleMateState(dt);
				break;
			}
			if (this._pos.getX() < 0 || this._pos.getX() >= _region_mngr.get_width() || this._pos.getY() < 0
					|| this._pos.getY() >= _region_mngr.get_height())
				adjustPosition();
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
		return this._sight_range;
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
