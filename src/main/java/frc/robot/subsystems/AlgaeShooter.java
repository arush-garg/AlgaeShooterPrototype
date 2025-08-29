package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.signals.*;

import badgerlog.Dashboard;
import badgerlog.entry.*;
import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj2.command.*;

import frc.robot.constants.ShooterConstants;

public class AlgaeShooter extends SubsystemBase {
    @Entry(EntryType.Subscriber)
    private static double targVel = 50;

    /** In rotations per second squared */
    @Entry(EntryType.Subscriber)
    private static double targAcc = 25;

    @Entry(EntryType.Subscriber)
    private static double kS = ShooterConstants.kS, kV = ShooterConstants.kV, kA = ShooterConstants.kA, kG = ShooterConstants.kG, kP = ShooterConstants.kP, kI = ShooterConstants.kI, kD = ShooterConstants.kD;

    @Entry(EntryType.Publisher)
    private static double leftMotorVel = 0;

    @Entry(EntryType.Publisher)
    private static double rightMotorVel = 0;

    private final Flywheel leftFlywheel;
    private final Flywheel rightFlywheel;

    public AlgaeShooter() {
        Slot0Configs slot0 = new Slot0Configs();
        slot0.kS = kS;
        slot0.kV = kV;
        slot0.kA = kA;
        slot0.kG = kG;
        slot0.kP = kP;
        slot0.kI = kI;
        slot0.kD = kD;

        VoltageConfigs voltages = new VoltageConfigs();
        voltages.PeakForwardVoltage = ShooterConstants.MAX_VOLTS;
        voltages.PeakReverseVoltage = -ShooterConstants.MAX_VOLTS;

        TalonFXConfiguration left_config = new TalonFXConfiguration();
        left_config.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        left_config.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        left_config.Voltage = voltages;
        left_config.Slot0 = slot0;
        leftFlywheel = new Flywheel(ShooterConstants.SHOOTER_LEFT_ID, left_config, targVel, targAcc);

        TalonFXConfiguration right_config = new TalonFXConfiguration();
        right_config.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        right_config.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        right_config.Voltage = voltages;
        right_config.Slot0 = slot0;
        rightFlywheel = new Flywheel(ShooterConstants.SHOOTER_RIGHT_ID, right_config, targVel, targAcc);

        EventLoop buttonLoop = CommandScheduler.getInstance().getDefaultButtonLoop();
        Dashboard.getAutoResettingButton("AlgaeShooter/Apply config", buttonLoop)
                .onTrue(setFlywheelConfigs(() -> targVel, () -> targAcc).ignoringDisable(true));

    }

    public Command start() {
        return Commands.parallel(leftFlywheel.start(), rightFlywheel.start(), new PrintCommand("Flywheel started"));
    }

    public Command stop() {
        return Commands.parallel(leftFlywheel.stop(), rightFlywheel.stop(), new PrintCommand("Flywheel stopped"));
    }

    public Command setFlywheelConfigs(DoubleSupplier vel, DoubleSupplier acc) {
        return Commands.parallel(
            leftFlywheel.setMMConfig(vel, acc),
            rightFlywheel.setMMConfig(vel, acc),
            Commands.runOnce(() -> System.out.printf("Applied max vel = %.2f, max acc = %.2f%n", vel.getAsDouble(), acc.getAsDouble()))
        );
    }

    @Override
    public void periodic() {
        leftMotorVel = leftFlywheel.getVelocity();
        rightMotorVel = rightFlywheel.getVelocity();
    }
}