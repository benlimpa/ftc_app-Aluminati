package com.qualcomm.ftcrobotcontroller.opmodes;

import java.util.HashMap;
import java.util.HashSet;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

/**
 * Created by BenL on 12/28/15.
 */
public class RobotHardware extends OpMode {

    // Controllers
    private DcMotorController[] motorControlrs;

    private ServoController   servoControlr;

    // Motors
    private HashMap<String, DcMotor> motors;
    /*
    private DcMotor leftDrive;
    private DcMotor rightDrive;

    private DcMotor pimpWheel;

    private DcMotor leftSpool;
    private DcMotor rightSpool;

    private DcMotor leftRangle;
    private DcMotor rightRangle;

    private DcMotor brush;
*/
    // Servos
    private HashMap<String, Servo> servos;
    
    private Servo leftBoxServo;
    private Servo rightBoxServo;

    private Servo leftClawServo;
    private Servo rightClawServo;

    private Servo leftBrushArmServo;
    private Servo rightBrushArmServo;

    private double leftBoxServoPos;
    private double rightBoxServoPos;

    private double leftClawServoPos;
    private double rightClawServoPos;

    private double leftBrushArmServoPos;
    private double rightBrushArmServoPos;

    // Miscellaneous
    private HashSet<String> unmappedComponents;
    private HashSet<String> otherErrors;
    private boolean driveEncoders;
    
    // Constants
    protected final boolean RIGHT = true;
    protected final boolean LEFT = false;

    protected final int PIMP_UP = 60; // Rotation in degrees
    protected final int PIMP_DOWN = 580; // Rotation in degrees

    protected final double L_BOX_UP = 0;
    protected final double L_BOX_DOWN = 0.6;
    protected final double R_BOX_UP = 0.6;
    protected final double R_BOX_DOWN = 0;

    protected final double L_CLAW_UP = 0.1;
    protected final double L_CLAW_DOWN = 0.7;
    protected final double R_CLAW_UP = 1;
    protected final double R_CLAW_DOWN = 0.35;

    protected final double L_BRUSH_BAR_UP = 0.25;
    protected final double L_BRUSH_BAR_DOWN = 0.8;
    protected final double R_BRUSH_BAR_UP = 0.53;
    protected final double R_BRUSH_BAR_DOWN = 0.2;

    public RobotHardware(boolean driveEncoders)
    {
        this.driveEncoders = driveEncoders;
    }


