// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.TankDriveConstants;

// spark imports (SparkMax, SparkMaxConfig, MotorType, PersistMode, ResetMode)
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

public class TankDrive_Subsystem extends SubsystemBase 
{
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  // Motors | Right side
  private final SparkMax left_leader = new SparkMax(TankDriveConstants.left_leader_ID, MotorType.kBrushed);
  private final SparkMax left_follower = new SparkMax(TankDriveConstants.left_follower_ID, MotorType.kBrushed);

  // Motors | Right side
  private final SparkMax right_leader = new SparkMax(TankDriveConstants.right_leader_ID, MotorType.kBrushed);
  private final SparkMax right_follower = new SparkMax(TankDriveConstants.right_follower_ID, MotorType.kBrushed);

  // Differential Drive
  private final DifferentialDrive drive = new DifferentialDrive(left_leader, right_leader);

  private final SparkMaxConfig drive_config = new SparkMaxConfig();

  /** Creates a new TankDrive_Subsystem. */
  @SuppressWarnings("removal")
  public TankDrive_Subsystem () 
  {
    m_chooser.setDefaultOption("Default Auto", TankDriveConstants.kDefault_Auto);
    m_chooser.addOption("My Auto", TankDriveConstants.kCustom_Auto);
    SmartDashboard.putData("Auto choices", m_chooser);

    // setting limits to the sparks
    drive_config.smartCurrentLimit(60);
    drive_config.voltageCompensation(12);

    // setting leftFollower to follow leftLeader
    drive_config.follow(left_leader);
    left_follower.configure(drive_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    // setting rightFollower to follow rightLeader
    drive_config.follow(right_leader);
    right_follower.configure(drive_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    // setting it so that the left and right motors are no longer followers of anyone else
    drive_config.disableFollowerMode();
    right_follower.configure(drive_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    left_follower.configure(drive_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    // MIGHT NEED TO INVERT RIGHT WHEELS OR LEFT WHEELS SO UNCOMMENT THIS OUT IF NEEDED
    // right_leader.setInverted(true);
    // right_follower.setInverted(true);
  }

  public void drive (double Y, double X)
  {
    drive.arcadeDrive(Y, X);
  }

  public void stop ()
  {
    left_leader.stopMotor();
    right_leader.stopMotor();
  }

  public double get_percent_out_left ()
  {
    return left_leader.get();
  }

  public double get_percent_out_right ()
  {
    return right_leader.get();
  }

  @Override
  public void periodic() 
  {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("LEFT PERCENT OUT", get_percent_out_left());
    SmartDashboard.putNumber("RIGHT PERCENT OUT", get_percent_out_right());
  }
}
