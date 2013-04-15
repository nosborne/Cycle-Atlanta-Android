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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class ItemizedOverlayTrack extends ItemizedOverlay<OverlayItem> {
	private final ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
	Context mContext = null;
	private int selected = 0;
	private int buffKey = 0;
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
		  builder.setTitle("Add Details");
		  final CharSequence[] detailList = {
				  "Asset: Water Fountain",
				  "Asset: Secret Passage",
				  "Asset: Public Restrooms",
				  "Asset: Bike Shop",
				  "Asset: Bike Parking",
				  "Issue: Pavement Issue",
				  "Issue: Traffic Signal",
				  "Issue: Enforcement",
				  "Issue: Bike Parking",
				  "Issue: Bike Lane Issue"};

		  builder.setSingleChoiceItems(detailList, selected, new DialogInterface.OnClickListener()
		  {
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				buffKey = which;
			}
		})
		.setCancelable(false)
		.setPositiveButton("OKAY", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				Toast.makeText(mContext,"Select "+detailList[buffKey],Toast.LENGTH_SHORT).show();
				selected = buffKey;
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
