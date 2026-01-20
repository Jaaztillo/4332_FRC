// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.LimelightConstants;
import frc.robot.LimelightHelpers; // Import the helper class (adjust package if necessary)
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard; // Optional: For viewing data on the dashboard

public class Limelight_Subsystem extends SubsystemBase {
  /** Creates a new Limelight_Subsystem. */
  public Limelight_Subsystem() {}

  /** Detects a tag on the right side (shoot right) */
  public boolean has_AprilTag_Right() {
      int id = (int) LimelightHelpers.getFiducialID(LimelightConstants.name);
      return id == 24 || id == 8;
  }

  /** Detects a tag on the left side (shoot left) */
  public boolean has_AprilTag_Left() {
      int id = (int) LimelightHelpers.getFiducialID(LimelightConstants.name);
      return id == 11 || id == 27;
  }

  /** Get left tank output */
  public double get_left_output() {
      double[] outputs = calculateOutputs();
      return outputs[0];
  }

  /** Get right tank output */
  public double get_right_output() {
      double[] outputs = calculateOutputs();
      return outputs[1];
  }

  /** Core logic for calculating tank outputs */
  private double[] calculateOutputs() {
      // Default stop
      double left = 0;
      double right = 0;

      // Check which tag is visible
      if (!has_AprilTag_Left() && !has_AprilTag_Right()) {
          return new double[]{0, 0};
      }

      // Get Limelight values
      double tx = LimelightHelpers.getTX(LimelightConstants.name);                            // horizontal error
      double z = LimelightHelpers.getTargetPose3d_RobotSpace(LimelightConstants.name).getZ(); // distance

      // Apply yaw offset based on April Tag
      if (has_AprilTag_Left()) {
          tx -= LimelightConstants.SIDE_YAW_OFFSET; // turn robot left
      } else if (has_AprilTag_Right()) {
          tx += LimelightConstants.SIDE_YAW_OFFSET; // turn robot right
      }

      // Calculate turn and drive
      double turn = tx * LimelightConstants.kTurnP;
      double drive = (z - LimelightConstants.DESIRED_DISTANCE) * LimelightConstants.kDriveP;

      // Combine for tank drive
      left = clamp(drive + turn);
      right = clamp(drive - turn);

      return new double[]{left, right};
  }

  /** Clamp output to max/min */
  private double clamp(double value) {
      return Math.max(-LimelightConstants.MAX_OUTPUT, Math.min(LimelightConstants.MAX_OUTPUT, value));
  }

  @Override
  public void periodic() {
      // Show info on SmartDashboard
      SmartDashboard.putBoolean("Has AprilTag", has_AprilTag_Left() || has_AprilTag_Right());
      SmartDashboard.putNumber("AprilTag ID", LimelightHelpers.getFiducialID(LimelightConstants.name));
  }
}
