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
        hostFragment(new EggFragment());
    }


    public void hostFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }


}
