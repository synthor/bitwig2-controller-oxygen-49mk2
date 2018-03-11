package com.maudio;

/*

    Oxygen 49 Midi CCs

    Track Prev          110
    Track Next          111

    Loop				113
    Prev				114
    Forward             115
    Stop				116
    Play				117
    Record              118

    Slider 1			33
    Slider 2			34
    Slider 3			35
    Slider 4			36
    Slider 5			37
    Slider 6			38
    Slider 7			39
    Slider 8			40
    Slider 9			41

    Slider Button 1     49
    Slider Button 2     50
    Slider Button 3     51
    Slider Button 4     52
    Slider Button 5     53
    Slider Button 6     54
    Slider Button 7     55
    Slider Button 8     56
    Slider Button 9     57

    Knob 1				17
    Knob 2				18
    Knob 3				19
    Knob 4				20
    Knob 5				21
    Knob 6				22
    Knob 7				23
    Knob 8				24

*/

public class CC
{
    static final int LOWEST_CC           = 2;
    static final int HIGHEST_CC          = 119;
    
    static final int DEVICE_START_CC     = 75;
    static final int DEVICE_END_CC       = 103;
    
    static final int PREV_TRACK          = 110;
    static final int NEXT_TRACK          = 111;
    static final int LOOP                = 113;
    static final int REWIND              = 114;
    static final int FORWARD             = 115;
    static final int STOP                = 116;
    static final int PLAY                = 117;
    static final int RECORD              = 118;
    static final int SLIDER              = 74;
}