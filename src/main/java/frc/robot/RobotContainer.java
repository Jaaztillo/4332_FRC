package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
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

// Commands
import frc.robot.commands.DifferentialDriveCommands.TankDrive;
import frc.robot.commands.DifferentialDriveCommands.AlignToTarget;
import frc.robot.commands.DifferentialDriveCommands.AlignToTower;
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

    /*********************************************************************************
     *                      //====== KEYBINDS ======\\
     * 
     *                        LEFT JOYSTICK  | DRIVE
     *                        RIGHT JOYSTICK | TURN
     * 
     *                        LEFT BUMPER    | EXTEND / RETRACT INTAKE                       
     *                        LEFT TRIGGER   | INTAKE
     *                        
     *                        RIGHT TRIGGER  | SHOOT TO TARGET
     * 
     *                        BUTTON Y       | CLIMB TO LEVEL 3
     *                        BUTTON X       | ALIGN TO LEFT RUNG
     *                        BUTTON B       | ALIGN TO RIGHT RUNG
     * 
     **********************************************************************************/

    // What if aldo doesn't align with anything what if when he pressed the shoot button depending on the robots pose on the field
    // We shoot either to our alliance side, outpost or hub

    // == TANKDRIVE BINDING == \\
    tankDrive.setDefaultCommand(
        new TankDrive(tankDrive,
          () -> controller01.getLeftY(), () -> controller01.getRightX())
    );

    // ==== INTAKE BINDING ==== \\

    controller01.leftTrigger().whileTrue(new IntakeFuel(intake));
    controller01.leftBumper().toggleOnTrue(new ExtendIntake(extension));

    // ==== SHOOTER BINDING ==== \\

    // On right trigger run sequence command align to target then shoot
    controller01.rightTrigger().whileTrue(new ShootFuel(shooter, roller));

    // ==== CLIMBER BINDING ==== \\

    // CLIMB TESTING (Get target positions for the constants)
    controller01.povUp().whileTrue(new ClimbFoward(climber));
    controller01.povDown().whileTrue(new ClimbReverse(climber));

    controller01.y().onTrue(new ClimbLevel_3(climber));
    controller01.x().whileTrue(new AlignToTower(tankDrive, limelight, controller01, "Left"));
    controller01.b().whileTrue(new AlignToTower(tankDrive, limelight, controller01, "Right"));
  }

  /**
   * Gets the climber subsystem to allow it to do things in teleop init/disabled and more methods
   * @return climber subsystem
   */
  public ClimberSubsystem getClimber () {
    return climber;
  }

  /**
   * gets the autonomous command with the name given to it
   * @param autoName the name of the autonomous you want to perform
   * @return the autnomous command with the name given
   */
  public Command AutonomousCommand (String autoName) {
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