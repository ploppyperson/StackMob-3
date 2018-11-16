package uk.antiperson.stackmob.stick;

public enum StickMode {

    STACK_ONE(1),
    STACK_NEARBY(2),
    UNSTACK_ONE(3),
    UNSTACK_NEARBY(4),
    DATA(5);

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
