package net.amarantha.mediascheduler.device;

import net.amarantha.mediascheduler.midi.MidiCommand;

import javax.sound.midi.ShortMessage;

import static javax.sound.midi.ShortMessage.NOTE_ON;

public enum ArKaosMidiCommand {

    CUE_LIST    (new MidiCommand(NOTE_ON, 1, 50, 0)),
    STOP        (new MidiCommand(ShortMessage.STOP, 0, 0, 0)),
    BRIGHTNESS  (new MidiCommand(NOTE_ON, 1, 50, 0)),
    CONTRAST    (new MidiCommand(NOTE_ON, 1, 51, 0))
    ;

    public final MidiCommand command;

    ArKaosMidiCommand(MidiCommand command) {
        this.command = command;
    }

}
