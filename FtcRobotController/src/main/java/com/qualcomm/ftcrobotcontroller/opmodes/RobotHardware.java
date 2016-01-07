package com.qualcomm.ftcrobotcontroller.opmodes;

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
    private DcMotor leftDrive;
    private DcMotor rightDrive;

    private DcMotor pimpWheel;

    private DcMotor leftSpool;
    private DcMotor rightSpool;

    private DcMotor leftRangle;
    private DcMotor rightRangle;

    private DcMotor brush;

    // Servos
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
        
        // Servos
        
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

        /*
         *
         */
    }

    public HashSet<String> getUnmappedComponents() {return unmappedComponents;}

    @Override
    public void start()
    {
        // Set Servos to default position

    }

    @Override
    public void loop()
    {


        // Update Servo Positions (servos must be sent a position, otherwise they will move to default)
        leftBoxServo.setPosition(leftBoxServoPos);
        rightBoxServo.setPosition(rightBoxServoPos);

        leftClawServo.setPosition(leftClawServoPos);
        rightClawServo.setPosition(rightClawServoPos);

        leftBrushArmServo.setPosition(leftBrushArmServoPos);
        rightBrushArmServo.setPosition(rightBrushArmServoPos);
    }

    @Override
    public void stop()
    {

    }
}
