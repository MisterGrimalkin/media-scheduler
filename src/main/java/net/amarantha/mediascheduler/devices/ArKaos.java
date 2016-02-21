package net.amarantha.mediascheduler.devices;

import com.google.inject.Singleton;

import javax.sound.midi.*;

@Singleton
public class ArKaos {

    private MidiDevice midiDevice;

    public ArKaos() {
        midiDevice = getMidiDevice(System.getenv("MIDIDEVICE"));
        if ( midiDevice==null ) {
            System.err.println("Could not open " + System.getenv("MIDIDEVICE"));
        }
    }

    public void testMidi() {
        try {
            midiDevice.open();

            Receiver receiver = midiDevice.getReceiver();

            for ( int i = 30; i<100; i++ ) {
                ShortMessage message = new ShortMessage();
                message.setMessage(ShortMessage.NOTE_ON, 1, i, 93);
                receiver.send(message, -1);
                Thread.sleep(50);
                message = new ShortMessage();
                message.setMessage(ShortMessage.NOTE_OFF, 1, i, 0);
                receiver.send(message, -1);
            }

            midiDevice.close();

        } catch (MidiUnavailableException | InvalidMidiDataException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }


    public void startCueList(int id) {

    }

    public void setBrightness(int brightness) {

    }

    public void setContrast(int contrast) {

    }

    private MidiDevice getMidiDevice(String name) {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : infos) {
            try {
                MidiDevice device = MidiSystem.getMidiDevice(info);
                if ( device.getReceiver()!=null && info.getDescription().contains(name)) {
                    return device;
                }
            } catch (MidiUnavailableException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

}
