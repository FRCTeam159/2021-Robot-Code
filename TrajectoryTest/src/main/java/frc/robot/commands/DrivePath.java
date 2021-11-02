// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.List;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.SimDriveTrain;
import utils.PlotTrajectoryUtils;

public class DrivePath extends CommandBase implements Constants {
  private final RamseteController m_ramsete = new RamseteController();
  private final Timer m_timer = new Timer();
  private Trajectory m_trajectory;
  boolean plotCalculatedPath = true;
  boolean plotCalculatedTrajectory = true;
  public static boolean first_call = true;
  private SimDriveTrain m_drive;

  /** Creates a new DrivePath. */
  public DrivePath(SimDriveTrain drive, Trajectory traj){
    m_drive=drive;
    setTrajectory(traj);
  }
  public DrivePath(SimDriveTrain drive) {
    m_drive=drive;
    Trajectory traj = TrajectoryGenerator.generateTrajectory
        (new Pose2d(2, 2, new Rotation2d()), List.of(),
        new Pose2d(6, 4, new Rotation2d()), 
        new TrajectoryConfig(maxSpeed, maxAccel)
        );
        setTrajectory(traj);
  }

  void setTrajectory(Trajectory t){
    addRequirements(m_drive);
    m_trajectory = t;
    if (first_call) {
      Trajectory trajectory = m_trajectory.relativeTo(m_trajectory.getInitialPose());
      List<Trajectory.State> list = trajectory.getStates();
      if (plotCalculatedPath) {
        PlotTrajectoryUtils.plotPathMotion(list, trackWidth);
      }
      if (plotCalculatedTrajectory) {
        PlotTrajectoryUtils.plotPathDynamics(list);
      }
    }
    first_call=false;
  }
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("DrivePath.initialize");
    m_timer.reset();
    m_timer.start();
    m_drive.resetOdometry(m_trajectory.getInitialPose());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double elapsed = m_timer.get();
    Trajectory.State reference = m_trajectory.sample(elapsed);
    ChassisSpeeds speeds = m_ramsete.calculate(m_drive.getPose(), reference);
    m_drive.drive(speeds.vxMetersPerSecond, speeds.omegaRadiansPerSecond);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("DrivePath.end:" + interrupted);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
