package com.shelflifeapp.database;

import com.shelflifeapp.android.R;

public class DbIcons 
{
	/* Category Ids */
	private final static int CAT_BABY = 1;
	private final static int CAT_BAKED_GOODS = 2;
	private final static int CAT_BAKING = 3;
	private final static int CAT_BEVERAGES = 4;
	private final static int CAT_CANDY = 5;
	private final static int CAT_CHEESE = 6;
	private final static int CAT_CONDIMENTS = 7;
	private final static int CAT_DAIRY = 8;
	private final static int CAT_FRUIT = 10;
	private final static int CAT_GRAINS = 11;
	private final static int CAT_PASTA = 12;
	private final static int CAT_POULTRY = 13;
	private final static int CAT_SNACK = 14;
	private final static int CAT_SOUP = 15;
	private final static int CAT_SPICES = 16;
	private final static int CAT_VEGGIES = 17;

	/* Plate Ids */
	private final static int PLATE_BABY = 1;
	private final static int PLATE_BAKED_GOODS = 2;
	private final static int PLATE_BAKING = 3;
	private final static int PLATE_BEVERAGES = 4;
	private final static int PLATE_CANDY = 5;
	private final static int PLATE_CHEESE = 6;
	private final static int PLATE_CONDIMENTS = 7;
	private final static int PLATE_DAIRY = 8;
	private final static int PLATE_FRUIT = 10;
	private final static int PLATE_GRAINS = 11;
	private final static int PLATE_PASTA = 12;
	private final static int PLATE_POULTRY = 13;
	private final static int PLATE_SNACK = 14;
	private final static int PLATE_SOUP = 15;
	private final static int PLATE_SPICES = 16;
	private final static int PLATE_VEGGIES = 17;

	public static int getIcon(int cat)
	{
		switch (cat)
		{
			case CAT_BABY:
				return R.drawable.baby;
			case CAT_BAKED_GOODS:
				return R.drawable.bakedgoods;
			case CAT_BAKING:
				return R.drawable.baking;
			case CAT_BEVERAGES:
				return R.drawable.beverages;
			case CAT_CANDY:
				return R.drawable.candy;
			case CAT_CHEESE:
				return R.drawable.cheese;
			case CAT_CONDIMENTS:
				return R.drawable.condiments;
			case CAT_DAIRY:
				return R.drawable.dairy;
			case CAT_FRUIT:
				return R.drawable.fruit;
			case CAT_GRAINS:
				return R.drawable.grains;
			case CAT_PASTA:
				return R.drawable.pasta;
			case CAT_SNACK:
				return R.drawable.snacks;
			case CAT_SOUP:
				return R.drawable.soup;
			case CAT_SPICES:
				return R.drawable.spices;
			case CAT_VEGGIES:
				return R.drawable.veggies;
		}
		
		return R.drawable.international;
	}
	
	public static int getPlateIcon(int plateIcon)
	{
		switch (plateIcon)
		{
			case PLATE_BABY:
				return R.drawable.baby;
			case PLATE_BAKED_GOODS:
				return R.drawable.bakedgoods;
			case PLATE_BAKING:
				return R.drawable.baking;
			case PLATE_BEVERAGES:
				return R.drawable.beverages;
			case PLATE_CANDY:
				return R.drawable.candy;
			case PLATE_CHEESE:
				return R.drawable.cheese;
			case PLATE_CONDIMENTS:
				return R.drawable.condiments;
			case PLATE_DAIRY:
				return R.drawable.dairy;
			case PLATE_FRUIT:
				return R.drawable.fruit;
			case PLATE_GRAINS:
				return R.drawable.grains;
			case PLATE_PASTA:
				return R.drawable.pasta;
			case PLATE_SNACK:
				return R.drawable.snacks;
			case PLATE_SOUP:
				return R.drawable.soup;
			case PLATE_SPICES:
				return R.drawable.spices;
			case PLATE_VEGGIES:
				return R.drawable.veggies;
		}
		
		return R.drawable.international;
	}
}
