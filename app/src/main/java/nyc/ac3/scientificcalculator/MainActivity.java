package nyc.ac3.scientificcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import net.objecthunter.exp4j.*;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text_view);
        initNumberButtons();
        initOperationButtons();
        initEqualButton();
        initClearButton();
    }

    private void initClearButton() {
        Button button = (Button) findViewById(R.id.button_clear);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
            }
        });
    }

    private void initEqualButton() {
        Button button = (Button) findViewById(R.id.button_equal);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expression = textView.getText().toString();
                expression = parseString(expression);
                String result = null;
                try {
                    Expression e = new ExpressionBuilder(expression).build();
                    result = Double.toString(e.evaluate());
                    if (result.endsWith(".0"))
                        result = result.substring(0, result.indexOf('.'));
                    System.out.println(result);
                } catch (IllegalArgumentException e) {
                    result = "Error";
                }
                textView.setText(result);
            }
        });
    }

    private String parseString(String expression) {
        if (expression.contains("×"))
            expression = expression.replace("×", "*");
        if (expression.contains("÷"))
            expression = expression.replace("÷", "/");
        if (expression.contains("√"))
            expression = expression.replace("√", "sqrt");
        if (expression.contains("√"))
            expression = expression.replace("³√", "cbrt");

        return expression;
    }

    private void initOperationButtons() {
        for (int i = 0; i < 24; i++) {
            String buttonID = null;
            switch(i){
                case 0:
                    buttonID = "button_divide";
                    break;
                case 1:
                    buttonID = "button_times";
                    break;
                case 2:
                    buttonID = "button_minus";
                    break;
                case 3:
                    buttonID = "button_plus";
                    break;
                case 4:
                    buttonID = "button_point";
                    break;
                case 5:
                    buttonID = "button_paren_left";
                    break;
                case 6:
                    buttonID = "button_paren_right";
                    break;
                case 7:
                    buttonID = "button_percent";
                    break;
                //== Scientific part ==//
                case 8:
                    buttonID = "button_ceil";
                    break;
                case 9:
                    buttonID = "button_floor";
                    break;
                case 10:
                    buttonID = "button_pi";
                    break;
                case 11:
                    buttonID = "button_e";
                    break;
                case 12:
                    buttonID = "button_sqrt";
                    break;
                case 13:
                    buttonID = "button_tan";
                    break;
                case 14:
                    buttonID = "button_cos";
                    break;
                case 15:
                    buttonID = "button_sin";
                    break;
                case 16:
                    buttonID = "button_cbrt";
                    break;
                case 17:
                    buttonID = "button_atan";
                    break;
                case 18:
                    buttonID = "button_acos";
                    break;
                case 19:
                    buttonID = "button_asin";
                    break;
                case 20:
                    buttonID = "button_power";
                    break;
                case 21:
                    buttonID = "button_log10";
                    break;
                case 22:
                    buttonID = "button_log2";
                    break;
                case 23:
                    buttonID = "button_ln";
                    break;
            }

            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            final Button button = (Button) findViewById(resID);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textView.getText().toString().equals("Error"))
                        textView.setText("");
                    textView.append(button.getText().toString());
                }
            });
        }
    }

    private void initNumberButtons() {
        for (int i = 0; i < 10; i++) {
            String buttonID = "button" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            final Button button = (Button) findViewById(resID);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textView.getText().toString().equals("Error"))
                        textView.setText("");
                    textView.append(button.getText().toString());
                }
            });
        }
    }
}
