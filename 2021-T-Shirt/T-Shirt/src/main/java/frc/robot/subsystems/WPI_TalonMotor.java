// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/** Add your docs here. */
public class WPI_TalonMotor extends WPI_TalonSRX implements MotorInterface {

    WPI_TalonMotor(int id) {
        super(id);
    }

    @Override
    public double getRotations() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void set(double speed) {
        // TODO Auto-generated method stub
        super.set(speed);
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }
}
