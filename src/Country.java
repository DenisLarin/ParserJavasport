import java.util.*;

public class Country {
    private String countryName;
    private String countryURL;
    Map<String,String> competions;
    ArrayList<Team> teamList;

    @Override
    public String toString() {
        return "Country{" +
                "countryName='" + countryName + '\'' +
                ", countryURL='" + countryURL + '\'' +
                ", competions=" + competions +
                ", teamList=" + teamList +
                '}';
    }

    public ArrayList<Team> getTeamList() {
        return teamList;
    }

    public void setTeamList(ArrayList<Team> teamList) {
        this.teamList = teamList;
    }

    public Country() {
        competions = new HashMap<>();
        countryName = "";
        countryURL = "";
        teamList = new ArrayList<>();
    }

    public Country(String countryName, String countryURL) {
        this.countryName = countryName;
        this.countryURL = countryURL;
        teamList = new ArrayList<>();
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryURL() {
        return countryURL;
    }

    public void setCountryURL(String countryURL) {
        this.countryURL = countryURL;
    }

    public Map<String, String> getCompetions() {
        return competions;
    }

    public void setCompetions(Map<String, String> competions) {
        this.competions = competions;
    }

}
