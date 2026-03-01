/**
   * Limelight "Point-of-Interest" Tracking
   * The Limelight web interface has a built-in feature to offset the target center relative to a detected AprilTag. 
   * Go to the Input tab in your Limelight 3D AprilTag pipeline.
   * Look for 3D Configuration or Point-of-Interest Tracking.
   * Define the 3D offset (in meters) from the AprilTag to the center of the hub (e.g., if the hub center is 0.5 meters to the right, enter that into the X-offset field).
   * The Limelight will now treat that new point as the target, and LimelightHelpers.getTX() will report the angle to the center, not the tag. 
   */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

// Limelight helpers
import frc.robot.LimelightHelpers;

// Geometry
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
// Smart Dashboard
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// Timer
import edu.wpi.first.wpilibj.Timer;

// Spark Max
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.RelativeEncoder;

import frc.robot.Constants.LimelightConstants;

public class LimelightSubsystem extends SubsystemBase {
  // Limelight Motor
  private final SparkMax limelightMotor = new SparkMax(LimelightConstants.Limelight_Motor_Id, MotorType.kBrushless);

  // Encoder
  private final RelativeEncoder encoder = limelightMotor.getEncoder();

  // Controller
  private final SparkClosedLoopController motorController = limelightMotor.getClosedLoopController();

  // Motor configuration
  private final SparkMaxConfig motorConfig = new SparkMaxConfig();

  // Pose timeout (seconds)
  private final double Field_Pose_Timeout = 1.5;

  // Current Target
  private Pose3d currentTarget = null;

  // Cached field pose
  private Pose3d lastFieldPose = null;

  // Timestamp of last valid pose
  private double lastPoseTimestamp = 0.0;

  // Current Heading Error
  private double headingError = 0.0;
  
  /**
   * Limelight constructor
   */
  @SuppressWarnings({"removal"})
  public LimelightSubsystem() {
    // Configure the limelight neo motor
    motorConfig
      .smartCurrentLimit(40)
      .voltageCompensation(12);

    // Apply the configuration
    limelightMotor.configure(motorConfig, SparkBase.ResetMode.kResetSafeParameters, SparkBase.PersistMode.kPersistParameters);

    // Set Encoder Position to Initial Position
    encoder.setPosition(0);
  }
  
  /**
   * checks for a valid tag
   * @return isValid tag
   */
  public boolean hasValidTag() {
    return getTagID() != -1;
  }

  /**
   * checks for a valid outpost tag
   * @return isValid shooting tag
   */
  public boolean hasAprilTagOutpost() {
    int id = getTagID();
    return LimelightConstants.Apriltag_Outpost.contains(id);
  }

  /**
   * checks for a valid shooting tag which is foward
   * @return isValid shooting tag
   */
  public boolean hasAprilTagShoot() {
    int id = getTagID();
    return LimelightConstants.Apriltag_Shoot.contains(id);
  }

  /**
   * checks for a valid shooting tag on the right side
   * @return isValid shooting tag
   */
  public boolean hasAprilTagShootRight() {
    int id = getTagID();
    return LimelightConstants.Apriltag_Shoot_Right.contains(id);
  }

  /**
   * checks for a valid shooting tag on the left side
   * @return isValid shooting tag
   */
  public boolean hasAprilTagShootLeft() {
    int id = getTagID();
    return LimelightConstants.Apriltag_Shoot_Left.contains(id);
  }

  /**
   * checks for a valid climbing tag
   * @return isValid climbing tag
   */
  public boolean hasAprilTagClimb() {
    int id = getTagID();
    return LimelightConstants.Apriltag_Climb.contains(id);
  }

  /**
   * Offsets the current april tag and sets a new virtual april tag with the offset
   * @param foward the relative-foward offset
   * @param left the relative-left offset
   * @param up the relative-up offset
   * @return new virtual april tag pose
   */
  public void setTarget (double foward, double left, double up) {
    Pose3d tagPose = LimelightHelpers.getTargetPose3d_RobotSpace(LimelightConstants.Name);

    Transform3d tagToTarget = new Transform3d(
      new Translation3d(foward, left, up),
      new Rotation3d()
    );

    currentTarget = tagPose.transformBy(tagToTarget);
  }

