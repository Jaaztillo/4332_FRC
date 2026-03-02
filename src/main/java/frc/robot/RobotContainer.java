/*
  * MASTER PROGRAMMERS WORK
  */

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

// Constants
import frc.robot.Constants.*;

// Subsystems
import frc.robot.subsystems.DriveSubsystem;

import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.RollerSubsystem;

import frc.robot.subsystems.IntakeSubsystem;

import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.ExtensionSubsystem;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.PigeonSubsystem;

// Commands
import frc.robot.commands.DifferentialDriveCommands.TankDrive;
import frc.robot.commands.DifferentialDriveCommands.AlignToTower;
import frc.robot.commands.DifferentialDriveCommands.AlignToTarget;

import frc.robot.commands.ShooterCommands.AlignShooter;
import frc.robot.commands.ShooterCommands.ShootFuel;

import frc.robot.commands.IntakeCommands.ExtendIntake;
import frc.robot.commands.IntakeCommands.IntakeFuel;

import frc.robot.commands.ClimbCommands.ClimbFoward;
import frc.robot.commands.ClimbCommands.ClimbReverse;
import frc.robot.commands.ClimbCommands.ClimbLevel_3;

import frc.robot.commands.AutonomousCommands.DepotClimbBlue;
import frc.robot.commands.AutonomousCommands.DepotClimbRed;

/**
 * Robot Container containing subsystems and commands
 */
public class RobotContainer {
  // Subsystems
  private LimelightSubsystem limelight = new LimelightSubsystem();
  private ExtensionSubsystem extension = new ExtensionSubsystem();
  private ClimberSubsystem climber = new ClimberSubsystem();
  private ShooterSubsystem shooter = new ShooterSubsystem();
  private DriveSubsystem tankDrive = new DriveSubsystem();
  private PigeonSubsystem pigeon = new PigeonSubsystem();
  private RollerSubsystem roller = new RollerSubsystem();
  private IntakeSubsystem intake = new IntakeSubsystem();

  // Xbox controller
  private final CommandXboxController controller01 = new CommandXboxController(OperatorConstants.kDriverControllerPort);

  /** 
   * The container for the robot. Contains subsystems, OI devices, and commands. 
  */
  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings () {
    /*
     * == KEYBINDS ==
     * LEFT JOYSTICK  : DRIVE
     * RIGHT JOYSTICK : TURN
     * 
     * LEFT TRIGGER   : INTAKE
     * RIGHT TRIGGER  : SHOOT
     * 
     * LEFT BUMPER    : EXTEND AND RETRACT INTAKE
     * RIGHT BUMPER   : SHOOTING DRIVE TRAIN ALIGN && ANGLE AND RPM ALIGNING
     * 
     * BUTTON Y       : CLIMB TO LEVEL 3
     * BUTTON X       : ALIGN TO LEFT RUNG
     * BUTTON B       : ALIGN TO RIGHT RUNG
    */

    // ==== TANK DRIVE BINDING ==== \\

    // Tank_Drive Default Command
    tankDrive.setDefaultCommand(
        new TankDrive(tankDrive,
          () -> controller01.getLeftY(), () -> controller01.getRightX())
    );

    // ==== INTAKE BINDING ==== \\

    // Intake Command
    controller01.leftTrigger().whileTrue(new IntakeFuel(intake));

    // Extend or Retract Command 
    controller01.leftBumper().toggleOnTrue(new ExtendIntake(extension));

    // ==== SHOOTER BINDING ==== \\

    // Align Drive and Angle + RPM to Hub
    controller01.rightBumper().whileTrue(new ParallelCommandGroup(
      new AlignToTarget(tankDrive, limelight, controller01),
      new AlignShooter(shooter, limelight)
    ));
    
    // Shooter Parallel Sequence (Run Shooter Motor -> and -> (1 second -> then -> Run Roller Motor))
    controller01.rightTrigger().whileTrue(new ShootFuel(shooter, roller));

    // ==== CLIMBER BINDING ==== \\

    // CLIMB TESTING (Get target positions for the constants)
    controller01.povUp().whileTrue(new ClimbFoward(climber));
    controller01.povDown().whileTrue(new ClimbReverse(climber));

    // Climb Up (Levels : 3)
    controller01.y().onTrue(new ClimbLevel_3(climber));

    // Turn Robot to Left Pole while driving
    controller01.x().whileTrue(new AlignToTower(tankDrive, limelight, controller01, "Left"));

    // Turn Robot to Right Pole while driving
    controller01.b().whileTrue(new AlignToTower(tankDrive, limelight, controller01, "Right"));
  }

  /**
   * Gets the climber subsystem to allow it to do things in teleop init/disabled and more methods
   * @return climber subsystem
   */
  public ClimberSubsystem getClimber () {
    return climber;
  }

  public Command getAutonomousCommand (String autoName) {
    switch (autoName) {
      case AutonomousNames.Depot_Climb_Blue:
        return new DepotClimbBlue(tankDrive, limelight, shooter, roller, climber);
      case AutonomousNames.Depot_Climb_Red:
        return new DepotClimbRed(tankDrive, limelight, shooter, roller, climber);
      default:
        return null;
    }
  }
}
