package net.amarantha.mediascheduler.midi;

public interface Midi {

    void openDevice();

    void openDevice(String name);

    void closeDevice();

    void send(int command, int channel, int data1, int data2);

    void send(MidiCommand command, int data2);

}
