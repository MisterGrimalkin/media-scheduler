package net.amarantha.scheduler.device;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.scheduler.cue.Cue;
import net.amarantha.scheduler.cue.MidiCue;
import net.amarantha.scheduler.midi.MidiCommand;
import net.amarantha.scheduler.midi.MidiService;
import net.amarantha.scheduler.utility.PropertyManager;

import static javax.sound.midi.ShortMessage.*;

@Singleton
public class ArKaos {

    private Cue currentCue;

    private int brightness;
    private int contrast;

    private int brightnessCC;
    private int contrastCC;

    private PropertyManager props;
    private MidiService midi;

    @Inject
    public ArKaos(PropertyManager props, MidiService midi) {
        this.props = props;
        this.midi = midi;
        brightness = props.getInt("brightness", 64);
        contrast = props.getInt("contrast", 64);
        brightnessCC = props.getInt("brightnessCC", 42);
        contrastCC = props.getInt("contrastCC", 43);
    }

    public void startup() {
        midi.openDevice();
        stopAll();
        brightnessCommand(brightness).send(midi);
        contrastCommand(contrast).send(midi);
    }

    public void shutdown() {
        stopAll();
        midi.closeDevice();
    }

    public void startCue(Cue cue) {
        if ( currentCue !=null ) {
            stopCueCommand(currentCue).send(midi);
        }
        currentCue = cue;
        startCueCommand(cue).send(midi);
    }

    public void stopAll() {
        currentCue = null;
        stopAllCommand().send(midi);
    }

    public Cue getCurrentCue() {
        return currentCue;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
        props.setProperty("brightness", brightness);
        brightnessCommand(brightness).send(midi);
    }

    public void setContrast(int contrast) {
        this.contrast = contrast;
        props.setProperty("contrast", contrast);
        contrastCommand(contrast).send(midi);
    }

    public int getBrightness() {
        return brightness;
    }

    public int getContrast() {
        return contrast;
    }

    //////////////
    // Commands //
    //////////////

    public MidiCommand brightnessCommand(int brightness) {
        return new MidiCommand(CONTROL_CHANGE, 1, brightnessCC, brightness);
    }

    public MidiCommand contrastCommand(int contrast) {
        return new MidiCommand(CONTROL_CHANGE, 1, contrastCC, contrast);
    }

    public MidiCommand startCueCommand(Cue cue) {
        return new MidiCommand(CONTROL_CHANGE, 1, ((MidiCue)cue).getCommand().getData1(), 127);
    }

    public MidiCommand stopCueCommand(Cue cue) {
        return new MidiCommand(CONTROL_CHANGE, 1, ((MidiCue)cue).getCommand().getData1(), 0);
    }

    public MidiCommand stopAllCommand() {
        return new MidiCommand() {
            @Override
            public void send(MidiService midi) {
                for (int n = 1; n < 11; n++) {
                    midi.send(CONTROL_CHANGE, 1, n, 0);
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