package uk.antiperson.stackmob.api.tools;

public enum BukkitVersion {
    UNSUPPORTED(0),
    V1_13(1),
    V1_14(2);

    private int id;
    BukkitVersion(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
