// PlotTestServer (host-based network tables server)
// Emulates a roborio (Robot) network server
// - On startup generates a new NetworkTable table named "datatable"
// - every 10 seconds publishes to the table an array object called NewPlot+id
// - fills plot data with random points (n-traces)
// 
// PlotTestClient
// - started first (before PlotTestServer is run)
// - waits for PlotTestSever to generate "datatable" server 
// - For every NewPlot+id that's published displays a java plotwindow that contains its data

import java.util.Random;


import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableEntry;

public class PlotTestServer {
	static int maxDataPoints = 100;

	NetworkTableEntry newPlot;
	NetworkTableEntry traceData[];

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
		traceData = new NetworkTableEntry[traces];
		// String lpath=System.getProperty("java.library.path");
		// System.out.println(lpath);
		newPlot = table.getEntry("Plot");

		for (int j = 0; j < traces; j++) {
			traceData[j] = table.getEntry("PlotData" + j);
		}
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
					double data[] = new double[traces + 2];

					data[0] = (double) i;
					data[1] = tm;

					for (int j = 0; j < traces; j++) {
						data[j + 2] = (double) random.nextDouble() * maxScore;
						traceData[j].setDoubleArray(data);
					}
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
