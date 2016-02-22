package net.amarantha.mediascheduler.devices;

import com.google.inject.Singleton;

import javax.sound.midi.*;

@Singleton
public class Midi {

    private MidiDevice midiDevice;

    public Midi() {}

    public void openDevice() {
        openDevice(System.getenv("MIDIDEVICE"));
    }

    public void openDevice(String name) {
        try {
            midiDevice = getMidiDevice(name);
            midiDevice.open();
        } catch (MidiUnavailableException e) {
            System.err.println("Could not open MIDI device '" + name + "'\n\t" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void closeDevice() {
        if ( midiDevice!=null ) {
            midiDevice.close();
        }
    }

    public void send(int command, int channel, int data1, int data2) {
        if ( midiDevice!=null ) {
            try {
                Receiver receiver = midiDevice.getReceiver();
                ShortMessage message = new ShortMessage();
                message.setMessage(command, channel, data1, data2);
                receiver.send(message, -1);
            } catch (InvalidMidiDataException e) {
                System.err.println("Invalid MIDI Data:\n" + e.getMessage());
            } catch (MidiUnavailableException e) {
                System.err.println("MIDI Device Unavailable:\n" + e.getMessage());
            }
        }
    }

    private MidiDevice getMidiDevice(String name) throws MidiUnavailableException {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : infos) {
                MidiDevice device = MidiSystem.getMidiDevice(info);
                try {
                    if (device.getReceiver() != null && info.getDescription().contains(name)) {
                        return device;
                    }
                } catch ( MidiUnavailableException ignored ) {}
        }
        throw new MidiUnavailableException("MIDI Device '" + name + "' not found");
    }

}
