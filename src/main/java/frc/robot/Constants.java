/*
  * MASTER PROGRAMMERS WORK
  */

/* 
 * Ports used: 
 *  0, 1, 2, 3 (TANK)
 *  4 (Pigeon 2.0)
 *  5 (Intake)
 *  6 (Shooter)
 *  7, 8 (Roller)
 *  9, 10 (Climb)
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
  
    public static final int left_leader_ID = 0;
    public static final int left_follower_ID = 1;

    public static final int right_leader_ID = 2;
    public static final int right_follower_ID = 3;

    public static final int pigeon_ID = 4;
  }

  public static class IntakeConstants
  {
    public static final int intake_ID = 5;
  }

  public static class ShooterConstants
  {
    public static final int shooter_ID = 6;
  }

  public static class RollerConstants
  {
    public static final int roller_left_ID = 7;
    public static final int roller_right_ID = 8;
  }

  public static class ClimbConstants
  {
    public static final double kInchesPerRevolution = 1; // CHANGE TO CONVERTION FACTOR

    public static final int climb_left_ID = 9;
    public static final int climb_right_ID = 10;
  }
}
