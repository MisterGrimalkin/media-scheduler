package net.amarantha.mediascheduler.device;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.mediascheduler.midi.Midi;
import net.amarantha.mediascheduler.midi.MidiCommand;
import net.amarantha.mediascheduler.scheduler.CueList;

import static javax.sound.midi.ShortMessage.*;

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
        cueListCommand(cueList).execute(midi);
    }

    public void stopCueList() {
        currentCueList = null;
        stopCommand().execute(midi);
    }

    public CueList getCurrentCueList() {
        return currentCueList;
    }

    public void setBrightness(int brightness) {
        brightnessCommand(brightness).execute(midi);
    }

    public void setContrast(int contrast) {
        contrastCommand(contrast).execute(midi);
    }

    //////////////
    // Commands //
    //////////////

    public MidiCommand cueListCommand(CueList cueList) {
        return new MidiCommand(NOTE_ON, 1, cueList.getNumber(), 100);
    }

    public MidiCommand brightnessCommand(int brightness) {
        return new MidiCommand(NOTE_ON, 1, brightness, 120);
    }

    public MidiCommand contrastCommand(int contrast) {
        return new MidiCommand(NOTE_OFF, 1, contrast, 60);
    }

    public MidiCommand stopCommand() {
        return new MidiCommand() {
            @Override
            public void execute(Midi midi) {
                for (int n = 0; n < 128; n++) {
                    midi.send(NOTE_OFF, 1, n, 0);
                }
            }
        };
    }


    ///////////////
    // Test Mode //
    ///////////////

    public void glissandoFifths() {
        for (int i = 30; i < 100; i++) {
            playFifth(i);
        }
        for (int i = 100; i > 30; i--) {
            playFifth(i);
        }
    }

    private void playFifth(int noteNo) {
        try {
            midi.send(NOTE_ON, 1, noteNo, 100);
            midi.send(NOTE_ON, 1, noteNo + 7, 100);
            Thread.sleep(50);
            midi.send(NOTE_OFF, 1, noteNo, 100);
            midi.send(NOTE_OFF, 1, noteNo + 7, 100);
        } catch (InterruptedException ignored) {}
    }

}