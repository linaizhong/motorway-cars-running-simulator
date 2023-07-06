package com.kuai.traffic.simulator.running;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.List;

import javax.swing.Timer;

import com.kuai.traffic.simulator.TrafficSimulationFrame;
import com.kuai.traffic.simulator.model.Coordinate;
import com.kuai.traffic.simulator.model.Intersection;
import com.kuai.traffic.simulator.model.Position;
import com.kuai.traffic.simulator.model.impl.DefaultVehicle;
import com.kuai.traffic.simulator.util.Utilities;

public class RunningVehicle extends DefaultVehicle implements ActionListener {
	public enum VehicleDirection {
		LEFT, RIGHT, UP, DOWN, DOWN_LEFT, DOWN_RIGHT, RIGHT_UP, RIGHT_DOWN, LEFT_UP, LEFT_DOWN, UP_LEFT, UP_RIGHT
	};

	private RunningLane runningLane;
	private RunningTrafficLight trafficLight;
	private ImageObserver imgObserve;

	private float MAX_SPEED; // Max speed of vehicle

	// private float mCurAngle; // Current angle of vehicle relative to normal

	private Coordinate vehiclePosition; // the position of vehicle
	private Timer mTimer;

	private VehicleBehaviour currentState; // Stores current vehicle state
	private VehicleDirection currentDirection; // Stores current vehicle
												// direction
	private VehicleBehaviour nextBehaviour;

	private AffineTransform trans; // The transformation of vehicle

	private boolean passedTrafficLight;

	// Active Cruise Control variables
	private RunningVehicle vehicleAhead;
	private boolean turnDecided = false;

	private Intersection intersection;
	private Coordinate upLeftStart;
	private Coordinate upLeftMiddle;
	private Coordinate upLeftEnd;
	private Coordinate downRightStart;
	private Coordinate downRightMiddle;
	private Coordinate downRightEnd;
	private Coordinate leftDownStart;
	private Coordinate leftDownMiddle;
	private Coordinate leftDownEnd;
	private Coordinate rightUpStart;
	private Coordinate rightUpMiddle;
	private Coordinate rightUpEnd;

	public RunningVehicle(RunningLane rl, VehicleBehaviour nextBehaviour, VehicleDirection initDirection,
			RunningTrafficLight tl, ImageObserver imObs, RunningVehicle vAhead, int index) {
		super();
		setId(String.valueOf(System.currentTimeMillis()));
		setDriver("Alan");

		this.runningLane = rl;
		this.vehicleAhead = vAhead;
		this.nextBehaviour = nextBehaviour;
		this.currentState = nextBehaviour;
		this.currentDirection = initDirection;
		this.imgObserve = imObs;
		this.trafficLight = tl;

		initIntersection();
		init(rl, vAhead);

		this.vehiclePosition = coordinate;
		this.MAX_SPEED = velocity;

		trans = new AffineTransform();
		trans.setToTranslation(vehiclePosition.getX(), vehiclePosition.getY());
		trans.rotate(Math.toRadians(angle), mImage.getWidth(imgObserve) / 2, mImage.getHeight(imgObserve) / 2);

		mTimer = new Timer(10, this);
		mTimer.start();
	}
	
