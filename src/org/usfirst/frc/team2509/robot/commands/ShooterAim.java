package org.usfirst.frc.team2509.robot.commands;

import java.util.ArrayList;
import java.util.Iterator;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team2509.robot.Robot;
import org.usfirst.frc.team2509.robot.RobotMap;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ShooterAim extends Command {
	public Command filterTargets = new FilterGearTarget();
	private ArrayList<MatOfPoint>
		contours = new ArrayList<MatOfPoint>();
	private final CvSink
		CVSINK = CameraServer.getInstance().getVideo("SHOOTER");
	private final CvSource 
		OUTPUT_STREAM = CameraServer.getInstance().putVideo("ALT-SHOOTER", 640, 480);
	private final Mat
		BINARY = new Mat(),
		CLUSTERS = new Mat(),
		HEIRARCHY = new Mat(),
		HSV = new Mat(),
		SOURCE = new Mat(),
		THRESH = new Mat();
	protected final Scalar 
	//COLOR VALUES
		BLACK = new Scalar(0,0,0),
		BLUE = new Scalar(255, 0, 0),
		GREEN = new Scalar(0, 255, 0),
		RED = new Scalar(0, 0, 255),
		YELLOW = new Scalar(0, 255, 255),
		//Thresholds values
		LOWER_BOUNDS = new Scalar(180,190,40),
		UPPER_BOUNDS = new Scalar(200,210,60);
		private final RobotDrive DT = RobotMap.DRIVETRAIN;
		private final ADXRS450_Gyro GYRO = RobotMap.DT_GYRO;
		private final DigitalInput SWITCH = RobotMap.GEAR_SWITCH;
		private Rect TARGET;
    public ShooterAim() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	new Thread(()->{
			while(true){
				contours.clear();
				RobotMap.SHOOT_CAM.setBrightness(0);
				RobotMap.SHOOT_CAM.setResolution(160,120);
				CVSINK.grabFrame(SOURCE);
				Imgproc.cvtColor(SOURCE, HSV, Imgproc.COLOR_BGR2RGB);
				Imgproc.threshold(HSV, BINARY, 180, 190, Imgproc.THRESH_BINARY_INV);	
				Imgproc.cvtColor(BINARY, THRESH, Imgproc.COLOR_HSV2BGR);
				Imgproc.cvtColor(THRESH, CLUSTERS, Imgproc.COLOR_BGR2GRAY);
				Mat GRAY = CLUSTERS;
				Imgproc.Canny(GRAY, HEIRARCHY, 2, 4);
				Imgproc.findContours(HEIRARCHY, contours, new Mat(),Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		        for(MatOfPoint mop :contours){
					Rect rec = Imgproc.boundingRect(mop);
					Imgproc.rectangle(SOURCE, rec.br(), rec.tl(), RED);
				}
				for(Iterator<MatOfPoint> iterator = contours.iterator();iterator.hasNext();){
					MatOfPoint matOfPoint = (MatOfPoint) iterator.next();
					Rect rec = Imgproc.boundingRect(matOfPoint);
					//float aspect = (float)rec.width/(float)rec.height;
					if( rec.height < 8||rec.height>25){
						iterator.remove();
						continue;
					}
					TARGET = rec;
					//DISTANCE = ((rec.height*(-1))+15);
					//DISTANCE = rec.height;
					//TARGETSPEED = (Math.sqrt((DISTANCE+2.5)*(0.3048)*(9.8)/(0.85))*(751.9113));
					//TARGETSPEED = Math.sqrt((DISTANCE+2.5)*(0.3048)*(9.8)/(0.85)*(751.9113586737));
					SmartDashboard.putNumber("S_Contours", contours.size());
					SmartDashboard.putNumber("S_X", rec.x);
					SmartDashboard.putNumber("S_Width", rec.width);
					SmartDashboard.putNumber("S_Height", rec.height);
					//SmartDashboard.putNumber("S_Distance", DISTANCE);
					//SmartDashboard.putNumber("Target Speed", TARGETSPEED);
				}			
				OUTPUT_STREAM.putFrame(SOURCE);
			}
		}).start();
    	//Target 115
    	
    		
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(TARGET != null&&Robot.oi.OPSTICK.getRawButton(3)){
    		System.out.println("AIMING NOW");
    		while(TARGET.x<=90||TARGET.x>=100&&Robot.oi.OPSTICK.getRawButton(3)){
    			if(TARGET.x>100&&Robot.oi.OPSTICK.getRawButton(3)){
    				System.out.println("To The Right");
    				DT.mecanumDrive_Cartesian(0, 0, 0.3, 0);
    				System.out.println("To The Right");
    				Timer.delay(0.1);
    				DT.drive(0, 0);
    			}else if(TARGET.x<90&&Robot.oi.OPSTICK.getRawButton(3)){
    				System.out.println("To The Left");
    				DT.mecanumDrive_Cartesian(0, 0, -0.3, 0);
    				System.out.println("To The Left");
    				Timer.delay(0.1);
    				DT.drive(0, 0);
    			}else{
    				DT.drive(0, 0);
    				System.out.println("Now Kick");
    				end();
    			}
    		}
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(Robot.oi.OPSTICK.getRawButton(3)){
    		return false;
    	}else{
    		Timer.delay(2.5);
    		return true;
    	}
    }

    // Called once after isFinished returns true
    protected void end() {
    	DT.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
