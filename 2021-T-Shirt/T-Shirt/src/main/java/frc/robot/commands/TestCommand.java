// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.TestOne;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.ToggleButton;

public class TestCommand extends CommandBase{
  public final TestOne testOne;
  public JoystickButton testjoybutton = new JoystickButton(OI.stick, 2);
  ToggleButton testbutton = new ToggleButton(testjoybutton);
  public TestCommand(TestOne tO) {
    testOne = tO;
    addRequirements(tO);
 
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  public void initialize() {
    System.out.println("TestCommand initialized");
  }

  // Called repeatedly when this Command is scheduled to run
  public void execute() {
 //ToggleButton testtoggle = new ToggleButton(Robot.testtoggle);
    Joystick stick = OI.stick;
    double rtVal = stick.getRawAxis(3);
    double ltVal =  -stick.getRawAxis(2);
  
    if(rtVal > 0.05 && ltVal > -0.05){
      testOne.spinTest(rtVal * 0.15 );
    }else if(rtVal < 0.05 && ltVal < -0.05 ){
      testOne.spinTest(ltVal * 0.15 );
    } else if(rtVal < 0.05 && ltVal > -0.05){
      testOne.spinTest(0.0);
    }
    if(testbutton.newState()){
      testOne.toggleSpin();
      testOne.doCount();
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  public boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  public void end() {}

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  public void interrupted() {}
}
