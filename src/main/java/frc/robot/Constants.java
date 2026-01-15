package frc.robot;

public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }

  /*
   * == TANK DRIVE CONSTANTS == 
   * 
   *    LEFT LEADER ID = 1
   *    LEFT FOLLOWER ID = 2
   * 
   *    RIGHT LEADER ID = 3
   *    RIGHT FOLLOWER ID = 4
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

  public static class IntakeConstants
  {
    public static final int intake_ID = 5;
  }
}
