package frc.robot.Commands.arms;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.hippo.HippoPositions;
import frc.robot.Subsystems.hippo.HippoRollers;
import frc.robot.Subsystems.hippo.HippoWrist;
import frc.robot.Subsystems.light.CmdIDSequences;
import frc.robot.Subsystems.light.Light;

public class HippoYeet extends CommandBase{

    private HippoWrist hippoWrist;
    private HippoRollers hippoRollers;
    private Light light;

    public HippoYeet() {
        
        hippoWrist = HippoWrist.getInstance();
        hippoRollers = HippoRollers.getInstance();
        light = Light.getInstance();
        addRequirements(hippoWrist, hippoRollers);
        //Initialize flag to zero. This will increment our sequence tracking in the switch case as the movement progresses.
        flag = 0;
    }

    private int flag;
    private double start;

    @Override
    public void initialize() { 
        hippoWrist.setAngle(HippoPositions.YEET);
        hippoRollers.setSpeed(HippoPositions.YEET);
        start = Timer.getFPGATimestamp();
        light.command = true;
        light.setAnimation(CmdIDSequences.HippoPlace);
        flag = 0;
    }

    @Override
    public void execute() {
        switch(flag) {
        case 0:
            if (1 < Timer.getFPGATimestamp() - start) {
                flag = 1;
                hippoWrist.setAngle(HippoPositions.STOW);
                hippoRollers.setSpeed(0);
                flag = 1;                
                
            }
            break;
        }
    }

    @Override
    public boolean isFinished() {
        //After everything is stowed we're done here.
        return flag == 1;
    }

    @Override
    public void end(boolean interrupted){
        light.command = false;
    }
}
