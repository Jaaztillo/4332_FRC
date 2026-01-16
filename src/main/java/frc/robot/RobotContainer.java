/*
  * MASTER PROGRAMMERS WORK
  */

package frc.robot;

import frc.robot.Constants.OperatorConstants;

import frc.robot.commands.Shoot_SequenceCommand;
import frc.robot.commands.TankDrive_Command;

import frc.robot.subsystems.Roller_Subsystem;
import frc.robot.subsystems.Shooter_Subsystem;
import frc.robot.subsystems.TankDrive_Subsystem;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private TankDrive_Subsystem TankDrive_Subsystem = new TankDrive_Subsystem();
  private Shooter_Subsystem Shooter_Subsystem = new Shooter_Subsystem();
  private Roller_Subsystem Roller_Subsystem = new Roller_Subsystem();

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController controller01 = new CommandXboxController(OperatorConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    /** TANK DRIVE SUBSYSTEM DRIVE COMMAND (CHANGE NEGATIVES TO VALUES IF NEEDED) */
    TankDrive_Subsystem.setDefaultCommand(new TankDrive_Command(TankDrive_Subsystem,
                                        () -> controller01.getLeftY(), 
                                        () -> controller01.getRightX())
    );

    /** On Left Bumper Shoot The Ball */ 
    controller01.rightBumper().whileTrue(new Shoot_SequenceCommand(Shooter_Subsystem, Roller_Subsystem));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return null;
  }
}
