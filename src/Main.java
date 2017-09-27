import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String site = "https://www.myscore.ru";
        List<Country> countries = new ArrayList<>();
        countries = getCountry(site);//поместили все страны
        //создаем папки с названием каждой страны
        File mainFolder;
        mainFolder = new File("countries");
        delete(mainFolder);
        System.out.println("Удалено");
        mainFolder = createDirectory("countries");

        createFoldersByCountryName(countries,"country","countries");

        //добавляем каждой стране лиги
        addLigs(countries);
        //добавление папки с названием лиги внутри папок с названием страны

        createFoldersByCountryName(countries,"ligs","countries");

        //ищем добавляем клубы
        addTeams(countries, site);
        //добавляем команды в лиги
        createFoldersByCountryName(countries,"team","countries");
        System.out.println("end");

    }

    private static void delete(File mainFolder) {
        if(!mainFolder.exists())
            return;
        if(mainFolder.isDirectory()){
            for (File f:mainFolder.listFiles()) {
                delete(f);
            }
        }
        mainFolder.delete();

    }

    private static void addTeams(List<Country> countries, String mainPage) throws IOException {
        String nameLig;
        String ligURL;
        int number = countries.size();
        for (Country c:countries) {
            System.out.println("осталось" + number);
            number--;
            for (Map.Entry<String,String> pair:c.getCompetions().entrySet()) {
                nameLig = pair.getKey();
                ligURL = pair.getValue() + "teams/" ;
                Document teamPage = Jsoup.connect(ligURL).get();
                Elements teamLIS = teamPage.getElementsByAttributeValueContaining("id","participant_");
                teamLIS.forEach(teamLI->{
                    Element a = teamLI.child(0).child(1);
                    String teamName = a.text();
                    String teamURL = mainPage + a.attr("href");
                    Team team = new Team(c.getCountryName(),teamName,teamURL);
                    Set<String> teamCompetitions = team.getCompetitions();
                    ArrayList<Team> countriesTeam = c.getTeamList();
                    //надо менять и обдумывать целесообразность проверок
                    teamCompetitions.add(pair.getKey());
                    if(countriesTeam.contains(team)){
                        for (int i = 0; i < countriesTeam.size(); i++) {
                            if(countriesTeam.get(i).equals(team)){
                                countriesTeam.set(i,team);
                                break;
                            }
                        }
                    }
                    else{
                        countriesTeam.add(team);
                    }
                });

            }
        }
    }

    private static void addLigs(List<Country> countries) throws IOException {
        String urlCountry;
        for (Country c:countries) {
            urlCountry = c.getCountryURL();
            getCompetitions(urlCountry,c);
        }
    }

    private static void getCompetitions(String fullURL, Country c) throws IOException {
        Map<String,String> returnCompetitons = new HashMap<>();
        Document countryPage = Jsoup.connect(fullURL).get();
        //берем элемент в котором хранятся соревнования класс selected-country-list
        Elements selectedCountryLists = countryPage.getElementsByClass("selected-country-list");
        selectedCountryLists.forEach(selectedCountryList->{
            Elements competitonsLIS = selectedCountryList.children();
            competitonsLIS.forEach(competitonsLI->{
                if(competitonsLI.hasClass("head") || competitonsLI.hasClass("show-more"))
                    return;

                String urlCompetition = competitonsLI.child(0).attr("href");
                urlCompetition = urlCompetition.substring(0,urlCompetition.length()-1);
                int indexprefix = urlCompetition.lastIndexOf("/");
                urlCompetition = fullURL + urlCompetition.substring(indexprefix,urlCompetition.length()) + "/";
                String nameCompetition = competitonsLI.child(0).text();
                returnCompetitons.put(nameCompetition,urlCompetition);
            });
        });
        c.setCompetions(returnCompetitons);
    }

    private static void createFoldersByCountryName(List<Country> countries, String by, String startupFolder) throws IOException {
        String folderName;
        String fileName;
        String path;
        File folder = null;
        File file = null;
        for (Country c : countries) {
            switch (by) {
                case "country":
                    folderName = c.getCountryName();
                    path = startupFolder + "/" + folderName;
                    folder = createDirectory(path);
                    break;
                case "ligs":
                    //пишем в файл все лиги, создаем папки с названием лиги
                    for (Map.Entry<String, String> pair : c.getCompetions().entrySet()) {
                        String ligName = pair.getKey();
                        String ligURL = pair.getValue();
                        fileName = "ligs.txt";
                        folderName = ligName;
                        path = startupFolder + "/" + c.getCountryName() + "/" + by + "/" + folderName;
                        folder = createDirectory(path);//создали папку
                        file = createFile(startupFolder + "/" + c.getCountryName() + "/" + fileName);//создали файл
                        //записываем лигу в файл
                        writeIntoFile(file,ligName,ligURL);
                    }
                    break;
                case "team":
                    for (Team t:c.getTeamList()) {
                        String countryName = c.getCountryName();
                        path = startupFolder + "/" + countryName + "/ligs/";
                        //проходим по всем чемпионатам в которых участвует команда
                        for (String ligname:t.getCompetitions()) {
                            path = path+ligname + "/";
                            fileName = "team.txt";
                            file = createFile(path + "/" + fileName);
                            path = path + t.getTeamName();
                            folder = createDirectory(path);
                            writeIntoFile(file,t.getTeamName(),t.getCompetitions().toString());
                        }
                    }
                    break;
            }
        }
    }

    private static void writeIntoFile(File file, String appedInfoName, String appedInfoURL) throws IOException {
        Writer writer = new FileWriter(file,true);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        bufferedWriter.write("Название: " + appedInfoName + "          " + "ссылка (название соревнования): " + appedInfoURL);
        bufferedWriter.newLine();
        bufferedWriter.flush();
        bufferedWriter.close();
        writer.close();
    }


    private static File createDirectory(String path) {
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdirs();
        }
        return folder;
    }
    private static File createFile(String path) throws IOException {
        File file = new File(path);
        if(!file.exists())
            file.createNewFile();
        return file;
    }
    private static List<Country> getCountry(String s) throws IOException {
        List<Country> returnsCountries = new ArrayList<>();
        Document startPage = Jsoup.connect(s).get();
        Elements countryMenuParts = startPage.getElementsByAttributeValue("class","menu country-list");
        countryMenuParts.forEach(countryMenuPart->{
            Elements countryLis = countryMenuPart.getElementsByAttributeValueContaining("id","lmenu");
            countryLis.forEach(countryLi ->{
               String url, name;
               Element a = countryLi.child(0);
               url = s + a.attr("href");//ссылку на страну
               url = url.substring(0,url.length()-1);
               name = a.text();//название страны
               Country county = new Country(name,url);
               returnsCountries.add(county);
            });
        });
        return returnsCountries;
    }
}
