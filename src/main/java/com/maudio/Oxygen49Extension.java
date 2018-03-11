package com.maudio;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.ShortMidiMessageReceivedCallback;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.Transport;
import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.CursorDevice;
import com.bitwig.extension.controller.api.CursorTrack;
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

        // Map CC 20 - 27 to device parameters
        this.transport = host.createTransport();
        this.cursortrack = host.createCursorTrack(3, 0);
        this.cursordevice = this.cursortrack.createCursorDevice();
        this.remotecontrols = this.cursordevice.createCursorRemoteControlsPage(8);

        for( int i = 0 ; i < 8 ; i++ ) {
            Parameter p = this.remotecontrols.getParameter(i);
            p.setIndication(true);
            p.setLabel("P" + (i + 1));
//            p.markInterested();
        }

        // Make the rest freely mappable
        this.usercontrols = host.createUserControls(CC.HIGHEST_CC - CC.LOWEST_CC + 1-8);

        for( int i = CC.LOWEST_CC ; i < CC.HIGHEST_CC ; i++ ) {
            if( !isIndexInDeviceCCRange(i) ) {
                int index = userIndexFromCC(i);
                this.usercontrols.getControl(index).setLabel("CC" + i);
//                this.usercontrols.getControl(index).markInterested();
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
    private Boolean isIndexInDeviceCCRange( int index ) {
        return index >= CC.DEVICE_START_CC && index <= CC.DEVICE_END_CC;
    }
    
    // Returns the user index from the given cc value
    private int userIndexFromCC( int cc ) {
        if( cc > CC.DEVICE_END_CC ) {
            return cc - CC.LOWEST_CC - 8;
        }
        
        return cc - CC.LOWEST_CC;
    }
    
    // MIDI-Callback
    private void onMidi0( ShortMidiMessage msg ) {
        getHost().println("Somehow triggered a midi: " + msg.getStatusByte() + " (getStatusByte)");
        getHost().println("Somehow triggered a midi: " + msg.getData1() + " (getData1)");
        getHost().println("Somehow triggered a midi: " + msg.getData2() + " (getData2)");
        getHost().println("Somehow triggered a midi: " + msg.getChannel() + " (getChannel)");
    }
    
    // SysEx-Callback
    private void onSysex0( final String data ) {
        getHost().println("Somehow triggered a sysex: " + data);
    }
}
