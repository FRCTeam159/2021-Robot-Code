// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.DriveWithGamepad;
import frc.robot.commands.autocommand;
import frc.robot.subsystems.TestOne;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Axon;

import frc.robot.commands.TestCommand;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  Compressor compressor;
  public static boolean pancake = true;
  public static final DriveTrain driveTrain = new DriveTrain();
  public static final Axon axon = new Axon(0.6);
  private final DriveWithGamepad driveWithGamepad = new DriveWithGamepad(driveTrain);
  public static final TestOne testOne = new TestOne();
  private final TestCommand testCommand = new TestCommand(testOne);
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    driveTrain.setDefaultCommand(driveWithGamepad);
    testOne.setDefaultCommand(testCommand);
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {}

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
   return new autocommand();
  }
}
