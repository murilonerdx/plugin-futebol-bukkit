package org.utils.objects;

import org.bukkit.entity.Player;

public class Position {
    private PositionCampEnum namePosition;
    private Long qtdGoals;

    public PositionCampEnum getNamePosition() {
        return namePosition;
    }

    public void setNamePosition(PositionCampEnum namePosition) {
        this.namePosition = namePosition;
    }

    public Long getQtdGoals() {
        return qtdGoals;
    }

    public void setQtdGoals(Long qtdGoals) {
        this.qtdGoals = qtdGoals;
    }

    public Position(PositionCampEnum namePosition, Long qtdGoals) {
        this.namePosition = namePosition;
        this.qtdGoals = qtdGoals;
    }

    @Override
    public String toString() {
        return "Position{" +
                "namePosition=" + namePosition +
                ", qtdGoals=" + qtdGoals +
                '}';
    }
}


