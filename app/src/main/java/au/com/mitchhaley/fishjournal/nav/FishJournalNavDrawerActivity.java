package au.com.mitchhaley.fishjournal.nav;

import android.content.Intent;
import au.com.mitchhaley.fishjournal.R;
import au.com.mitchhaley.fishjournal.activity.FishEntryActivity;
import au.com.mitchhaley.fishjournal.activity.FishListActivity;
import au.com.mitchhaley.fishjournal.activity.TripEntryActivity;
import au.com.mitchhaley.fishjournal.activity.TripListActivity;

public abstract class FishJournalNavDrawerActivity extends AbstractNavDrawerActivity {

	public abstract int getMainLayout();
	
    @Override
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {

        NavDrawerItem[] menu = new NavDrawerItem[] {
                NavMenuSection.create(100, "Fish"),
                NavMenuItem.create(101, "Fish Entry", R.color.fishEntryBackground, false, this),
                NavMenuItem.create(102, "Fish List", R.color.fishListBackground, false, this),
                NavMenuSection.create(103, "Trip"),
                NavMenuItem.create(104, "Trip Entry", R.color.tripEntryBackground, false, this),
                NavMenuItem.create(105, "Trip List", R.color.tripListBackground, false, this)};

        NavDrawerActivityConfiguration navDrawerActivityConfiguration = new NavDrawerActivityConfiguration();
        navDrawerActivityConfiguration.setMainLayout(getMainLayout());
        navDrawerActivityConfiguration.setDrawerLayoutId(R.id.drawer_layout);
        navDrawerActivityConfiguration.setLeftDrawerId(R.id.left_drawer);
        navDrawerActivityConfiguration.setNavItems(menu);
//            navDrawerActivityConfiguration.setDrawerShadow(R.drawable.drawer_shadow);
        navDrawerActivityConfiguration.setDrawerOpenDesc(R.string.drawer_open);
        navDrawerActivityConfiguration.setDrawerCloseDesc(R.string.drawer_close);
        navDrawerActivityConfiguration.setBaseAdapter(
                new NavDrawerAdapter(this, R.layout.navdrawer_item, menu ));
        return navDrawerActivityConfiguration;
    }

    @Override
    public int getNavDrawerPosition() {
        if (this instanceof FishEntryActivity) {
            return 1;
        } else if (this instanceof FishListActivity) {
            return 2;
        } else if (this instanceof TripEntryActivity) {
            return 3;
        } else if (this instanceof TripListActivity) {
            return 4;
        } else {
            return 0;
        }

    }

    @Override
    protected void onNavItemSelected(int id) {

        Intent i;
        switch ((int)id) {
            case 100:
                break;
            case 101:
                i = new Intent(this, FishEntryActivity.class);
                i.putExtra(NAV_DRAWER_POSITION, 1);
                startActivity(i);
                break;
            case 102:
                i = new Intent(this, FishListActivity.class);
                i.putExtra(NAV_DRAWER_POSITION, 1);
                startActivity(i);
                break;
            case 103:
                break;
            case 104:
                i = new Intent(this, TripEntryActivity.class);
                i.putExtra(NAV_DRAWER_POSITION, 1);
                startActivity(i);
                break;
            case 105:
                i = new Intent(this, TripListActivity.class);
                i.putExtra(NAV_DRAWER_POSITION, 1);
                startActivity(i);
                break;
        }
    }

	
}
