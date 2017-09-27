import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Team {
    private String countryName;
    private String teamName;
    private String teamURL;
    //private List<String> competitions;
    private Set<String> competitions;

    public Team(String countryName, String teamName, String teamURL, Set<String> competitions) {
        this.countryName = countryName;
        this.teamName = teamName;
        this.teamURL = teamURL;
       // this.competitions = competitions;
        this.competitions = competitions;
    }

    public Team(String countryName, String teamName, String teamURL) {
        this.countryName = countryName;
        this.teamName = teamName;
        this.teamURL = teamURL;
        competitions = new HashSet<>();
    }

    public String getCountryName() {

        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamURL() {
        return teamURL;
    }

    public void setTeamURL(String teamURL) {
        this.teamURL = teamURL;
    }

    public Set<String> getCompetitions() {
        return competitions;
    }

    public void setCompetitions(Set<String> competitions) {
        this.competitions = competitions;
    }
}
