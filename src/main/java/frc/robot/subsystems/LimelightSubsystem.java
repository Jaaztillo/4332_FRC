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

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
// Geometry
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;

// Timer
import edu.wpi.first.wpilibj.Timer;

// Dashboard
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.DoublePublisher;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

// Servo
import edu.wpi.first.wpilibj.Servo;

import frc.robot.Constants.DashboardIds;
import frc.robot.Constants.LimelightConstants;

public class LimelightSubsystem extends SubsystemBase {
  // Servo Motor
  private final Servo servo = new Servo(LimelightConstants.Limelight_Motor_Id);

  // Last Target Pose
  private Pose3d lastFieldPose = null;
  private double lastPoseTimestamp = 0.0;

  // Current tag relative to field
  private Pose3d tagFieldPose = null;

  // Current Target
  private Pose3d currentTarget = null;

  // Servo Roaming
  private double roamAngle = 0.0;
  private boolean increasing = true;

  // Dashboard Publishers
  DoublePublisher aprilTagPublisher;
  DoublePublisher anglePublisher;
  DoublePublisher headingPublisher;
  DoublePublisher distancePublisher;
  
  /**
   * Limelight constructor
   * Sets up the dashboard values
   */
  public LimelightSubsystem() {
    // Dashboard configuration
    aprilTagPublisher = NetworkTableInstance.getDefault()
      .getTable("SmartDashboard")
      .getDoubleTopic(DashboardIds.AprilTag)
      .publish();

    anglePublisher = NetworkTableInstance.getDefault()
      .getTable("SmartDashboard")
      .getDoubleTopic(DashboardIds.Angle)
      .publish();

    headingPublisher = NetworkTableInstance.getDefault()
      .getTable("SmartDashboard")
      .getDoubleTopic(DashboardIds.Heading_Error)
      .publish();

    distancePublisher = NetworkTableInstance.getDefault()
      .getTable("SmartDashboard")
      .getDoubleTopic(DashboardIds.Distance)
      .publish();

    aprilTagPublisher.set(-1);
    anglePublisher.set(0.0);
    headingPublisher.set(0.0);
    distancePublisher.set(0.0);
  }
  
  /**
   * checks for a valid tag
   * @return isValid tag
   */
  public boolean hasValidTag() {
    return getTagID() != -1;
  }

  /**
   * get the current target the driver is aiming to
   * @return the current target
   */
  public Pose3d getCurrentTarget () {
    return currentTarget;
  }

  /**
   * Get the angle we need to turn to
   * @return the angle to the current target in the x axis
   */
  public double turnAngle (Pose3d target) {
    if (lastFieldPose == null || target == null) {
      return 0.0;
    }

    // Robot position (field)
    double rx = lastFieldPose.getX();
    double ry = lastFieldPose.getY();

    // Target position (field)
    double dx = target.getX() - rx;
    double dy = target.getY() - ry;

    // Robot heading (field-relative)
    double targetBearing = Math.toDegrees(
      Math.atan2(dy, dx));

    // Angle robot must turn
    double error = targetBearing - targetBearing;

    // Normalize to [-180, 180]
    while (error > 180) error -= 360;
    while (error < -180) error += 360;

    return error;
  }

  /**
   * Get the distance from the robot to the target
   * @param target the target we want the distance to
   * @return the distance from the robot to target
   */
  public double getDistance (Pose3d target) {
    if (lastFieldPose == null || target == null) {
      return 0.0;
    }

    // Robot position (field)
    double rx = lastFieldPose.getX();
    double ry = lastFieldPose.getY();

    // Target position (field)
    double dx = target.getX() - rx;
    double dy = target.getY() - ry;

    double distance = Math.hypot(dx, dy);

    return distance;
  }

  /**
   * Gets the robots field-relative pose
   * @return field-relative pose of robot
   */
  public Pose3d getFieldRelativePose() {
    if (!hasValidTag()) tagFieldPose = null;

    double now = Timer.getFPGATimestamp();
    
    // If there is a valid tag compute a new pose
    if (hasValidTag()) {
      tagFieldPose = LimelightConstants.tagFieldMap.get(getTagID());
      if (tagFieldPose == null) return null;

      Pose3d tagCameraPose =
          LimelightHelpers.getTargetPose3d_RobotSpace(LimelightConstants.Name);

      Transform3d tagToCamera = new Transform3d(
          tagCameraPose.getTranslation(),
          tagCameraPose.getRotation()
      );

      Transform3d cameraToRobot = new Transform3d(
          new Translation3d(5.75, 11.25, 11.00),
          new Rotation3d(0.0, 0.0, Math.toRadians(servo.getAngle() - LimelightConstants.Servo_To_Camera_Offset)) // Do the actual offset after finding out
      );

      lastFieldPose = tagFieldPose
          .transformBy(tagToCamera.inverse())
          .transformBy(cameraToRobot.inverse());

      lastPoseTimestamp = now;
      return lastFieldPose;
    }

    // Get Last Field Relative Robot Pose based on timeout
    if (lastFieldPose != null && (now - lastPoseTimestamp) <= LimelightConstants.Field_Pose_Timeout) {
      return lastFieldPose;
    }

    // Field Relative Robot Pose Timed out
    return null;
  }
  
  /**
   * Get the angle from the robot to the hub
   * @return the target angle from the robot to the hub
   */
  public double lookAtTarget () {
    if (lastFieldPose == null) return 0.0;

    currentTarget = getGridTarget(getAlliance()); 

    if (currentTarget == null) return 0.0;
    
    return turnAngle(currentTarget);
  }