    @Override
    public void init()
    {
        unmappedComponents = new HashSet<String>();
        motors = new HashMap<String, DcMotor>();
        //
        // Map Robot Components
        //
        
        // Motor Controllers
        motorControlrs = new DcMotorController[4];
        for (int i = 0; i < 4; i++) 
        {
            try
            {
                motorControlrs[i] = hardwareMap.dcMotorController.get("MotorControl" + (i + 1));
            }
            catch (Exception e)
            {
                unmappedComponents.add("MotorControl" + (i + 1));
                DbgLog.logStacktrace(e);
            }
            
            // Set Device Mode
            if (motorControlrs[i] != null)
                motorControlrs[i].setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
        }
        
        // Servo Controllers
        try
        {
            servoControlr = hardwareMap.servoController.get("ServoControl");
        }
        catch (Exception e)
        {
            unmappedComponents.add("ServoControl");
            DbgLog.logStacktrace(e);
        }
        
        // Motors
        try
        {
            motors.put("leftDrive", hardwareMap.dcMotor.get("leftDrive"));
            
            if (motors.get("leftDrive") != null && driveEncoders)
                motors.get("leftDrive").setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        }
        catch (Exception e)
        {
            unmappedComponents.add("leftDrive");
            DbgLog.logStacktrace(e);
        }

        try
        {
            motors.put("rightDrive", hardwareMap.dcMotor.get("rightDrive"));

            if (motors.get("rightDrive") != null)
            {
                motors.get("rightDrive").setDirection(DcMotor.Direction.REVERSE);
                if (driveEncoders)
                    motors.get("rightDrive").setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
            }
        }
        catch (Exception e)
        {
            unmappedComponents.add("rightDrive");
            DbgLog.logStacktrace(e);
        }

        try
        {
            motors.put("pimpWheel", hardwareMap.dcMotor.get("pimpWheel"));

            if (motors.get("pimpWheel") != null)
            {
                motors.get("pimpWheel").setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
                motors.get("pimpWheel").setPower(1);
            }
        }
        catch (Exception e)
        {
            unmappedComponents.add("pimpWheel");
            DbgLog.logStacktrace(e);
        }

        try
        {
            motors.put("leftSpool", hardwareMap.dcMotor.get("leftSpool"));
        }
        catch (Exception e)
        {
            unmappedComponents.add("leftSpool");
            DbgLog.logStacktrace(e);
        }

        try
        {
            motors.put("rightSpool", hardwareMap.dcMotor.get("rightSpool"));

            if (motors.get("rightSpool") != null)
                motors.get("rightSpool").setDirection(DcMotor.Direction.REVERSE);
        }
        catch (Exception e)
        {
            unmappedComponents.add("rightSpool");
            DbgLog.logStacktrace(e);
        }

        try
        {
            motors.put("leftRangle", hardwareMap.dcMotor.get("leftRangle"));
        }
        catch (Exception e)
        {
            unmappedComponents.add("leftRangle");
            DbgLog.logStacktrace(e);
        }

        try
        {
            motors.put("rightRangle", hardwareMap.dcMotor.get("rightRangle"));

            if (motors.get("rightRangle") != null)
                motors.get("rightRangle").setDirection(DcMotor.Direction.REVERSE);
        }
        catch (Exception e)
        {
            unmappedComponents.add("rightRangle");
            DbgLog.logStacktrace(e);
        }

        try
        {
            motors.put("brush", hardwareMap.dcMotor.get("rightRangle"));
        }
        catch (Exception e)
        {
            unmappedComponents.add("brush");
            DbgLog.logStacktrace(e);
        }
        
        /*
        try
        {
            leftDrive = hardwareMap.dcMotor.get("leftDrive");

            if (leftDrive != null && driveEncoders)
                leftDrive.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        }
        catch (Exception e)
        {
            unmappedComponents.add("leftDrive");
            DbgLog.logStacktrace(e);
        }

        try
        {
            rightDrive = hardwareMap.dcMotor.get("rightDrive");

            if (rightDrive != null)
            {
                rightDrive.setDirection(DcMotor.Direction.REVERSE);
                if (driveEncoders)
                    rightDrive.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
            }
        }
        catch (Exception e)
        {
            unmappedComponents.add("rightDrive");
            DbgLog.logStacktrace(e);
        }

        try
        {
            pimpWheel = hardwareMap.dcMotor.get("pimpWheel");

            if (pimpWheel != null)
            {
                pimpWheel.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
                pimpWheel.setPower(1);
            }
        }
        catch (Exception e)
        {
            unmappedComponents.add("pimpWheel");
            DbgLog.logStacktrace(e);
        }

        try
        {
            leftSpool = hardwareMap.dcMotor.get("leftSpool");
        }
        catch (Exception e)
        {
            unmappedComponents.add("leftSpool");
            DbgLog.logStacktrace(e);
        }
        
        try
        {
            rightSpool = hardwareMap.dcMotor.get("rightSpool");

            if (rightSpool != null)
                rightSpool.setDirection(DcMotor.Direction.REVERSE);
        }
        catch (Exception e)
        {
            unmappedComponents.add("rightSpool");
            DbgLog.logStacktrace(e);
        }

        try
        {
            leftRangle = hardwareMap.dcMotor.get("leftRangle");
        }
        catch (Exception e)
        {
            unmappedComponents.add("leftRangle");
            DbgLog.logStacktrace(e);
        }

        try
        {
            rightRangle = hardwareMap.dcMotor.get("rightRangle");

            if (rightRangle != null)
                rightRangle.setDirection(DcMotor.Direction.REVERSE);
        }
        catch (Exception e)
        {
            unmappedComponents.add("rightRangle");
            DbgLog.logStacktrace(e);
        }

        try
        {
            brush = hardwareMap.dcMotor.get("brush");
        }
        catch (Exception e)
        {
            unmappedComponents.add("brush");
            DbgLog.logStacktrace(e);
        }
        */
        
        // Servos

        try
        {
            servos.put("leftBoxServo", hardwareMap.servo.get("leftBoxServo"));
        }
        catch (Exception e)
        {
            unmappedComponents.add("leftBoxServo");
            DbgLog.logStacktrace(e);
        }

        try
        {
            servos.put("rightBoxServo", hardwareMap.servo.get("rightBoxServo"));
        }
        catch (Exception e)
        {
            unmappedComponents.add("rightBoxServo");
            DbgLog.logStacktrace(e);
        }

        try
        {
            servos.put("leftBrushArmServo", hardwareMap.servo.get("leftBrushArmServo"));
        }
        catch (Exception e)
        {
            unmappedComponents.add("leftBrushArmServo");
            DbgLog.logStacktrace(e);
        }

        try
        {
            servos.put("rightBrushArmServo", hardwareMap.servo.get("rightBrushArmServo"));
        }
        catch (Exception e)
        {
            unmappedComponents.add("rightBrushArmServo");
            DbgLog.logStacktrace(e);
        }

        try
        {
            servos.put("leftClawServo", hardwareMap.servo.get("leftClawServo"));
        }
        catch (Exception e)
        {
            unmappedComponents.add("leftClawServo");
            DbgLog.logStacktrace(e);
        }

        try
        {
            servos.put("rightClawServo", hardwareMap.servo.get("rightClawServo"));
        }
        catch (Exception e)
        {
            unmappedComponents.add("rightClawServo");
            DbgLog.logStacktrace(e);
        }
        /*
        try
        {
            leftBoxServo = hardwareMap.servo.get("leftBoxServo");
        }
        catch (Exception e)
        {
            unmappedComponents.add("leftBoxServo");
            DbgLog.logStacktrace(e);
        }

        try
        {
            rightBoxServo = hardwareMap.servo.get("rightBoxServo");
        }
        catch (Exception e)
        {
            unmappedComponents.add("rightBoxServo");
            DbgLog.logStacktrace(e);
        }

        try
        {
            leftBrushArmServo = hardwareMap.servo.get("leftBrushArmServo");
        }
        catch (Exception e)
        {
            unmappedComponents.add("leftBrushArmServo");
            DbgLog.logStacktrace(e);
        }

        try
        {
            rightBrushArmServo = hardwareMap.servo.get("rightBrushArmServo");
        }
        catch (Exception e)
        {
            unmappedComponents.add("rightBrushArmServo");
            DbgLog.logStacktrace(e);
        }

        try
        {
            leftClawServo = hardwareMap.servo.get("leftClawServo");
        }
        catch (Exception e)
        {
            unmappedComponents.add("leftClawServo");
            DbgLog.logStacktrace(e);
        }

        try
        {
            rightClawServo = hardwareMap.servo.get("rightClawServo");
        }
        catch (Exception e)
        {
            unmappedComponents.add("rightClawServo");
            DbgLog.logStacktrace(e);
        }
*/
        /*
         *
         */
    }

