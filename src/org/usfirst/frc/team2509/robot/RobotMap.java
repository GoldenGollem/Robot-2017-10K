/**
 * @author Tigerbots
 */
package org.usfirst.frc.team2509.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
    public static CANTalon CLIMB_MOTOR;
    public static CANTalon CLIMB_ALT;
     public static CANTalon DT_LEFTFRONT;
    public static CANTalon DT_RIGHTFRONT;
    public static CANTalon DT_LEFTREAR;
    public static CANTalon DT_RIGHTREAR;
    public static ADXRS450_Gyro DT_GYRO;
    public static RobotDrive DRIVETRAIN;
    public static UsbCamera GEAR_CAM;
    public static DigitalInput GEAR_SWITCH;
    public static UsbCamera SHOOT_CAM;
    public static CANTalon SHOOT_MOTOR;
    public static Encoder SHOOT_ENCODER;
    public static Talon SHOOT_KICKER;
    public static Talon GATE;
    public static CANTalon SWEEP_MOTOR;

    public static void init() {
    	CLIMB_MOTOR = new CANTalon(4);
    	CLIMB_MOTOR.setInverted(true);
    	LiveWindow.addActuator("Climb", "Motor 1", CLIMB_MOTOR);
        CLIMB_ALT = new CANTalon(7);
        CLIMB_ALT.setInverted(true);
        LiveWindow.addActuator("Climb", "Motor 2", CLIMB_ALT);
        
        GATE = new Talon(0);
        GATE.setInverted(true);
        LiveWindow.addActuator("Shooter", "Gate", GATE);
        
        DT_LEFTFRONT = new CANTalon(2);
        LiveWindow.addActuator("DriveTrain", "DT_LEFTFRONT", DT_LEFTFRONT);
        
        DT_RIGHTFRONT = new CANTalon(1);
        LiveWindow.addActuator("DriveTrain", "DT_RIGHTFRONT", DT_RIGHTFRONT);
        
        DT_LEFTREAR = new CANTalon(0);
        LiveWindow.addActuator("DriveTrain", "DT_LEFTREAR", DT_LEFTREAR);
        
        DT_RIGHTREAR = new CANTalon(3);
        LiveWindow.addActuator("DriveTrain", "DT_RIGHTREAER", DT_RIGHTREAR);
        
        DT_GYRO = new ADXRS450_Gyro();
       	DT_GYRO.reset();
       	DT_GYRO.calibrate();
        SmartDashboard.putDouble("GYRO", RobotMap.DT_GYRO.getAngle());
        LiveWindow.addSensor("DriveTrain", "DT_GYRO", DT_GYRO);
       	
        DRIVETRAIN = new RobotDrive(DT_LEFTFRONT, DT_LEFTREAR,DT_RIGHTFRONT, DT_RIGHTREAR);
        DRIVETRAIN.setSafetyEnabled(false);
        DRIVETRAIN.setExpiration(1.0);
        DRIVETRAIN.setSensitivity(1.0);
        DRIVETRAIN.setMaxOutput(1.0);
        DRIVETRAIN.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        DRIVETRAIN.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
            
        GEAR_CAM = CameraServer.getInstance().startAutomaticCapture("GEAR", 0);
        GEAR_CAM.setBrightness(0);
        GEAR_CAM.setResolution(160,120);
        GEAR_CAM.setFPS(30);
        
        GEAR_SWITCH = new DigitalInput(0);
        LiveWindow.addSensor("GEAR SWITCH", 0, GEAR_SWITCH);
        
        SHOOT_CAM = CameraServer.getInstance().startAutomaticCapture("SHOOTER", 1);
        SHOOT_CAM.setBrightness(0);
        SHOOT_CAM.setResolution(160, 120);
        
        SHOOT_KICKER = new Talon(1);
      //SHOOT_KICKER.setInverted(true);
        LiveWindow.addActuator("Shooter", "Kicker", SHOOT_KICKER);
        
        SHOOT_MOTOR = new CANTalon(6);
        SHOOT_MOTOR.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
        SHOOT_MOTOR.reverseSensor(false);
        SHOOT_MOTOR.configNominalOutputVoltage(+0.0f, -0.0f);
        SHOOT_MOTOR.configPeakOutputVoltage(+12.0f, -12.0f);
        SHOOT_MOTOR.setProfile(0); 
        SHOOT_MOTOR.setF(0);
        SHOOT_MOTOR.setP(0.04);//0.1
        SHOOT_MOTOR.setI(0.0002);//0.0002
        SHOOT_MOTOR.setD(0.0001);//0.0001
        SHOOT_MOTOR.changeControlMode(TalonControlMode.Speed);
        LiveWindow.addActuator("Shooter", "Motor", (CANTalon) SHOOT_MOTOR);
                
        SWEEP_MOTOR = new CANTalon(5);
        LiveWindow.addActuator("Sweeper", "Motor", (CANTalon) SWEEP_MOTOR);
        
    }
}
