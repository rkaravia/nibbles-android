package nibbles.settings;

import nibbles.ui.NibblesActivity;
import nibbles.ui.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class Settings extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	public static final String KEY_PREF_SPEED = "pref_speed";
	public static final String KEY_PREF_HUMAN_PLAYERS = "pref_human_players";
	public static final String KEY_PREF_ADVERSARIES = "pref_adversaries";
	public static final String KEY_PREF_SOUND = "pref_sound";
	public static final String KEY_PREF_MONOCHROME = "pref_monochrome";
	public static final String KEY_PREF_START = "pref_start";
	
	private static final int REQUEST_CODE = 1;
	
	private static final QuantityChoice CHOICE_HUMAN_PLAYERS = new QuantityChoice(0, 2, 1);
	private static final QuantityChoice CHOICE_ADVERSARIES = new QuantityChoice(0, 2, 1);
	
	private ListPreference nPlayersPref;
	private ListPreference nAdversariesPref;
	private ListPreference speedPref;
	
	public static Bundle SAVED_STATE;
	
	private static class QuantityChoice {
		private final int from;
		private final int to;
		private final int defaultValue;
		
		private QuantityChoice(int from, int to, int defaultValue) {
			this.from = from;
			this.to = to;
			this.defaultValue = defaultValue;
		}
	}

	private void initQuantityChoice(ListPreference pref, int quantity, QuantityChoice choice) {
		int nEntries = choice.to - choice.from + 1;
		String[] values = new String[nEntries];
		String[] entries = new String[nEntries];
		for (int i = 0; i < nEntries; i++) {
			int value = choice.from + i;
			values[i] = Integer.toString(value);
			entries[i] = getResources().getQuantityString(quantity, value, value);
		}
		pref.setEntryValues(values);
		pref.setEntries(entries);
		pref.setValue(Integer.toString(choice.defaultValue));
		updateSummary(pref);
	}
	
	@SuppressWarnings("deprecation")
	private void initPrefs() {
		nPlayersPref = (ListPreference) findPreference(KEY_PREF_HUMAN_PLAYERS);
		initQuantityChoice(nPlayersPref, R.plurals.player, CHOICE_HUMAN_PLAYERS);
		
		nAdversariesPref = (ListPreference) findPreference(KEY_PREF_ADVERSARIES);
		initQuantityChoice(nAdversariesPref, R.plurals.adversary, CHOICE_ADVERSARIES);
		
		speedPref = (ListPreference) findPreference(KEY_PREF_SPEED);
		updateSummary(speedPref);
		
		findPreference(KEY_PREF_START).setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				startActivityForResult(new Intent(Settings.this, NibblesActivity.class), REQUEST_CODE);
				return true;
			}
		});
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			SAVED_STATE = data.getExtras();
		}
	}
}