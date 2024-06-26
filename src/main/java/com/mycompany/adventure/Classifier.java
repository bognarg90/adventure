package com.mycompany.adventure;

import com.mycompany.adventure.Entities.Good.Adventurer;
import com.mycompany.adventure.Entities.Good.Blacksmith;
import com.mycompany.adventure.Entities.Good.FoodVendor;

import java.util.ArrayList;

public class Classifier 
{
   public static String Weapon = "Weapon";
    public static String Tool = "Tool";
    public static String Food = "Food";
    public static String Utility = "Utility";
    public static String Chest = "TreasureChest";

    public static String Green_Slime = "Green_Slime";
    public static String Orange_Slime = "Orange_Slime";
    public static String Purple_Slime = "Purple_Slime";

    public static String Goblin = "Goblin";
    public static String Skeleton = "Skeleton";

    public static String Fegyvermester = "Fegyvermester";
    public static String Villager = "Villager";
    public static String Kalandor = "Kalandor";
    public static String Boltos = "Boltos";

    public static ArrayList<String> marketplaceNPCs = new ArrayList<String>() 
    {{
        add(Fegyvermester);
        add(Kalandor);
        add(Boltos);
    }}; 
}
