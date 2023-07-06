package com.kuai.traffic.simulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.kuai.traffic.simulator.model.Coordinate;
import com.kuai.traffic.simulator.model.Intersection;
import com.kuai.traffic.simulator.model.Road;
import com.kuai.traffic.simulator.running.RunningLane;
import com.kuai.traffic.simulator.running.RunningTraffic;
import com.kuai.traffic.simulator.running.RunningTrafficLight;
import com.kuai.traffic.simulator.running.RunningVehicle;

public class Simulation extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private static Image mTerrain;
	
	private Timer tm = new Timer(1, this);

	private int carSpawnTimer = 0; // timer regulating the rate new cars are
									// created
	private RunningTraffic runningTraffic;
	private List<RunningLane> lanes;
	private List<RunningTrafficLight> trafficLights;
	private List<Intersection> intersections;
	
	private boolean swit = false;
	
	public Simulation() {
		setLayout(new BorderLayout());

		try {
			mTerrain = ImageIO.read(new File("resources\\images\\M4 MMS Schematics v0b.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(null, "Coordinates: (" + e.getX() + ", " + e.getY() + ")");
			}
		});

		runningTraffic = new RunningTraffic(this);
		lanes = runningTraffic.getRunningLanes();
		trafficLights = runningTraffic.getTrafficLights();
		intersections = runningTraffic.getIntersections();		

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				tm.start();
			}
		});
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2D = (Graphics2D) g;
		
		g2D.drawImage(mTerrain, 0, 0, this);
		drawAllRunningTrafficLight(g2D);
		drawAllVehicles(g2D);
	}
	
	public void actionPerformed(ActionEvent e) {
		carSpawnTimer++;

		// This section is where cars are created, every 800s
		if (carSpawnTimer % 20 == 0) {
			for(RunningLane rl : lanes) {
				rl.addRunningVehicle();
			}

			carSpawnTimer = 0;
		}

		repaint();
	}

	public Image getBackgroundImage() {
		return mTerrain;
	}

	private void drawAllVehicles(Graphics2D g2D) {
		for(RunningLane rl : lanes) {
		  if(rl.getSideGroup().getDirection().equals("LEFT") || rl.getSideGroup().getDirection().equals("UP")) {
        for (int i = rl.getVehicles().size() - 1; i >= 0; i--) {
          RunningVehicle v = rl.getVehicles().get(i);
          
          if (v.isInView(rl)) {
            if(showable(rl, v)) {
              g2D.drawImage(v.getImage(), v.getTrans(), this);
            }
          } else if(!v.isValid(rl)) {
            v.stop();
            rl.getVehicles().remove(v);
          }
        }
		  } else {
        for (int i = 0; i < rl.getVehicles().size(); i++) {
          RunningVehicle v = rl.getVehicles().get(i);
          
          if (v.isInView(rl)) {
            if(showable(rl, v)) {
              g2D.drawImage(v.getImage(), v.getTrans(), this);
            }
          } else if(!v.isValid(rl)) {
            v.stop();
            rl.getVehicles().remove(v);
          }
        }
		  }
		}

		swit = false;
	}
	
	public void setSwitch(boolean swi) {
	  swit = swi;
	}

	private boolean showable(RunningLane rl, RunningVehicle rv) {
		boolean showable = true;
		for(Intersection intersection : intersections) {
			if(intersection.getPosition().size() == 2) {
				Coordinate topLeft = intersection.getPosition().get(0).getCoordinate();
				Coordinate bottomRight = intersection.getPosition().get(1).getCoordinate();

				if(rl.getSideGroup().getDirection().equals("LEFT")) {
					if((topLeft.getX() - rv.getImage().getWidth()) < rv.getVehiclePosition().getX()
							&& topLeft.getY() < (rv.getVehiclePosition().getY() + rv.getImage().getHeight())
							&& bottomRight.getX() > rv.getVehiclePosition().getX()  
							&& bottomRight.getY() > rv.getVehiclePosition().getY()) {
						showable = true;
						if((intersection.getRoad1Id() != null && !intersection.getRoad1Id().equals(rl.getRoad().getId()))) {
							showable = showable(intersection.getRoad1Id(), rl.getRoad().getLayer());
							return showable;
						}
						if((intersection.getRoad2Id() != null && !intersection.getRoad2Id().equals(rl.getRoad().getId()))) {
							showable = showable(intersection.getRoad2Id(), rl.getRoad().getLayer());
							return showable;
						}
						if((intersection.getRoad3Id() != null && !intersection.getRoad3Id().equals(rl.getRoad().getId()))) {
							showable = showable(intersection.getRoad3Id(), rl.getRoad().getLayer());
							return showable;
						}
						if((intersection.getRoad4Id() != null && !intersection.getRoad4Id().equals(rl.getRoad().getId()))) {
							showable = showable(intersection.getRoad4Id(), rl.getRoad().getLayer());
							return showable;
						}
					}
				} else if(rl.getSideGroup().getDirection().equals("RIGHT")) {
					if((topLeft.getX() - rv.getImage().getWidth()) < rv.getVehiclePosition().getX() 
							&& topLeft.getY() < (rv.getVehiclePosition().getY() + rv.getImage().getHeight())
							&& bottomRight.getX() > rv.getVehiclePosition().getX() 
							&& bottomRight.getY() > rv.getVehiclePosition().getY()) {
						showable = true;
						if((intersection.getRoad1Id() != null && !intersection.getRoad1Id().equals(rl.getRoad().getId()))) {
							showable = showable(intersection.getRoad1Id(), rl.getRoad().getLayer());
							return showable;
						}
						if((intersection.getRoad2Id() != null && !intersection.getRoad2Id().equals(rl.getRoad().getId()))) {
							showable = showable(intersection.getRoad2Id(), rl.getRoad().getLayer());
							return showable;
						}
						if((intersection.getRoad3Id() != null && !intersection.getRoad3Id().equals(rl.getRoad().getId()))) {
							showable = showable(intersection.getRoad3Id(), rl.getRoad().getLayer());
							return showable;
						}
						if((intersection.getRoad4Id() != null && !intersection.getRoad4Id().equals(rl.getRoad().getId()))) {
							showable = showable(intersection.getRoad4Id(), rl.getRoad().getLayer());
							return showable;
						}
					}
				}
			}
		}

		return true;
	}
	
	private boolean showable(String roadId, int layer) {
		List<Road> roads = TrafficSimulationFrame.getInstance().getTraffic().getRoad();
		if(roads != null) {
			for(Road road : roads) {
				if(road.getId().equals(roadId)) {
					return road.getLayer() <= layer;
				}
			}
		}
		
		return false;
	}
	
	private void drawAllRunningTrafficLight(Graphics2D g2D) {
		for (RunningTrafficLight t : trafficLights) {
			Color colors[] = t.getCurrentLightColor();
			if (t.getAngle() == 0 || t.getAngle() == 180) {
				g2D.setColor(colors[1]);
				g2D.fillRect(t.getForwardPosition().getX(), t.getForwardPosition().getY(), 21, 29);
				g2D.setColor(colors[0]);
				g2D.fillRect(t.getLeftLightPosition().getX(), t.getLeftLightPosition().getY(), 20, 29);
				g2D.setColor(colors[2]);
				g2D.fillRect(t.getRightLightPosition().getX(), t.getRightLightPosition().getY(), 20, 29);
			} else {
				g2D.setColor(colors[1]);
				g2D.fillRect(t.getForwardPosition().getX(), t.getForwardPosition().getY(), 29, 22);
				g2D.setColor(colors[0]);
				g2D.fillRect(t.getLeftLightPosition().getX(), t.getLeftLightPosition().getY(), 29, 22);
				g2D.setColor(colors[2]);
				g2D.fillRect(t.getRightLightPosition().getX(), t.getRightLightPosition().getY(), 29, 22);
			}
			
			g2D.drawImage(t.getLayoutImg(), t.getAffineTransform(), this);
		}
	}

	public RunningTraffic getRunningTraffic() {
    return runningTraffic;
  }

  public List<RunningLane> getLanes() {
		return lanes;
	}
//  
//  public void updateRunningLane(RunningLane rlane) {
//    if(lanes != null) {
//      synchronized(lanes) {
//        for(int i=0; i<lanes.size() - 1; i++) {
//          RunningLane lane = lanes.get(i);
//          System.out.println("updateRunningLane: " + lane);
//          if(lane.getLane().getId().equals(rlane.getLane().getId())) {
//            lanes.remove(lane);
//            lanes.add(rlane);
//            
//            break;
//          }
//        }
//      }
//    }
//  }
}
