package com.kuai.traffic.simulator.running;

import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

import com.kuai.traffic.simulator.TrafficSimulationFrame;
import com.kuai.traffic.simulator.model.Intersection;
import com.kuai.traffic.simulator.model.Lane;
import com.kuai.traffic.simulator.model.Position;
import com.kuai.traffic.simulator.model.Road;
import com.kuai.traffic.simulator.model.SideGroup;
import com.kuai.traffic.simulator.model.Traffic;
import com.kuai.traffic.simulator.model.TrafficLight;
import com.kuai.traffic.simulator.util.Utilities;

public class RunningTraffic {
	private List<RunningTrafficLight> trafficLights;
	private List<RunningLane> runningLanes;
	private List<Intersection> intersections;
	
	private ImageObserver imageObserver;
	
	public RunningTraffic(ImageObserver iObs) {
		this.imageObserver = iObs;
		
		buildRunningTrafficLight();
		buildRunningLanes();
		buildIntersections();
	}
	
	private void buildRunningTrafficLight() {
		trafficLights = new ArrayList<>();

		TrafficLight tl = new TrafficLight();
		tl.setBackgroundImage("resources\\images\\trafficLight.png");
		Position pos = new Position();
		pos.setCoordinate(Utilities.buildCoordinate(390, 50));
		tl.setPosition(pos);
		tl.setOrientation("Horizontal");
		tl.setId("1");
		tl.setForwardTime(5);
		RunningTrafficLight rtl = new RunningTrafficLight(tl, 1, 90, imageObserver);
		rtl.setLeftLightPosition(Utilities.buildCoordinate(410, 32));
		rtl.setForwardPosition(Utilities.buildCoordinate(410, 55));
		rtl.setRightLightPosition(Utilities.buildCoordinate(410, 79));
		trafficLights.add(rtl);

		tl = new TrafficLight();
		tl.setBackgroundImage("resources\\images\\trafficLight.png");
		pos = new Position();
		pos.setCoordinate(Utilities.buildCoordinate(376, 157));
		tl.setPosition(pos);
		tl.setOrientation("Vertical");
		tl.setId("2");
		tl.setForwardTime(5);
		rtl = new RunningTrafficLight(tl, 2, 0, imageObserver);
		rtl.setLeftLightPosition(Utilities.buildCoordinate(376, 158));
		rtl.setForwardPosition(Utilities.buildCoordinate(399, 158));
		rtl.setRightLightPosition(Utilities.buildCoordinate(423, 158));
		trafficLights.add(rtl);

		tl = new TrafficLight();
		tl.setBackgroundImage("resources\\images\\trafficLight.png");
		pos = new Position();
		pos.setCoordinate(Utilities.buildCoordinate(490, 180));
		tl.setPosition(pos);
		tl.setOrientation("Horizontal");
		tl.setId("3");
		tl.setForwardTime(5);
		rtl = new RunningTrafficLight(tl, 3, -90, imageObserver);
		rtl.setLeftLightPosition(Utilities.buildCoordinate(510, 160));
		rtl.setForwardPosition(Utilities.buildCoordinate(510, 183));
		rtl.setRightLightPosition(Utilities.buildCoordinate(510, 207));
		trafficLights.add(rtl);

		tl = new TrafficLight();
		tl.setBackgroundImage("resources\\images\\trafficLight.png");
		pos = new Position();
		pos.setCoordinate(Utilities.buildCoordinate(504, 70));
		tl.setPosition(pos);
		tl.setOrientation("Vertical");
		tl.setId("4");
		tl.setForwardTime(5);
		rtl = new RunningTrafficLight(tl, 4, 180, imageObserver);
		rtl.setLeftLightPosition(Utilities.buildCoordinate(504, 70));
		rtl.setForwardPosition(Utilities.buildCoordinate(527, 70));
		rtl.setRightLightPosition(Utilities.buildCoordinate(551, 70));
		trafficLights.add(rtl);
		
	}
	
	private void buildRunningLanes() {
		runningLanes = new ArrayList<>();
		
		Traffic traffic = TrafficSimulationFrame.getInstance().getTraffic();
		
		if(traffic != null) {
			List<Road> roads = traffic.getRoad();
			if(roads != null) {
				for(Road road : roads) {
					List<SideGroup> sides = road.getSideGroup();
					if(sides != null) {
						for(SideGroup sideGroup : sides) {
							List<Lane> lanes = sideGroup.getLane();
							if(lanes != null) {
								for(Lane lane : lanes) {
									RunningTrafficLight runningTrafficLight = getRunningTrafficLight(sideGroup.getDirection());
									RunningLane rlane = null;
									if(road.getId().equals("M4")) {
										rlane = new RunningLane(road, sideGroup, lane, null, imageObserver);
									} else {
										rlane = new RunningLane(road, sideGroup, lane, runningTrafficLight, imageObserver);
									}
									runningLanes.add(rlane);
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void buildIntersections() {
		Traffic traffic = TrafficSimulationFrame.getInstance().getTraffic();
		
		if(traffic != null) {
			intersections = traffic.getIntersection();
		}
	}
	
	private RunningTrafficLight getRunningTrafficLight(String dir) {
		if(dir.equals("LEFT")) {
			return getRunningTrafficLight(-90);
		} else if(dir.equals("RIGHT")) {
			return getRunningTrafficLight(90);
		} else if(dir.equals("UP")) {
			return getRunningTrafficLight(0);
		} else {
			return getRunningTrafficLight(180);
		}
	}
	
	private RunningTrafficLight getRunningTrafficLight(int dir) {
		for(RunningTrafficLight rtl : trafficLights) {
			if(rtl.getAngle() == dir) {
				return rtl;
			}
		}
		
		return null;
	}
	
	public List<RunningTrafficLight> getTrafficLights() {
		return trafficLights;
	}

	public void setTrafficLights(List<RunningTrafficLight> trafficLights) {
		this.trafficLights = trafficLights;
	}

	public List<RunningLane> getRunningLanes() {
		return runningLanes;
	}

	public void setRunningLanes(List<RunningLane> runningLanes) {
		this.runningLanes = runningLanes;
	}

	public List<Intersection> getIntersections() {
		return intersections;
	}

	public void setIntersections(List<Intersection> intersections) {
		this.intersections = intersections;
	}
}
