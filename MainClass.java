import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class MainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length > 1) {
			Vector elmList = readGPX(args[0]);
			if(elmList!=null) {
				writeGPX(elmList, args[1]);
			}
		} else {
			System.out.println("Usage: javac SGPXF.java ./input.gpx ./output.gpx");
		}
	}
	
	/**
	 * GPX reader method is based on
	 * http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
	 * @param gpxFile
	 */
	private static Vector readGPX(String gpxFile) {
		try {
			System.out.println("Processing Strava GPX File...");
			
			File fXmlFile = new File( gpxFile );
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
		 
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			int yearone = 1900;
			
			NodeList nList = doc.getElementsByTagName("trkpt");
			SimpleDateFormat format = new SimpleDateFormat("MM-dd'T'HH:mm:'00Z'");
			
			Vector<Vector> elmListVec = new Vector<Vector>(1,1);
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
		 
				Node nNode = nList.item(temp);
		 
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
					
					Date now = new Date();	
					yearone=yearone+1;
					
					String lat = eElement.getAttribute("lat");
					String lon = eElement.getAttribute("lon");
					String ele = eElement.getElementsByTagName("ele").item(0).getTextContent();
					String tim = (yearone)+"-"+format.format(now);
					
					System.out.println("Latitude : " + lat);
					System.out.println("Longitude : " + lon);
					System.out.println("Elevation : " + ele);

					// Create a time tag for each element							         
					System.out.println("Time : " + tim);	
					
					// Add elements to a vector
					Vector<String> elementVect = new Vector<String>(4,1);
					elementVect.add(lat);
					elementVect.add(lon);
					elementVect.add(ele);
					elementVect.add(tim);
					
					// Add elementVector to another list
					elmListVec.add(elementVect);
					
				}
				
				
			}
			
			// Write the new GPX file
			if(elmListVec.size()>1) {
				System.out.println("Writing new GPX file...");
				return elmListVec;
			} else {
				System.out.println("Stopped due to no elements to write.");
			}
			
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		return null;		
	}

	/**
	 * Based on parts found here
	 * https://code.google.com/p/osmtracker-android/source/browse/trunk/src/me/guillaumin/android/osmtracker/gpx/GPXFileWriter.java?r=45
	 * @param elmList
	 * @param fName
	 */
	private static void writeGPX(Vector elmList, String fName) {
		try {
			FileWriter fw = new FileWriter(fName);
			
			/*
			 * Write the XML/GPX header
			 */
			rtElm(fw,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			rtElm(fw,"<gpx creator=\"SGPXF\" version=\"1.1\" xmlns=\"http://www.topografix.com/GPX/1/1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">");
			rtElm(fw,"<metadata>");
			//rtElm(fw,"<time>"++"</time>");
			rtElm(fw,"/<metadata>");
			/*
			 * Start the main node tags 
			 */
			rtElm(fw,"<trk>");
			rtElm(fw,"<name>Converted Strava Ride</name>"); // Adding this but not using it for now
			rtElm(fw,"<trkseg>");
			/*
			 * Write each track element
			 */
			for (int i=0; i < elmList.size(); ++i) {
				rtElm(fw,"<trkpt lat=\"" + ((Vector) elmList.get(i)).get(0) + "\" lon=\"" + ((Vector) elmList.get(i)).get(1) +"\">");
				rtElm(fw,"\t<ele>" + ((Vector) elmList.get(i)).get(2) + "</ele>");
				rtElm(fw,"\t<time>" + ((Vector) elmList.get(i)).get(3) + "</time>");
				rtElm(fw,"</trkpt>");
			}
			/*
			 * Close the main node tags
			 */
			rtElm(fw,"</trkseg>");
			rtElm(fw,"</trk>");
			rtElm(fw,"</gpx>");
            /*
             * close the file
             */
            fw.close();
	
		} catch (IOException e) {
			System.out.println("Failed to write file.");
			e.printStackTrace();
		}
		
	}
	
	private static void rtElm(FileWriter fw, String ElmNode) throws IOException {
		fw.write(ElmNode + "\r\n");
	}
}
