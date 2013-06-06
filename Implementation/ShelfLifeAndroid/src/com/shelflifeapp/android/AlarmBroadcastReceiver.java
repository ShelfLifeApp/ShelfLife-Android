package com.shelflifeapp.android;

import java.util.Calendar;

import com.shelflifeapp.database.FoodTable;
import com.shelflifeapp.database.MyFoodTable;
import com.shelflifeapp.models.Category;
import com.shelflifeapp.models.ExpirationData;
import com.shelflifeapp.models.MyFood;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
	 
	 @Override
	 public void onReceive(Context context, Intent intent) {
	         showNotification(context);
	         Log.d("mgrap", "alarm");
	 }
	 
	 public void SetAlarm(Context context)
	    {
	        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
	        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
	        // Notifications sent at 1:00am each day
	        Calendar calendar = Calendar.getInstance();
	        calendar.set(Calendar.HOUR_OF_DAY, 01);
	        calendar.set(Calendar.MINUTE, 00);
	        calendar.set(Calendar.SECOND, 00);
	        
	        // To test notifications, use System.currentTimeMillis() and 10*1000
	        // instead of calendar.getTimeInMillis() and 24*60*60*1000
	        // Alarm repeats every 24 hours 24*60*60*1000
	        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 30*1000 , pi);
	    }
	 
	    public void CancelAlarm(Context context)
	    {
	        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
	        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
	        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	        alarmManager.cancel(sender);
	    }
	    
	    private void showNotification(Context context) {			
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
			boolean notificationsEnabled = pref.getBoolean
					(context.getResources().getString(R.string.KEY_NOTIF_ENABLED), false);
			String rateString = pref.getString(context.getResources().getString(R.string.KEY_NOTIF_RATE), 
					context.getResources().getStringArray(R.array.pref_notif_options)[0]);
			int notificationRate = getRate(rateString, context);
			int numExpiring = getNumExpiring(context, notificationRate);
			NotificationCompat.Builder ncb;
			
			Log.d("mgrap", "Notifications: " + notificationsEnabled);
			Log.d("mgrap", "Rate: " + notificationRate);
			Log.d("mgrap", "Num Expiring: " + numExpiring);
			
			if(numExpiring > 0 && notificationsEnabled){
				Resources resources = context.getResources();
				PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
						new Intent(), 0);
				if(notificationRate == 7){
					ncb = new NotificationCompat.Builder(context)
						.setSmallIcon(R.drawable.icon_shelflife)
				        .setContentTitle(resources.getString(R.string.notification_title))
				        .setContentText(resources.getString(R.string.notification_text_7_days) + numExpiring)
				        .setTicker(resources.getString(R.string.notification_ticker));
				}else if(notificationRate == 1){
					ncb = new NotificationCompat.Builder(context)
					.setSmallIcon(R.drawable.icon_shelflife)
			        .setContentTitle(resources.getString(R.string.notification_title))
			        .setContentText(resources.getString(R.string.notification_text_1_days) + numExpiring)
			        .setTicker(resources.getString(R.string.notification_ticker));
				}else{
					ncb = new NotificationCompat.Builder(context)
					.setSmallIcon(R.drawable.icon_shelflife)
			        .setContentTitle(resources.getString(R.string.notification_title))
			        .setContentText(resources.getString(R.string.notification_text_0_days) + numExpiring)
			        .setTicker(resources.getString(R.string.notification_ticker));
				}
				ncb.setContentIntent(contentIntent);
				ncb.setAutoCancel(true);
				NotificationManager nm =
				    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				nm.notify(1, ncb.getNotification());				
			}

		}
	    
	    private int getRate(String str, Context context){
	    	if(context.getResources().getStringArray(R.array.pref_notif_options)[2].equals(str)){
	    		return 7;
	    	}else if(context.getResources().getStringArray(R.array.pref_notif_options)[1].equals(str)){
	    		return 1;
	    	}else{
	    		return 0;
	    	}
	    }
	    
	    private int getNumExpiring(Context context, int rate){
	    	int numExpiring = 0;
	    	String[] projection = {FoodTable.DATABASE_TABLE_FOOD + "." 
					+ FoodTable.FOOD_KEY_ID, 
					FoodTable.DATABASE_TABLE_FOOD + "." 
					+ FoodTable.FOOD_KEY_NAME,
					FoodTable.FOOD_KEY_CATEGORY,
					FoodTable.FOOD_KEY_SHELF_U,
					FoodTable.FOOD_KEY_SHELF_O,
					FoodTable.FOOD_KEY_FRIDGE_U,
					FoodTable.FOOD_KEY_FRIDGE_O,
					FoodTable.FOOD_KEY_FREEZER_U,
					FoodTable.FOOD_KEY_FREEZER_O,
					FoodTable.FOOD_KEY_TIPS,
					MyFoodTable.DATABASE_TABLE_MYFOOD + "." + MyFoodTable.FOOD_KEY_ID,
					MyFoodTable.DATABASE_TABLE_MYFOOD + "." + MyFoodTable.FOOD_KEY_NAME,
					MyFoodTable.FOOD_KEY_FOODID,
					MyFoodTable.FOOD_KEY_PURCHASED,
					MyFoodTable.FOOD_KEY_OPENED,
					MyFoodTable.FOOD_KEY_STATE,
					MyFoodTable.FOOD_KEY_QUANTITY,
					MyFoodTable.FOOD_KEY_PICTURE,
					MyFoodTable.FOOD_KEY_NOTES};
			Uri uri = Uri.parse("content://com.shelflifeapp.android.provider/myfood_table/all");
			Cursor c = context.getContentResolver().query(uri, projection, null, null, null);			
			
			// Count the number of my foods expiring.
			if(c.moveToFirst()){
				do {
					int catId = c.getInt(FoodTable.FOOD_COL_CATEGORY);
					int shelf_u = c.getInt(FoodTable.FOOD_COL_SHELF_U);
					int shelf_o = c.getInt(FoodTable.FOOD_COL_SHELF_O);
					int fridge_u = c.getInt(FoodTable.FOOD_COL_FRIDGE_U);
					int fridge_o = c.getInt(FoodTable.FOOD_COL_FRIDGE_O);
					int freezer_u = c.getInt(FoodTable.FOOD_COL_FREEZER_U);
					int freezer_o = c.getInt(FoodTable.FOOD_COL_FREEZER_O);
					String tips = c.getString(FoodTable.FOOD_COL_TIPS);
					int id = c.getInt(MyFoodTable.FOOD_COL_ID + FoodTable.FOOD_COL_TIPS + 1);
					String name = c.getString(MyFoodTable.FOOD_COL_NAME + FoodTable.FOOD_COL_TIPS + 1);
					int foodid  = c.getInt(MyFoodTable.FOOD_COL_FOODID + FoodTable.FOOD_COL_TIPS + 1);
					String purchased  = c.getString(MyFoodTable.FOOD_COL_PURCHASED + FoodTable.FOOD_COL_TIPS + 1);
					String opened  = c.getString(MyFoodTable.FOOD_COL_OPENED + FoodTable.FOOD_COL_TIPS + 1);
					String state  = c.getString(MyFoodTable.FOOD_COL_STATE + FoodTable.FOOD_COL_TIPS + 1);
					int quantity  = c.getInt(MyFoodTable.FOOD_COL_QUANTITY + FoodTable.FOOD_COL_TIPS + 1);
					byte[] picture = c.getBlob(MyFoodTable.FOOD_COL_PICTURE + FoodTable.FOOD_COL_TIPS + 1);
					String notes  = c.getString(MyFoodTable.FOOD_COL_NOTES + FoodTable.FOOD_COL_TIPS + 1);
					
					Bitmap bitMap = null;		
					if(picture != null){
						bitMap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
					}
					
					MyFood food = new MyFood(id, name, new Category(), 
							new ExpirationData(shelf_o, shelf_u, fridge_o, fridge_u, 
									freezer_o, freezer_u), 
							tips, state, MyFood.convertStringToDate(purchased), 
							MyFood.convertStringToDate(opened), quantity, notes, bitMap);
					if(food.getExpirationDaysLeft() == rate){
						numExpiring++;
					}
				}while(c.moveToNext());
			}
			return numExpiring;
	    }
}
