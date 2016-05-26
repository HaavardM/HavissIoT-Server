package net.haviss.havissIoT.Communication;

/**
 * Created by Håvard on 25.05.2016.
 */
public enum MQTTQOS {
    ATMOSTONCE((byte)0),
    ATLEASTONCE((byte)1),
    EXACTLYONCE((byte)2);

    private final byte qosLevel;
    private MQTTQOS(byte level) {
        qosLevel = level;
    }
    public byte getValue() {
        return qosLevel;
    }
    public static MQTTQOS fromValue(byte value) {
        switch (value) {
            case 0:
                return ATMOSTONCE;
            case 1:
                return ATLEASTONCE;
            case 2:
                return EXACTLYONCE;
        }
        return null;
    }

}
