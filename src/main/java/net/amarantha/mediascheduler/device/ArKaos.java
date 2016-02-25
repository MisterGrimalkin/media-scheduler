package net.amarantha.mediascheduler.device;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.mediascheduler.midi.Midi;

import static javax.sound.midi.ShortMessage.NOTE_OFF;
import static javax.sound.midi.ShortMessage.NOTE_ON;
import static net.amarantha.mediascheduler.device.ArKaosMidiCommand.*;

@Singleton
public class ArKaos {

    @Inject
    private Midi midi;

    public ArKaos() {
    }

    public void open() {
        midi.openDevice();
    }

    public void close() {
        midi.closeDevice();
    }

    public void startCueList(int id) {
        midi.send(CUE.command, id);
    }

    public void setBrightness(int brightness) {
        midi.send(BRIGHTNESS.command, brightness);
    }

    public void setContrast(int contrast) {
        midi.send(CONTRAST.command, contrast);
    }

    public void testMidi() {
        try {
            for (int i = 30; i < 100; i++) {
                midi.send(NOTE_ON, 1, i, 100);
                midi.send(NOTE_ON, 1, i + 7, 100);
                Thread.sleep(50);
                midi.send(NOTE_OFF, 1, i, 100);
                midi.send(NOTE_OFF, 1, i + 7, 100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}