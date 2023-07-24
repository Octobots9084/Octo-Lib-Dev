package frc.robot.Commands.arms;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Libraries.Util.MathUtil;
import frc.robot.Subsystems.arm.ArmExtension;
import frc.robot.Subsystems.arm.ArmPivot;
import frc.robot.Subsystems.arm.ArmPositions;
import frc.robot.Subsystems.arm.ArmRollers;
import frc.robot.Subsystems.arm.ArmSpeeds;
import frc.robot.Subsystems.arm.ArmWrist;
import frc.robot.Subsystems.hippo.HippoPositions;
import frc.robot.Subsystems.hippo.HippoRollers;
import frc.robot.Subsystems.hippo.HippoWrist;

public class CollectFloor extends CommandBase{

    private ArmPivot armPivot;
    private ArmExtension armExtension;
    private ArmWrist armWrist;
    private ArmRollers armRollers;
    private HippoWrist hippoWrist;
    private HippoRollers hippoRollers;

    public CollectFloor() {
        //Setup the subsystems. We may want to release the hippo here if a neccessary circumstance can be hypothesized.
        armPivot = ArmPivot.getInstance();
        armExtension = ArmExtension.getInstance();
        armWrist = ArmWrist.getInstance();
        armRollers = ArmRollers.getInstance();
        hippoWrist = HippoWrist.getInstance();
        hippoRollers = HippoRollers.getInstance();
        addRequirements(armPivot, armExtension, armWrist, armRollers, hippoWrist, hippoRollers);
        //Initialize flag to zero. This will increment our sequence tracking in the switch case as the movement progresses.
        flag = 0;
    }

    private int flag;
    private double start;

    @Override
    public void initialize() { 
        //Start by stowing the hippo, and beginning to raise the arm. RAW, the hippo is not neccessary but it is courtious to our teammates. Hold the cone and begin moving.
        hippoWrist.setAngle(HippoPositions.STOW);
        armRollers.setSpeed(ArmSpeeds.COLLECT);
        armPivot.setAngle(ArmPositions.INTAKE_GROUND);
        start = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        switch(flag) {
        case 0: //At a certain point of acceptable height, we allow the extension and wrist to begin moving once the pivot is done.
            if (0.65 > Timer.getFPGATimestamp() - start || MathUtil.isWithinTolerance(armPivot.getAngle(), ArmPositions.CUBE_PLACE_MID.armAngle, 0.1)) {
                armExtension.setPosition(ArmPositions.INTAKE_GROUND, false);
                armWrist.setAngle(ArmPositions.INTAKE_GROUND);
                flag = 1;
            }
        }
    }

    @Override
    public boolean isFinished() {
        //After everything is stowed we're done here.
        return flag == 1;
    }
}