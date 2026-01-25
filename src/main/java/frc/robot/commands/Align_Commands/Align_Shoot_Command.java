// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Align_Commands;

import frc.robot.subsystems.Limelight_Subsystem;
import frc.robot.subsystems.TankDrive_Subsystem;
import frc.robot.subsystems.Pigeon_Subsystem;

import frc.robot.Constants.Shoot_Align_Constants;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class Align_Shoot_Command extends Command {
  private Limelight_Subsystem Limelight_Subsystem;
  private TankDrive_Subsystem TankDrive_Subsystem;
  private Pigeon_Subsystem Pigeon_Subsystem;

  private final CommandXboxController controller;

  private final Timer timer = new Timer();

  private boolean rumbled = false;

  /** Creates a new Align_Command. */
  public Align_Shoot_Command(Limelight_Subsystem Limelight_Subsystem, TankDrive_Subsystem TankDrive_Subsystem, Pigeon_Subsystem Pigeon_Subsystem,CommandXboxController controller) 
  {
    // Use addRequirements() here to declare subsystem dependencies.
    this.Limelight_Subsystem = Limelight_Subsystem;
    this.TankDrive_Subsystem = TankDrive_Subsystem;
    this.Pigeon_Subsystem = Pigeon_Subsystem;
    this.controller = controller;

    addRequirements(Limelight_Subsystem, TankDrive_Subsystem, Pigeon_Subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
    double drive, turn, left, right;
    double angle_offset, tx, z;
    double heading_error;

    if (!Limelight_Subsystem.hasValidTag()) return;

    tx = Limelight_Subsystem.getTX();
    z = Limelight_Subsystem.getFilteredDistanceZ();

    angle_offset = Math.toDegrees(Math.atan2(Shoot_Align_Constants.X_OFFSET, z));
    
    tx += Limelight_Subsystem.has_AprilTag_Left() ? -angle_offset :
          Limelight_Subsystem.has_AprilTag_Right() ? angle_offset : 
          0.0;

    // If anything delete this
    heading_error = Limelight_Subsystem.has_AprilTag_Left() ? -45 :
                    Limelight_Subsystem.has_AprilTag_Right() ? 45 : 
                    0.0;

    turn = tx * Shoot_Align_Constants.KTURNP;

    turn += Pigeon_Subsystem.getHeadingError(heading_error) * Shoot_Align_Constants.KYAWP;

    drive = (z - Shoot_Align_Constants.DESIRED_DISTANCE)
              * Shoot_Align_Constants.KDRIVEP;

    left  = drive + turn;
    right = drive - turn;

    double maxMagnitude = Math.max(Math.abs(left), Math.abs(right));

    if (maxMagnitude > Shoot_Align_Constants.MAX_OUTPUT) {
        left  *= Shoot_Align_Constants.MAX_OUTPUT / maxMagnitude;
        right *= Shoot_Align_Constants.MAX_OUTPUT / maxMagnitude;
    }

    left = Math.abs(left) < 0.05 ? 0 : left;
    right = Math.abs(right) < 0.05 ? 0 : right;

    // Check alignment
    boolean aligned =
        Limelight_Subsystem.hasValidTag()
        && Math.abs(tx) < Shoot_Align_Constants.TX_TOLERANCE
        && Math.abs(z - Shoot_Align_Constants.DESIRED_DISTANCE) < Shoot_Align_Constants.Z_TOLERANCE;

    // Rumble ONCE when aligned
    if (aligned && !rumbled) {
      TankDrive_Subsystem.stop();

      controller.setRumble(RumbleType.kBothRumble, 1.0);

      rumbled = true;
      timer.start();
    }

    TankDrive_Subsystem.set(left, right);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    // Stop rumble
    controller.setRumble(RumbleType.kBothRumble, 0.0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    /** RUMBLE CONTROLLER FOR 0.5 SECONDS THEN FINISH */
    return rumbled && timer.hasElapsed(0.5);
  }
}
