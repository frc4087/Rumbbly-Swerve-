// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.PhysicsSim;
import au.grapplerobotics.LaserCan;
import frc.robot.Robot;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class FrankenArm extends SubsystemBase {
  
  public LaserCan LaunchSensor = new LaserCan(TunerConstants.LaunchSensor);
  public LaserCan IntakeSensor = new LaserCan(TunerConstants.IntakeSensor);
  public TalonFX IntakeFeedMotor = new TalonFX(TunerConstants.IntakeFeed);
  public TalonFX IntakeCenterMotor = new TalonFX(TunerConstants.IntakeCenter);
  public TalonFX LauncherFeedMotor = new TalonFX(TunerConstants.LaunchFeed);
  public TalonFX LaunchRtFlywheel = new TalonFX(TunerConstants.LaunchRtFlywheel);
  public TalonFX LaunchLtFlywheel = new TalonFX(TunerConstants.LaunchLtFlywheel);

  public final TalonFX m_fx = new TalonFX(TunerConstants.ArmAngle);
  public final CANcoder m_ex = new CANcoder(TunerConstants.ArmSensor);
  public final MotionMagicVoltage m_mmReq = new MotionMagicVoltage(0);
  public final CommandXboxController m_joystick = new CommandXboxController(1);
  public final double k_tolerance = 3.0;
  public boolean isChecking = false;
 
  double setpoint1 = 05;
  double setpoint2 = 10;
  double setpoint3 = 20;
  double fullSpeedAhead = 1;
  double Die = 0;
  double warmup = 0.5;  

    public void initDefaultCommand() {
    if (m_joystick.button(8).getAsBoolean()) {
      m_fx.setControl(m_mmReq.withPosition(setpoint1));
      LaunchRtFlywheel.set(warmup);
      LaunchLtFlywheel.set(warmup);
      }
      

    if (m_joystick.button(1).getAsBoolean()) {
      m_fx.setControl(m_mmReq.withPosition(setpoint1));
      IntakeFeedMotor.set(fullSpeedAhead);
      IntakeCenterMotor.set(fullSpeedAhead);
      LauncherFeedMotor.set(fullSpeedAhead);
      shootOff();
      //startChecking();
      intakeOff();
      }

    if (m_joystick.button(2).getAsBoolean()) {
      m_fx.setControl(m_mmReq.withPosition(setpoint2));
      }

    if (m_joystick.button(3).getAsBoolean()) {
      m_fx.setControl(m_mmReq.withPosition(setpoint3));
      }

    if (m_joystick.button(4).getAsBoolean()) {
      //m_fx.setControl(m_mmReq.withPosition(setpoint1));
      LaunchRtFlywheel.set(1);
      LaunchLtFlywheel.set(1);
      LauncherFeedMotor.set(1);
      }
    }

    public void shootOff(){
    LaserCan.Measurement measurement = IntakeSensor.getMeasurement();
    if (measurement.status == LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT && measurement.distance_mm <= 100) {
      IntakeFeedMotor.set(Die);
      IntakeCenterMotor.set(Die);
      LauncherFeedMotor.set(Die);
    }
  }
  
  public void intakeOff(){
    LaserCan.Measurement measurement = LaunchSensor.getMeasurement();
    if (measurement.status == LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT && measurement.distance_mm <= 100) {
      LauncherFeedMotor.set(Die);
    }
  }

// public void checkSet() {
//     double currentPosition = m_ex.getPosition().getValue(); // Extract the value from StatusSignal
//     if (Math.abs(currentPosition - setpoint1) <= k_tolerance) {
//         LauncherFeedMotor.set(0.5);
//     } else {
//         LauncherFeedMotor.set(0);
//     }
//}

// public void startChecking() {
//     if (!isChecking) {
//         isChecking = true;
//         new Thread(() -> {
//             while (isChecking) {
//                 double currentPosition = m_ex.getPosition().getValue(); // Extract the value from StatusSignal
//                 if (Math.abs(currentPosition - setpoint1) <= k_tolerance) {
//                     LauncherFeedMotor.set(warmup);
//                     isChecking = false; // Stop checking once within tolerance
//                 } else {
//                     LauncherFeedMotor.set(fullSpeedAhead);
//                 }
//                 try {
//                     Thread.sleep(100); // Sleep for a short period to avoid busy-waiting
//                 } catch (InterruptedException e) {
//                     Thread.currentThread().interrupt();
//                 }
//             }
//         }).start();
//     }
// }
}