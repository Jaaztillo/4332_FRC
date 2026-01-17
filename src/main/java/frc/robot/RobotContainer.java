/*
  * MASTER PROGRAMMERS WORK
  */

package frc.robot;

import frc.robot.Constants.*;

/** TANK DRIVE (SUBSYSTEM) */
import frc.robot.subsystems.TankDrive_Subsystem;

/** TANK DRIVE (COMMANDS) */
import frc.robot.commands.TankDrive_Command;

/** SHOOTER SEQUENCE (SUBSYSTEM) */
import frc.robot.subsystems.Roller_Subsystem;
import frc.robot.subsystems.Shooter_Subsystem;

/** SHOOTER SEQUENCE (COMMANDS) */
import frc.robot.commands.Shoot_SequenceCommand;

/** INTAKE (SUBSYSTEM) */
import frc.robot.subsystems.Intake_Subsystem;

/** INTAKE (COMMANDS) */
import frc.robot.commands.Intake_Command;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

// Pigeon 2.0
import com.ctre.phoenix6.hardware.Pigeon2;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  /** VARIABLES */
  private final Pigeon2 gyroscope = new Pigeon2(TankDriveConstants.pigeon_ID);

  /** SUBSYSTEMS */
  private TankDrive_Subsystem TankDrive_Subsystem = new TankDrive_Subsystem(gyroscope);
  private Shooter_Subsystem Shooter_Subsystem = new Shooter_Subsystem();
  private Roller_Subsystem Roller_Subsystem = new Roller_Subsystem();
  private Intake_Subsystem Intake_Subsystem = new Intake_Subsystem();

  /** XBOX CONTROLLER */
  private final CommandXboxController controller01 = new CommandXboxController(OperatorConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();

    // RESET GYROSCOPE
    gyroscope.reset();
    gyroscope.setYaw(0);
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

    /** On Left Trigger Intake The Balls */
    controller01.leftTrigger().whileTrue(new Intake_Command(Intake_Subsystem));

    /** On Right Trigger Shoot The Balls */
    controller01.rightTrigger().whileTrue(new Shoot_SequenceCommand(Shooter_Subsystem, Roller_Subsystem));
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
