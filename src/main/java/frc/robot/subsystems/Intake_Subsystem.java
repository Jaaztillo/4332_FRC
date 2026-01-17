// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

// Import the necessary WPI_TalonSRX class from the phoenix library
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import frc.robot.Constants.IntakeConstants;

public class Intake_Subsystem extends SubsystemBase {
  // Define WPI_TalonSRX motor controllers as private final members
  private final WPI_TalonSRX TalonSRX_Intake = new WPI_TalonSRX(IntakeConstants.intake_ID);

  /** Creates a new Intake_Subsystem. */
  public Intake_Subsystem() 
  {
    TalonSRX_Intake.setNeutralMode(NeutralMode.Brake); // Motors stop immediately when no power is applied
    TalonSRX_Intake.neutralOutput(); // Ensure the motor is not running initially
    TalonSRX_Intake.setSensorPhase(false); // Adjust sensor phase if necessary

    // Configure limit switches connected via the Feedback Connector
    TalonSRX_Intake.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
    TalonSRX_Intake.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
    
    // Set nominal (minimum) output to zero
    TalonSRX_Intake.configNominalOutputForward(0.0, 0);
    TalonSRX_Intake.configNominalOutputReverse(0.0, 0);
  }

  public void intake ()
  {
    TalonSRX_Intake.set(1.0);
  }

  public void stop ()
  {
    TalonSRX_Intake.set(0.0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
