package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.LimelightConstants;
import frc.robot.LimelightHelpers;

public class LimelightSubsystem extends SubsystemBase {
  private double filteredZ;
  
  /**
   * Limelight constructor
   */
  public LimelightSubsystem() {
    this.filteredZ = 0.0;
  }
  
  /**
   * checks for a valid tag
   * @return isValid tag
   */
  public boolean hasValidTag() {
    return getDebouncedTagID() != -1;
  }

  /**
   * checks for a valid shooting tag
   * @return isValid shooting tag
   */
  public boolean has_AprilTag_Shoot() {
    int id = getDebouncedTagID();
    return LimelightConstants.Apriltag_Shoot.contains(id);
  }

  /**
   * checks for a valid climbing tag
   * @return isValid climbing tag
   */
  public boolean has_AprilTag_Climb() {
    int id = getDebouncedTagID();
    return LimelightConstants.Apriltag_Climb.contains(id);
  }

  /**
   * Limelight "Point-of-Interest" Tracking
   * The Limelight web interface has a built-in feature to offset the target center relative to a detected AprilTag. 
   * Go to the Input tab in your Limelight 3D AprilTag pipeline.
   * Look for 3D Configuration or Point-of-Interest Tracking.
   * Define the 3D offset (in meters) from the AprilTag to the center of the hub (e.g., if the hub center is 0.5 meters to the right, enter that into the X-offset field).
   * The Limelight will now treat that new point as the target, and LimelightHelpers.getTX() will report the angle to the center, not the tag. 
   */

  /**
   * Gets the angle the april tag is at in the x-plane
   * @return angle of april tag
   */
  public double getTX ()
  {
    return LimelightHelpers.getTX(LimelightConstants.Name);
  }

  /**
   * get the distance from the april tag to the robot
   * @return filtered distance from target to robot
   */
  public double getFilteredDistanceZ() {
    double rawZ = LimelightHelpers.getTargetPose3d_RobotSpace(LimelightConstants.Name).getZ();

    if (!LimelightHelpers.getTV(LimelightConstants.Name)) return filteredZ;

    return LimelightConstants.Distance_Filter.calculate(rawZ);
  }
  
  /**
   * gets the current tag id
   * @return tag id
   */
  private int getDebouncedTagID() {
    return (int) LimelightHelpers.getFiducialID(LimelightConstants.Name);
  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("AprilTag", hasValidTag());
    SmartDashboard.putNumber("AprilTag Distance", getFilteredDistanceZ());
    SmartDashboard.putNumber("AprilTag Angle", getTX());
  }
}
