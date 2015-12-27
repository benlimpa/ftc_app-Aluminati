package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

/**
 * Created by BenL on 10/7/15.
 */
public class MainTeleOp extends OpMode
{
    // Controllers
    DcMotorController               motorControl1;
    DcMotorController               motorControl2;
    DcMotorController               motorControl3;
    DcMotorController               motorControl4;

    ServoController                 servoControl;

    // Motors
    DcMotor     leftDrive;
    DcMotor     rightDrive;

    DcMotor     pimpWheel;

    DcMotor     bottomSpool;
    DcMotor     topSpool;

    DcMotor     leftRangle;
    DcMotor     rightRangle;

    DcMotor     brush;

    // Servos
    Servo       servoTest;
    Servo       leftBoxServo;
    Servo       rightBoxServo;

    // States
    boolean     manualMode;
    boolean     fieldOrientation;

    // Constants
    final boolean RIGHT = true;
    final boolean LEFT = false;

    @Override
    public void init()
    {
        // States
        manualMode = false;
        fieldOrientation = RIGHT;

        // Controllers
        motorControl1 = hardwareMap.dcMotorController.get("MotorControl1");
        motorControl2 = hardwareMap.dcMotorController.get("MotorControl2");
        motorControl3 = hardwareMap.dcMotorController.get("MotorControl3");
        motorControl4 = hardwareMap.dcMotorController.get("MotorControl4");

        servoControl = hardwareMap.servoController.get("ServoControl");

        motorControl1.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
        motorControl2.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
        motorControl3.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
        motorControl4.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);

        // Motors
        topSpool = hardwareMap.dcMotor.get("topSpool");
        bottomSpool = hardwareMap.dcMotor.get("bottomSpool");

        leftRangle = hardwareMap.dcMotor.get("leftRangle");
        rightRangle = hardwareMap.dcMotor.get("rightRangle");
        rightRangle.setDirection(DcMotor.Direction.REVERSE);

        leftDrive = hardwareMap.dcMotor.get("leftDrive");
        rightDrive = hardwareMap.dcMotor.get("rightDrive");
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        pimpWheel = hardwareMap.dcMotor.get("pimpWheel");

        brush = hardwareMap.dcMotor.get("brush");

        // Servos
        servoTest = hardwareMap.servo.get("testServo");
        leftBoxServo = hardwareMap.servo.get("leftBoxServo");
        rightBoxServo = hardwareMap.servo.get("rightBoxServo");
    }

    @Override
    public void loop()
    {
        // Change Modes:
        if (gamepad2.dpad_down)
        {
            manualMode = true;
        }
        else if (gamepad2.dpad_up)
        {
            manualMode = false;
        }

        if (gamepad1.dpad_right)
        {
            fieldOrientation = RIGHT;
        }
        else if (gamepad1.dpad_left)
        {
            fieldOrientation = LEFT;
        }

        if (fieldOrientation == RIGHT)
        {
            telemetry.addData("Field Orientation: ", "right");
        }
        else if (fieldOrientation == LEFT)
        {
            telemetry.addData("Field Orientation: ", "left");
        }


        // Switch Between controlling the rangles and the spools
        if (!manualMode)
        {
            // Rangle Control
            if (gamepad2.a)
            {
                rightRangle.setPower(-0.4);
                leftRangle.setPower(-0.4);
            }
            else if (gamepad2.b)
            {
                rightRangle.setPower(0.4);
                leftRangle.setPower(0.4);
            }
            else
            {
                rightRangle.setPower(0);
                leftRangle.setPower(0);
            }

            // Spool Control
            if (gamepad2.x)
            {
                topSpool.setPower(1);
                bottomSpool.setPower(1);
            }
            else if (gamepad2.y)
            {
                topSpool.setPower(-1);
                bottomSpool.setPower(-1);
            }
            else
            {
                topSpool.setPower(0);
                bottomSpool.setPower(0);
            }
            topSpool.setPower(scaleInput(gamepad2.right_stick_y));
            bottomSpool.setPower(scaleInput(gamepad2.left_stick_y)*2/5);
        } else {
            rightRangle.setPower(-scaleInput(gamepad2.right_stick_y));
            leftRangle.setPower(-scaleInput(gamepad2.left_stick_y));
        }

        double[] drivePower = getDrivePower(-scaleInput(gamepad1.left_stick_x), -scaleInput(gamepad1.left_stick_y));
        leftDrive.setPower(drivePower[0]);
        rightDrive.setPower(drivePower[1]);

        brush.setPower(scaleInput(gamepad1.right_trigger));

        // Pimp Wheel Control
        if (gamepad1.a) {
            pimpWheel.setPower(-0.4);
        } else if (gamepad1.b) {
            pimpWheel.setPower(0.4);
        } else {
            pimpWheel.setPower(0);
        }

        telemetry.addData("Control Mode: ", manualMode);

        // Servos

        if (fieldOrientation == RIGHT)
        {
            if (gamepad1.x)
            {
                rightBoxServo.setPosition(0); // down
                telemetry.addData("Right Box Servo: ", 0);
            }
            else if (gamepad1.y)
            {
                rightBoxServo.setPosition(0.6); // up
                telemetry.addData("Right Box Servo: ", 0.6);
            }
        }
        else if (fieldOrientation == LEFT)
        {
            if (gamepad1.x)
            {
                leftBoxServo.setPosition(0.6); // up
                telemetry.addData("Left Box Servo: ", 0.6);
            }
            else if (gamepad1.y)
            {
                leftBoxServo.setPosition(0); // down
                telemetry.addData("Left Box Servo: ", 0);
            }
        }
    }

    @Override
    public void stop()
    {
        // nothing to stop
    }

    // Takes x and y value of joystick, returns drivePower[leftDrive, rightDrive]
    private double[] getDrivePower(double x, double y)
    {
        double[] drivePower = new double[2];

        /*
         *  The Following algorithm is Kendra's Joystick to Tank Drive Conversion (http://home.kendra.com/mauser/Joystick.html)
         */

        double rightPlusLeft     = (100 - Math.abs(x * 100)) * y + (y * 100);
        double rightMinusLeft    = (100 - Math.abs(y * 100)) * x + (x * 100);

        drivePower[0] = (rightPlusLeft-rightMinusLeft)/200; // left drive power
        drivePower[1] = (rightPlusLeft+rightMinusLeft)/200; // right drive power

        // Make sure that the power is not out of bounds
        for (int index = 0; index <= 1; index++)
        {
            if (Math.abs(drivePower[index]) > 1.0)
            {
                drivePower[index] = 1f;
            }
        }

        return drivePower;
    }

    // Input scale from NXTTeleOp for more accuracy in lower ranges
    double scaleInput(double dVal)
    {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }
}
