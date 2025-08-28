package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.*;

public class Flywheel extends SubsystemBase{

    private TalonFX m_motor;
    private MotionMagicVelocityVoltage m_motionMagic;


    public Flywheel(int motorId, TalonFXConfiguration config, double maxVel, double maxAcc) {
        m_motor = new TalonFX(motorId, "rio");
        m_motor.getConfigurator().apply(config);
        setMMConfig(()->maxVel, ()->maxAcc);
    }

    public Command start() {
        return this.runOnce(() -> {
            m_motor.setControl(m_motionMagic);
        });
    }
    
    public Command stop() {
        return this.runOnce(() -> {
            m_motor.stopMotor();
        });
    }

    public Command setMMConfig(DoubleSupplier maxVel, DoubleSupplier maxAcc) {
        return this.runOnce(() -> {
            m_motionMagic = new MotionMagicVelocityVoltage(maxVel.getAsDouble()).withAcceleration(maxAcc.getAsDouble());
        });
    }

    public double getVelocity() {
        return m_motor.getVelocity().getValueAsDouble();
    }


}
