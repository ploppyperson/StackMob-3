package uk.antiperson.stackmob.tools;

public enum StickMode {

    STACK_ONE(1),
    STACK_NEARBY(2),
    UNSTACK_ONE(3),
    UNSTACK_NEARBY(4);

    private final int id;
    StickMode(int id){
        this.id = id;
    }

    public static StickMode getStickMode(int id){
        for(StickMode mode : values()){
            if(mode.id == id){
                return mode;
            }
        }
        throw new IllegalArgumentException("Invalid stick id " + id);
    }
}
