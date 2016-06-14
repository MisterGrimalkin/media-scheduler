package net.amarantha.scheduler.cue;

import com.google.inject.Inject;
import net.amarantha.scheduler.midi.MidiCommand;
import net.amarantha.scheduler.midi.MidiService;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MidiCue midiCue = (MidiCue) o;

        return command != null ? command.equals(midiCue.command) : midiCue.command == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (command != null ? command.hashCode() : 0);
        return result;
    }
}
