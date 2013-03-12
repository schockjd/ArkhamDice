package com.schock.dpc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class DiceProbabilityCalculator extends Activity {
	private Integer mDice;
	private Integer mSuccesses;
	private Integer mTarget;
	private boolean mShotgun;
	private boolean mThompson;
	private Integer mRerolls;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDice = 1;
        mSuccesses = 1;
        mTarget = 5;
        mShotgun = false;
        mThompson = false;
        mRerolls = 0;
        setContentView(R.layout.main);

        updateView();
        
        final Button diceup = (Button) findViewById(R.id.diceup);
        diceup.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DiceProbabilityCalculator.this.mDice++;
				DiceProbabilityCalculator.this.updateView();
			}
		});
        
        final Button dicedown = (Button) findViewById(R.id.dicedown);
        dicedown.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DiceProbabilityCalculator.this.mDice--;
				if (DiceProbabilityCalculator.this.mDice < 1) {
					DiceProbabilityCalculator.this.mDice = 1;
				}
				DiceProbabilityCalculator.this.updateView();
			}
		});
        
        final Button targetup = (Button) findViewById(R.id.targetup);
        targetup.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DiceProbabilityCalculator.this.mTarget++;
				if (DiceProbabilityCalculator.this.mTarget > 6) {
					DiceProbabilityCalculator.this.mTarget = 6;
				}
				DiceProbabilityCalculator.this.updateView();
			}
		});
        
        final Button targetdown = (Button) findViewById(R.id.targetdown);
        targetdown.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DiceProbabilityCalculator.this.mTarget--;
				if (DiceProbabilityCalculator.this.mTarget < 1) {
					DiceProbabilityCalculator.this.mTarget = 1;
				}
				DiceProbabilityCalculator.this.updateView();
			}
		});
        
        final Button successup = (Button) findViewById(R.id.successup);
        successup.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DiceProbabilityCalculator.this.mSuccesses++;
				DiceProbabilityCalculator.this.updateView();
			}
		});
        
        final Button successdown = (Button) findViewById(R.id.succesdown);
        successdown.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DiceProbabilityCalculator.this.mSuccesses--;
				if (DiceProbabilityCalculator.this.mSuccesses < 1) {
					DiceProbabilityCalculator.this.mSuccesses = 1;
				}
				DiceProbabilityCalculator.this.updateView();
			}
		});
        
        final CheckBox shotgun = (CheckBox) findViewById(R.id.shotgun);
        shotgun.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        	
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				DiceProbabilityCalculator.this.mShotgun = isChecked;
				DiceProbabilityCalculator.this.updateView();
			}
		});
        
        final CheckBox thompson = (CheckBox) findViewById(R.id.thompson);
        thompson.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				DiceProbabilityCalculator.this.mThompson = isChecked;
				DiceProbabilityCalculator.this.updateView();
			}
		});
        
        final Button rerollup = (Button) findViewById(R.id.rerollup);
        rerollup.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DiceProbabilityCalculator.this.mRerolls++;
				DiceProbabilityCalculator.this.updateView();
			}
		});
        
        final Button rerolldown = (Button) findViewById(R.id.rerolldown);
        rerolldown.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DiceProbabilityCalculator.this.mRerolls--;
				if (DiceProbabilityCalculator.this.mRerolls < 0) {
					DiceProbabilityCalculator.this.mRerolls = 0;
				}
				DiceProbabilityCalculator.this.updateView();
			}
		});
    }
    
    private int rollDice() {
    	return (int)(Math.random()*6) + 1;
    }
    
	private Double calcPercentage() {
		double successSum=0;
		int rerolls = mRerolls;
		for (int i=0; i < 10000; i++) {
			int successes= 0;
			int successfulDice = 0;
			for (int dice = 0; dice < mDice; dice++) {
				int value = rollDice();
				if (value >= mTarget) {
					successes++;
					successfulDice++;
				}
				if (mShotgun && value == 6) {
					successes++;
				}
			}
			if (mThompson && (successes < mSuccesses)) {
				for (int dice = 0; dice < (mDice-successfulDice); dice++) {
					int value = rollDice();
					if (value >= mTarget) {
						successes++;
						successfulDice++;
					}
					if (mShotgun && value == 6) {
						successes++;
					}
				}
			}
			if (successes >= mSuccesses) {
				successSum++;
				rerolls = mRerolls;
			} else {
				if (rerolls>0) {
					i--;
					rerolls--;
				} else {
					rerolls = mRerolls;
				}
			}
		}
		
		return Double.valueOf(successSum/100.0d);
	}
	
	private void updateView() {
		final TextView dice = (TextView) findViewById(R.id.dice);
		dice.setText(mDice+"D6");
		
		
		final TextView target = (TextView) findViewById(R.id.target);
		target.setText(">="+mTarget);
		
		final TextView rerolls = (TextView) findViewById(R.id.reroll);
		rerolls.setText(mRerolls + " rerolls");
		
		final TextView successes = (TextView) findViewById(R.id.successes);
		successes.setText("x " + mSuccesses + " = " + Double.toString(((double)Math.round(calcPercentage()*100))/100) + "%");
		
	}
}