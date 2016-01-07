package com.qualcomm.ftcrobotcontroller.opmodes;

import java.util.HashSet;

/**
 * Created by BenL on 12/30/15.
 */
public class RobotTelemetry extends RobotHardware
{
    private HashSet<String> unmappedComponents;
    public RobotTelemetry(boolean driveEncoders)
    {
        super(driveEncoders);
        unmappedComponents = getUnmappedComponents();
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

        // Other telemetry Data
    }

}
