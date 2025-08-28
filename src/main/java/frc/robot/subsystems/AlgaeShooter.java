package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.*;

import badgerlog.Dashboard;
import badgerlog.entry.*;
import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj2.command.*;

import frc.robot.constants.ShooterConstants;

public class AlgaeShooter extends SubsystemBase {
    @Entry(EntryType.Subscriber)
    private static double MAX_VEL = 50;

    /** In rotations per second<sup>2</sup> */
    @Entry(EntryType.Subscriber)
    private static double MAX_ACC = 25;

    @Entry(EntryType.Publisher)
    private static double leftMotorVel = 0;

    @Entry(EntryType.Publisher)
    private static double rightMotorVel = 0;

    private final Flywheel leftFlywheel;
    private final Flywheel rightFlywheel;

    public AlgaeShooter() {
        TalonFXConfiguration left_config = new TalonFXConfiguration();
        left_config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        left_config.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        left_config.Voltage.PeakForwardVoltage = ShooterConstants.MAX_VOLTS;
        left_config.Voltage.PeakReverseVoltage = -ShooterConstants.MAX_VOLTS;
        leftFlywheel = new Flywheel(ShooterConstants.SHOOTER_LEFT_ID, left_config, MAX_VEL, MAX_ACC);

        TalonFXConfiguration right_config = new TalonFXConfiguration();
        right_config.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        right_config.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        right_config.Voltage.PeakForwardVoltage = ShooterConstants.MAX_VOLTS;
        right_config.Voltage.PeakReverseVoltage = -ShooterConstants.MAX_VOLTS;
        rightFlywheel = new Flywheel(ShooterConstants.SHOOTER_RIGHT_ID, right_config, MAX_VEL, MAX_ACC);

        EventLoop buttonLoop = CommandScheduler.getInstance().getDefaultButtonLoop();
        Dashboard.getAutoResettingButton("AlgaeShooter/Apply config", buttonLoop)
                .onTrue(setFlywheelConfigs(() -> MAX_VEL, () -> MAX_ACC).ignoringDisable(true));

    }

    public Command start() {
        return Commands.parallel(leftFlywheel.start(), rightFlywheel.start());
    }

    public Command stop() {
        return Commands.parallel(leftFlywheel.stop(), rightFlywheel.stop());
    }

    public Command setFlywheelConfigs(DoubleSupplier maxVel, DoubleSupplier maxAcc) {
        return Commands.parallel(
            leftFlywheel.setMMConfig(maxVel, maxAcc),
            rightFlywheel.setMMConfig(maxVel, maxAcc),
            Commands.runOnce(() -> System.out.printf("Applied max vel = %.2f, max acc = %.2f%n", maxVel.getAsDouble(), maxAcc.getAsDouble()))
        );
    }

    @Override
    public void periodic() {
        leftMotorVel = leftFlywheel.getVelocity();
        rightMotorVel = rightFlywheel.getVelocity();
    }
}