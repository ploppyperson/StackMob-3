package uk.antiperson.stackmob.api.tools;

public interface IUpdateChecker {
    String getLatestVersion();

    String updateString();

    String update();
}
