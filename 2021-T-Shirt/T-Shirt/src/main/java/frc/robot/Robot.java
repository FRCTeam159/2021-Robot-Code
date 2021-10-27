// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.Axon.TargetData;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  //private static final String[] String = null;

  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;
  public static JoystickButton targetButton;
  public static JoystickButton cameraButton;
  public static JoystickButton toggleLaunchButton;
  public static JoystickButton testtoggle;
  public static JoystickButton intakeButton;
  public static JoystickButton shiftButton;
  public static JoystickButton climbButton;
  public static double leftTriggerPressed;
  public static double rightTriggerPressed;
  public static boolean isAuto;

  NetworkTable table;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer. This will perform all our button bindings,
    // and put our
    // autonomous chooser on the dashboard.
    testtoggle = new JoystickButton(OI.stick, 1);
    m_robotContainer = new RobotContainer();
    table = NetworkTableInstance.getDefault().getTable("ML");
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler. This is responsible for polling buttons, adding
    // newly-scheduled
    // commands, running already-scheduled commands, removing finished or
    // interrupted commands,
    // and running subsystem periodic() methods. This must be called from the
    // robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    targetTest();
  }

  /**
   * This autonomous runs the autonomous command selected by your
   * {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    targetTest();
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
    targetTest();
  }
  public void targetTest(){
    RobotContainer.axon.getTargets();
    SmartDashboard.putNumber("fps", RobotContainer.axon.getFps());
    SmartDashboard.putNumber("num-targets", RobotContainer.axon.numTargets());
    TargetData target=RobotContainer.axon.getTarget();
    double xmax = 0;
    double xmin = 0;
    double ymin = 0;
    double ymax = 0;
    String tstr = "";
    double conf = 0;
    if (target != null) {
      tstr = target.toString();
      conf = target.confidence;
      xmin = target.rect.ul.x;
      xmax = target.rect.lr.x;
      ymin = target.rect.ul.y;
      ymax = target.rect.lr.y;
    }
    SmartDashboard.putString("target", tstr);
    SmartDashboard.putNumber("Confidence", conf);
    SmartDashboard.putNumber("xmin", xmin);
    SmartDashboard.putNumber("xmax", xmax);
    SmartDashboard.putNumber("ymin", ymin);
    SmartDashboard.putNumber("ymax", ymax);
  }

}
