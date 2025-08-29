// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.AlgaeShooter;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;


public class RobotContainer {
  private AlgaeShooter algaeShooter = new AlgaeShooter();
  private final CommandXboxController m_controller = new CommandXboxController(2);
  
  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {
    if(!RobotBase.isSimulation()) {
      m_controller.a()
      .onTrue(algaeShooter.start()); 

      m_controller.b()
      .onTrue(algaeShooter.stop());
    }

    RobotModeTriggers.disabled().onTrue(
      algaeShooter.stop().ignoringDisable(true)
    );
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
