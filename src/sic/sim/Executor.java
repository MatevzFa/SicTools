package sic.sim;

import sic.sim.vm.Machine;

import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author jure
 */
// TODO: rename class
public class Executor {

    public final Machine machine;
    private Timer timer;
    private int speedkHz = 10;
    public final Breakpoints breakpoints;
    public ActionListener onBreakpoint;

    public Executor(Machine machine) {
        this.machine = machine;
        this.breakpoints = new Breakpoints();
    }

    public Machine getMachine() {
        return machine;
    }

    public int getSpeed() {
        return speedkHz;
    }

    public void setSpeed(int kHz) {
        if (kHz > 10000) kHz = 10000;
        speedkHz = kHz;
    }

    public void start() {
        if (timer != null) return;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < speedkHz; i++) {
                    machine.execute();
                    if (breakpoints.has(machine.registers.getPC())) {
                        stop();
                        if (onBreakpoint != null) onBreakpoint.actionPerformed(null);
                        break;
                    }
                }
            }
        }, 0, 1);
    }

    public void stop() {
        if (timer == null) return;
        timer.cancel();
        timer = null;
    }

    public void step() {
        if (timer == null) machine.execute();
    }

    public boolean isRunning() {
        return timer != null;
    }

}
