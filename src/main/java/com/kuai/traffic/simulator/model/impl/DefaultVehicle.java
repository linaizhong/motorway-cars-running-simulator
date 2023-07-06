package com.kuai.traffic.simulator.model.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import com.kuai.traffic.simulator.model.Coordinate;
import com.kuai.traffic.simulator.model.Vehicle;
import com.kuai.traffic.simulator.running.RunningLane;
import com.kuai.traffic.simulator.running.RunningVehicle;
import com.kuai.traffic.simulator.util.Utilities;

public abstract class DefaultVehicle extends Vehicle {
  private String[] carImages = { "resources\\images\\car1.png", "resources\\images\\car2.png",
      "resources\\images\\car3.png", "resources\\images\\car4.png", "resources\\images\\ambulance.png",
      "resources\\images\\police.png", "resources\\images\\truck1.png", "resources\\images\\truck2.png" };

  // Position Constants
  protected Coordinate RIGHT_LEFT_POS;
  protected Coordinate LEFT_RIGHT_POS;
  protected Coordinate DOWN_UP_POS;
  protected Coordinate UP_DOWN_POS;

  // Junction Turning Constants
  protected final Coordinate DOWN_LEFT = Utilities.buildCoordinate(487, 101);
  protected final Coordinate DOWN_RIGHT = Utilities.buildCoordinate(487, 101);
  protected final Coordinate RIGHT_UP = Utilities.buildCoordinate(448, 115);
  protected final Coordinate RIGHT_DOWN = Utilities.buildCoordinate(475, 114);
  protected final Coordinate LEFT_UP = Utilities.buildCoordinate(473, 142);
  protected final Coordinate LEFT_DOWN = Utilities.buildCoordinate(473, 142);
  protected final Coordinate UP_LEFT = Utilities.buildCoordinate(461, 156);
  protected final Coordinate UP_RIGHT = Utilities.buildCoordinate(461, 128);

  protected int SAFE_GAP = 100;
  protected int ININ_GAP = 100;

  public enum VehicleBehaviour {
    GO_STRAIGHT, SLOW_DOWN, SPEED_UP, BRAKE, CHANGE_LANE, OVERTAKE, TURN_LEFT, TURN_RIGHT
  };

  public enum VehicleGear {
    PARKING, BACKWARD, NOP, FORWARD_1, FORWARD_2, FORWARD_3
  }

  protected Random random = new Random();

  // For controlling turning
  protected Image mImage; // Image of vehicle
  protected int bgiWidth, bgiLength;

  protected float steerWheel = 0;    //-: right turn; +: left turn;
  protected float brakePadel = 0;
  protected float gasPadel = 0;
  protected VehicleGear gearHandle = VehicleGear.PARKING;
  
  protected float angle;
  protected float velocity;

  public DefaultVehicle() {
    try {
      int carImageId = random.nextInt(carImages.length);
      if (carImageId < 0) {
        carImageId = 0;
      }

      BufferedImage img = ImageIO.read(new File(carImages[carImageId]));
      mImage = Utilities.resize(img, (int) ((double) img.getWidth() * 0.50),
          (int) ((double) img.getHeight() * 0.50));
      bgiLength = ((BufferedImage) mImage).getWidth();
      bgiWidth = ((BufferedImage) mImage).getHeight();
    } catch (IOException e) {
      System.out.println("Can't find image");
      e.printStackTrace();
    }
  }
  
  protected void init(RunningLane rl, RunningVehicle vehicleAhead) {
    initVehicleStartPoint(rl);
    initVehicleCurrentPoint(vehicleAhead);
    initVelocity(vehicleAhead);
    initAngle();
  }
  
  private void initVelocity(RunningVehicle vehicleAhead) {
    if(vehicleAhead == null) {
      this.velocity = 6;
    } else {
      this.velocity = 7 - random.nextInt(3);
    }
  }

  private void initVehicleStartPoint(RunningLane rl) {
    List<Coordinate> coordinates = rl.getLane().getCoordinate();
    switch (getVehicleDirection()) {
    case LEFT:
      RIGHT_LEFT_POS = Utilities.buildCoordinate(coordinates.get(1).getX(),
          coordinates.get(1).getY() - (bgiWidth / 2));
      break;
    case RIGHT:
      LEFT_RIGHT_POS = Utilities.buildCoordinate(coordinates.get(0).getX(),
          coordinates.get(0).getY() - (bgiWidth / 2));
      break;
    case UP:
      DOWN_UP_POS = Utilities.buildCoordinate(coordinates.get(1).getX() - bgiWidth, coordinates.get(1).getY());
      break;
    case DOWN:
      UP_DOWN_POS = Utilities.buildCoordinate(coordinates.get(0).getX() - bgiWidth, coordinates.get(0).getY());
      break;
    default:
      break;
    }
  }

