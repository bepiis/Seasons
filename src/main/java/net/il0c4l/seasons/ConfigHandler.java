package net.il0c4l.seasons;

import net.il0c4l.seasons.exceptions.MissingPropertyException;
import net.il0c4l.seasons.reward.ItemReward;
import net.il0c4l.seasons.reward.Reward;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {


    private FileConfiguration config;
    private static List<Challenge> availableChallenges;
    private List<Tier> tierList;
    private final double totalPoints, pointsPerTier;
    private String prefix;


    public ConfigHandler(final Main plugin){
        config = plugin.getConfig();

        prefix = config.getString("prefix");

        availableChallenges = buildChallengeList();
        tierList = makeTierList();
        totalPoints = config.getDouble("totalpoints");
        pointsPerTier = totalPoints/tierList.size();
    }

    public Challenge buildChallenge(ConfigurationSection section){
        ConfigurationSection challengeSection = section.getConfigurationSection("challenge");
        String activationEvent = challengeSection.getString("activationEvent");
        String conditions = challengeSection.getString("conditions");
        int count = challengeSection.getInt("count");
        String message = challengeSection.getString("message");
        String guiItem = challengeSection.getString("gui_item");

        return new Challenge(activationEvent, conditions, count, message, guiItem);
    }

    public List<Challenge> buildChallengeList(){
        List<Challenge> challengeList = new ArrayList<Challenge>();

        for(String sec : config.getConfigurationSection("Challenges").getKeys(false)){
            ConfigurationSection subSec = config.getConfigurationSection("Challenges." + sec);
            challengeList.add(buildChallenge(subSec));
        }
        return challengeList;
    }

    public boolean checkConditions(Challenge challenge, String command){

        if(command.contains(challenge.getActivationEvent())){
            if(command.contains(challenge.getCondition())){
                return true;
            }
        }
        return false;
    }

    public Challenge getDesiredChallenge(String command){
        for(Challenge chal : availableChallenges){
            if(checkConditions(chal, command)){
                return chal;
            }
        }
        return null;
    }

    public static List<Challenge> getAvailableChallenges(){
        return availableChallenges;
    }

    public List<Tier> getCompletedTiers(int points, int pastPoints){
        List<Tier> completed = new ArrayList<>();
        for(int i=pastPoints/(int)pointsPerTier; i<points/(int)pointsPerTier; i++){
            completed.add(tierList.get(i));
        }
        return completed;
    }

    protected Reward makeTierReward(ConfigurationSection subSec) throws MissingPropertyException {
        Reward reward = null;
        if(!subSec.contains("type"))
            throw new MissingPropertyException(Utils.missingProperty(subSec.getCurrentPath() + ".type"));

        String type = subSec.getString("type");
        if(type.equalsIgnoreCase("item")){

            if(!subSec.contains("amount"))
                throw new MissingPropertyException(Utils.missingProperty(subSec.getCurrentPath() + ".amount"));

            int amount = subSec.getInt("amount");
            if(amount <= 0)
                return null;

            if(!subSec.contains("meta.type"))
                throw new MissingPropertyException(Utils.missingProperty(subSec.getCurrentPath() + ".meta.type"));

            String subType = subSec.getString("meta.type");
            String displayName = "";

            if(subSec.contains("meta.display-name"))
                displayName = subSec.getString("meta.display-name");

            List<String> lore = new ArrayList<>();
            if(subSec.contains("meta.lore"))
                lore = subSec.getStringList("meta.lore");

            List<String> enchants = new ArrayList<>();
            if(subSec.contains("meta.enchantments"))
                enchants = subSec.getStringList("meta.enchantments");

            reward = new ItemReward(Material.matchMaterial(subType), amount, displayName, lore, enchants);
        }
        return reward;
    }

    protected Tier makeTier(ConfigurationSection sec){
        String title = sec.toString();
        if(sec.contains("title"))
            title = sec.getString("title");

        String guiItem = "STONE";
        if(sec.contains("guiItem"))
            guiItem = sec.getString("guiItem");

        List<String> lore = new ArrayList<>();
        if(sec.contains("lore"))
            lore = sec.getStringList("lore");

        List<String> messages = new ArrayList<>();
        if(sec.contains("messages"))
            messages = sec.getStringList("messages");

        List<Reward> rewardList = new ArrayList<>();
        ConfigurationSection rSec = sec.getConfigurationSection("rewards");
        rSec.getKeys(false).forEach(it -> {
            ConfigurationSection subSubSec = rSec.getConfigurationSection(it);
            try{
                rewardList.add(makeTierReward(subSubSec));
            } catch(MissingPropertyException e){
                e.printStackTrace();
            }
        });
        return new Tier(title, lore, guiItem, rewardList, messages);
    }

    public List<Tier> makeTierList(){
        List<Tier> tierList = new ArrayList<>();
        config.getConfigurationSection("Tiers").getKeys(false).forEach(it -> {
            ConfigurationSection sec = config.getConfigurationSection("Tiers." + it);
            tierList.add(makeTier(sec));
        });
        return tierList;
    }

    public double getPointsPerTier(){
        return pointsPerTier;
    }

    public double getTotalPoints(){
        return totalPoints;
    }

    public List<Tier> getTierList(){
        return tierList;
    }

    public String getPrefix(){
        return prefix;
    }

}
