/**	 Cycle Altanta, Copyright 2012 Georgia Institute of Technology
 *                                    Atlanta, GA. USA
 *
 *   @author Christopher Le Dantec <ledantec@gatech.edu>
 *   @author Anhong Guo <guoanhong15@gmail.com>
 *
 *   Updated/Modified for Atlanta's app deployment. Based on the
 *   CycleTracks codebase for SFCTA.
 *
 *   CycleTracks, Copyright 2009,2010 San Francisco County Transportation Authority
 *                                    San Francisco, CA, USA
 *
 * 	 @author Billy Charlton <billy.charlton@sfcta.org>
 *
 *   This file is part of CycleTracks.
 *
 *   CycleTracks is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   CycleTracks is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with CycleTracks.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.gatech.ppl.cycleatlanta;

// This is all from the hello-mapview tutorial

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class ItemizedOverlayTrack extends ItemizedOverlay<OverlayItem> {
	private final ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
	Context mContext = null;
	private boolean should_delete = false;

	public ItemizedOverlayTrack(Drawable defaultMarker) {
		super(boundCenter(defaultMarker));
	}

	public ItemizedOverlayTrack(Drawable defaultMarker, Context context) {
		  super(boundCenterBottom(defaultMarker));
		  mContext = context;
		  populate();
		}

	@Override
	protected OverlayItem createItem(int i) {
			return overlays.get(i);
	}

	@Override
	public int size() {
		return overlays.size();
	}

	public void addOverlay(OverlayItem overlay) {
		overlays.add(overlay);
		populate();
	}

	public void repopulate() {
		populate();
	}

	public void delete_item(int index){
		overlays.remove(index);
	}

	@Override
	protected boolean onTap(int index) {
		final int index2 = index;

	  if(mContext != null){
		  should_delete = false;
		  //OverlayItem item = overlays.get(index);
		  AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		  View view = LayoutInflater.from(mContext).inflate(R.layout.detaildialog, null, false);
		  final CharSequence[] issueList = {"Pavement Issue",
				  "Traffic Signal",
				  "Enforcement",
				  "Bike Parking",
				  "Bike Lane Issue"};
		  final CharSequence[] assetList = {"Water Fountain",
				  "Secret Passage",
				  "Public Restrooms",
				  "Bike Shop",
				  "Bike Parking"};



		  //create radio group
		  final RadioGroup rg = (RadioGroup) view.findViewById(R.id.issueList);
		  //create spinner and set values
		  final Spinner spinner = (Spinner) view.findViewById(R.id.issueTypeSpinner);
		  //crate edit text
		  final EditText commentTxt = (EditText) view.findViewById(R.id.commentText);

		  List<String> spinnerVals = new ArrayList<String>();
		  spinnerVals.add("Asset");
		  spinnerVals.add("Issue");
		  ArrayAdapter<String> adp = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item,spinnerVals);
		  adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		  spinner.setAdapter(adp);

		  class SpinnerOnItemSelectListener extends Activity implements OnItemSelectedListener
		  {
			  	String spinnerText = "";
			    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			    {
			    	spinnerText = spinner.getSelectedItem().toString();

			    	for (int i=0; i < rg.getChildCount();i++)
			    	{
			    		//if asset
				    	if (spinnerText == "Asset")
				    	{
				    		View v = rg.getChildAt(i);
				    		if (v instanceof RadioButton)
				    		{
				    			RadioButton btn = (RadioButton)v;
				    			btn.setText(assetList[i]);
				    		}
				    	}
				    	//if issue
				    	if (spinnerText == "Issue")
				    	{
				    		View v = rg.getChildAt(i);
				    		if (v instanceof RadioButton)
				    		{
				    			RadioButton btn = (RadioButton)v;
				    			btn.setText(issueList[i]);
				    		}

				    	}
			    	}

			    }
			    public void onNothingSelected(AdapterView<?> parent)
			    {
			    }
			}

		  spinner.setOnItemSelectedListener(new SpinnerOnItemSelectListener());


		  //get radio group and set value to radioText

		  class RadioOnCheckedChangeListener extends Activity implements OnCheckedChangeListener
		  {
			  String radioText = "";
		        public void onCheckedChanged(RadioGroup group, int checkedId)
		        {
		        	RadioButton chkdBtn = (RadioButton) rg.findViewById(checkedId);
	                radioText = chkdBtn.getText().toString();
		        }
		  }
		  rg.setOnCheckedChangeListener(new RadioOnCheckedChangeListener());

		  //set value of commmentTxt from EditText box
		  class CommentTextWatcher extends Activity implements TextWatcher
		  {
			String commentText = "";
			@Override
			public void afterTextChanged(Editable s)
			{
				commentText = s.toString();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after){}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count){}
		  }
		  commentTxt.addTextChangedListener(new CommentTextWatcher());

		builder.setView(view)
		.setCancelable(false)
		.setPositiveButton("OKAY", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				Toast.makeText(mContext,"Saved",Toast.LENGTH_SHORT).show();
			}
		})
		.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				Toast.makeText(mContext,"Cancel Click",Toast.LENGTH_SHORT).show();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	  }
	  return true;
	}
}
