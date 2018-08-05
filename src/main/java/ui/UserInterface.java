package ui;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class UserInterface implements BaseUserInterface {

    private BaseUserInterfacePresenter presenter;
    private Scanner scanner;

    public UserInterface() throws SQLException {
        scanner = new Scanner(System.in);
        presenter = new UserInterfacePresenter(this);
        presenter.onStart();
    }

    private void onRestart() {
        promptForInformation();
        listenForInput();
    }

    @Override
    public void onStart() {
        System.out.println("Welcome to HowDoICraftGW2 crafting tool.");
        System.out.println("This tool allows you to enter an item name and retrieve a list of resources you need");
        System.out.println("as well as a crafting order that allows you to first craft optimally and simply.");
        System.out.println("THIS TOOL DOES NOT SUPPORT LEGENDARY items! Strange crafting quirks make this very hard to support.");
        System.out.println("Luckily, the crafting process for legendary items is very well documented. As such, this tool works great for all other items.");
        promptForInformation();
        listenForInput();
    }

    private void promptForInformation() {
        System.out.println("Please first enter the name of the item exactly as it appears in game.");
        System.out.println("Afterwards, on a separate line, enter the quantity that you would like to craft.");
    }

    private void listenForInput() {
        String itemName = scanner.nextLine();
        int quantity = Integer.parseInt(scanner.nextLine());
        presenter.onRequest(itemName, quantity);
    }

    @Override
    public void onNotify(String message, boolean flag) {
        if(flag) {
            try {
                System.out.write(message.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(message);
        }

    }

    @Override
    public void onResponse(String response) {
        System.out.println(response);
        try {
            Thread.sleep(5000);
            onRestart();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
