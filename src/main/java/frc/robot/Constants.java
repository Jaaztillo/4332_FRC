/*
  * MASTER PROGRAMMERS WORK
  */

/* 
 * Ports used: 
 *  1, 2, 3, 4 (TANK)
 *  5 (Pigeon 2.0)
 *  6 (Intake)
 *  7 (Shooter)
 *  8, 9 (Roller)
 *  10, 11 (Climb)
 */

package frc.robot;

public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }

  /*
   * == TANK DRIVE CONSTANTS == 
   * 
   *    LEFT LEADER ID    = 0
   *    LEFT FOLLOWER ID  = 1
   * 
   *    RIGHT LEADER ID   = 2
   *    RIGHT FOLLOWER ID = 3
   */

  public static class TankDriveConstants 
  {
    public static final String kDefault_Auto = "Default";
    public static final String kCustom_Auto = "My Auto";
    public String m_autoSelected;
  
    public static final int left_leader_ID = 1;
    public static final int left_follower_ID = 2;

    public static final int right_leader_ID = 3;
    public static final int right_follower_ID = 4;

    public static final int pigeon_ID = 5;
  }

  public static class IntakeConstants
  {
    public static final int intake_ID = 6;
  }

  public static class ShooterConstants
  {
    public static final int shooter_ID = 7;
  }

  public static class RollerConstants
  {
    public static final int roller_left_ID = 8;
    public static final int roller_right_ID = 9;
  }

  public static class ClimbConstants
  {
    public static final double kInchesPerRevolution = 1; // CHANGE TO CONVERTION FACTOR

    public static final int climb_left_ID = 10;
    public static final int climb_right_ID = 11;
  }

  public static class LimelightConstants
  {
    public static final double kTurnP = 0.03;             // turning strength
    public static final double kDriveP = 0.8;             // forward/backward strength
    public static final double MAX_OUTPUT = 0.6;          // max motor output
    public static final double DESIRED_DISTANCE = 2.0;    // meters from target
    public static final double SIDE_YAW_OFFSET = 30.0;    // degrees sideways

    public static final double TX_TOLERANCE = 2.0;        // TOLERANCE FOR X AXIS
    public static final double Z_TOLERANCE = 0.08;         // TOLAREANCE FOR DISTANCE

    public static final String name = "limelight";        // name of limelight


    public static final int[] APRILTAG_RIGHT  = {8, 24};  // APRIL TAGS FOR ROBOT TO TURN RIGHT
    public static final int[] APRILTAG_LEFT   = {11, 27}; // APRIL TAGS FOR ROBOT TO TURN LEFT
  }
}
