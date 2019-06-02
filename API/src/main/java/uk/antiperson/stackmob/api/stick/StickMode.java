package uk.antiperson.stackmob.api.stick;

public enum StickMode {

    STACK_ONE(1),
    STACK_NEARBY(2),
    UNSTACK_ONE(3),
    UNSTACK_NEARBY(4),
    SPLIT_ONE(5),
    DATA(6);

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
