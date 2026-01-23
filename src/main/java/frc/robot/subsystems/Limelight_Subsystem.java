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
  // =============================
  // ID DEBOUNCE
  // =============================
  private int lastValidTagID = -1;
  private double lastSeenTime = 0.0;
  private static final double ID_TIMEOUT = 0.25; // seconds

  // =============================
  // DISTANCE FILTERING (EMA)
  // =============================
  private double filteredZ = 0.0;
  private boolean hasZInit = false;
  private static final double Z_ALPHA = 0.2; // lower = smoother

  private double left_power = 0.0;
  private double right_power = 0.0;
  private double offset_tx = 0.0;

  public Limelight_Subsystem() {}

  // =============================
  // TAG DETECTION
  // =============================
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

  // =============================
  // TANK OUTPUTS
  // =============================
  public double get_left_output() {
    return left_power;
  }

  public double get_right_output() {
    return right_power;
  }

  public double get_offsetTX_output() {
    return offset_tx;
  }

  // =============================
  // CORE LOGIC
  // =============================
  private double[] calculateOutputs() {
    if (!hasValidTag()) {
      return new double[]{0, 0, 0};
    }

    double tx = LimelightHelpers.getTX(LimelightConstants.name);
    double z = getFilteredDistanceZ();

    /* Get the angle for the robot to look at the tower and not at the tag itself
     *  angle = arctan(opposite / adjacent)
     *  angle = arctan(X_OFFSET, z)
     *  
     *  Convert angle to degrees because Math.atan2 returns radians while tx measures in degrees
     * 
     *        ● Tower Center
     *        |\
     *        | \
     *      z |  \
     *        |   \
     *        |θ   \
     *  Robot +-----● AprilTag
     *        X_OFFSET
     * 
     *  Explanation:
     *    Tower_Center to the AprilTag have a offset in the game
     *    z is the adjacent to keep the angle ratio to the distance
     * 
     *    if z was not in the equation the angle could be too big or too little
     *    remember tan(θ) = opposite / adjacent
     *      Opposite is how far sideways you must move to hit the tower corner
     *      Adjacent is how far along the robot's current sight line the tag is
     *    
     */

    double angle = Math.toDegrees(Math.atan2(LimelightConstants.X_OFFSET, z));
    
    // Apply yaw offset
    if (has_AprilTag_Left()) {
      tx -= angle;
    } else if (has_AprilTag_Right()) {
      tx += angle;
    }

    double turn = tx * LimelightConstants.kTurnP;
    double drive = (z - LimelightConstants.DESIRED_DISTANCE)
                  * LimelightConstants.kDriveP;

    double leftRaw  = drive + turn;
    double rightRaw = drive - turn;

    /*
     * EXAMPLE: 
     *  abs(-1.431) = 1.431
     *  abs(-1.449) = 1.449
     *  maxMagnitude = 1.449 
     *  
     *  Since maxMagnitude > MAX_OUTPUT (1.449 > 0.6), we scale:
     *      scale = MAX_OUTPUT / maxMagnitude
     *      0.6 / 1.449 ≈ 0.4141 (Scale)
     *  
     *  NEW VALUES
     *      leftRaw * scale  | -1.431 * 0.4141 | leftRaw  = -0.593
     *      rightRaw * scale | -1.449 * 0.4141 | rightRaw = -0.600
     */

    double maxMagnitude = Math.max(Math.abs(leftRaw), Math.abs(rightRaw));
    if (maxMagnitude > LimelightConstants.MAX_OUTPUT) {
        leftRaw  *= LimelightConstants.MAX_OUTPUT / maxMagnitude;
        rightRaw *= LimelightConstants.MAX_OUTPUT / maxMagnitude;
    }

    double left = leftRaw;
    double right = rightRaw;

    return new double[]{left, right, tx};
  }

  // =============================
  // FILTERED DISTANCE
  // =============================
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

  // =============================
  // TAG ID DEBOUNCE
  // =============================
  private int getDebouncedTagID() {
    int currentID = (int) LimelightHelpers.getFiducialID(LimelightConstants.name);

    if (currentID != -1) {
      lastValidTagID = currentID;
      lastSeenTime = Timer.getFPGATimestamp();
    }

    if (Timer.getFPGATimestamp() - lastSeenTime < ID_TIMEOUT) {
      return lastValidTagID;
    }

    return -1;
  }

  @Override
  public void periodic() {
    double[] output = calculateOutputs();

    left_power = output[0];
    right_power = output[1];
    offset_tx = output[2];

    SmartDashboard.putBoolean("Has AprilTag", hasValidTag());
    SmartDashboard.putNumber("Debounced Tag ID", getDebouncedTagID());
    SmartDashboard.putNumber("Filtered Z", filteredZ);
  }
}
