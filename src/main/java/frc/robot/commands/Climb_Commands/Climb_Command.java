// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Climb_Commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import frc.robot.subsystems.Climb_Subsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class Climb_Command extends SequentialCommandGroup {
  /** Creates a new Climb_Command. */
  public Climb_Command(Climb_Subsystem Climb_Subsystem) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    /*
     * Climb Loop
     *  Climber reaches Rung One/Two/Three and latches
     *  Wait (1 second)
     *  Climber pulls robot up
     */

    addCommands(
      // FIRST RUNG
      new Climb_Level1(Climb_Subsystem).withTimeout(3.0),
      new WaitCommand(1),
      new Climb_Self(Climb_Subsystem).withTimeout(3.0),
      new WaitCommand(0.5),

      // SECOND RUNG
      new Climb_Level2(Climb_Subsystem).withTimeout(3.0),
      new WaitCommand(1),
      new Climb_Self(Climb_Subsystem).withTimeout(3.0),
      new WaitCommand(0.5),
      
      // THIRD RUNG
      new Climb_Level3(Climb_Subsystem).withTimeout(3.0),
      new WaitCommand(1),
      new Climb_Self(Climb_Subsystem).withTimeout(3.0)
    );

    addRequirements(Climb_Subsystem);
  }
}
