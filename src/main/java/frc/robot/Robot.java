package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

// Camera Server
import edu.wpi.first.cameraserver.CameraServer;

// ShuffleBoard
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;

import frc.robot.commands.ClimbCommands.ClimbDown;

import frc.robot.Constants.AutonomousNames;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  private Command autonomous;

  private final RobotContainer robotContainer;

  private final SendableChooser<String> autonomousChooser = new SendableChooser<>();

  private final Field2d field = new Field2d();

  private String autoSelected;

  /**
   * Robot constructor runs when the robot first starts up and should be for any initialization code
   */
  public Robot() {
    // Initiate Robot Container
    robotContainer = new RobotContainer();

    // Display Autonomous Options on Dashboard
    autonomousChooser.setDefaultOption("Depot Climb Blue", AutonomousNames.Depot_Climb_Blue);
    autonomousChooser.addOption("Depot Climb Red", AutonomousNames.Depot_Climb_Red);

    // Start Camera Server
    CameraServer.startAutomaticCapture("Camera", 0);
  }

  @Override
  public void robotInit () {
    SmartDashboard.putData("Autonomous Choices", autonomousChooser);
    SmartDashboard.putData("Game Field", field);
  }

  /**
   * Run all the commands that are running periodically, without this nothing else works
   */
  @Override
  public void robotPeriodic() { CommandScheduler.getInstance().run(); }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    autoSelected = autonomousChooser.getSelected();

    System.out.println("Auto selected: " + autoSelected);

    autonomous = robotContainer.getAutonomousCommand(autoSelected);
    
    // schedule the autonomous command (example)
    if (autonomous != null) {
      CommandScheduler.getInstance().schedule(autonomous);
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}
  
  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (autonomous != null) {
      autonomous.cancel();
    }

    CommandScheduler.getInstance().schedule(new ClimbDown(robotContainer.getClimber()));;
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}
}