	private void initIntersection() {
		intersection = TrafficSimulationFrame.getInstance().getIntersectionById("gwh-russel_st");
		if (intersection != null) {
			List<Position> positions = intersection.getPosition();
			int sx = positions.get(0).getCoordinate().getX();
			int sy = positions.get(0).getCoordinate().getY();
			int ex = positions.get(1).getCoordinate().getX();
			int ey = positions.get(1).getCoordinate().getY();
			int dx = (ex - sx) / 4;
			int dy = (ey - sy) / 4;
			
			upLeftStart = Utilities.buildCoordinate(sx + dx, ey);
			upLeftEnd = Utilities.buildCoordinate(sx, ey - dy);
			upLeftMiddle = Utilities.buildCoordinate((upLeftStart.getX() + upLeftEnd.getX()) / 2, (upLeftStart.getY() + upLeftEnd.getY()) / 2);
			
			downRightStart = Utilities.buildCoordinate(ex - dx, sy);
			downRightEnd = Utilities.buildCoordinate(ex, sy + dy);
			downRightMiddle = Utilities.buildCoordinate((downRightStart.getX() + downRightEnd.getX()) / 2, (downRightStart.getY() + downRightEnd.getY()) / 2);
			
			leftDownStart = Utilities.buildCoordinate(ex, ey - dy);
			leftDownEnd = Utilities.buildCoordinate(ex - dx, ey);
			leftDownMiddle = Utilities.buildCoordinate((leftDownStart.getX() + leftDownEnd.getX()) / 2, (leftDownStart.getY() + leftDownEnd.getY()) / 2);
			
			rightUpStart = Utilities.buildCoordinate(sx, sy + dy);
			rightUpEnd = Utilities.buildCoordinate(sx + dx, sy);
			rightUpMiddle = Utilities.buildCoordinate((rightUpStart.getX() + rightUpEnd.getX()) / 2, (rightUpStart.getY() + rightUpEnd.getY()) / 2);
		}
	}

