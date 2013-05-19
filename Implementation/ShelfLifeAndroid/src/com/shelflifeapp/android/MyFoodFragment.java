package com.shelflifeapp.android;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.shelflifeapp.models.Category;
import com.shelflifeapp.models.ExpirationData;
import com.shelflifeapp.models.Food;
import com.shelflifeapp.models.MyFood;
import com.shelflifeapp.models.MyFood.State;
import com.shelflifeapp.views.FoodListItem;
import com.shelflifeapp.views.MyFoodListItem;
import com.shelflifeapp.views.ShelfLifeListViewHeader;

public class MyFoodFragment extends ListFragment
{
	private ArrayList<MyFood> myFoodList;
	private Context mContext;
	
	  @Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);

	    mContext = this.getActivity();
	    
	    this.getListView().setDividerHeight(0);
	    this.getListView().setVerticalScrollBarEnabled(false);
	    if (savedInstanceState == null)
	    	this.getListView().addHeaderView(new ShelfLifeListViewHeader(mContext, "My Foods", "Items you have"));
	    
	    mContext = getActivity();

	    myFoodList = new ArrayList<MyFood>();
	    myFoodList.add(new MyFood(0, "Apple", new Category(0, "Fruit", this.getResources().getDrawable(R.drawable.icon_fruit)), 
	    		new ExpirationData(1, 2, 1, 2, 1, 2), "watch out for mold", State.SHELF_OPENED, new Date()));
	    myFoodList.add(new MyFood(0, "Orange", new Category(0, "Fruit", this.getResources().getDrawable(R.drawable.icon_fruit)), 
	    		new ExpirationData(1, 2, 1, 2, 1, 2), "watch out for mold", State.SHELF_OPENED, new Date()));
	    myFoodList.add(new MyFood(0, "Grape", new Category(0, "Fruit", this.getResources().getDrawable(R.drawable.icon_fruit)), 
	    		new ExpirationData(1, 2, 1, 2, 1, 2), "watch out for mold", State.SHELF_OPENED, new Date()));
	    myFoodList.add(new MyFood(0, "Tangerine", new Category(0, "Fruit", this.getResources().getDrawable(R.drawable.icon_fruit)), 
	    		new ExpirationData(1, 2, 1, 2, 1, 2), "watch out for mold", State.SHELF_OPENED, new Date()));
	    myFoodList.add(new MyFood(0, "Melon", new Category(0, "Fruit", this.getResources().getDrawable(R.drawable.icon_fruit)), 
	    		new ExpirationData(1, 2, 1, 2, 1, 2), "watch out for mold", State.SHELF_OPENED, new Date()));
	    myFoodList.add(new MyFood(0, "Pinapple", new Category(0, "Fruit", this.getResources().getDrawable(R.drawable.icon_fruit)), 
	    		new ExpirationData(1, 2, 1, 2, 1, 2), "watch out for mold", State.SHELF_OPENED, new Date()));
	    setListAdapter(new ShelfLifeMyFoodAdapter(mContext, myFoodList));
	  }

	  @Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
	    // Do something with the data

	  }
	  
	  public class ShelfLifeMyFoodAdapter extends BaseAdapter
	  {
		  private List<MyFood> myFoodList;
		  private Context mContext;
		  
	     public ShelfLifeMyFoodAdapter(Context context, List<MyFood> myFoodList) 
		 {		
	 		this.myFoodList = myFoodList; 
	 		this.mContext = context;
  		 }
	     
	     @Override
	     public View getView(int position, View convertView, ViewGroup parent) 
	     {
	    	 MyFoodListItem rowView = (MyFoodListItem)convertView;
	    	 MyFood myFood = myFoodList.get(position);
	    	 
	    	 if (rowView == null)
	    	 {
	    		 rowView = new MyFoodListItem(mContext, myFoodList.get(position));
	    	 }
	    	 else
	    	 {
	    		 rowView = (MyFoodListItem) convertView;
	    	 }
	    	 
	    	 rowView.setMyFood(myFood);
	    	 
	    	 return rowView;
	     }

		@Override
		public int getCount() {
			return myFoodList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return myFoodList.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
	  }
}
