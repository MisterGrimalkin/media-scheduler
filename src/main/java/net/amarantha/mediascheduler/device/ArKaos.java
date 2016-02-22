package net.amarantha.mediascheduler.device;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.mediascheduler.midi.Midi;

import static javax.sound.midi.ShortMessage.NOTE_OFF;
import static javax.sound.midi.ShortMessage.NOTE_ON;

@Singleton
public class ArKaos {

    @Inject private Midi midi;

    public ArKaos() {}

    public void open() {
        midi.openDevice();
    }

    public void close() {
        midi.closeDevice();
    }

    public void startCueList(int id) {
        int[] command = COMMANDS[CUE];
        midi.send(command[0], command[1], command[2], id);
    }

    public void setBrightness(int brightness) {
        int[] command = COMMANDS[BRIGHTNESS];
        midi.send(command[0], command[1], command[2], brightness);
    }

    public void setContrast(int contrast) {
        int[] command = COMMANDS[CONTRAST];
        midi.send(command[0], command[1], command[2], contrast);
    }

    public void testMidi() {
        try {
            for ( int i=30; i<100; i++ ) {
                midi.send(NOTE_ON, 1, i, 100);
                midi.send(NOTE_ON, 1, i+7, 100);
                Thread.sleep(50);
                midi.send(NOTE_OFF, 1, i, 100);
                midi.send(NOTE_OFF, 1, i+7, 100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    ////////////////////////////////
    // MIDI Command Configuration //
    ////////////////////////////////

    static final int CUE = 0;
    static final int BRIGHTNESS = 1;
    static final int CONTRAST = 2;

    static final int[][] COMMANDS =
            {
                    { NOTE_ON, 1, 50 },
                    { NOTE_ON, 1, 51 },
                    { NOTE_ON, 1, 52 },
            };

}
