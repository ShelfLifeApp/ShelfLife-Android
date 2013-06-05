package com.shelflifeapp.android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.shelflifeapp.database.MyFoodTable;
import com.shelflifeapp.models.MyFood;
import com.shelflifeapp.views.MyFoodListItem;

public class EditFoodActivity extends SherlockActivity  {

	private final static int SHELF = 0;
	private final static int FRIDGE = 1;
	private final static int FREEZER = 2;

	public final static int ADD = 3;
	public final static int EDIT = 4;
	
	private static final int PICTURE_REQUEST_CODE = 2;

	private MyFood m_myfood;
	private int foodid;
	private int operation;

	private EditText myFoodName;
	private Button purchasedButton;
	private RadioGroup openedBool;
	private TextView openedTitle;
	private Button openedButton;
	private Spinner state;
	private EditText quantity;
	private EditText notes;
	private ImageView pictureView;
	private Bitmap photo;

	DateFormat formatDateTime = DateFormat.getDateInstance();
	Calendar purchaseDate = Calendar.getInstance();
	Calendar openDate = Calendar.getInstance();
	private TextView purchaseDateLabel;
	private TextView openDateLabel;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_food);
	}
}