  private void initVehicleCurrentPoint(RunningVehicle vehicleAhead) {
    switch (getVehicleDirection()) {
    case LEFT:
      if (vehicleAhead != null && vehicleAhead.getVehiclePosition().getX() > (RIGHT_LEFT_POS.getX() - ININ_GAP)) {
        coordinate = Utilities.buildCoordinate(vehicleAhead.getVehiclePosition().getX() + ININ_GAP,
            vehicleAhead.getVehiclePosition().getY());
      } else
        coordinate = RIGHT_LEFT_POS;
      break;
    case RIGHT:
      if (vehicleAhead != null && vehicleAhead.getVehiclePosition().getX() < (LEFT_RIGHT_POS.getX() + ININ_GAP)) {
        coordinate = Utilities.buildCoordinate(vehicleAhead.getVehiclePosition().getX() - ININ_GAP,
            vehicleAhead.getVehiclePosition().getY());
      } else
        coordinate = LEFT_RIGHT_POS;
      break;
    case UP:
      if (vehicleAhead != null && vehicleAhead.getVehiclePosition().getY() > (DOWN_UP_POS.getY() - ININ_GAP)) {
        coordinate = Utilities.buildCoordinate(vehicleAhead.getVehiclePosition().getX(),
            vehicleAhead.getVehiclePosition().getY() + ININ_GAP);
      } else
        coordinate = DOWN_UP_POS;
      break;
    case DOWN:
      if (vehicleAhead != null && vehicleAhead.getVehiclePosition().getY() < (UP_DOWN_POS.getY() + ININ_GAP)) {
        coordinate = Utilities.buildCoordinate(vehicleAhead.getVehiclePosition().getX(),
            vehicleAhead.getVehiclePosition().getY() - ININ_GAP);
      } else
        coordinate = UP_DOWN_POS;
      break;
    default:
      break;
    }
    
    setVehiclePosition(coordinate);
  }

  private void initAngle() {
    if (getVehicleDirection() == RunningVehicle.VehicleDirection.LEFT) {
      angle = 0;
    } else if (getVehicleDirection() == RunningVehicle.VehicleDirection.RIGHT) {
      angle = 180;
    } else if (getVehicleDirection() == RunningVehicle.VehicleDirection.UP) {
      angle = -90;
    } else {
      angle = 90;
    }
  }

  public boolean isInView(RunningLane runningLane) {
    List<Coordinate> coords = runningLane.getLane().getCoordinate();
    if (getVehicleDirection() == RunningVehicle.VehicleDirection.LEFT
        && (getVehiclePosition().getX() < coords.get(0).getX() || getVehiclePosition().getX() > coords.get(1).getX())) {
      return false;
    }
    if (getVehicleDirection() == RunningVehicle.VehicleDirection.RIGHT
        && (getVehiclePosition().getX() < coords.get(0).getX() || getVehiclePosition().getX() > coords.get(1).getX())
        && (getVehiclePosition().getX() > bgiLength)) {
      return false;
    }
    if (getVehicleDirection() == RunningVehicle.VehicleDirection.UP
        && (getVehiclePosition().getY() < coords.get(0).getY() || getVehiclePosition().getY() > coords.get(1).getY())) {
      return false;
    }
    if (getVehicleDirection() == RunningVehicle.VehicleDirection.DOWN
        && (getVehiclePosition().getY() < coords.get(0).getY() || getVehiclePosition().getY() > coords.get(1).getY())
        && (getVehiclePosition().getY() > bgiLength)) {
      return false;
    }

    return true;
  }

  public boolean isValid(RunningLane runningLane) {
    List<Coordinate> coords = runningLane.getLane().getCoordinate();
    if (getVehicleDirection() == RunningVehicle.VehicleDirection.LEFT && (getVehiclePosition().getX() < coords.get(0).getX())) {
      return false;
    }
    if (getVehicleDirection() == RunningVehicle.VehicleDirection.RIGHT && (getVehiclePosition().getX() > coords.get(1).getX())) {
      return false;
    }
    if (getVehicleDirection() == RunningVehicle.VehicleDirection.UP && (getVehiclePosition().getY() < coords.get(0).getY())) {
      return false;
    }
    if (getVehicleDirection() == RunningVehicle.VehicleDirection.DOWN && (getVehiclePosition().getY() > coords.get(1).getY())) {
      return false;
    }

    return true;
  }
  
  public abstract Coordinate getVehiclePosition();
  public abstract void setVehiclePosition(Coordinate position);
  public abstract RunningVehicle.VehicleDirection getVehicleDirection();
  public abstract VehicleBehaviour getCurrentState();

  protected float accelerate(int current_pos, int final_pos) {
    // float dist = final_pos - current_pos;
    float dist = bgiLength + SAFE_GAP;
    float t = dist / velocity;

    if (Math.abs(t) > 0)
      return (0 - velocity) / t;
    else
      return 0;

  }

  protected void steerTowards(float tangle, float t) {
    // first we calculate the angular velocity required to get the vehicle
    // to angle in time t
    float angularVel = tangle / t;
    if (tangle == 0 || tangle == 90 || tangle == 270) {
      angularVel = (tangle - angle) / t;
    }
    // if(Math.abs(mCurAngle) < Math.abs(angle))
    angle += angularVel;

//    trans.rotate(Math.toRadians(angle), mImage.getWidth(imgObserve), mImage.getHeight(imgObserve));
  }

  public float getSteerWheel() {
    return steerWheel;
  }

  public void setSteerWheel(float steerWheel) {
    this.steerWheel = steerWheel;
  }

  public float getBrakePadel() {
    return brakePadel;
  }

  public void setBrakePadel(float brakePadel) {
    this.brakePadel = brakePadel;
  }

  public float getGasPadel() {
    return gasPadel;
  }

  public void setGasPadel(float gasPadel) {
    this.gasPadel = gasPadel;
  }

  public VehicleGear getGearHandle() {
    return gearHandle;
  }

  public void setGearHandle(VehicleGear gearHandle) {
    this.gearHandle = gearHandle;
  }
}
