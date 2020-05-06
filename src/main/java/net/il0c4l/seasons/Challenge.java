package net.il0c4l.seasons;

public class Challenge {

    private String activationEvent, condition, message, guiItem;
    private int count;
    private double points;

    public static final String[] VALID_REWARD_TYPES = {"ITEM", "MONEY"};
    public static final String[] VALID_CHALLENGE_TYPES = {"EntityDeathChallengeEvent"};

    public Challenge(String activationEvent, String condition, int count, double points, String message, String guiItem){
        this.activationEvent = activationEvent;
        this.condition = condition;
        this.count = count;
        this.message = message;
        this.guiItem = guiItem;
        this.points = points;
    }

    public String getActivationEvent(){
        return activationEvent;
    }

    public String getCondition(){
        return condition;
    }

    public int getCount(){ return count; }

    public double getPoints(){ return points; }

    public String getCommand(){ return activationEvent + "." + condition; }

    public String getMessage(){ return message; }
}
