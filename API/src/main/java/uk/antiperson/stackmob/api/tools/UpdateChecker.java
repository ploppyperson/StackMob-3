package uk.antiperson.stackmob.api.tools;

public interface UpdateChecker {
    String getLatestVersion();

    String updateString();

    String update();
}
