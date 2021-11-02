package utils;
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;

/** Add your docs here. */
public class PlotTrajectoryUtils {
    private static final boolean printCalculatedTrajectory = false;
    private static final boolean printCalculatedPath = false;
    static ArrayList<PathData> data = new ArrayList<>();
    
    public static void plotPathMotion(List<Trajectory.State> list, double chassis_width) {
        data.clear();
        for (int i = 0; i < list.size(); i++) {
            Trajectory.State state = list.get(i);
            double tm = state.timeSeconds;
            double x = state.poseMeters.getX();
            double y = state.poseMeters.getY();
            PathData pd = new PathData();
            Rotation2d heading = state.poseMeters.getRotation();
            double cos_angle = heading.getCos();
            double sin_angle = heading.getSin();
            double w = 0.5 * chassis_width;

            double lx = x - (w * sin_angle);
            double ly = y + (w * cos_angle);
            double rx = x + (w * sin_angle);
            double ry = y - (w * cos_angle);

            pd.tm = tm;
            pd.d[0] = metersToFeet(lx);
            pd.d[1] = metersToFeet(ly);
            pd.d[2] = metersToFeet(x);
            pd.d[3] = metersToFeet(y);
            pd.d[4] = metersToFeet(rx);
            pd.d[5] = metersToFeet(ry);
            if (printCalculatedPath)
                System.out.format("%d %f %f %f %f %f %f %f\n", i, tm, lx, ly, x, y, rx, ry);
            data.add(pd);
        }
        String label_list[] = { "Left", "Center", "Right" };
        JFrame frame = new PlotPath(data, 3, PlotPath.XY_MODE, label_list);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void plotPathDynamics(List<Trajectory.State> list) {
        data.clear();
        for (int i = 0; i < list.size(); i++) {
            Trajectory.State state = list.get(i);
            double tm = state.timeSeconds;
            double x = state.poseMeters.getX();
            double y = state.poseMeters.getY();
            double v = state.velocityMetersPerSecond;
            double a = state.accelerationMetersPerSecondSq;
            double h = state.poseMeters.getRotation().getRadians();
            if (printCalculatedTrajectory)
                System.out.format("%d %f %f %f %f %f %f\n", i, tm, x, y, v, a, h);
            PathData pd = new PathData();
            pd.tm = tm;
            pd.d[0] = metersToFeet(x);
            pd.d[1] = metersToFeet(y);
            pd.d[2] = metersToFeet(v);
            pd.d[3] = metersToFeet(a);
            pd.d[4] = h;
            data.add(pd);
        }
        String label_list[] = { "X", "Y", "Velocity", "Acceleration", "Heading" };
        JFrame frame = new PlotPath(data, 5, PlotPath.TIME_MODE, label_list);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    private static double metersToFeet(double meters) {
        return meters * 100 / (2.54 * 12);
    }

}
