/*
  * MASTER PROGRAMMERS WORK
  */

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

// == CONSTANTS == \\
import frc.robot.Constants.*;

// == SUBSYSTEMS == \\
import frc.robot.subsystems.TankDrive_Subsystem;

//import frc.robot.subsystems.Roller_Subsystem;
import frc.robot.subsystems.Shooter_Subsystem;
import frc.robot.subsystems.Intake_Subsystem;

import frc.robot.subsystems.Climb_Subsystem;

import frc.robot.subsystems.Pigeon_Subsystem;
import frc.robot.subsystems.Limelight_Subsystem;

// == COMMANDS == \\
//import frc.robot.commands.Align_Commands.Align_Climb_Command;
import frc.robot.commands.Align_Commands.Align_Shoot_Command;

import frc.robot.commands.Climb_Commands.Climb_Command;
import frc.robot.commands.Climb_Commands.Climb_Down;

import frc.robot.commands.DifferentialDrive__Commands.TankDrive_Inverse_Command;
import frc.robot.commands.DifferentialDrive__Commands.TankDrive_Command;

//import frc.robot.commands.Shooter_Commands.Shoot_SequenceCommand;
import frc.robot.commands.Shooter_Commands.Shooter_Command;
import frc.robot.commands.Intake_Commands.Intake_Command;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  /** VARIABLES */

  /** SUBSYSTEMS */
  private TankDrive_Subsystem TankDrive_Subsystem = new TankDrive_Subsystem();

  private Shooter_Subsystem Shooter_Subsystem = new Shooter_Subsystem();
  //private Roller_Subsystem Roller_Subsystem = new Roller_Subsystem();
  private Intake_Subsystem Intake_Subsystem = new Intake_Subsystem();
  
  private Climb_Subsystem Climb_Subsystem = new Climb_Subsystem();

  private Limelight_Subsystem Limelight_Subsystem = new Limelight_Subsystem();
  private Pigeon_Subsystem Pigeon_Subsystem = new Pigeon_Subsystem();

  /** XBOX CONTROLLER */
  private final CommandXboxController controller01 = new CommandXboxController(OperatorConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
  }

  public Pigeon_Subsystem getPigeon() {
    return Pigeon_Subsystem;
  }

  private void configureBindings() {
    /*
     * == KEYBINDS ==
     * LEFT JOYSTICK  : DRIVE
     * RIGHT JOYSTICK : TURN
     * 
     * LEFT TRIGGER   : INTAKE
     * RIGHT TRIGGER  : SHOOT
     * 
     * D-PAD UP       : CLIMB (Level 3)
     * D-PAD DOWN     : CLIMB DOWN (Level 1)
     * 
     * D-PAD RIGHT    : SHOOTER ALIGN
     * D-PAD LEFT     : CLIMBER ALIGN
     * BUTTON X       : ROTATE ROBOT 180 && INVERSE CONTROLS
    */

    // Create commands that can be cancelled only once
    Align_Shoot_Command alignShootCommand = new Align_Shoot_Command(
        Limelight_Subsystem, TankDrive_Subsystem, Pigeon_Subsystem, controller01
    );

    /*
    Align_Climb_Command alignClimbCommand = new Align_Climb_Command(
        Limelight_Subsystem, TankDrive_Subsystem, Pigeon_Subsystem, controller01
    );
    */

    // == XBOX CONTROLLER KEYBINDING == \\
    TankDrive_Subsystem.setDefaultCommand(new TankDrive_Command(TankDrive_Subsystem,
                                        () -> controller01.getLeftY(), 
                                        () -> controller01.getRightX())
    );

    // Intake
    controller01.leftTrigger().whileTrue(new Intake_Command(Intake_Subsystem));

    // Shooter Sequence (Run Shooter Motor -> 2 seconds -> Push Balls into Shooter -> Balls are shot)
    controller01.rightTrigger().whileTrue(new Shooter_Command(Shooter_Subsystem));
    //controller01.rightTrigger().whileTrue(new Shoot_SequenceCommand(Shooter_Subsystem, Roller_Subsystem));

    // Climb Up   (Levels : 3)
    controller01.povUp().onTrue(new Climb_Command(Climb_Subsystem));

    // Climb Down (Levels : 1)
    controller01.povDown().onTrue(new Climb_Down(Climb_Subsystem));

    // Align The Shooter (Cancel on Second press)
    controller01.povRight().onTrue(
        new InstantCommand(() -> {
            if (!alignShootCommand.isScheduled()) {
                alignShootCommand.schedule();
            } else {
                alignShootCommand.cancel();
            }
        })
    );
    
    // Align The Climber (Cancel on Second press)
    // controller01.povLeft().onTrue(
    //    new InstantCommand(() -> {
    //        if (!alignClimbCommand.isScheduled()) {
    //            alignClimbCommand.schedule();
    //        } else {
    //            alignClimbCommand.cancel();
    //        }
    //    })
    //);
    
    // Rotate Robot && Inverse Controls
    controller01.x().onTrue(new TankDrive_Inverse_Command(TankDrive_Subsystem, Pigeon_Subsystem));
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
