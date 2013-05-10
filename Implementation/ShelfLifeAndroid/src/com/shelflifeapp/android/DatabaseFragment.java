package com.shelflifeapp.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.shelflife.android.models.Food;
import com.shelflifeapp.views.FoodListItem;

public class DatabaseFragment extends ListFragment
{
	private ArrayList<Food> foodList;
	private Context mContext;
	
	  @Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);

	    this.getListView().setDividerHeight(0);
	    this.getListView().setVerticalScrollBarEnabled(false);
	    
	    mContext = getActivity();
	    
	    foodList = new ArrayList<Food>();
	    foodList.add(new Food("Apple", R.drawable.icon_fruit));
	    foodList.add(new Food("Orange", R.drawable.icon_fruit));
	    foodList.add(new Food("Pear", R.drawable.icon_fruit));
	    foodList.add(new Food("Grape", R.drawable.icon_fruit));
	    foodList.add(new Food("Tangerine", R.drawable.icon_fruit));
	    foodList.add(new Food("Raisin", R.drawable.icon_fruit));
	    foodList.add(new Food("Blueberry", R.drawable.icon_fruit));
	    foodList.add(new Food("Raspberry", R.drawable.icon_fruit));
	    foodList.add(new Food("Banana", R.drawable.icon_fruit));
	    setListAdapter(new ShelfLifeDatabaseAdapter(mContext, foodList));
	  }

	  @Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
	    // Do something with the data

	  }
	  
	  public class ShelfLifeDatabaseAdapter extends BaseAdapter
	  {
		  private List<Food> foodList;
		  private Context mContext;
		  
	     public ShelfLifeDatabaseAdapter(Context context, List<Food> foodList) 
		 {		
	 		this.foodList = foodList; 
	 		this.mContext = context;
  		 }
	     
	     @Override
	     public View getView(int position, View convertView, ViewGroup parent) 
	     {
	    	 FoodListItem rowView = (FoodListItem)convertView;
	    	 Food food = foodList.get(position);
	    	 
	    	 if (rowView == null)
	    	 {
	    		 rowView = new FoodListItem(mContext, foodList.get(position));
	    	 }
	    	 else
	    	 {
	    		 rowView = (FoodListItem) convertView;
	    	 }
	    	 
	    	 rowView.setFood(food);
	    	 
	    	 return rowView;
	     }

		@Override
		public int getCount() {
			return foodList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return foodList.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
	  }
}
