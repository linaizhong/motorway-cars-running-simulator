package com.kuai.traffic.simulator.running;

import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.kuai.traffic.simulator.TrafficSimulationFrame;
import com.kuai.traffic.simulator.model.Intersection;
import com.kuai.traffic.simulator.model.Lane;
import com.kuai.traffic.simulator.model.Road;
import com.kuai.traffic.simulator.model.SideGroup;
import com.kuai.traffic.simulator.model.impl.DefaultVehicle;

public class RunningLane implements Comparator<RunningVehicle> {
	private int MAX_VEHICLE = 4;

	private Road road;
	private SideGroup sideGroup;
	private Lane lane;
	private RunningTrafficLight runningTrafficLight;

	private ImageObserver imageObserver;

	private List<RunningVehicle> vehicles = new ArrayList<>();

	public RunningLane(Road road, SideGroup sg, Lane lane, RunningTrafficLight rtl, ImageObserver iObs) {
		this.road = road;
		this.sideGroup = sg;
		this.lane = lane;
		this.runningTrafficLight = rtl;
		this.imageObserver = iObs;

		if (road.getId().equals("M4")) {
			MAX_VEHICLE = 100;
		} else if (road.getId().equals("Greate Western Highway")) {
			MAX_VEHICLE = 20;
		}

		addRunningVehicle();
	}

	private RunningVehicle.VehicleDirection getDirection(String dir) {
		if (dir.equals("LEFT")) {
			return RunningVehicle.VehicleDirection.LEFT;
		} else if (dir.equals("RIGHT")) {
			return RunningVehicle.VehicleDirection.RIGHT;
		} else if (dir.equals("UP")) {
			return RunningVehicle.VehicleDirection.UP;
		} else {
			return RunningVehicle.VehicleDirection.DOWN;
		}
	}

	public void addRunningVehicle() {
//		if (road.getId().equals("M4")) {
//			return;
//		}

		if (vehicles.size() < MAX_VEHICLE) {
			synchronized (vehicles) {
				RunningVehicle.VehicleDirection direction = getDirection(sideGroup.getDirection());
				RunningVehicle rv = getInitVehicleAhead(direction);
				vehicles.add(new RunningVehicle(this, DefaultVehicle.VehicleBehaviour.GO_STRAIGHT, direction, runningTrafficLight,
						imageObserver, rv, vehicles.size()));
			}
		}
	}

	private RunningVehicle getInitVehicleAhead(RunningVehicle.VehicleDirection direction) {
		if (vehicles == null || vehicles.size() < 1) {
			return null;
		}

		switch (direction) {
		case UP:
			return vehicles.get(vehicles.size() - 1);
		case DOWN:
			return vehicles.get(0);
		case LEFT:
			return vehicles.get(vehicles.size() - 1);
		case RIGHT:
			return vehicles.get(0);
		default:
			break;
		}

		return null;
	}

	public void moveVehicleBetweenRoads(RunningVehicle rv, String intersectionId) {
		Intersection intersection = TrafficSimulationFrame.getInstance().getIntersectionById(intersectionId);
		Road currRoad = rv.getRunningLane().getRoad();
		Road targetRoad = null;
		if (rv.getVehicleDirection() == RunningVehicle.VehicleDirection.RIGHT || rv.getVehicleDirection() == RunningVehicle.VehicleDirection.LEFT) {
			String road1Id = intersection.getRoad1Id();
			String road2Id = intersection.getRoad2Id();

			if (road1Id != null && road1Id.equals(currRoad.getId())) {
				targetRoad = getRoadById(road2Id);
			} else if (road2Id != null && road2Id.equals(currRoad.getId())) {
				targetRoad = getRoadById(road1Id);
			}

			if (targetRoad != null) {
				Lane lane = null;

				if (rv.getVehicleDirection() == RunningVehicle.VehicleDirection.RIGHT) {
					lane = getLaneByDirection(targetRoad, "RIGHT");
				} else if (rv.getVehicleDirection() == RunningVehicle.VehicleDirection.LEFT) {
					lane = getLaneByDirection(targetRoad, "LEFT");
				}

				if (lane != null) {
					RunningLane targetRunningLane = getRunningLaneByLaneId(lane.getId());
					if (targetRunningLane != null) {
						RunningLane currentRunningLane = rv.getRunningLane();

						rv.setRunningLane(targetRunningLane);
						targetRunningLane.getVehicles().add(rv);
						currentRunningLane.getVehicles().remove(rv);
						TrafficSimulationFrame.getInstance().getSimulation().setSwitch(true);
					}
				}
			}
		} else {
			String road1Id = intersection.getRoad1Id();
			String road2Id = intersection.getRoad2Id();

			if (road1Id != null && road1Id.equals(currRoad.getId())) {
				targetRoad = getRoadById(road2Id);
			} else if (road2Id != null && road2Id.equals(currRoad.getId())) {
				targetRoad = getRoadById(road1Id);
			}

			if (targetRoad != null) {
				Lane lane = null;

				if (rv.getVehicleDirection() == RunningVehicle.VehicleDirection.DOWN) {
					lane = getLaneByDirection(targetRoad, "DOWN");
				} else if (rv.getVehicleDirection() == RunningVehicle.VehicleDirection.UP) {
					lane = getLaneByDirection(targetRoad, "UP");
				}

				if (lane != null) {
					RunningLane targetRunningLane = getRunningLaneByLaneId(lane.getId());
					if (targetRunningLane != null) {
						RunningLane currentRunningLane = rv.getRunningLane();

						rv.setRunningLane(targetRunningLane);
						targetRunningLane.getVehicles().add(rv);
						currentRunningLane.getVehicles().remove(rv);
						TrafficSimulationFrame.getInstance().getSimulation().setSwitch(true);
					}
				}
			}
		}
	}

