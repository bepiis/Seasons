package net.il0c4l.seasons;

import net.il0c4l.seasons.exceptions.MissingPropertyException;
import net.il0c4l.seasons.reward.CommandReward;
import net.il0c4l.seasons.reward.ItemReward;
import net.il0c4l.seasons.reward.Reward;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigHandler {

    private FileConfiguration config;
    private Logger logger;
    private List<Challenge> availableChallenges;
    private String prefix, challengeCompletedMessage;

    private static double pointsPerTier;

    public static double totalPoints;
    public static List<Tier> tierList;
    public static final Map<String, ItemStack> progBarMats = new HashMap<>();

    public ConfigHandler(final Main plugin){
        this.logger = plugin.getLogger();
        config = plugin.getConfig();

        totalPoints = config.getDouble("totalpoints");
        challengeCompletedMessage = config.getString("challenge_done_message");
        prefix = config.getString("prefix");
        availableChallenges = buildChallengeList();
        tierList = makeTierList();
        pointsPerTier = totalPoints/tierList.size();

        ConfigurationSection sec = config.getConfigurationSection("gui");
        for(String key : sec.getKeys(false)){
            if(key.equalsIgnoreCase("progressbar")){
                sec.getConfigurationSection("progressbar").getKeys(false).forEach(it ->
                        progBarMats.put(it, new ItemStack(Material.valueOf(sec.getString("progressbar." + it)))));
                continue;
            }
            progBarMats.put(key, new ItemStack(Material.valueOf(sec.getString(key))));
        }
    }

    public boolean checkRequiredElements() throws MissingPropertyException {
        String missing = "Missing path from config: ";

        if(!config.contains("prefix")){
            logger.log(Level.WARNING, missing + "prefix. " + "I will add a default for you...");
            config.set("prefix", "&8[&6&lSeasons&8]");
        }

        final String[] REQUIRED_ELEMENTS = {"totalpoints", "tiers", "gui", "storage"};

        for(String required : REQUIRED_ELEMENTS){
            if(!config.contains(required)) throw new MissingPropertyException(missing + required);
        }


        return false;
    }


    public Challenge buildChallenge(ConfigurationSection section) throws MissingPropertyException{
        final String[] REQUIRED_ELEMENTS = {"activationEvent", "conditions", "count", "weight"};

        //todo test control statements

        String check = checkRequiredElements(section, REQUIRED_ELEMENTS);
        if(!check.equals("")){
            String path = section.getCurrentPath() + check;
            logger.log(Level.SEVERE, INVALID_CONFIG);
            throw new MissingPropertyException(Utils.missingProperty(path));
        }

        int count = section.getInt("count");
        if(count <= 0){
            count = 1;
            section.set("count", 1);
            logger.log(Level.WARNING,
                    "The count specified in: " + section.getCurrentPath() + " must be greater than 0!" +
                            "Defaulting this value to 1...");
        }

        double points = section.getDouble("weight") * totalPoints;
        if(points > totalPoints){
            points = 0.10 * totalPoints;
            section.set("weight", 0.10);
            logger.log(Level.WARNING,
                    "The weight specefied in: " + section.getCurrentPath() + " cannot be greater than 1!" +
                            "Defaulting this value to 0.10...");
        }

        String activationEvent = section.getString("activationEvent");
        String conditions = section.getString("conditions");

        String guiItem = "STONE";
        if(section.contains("gui_item"))
            guiItem = section.getString("gui_item");

        String message = "";
        if(section.contains("message"))
            message = section.getString("message");

        return new Challenge(activationEvent, conditions, count, points, message, guiItem);
    }

    public List<Challenge> buildChallengeList(){
        List<Challenge> challengeList = new ArrayList<>();

        for(String sec : config.getConfigurationSection("Challenges").getKeys(false)){
            ConfigurationSection subSec = config.getConfigurationSection("Challenges." + sec);
            try{
                challengeList.add(buildChallenge(subSec));
            } catch(MissingPropertyException e){
                e.printStackTrace();
            }
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
        return availableChallenges.stream().filter(match -> checkConditions(match, command)).findAny().orElse(null);
    }

    public static List<Tier> getCompletedTiers(int points, int pastPoints){
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

        if(type.equalsIgnoreCase("command")){
            List<String> commands = subSec.getStringList("commands");
            reward = new CommandReward(commands);
        }

        if(type.equalsIgnoreCase("item")){

            final String[] REQUIRED_ELEMENTS = {"amount", "material"};

            //todo check control statements

            String check = checkRequiredElements(subSec, REQUIRED_ELEMENTS);
            if(!check.equals("")){
                String path = subSec.getCurrentPath() + check;
                logger.log(Level.SEVERE, INVALID_CONFIG);
                throw new MissingPropertyException(Utils.missingProperty(path));
            }

            int amount = subSec.getInt("amount");
            if(amount <= 0){
                amount = 1;
                subSec.set("amount", 1);
                logger.log(Level.WARNING,
                        "The amount specified in: " + subSec.getCurrentPath() + " must be greater than 0!" +
                                "Defaulting this value to 1...");
            }

            String material = subSec.getString("material");
            String displayName = "";

            if(subSec.contains("meta.display-name"))
                displayName = subSec.getString("meta.display-name");

            List<String> lore = new ArrayList<>();
            if(subSec.contains("meta.lore"))
                lore = subSec.getStringList("meta.lore");

            List<String> enchants = new ArrayList<>();
            if(subSec.contains("meta.enchantments"))
                enchants = subSec.getStringList("meta.enchantments");

            reward = new ItemReward(Material.matchMaterial(material), amount, displayName, lore, enchants);
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

    private String checkRequiredElements(ConfigurationSection sec, final String[] REQUIRED_ELEMENTS){
        return Arrays.stream(REQUIRED_ELEMENTS).filter(match ->
                sec.getKeys(true).stream().noneMatch(comp ->
                        comp.equalsIgnoreCase(match))).findAny().orElse("");
    }



    public List<Tier> getTierList() { return tierList; }

    public String getPrefix(){
        return prefix;
    }

    public double getTotalPoints(){
        return totalPoints;
    }

    public String getChallengeCompletedMessage(){
        return challengeCompletedMessage;
    }

    private static final String INVALID_CONFIG = "There is something wrong in your configuration file!";

}
