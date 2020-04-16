package net.il0c4l.seasons;

public class Challenge {

    private String activationEvent, condition, message;
    private int count;

    public static final String[] VALID_REWARD_TYPES = {"ITEM", "MONEY"};
    public static final String[] VALID_CHALLENGE_TYPES = {"EntityDeathChallengeEvent"};

    public Challenge(String activationEvent, String condition, int count, String message, String guiItem){
        this.activationEvent = activationEvent;
        this.condition = condition;
        this.count = count;
        this.message = message;
    }

    public boolean isRewardType(String rewardType){
        int check = 0;
        for(int i=0; i<VALID_REWARD_TYPES.length; i++){
            if(rewardType.equals(VALID_REWARD_TYPES[i])){
                check++;
            }
        }
        if(check != 1){
            return false;
        } return true;
    }

    public boolean isChallengeType(String challengeType){
        int check = 0;
        for(int i = 0; i< VALID_CHALLENGE_TYPES.length; i++){
            if(challengeType.equals(VALID_CHALLENGE_TYPES[i])) {
                check++;
            }
        }
        if(check != 1){
            return false;
        } return true;
    }

    public String getActivationEvent(){
        return activationEvent;
    }

    public String getCondition(){
        return condition;
    }

    public int getCount(){ return count; }

    public String getCommand(){ return activationEvent + "." + condition; }

    public String getMessage(){ return message; }
}
