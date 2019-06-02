package uk.antiperson.stackmob.api.entity.death;

public enum  DeathType {
    KILL_ALL("kill-all"),
    KILL_STEP("kill-step"),
    KILL_STEP_DAMAGE("kill-step-damage");

    private final String type;
    DeathType(String name){
        type = name;
    }

    public String getType() {
        return type;
    }
}
