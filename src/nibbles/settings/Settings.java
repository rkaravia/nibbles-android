package nibbles.settings;

import nibbles.ui.NibblesActivity;
import nibbles.ui.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Settings extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	public static final String KEY_PREF_SPEED = "pref_speed";
	public static final String KEY_PREF_HUMAN_PLAYERS = "pref_human_players";
	public static final String KEY_PREF_ADVERSARIES = "pref_adversaries";
	public static final String KEY_PREF_SOUND = "pref_sound";
	public static final String KEY_PREF_MONOCHROME = "pref_monochrome";
	public static final String KEY_PREF_START = "pref_start";
	
	private ListPreference nPlayersPref;
	private ListPreference nAdversariesPref;
	private ListPreference speedPref;

	private void initQuantityChoice(ListPreference pref, int quantity, int from, int to, int defaultValue) {
		int nEntries = to - from + 1;
		String[] values = new String[nEntries];
		String[] entries = new String[nEntries];
		for (int i = 0; i < nEntries; i++) {
			int value = from + i;
			values[i] = Integer.toString(value);
			entries[i] = getResources().getQuantityString(quantity, value, value);
		}
		pref.setEntryValues(values);
		pref.setEntries(entries);
		pref.setValue(Integer.toString(defaultValue));
		updateSummary(pref);
	}
	
	@SuppressWarnings("deprecation")
	private void initPrefs() {
		nPlayersPref = (ListPreference) findPreference(KEY_PREF_HUMAN_PLAYERS);
		initQuantityChoice(nPlayersPref, R.plurals.player, 1, 2, 1); //TODO outsource constants 1, 2, 1
		
		nAdversariesPref = (ListPreference) findPreference(KEY_PREF_ADVERSARIES);
		initQuantityChoice(nAdversariesPref, R.plurals.adversary, 0, 2, 1); //TODO outsource constants 0, 2, 1
		
		speedPref = (ListPreference) findPreference(KEY_PREF_SPEED);
		updateSummary(speedPref);
		
		findPreference(KEY_PREF_START).setIntent(new Intent(this, NibblesActivity.class));
	}
	
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        initPrefs();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
    
    private void updateSummary(ListPreference pref) {
    	pref.setSummary(pref.getEntry());
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(KEY_PREF_SPEED)) {
			updateSummary(speedPref);
		} else if (key.equals(KEY_PREF_HUMAN_PLAYERS)) {
			updateSummary(nPlayersPref);
		} else if (key.equals(KEY_PREF_ADVERSARIES)) {
			updateSummary(nAdversariesPref);
		}
	}
}