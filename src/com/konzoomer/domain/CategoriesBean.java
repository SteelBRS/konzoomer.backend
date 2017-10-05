package com.konzoomer.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Torben Vesterager
 * Date: 20-11-2010
 * Time: 13:52:22
 */
public class CategoriesBean {

    public static final Category[] CATEGORIES = new Category[]{
            new Category((byte) 0, (byte) 0, "cat_all"),

            new Category((byte) 1, (byte) 0, "cat_bakery"),
            new Category((byte) 2, (byte) 1, "cat_rye_bread"),
            new Category((byte) 3, (byte) 1, "cat_bread_n_buns"),
            new Category((byte) 4, (byte) 1, "cat_other"),

            new Category((byte) 5, (byte) 0, "cat_dairy"),
            new Category((byte) 6, (byte) 5, "cat_milk_n_yoghurt"),
            new Category((byte) 7, (byte) 5, "cat_cheese"),
            new Category((byte) 8, (byte) 5, "cat_eggs"),
            new Category((byte) 9, (byte) 5, "cat_butter_n_fat"),
            new Category((byte) 10, (byte) 5, "cat_other"),

            new Category((byte) 11, (byte) 0, "cat_meat_fish_n_poultry"),
            new Category((byte) 12, (byte) 11, "cat_beef"),
            new Category((byte) 13, (byte) 11, "cat_pork"),
            new Category((byte) 14, (byte) 11, "cat_poultry"),
            new Category((byte) 15, (byte) 11, "cat_sausages"),
            new Category((byte) 16, (byte) 11, "cat_fish_n_seafood"),
            new Category((byte) 17, (byte) 11, "cat_other"),

            new Category((byte) 18, (byte) 0, "cat_fruits_n_vegetables"),
            new Category((byte) 19, (byte) 18, "cat_fruits"),
            new Category((byte) 20, (byte) 18, "cat_vegetables"),
            new Category((byte) 21, (byte) 18, "cat_dried_fruit_n_nuts"),
            new Category((byte) 22, (byte) 18, "cat_other"),

            new Category((byte) 23, (byte) 0, "cat_cooked_meats_n_salads"),
            new Category((byte) 24, (byte) 23, "cat_salads"),
            new Category((byte) 25, (byte) 23, "cat_cooked_meats_sliced"),
            new Category((byte) 26, (byte) 23, "cat_cooked_meats_whole_pieces"),
            new Category((byte) 27, (byte) 23, "cat_pate"),
            new Category((byte) 28, (byte) 23, "cat_other"),

            new Category((byte) 29, (byte) 0, "cat_frozen"),
            new Category((byte) 30, (byte) 29, "cat_bread"),
            new Category((byte) 31, (byte) 29, "cat_meals_n_pizza"),
            new Category((byte) 32, (byte) 29, "cat_vegetables_n_french_fries"),
            new Category((byte) 33, (byte) 29, "cat_meat_fish_n_poultry"),
            new Category((byte) 34, (byte) 29, "cat_soups"),
            new Category((byte) 35, (byte) 29, "cat_ice_cream_n_desserts"),
            new Category((byte) 36, (byte) 29, "cat_other"),

            new Category((byte) 37, (byte) 0, "cat_processed_food"),
            new Category((byte) 38, (byte) 37, "cat_tinned_food"),
            new Category((byte) 39, (byte) 37, "cat_condiments_n_sauces"),
            new Category((byte) 40, (byte) 37, "cat_sugar_n_flour"),
            new Category((byte) 41, (byte) 37, "cat_cereals"),
            new Category((byte) 42, (byte) 37, "cat_coffee_tea_n_cocoa"),
            new Category((byte) 43, (byte) 37, "cat_other"),

            new Category((byte) 44, (byte) 0, "cat_beverages"),
            new Category((byte) 45, (byte) 44, "cat_juice"),
            new Category((byte) 46, (byte) 44, "cat_water"),
            new Category((byte) 47, (byte) 44, "cat_soft_drinks"),
            new Category((byte) 48, (byte) 44, "cat_beer"),
            new Category((byte) 49, (byte) 44, "cat_wine"),
            new Category((byte) 50, (byte) 44, "cat_spirits"),
            new Category((byte) 51, (byte) 44, "cat_other"),

            new Category((byte) 52, (byte) 0, "cat_candy_n_snacks"),
            new Category((byte) 53, (byte) 52, "cat_candy"),
            new Category((byte) 54, (byte) 52, "cat_snacks_n_dip"),

            new Category((byte) 55, (byte) 0, "cat_health_n_beauty"),
            new Category((byte) 56, (byte) 55, "cat_deodorants_n_roll_on"),
            new Category((byte) 57, (byte) 55, "cat_shaving"),
            new Category((byte) 58, (byte) 55, "cat_haircare"),
            new Category((byte) 59, (byte) 55, "cat_skincare"),
            new Category((byte) 60, (byte) 55, "cat_oral_care"),
            new Category((byte) 61, (byte) 55, "cat_other"),

            new Category((byte) 62, (byte) 0, "cat_household"),
            new Category((byte) 63, (byte) 62, "cat_laundry"),
            new Category((byte) 64, (byte) 62, "cat_dishwashers_n_washing_up"),
            new Category((byte) 65, (byte) 62, "cat_cleaning"),
            new Category((byte) 66, (byte) 62, "cat_paper_n_bags"),
            new Category((byte) 67, (byte) 62, "cat_lightbulbs_n_batteries"),
            new Category((byte) 68, (byte) 62, "cat_other")
    };

    public Category[] getCategories() {
        return CATEGORIES;
    }
}
