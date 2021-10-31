package tests;
// PlotTestClient
// plots data sent by a NetworkTable server
// - On startup looks at optional "ip" argument (set in launch.json)
//  1) fake data sent by running PlotTestServer main function included in this project 
//   - ip=localhost (default if no program arguments given)
//  2) Pathfinder data generated on the Roborio as part of the Robot program
//     ip=10.1.59.2
// - Network client attaches to a server named "datatable"
// - waits for a new array string called NewPlot+id to appear in the table
// - decodes the number of points (npoints) and traces (ntraces)in the plot from the array data
// - waits for new entries named PlotData+i to appear in the table i=0 to npoints*ntraces
// - when all expected points have arrived and saved into a data buffer outputs a new java plot of the data
// - increments the plot id counter (id)
// - waits for another NewPlot+id message to appear in the table

import java.util.ArrayList;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import edu.wpi.first.networktables.ConnectionNotification;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.EntryNotification;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import utils.PathData;
import utils.PlotPath;
import edu.wpi.first.networktables.NetworkTableEntry;

public class PlotTestClient implements Consumer<EntryNotification>, EntryListenerFlags {
	ArrayList<PathData> list = new ArrayList<PathData>();
	int traces = 0;
	int index = 0;
	int points = 0;
	int count = 0;
	int plot_count = 0;
	int id = 0;

	static NetworkTable table;
	NetworkTableInstance inst;
	NetworkTableEntry newPlot;
	NetworkTableEntry plotData;

	public static void main(String[] args) {
		new PlotTestClient(args).run();
	}

	public PlotTestClient(String[] args) {
		inst = NetworkTableInstance.getDefault();
		table = inst.getTable("datatable");
		// NetworkTable.setIPAddress("10.1.59.2");
		System.out.println("new PlotTestClient " + args.length);
		if (args.length == 0)
			inst.startClient("localhost");
		else {
			inst.startClient(args[0]);
		}
		newPlot = table.getEntry("Plot");
		plotData = table.getEntry("PlotData");

		inst.addEntryListener("/datatable/Plot", this, kLocal | kNew | kUpdate);
	}

	public void run() {
		// table.addTableListener(this,true);
		while (true) {
			try {
				Thread.sleep(1000);
				// System.out.println("PlotTestClient waiting");
			} catch (InterruptedException ex) {
				System.out.println("exception)");
			}
		}
	}

	private static void createAndShowGui(int id, ArrayList<PathData> list, int traces) {
		JFrame frame = new PlotPath(list, traces);
		frame.setTitle("Path Plot " + id);

		System.out.println("Showing plot: Size = " + list.size());
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	public void accept(EntryNotification t) {
		if (t.name.equals("/datatable/Plot")) {
			list.clear();
			double info[] = newPlot.getDoubleArray(new double[0]);
			id = (int) info[0];
			traces = (int) info[1];
			points = (int) info[2];
			index = 0;
			count = 0;
			System.out.println("NewPlot:" + id + " " + traces + " " + points);
		} else if (t.name.contains("/datatable/PlotData")) {
			//String idx=t.name.substring(t.name.length()-1);
			//ndex=Integer.parseInt(idx);
			NetworkTableEntry plotData = table.getEntry("PlotData" + index);
			double data[] = plotData.getDoubleArray(new double[0]);

			PathData pd = new PathData();
			index = (int) data[0];
			pd.tm = data[1];
			for (int i = 0; i < data.length - 2; i++) {
				pd.d[i] = data[i + 2];
			}
			list.add(pd);
			index++;
			if (index == points) {
				index = 0;
				//plot_count++;
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						createAndShowGui(id, list, traces);
					}
				});
			}

		}
	}
}
