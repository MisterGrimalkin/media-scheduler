package net.amarantha.mediascheduler.midi;

import com.google.inject.Singleton;

import javax.sound.midi.*;

@Singleton
public class MidiServiceImpl implements MidiService {

    private MidiDevice midiDevice;

    @Override
    public void openDevice() {
        openDevice(System.getenv("MIDIDEVICE"));
    }

    @Override
    public void openDevice(String name) {
        try {
            midiDevice = getMidiDevice(name);
            midiDevice.open();
        } catch (MidiUnavailableException e) {
            System.err.println("Could not startup MIDI device '" + name + "': " + e.getMessage());
        }
    }

    @Override
    public void closeDevice() {
        if ( midiDevice!=null ) {
            midiDevice.close();
        }
    }

    @Override
    public void send(MidiCommand midiCommand) {
        if ( midiCommand!=null ) {
            send(midiCommand.getCommand(), midiCommand.getChannel(), midiCommand.getData1(), midiCommand.getData2());
        }
    }

    @Override
    public void send(int command, int channel, int data1, int data2) {
        if ( midiDevice!=null ) {
            try {
                Receiver receiver = midiDevice.getReceiver();
                ShortMessage message = new ShortMessage();
                message.setMessage(command, channel, data1, data2);
                receiver.send(message, -1);
            } catch (InvalidMidiDataException e) {
                System.err.println("Invalid MIDI Data: " + e.getMessage());
            } catch (MidiUnavailableException e) {
                System.err.println("MIDI Device Unavailable: " + e.getMessage());
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
