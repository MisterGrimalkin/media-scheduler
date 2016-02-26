package net.amarantha.mediascheduler.device;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.mediascheduler.entity.CueList;
import net.amarantha.mediascheduler.midi.Midi;

import static javax.sound.midi.ShortMessage.NOTE_OFF;
import static javax.sound.midi.ShortMessage.NOTE_ON;
import static net.amarantha.mediascheduler.device.ArKaosMidiCommand.*;

@Singleton
public class ArKaos {

    private CueList currentCueList;

    @Inject
    private Midi midi;

    public ArKaos() {
    }

    public void startup() {
        midi.openDevice();
        stopCueList();
    }

    public void shutdown() {
        stopCueList();
        midi.closeDevice();
    }

    public void startCueList(CueList cueList) {
        currentCueList = cueList;
        midi.send(CUE_LIST.command, cueList.getNumber());
    }

    public void stopCueList() {
        currentCueList = null;
        midi.send(CUE_LIST.command, 0);
    }

    public CueList getCurrentCueList() {
        return currentCueList;
    }

    public void setBrightness(int brightness) {
        midi.send(BRIGHTNESS.command, brightness);
    }

    public void setContrast(int contrast) {
        midi.send(CONTRAST.command, contrast);
    }

    public void testMidi() {
        System.out.println("Running MIDI Test....");
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
        System.out.println("MIDI Test Complete");
    }

}