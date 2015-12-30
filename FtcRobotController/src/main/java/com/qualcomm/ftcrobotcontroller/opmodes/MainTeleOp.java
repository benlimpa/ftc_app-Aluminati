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

    DcMotor     leftSpool;
    DcMotor     rightSpool;

    DcMotor     leftRangle;
    DcMotor     rightRangle;

    DcMotor     brush;

    // Servos
    Servo       leftBoxServo;
    Servo       rightBoxServo;

    Servo       leftClawServo;
    Servo       rightClawServo;

    Servo       leftBrushArmServo;
    Servo       rightBrushArmServo;

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
        rightSpool = hardwareMap.dcMotor.get("rightSpool");
        leftSpool = hardwareMap.dcMotor.get("leftSpool");

        leftRangle = hardwareMap.dcMotor.get("leftRangle");
        rightRangle = hardwareMap.dcMotor.get("rightRangle");
        rightRangle.setDirection(DcMotor.Direction.REVERSE);

        leftDrive = hardwareMap.dcMotor.get("leftDrive");
        rightDrive = hardwareMap.dcMotor.get("rightDrive");
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        pimpWheel = hardwareMap.dcMotor.get("pimpWheel");
        pimpWheel.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        pimpWheel.setPower(1);

        brush = hardwareMap.dcMotor.get("brush");

        // Servos

        leftBoxServo = hardwareMap.servo.get("leftBoxServo");
        rightBoxServo = hardwareMap.servo.get("rightBoxServo");

        leftBrushArmServo = hardwareMap.servo.get("leftBrushArmServo");
        rightBrushArmServo = hardwareMap.servo.get("rightBrushArmServo");

        leftClawServo = hardwareMap.servo.get("leftClawServo");
        rightClawServo = hardwareMap.servo.get("rightClawServo");
    }

    @Override
    public void start()
    {

        // Set Servos to default positions
        leftBoxServo.setPosition(0);
        rightBoxServo.setPosition(0.6);
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

        if (gamepad2.dpad_right)
        {
            fieldOrientation = RIGHT;
        }
        else if (gamepad2.dpad_left)
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
            rightSpool.setPower(scaleInput(gamepad2.right_stick_y));
            leftSpool.setPower(scaleInput(gamepad2.left_stick_y)*2/5);

        }
        else
        {
            rightRangle.setPower(-scaleInput(gamepad2.right_stick_y));
            leftRangle.setPower(-scaleInput(gamepad2.left_stick_y));
        }

        // Drive Control
        double[] drivePower = getDrivePower(-scaleInput(gamepad1.left_stick_x), -scaleInput(gamepad1.left_stick_y));
        leftDrive.setPower(drivePower[0]);
        rightDrive.setPower(drivePower[1]);

        // Brush Control
        if (gamepad1.right_trigger > 0)
            brush.setPower(gamepad1.right_trigger);
        else if (gamepad1.right_bumper)
            brush.setPower(-0.3);
        else
            brush.setPower(0);

        // Pimp Wheel Control
        if (Math.abs(drivePower[0] - drivePower[1]) > 0.75)
            pimpWheel.setTargetPosition(degToEncoder(360));
        else
            pimpWheel.setTargetPosition(degToEncoder(0));


        telemetry.addData("Manual Rangle Control: ", manualMode);

        /*
         *  Servos
         */
        if (fieldOrientation == RIGHT)
        {
            if (gamepad2.x)
            {
                rightBoxServo.setPosition(0); // down
                telemetry.addData("Right Box Servo: ", 0);
            }
            else if (gamepad2.y)
            {
                rightBoxServo.setPosition(0.6); // up
                telemetry.addData("Right Box Servo: ", 0.6);
            }
        }
        else if (fieldOrientation == LEFT)
        {
            if (gamepad2.x)
            {
                leftBoxServo.setPosition(0.6); // down
                telemetry.addData("Left Box Servo: ", 0.6);
            }
            else if (gamepad2.y)
            {
                leftBoxServo.setPosition(0); // up
                telemetry.addData("Left Box Servo: ", 0);
            }
        }

        // Claws
        if (gamepad2.right_bumper) // up
        {
            leftClawServo.setPosition(0.1);
            rightClawServo.setPosition(1);
        }
        else if (gamepad2.right_trigger > 0.1) // down
        {
            leftClawServo.setPosition(0.7);
            rightClawServo.setPosition(0.35);
        }

        // Brush Arm
        if (gamepad1.left_bumper) // up
        {
            leftBrushArmServo.setPosition(0.25);
            rightBrushArmServo.setPosition(0.53);
        }
        else if (gamepad1.left_trigger > 0.1) // down
        {
            leftBrushArmServo.setPosition(0.8);
            rightBrushArmServo.setPosition(0.2);
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

    // Convert Degrees to motor encoder steps
    int degToEncoder(int degrees)
    {
        return (int) Math.round((double) degrees * 1450 / 360);
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
