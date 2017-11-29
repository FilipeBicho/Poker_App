package com.filipebicho.poker;

/**
 * Created by Flip on 04/11/2017.
 */

class MenuOption {

    // Store the menu icon
    private final int menuIcon;

    // Store the menu name
    private final String menuName;

    // Store the menu background
    private final int menuBackground;

    // Constructor to receive the menu elements
    MenuOption(int menuIcon, String menuName, int menuBackground)
    {
        this.menuIcon = menuIcon;
        this.menuName = menuName;
        this.menuBackground = menuBackground;
    }

    // Define the getters to get menu elements outside of this class
    public int getMenuIcon() {
        return menuIcon;
    }

    public String getMenuName() {
        return menuName;
    }

    public int getMenuBackground() {
        return menuBackground;
    }




}