	public void stop() {
		if (mTimer != null) {
			mTimer.stop();
			mTimer = null;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		vehicleAhead = runningLane.getVehicleAhead(this);

		nextBehaviour = decideBehaviour();
		switch (nextBehaviour) {
		case GO_STRAIGHT:
			move();
			break;
		case BRAKE:
			brake();
			break;
		case SLOW_DOWN:
			slowDown();
			move();
			break;
		case SPEED_UP:
			speedUp();
			move();
			break;
		case CHANGE_LANE:
			changeLane();
			move();
			break;
		case OVERTAKE:
			overtake();
			move();
			break;
		case TURN_LEFT:
			this.trafficLight = null;
			processTurnLeft();
			break;
		case TURN_RIGHT:
			this.trafficLight = null;
			processTurnRight();
			break;
		default:
			break;
		}
	}

	private void move() {
		if(Math.abs(velocity) < 2) {
			velocity = 2;
		}

		switch (currentDirection) {
		case LEFT:
			processMoveX();
			break;
		case RIGHT:
			processMoveX();
			break;
		case UP:
			processMoveY();
			break;
		case DOWN:
			processMoveY();
			break;
		default:
			break;
		}
	}

	private void slowDown() {
		velocity *= 0.8;
	}

	private void speedUp() {
		velocity *= 1.5;
	}

	private void brake() {
		velocity = 0;
	}

	private void changeLane() {

	}

	private void overtake() {

	}

	private void processMoveX() {
		List<Coordinate> coordinates = runningLane.getLane().getCoordinate();

		if (currentDirection == VehicleDirection.LEFT) {
			angle = 180;
			if (velocity > 0) {
				velocity *= -1;
				if(Math.abs(velocity) * 1.5 < MAX_SPEED) {
					speedUp();
				}
			}
		} else if (currentDirection == VehicleDirection.RIGHT) {
			angle = 0;
			if (velocity < 0) {
				velocity *= -1;
				if(Math.abs(velocity) * 1.5 < MAX_SPEED) {
					speedUp();
				}
			}
		}

		vehiclePosition.setX(vehiclePosition.getX() + (int) velocity);
		if (currentDirection == VehicleDirection.LEFT) {
			int dy = coordinates.get(0).getY() - coordinates.get(1).getY();
			int dx = coordinates.get(0).getX() - coordinates.get(1).getX();
			double rate = (float) dy / (float) dx;
			int rx = vehiclePosition.getX() - coordinates.get(1).getX();
			int ry = (int) ((float) rx * rate);
			vehiclePosition.setY(coordinates.get(1).getY() + ry - (bgiWidth / 2));
		} else if (currentDirection == VehicleDirection.RIGHT) {
			int dy = coordinates.get(1).getY() - coordinates.get(0).getY();
			int dx = coordinates.get(1).getX() - coordinates.get(0).getX();
			double rate = (float) dy / (float) dx;
			int rx = vehiclePosition.getX() - coordinates.get(0).getX();
			int ry = (int) ((float) rx * rate);
			vehiclePosition.setY(coordinates.get(0).getY() + ry - (bgiWidth / 2));
		}

		trans.setToTranslation(vehiclePosition.getX(), vehiclePosition.getY());
		trans.rotate(Math.toRadians(angle), mImage.getWidth(imgObserve) / 2, mImage.getHeight(imgObserve) / 2);
	}

	private void processMoveY() {
		switch (currentDirection) {
		case UP:
			angle = -90;
			if (velocity > 0) {
				velocity *= -1;
				if(Math.abs(velocity) * 1.5 < MAX_SPEED) {
					speedUp();
				}
			}
			break;
		case DOWN:
			angle = 90;
			if (velocity < 0) {
				velocity *= -1;
				if(Math.abs(velocity) * 1.5 < MAX_SPEED) {
					speedUp();
				}
			}
			break;
		default:
			break;
		}

		vehiclePosition.setY(vehiclePosition.getY() + (int) velocity);
		trans.setToTranslation(vehiclePosition.getX(), vehiclePosition.getY());
		trans.rotate(Math.toRadians(angle), mImage.getWidth(imgObserve) / 2, mImage.getHeight(imgObserve) / 2);
	}

	private void processTurnLeft() {
		switch (currentDirection) {
		case UP:
			if (this.vehiclePosition.getY() > UP_LEFT.getY()) {
				// Slow down
				if(Math.abs(velocity) > 2) {
					slowDown();
				}
				
				this.vehiclePosition.setY(this.vehiclePosition.getY() - Math.abs((int) velocity));

				this.trans.setToTranslation(vehiclePosition.getX(), vehiclePosition.getY());
				this.trans.rotate(Math.toRadians(angle), mImage.getWidth(imgObserve) / 2,
						mImage.getHeight(imgObserve) / 2);
			} else {
				if (angle == -135) {
					angle = 180;
					currentDirection = VehicleDirection.LEFT;
					nextBehaviour = VehicleBehaviour.GO_STRAIGHT;
					currentState = VehicleBehaviour.GO_STRAIGHT;
					turnDecided = false;
					passedTrafficLight = true;
					velocity = 2;

					// Change traffic light

					this.vehiclePosition.setX(upLeftEnd.getX());
					this.vehiclePosition.setY(upLeftEnd.getY());

					// change this to another road
					runningLane.moveVehicleBetweenRoads(this, "gwh-russel_st");
				} else {
					// steerTowards(-180, 40);
					angle = -135;

					this.vehiclePosition.setX(upLeftMiddle.getX());
					this.vehiclePosition.setY(upLeftMiddle.getY());

					this.trans.setToTranslation(vehiclePosition.getX(), vehiclePosition.getY());
					this.trans.rotate(Math.toRadians(angle), mImage.getWidth(imgObserve) / 2,
							mImage.getHeight(imgObserve) / 2);
				}
			}

			break;
		case DOWN:
			if (this.vehiclePosition.getY() < DOWN_RIGHT.getY()) {
				// Slow down
				if(Math.abs(velocity) > 2) {
					slowDown();
				}
				
				this.vehiclePosition.setY(this.vehiclePosition.getY() + Math.abs((int) velocity));

				this.trans.setToTranslation(vehiclePosition.getX(), vehiclePosition.getY());
				this.trans.rotate(Math.toRadians(angle), mImage.getWidth(imgObserve) / 2,
						mImage.getHeight(imgObserve) / 2);
			} else {
				if (angle == 135) {
					angle = 0;
					currentDirection = VehicleDirection.RIGHT;
					nextBehaviour = VehicleBehaviour.GO_STRAIGHT;
					currentState = VehicleBehaviour.GO_STRAIGHT;
					turnDecided = false;
					passedTrafficLight = true;
					velocity = 2;

					// Change traffic light

					this.vehiclePosition.setX(downRightEnd.getX());
					this.vehiclePosition.setY(downRightEnd.getY());

					this.trans.setToTranslation(vehiclePosition.getX(), vehiclePosition.getY());
					this.trans.rotate(Math.toRadians(angle), mImage.getWidth(imgObserve) / 2,
							mImage.getHeight(imgObserve) / 2);

					// change this to another road
					runningLane.moveVehicleBetweenRoads(this, "gwh-russel_st");
				} else {
					// steerTowards(180, 40);
					angle = 135;

					this.vehiclePosition.setX(downRightMiddle.getX());
					this.vehiclePosition.setY(downRightMiddle.getY());

					this.trans.setToTranslation(vehiclePosition.getX(), vehiclePosition.getY());
					this.trans.rotate(Math.toRadians(angle), mImage.getWidth(imgObserve) / 2,
							mImage.getHeight(imgObserve) / 2);
				}
			}

			break;
		case LEFT:
			if (this.vehiclePosition.getX() > LEFT_DOWN.getX()) {
				// Slow down
				if(Math.abs(velocity) > 2) {
					slowDown();
				}
				
				this.vehiclePosition.setX(this.vehiclePosition.getX() - Math.abs((int) velocity));

				this.trans.setToTranslation(vehiclePosition.getX(), vehiclePosition.getY());
				this.trans.rotate(Math.toRadians(angle), mImage.getWidth(imgObserve) / 2,
						mImage.getHeight(imgObserve) / 2);
			} else {
				if (angle == -135) {
					angle = -90;
					currentDirection = VehicleDirection.DOWN;
					nextBehaviour = VehicleBehaviour.GO_STRAIGHT;
					currentState = VehicleBehaviour.GO_STRAIGHT;
					turnDecided = false;
					passedTrafficLight = true;
					velocity = 2;

					// Change traffic light

					this.vehiclePosition.setX(leftDownEnd.getX() - bgiWidth);
					this.vehiclePosition.setY(leftDownEnd.getY());

					// change this to another road
					runningLane.moveVehicleBetweenRoads(this, "gwh-russel_st");
				} else {
					// steerTowards(-90, 40);
					angle = -135;

					this.vehiclePosition.setX(leftDownMiddle.getX());
					this.vehiclePosition.setY(leftDownMiddle.getY());

					this.trans.setToTranslation(vehiclePosition.getX(), vehiclePosition.getY());
					this.trans.rotate(Math.toRadians(angle), mImage.getWidth(imgObserve) / 2,
							mImage.getHeight(imgObserve) / 2);
				}
			}
			break;
		case RIGHT:
			if (this.vehiclePosition.getX() < RIGHT_UP.getX()) {
				// Slow down
				if(Math.abs(velocity) > 2) {
					slowDown();
				}
				
				this.vehiclePosition.setX(this.vehiclePosition.getX() + Math.abs((int) velocity));

				this.trans.setToTranslation(vehiclePosition.getX(), vehiclePosition.getY());
				this.trans.rotate(Math.toRadians(angle), mImage.getWidth(imgObserve) / 2,
						mImage.getHeight(imgObserve) / 2);
			} else {
				if (angle == 45) {
					angle = 90;
					currentDirection = VehicleDirection.UP;
					nextBehaviour = VehicleBehaviour.GO_STRAIGHT;
					currentState = VehicleBehaviour.GO_STRAIGHT;
					turnDecided = false;
					passedTrafficLight = true;
					velocity = 2;

					// Change traffic light

					this.vehiclePosition.setX(rightUpEnd.getX() - bgiWidth);
					this.vehiclePosition.setY(rightUpEnd.getY());

					// change this to another road
					runningLane.moveVehicleBetweenRoads(this, "gwh-russel_st");
				} else {
					// steerTowards(90, 40);
					angle = 45;

					this.vehiclePosition.setX(rightUpMiddle.getX());
					this.vehiclePosition.setY(rightUpMiddle.getY());

					this.trans.setToTranslation(vehiclePosition.getX(), vehiclePosition.getY());
					this.trans.rotate(Math.toRadians(angle), mImage.getWidth(imgObserve) / 2,
							mImage.getHeight(imgObserve) / 2);
				}
			}
			break;
		default:
			break;
		}
	}

	private void processTurnRight() {
		switch (currentDirection) {
		case UP:
			break;
		case DOWN:
			break;
		case LEFT:
			break;
		case RIGHT:
			break;
		default:
			break;
		}
	}

	private VehicleBehaviour decideBehaviour() {
		if (nextBehaviour == VehicleBehaviour.TURN_LEFT) {
			return nextBehaviour;
		}

		VehicleBehaviour behaviourByTrafficLight = lookTrafficLight();
		VehicleBehaviour behaviourByVehicleAhead = lookVehicleAhead();

		if (behaviourByTrafficLight == VehicleBehaviour.BRAKE || behaviourByVehicleAhead == VehicleBehaviour.BRAKE) {
			return VehicleBehaviour.BRAKE;
		} else if (behaviourByTrafficLight == VehicleBehaviour.SLOW_DOWN
				|| behaviourByVehicleAhead == VehicleBehaviour.SLOW_DOWN) {
			return VehicleBehaviour.SLOW_DOWN;
		} else if (behaviourByVehicleAhead == VehicleBehaviour.TURN_LEFT) {
			return behaviourByVehicleAhead;
		}

		return VehicleBehaviour.GO_STRAIGHT;
	}

	private VehicleBehaviour lookTrafficLight() {
		if (trafficLight != null) {
			Coordinate tlPosition = trafficLight.getPosition();

			switch (currentDirection) {
			case LEFT:
				if (!trafficLight.forwardGo) {
					if ((vehiclePosition.getX() - tlPosition.getX()) > 0
							&& (vehiclePosition.getX() - tlPosition.getX()) < 20) {
						return VehicleBehaviour.BRAKE;
					} else if ((vehiclePosition.getX() - tlPosition.getX()) > 0
							&& (vehiclePosition.getX() - tlPosition.getX()) < SAFE_GAP) {
						return VehicleBehaviour.SLOW_DOWN;
					}
				}
				break;
			case RIGHT:
				if (!trafficLight.forwardGo) {
					if (tlPosition.getX() - vehiclePosition.getX() > 0
							&& tlPosition.getX() - vehiclePosition.getX() < 10) {
						return VehicleBehaviour.BRAKE;
					} else if (tlPosition.getX() - vehiclePosition.getX() > 0
							&& tlPosition.getX() - vehiclePosition.getX() < SAFE_GAP) {
						return VehicleBehaviour.SLOW_DOWN;
					}
				}
				break;
			case UP:
				if (!trafficLight.forwardGo) {
					if (vehiclePosition.getY() - tlPosition.getY() < 20
							&& vehiclePosition.getY() - tlPosition.getY() > 0) {
						return VehicleBehaviour.BRAKE;
					} else if (vehiclePosition.getY() - tlPosition.getY() < SAFE_GAP
							&& vehiclePosition.getY() - tlPosition.getY() > 0) {
						return VehicleBehaviour.SLOW_DOWN;
					}
				}
				break;
			case DOWN:
				if (!trafficLight.forwardGo) {
					if (tlPosition.getY() - vehiclePosition.getY() > 0
							&& tlPosition.getY() - vehiclePosition.getY() < 10) {
						return VehicleBehaviour.BRAKE;
					} else if (tlPosition.getY() - vehiclePosition.getY() > 0
							&& tlPosition.getY() - vehiclePosition.getY() < SAFE_GAP) {
						return VehicleBehaviour.SLOW_DOWN;
					}
				}
				break;
			default:
				break;
			}
		}

		return VehicleBehaviour.GO_STRAIGHT;
	}

	private VehicleBehaviour lookVehicleAhead() {
		if (trafficLight != null) {
			Coordinate tlPosition = trafficLight.getPosition();

			switch (currentDirection) {
			case LEFT:
				if (vehicleAhead != null) {
					if (tlPosition.getX() - vehicleAhead.getVehiclePosition().getX() > 0
							&& tlPosition.getX() - vehicleAhead.getVehiclePosition().getX() < SAFE_GAP) {
						return VehicleBehaviour.BRAKE;
					} else if (vehiclePosition.getX() - vehicleAhead.getVehiclePosition().getX() > 0
							&& vehiclePosition.getX() - vehicleAhead.getVehiclePosition().getX() < SAFE_GAP) {
						return VehicleBehaviour.BRAKE;
					}
				}

				if (!turnDecided && (vehiclePosition.getX() - LEFT_DOWN.getX()) < 50) {
					turnDecided = true;
					int rand = random.nextInt(10);
					if (rand > 7) {
						setDriver("Turning Driver 3");
						nextBehaviour = VehicleBehaviour.TURN_LEFT;
						currentState = VehicleBehaviour.TURN_LEFT;
						return VehicleBehaviour.TURN_LEFT;
					}
				}
				break;
			case RIGHT:
				if (vehicleAhead != null) {
					if (vehicleAhead.getVehiclePosition().getX() - tlPosition.getX() > 0
							&& vehicleAhead.getVehiclePosition().getX() - tlPosition.getX() < SAFE_GAP) {
						return VehicleBehaviour.BRAKE;
					} else if (vehicleAhead.getVehiclePosition().getX() - vehiclePosition.getX() > 0
							&& vehicleAhead.getVehiclePosition().getX() - vehiclePosition.getX() < SAFE_GAP) {
						return VehicleBehaviour.BRAKE;
					}
				}

				if (!turnDecided && (RIGHT_UP.getX() - vehiclePosition.getX()) < 50) {
					turnDecided = true;
					int rand = random.nextInt(10);
					if (rand > 7) {
						setDriver("Turning Driver 4");
						nextBehaviour = VehicleBehaviour.TURN_LEFT;
						currentState = VehicleBehaviour.TURN_LEFT;
						return VehicleBehaviour.TURN_LEFT;
					}
				}
				break;
			case UP:
				if (vehicleAhead != null) {
					if (tlPosition.getY() - vehicleAhead.getVehiclePosition().getY() > 0
							&& tlPosition.getY() - vehicleAhead.getVehiclePosition().getY() < SAFE_GAP) {
						return VehicleBehaviour.BRAKE;
					} else if (vehiclePosition.getY() - vehicleAhead.getVehiclePosition().getY() > 0
							&& vehiclePosition.getY() - vehicleAhead.getVehiclePosition().getY() < SAFE_GAP) {
						return VehicleBehaviour.BRAKE;
					}
				}

				if (!turnDecided && (vehiclePosition.getY() - UP_LEFT.getY()) < 50) {
					turnDecided = true;
					int rand = random.nextInt(10);
					if (rand > 7) {
						setDriver("Turning Driver 1");
						nextBehaviour = VehicleBehaviour.TURN_LEFT;
						currentState = VehicleBehaviour.TURN_LEFT;
						return VehicleBehaviour.TURN_LEFT;
					}
				}
				break;
			case DOWN:
				if (vehicleAhead != null) {
					if (vehicleAhead.getVehiclePosition().getY() - tlPosition.getY() > 0
							&& vehicleAhead.getVehiclePosition().getY() - tlPosition.getY() < SAFE_GAP) {
						return VehicleBehaviour.BRAKE;
					} else if (vehicleAhead.getVehiclePosition().getY() - vehiclePosition.getY() > 0
							&& vehicleAhead.getVehiclePosition().getY() - vehiclePosition.getY() < SAFE_GAP) {
						return VehicleBehaviour.BRAKE;
					}

					if (!turnDecided && (DOWN_RIGHT.getY() - vehiclePosition.getY()) < 50) {
						turnDecided = true;
						int rand = random.nextInt(10);
						if (rand > 7) {
							setDriver("Turning Driver 2");
							nextBehaviour = VehicleBehaviour.TURN_LEFT;
							currentState = VehicleBehaviour.TURN_LEFT;
							return VehicleBehaviour.TURN_LEFT;
						}
					}
				}
				break;
			default:
				break;
			}
		} else {
			switch (currentDirection) {
			case LEFT:
				if (vehicleAhead != null) {
					if (vehiclePosition.getX() - vehicleAhead.getVehiclePosition().getX() > 0
							&& vehiclePosition.getX() - vehicleAhead.getVehiclePosition().getX() < SAFE_GAP) {
						return VehicleBehaviour.BRAKE;
					}
				}
				break;
			case RIGHT:
				if (vehicleAhead != null) {
					if (vehicleAhead.getVehiclePosition().getX() - vehiclePosition.getX() > 0
							&& vehicleAhead.getVehiclePosition().getX() - vehiclePosition.getX() < SAFE_GAP) {
						return VehicleBehaviour.BRAKE;
					}
				}
				break;
			case UP:
				if (vehicleAhead != null) {
					if (vehiclePosition.getY() - vehicleAhead.getVehiclePosition().getY() > 0
							&& vehiclePosition.getY() - vehicleAhead.getVehiclePosition().getY() < SAFE_GAP) {
						return VehicleBehaviour.BRAKE;
					}
				}

				if (!turnDecided && (vehiclePosition.getY() - UP_LEFT.getY()) < 50) {
					turnDecided = true;
					int rand = random.nextInt(10);
					if (rand > 7) {
						setDriver("Turning Driver 1");
						nextBehaviour = VehicleBehaviour.TURN_LEFT;
						currentState = VehicleBehaviour.TURN_LEFT;
						return VehicleBehaviour.TURN_LEFT;
					}
				}
				break;
			case DOWN:
				if (vehicleAhead != null) {
					if (vehicleAhead.getVehiclePosition().getY() - vehiclePosition.getY() > 0
							&& vehicleAhead.getVehiclePosition().getY() - vehiclePosition.getY() < SAFE_GAP) {
						return VehicleBehaviour.BRAKE;
					}

					if (!turnDecided && (DOWN_RIGHT.getY() - vehiclePosition.getY()) < 50) {
						turnDecided = true;
						int rand = random.nextInt(10);
						if (rand > 7) {
							setDriver("Turning Driver 2");
							nextBehaviour = VehicleBehaviour.TURN_LEFT;
							currentState = VehicleBehaviour.TURN_LEFT;
							return VehicleBehaviour.TURN_LEFT;
						}
					}
				}
				break;
			default:
				break;
			}
		}

		return VehicleBehaviour.GO_STRAIGHT;
	}

	@Override
	public VehicleBehaviour getCurrentState() {
		return currentState;
	}

	public void setCurrentState(VehicleBehaviour vState) {
		this.currentState = vState;
	}

	@Override
	public VehicleDirection getVehicleDirection() {
		return currentDirection;
	}

	public void setVehicleDirection(VehicleDirection vDirection) {
		this.currentDirection = vDirection;
	}

	public BufferedImage getImage() {
		return (BufferedImage) mImage;
	}

	public void setImage(Image image) {
		mImage = image;
	}

	public float getSpeed() {
		return velocity;
	}

	public void setSpeed(int speed) {
		velocity = speed;
	}

	public float getCurAngle() {
		return angle;
	}

	public boolean hasPassedTrafficLight() {
		return passedTrafficLight;
	}

	public void setCurAngle(float curAngle) {
		angle = curAngle;
	}

	@Override
	public Coordinate getVehiclePosition() {
		return vehiclePosition;
	}

	@Override
	public void setVehiclePosition(Coordinate vehiclePosition) {
		this.vehiclePosition = vehiclePosition;
	}

	public AffineTransform getTrans() {
		return trans;
	}

	public RunningLane getRunningLane() {
		return runningLane;
	}

	public void setRunningLane(RunningLane runningLane) {
		this.runningLane = runningLane;
	}
}
