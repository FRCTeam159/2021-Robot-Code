/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
//import frc.robot.Robot;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.subsystems.SimDriveTrain;

/**
 * An example command.  You can replace me with your own command.
 */
public class DriveWithGamepad extends CommandBase implements Constants{
  SlewRateLimiter m_speedLimiter = new SlewRateLimiter(3);
  SlewRateLimiter m_rotLimiter = new SlewRateLimiter(3);
  private SimDriveTrain m_drive;
public DriveWithGamepad(SimDriveTrain drive) {
    // Use requires() here to declare subsystem dependencies
    m_drive=drive;
    addRequirements(drive);
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
    XboxController stick = OI.driverController;
   
    double minc=5e-3;
    double left= stick.getRawAxis(LEFT_JOYSTICK);
    double right=stick.getRawAxis(RIGHT_JOYSTICK);

    //double right= stick.getLeftX();
    //double left=stick.getLeftY();

    left=Math.abs(left)<minc?0:left;
    right=Math.abs(right)<minc?0:right;

    //System.out.println("left:"+left+" right:"+right);
    double xSpeed = -m_speedLimiter.calculate(left) * maxSpeed;

    // Get the rate of angular rotation. We are inverting this because we want a
    // positive value when we pull to the left (remember, CCW is positive in
    // mathematics). Xbox controllers return positive values when you pull to
    // the right by default.
    double rot = -m_rotLimiter.calculate(right) * maxAngularSpeed;
    
    //System.out.println(left+" "+right);
    m_drive.drive(xSpeed, rot);
  
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return false;
  }

}

