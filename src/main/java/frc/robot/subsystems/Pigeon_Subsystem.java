// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.MathUtil;

// Pigeon 2.0
import com.ctre.phoenix6.hardware.Pigeon2;

import frc.robot.Constants.PigeonConstants;

public class Pigeon_Subsystem extends SubsystemBase {
  private Pigeon2 gyroscope = new Pigeon2(PigeonConstants.Pigeon_ID);

  /** Creates a new Pigeon_Subsystem. */
  public Pigeon_Subsystem() {}

  public void resetYaw ()
  {
    gyroscope.reset();
  }

  // GET RAW HEADING
  public double getHeading()
  {
    return gyroscope.getYaw().getValueAsDouble();
  }

  // GET HEADING FROM -180 to 180
  public double getHeadingWrapped () 
  {
      return MathUtil.angleModulus(getHeading());
  }

  // GET HOW MUCH WE NEED TO TURN
  public double getHeadingError(double targetHeading) {
    return MathUtil.angleModulus(targetHeading - getHeading());
  }

  public void setHeading (double degree)
  {
    gyroscope.setYaw(degree);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
