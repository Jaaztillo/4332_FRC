package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

// Differential Drive
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

// Differential Drive Pose
import edu.wpi.first.math.estimator.DifferentialDrivePoseEstimator;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
// Encoders (REV Through Bore)
import edu.wpi.first.wpilibj.DutyCycleEncoder;

// TalonSRX + Pigeon
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import frc.robot.Constants.DashboardIds;
import frc.robot.Constants.GamePoseConstants;
import frc.robot.Constants.PigeonConstants;
import frc.robot.Constants.TankDriveConstants;

public class DriveSubsystem extends SubsystemBase 
{
  // Leaders
  private final WPI_TalonSRX leftLeader = new WPI_TalonSRX(TankDriveConstants.Left_Leader_ID);
  private final WPI_TalonSRX rightLeader = new WPI_TalonSRX(TankDriveConstants.Right_Leader_ID);

  // Followers
  private final WPI_TalonSRX leftFollower = new WPI_TalonSRX(TankDriveConstants.Left_Follower_ID);
  private final WPI_TalonSRX rightFollower = new WPI_TalonSRX(TankDriveConstants.Right_Follower_ID);

  // Differential Drive Train
  private final DifferentialDrive drive = new DifferentialDrive(leftLeader, rightLeader);

  // Pigeon
  private final Pigeon2 pigeon = new Pigeon2(PigeonConstants.Pigeon_ID);

  // Tick Encoders
  private final DutyCycleEncoder leftEncoder = new DutyCycleEncoder(0);
  private final DutyCycleEncoder rightEncoder = new DutyCycleEncoder(1);

  private final DifferentialDriveKinematics kinematics =
      new DifferentialDriveKinematics(TankDriveConstants.Track_Width);

  // Last tracked ticks
  private double lastLeft = 0;
  private double lastRight = 0;

  // Distance traveled
  private double leftDistance = 0;
  private double rightDistance = 0;


  // Pose Estimator
  DifferentialDrivePoseEstimator poseEstimator;
  
  /**
   * Configure the tank drive motors and the robot's odometry and pose
   * @param pigeon the subsystem responsible for getting the robots heading
   */
  public DriveSubsystem () {
    // Configure Followers
    leftFollower.follow(leftLeader);
    rightFollower.follow(rightLeader);

    // Left Basic Talon SRX Config
    leftLeader.configContinuousCurrentLimit(40);
    leftLeader.enableCurrentLimit(true);
    leftLeader.setNeutralMode(NeutralMode.Brake);
    
    // Right Basic Talon SRX Config
    rightLeader.configContinuousCurrentLimit(40);
    rightLeader.enableCurrentLimit(true);
    rightLeader.setNeutralMode(NeutralMode.Brake);

    // Inverse the Follows
    rightLeader.setInverted(true);
    rightFollower.setInverted(true);

    // Zero gyro
    pigeon.setYaw(0);

    // Pose Estimator
    poseEstimator = new DifferentialDrivePoseEstimator(
        kinematics,
        Rotation2d.fromDegrees(getYaw()),
        getLeftDistanceMeters(),
        getRightDistanceMeters(),
        new Pose2d(),

        VecBuilder.fill(0.02, 0.02, 0.01),
        VecBuilder.fill(0.5, 0.5, 0.5)
    );

    lastLeft = leftEncoder.get();
    lastRight = rightEncoder.get();
  }

  // == DRIVE LOGIC == \\

  /**
   * Drive the drive train in arcade drive mode
   * @param Y Left  Joystick (-Y)
   * @param X Right Joystick (X)
   */
  public void arcadeDrive(double Y, double X) {
    drive.arcadeDrive(-Y, X);
  }

  
  /**
   * Drive the drive train in arcade drive mode
   * @param left left side of differential drive
   * @param right right side of differential drive
   */
  public void tankDrive(double left, double right) {
    drive.tankDrive(left, right);
  }

  /**
   * Stop the drive train
   */
  public void stop () {
    leftLeader.stopMotor(); 
    rightLeader.stopMotor(); 
  }

  // == ENCODER  LOGIC == \\

  /**
   * @return get traveled distance of the left side
   */
  private double getLeftDistanceMeters() {
    double current = leftEncoder.get();
    double delta = current - lastLeft;

    // Handle wraparound
    if (delta > 0.5) delta -= 1;
    if (delta < -0.5) delta += 1;

    leftDistance += delta * getWheelCircumference();
    lastLeft = current;

    return -leftDistance;
  }

  /**
   * @return get traveled distance of the right side
   */
  private double getRightDistanceMeters() {
    double current = rightEncoder.get();
    double delta = current - lastRight;

    if (delta > 0.5) delta -= 1;
    if (delta < -0.5) delta += 1;

    rightDistance += delta * getWheelCircumference();
    lastRight = current;

    return rightDistance;
  }

  /**
   * @return the circumfrence of wheel
   */
  private double getWheelCircumference() { 
    return Math.PI * TankDriveConstants.Wheel_Diameter / TankDriveConstants.Gear_Ratio; 
  }

