package tests;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.Filesystem;
import utils.PathData;
import utils.PlotTrajectoryUtils;

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/** Add your docs here. */
public class PathWeaverTest {
    // String trajectoryJSON = "paths/left-scale-from-right.wpilib.json";
    ///String trajectoryJSON = "paths/right-switch-from-right.wpilib.json";

    // String trajectoryJSON = "paths/right-switch-from-left.wpilib.json";
     String trajectoryJSON = "paths/wierd-path.wpilib.json";

    private static final boolean plotCalculatedTrajectory = true;
    private static final boolean plotCalculatedPath = true;
    public static boolean usePathWeaverPath = true;

    Trajectory trajectory = null;
    double chassis_width = 0.5;
    public static double MAX_VEL = 2;
    public static double MAX_ACC = 5;

    double distance = feetToMeters(10); // forward distance
    double offset = feetToMeters(3); // turn distance

    List<Trajectory.State> list;
    ArrayList<PathData> data = new ArrayList<>();

    public static void main(String[] args) {
        PathWeaverTest test = new PathWeaverTest();
        test.showPath();
    }

    public void showPath() {
        try {
            if (usePathWeaverPath) {
                Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
                System.out.println(trajectoryPath);
                trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
            } else
                trajectory=makeTestPath();
            trajectory = trajectory.relativeTo(trajectory.getInitialPose());
            list = trajectory.getStates();
            if (plotCalculatedPath) {
                PlotTrajectoryUtils.plotPathMotion(list,chassis_width);
            }
            if (plotCalculatedTrajectory) {
                PlotTrajectoryUtils.plotPathDynamics(list);
            }
        } catch (IOException e) {
            System.out.println("Unable to create trajectory: " + trajectoryJSON);
            e.printStackTrace(System.out);
        }
    }

    Trajectory makeTestPath() {
        TrajectoryConfig config = new TrajectoryConfig(MAX_VEL,MAX_ACC);

        // An example trajectory to follow. All units in meters.

        return TrajectoryGenerator.generateTrajectory(
                List.of(
                    new Pose2d(0, 0, new Rotation2d(0)),
                    new Pose2d(distance-offset, 0, new Rotation2d(0)),
                    new Pose2d(distance, offset, new Rotation2d(0.5*Math.PI))
                    ),
                config);
    }

    private static double feetToMeters(double feet) {
        return 2.54 * 12 * feet / 100;
    }
   
}
