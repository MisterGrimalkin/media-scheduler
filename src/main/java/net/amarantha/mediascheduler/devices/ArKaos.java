package net.amarantha.mediascheduler.devices;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.sound.midi.*;

@Singleton
public class ArKaos {

    @Inject private Midi midi;

    public ArKaos() {}

    public void start() {
        midi.openDevice();
    }

    public void stop() {
        midi.closeDevice();
    }

    public void testMidi() {
        try {
            for ( int i=30; i<100; i++ ) {
                midi.send(ShortMessage.NOTE_ON, 1, i, 100);
                midi.send(ShortMessage.NOTE_ON, 1, i+7, 100);
                Thread.sleep(50);
                midi.send(ShortMessage.NOTE_OFF, 1, i, 100);
                midi.send(ShortMessage.NOTE_OFF, 1, i+7, 100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void startCueList(int id) {

    }

    public void setBrightness(int brightness) {

    }

    public void setContrast(int contrast) {

    }

}
