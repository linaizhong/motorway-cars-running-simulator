package com.kuai.traffic.simulator.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.kuai.traffic.simulator.model.Coordinate;

public class Utilities {
  public static Coordinate buildCoordinate(int x, int y) {
    Coordinate coord = new Coordinate();
    coord.setX(x);
    coord.setY(y);
    
    return coord;
  }
  
  public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = dimg.createGraphics();
    g2d.drawImage(tmp, 0, 0, null);
    g2d.dispose();

    return dimg;
  }  
}
