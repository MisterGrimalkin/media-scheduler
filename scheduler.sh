#!/usr/bin/env bash

export MIDIDEVICE="USB Uno MIDI Interface"

aconnect "$MIDIDEVICE" "Midi Through" 2>/dev/null
if [ $? -eq 0 ]; then
	java -cp "/home/pi/lightboard/lib/*:/home/pi/scheduler/lib/*:/home/pi/scheduler/target/classes" net.amarantha.mediascheduler.Main $*
	aconnect -d "$MIDIDEVICE" "Midi Through"
else
	echo "Could not open $MIDIDEVICE"
fi
