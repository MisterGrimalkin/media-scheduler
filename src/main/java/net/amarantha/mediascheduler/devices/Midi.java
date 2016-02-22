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
        midiDevice = getMidiDevice(name);
        if ( midiDevice!=null ) {
            try {
                midiDevice.open();
            } catch (MidiUnavailableException e) {
                System.err.println("Could not open MIDI device '" + name + "'\n" + e.getMessage());
            }
        }
    }

    public void closeDevice() {
        if ( midiDevice!=null ) {
            midiDevice.close();
        }
    }

    public void send(int command, int channel, int data1, int data2) {
        try {
            Receiver receiver = midiDevice.getReceiver();
            ShortMessage message = new ShortMessage();
            message.setMessage(command, channel, data1, data2);
            receiver.send(message, -1);
        } catch (InvalidMidiDataException | MidiUnavailableException e) {
            System.err.println("Invalid MIDI Data!");
        }
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
        System.err.println("MIDI Device '" + name + "' not found");
        return null;
    }

}
