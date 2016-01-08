package com.qualcomm.ftcrobotcontroller.opmodes;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

/**
 * Created by BenL on 12/30/15.
 */
public class RobotTelemetry extends RobotHardware
{
    private final int RESERVED_INDEX = 6;
    private HashSet<String> unmappedComponents;
    private LinkedHashMap<String, String> telemetryData;
    private int telemetryDataIndex;

    public RobotTelemetry(boolean driveEncoders)
    {
        super(driveEncoders);
        unmappedComponents = getUnmappedComponents();
        telemetryData = new LinkedHashMap<String, String>();
        telemetryDataIndex = 0;
    }

    public void updateTelemetry()
    {
        // Create Warning if required components cannot be mapped
        if (!unmappedComponents.isEmpty())
        {
            String finalWarning = "";
            for (String component : unmappedComponents)
            {
                finalWarning += component;
                finalWarning += ", ";
            }
            finalWarning = finalWarning.substring(0, finalWarning.length() - 2);
            telemetry.addData("00: ", "COULD NOT FIND: " + finalWarning);
        }

        // Servo Telemetry
        /*
        0: leftBox
        1: rightBox
        2: leftClaw
        3: rightClaw
        4: leftBrush
        5: rightBrush
        */
        double[] servoVals = getServoVals();
        telemetry.addData("01: ", "Left Box Servo: " + servoVals[0]);
        telemetry.addData("02: ", "Right Box Servo: " + servoVals[1]);
        telemetry.addData("03: ", "Left Claw Servo: " + servoVals[2]);
        telemetry.addData("04: ", "Right Claw Servo: " + servoVals[3]);
        telemetry.addData("05: ", "Left Brush Arm Servo: " + servoVals[4]);
        telemetry.addData("06: ", "Right Brush Arm Servo: " + servoVals[5]);

        // Variable Telemetry
        Set telemetrySet = telemetryData.entrySet();
        Iterator telemetryIterator = telemetrySet.iterator();

        int teleIndex = RESERVED_INDEX + 1;
        while (telemetryIterator.hasNext())
        {
            Map.Entry teleEntry = (Map.Entry) telemetryIterator.next();
            telemetry.addData(teleIndex + ": ", (String) teleEntry.getKey() + (String) teleEntry.getValue());
        }
    }

    public void addTelemetry(int index, String data)
    {
        if (!(index <= RESERVED_INDEX))
            if (index < 10)
                telemetry.addData("0" + index + ": ", data);
            else
                telemetry.addData(index + ": ", data);
    }

    public void addTelemetry(String index, String data)
    {
        telemetryData.put(index, data);
    }
}
