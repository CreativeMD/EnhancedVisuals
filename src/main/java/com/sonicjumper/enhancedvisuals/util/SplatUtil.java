package com.sonicjumper.enhancedvisuals.util;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SplatUtil {
	private static Random rand;
	
	/**
	 * In a screen bounded by regions [0.0, 0.0] and [1.0, 1.0], generate a list of points that when plotted are slightly grouped or streaked.
	 * @return
	 */
	public static List<Point2D> generateRandomSplatStreak(int totalSplats) {
		if(rand == null) {
			rand = new Random();
		}
		
		ArrayList<Point2D> listOfPoints = new ArrayList<Point2D>();
		
		// Create start point
		Point2D startPoint = new Point2D.Double(rand.nextDouble(), rand.nextDouble());
		// Create end point
		Point2D endPoint = generateRandomPointWithDistance(startPoint, 0.5D);
		// Create control point
		Point2D controlPoint = generateRandomPointWithDistance(startPoint, 0.25D);
		
		System.out.println("Start: " + startPoint.toString());
		System.out.println("Control: " + controlPoint.toString());
		System.out.println("End: " + endPoint.toString());
		
		float steps = totalSplats;
		for(int i = 0; i < steps + 1; i++) {
			float t = i / steps;
			double x = (1 - t) * (1 - t) * startPoint.getX() + 2 * (1 - t) * t * controlPoint.getX() + t * t * endPoint.getX();
			double y = (1 - t) * (1 - t) * startPoint.getY() + 2 * (1 - t) * t * controlPoint.getY() + t * t * endPoint.getY();
			Point2D point = generateRandomPointWithDistance(new Point2D.Double(x, y), 0.05F);
			// Check to make sure point is not outside bounds, then add it
			if(point.getX() >= 0.0D && point.getX() <= 1.0D && point.getY() >= 0.0D && point.getY() <= 1.0D) {
				listOfPoints.add(point);
			}
		}
		
		return listOfPoints;
	}
	
	/**
	 * Generate a random point with a set distance from the input point
	 * @param point
	 * @param maxDistance
	 * @return
	 */
	public static Point2D generateRandomPointWithDistance(Point2D point, double maxDistance) {
		double distInXSq = rand.nextFloat() * Math.pow(maxDistance, 2);
		double distInYSq = Math.pow(maxDistance, 2) - distInXSq;
		
		double newXPos = (float) (rand.nextInt(2) == 0 ? point.getX() + Math.sqrt(distInXSq) : point.getX() - Math.sqrt(distInXSq));
		double newYPos = (float) (rand.nextInt(2) == 0 ? point.getY() + Math.sqrt(distInYSq) : point.getY() - Math.sqrt(distInYSq));
		
		return new Point2D.Double(newXPos, newYPos);
	}
}
