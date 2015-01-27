package Reader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.nocrala.tools.gis.data.esri.shapefile.ShapeFileReader;
import org.nocrala.tools.gis.data.esri.shapefile.exception.InvalidShapeFileException;
import org.nocrala.tools.gis.data.esri.shapefile.header.ShapeFileHeader;
import org.nocrala.tools.gis.data.esri.shapefile.shape.AbstractShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.AbstractPolyShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.MultiPointZShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PointShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolygonShape;

import processing.core.PApplet;

public class parsing extends PApplet{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ArrayList<AbstractShape> shapeList = new ArrayList<>();
	ArrayList<AbstractShape> ptList = new ArrayList<>();
	ArrayList<AbstractShape> MultiPZList = new ArrayList<>();

	
	public void setup(){
		size(1900,900);
		smooth();
		
		FileInputStream is = null;
		try {
/*			is = new FileInputStream(
			        "C:/Users/Panther/Desktop/datasf/SanFranciscoNoiseLevels_2010CensusTracts.shp");
*/			is = new FileInputStream(
			        "C:/Users/Panther/Desktop/SanFranciscoMurals/SanFranciscoMurals.shp");

			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    ShapeFileReader r = null;

			try {
				r = new ShapeFileReader(is);
				
			} catch (InvalidShapeFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    ShapeFileHeader h = r.getHeader();
		    
		    System.out.println("The shape type of this files is " + h.getClass().getTypeParameters());
		    
		    
		    int total = 0;
		    AbstractShape s;
		    try {
				while ((s = r.next()) != null) {
				  switch (s.getShapeType()) {
				  case POINT:
				    PointShape aPoint = (PointShape) s;
				    
				    ptList.add(aPoint);
				    // Do something with the point shape...
				    break;
				  case MULTIPOINT_Z:
				    MultiPointZShape aMultiPointZ = (MultiPointZShape) s;
				    MultiPZList.add(aMultiPointZ);
				    // Do something with the MultiPointZ shape...
				    break;
				  case POLYGON:
				    PolygonShape aPolygon = (PolygonShape) s;
					shapeList.add(aPolygon);

/*				    System.out.println("I read a Polygon with "
				        + aPolygon.getNumberOfParts() + " parts and "
				        + aPolygon.getNumberOfPoints() + " points");
*/				    break;
				  default:
				    System.out.println("Read other type of shape.");
				  }
				  total++;
				  println(total);
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidShapeFileException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		    //System.out.println("Total shapes read: " + shapeList.size());
		    System.out.println("Total points read: " + ptList.size());
		    //System.out.println("Total multPTZ read: " + MultiPZList.size());
		    for(AbstractShape pt: ptList){
		    System.out.println("point: " + ((PointShape)pt).getX()+","+((PointShape)pt).getY());
		    }
		    
		    
		    try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    for(int j=0;j<shapeList.size();j++){
		    	PointData[] pts= ((AbstractPolyShape) shapeList.get(j)).getPoints();
			      beginShape();
		    	for (int i = 0; i < pts.length; i++) {
				      vertex((float)pts[i].getX()/1200-2800,(float)pts[i].getY()/1200-1200);
				  }
			      endShape(CLOSE);

		    }
	}
	
	public void draw(){
		background(0);
		pushStyle();
		for(int j=0;j<shapeList.size();j++){
	    	PointData[] pts= ((AbstractPolyShape) shapeList.get(j)).getPoints();
	    	
	    	stroke(0);
		      beginShape();
	    	for (int i = 0; i < pts.length; i++) {
			      vertex(((float)transferX(pts[i].getX())),((float)transferY(pts[i].getY())));
			  }
		      endShape(CLOSE);
		      
/*		    	fill(255,100);
		    	noStroke();
		      beginShape();
			  vertex((float)transferX(((AbstractPolyShape) shapeList.get(j)).getBoxMinX()), (float)transferY(((AbstractPolyShape) shapeList.get(j)).getBoxMinY()));
			  vertex((float)transferX(((AbstractPolyShape) shapeList.get(j)).getBoxMinX()), (float)transferY(((AbstractPolyShape) shapeList.get(j)).getBoxMaxY()));
			  vertex((float)transferX(((AbstractPolyShape) shapeList.get(j)).getBoxMaxX()), (float)transferY(((AbstractPolyShape) shapeList.get(j)).getBoxMaxY()));
			  vertex((float)transferX(((AbstractPolyShape) shapeList.get(j)).getBoxMaxX()), (float)transferY(((AbstractPolyShape) shapeList.get(j)).getBoxMinY()));
			  
		      endShape(CLOSE);
*/	    }
		popStyle();

	}
	
	public double transferX(double a){
		return (a-1195*5000)/60;
	}
	
	public double transferY(double a){
		return (a-416*5000)/60;
	}
	
}
