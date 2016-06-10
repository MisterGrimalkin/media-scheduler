package net.amarantha.scheduler.cue;

import com.google.inject.Inject;
import net.amarantha.scheduler.midi.MidiService;
import net.amarantha.scheduler.midi.MidiCommand;

public class MidiCue extends Cue {

    @Inject private MidiService midi;

    private MidiCommand command;

    @Override
    public void start() {
        midi.send(command);
    }

    @Override
    public void stop() {}

    public void setCommand(MidiCommand command) {
        this.command = command;
    }

    public MidiCommand getCommand() {
        return command;
    }

}
