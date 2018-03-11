package com.maudio;

import java.util.UUID;

import com.bitwig.extension.api.PlatformType;
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;

public class Oxygen49ExtensionDefinition extends ControllerExtensionDefinition
{
    private static final UUID DRIVER_ID = UUID.fromString("71c66db9-7e85-4f65-82e8-37ab798cd2ab");

    public Oxygen49ExtensionDefinition()
    {
    }

    @Override
    public String getName()
    {
        return "Oxygen49";
    }

    @Override
    public String getAuthor()
    {
        return "synthor";
    }

    @Override
    public String getVersion()
    {
        return "0.1";
    }

    @Override
    public UUID getId()
    {
        return DRIVER_ID;
    }

    @Override
    public String getHardwareVendor()
    {
        return "M-Audio";
    }

    @Override
    public String getHardwareModel()
    {
        return "Oxygen 49 MKII";
    }

    @Override
    public int getRequiredAPIVersion()
    {
        return 6;
    }

    @Override
    public int getNumMidiInPorts()
    {
        return 1;
    }

    @Override
    public int getNumMidiOutPorts()
    {
        return 0;
    }

    @Override
    public void listAutoDetectionMidiPortNames(final AutoDetectionMidiPortNamesList list, final PlatformType platformType)
    {
        // list.add(new String[]{"Input Port 0", "Input Port -1"}, new String[]{"Output Port 0"});
        
        switch( platformType )
        {
            case WINDOWS:
                // No Windows here. Please add if you want support.
                break;
            case MAC:
                // No Mac here. Please add if you want support.
                break;
            case LINUX:
                list.add(new String[]{"Oxygen 49"}, new String[]{"Oxygen 49"});
                list.add(new String[]{"Oxygen 49 MIDI 1"}, new String[]{"Oxygen 49 MIDI 1"});
                break;
        }
    }

    @Override
    public Oxygen49Extension createInstance(final ControllerHost host)
    {
       return new Oxygen49Extension(this, host);
    }
}