    // Getters
    public HashSet<String> getUnmappedComponents() {return unmappedComponents;}
    public HashSet<String> getOtherErrors() {return otherErrors;}
    public double[] getServoVals()
    {
        double[] servoVals = new double[6];

        servoVals[0] = leftBoxServoPos;
        servoVals[1] = rightBoxServoPos;
        servoVals[2] = leftClawServoPos;
        servoVals[3] = rightClawServoPos;
        servoVals[4] = leftBrushArmServoPos;
        servoVals[5] = rightBrushArmServoPos;
        return servoVals;
    }
    
    // Setters
    
    protected void setMotorPower(String motorName, double power)
    {
        
    }
    
    protected void setServoVal(String servoName, double servoPos)
    {
        
    }

    @Override
    public void start()
    {
        // Set Servos to default positions
        leftBoxServoPos = L_BOX_UP;
        rightBoxServoPos = R_BOX_UP;

        leftClawServoPos = L_CLAW_UP;
        rightClawServoPos = R_CLAW_UP;

        leftBrushArmServoPos = L_BRUSH_BAR_DOWN;
        rightBrushArmServoPos = R_BRUSH_BAR_DOWN;
    }

    @Override
    public void loop()
    {
        // Update Servo Positions (servos must be sent a position, otherwise they will move to default)
        servos.get("leftBoxServo").setPosition(leftBoxServoPos);
        servos.get("rightBoxServo").setPosition(rightBoxServoPos);

        servos.get("leftClawServo").setPosition(leftClawServoPos);
        servos.get("rightClawServo").setPosition(rightClawServoPos);

        servos.get("leftBrushArmServo").setPosition(leftBrushArmServoPos);
        servos.get("rightBrushArmServo").setPosition(rightBrushArmServoPos);
    }

    @Override
    public void stop()
    {

    }
}
