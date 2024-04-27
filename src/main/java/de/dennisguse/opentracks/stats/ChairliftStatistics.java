package de.dennisguse.opentracks.stats;

import de.dennisguse.opentracks.data.models.Chairlift;

public class ChairliftStatistics extends Chairlift{

    public ChairliftStatistics(String name, int number, double averageSpeed, String liftType) {
        super(name, number, averageSpeed, liftType);
    }

    public double getChairliftSpeed(){
        return super.getchairliftSpeed();
    }
}