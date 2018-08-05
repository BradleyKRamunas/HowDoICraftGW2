package ui;

public interface BaseUserInterface {
    void onStart();
    void onNotify(String message, boolean flag);
    void onResponse(String response);
}
