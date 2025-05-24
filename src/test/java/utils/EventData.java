package utils;

import java.util.List;

public class EventData {
    public String homeTeam;
    public String awayTeam;
    public int homeScore;
    public int awayScore;
    public String marketName;
    public List<Double> odds;

    public EventData(String homeTeam, String awayTeam, int homeScore, int awayScore, String marketName,
            List<Double> odds) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.marketName = marketName;
        this.odds = odds;
    }

    @Override
    public String toString() {
        return String.format("%s %d - %d %s | Market: %s | Odds: %s",
                homeTeam, homeScore, awayScore, awayTeam, marketName, odds);
    }
}