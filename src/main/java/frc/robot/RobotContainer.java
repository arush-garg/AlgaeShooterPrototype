// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.AlgaeShooter;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.button.Trigger;


public class RobotContainer {
  private AlgaeShooter algaeShooter = new AlgaeShooter();
  private final XboxController m_controller = new XboxController(2);
  
  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {
    if(!RobotBase.isSimulation()) {
      new Trigger(() -> m_controller.getAButton())
      .onTrue(algaeShooter.start()); 

      new Trigger(() -> m_controller.getBButton())
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
