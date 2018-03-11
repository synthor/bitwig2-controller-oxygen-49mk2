package com.maudio;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.ShortMidiMessageReceivedCallback;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.Transport;
import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.CursorDevice;
import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.NoteInput;
import com.bitwig.extension.controller.api.Parameter;
import com.bitwig.extension.controller.api.RemoteControlsPage;
import com.bitwig.extension.controller.api.UserControlBank;

public class Oxygen49Extension extends ControllerExtension
{
    private UserControlBank usercontrols;
    private Transport transport;
    private CursorTrack cursortrack;
    private CursorDevice cursordevice;
    private RemoteControlsPage remotecontrols;
    private NoteInput noteinput;
    
    protected Oxygen49Extension(final Oxygen49ExtensionDefinition definition, final ControllerHost host)
    {
        super(definition, host);
    }

    @Override
    public void init()
    {
        final ControllerHost host = getHost();
        
        // Set up callbacks
        host.getMidiInPort(0).setMidiCallback((ShortMidiMessageReceivedCallback) this::onMidi0);
        host.getMidiInPort(0).setSysexCallback(this::onSysex0);

        // Add a note input
        this.noteinput = host.getMidiInPort(0).createNoteInput("", "??????");
        this.noteinput.setShouldConsumeEvents(false);
        
        // Map CC 20 - 27 to device parameters
        this.transport = host.createTransport();
        this.cursortrack = host.createCursorTrack(3, 0);
        this.cursordevice = this.cursortrack.createCursorDevice();
        this.remotecontrols = this.cursordevice.createCursorRemoteControlsPage(8);

        for( int i = 0 ; i < 8 ; i++ ) {
            Parameter p = this.remotecontrols.getParameter(i);
            p.setIndication(true);
            p.setLabel("P" + (i + 1));
            p.markInterested();
        }

        // Make the rest freely mappable
        this.usercontrols = host.createUserControls(CC.HIGHEST_CC - CC.LOWEST_CC + 1-8);

        for( int i = CC.LOWEST_CC ; i < CC.HIGHEST_CC ; i++ ) {
            if( !isIndexInDeviceCCRange(i) ) {
                int index = userIndexFromCC(i);
                this.usercontrols.getControl(index).setLabel("CC" + i);
                this.usercontrols.getControl(index).markInterested();
            }
        }

        host.showPopupNotification("Oxygen49Extension Initialized");
    }

    @Override
    public void exit()
    {
        getHost().showPopupNotification("Oxygen49Extension Exited");
    }

    @Override
    public void flush()
    {
        // TODO Send any updates you need here
    }
    
    // Returns true if the given index is in the device's cc range or false if not
    private Boolean isIndexInDeviceCCRange( int index )
    {
        return index >= CC.DEVICE_START_CC && index <= CC.DEVICE_END_CC;
    }
    
    // Returns the user index from the given cc value
    private int userIndexFromCC( int cc )
    {
        if( cc > CC.DEVICE_END_CC ) {
            return cc - CC.LOWEST_CC - 8;
        }
        
        return cc - CC.LOWEST_CC;
    }
    
    // MIDI-Callback
    private void onMidi0( ShortMidiMessage msg )
    {
        if( !msg.isControlChange() ) {
            return;
        }
        
        int data1 = msg.getData1();
        int data2 = msg.getData2();
  
        if( isIndexInDeviceCCRange(data1) )
        {
            int index = -1;

            if( data1 == 75) { index = 0; }
            if( data1 == 76) { index = 1; }
            if( data1 == 92) { index = 2; }
            if( data1 == 95) { index = 3; }
            if( data1 == 10) { index = 4; }
            if( data1 == 77) { index = 5; }
            if( data1 == 78) { index = 6; }
            if( data1 == 79) { index = 7; }

            if( index > -1 ) 
            {
                this.remotecontrols.getParameter(index).value().set(data2, 128);
            }
        }
        // Handle transport-buttons and trackselection
        else if( (data1 >= CC.PREV_TRACK && data1 <= CC.RECORD && data1 != 112) && 
                  data2 > 0 )
        {
            switch( data1 ) {
                case CC.PREV_TRACK:
                    this.cursortrack.selectPrevious();
                    break;
                case CC.NEXT_TRACK:
                    this.cursortrack.selectNext();
                    break;
                case CC.LOOP:
                    //this.transport.toggleLoop(); // Deprecated
                    this.transport.isArrangerLoopEnabled().toggle();
                    break;
                case CC.REWIND:
                    this.transport.rewind();
                    break;
                case CC.FORWARD:
                    this.transport.fastForward();
                    break;
                case CC.STOP:
                    this.transport.stop();
                    break;
                case CC.PLAY:
                    this.transport.play();
                    break;
                case CC.RECORD:
                    this.transport.record();
                    break;
            }
        }
        else if( data1 >= CC.LOWEST_CC && data1 <= CC.HIGHEST_CC )
        {
            // Handle slider as trackvolume
            if( data1 == CC.SLIDER )
            {
                this.cursortrack.getVolume().set(data2, 128);
            }
            else
            {
                // Handle CC 02-119
                if( data1 >= CC.LOWEST_CC && data1 <= CC.HIGHEST_CC )
                {
                    int userindex = userIndexFromCC(data1);
                    this.usercontrols.getControl(userindex).value().set(data2, 128);
                }
            }
        }
    }
    
    // SysEx-Callback
    private void onSysex0( final String data ) {}
}