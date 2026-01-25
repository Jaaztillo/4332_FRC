/*
 * MASTER PROGRAMMERS WORK
 */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.LimelightConstants;
import frc.robot.LimelightHelpers;

public class Limelight_Subsystem extends SubsystemBase {
  // ID DEBOUNCE
  private int lastValidTagID = -1;
  private double lastSeenTime = 0.0;

  // DISTANCE FILTERING (EMA)
  private double filteredZ = 0.0;
  private boolean hasZInit = false;
  private static final double Z_ALPHA = 0.2; // lower = smoother

  public Limelight_Subsystem() {}
  
  // TAG DETECTION
  public boolean hasValidTag() {
    return getDebouncedTagID() != -1;
  }

  public boolean has_AprilTag_Left() {
    int id = getDebouncedTagID();
    return id == LimelightConstants.APRILTAG_LEFT[0]
        || id == LimelightConstants.APRILTAG_LEFT[1];
  }

  public boolean has_AprilTag_Right() {
    int id = getDebouncedTagID();
    return id == LimelightConstants.APRILTAG_RIGHT[0]
        || id == LimelightConstants.APRILTAG_RIGHT[1];
  }

  public boolean has_AprilTag_Climb() {
    int id = getDebouncedTagID();
    return id == LimelightConstants.APRILTAG_CLIMB[0]
        || id == LimelightConstants.APRILTAG_CLIMB[1];
  }

  // GET TX
  public double getTX ()
  {
    return LimelightHelpers.getTX(LimelightConstants.name);
  }

  // FILTERED DISTANCE
  public double getFilteredDistanceZ() {
    double rawZ =
        LimelightHelpers
            .getTargetPose3d_RobotSpace(LimelightConstants.name)
            .getZ();

    if (!hasZInit) {
      filteredZ = rawZ;
      hasZInit = true;
    } else {
      filteredZ = (Z_ALPHA * rawZ) + ((1 - Z_ALPHA) * filteredZ);
    }

    return filteredZ;
  }
  
  // TAG ID DEBOUNCE
  private int getDebouncedTagID() {
    int currentID = (int) LimelightHelpers.getFiducialID(LimelightConstants.name);

    if (currentID != -1) {
      lastValidTagID = currentID;
      lastSeenTime = Timer.getFPGATimestamp();
    }

    if (Timer.getFPGATimestamp() - lastSeenTime < LimelightConstants.ID_TIMEOUT) {
      return lastValidTagID;
    }

    return -1;
  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("Has AprilTag", hasValidTag());
    SmartDashboard.putNumber("Debounced Tag ID", getDebouncedTagID());
    SmartDashboard.putNumber("Filtered Z", filteredZ);
  }
}
