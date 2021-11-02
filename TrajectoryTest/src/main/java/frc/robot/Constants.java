/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public interface Constants {
  // Motor IDs
  public static final int FRONT_LEFT = 1;
  public static final int BACK_LEFT = 2;
  public static final int FRONT_RIGHT = 3;
  public static final int BACK_RIGHT = 4;

  // Controller IDs
  public static final int DRIVER = 0;
  // These are button IDs
  public static final int LEFT_JOYSTICK = 1;
  public static final int RIGHT_JOYSTICK = 4;
  // General Constants
  public static final int TIMEOUT = 10;
 
  public static final int LEFT_POSITION = 0;
  public static final int CENTER_POSITION = 1;
  public static final int RIGHT_POSITION = 2;
  public static final int ILLEGAL_POSITION = 3;
  public static final double maxSpeed = 2.0;
  public static final double maxAccel = 5;
  public static final double trackWidth = 0.381 * 2;
  public static final double wheelRadius = 0.0508;

  // 1/2 rotation per second.
  public static final double maxAngularSpeed = Math.PI;

}
