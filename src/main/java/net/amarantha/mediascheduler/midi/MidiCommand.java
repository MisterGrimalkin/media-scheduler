package net.amarantha.mediascheduler.midi;

public class MidiCommand {

    public final int command;
    public final int channel;
    public final int data1;

    public MidiCommand(int command, int channel, int data1) {
        this.command = command;
        this.channel = channel;
        this.data1 = data1;
    }

}
