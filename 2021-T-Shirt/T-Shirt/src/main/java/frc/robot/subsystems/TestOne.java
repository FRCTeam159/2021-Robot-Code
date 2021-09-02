// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.RobotContainer;
import frc.robot.RobotMap;
import frc.robot.subsystems.SparkMotor;
import frc.robot.OI;
import edu.wpi.first.wpilibj.Solenoid;

/** Add your docs here. */
public class TestOne extends SubsystemBase {
  public Solenoid fire;
  private SparkMotor spinnyBoi;
  private boolean yes = true;
  private double stick = 0.0;
  public boolean isspinning = false;
  public int count = 0;
 
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public TestOne() {
   spinnyBoi = new SparkMotor(RobotMap.TSHIRT_TILT_MOTOR);
   fire = new Solenoid(RobotMap.FIRE_PISTON);
  
  }
  public void spinMotor(){
 System.out.println("motor turning = " + isTurning());

  }
  public boolean isTurning(){
    if(stick < 0.05 && stick > -0.05 ){
      System.out.println("the stick is at 0");
      yes = false;
      } else{
        System.out.println("the stick is not at 0");
        yes = true;
      }

    return yes;
  }
  public void spinTest(double spinVal){
   // System.out.println("the spinval is " + spinVal);
   // spinnyBoi.set(spinVal);
  }
  public void toggleSpin(){
switch (count){
case 0:
spinnyBoi.set(-0.1);
System.out.println("count is " + count);
break;
case 1:
spinnyBoi.set(0.0);
System.out.println("count is " + count);
break;
case 2:
spinnyBoi.set(0.1);
System.out.println("count is " + count);
break;
case 3:
spinnyBoi.set(0.0);
break;
 }
  }
  public boolean isspintoggled(){
    return isspinning;
 
  }
  public void doCount(){
    count++;
    if(count>3){
      count = 0;
    }
    System.out.println("count done count is " + count);
  }
}