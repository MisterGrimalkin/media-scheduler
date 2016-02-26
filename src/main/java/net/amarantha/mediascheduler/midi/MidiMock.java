package net.amarantha.mediascheduler.midi;

import com.google.inject.Singleton;

@Singleton
public class MidiMock implements Midi {

    private boolean deviceOpen = false;
    private int[] lastCommand = null;

    @Override
    public void openDevice() {
        deviceOpen = true;
    }

    @Override
    public void openDevice(String name) {
        deviceOpen = true;
    }

    @Override
    public void closeDevice() {
        deviceOpen = false;
    }

    @Override
    public void send(int command, int channel, int data1, int data2) {
        lastCommand = new int[4];
        lastCommand[0] = command;
        lastCommand[1] = channel;
        lastCommand[2] = data1;
        lastCommand[3] = data2;
    }

    @Override
    public void send(MidiCommand command, int data2) {
        send(command.command, command.channel, command.data1, data2);
    }

    public int[] getLastCommand() {
        return lastCommand;
    }

    public boolean isDeviceOpen() {
        return deviceOpen;
    }

}
