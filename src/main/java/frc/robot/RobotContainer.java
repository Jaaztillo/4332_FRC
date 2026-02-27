/*
  * MASTER PROGRAMMERS WORK
  */

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

// Constants
import frc.robot.Constants.*;

// Subsystems
import frc.robot.subsystems.TankDriveSubsystem;
import frc.robot.commands.DifferentialDriveCommands.AlignClimberCommand;
import frc.robot.commands.DifferentialDriveCommands.AlignDriveCommand;

import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.RollerSubsystem;

import frc.robot.subsystems.IntakeSubsystem;

import frc.robot.subsystems.ClimbSubsystem;
import frc.robot.subsystems.ExtensionSubsystem;
import frc.robot.subsystems.LimelightSubsystem;

// Commands
import frc.robot.commands.DifferentialDriveCommands.TankDriveCommand;
import frc.robot.commands.ShooterCommands.AlignShooterCommand;
import frc.robot.commands.ShooterCommands.ShooterParallelCommand;
import frc.robot.commands.IntakeCommands.ExtendCommand;
import frc.robot.commands.IntakeCommands.IntakeCommand;

import frc.robot.commands.ClimbCommands.ClimbFoward;
import frc.robot.commands.ClimbCommands.ClimbReverse;
import frc.robot.commands.ClimbCommands.SequentialClimb;

/**
 * Robot Container containing subsystems and commands
 */
public class RobotContainer {
  // Subsystems
  private LimelightSubsystem limelight = new LimelightSubsystem();
  private TankDriveSubsystem tankDrive = new TankDriveSubsystem();
  private ExtensionSubsystem extension = new ExtensionSubsystem();
  private ShooterSubsystem shooter = new ShooterSubsystem();
  private RollerSubsystem roller = new RollerSubsystem();
  private IntakeSubsystem intake = new IntakeSubsystem();
  private ClimbSubsystem climber = new ClimbSubsystem();

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
        new TankDriveCommand(tankDrive,
          () -> controller01.getLeftY(), () -> controller01.getRightX())
    );

    // ==== INTAKE BINDING ==== \\

    // Intake Command
    controller01.leftTrigger().whileTrue(new IntakeCommand(intake));

    // Extend or Retract Command 
    controller01.leftBumper().toggleOnTrue(new ExtendCommand(extension, climber));

    // ==== SHOOTER BINDING ==== \\

    // Align Turret to Hub while shooting if pressing right_bumper()
    controller01.rightBumper().whileTrue(new AlignDriveCommand(tankDrive, limelight, controller01));

    // Align the shooter only and only if driver is aligning and shooting
    controller01.rightTrigger().and(controller01.rightBumper()).whileTrue(new AlignShooterCommand(shooter, limelight));
    
    // Shooter Parallel Sequence (Run Shooter Motor -> and -> (1 second -> then -> Run Roller Motor))
    controller01.rightTrigger().whileTrue(new ShooterParallelCommand(shooter, roller));

    // ==== CLIMBER BINDING ==== \\

    // CLIMB TESTING (Get target positions for the constants)
    controller01.povUp().whileTrue(new ClimbFoward(climber));
    controller01.povDown().whileTrue(new ClimbReverse(climber));

    // Climb Up (Levels : 3)
    controller01.y().onTrue(new SequentialClimb(climber));

    // Turn Robot to Left Pole while driving
    controller01.x().whileTrue(new AlignClimberCommand(tankDrive, limelight, controller01, "left"));

    // Turn Robot to Right Pole while driving
    controller01.b().whileTrue(new AlignClimberCommand(tankDrive, limelight, controller01, "right"));
  }

  /**
   * Gets the climber subsystem to allow it to do things in teleop init/disabled and more methods
   * @return climber subsystem
   */
  public ClimbSubsystem getClimber () {
    return climber;
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand () {
    // An example command will be run in autonomous
    return null;
  }
}
