package com.kuai.traffic.simulator.running;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import com.kuai.traffic.simulator.Simulation;
import com.kuai.traffic.simulator.model.Coordinate;
import com.kuai.traffic.simulator.model.TrafficLight;

public class RunningTrafficLight implements ActionListener {
	private TrafficLight trafficLight;

	private Image layoutImg;

	private Timer tm;

	private int id, angle, timer = 0;

	private ImageObserver imageObserver;

	private Coordinate position;
	private AffineTransform trans;

	private int trafficTime = 5;
	private Color[] currentLightColor;

	private Coordinate leftLightPosition;
	private Coordinate rightLightPosition;
	private Coordinate forwardPosition;

	public boolean leftGo, forwardGo, rightGo;

	public enum TrafficState {
		RED, YELLOW, GREEN
	};

	public RunningTrafficLight(TrafficLight tl, int id, int angle, ImageObserver iObs) {
		this.trafficLight = tl;
		this.id = id;
		this.angle = angle;
		this.imageObserver = iObs;

		this.trafficTime = trafficLight.getForwardTime();

		trans = new AffineTransform();

		leftGo = false;
		forwardGo = false;
		rightGo = false;

		currentLightColor = new Color[3]; // index 0-Left, 1-forward, 2-right
		currentLightColor[0] = Color.red;
		currentLightColor[1] = Color.red;
		currentLightColor[2] = Color.red;

		try {
			layoutImg = ImageIO.read(new File(tl.getBackgroundImage()));
			position = tl.getPosition().getCoordinate();
			trans.setToTranslation(position.getX(), position.getY());
			trans.rotate(Math.toRadians(angle), layoutImg.getWidth(imageObserver) / 2,
					layoutImg.getHeight(imageObserver) / 2);

			tm = new Timer(1, this);
			tm.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		timer++;

		if (id == 1) {
			if (timer > 550 && timer < (trafficTime * 1000) - 700) {
				leftGo = true;
				forwardGo = true;
				rightGo = false;
			}

			if (timer < trafficTime * 1000 && timer > (trafficTime * 1000) - 499) {
				leftGo = false;
				forwardGo = false;
				currentLightColor[0] = Color.yellow;
				currentLightColor[1] = Color.yellow;
			}
			if (timer > trafficTime * 1000) {
				currentLightColor[0] = Color.red;
				currentLightColor[1] = Color.red;
			}
			if (timer > (trafficTime + 4) * 1000) {
				//removeAllLaneVehicles();
				timer = 0;
			}
		}

		if (id == 2) {
			if (timer > trafficTime * 1000 && timer < ((trafficTime + 4) * 1000) - 500) {
				forwardGo = true;
				rightGo = true;
				leftGo = false;
			}

			if (timer < (trafficTime + 4) * 1000 && timer > ((trafficTime + 4) * 1000) - 499) {
				forwardGo = false;
				rightGo = false;

				currentLightColor[2] = Color.yellow;
				currentLightColor[1] = Color.yellow;
			}

			if (timer > (trafficTime + 4) * 1000) {
				currentLightColor[2] = Color.red;
				currentLightColor[1] = Color.red;
			}
			if (timer > (trafficTime + 4) * 1000) {
				//removeAllLaneVehicles();
				timer = 0;
			}
		}

		if (id == 3) {
			if (timer > 550 && timer < (trafficTime * 1000) - 700) {
				leftGo = false;
				forwardGo = true;
				rightGo = true;
			}
			if (timer < trafficTime * 1000 && timer > (trafficTime * 1000) - 499) {
				rightGo = false;
				forwardGo = false;
				currentLightColor[2] = Color.yellow;
				currentLightColor[1] = Color.yellow;
			}
			if (timer > trafficTime * 1000) {
				rightGo = false;
				forwardGo = false;
				currentLightColor[2] = Color.red;
				currentLightColor[1] = Color.red;
			}
			if (timer > (trafficTime + 4) * 1000) {
				//removeAllLaneVehicles();
				timer = 0;
			}
		}

		if (id == 4) {
			if (timer > trafficTime * 1000 && timer < ((trafficTime + 4) * 1000) - 500) {
				forwardGo = true;
				rightGo = false;
				leftGo = true;
			}

			if (timer < (trafficTime + 4) * 1000 && timer > ((trafficTime + 4) * 1000) - 499) {
				forwardGo = false;
				leftGo = false;

				currentLightColor[0] = Color.yellow;
				currentLightColor[1] = Color.yellow;
			}

			if (timer > (trafficTime + 4) * 1000) {
				currentLightColor[0] = Color.red;
				currentLightColor[1] = Color.red;
				//removeAllLaneVehicles();
				timer = 0;
			}
		}

		// color index, 0-left, 1-forward, 2-right
		if (leftGo) {
			currentLightColor[0] = Color.green;
		}

		if (forwardGo) {
			currentLightColor[1] = Color.green;
		}

		if (rightGo) {
			currentLightColor[2] = Color.green;
		}
	}
	
	private void removeAllLaneVehicles() {
		List<RunningLane> lanes = ((Simulation)imageObserver).getLanes();
		synchronized(lanes) {
			if(lanes != null) {
				for(RunningLane lane : lanes) {
					if(lane.getVehicles() != null) {
						for(RunningVehicle rv : lane.getVehicles()) {
							lane.getVehicles().remove(rv);
						}
					}
				}
			}
		}
	}

	public Coordinate getLeftLightPosition() {
		return leftLightPosition;
	}

	public void setLeftLightPosition(Coordinate leftLightPosition) {
		this.leftLightPosition = leftLightPosition;
	}

	public Coordinate getRightLightPosition() {
		return rightLightPosition;
	}

	public void setRightLightPosition(Coordinate rightLightPosition) {
		this.rightLightPosition = rightLightPosition;
	}

	public Coordinate getForwardPosition() {
		return forwardPosition;
	}

	public void setForwardPosition(Coordinate forwardPosition) {
		this.forwardPosition = forwardPosition;
	}

	public int getOrientation() {
		return trafficLight.getOrientation().toLowerCase().equals("horizontal") ? 0 : 1;
	}

	public AffineTransform getAffineTransform() {
		return trans;
	}

	public void setAffineTransform(AffineTransform trans) {
		this.trans = trans;
	}

	public Image getLayoutImg() {
		return layoutImg;
	}

	public void setLayoutImg(Image layoutImg) {
		this.layoutImg = layoutImg;
	}

	public Coordinate getPosition() {
		return position;
	}

	public void setPosition(Coordinate position) {
		this.position = position;
	}

	public Color[] getCurrentLightColor() {
		return currentLightColor;
	}

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}
}
