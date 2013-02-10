package nibbles.settings;

import java.io.Serializable;

import nibbles.ui.NibblesActivity;
import nibbles.ui.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;

public class Settings extends Activity {
	public static final String KEY_SETTINGS = "nibbles.settings.SETTINGS";
	
	private static final int N_PLAYERS_MAX = 2;

	public static class Values implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private final int nPlayers;
		private final boolean isMonochrome;
		
		private Values(int nPlayers, boolean isMonochrome) {
			this.nPlayers = nPlayers;
			this.isMonochrome = isMonochrome;
		}

		public int getnPlayers() {
			return nPlayers;
		}
		
		public boolean isMonochrome() {
			return isMonochrome;
		}
	}

	private void initSpinner() {
		String[] playerChoices = new String[N_PLAYERS_MAX];
		for (int i = 1; i <= N_PLAYERS_MAX; i++) {
			playerChoices[i - 1] = String.format(getResources()
					.getQuantityString(R.plurals.player, i), i);
		}
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, playerChoices);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
//		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int pos, long id) {
//				values.setnPlayers(pos + 1);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//			}
//		});
	}
	
//	private void initCheckbox() {
//		CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);
//		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				values.setMonochrome(isChecked);
//			}
//		});
//	}
	
	public void startGame (View view) {
	    Intent intent = new Intent(this, NibblesActivity.class);
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);
		Values values = new Values(spinner.getSelectedItemPosition() + 1, checkBox.isChecked());
	    intent.putExtra(KEY_SETTINGS, values);
	    startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nibbles_settings);

		initSpinner();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

}
