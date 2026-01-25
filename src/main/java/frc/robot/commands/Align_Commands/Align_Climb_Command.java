// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Align_Commands;


import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.Timer;

import frc.robot.subsystems.Limelight_Subsystem;
import frc.robot.subsystems.TankDrive_Subsystem;
import frc.robot.subsystems.Pigeon_Subsystem;

import frc.robot.Constants.Climb_Align_Constants;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class Align_Climb_Command extends Command {
  private Limelight_Subsystem Limelight_Subsystem;
  private TankDrive_Subsystem TankDrive_Subsystem;
  private Pigeon_Subsystem Pigeon_Subsystem;

  private final CommandXboxController controller;

  private final Timer timer = new Timer();

  private boolean rumbled = false;

  private boolean position_reached = false;
  private boolean aligned = false;

  /** Creates a new Align_Climb_Command. */
  public Align_Climb_Command(Limelight_Subsystem Limelight_Subsystem, TankDrive_Subsystem TankDrive_Subsystem, Pigeon_Subsystem Pigeon_Subsystem, CommandXboxController controller) {
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
  public void execute() {
    double drive, turn, left, right;
    double desired_distance, tx, z;

    if (!Limelight_Subsystem.hasValidTag()) return;

    tx = Limelight_Subsystem.getTX();
    z = Limelight_Subsystem.getFilteredDistanceZ();

    tx += Math.toDegrees(Math.atan2(Climb_Align_Constants.X_OFFSET, z));

    desired_distance = position_reached ? 
      Climb_Align_Constants.DESIRED_DISTANCE_02: 
      Climb_Align_Constants.DESIRED_DISTANCE_01;

    turn = tx * Climb_Align_Constants.KTURNP;

    /* if anything delete the turn logic and add this:
    * turn = position_reached ?
    *       Pigeon_Subsystem.getHeadingError(Climb_Align_Constants.FINAL_YAW):
    *       tx * Climb_Align_Constants.KTURNP;
    */

    turn += Pigeon_Subsystem.getHeadingError(Climb_Align_Constants.FINAL_YAW) * Climb_Align_Constants.KYAWP;

    drive = (z - desired_distance) * Climb_Align_Constants.KDRIVEP;

    left  = drive + turn;
    right = drive - turn;

    double maxMagnitude = Math.max(Math.abs(left), Math.abs(right));

    if (maxMagnitude > Climb_Align_Constants.MAX_OUTPUT) {
        left  *= Climb_Align_Constants.MAX_OUTPUT / maxMagnitude;
        right *= Climb_Align_Constants.MAX_OUTPUT / maxMagnitude;
    }

    left = Math.abs(left) < 0.05 ? 0 : left;
    right = Math.abs(right) < 0.05 ? 0 : right;

    // Check alignment
    if (!position_reached) {
      position_reached =
        Limelight_Subsystem.hasValidTag()
        && Math.abs(tx) < Climb_Align_Constants.TX_TOLERANCE
        && (Math.abs(z - Climb_Align_Constants.DESIRED_DISTANCE_01) < Climb_Align_Constants.Z_TOLERANCE
        || Pigeon_Subsystem.getHeading() == 0.0);
    } else {
      aligned = 
        Limelight_Subsystem.hasValidTag()
        && Math.abs(tx) < Climb_Align_Constants.TX_TOLERANCE
        && Math.abs(z - Climb_Align_Constants.DESIRED_DISTANCE_02) < Climb_Align_Constants.Z_TOLERANCE;
    }

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