  /**
   * Gets the current target of the limelight
   * @return the current target of the limelight
   */
  public Pose3d getTarget () { return currentTarget; }

  /**
   * Get the tx of the current april tag
   * @return the angle to the current target in the x axis
   */
  public double getTx () {
    return Math.toDegrees(
      Math.atan2(currentTarget.getX(), currentTarget.getZ())
    );
  }

  /**
   * Gets the robots field-relative pose
   * @return field-relative pose of robot
   */
  public Pose3d getFieldRelativePose() {
    double now = Timer.getFPGATimestamp();

    // If there is a valid tag compute a new pose
    if (hasValidTag()) {
      Pose3d tagFieldPose = LimelightConstants.tagFieldMap.get(getTagID());
      if (tagFieldPose == null) return null;

      Pose3d cameraToTagPose =
          LimelightHelpers.getTargetPose3d_RobotSpace(LimelightConstants.Name);

      Transform3d cameraToTag = new Transform3d(
          cameraToTagPose.getTranslation(),
          cameraToTagPose.getRotation()
      );

      Transform3d cameraToRobot = new Transform3d(
          new Translation3d(0.0, 0.0, 0.0),
          new Rotation3d(0.0, 0.0, Math.toRadians(getPositionAngle()))
      );

      lastFieldPose = tagFieldPose
          .transformBy(cameraToTag.inverse())
          .transformBy(cameraToRobot.inverse());

      lastPoseTimestamp = now;
      return lastFieldPose;
    }

    // Get Last Field Relative Robot Pose based on timeout
    if (lastFieldPose != null && (now - lastPoseTimestamp) <= Field_Pose_Timeout) {
      return lastFieldPose;
    }

    // Field Relative Robot Pose Timed out
    return null;
  }

  /**
   * Resets the current target
   */
  public void resetTarget () { currentTarget = null; }

  /**
   * Get the angle limelight motor is at
   * @return the angle the motor is at
   */
  public double getPositionAngle () { return motorController.getSetpoint(); }

  /**
   * Set the heading error of the robot
   * @param headingError the new heading error of the robot
   */
  public void setHeadingError (double headingError) { this.headingError = headingError; }
  
  /**
   * gets the current tag id
   * @return tag id
   */
  private int getTagID() { return (int) LimelightHelpers.getFiducialID(LimelightConstants.Name); }

  /**
   * Moves Neo Motor to target angle
   * @param targetAngle the target angle we want the neo motor to look at
   */
  private void moveToAngle (double targetAngle) {
    double currentAngle = encoder.getPosition() * 360.0;

    double delta = targetAngle - currentAngle;

    if (delta > 180.0) delta -= 360.0;
    if (delta < -180.0) delta += 360.0;

    double newTarget = currentAngle + delta;

    motorController.setSetpoint(newTarget / 360.0, ControlType.kPosition);
  }

  /**
   * Display the current april tag and the distance and angle it is at | 20ms
   */
  @Override
  public void periodic() {
    if (currentTarget == null) {
      motorController.setSetpoint(0, ControlType.kPosition);
    } else {
      moveToAngle(getTx());
    }

    String currentTag = 
      hasAprilTagOutpost() ? "Outpost Tag"
    : hasAprilTagShoot() ? "Shooter Tag" 
    : hasAprilTagShootLeft() ?" Shooter Left Tag" 
    : hasAprilTagShootRight() ? "Shooter Right Tag" 
    : hasAprilTagClimb() ? "Climber Tag"
    : "No Tag Found";
    
    SmartDashboard.putString("AprilTag", currentTag);
    SmartDashboard.putNumber("Heading Error", headingError);

    if (currentTarget != null) {
      SmartDashboard.putNumber("Distance", currentTarget.getZ());
      SmartDashboard.putNumber("Angle", getTx());
    }
  }
}
