package org.utils.objects;

public class Game {
    private Team team1;
    private Team team2;
    private boolean gameLock = false;

    public Team getTeam1() {
        return team1;
    }

    public boolean getGameLock() {
        return gameLock;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam1(Team team1) {
        if(team1 != null){
            team1.setTeamId("TimeA");

        }
        this.team1 = team1;
    }

    public void setTeam2(Team team2) {
        if(team2 != null){
            team2.setTeamId("TimeB");
        }

        this.team2 = team2;
    }

    public void setGameLock(Boolean gameLock) {
        this.gameLock = gameLock;
    }

    public boolean verifyTeamsOpenInGame() {
        return team1 != null && team2 != null;
    }

    public void lockGame() {
        gameLock = true;
    }

    public boolean isGameLock() {
        return gameLock;
    }

    public void setGameLock(boolean gameLock) {
        this.gameLock = gameLock;
    }

}
