package nyc.ac3.scientificcalculator;

/**
 * Created by huilin on 10/14/16.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by huilin on 10/14/16.
 */

public class EasterEggActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.egg_activity);
        String trigger = getIntent().getStringExtra(MainActivity.KEY_FOR_FRAGMENTS);
        switch (trigger) {
            case "√(666)":
                hostFragment(new Philosoraptor());
                break;
            case "clear":
                hostFragment(new ClearCalc());
                break;
            case "5+6":
                hostFragment(new BabyCalc());
                break;
        }
    }


    public void hostFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }


}
