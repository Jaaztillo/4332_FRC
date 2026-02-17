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

  public static class TankDriveConstants 
  {
    public static final String kDefault_Auto = "Default";
    public static final String kCustom_Auto = "My Auto";
    public String m_autoSelected;

    public static final int state = 0;
  
    public static final int left_leader_ID    = 1;
    public static final int left_follower_ID  = 2;

    public static final int right_leader_ID   = 3;
    public static final int right_follower_ID = 4;
  }

  public static class PigeonConstants
  {
    public static final int Pigeon_ID = 5;
  }

  public static class IntakeConstants
  {
    public static final int intake_ID = 6;
  }

  public static class ShooterConstants
  {
    public static final int shooter_ID = 7;

    public static final double kP = 0.0001;
    public static final double kI = 0;
    public static final double kD = 0;
    public static final double kFF = 0.0002;
  }

  public static class RollerConstants
  {
    public static final int roller_left_ID  = 8;
    public static final int roller_right_ID = 9;
  }

  public static class ClimbConstants
  {
    public static final double kInchesPerRevolution = 1;  // CHANGE TO CONVERTION FACTOR
    public static final double kP = 0.8;                  // 0.5 -> 1
    public static final double kI = 0;                    // 0
    public static final double kD = 0.05;                 // 0.05 -> 0.1

    public static final double kPositionFactor = 0.2;     // If 10 inches = 50 rotations, factor is 0.2

    public static final double First_Rung_Distance  = 10;
    public static final double Second_Rung_Distance = 5;
    public static final double Third_Rung_Distance  = 5;

    public static final double Robot_Back_Distance  = 5;

    public static final int climb_left_ID   = 10;
    public static final int climb_right_ID  = 11;
  }

  public static class LimelightConstants
  {
    public static final double ID_TIMEOUT   = 0.25;       // ID TIMEOUT AFTER NOT BEING READ

    public static final String name = "limelight";        // Name of limelight

    public static final int[] APRILTAG_RIGHT  = {8, 24};  // APRIL TAGS FOR ROBOT TO TURN RIGHT
    public static final int[] APRILTAG_LEFT   = {11, 27}; // APRIL TAGS FOR ROBOT TO TURN LEFT
    public static final int[] APRILTAG_CLIMB  = {10, 26}; // APRIL TAGS FOR ROBOT TO ALIGN TO CLIMBER
  }

  public static class Shoot_Align_Constants
  {
    public static final double KTURNP           = 0.03;   // turning strength
    public static final double KYAWP            = 0.05;   // turning strength
    public static final double KDRIVEP          = 0.8;    // forward/backward strength
    public static final double MAX_OUTPUT       = 0.6;    // max motor output
    public static final double DESIRED_DISTANCE = 2.0;    // meters from target

    public static final double X_OFFSET  = 0.23114;   // OFFSET FROM APRIL TAG TO CENTER OF HUB

    public static final double TX_TOLERANCE  = 1.0;       // TOLERANCE FOR X AXIS

    public static final double Z_TOLERANCE   = 0.01;      // TOLAREANCE FOR DISTANCE
  }

  public static class Climb_Align_Constants
  {
    public static final double KTURNP     = 0.03;         // turning strength
    public static final double KYAWP      = 0.05;         // turning strength
    public static final double KDRIVEP    = 0.8;          // forward/backward strength
    public static final double MAX_OUTPUT = 0.6;          // max motor output

    public static final double DESIRED_DISTANCE_01 = 0.5; // meters from target
    public static final double DESIRED_DISTANCE_02 = 2.0; // meters from target

    public static final double X_OFFSET     = 0.682874;   // OFFSET FROM CENTER HUB APRIL TAG TO TOWER CENTER
    
    public static final double TX_TOLERANCE = 1.0;        // TOLERANCE FOR X AXIS
    public static final double Z_TOLERANCE  = 0.01;       // TOLAREANCE FOR DISTANCE

    public static final double FINAL_YAW    = 0.0;        // Where the robot will look in the end
  }
}