  // == PIGEON LOGIC == \\

  /**
   * @return the yaw of the robot
   */
  public double getYaw() { 
    return pigeon.getYaw().getValueAsDouble(); 
  }

  // == VISION LOGIC == \\

  public void addVisionMeasurement (Pose2d pose, double timestamp) { 
    poseEstimator.addVisionMeasurement(pose, timestamp); 
  }

  // == POSE LOGIC == \\

  /**
   * @return the robot pose
   */
  public Pose2d getPose() { 
    return poseEstimator.getEstimatedPosition();
  }

  /**
   * Reset Pose
   * @param pose current robot pose
   */
  public void resetPose(Pose2d pose) {
    poseEstimator.resetPosition(
        Rotation2d.fromDegrees(getYaw()),
        getLeftDistanceMeters(),
        getRightDistanceMeters(),
        pose
    );
  }

  // == TARGETING LOGIC == \\

  /**
   * Get the heading error from the robot to the current target based on the grid the robot is on
   * @return the target angle from the robot to the hub
   */
  public double angleToTarget (Pose3d target) {
    Pose2d robotPose = getPose();

    if (robotPose == null) return 0.0;

    target = getGridTarget(); 

    if (target == null) return 0.0;
    
    return turnAngle(target);
  }

  /**
   * Get the angle we need to turn to
   * @return the angle to the current target in the x axis
   */
  public double turnAngle (Pose3d target) {
    Pose2d robotPose = getPose();

    if (robotPose == null || target == null) {
      return 0.0;
    }

    // Robot position (field)
    double rx = robotPose.getX();
    double ry = robotPose.getY();

    // Target position (field)
    double dx = target.getX() - rx;
    double dy = target.getY() - ry;

    // Robot heading (field-relative)
    double targetBearing = Math.toDegrees(
      Math.atan2(dy, dx));

    // Angle robot must turn
    double error = targetBearing - targetBearing;

    return error;
  }

   /**
   * Get the distance from the robot to the target
   * @param target the target we want the distance to
   * @return the distance from the robot to target
   */
  public double getDistance (Pose3d target) {
    Pose2d robotPose = getPose();

    if (robotPose == null || target == null) {
      return 0.0;
    }

    // Robot position (field)
    double rx = robotPose.getX();
    double ry = robotPose.getY();

    // Target position (field)
    double dx = target.getX() - rx;
    double dy = target.getY() - ry;

    double distance = Math.hypot(dx, dy);

    return distance;
  }

  // == GRID LOGIC == \\

  public Pose3d getGridTarget () {
    if (getPose() == null) return null;

    String alliance = getAlliance();

    return isInHubGrid(alliance) != null ? isInHubGrid(alliance) 
    : isInOutpostGrid(alliance) != null ? isInOutpostGrid(alliance)
    : isInAllianceAreaGrid(alliance);
  }

  private Pose3d isInHubGrid (String alliance) {
    if (getPose() == null) return null;

    if (alliance.equals("Red")) {
      // Target Red Hub
      if (!robotInGrid(469, 650, 0, 316.64)) return null;

      return GamePoseConstants.Red_Hub;
    
    } else {
      // Target Blue Hub
      if (!robotInGrid(0, 181, 0, 316.64)) return null;

      return GamePoseConstants.Blue_Hub;
    }
  }

  private Pose3d isInOutpostGrid (String alliance) {
    if (getPose() == null) return null;

    if (alliance.equals("Red")) {
      // Target Red Outpost
      if (!robotInGrid(325.06, 469, 158.32, 316.64)) return null;

      return GamePoseConstants.Red_Outpost;
    } else {
      // Target Blue Outpost
      if (!robotInGrid(181, 325.06, 0, 158.32)) return null;

      return GamePoseConstants.Blue_Outpost;
    }
  }

  private Pose3d isInAllianceAreaGrid (String alliance) {
    if (getPose() == null) return null;

    // Return the respectives alliance area no matter what
    if (alliance.equals("Red")) {
      return GamePoseConstants.Red_Alliance_Area;
    } else {
      return GamePoseConstants.Blue_Alliance_Area;
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
    double robot_x = getPose().getX();
    double robot_y = getPose().getY();


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
   * Update Pose and display the pose on the dashboard
   */
  @Override
  public void periodic() {
    poseEstimator.update(
        Rotation2d.fromDegrees(getYaw()),
        getLeftDistanceMeters(),
        getRightDistanceMeters()
    );

    // Update robot pose
    Pose2d robotPose = getPose();

    // Display robot pose on dashboard
    if (robotPose != null) {
      var poseEntry = NetworkTableInstance.getDefault()
        .getTable("SmartDashboard")
        .getEntry(DashboardIds.Game_Field);

      double x = robotPose.getX();
      double y = robotPose.getY();
      double rot = robotPose.getRotation().getDegrees();

      // Publish as array [x, y, yaw]
      poseEntry.setDoubleArray(new double[]{x, y, rot});
    }
  }
}