package tests;
// PlotTestServer (host-based network tables server)
// Emulates a roborio (Robot) network server
// - On startup generates a new NetworkTable table named "datatable"
// - every 10 seconds publishes to the table an array object called "Plot"
//   o contains plot info (id, #traces, #points)
// - fills plot data with random points (n-traces)
//   o each new point contains: point-id, time value, trace1-value, trace2-value ..
//   o points are published one at a time to to a new newwork tables entry "PlotData"+point-id
// 
// PlotTestClient
// - could be started first (before PlotTestServer is run)
// - waits for PlotTestSever to generate "datatable" server 
// - For every entry "Plot" that's published displays a java plotwindow that contains its data
//   o captures each multi-trace point as it is received from (new) "PlotData"+index entry object
//   o increments index after each point is processed
//   o when index=last point, opens a PlotData window
//
//  Notes:
//  1) It seems inefficient to require a new NetworkTableEntry object for each point in the plot but wpilib NeyworkTables
//     apparantely limits array sizes to <= 256
//     o Might be able use this advantageously by modifying plot to show new data as it arrives instead of all at once at the end
//  2) Windows host based tests require ntcore.dll & ntcorejni to be in java.library.path on launch
//     o original configuration in launch.json produced linker errors
//     o fixed by building ntcore from allwpilib github repo (gradlew build), copying dlls to ~/libs and adding to configuration:
//       with: "vmArgs": [
//        "-Djava.library.path=${workspaceFolder:TrajectoryTest}/libs"
//		 ],
//     o Alternatively, just added the following to configuration in launch.json:
//      "env": {
//	        "PATH": "$PATH:${workspaceFolder:TrajectoryTest}/build/jni/debug" 
//       },
//     o might need to first run "simulate on desktop" so that build contains native libraries (?)

import java.util.Random;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableEntry;

public class PlotTestServer {
	static int maxDataPoints = 100;

	NetworkTableEntry newPlot;
	NetworkTableEntry plotData;

	public static void main(String[] args) {
		new PlotTestServer().run();
	}
    
	public void run() {
		NetworkTableInstance inst = NetworkTableInstance.getDefault();
		inst.setServer("localhost");

		//inst.setServer("ServerTest");
		NetworkTable table = inst.getTable("datatable");
		int traces = 3;
		Random random = new Random();
		int maxScore = 10;
		double info[] = new double[3];
		int id = 0;
		plotData = table.getEntry("PlotData");
		// String lpath=System.getProperty("java.library.path");
		// System.out.println(lpath);
		newPlot = table.getEntry("Plot");

		inst.startServer();
	
		while (true) {
			double tm = 0;
			info[0] = id;// plot id
			info[1] = traces; // 1 trace
			info[2] = maxDataPoints;

			try {				
				Thread.sleep(10000); // new plot every 10 seconds
				newPlot.setDoubleArray(info);

				System.out.println("Plot" + id);

				for (int i = 0; i < maxDataPoints; i++) {
					plotData = table.getEntry("PlotData"+i);
					double data[] = new double[traces + 2];

					data[0] = (double) i;
					data[1] = tm;

					for (int j = 0; j < traces; j++) {
						data[j + 2] = (double) random.nextDouble() * maxScore;
					}
					plotData.setDoubleArray(data);

					tm += 0.02;
				}
				id++;
			} catch (InterruptedException ex) {
				System.out.println("exception)");
				inst.stopServer();
				break;
			}
		}
	}
}
