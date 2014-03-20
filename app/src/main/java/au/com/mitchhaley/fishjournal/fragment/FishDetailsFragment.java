package au.com.mitchhaley.fishjournal.fragment;

import android.app.Dialog;


import android.app.FragmentManager;
import android.content.Context;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import au.com.mitchhaley.fishjournal.R;
import au.com.mitchhaley.fishjournal.activity.FishEntryActivity;
import au.com.mitchhaley.fishjournal.contentprovider.FishEntryContentProvider;
import au.com.mitchhaley.fishjournal.contentprovider.TripEntryContentProvider;
import au.com.mitchhaley.fishjournal.db.FishEntryTable;
import au.com.mitchhaley.fishjournal.db.TripEntryTable;
import au.com.mitchhaley.fishjournal.helper.DateTimeHelper;
import au.com.mitchhaley.fishjournal.picker.DateTimePicker;

/**
 * Created by mitch on 18/10/13.
 */
public class FishDetailsFragment extends Fragment implements TripListDialogFragment.SuperListener {
    
    private EditText mSize;
    
    private EditText mWeight;

    private EditText mDateTime;

    private DateTimePicker custom;

    private Date dateTime;

    private long selectedTripId = -1;


    private EditText mTripEdit;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ((FishEntryActivity) getActivity()).setFishDetailsFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fishentry_details, container, false);

        mSize = (EditText) view.findViewById(R.id.fishSizeEditText);       
        mWeight = (EditText) view.findViewById(R.id.fishWeightEditText);

        mDateTime = ((EditText) view.findViewById(R.id.dateTimeEdit));

        mTripEdit = ((EditText) view.findViewById(R.id.tripEditText));
        mTripEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TripListDialogFragment dialog = TripListDialogFragment.newInstance(FishDetailsFragment.this);
                dialog.show(getFragmentManager(), "trip_dialog_fragment");
            }
        });

        if (getArguments() != null && getArguments().containsKey(FishEntryContentProvider.CONTENT_ITEM_TYPE)) {
            fillData((Uri) getArguments().get(FishEntryContentProvider.CONTENT_ITEM_TYPE));
        } else {
            dateTime = new Date();
            mDateTime.setText(DateTimeHelper.convertDate(dateTime, DateTimeHelper.dateTimeDisplayFormat));
        }

        custom = new DateTimePicker(getActivity(),
                new DateTimePicker.ICustomDateTimeListener() {

                    @Override
                    public void onSet(Dialog dialog, Calendar calendarSelected,
                                      Date dateSelected, int year, String monthFullName,
                                      String monthShortName, int monthNumber, int date,
                                      String weekDayFullName, String weekDayShortName,
                                      int hour24, int hour12, int min, int sec,
                                      String AM_PM) {
                        dateTime = dateSelected;
                        mDateTime.setText(DateTimeHelper.convertDate(dateSelected, DateTimeHelper.dateTimeDisplayFormat));
                    }

                    @Override
                    public void onCancel() {

                    }
                });

          custom.set24HourFormat(false);
          custom.setDate(Calendar.getInstance());

        view.findViewById(R.id.dateTimeButton).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        custom.showDialog();
                    }
                });

        return view;
    }

    private void fillData(Uri uri) {
        String[] projection = new String[] { FishEntryTable.COLUMN_SIZE, FishEntryTable.COLUMN_WEIGHT, FishEntryTable.COLUMN_DATETIME, FishEntryTable.COLUMN_TRIP_KEY, TripEntryTable.COLUMN_TITLE };

        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            mSize.setText(cursor.getString(cursor.getColumnIndexOrThrow(FishEntryTable.COLUMN_SIZE)));
            mWeight.setText(cursor.getString(cursor.getColumnIndexOrThrow(FishEntryTable.COLUMN_WEIGHT)));

            long dateTimeLong = cursor.getLong(cursor.getColumnIndexOrThrow(FishEntryTable.COLUMN_DATETIME));
            dateTime = new Date(dateTimeLong);
            mDateTime.setText(DateTimeHelper.convertDate(dateTime, DateTimeHelper.dateTimeDisplayFormat));

            selectedTripId = cursor.getInt(cursor.getColumnIndexOrThrow(FishEntryTable.COLUMN_TRIP_KEY));
            mTripEdit.setText(cursor.getString(cursor.getColumnIndexOrThrow(TripEntryTable.COLUMN_TITLE)));
            // always close the cursor
            cursor.close();
        }
    }

    public String getWeight() {
        return mWeight.getText().toString();
    }

    public String getSize() {
        return mSize.getText().toString();
    }

    public long getSelectedDateTime() {
        return dateTime.getTime();
    }

    public long getSelectedTripId() {
        return selectedTripId;
    }

    @Override
    public void onSomethingHappened(Uri uri) {
        String[] projection = new String[] { TripEntryTable.PRIMARY_KEY, TripEntryTable.COLUMN_TITLE };

        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            mTripEdit.setText(cursor.getString(cursor.getColumnIndexOrThrow(TripEntryTable.COLUMN_TITLE)));
            selectedTripId = cursor.getInt(cursor.getColumnIndexOrThrow(TripEntryTable.PRIMARY_KEY));

            cursor.close();
        }
    }
}
