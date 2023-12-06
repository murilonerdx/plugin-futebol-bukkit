package org.utils.objects;

public enum PositionCampGetLocation {
    GOLEIRO_TA(new LocationPosition(179.136, 66.00,-157.669)),
    ZAGUEIRO_TA(new LocationPosition(171.913, 66.00, -157.556)),
    ATACANTE_TA(new LocationPosition(161.117, 66.00, -157.488)),
    TECNICO_TA(new LocationPosition()),

    GOLEIRO_TB(new LocationPosition()),
    ZAGUEIRO_TB(new LocationPosition()),
    ATACANTE_TB(new LocationPosition(161.117, 66.00, -157.488)),
    TECNICO_TB(new LocationPosition());

    final LocationPosition position;

    PositionCampGetLocation(LocationPosition position){
        this.position = position;
    }


    public LocationPosition getPosition() {
        return position;
    }
}

