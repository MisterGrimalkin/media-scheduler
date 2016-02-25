package net.amarantha.mediascheduler.device;

import net.amarantha.mediascheduler.midi.MidiCommand;

import static javax.sound.midi.ShortMessage.NOTE_ON;

public enum ArKaosMidiCommand {

    CUE         (new MidiCommand(NOTE_ON, 1, 50)),
    BRIGHTNESS  (new MidiCommand(NOTE_ON, 1, 51)),
    CONTRAST    (new MidiCommand(NOTE_ON, 1, 52))
    ;

    public final MidiCommand command;

    ArKaosMidiCommand(MidiCommand command) {
        this.command = command;
    }

}
