// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/** Add your docs here. */
public class TalonMotor extends TalonSRX implements MotorInterface {
    TalonMotor(int id) {
        super(id);
    }
    public double getRotations() {
        return 0;
    }

    public void set(double arg) {
        super.set(ControlMode.PercentOutput, arg);
    }

    public void reset() {

    }
}
