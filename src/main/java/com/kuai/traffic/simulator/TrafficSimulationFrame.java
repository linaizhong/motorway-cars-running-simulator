package com.kuai.traffic.simulator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.kuai.traffic.simulator.model.Intersection;
import com.kuai.traffic.simulator.model.Traffic;

@SuppressWarnings("serial")
public class TrafficSimulationFrame extends JFrame {

	private static TrafficSimulationFrame tsf = null;

	private Traffic traffic;
	
	private Simulation simulation;
	
	private SoundManager manager;

	public static synchronized TrafficSimulationFrame getInstance() {
		if (tsf == null) {
			tsf = new TrafficSimulationFrame();
		}

		return tsf;
	}

	public TrafficSimulationFrame() {
		super("Traffic Simulation");

		traffic = loadTrafficModelFromXml();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			  simulation = new Simulation();

				BufferedImage bi = (BufferedImage) simulation.getBackgroundImage();
				final Dimension dim = new Dimension(bi.getWidth(simulation), bi.getHeight(simulation));
				setSize(1920, (int) dim.getHeight() + 64);

				JPanel simContainer = new JPanel(new BorderLayout());
				setContentPane(simContainer);
				simContainer.setPreferredSize(dim);
				simulation.setPreferredSize(dim);
				JScrollPane sp = new JScrollPane(simulation);
				simContainer.add(sp, BorderLayout.CENTER);
			}
		});

		// manager = new SoundManager();
		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				if(manager != null) {
					manager.clip.close();
				}
			}
		});

		if(manager != null) {
			manager.clip.close();
		}
	}

	private static Traffic loadTrafficModelFromXml() {
		try {
			JAXBContext jc = JAXBContext.newInstance(Traffic.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			File xml = new File("resources/model.xml");
			Traffic traffic = (Traffic) unmarshaller.unmarshal(xml);
			return traffic;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public Traffic getTraffic() {
		return traffic;
	}

	public Intersection getIntersectionById(String id) {
	    if(traffic.getIntersection() == null || traffic.getIntersection().size() < 1) {
	        return null;
	      }
	      
	      for(Intersection inter : traffic.getIntersection()) {
	        if(inter.getId().equals(id)) {
	          return inter;
	        }
	      }
	      
	      return null;
	}

	public Simulation getSimulation() {
    return simulation;
  }

  public static void main(String args[]) {
		tsf = new TrafficSimulationFrame();
	}
}