	private RunningLane getRunningLaneByLaneId(String laneId) {
		List<RunningLane> runningLanes = TrafficSimulationFrame.getInstance().getSimulation().getRunningTraffic()
				.getRunningLanes();
		if (runningLanes != null) {
			for (RunningLane runningLane : runningLanes) {
				if (runningLane.getLane().getId().equals(laneId)) {
					return runningLane;
				}
			}
		}
		return null;
	}

	private Lane getLaneByDirection(Road road, String dir) {
		List<SideGroup> sideGroups = road.getSideGroup();
		if (sideGroups != null) {
			for (SideGroup sideGroup : sideGroups) {
				if (sideGroup.getDirection().equals(dir)) {
					return sideGroup.getLane().get(0);
				}
			}
		}
		return null;
	}

	private Road getRoadById(String rid) {
		List<Road> roads = TrafficSimulationFrame.getInstance().getTraffic().getRoad();
		if (roads != null) {
			for (Road road : roads) {
				if (road.getId().equals(rid)) {
					return road;
				}
			}
		}
		return null;
	}

	public RunningVehicle getVehicleAhead(RunningVehicle rv) {
		synchronized (vehicles) {
			if (vehicles == null || vehicles.size() < 2) {
				return null;
			}

			Collections.sort(vehicles, this);

			int index = vehicles.indexOf(rv);
			if (sideGroup.getDirection().equals("UP") || sideGroup.getDirection().equals("LEFT")) {
				if (index > 0) {
					return vehicles.get(index - 1);
				}
			} else if (sideGroup.getDirection().equals("DOWN") || sideGroup.getDirection().equals("RIGHT")) {
				if (index < vehicles.size() - 1) {
					return vehicles.get(index + 1);
				}
			}
		}

		return null;
	}

	@Override
	public int compare(RunningVehicle v1, RunningVehicle v2) {
		switch (getDirection(sideGroup.getDirection())) {
		case LEFT:
			int coord1 = v1.getVehiclePosition().getX();
			int coord2 = v2.getVehiclePosition().getX();
			return coord1 - coord2;
		case RIGHT:
			coord1 = v1.getVehiclePosition().getX();
			coord2 = v2.getVehiclePosition().getX();
			return coord1 - coord2;
		case UP:
			coord1 = v1.getVehiclePosition().getY();
			coord2 = v2.getVehiclePosition().getY();
			return coord1 - coord2;
		case DOWN:
			coord1 = v1.getVehiclePosition().getY();
			coord2 = v2.getVehiclePosition().getY();
			return coord1 - coord2;
		default:
			break;
		}

		return 0;
	}

	public Road getRoad() {
		return road;
	}

	public void setRoad(Road road) {
		this.road = road;
	}

	public Lane getLane() {
		return lane;
	}

	public void setLane(Lane lane) {
		this.lane = lane;
	}

	public List<RunningVehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<RunningVehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public SideGroup getSideGroup() {
		return sideGroup;
	}
}