  /**
   * Get the angle from the robot to the tower
   * @param side the side the robot will climb
   * @return the target angle from the robot to the tower
   */
  public double lookAtClimber (String side) {
    if (lastFieldPose == null) return 0.0;

    currentTarget = getAlliance().equals("Red") ?
      side.equals("Left") ? LimelightConstants.Red_Left_Pole : LimelightConstants.Red_Right_Pole
    : side.equals("Left") ? LimelightConstants.Blue_Left_Pole : LimelightConstants.Blue_Right_Pole;

    return turnAngle(currentTarget);
  }

  /**
   * Stop Traching a target and keep roaming
   */
  public void stopTargeting () { 
    currentTarget = null; 
  }
  
  /**
   * Camera roams looking for a tag all the time
   */
  private void roamSearch() {
    if (hasValidTag()) {
      double tx = LimelightHelpers.getTX(LimelightConstants.Name);

      double servoTarget = LimelightConstants.Servo_To_Camera_Offset + tx;
      servoTarget = MathUtil.clamp(servoTarget, LimelightConstants.Minimum_Servo, LimelightConstants.Maximum_Servo);

      servo.setAngle(servoTarget);
      return;
    }

    if (increasing) {
        roamAngle += LimelightConstants.Roam_Speed;
        if (roamAngle >= LimelightConstants.Maximum_Servo) {
            roamAngle = LimelightConstants.Maximum_Servo;
            increasing = false;
        }
    } else {
        roamAngle -= LimelightConstants.Roam_Speed;
        if (roamAngle <= LimelightConstants.Minimum_Servo) {
            roamAngle = LimelightConstants.Minimum_Servo;
            increasing = true;
        }
    }

    servo.setAngle(roamAngle);
  }

  private Pose3d getGridTarget (String alliance) {
    if (lastFieldPose == null) return null;

    return isInHubGrid(alliance) != null ? isInHubGrid(alliance) 
    : isInOutpostGrid(alliance) != null ? isInOutpostGrid(alliance)
    : isInAllianceAreaGrid(alliance);
  }

  private Pose3d isInHubGrid (String alliance) {
    if (lastFieldPose == null) return null;

    if (alliance.equals("Red")) {
      // Target Red Hub
      if (!robotInGrid(469, 650, 0, 316.64)) return null;

      return LimelightConstants.Red_Hub;
    
    } else {
      // Target Blue Hub
      if (!robotInGrid(0, 181, 0, 316.64)) return null;

      return LimelightConstants.Blue_Hub;
    }
  }

  private Pose3d isInOutpostGrid (String alliance) {
    if (lastFieldPose == null) return null;

    if (alliance.equals("Red")) {
      // Target Red Outpost
      if (!robotInGrid(325.06, 469, 158.32, 316.64)) return null;

      return LimelightConstants.Red_Outpost;
    } else {
      // Target Blue Outpost
      if (!robotInGrid(181, 325.06, 0, 158.32)) return null;

      return LimelightConstants.Blue_Outpost;
    }
  }

  private Pose3d isInAllianceAreaGrid (String alliance) {
    if (lastFieldPose == null) return null;

    // Return the respectives alliance area no matter what
    if (alliance.equals("Red")) {
      return LimelightConstants.Red_Alliance_Area;
    } else {
      return LimelightConstants.Blue_Alliance_Area;
    }
  }

  /**
   * Check if the robot is in the grid provided
   * 
   * Grid starts from the top right relative to the field
   * @param x1 right
   * @param x2 left
   * @param y1 top
   * @param y2 bottom
   * @return true if the robot is in the field and false otherwise
   */
  private boolean robotInGrid (double x1, double x2, double y1, double y2) {
    double robot_x = lastFieldPose.getX();
    double robot_y = lastFieldPose.getY();


    // Check if robot is in the grid provided
    if ((robot_x >= x1 && robot_x <= x2) && (robot_y >= y2 && robot_y <= y1)) {
      return true;
    }

    return false;
  } 

  /**
   * get the alliance color we are on
   * @return the alliance we're on
   */
  private String getAlliance () {
    return DriverStation.getAlliance()
      .map(a -> a == Alliance.Red ? "Red" : "Blue")
      .orElse("Red");
  }

  /**
   * gets the current tag id
   * @return tag id
   */
  private int getTagID() { 
    return (int) LimelightHelpers.getFiducialID(LimelightConstants.Name); 
  }
  
  /**
   * Display the current april tag and the distance and angle it is at | 20ms
   */
  @Override
  public void periodic() {
    // Update robot pose
    Pose3d robotPose = getFieldRelativePose();

    // Servo scans and tracks the current target
    roamSearch();

    // Current detected tag
    int tagId = getTagID();
    aprilTagPublisher.set(tagId);

    if (lastFieldPose != null && currentTarget != null) {
      distancePublisher.set(currentTarget.getZ());
      anglePublisher.set(turnAngle(currentTarget));
    } else {
      distancePublisher.set(0.0);
      anglePublisher.set(0.0);
    }

    // Display robot pose on dashboard
    if (robotPose != null) {
        // 2D Robot Pose Components
        double x = robotPose.getX();
        double y = robotPose.getY();
        double yaw = Math.toDegrees(robotPose.getRotation().getZ());

        // Update 2D Robot Pose on Dashboard
        DashboardIds.Field.setRobotPose(
          new Pose2d(x, y, new Rotation2d(yaw))
        );

        // Display 2D Robot Pose for debugging
        System.out.printf("X: %.2f | Y: %.2f | Yaw | %.2f", 
          x, y, yaw);
    }
  }
}