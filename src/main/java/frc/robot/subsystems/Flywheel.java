package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.*;

public class Flywheel extends SubsystemBase{

    private TalonFX motor;
    private MotionMagicVelocityVoltage motionMagic;


    public Flywheel(int motorId, TalonFXConfiguration config, double maxVel, double maxAcc) {
        motor = new TalonFX(motorId, "rio");
        motor.getConfigurator().apply(config);
        setMMConfig(()->maxVel, ()->maxAcc);
    }

    public Command start() {
        return this.runOnce(() -> {
            motor.setControl(motionMagic);
        });
    }
    
    public Command stop() {
        return this.runOnce(() -> {
            motor.stopMotor();
        });
    }

    public Command setMMConfig(DoubleSupplier maxVel, DoubleSupplier maxAcc) {
        return this.runOnce(() -> {
            motionMagic = new MotionMagicVelocityVoltage(maxVel.getAsDouble()).withAcceleration(maxAcc.getAsDouble());
        });
    }

    public double getVelocity() {
        return motor.getVelocity().getValueAsDouble();
    }


}
