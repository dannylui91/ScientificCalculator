package nyc.ac3.scientificcalculator;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.operator.Operator;

public class MainActivity extends AppCompatActivity {
    private boolean isLandscape;
    private boolean isClearEntry;
    private static double ans = 0;
    private static Operator factorial;
    private final static String KEY_FOR_TEXT_VIEW = "text_key";
    private final static String KEY_FOR_HISTORY_VIEW = "history_key";

    TextView textView;
    TextView historyView;
    Button clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            isLandscape = true;
        else
            isLandscape = false;

        historyView = (TextView) findViewById(R.id.history_view);
        textView = (TextView) findViewById(R.id.text_view);

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            historyView.setText(savedInstanceState.getString(KEY_FOR_HISTORY_VIEW));
            textView.setText(savedInstanceState.getString(KEY_FOR_TEXT_VIEW));
        } else {
            // Probably initialize members with default values for a new instance
        }

        initFactorialOperator(); //to do factorial operation

        initNumberButtons();
        initOperationButtons();
        initEqualButton();
        initClearButton();
    }

    //when buttons 0->9 and Ans is pressed
    private void initNumberButtons() {
        for (int i = 0; i < 11; i++) { //11 buttons: buttons0->9 including Ans button (button10)
            String buttonID = "button" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            final Button button = (Button) findViewById(resID);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isClearEntry = true;
                    clearButton.setText("CE");
                    if (!historyView.getText().toString().isEmpty() && !historyView.getText().toString().startsWith("Ans")) {
                        textView.setText("");
                        setHistoryView();
                        textView.append(button.getText().toString());
                    } else
                        textView.append(button.getText().toString());
                }
            });
        }
    }

    //when every button except buttons 0->9 and Ans are pressed
    private void initOperationButtons() {
        int maxButtons = 7;
        if (isLandscape)
            maxButtons = 22;

        for (int i = 0; i < maxButtons; i++) {
            String buttonID = null;
            switch (i) {
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
                /* ============ Scientific part ============ */
                case 7:
                    buttonID = "button_factorial";
                    break;
                case 8:
                    buttonID = "button_pi";
                    break;
                case 9:
                    buttonID = "button_e";
                    break;
                case 10:
                    buttonID = "button_sqrt";
                    break;
                case 11:
                    buttonID = "button_tan";
                    break;
                case 12:
                    buttonID = "button_cos";
                    break;
                case 13:
                    buttonID = "button_sin";
                    break;
                case 14:
                    buttonID = "button_cbrt";
                    break;
                case 15:
                    buttonID = "button_atan";
                    break;
                case 16:
                    buttonID = "button_acos";
                    break;
                case 17:
                    buttonID = "button_asin";
                    break;
                case 18:
                    buttonID = "button_power";
                    break;
                case 19:
                    buttonID = "button_log10";
                    break;
                case 20:
                    buttonID = "button_log2";
                    break;
                case 21:
                    buttonID = "button_ln";
                    break;
            }

            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            final Button button = (Button) findViewById(resID);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isClearEntry = true;
                    clearButton.setText("CE");
                    if (textView.getText().toString().equals("Error"))
                        textView.setText("");
                    if (!historyView.getText().toString().isEmpty() && !historyView.getText().toString().startsWith("Ans")) {
                        System.out.println("DO THIS LINE");
                        String currentButton = button.getText().toString();
                        if (!currentButton.equals("+") && !currentButton.equals("×") && !currentButton.equals("÷")
                                && !currentButton.equals("-") && !currentButton.equals("!") && !currentButton.equals("^")) {
                            textView.setText("");
                            setHistoryView();
                            String operatorWithParen = addLeftParen(button.getText().toString());
                            textView.setText(operatorWithParen);
                        } else {
                            setHistoryView();
                            String operatorWithParen = addLeftParen(button.getText().toString());
                            textView.append(operatorWithParen);
                        }

                    } else {
                        String operatorWithParen = addLeftParen(button.getText().toString());
                        textView.append(operatorWithParen);
                    }

                }
            });
        }
    }

    //when equal is pressed
    private void initEqualButton() {
        Button button = (Button) findViewById(R.id.button_equal);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClearEntry = false;
                clearButton.setText("AC");
                String operatorWithParen = addRightParen(textView.getText().toString());
                textView.setText(operatorWithParen);
                String expression = textView.getText().toString();
                if (expression.equals("sin()")) {
                    seePope();
                }
                if (expression.equals("√(666)")) {
                    seePhilosoraptor();
                }
                historyView.setText(expression + "=");
                expression = parseForCalculation(expression);
                System.out.println(expression);
                String result = null;
                try {
                    Expression e = new ExpressionBuilder(expression).operator(factorial).build();
                    result = Double.toString(e.evaluate());
                    if (result.endsWith(".0"))
                        result = result.substring(0, result.indexOf('.'));
                    System.out.println(result);
                } catch (IllegalArgumentException e) {
                    result = "Error";
                }
                textView.setText(result);
                if (!result.equals("Error"))
                    ans = Double.valueOf(result);
            }
        });
    }

    //when clear is pressed
    private void initClearButton() {
        clearButton = (Button) findViewById(R.id.button_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClearEntry) {
                    String currentString = textView.getText().toString();
                    if (!currentString.isEmpty()) {
                        if (currentString.endsWith("sin⁻¹(") || currentString.endsWith("cos⁻¹(")
                                || currentString.endsWith("tan⁻¹(") || currentString.endsWith("log₁₀("))
                            textView.setText(currentString.substring(0, currentString.length() - 6));
                        else if (currentString.endsWith("log₂("))
                            textView.setText(currentString.substring(0, currentString.length() - 5));
                        else if (currentString.endsWith("Ans") || currentString.endsWith("sin(")
                                || currentString.endsWith("cos(") || currentString.endsWith("tan(")
                                || currentString.endsWith("sin⁻¹("))
                            textView.setText(currentString.substring(0, currentString.length() - 4));
                        else if (currentString.endsWith("³√(") || currentString.endsWith("ln("))
                            textView.setText(currentString.substring(0, currentString.length() - 3));
                        else if (currentString.endsWith("√("))
                            textView.setText(currentString.substring(0, currentString.length() - 2));
                        else
                            textView.setText(currentString.substring(0, currentString.length() - 1));
                    }
                } else {
                    if (!historyView.getText().toString().isEmpty()) {
                        setHistoryView();
                    }
                    textView.setText("");
                }
            }
        });
    }

    //simply to set the history view
    private void setHistoryView() {
        if (Double.toString(ans).endsWith(".0"))
            historyView.setText("Ans = " + (int) ans);
        else
            historyView.setText("Ans = " + ans);
    }

    //convert the operators that the user sees into parsable operators used in exp4j parser
    private String parseForCalculation(String expression) {
        if (expression.contains("×"))
            expression = expression.replace("×", "*");
        if (expression.contains("÷"))
            expression = expression.replace("÷", "/");

        if (expression.contains("³√"))
            expression = expression.replace("³√", "cbrt");
        if (expression.contains("√"))
            expression = expression.replace("√", "sqrt");

        if (expression.contains("sin⁻¹"))
            expression = expression.replace("sin⁻¹", "asin");
        if (expression.contains("cos⁻¹"))
            expression = expression.replace("cos⁻¹", "acos");
        if (expression.contains("tan⁻¹"))
            expression = expression.replace("tan⁻¹", "atan");

        if (expression.contains("ln"))
            expression = expression.replace("ln", "log");
        if (expression.contains("log₂"))
            expression = expression.replace("log₂", "log2");
        if (expression.contains("log₁₀"))
            expression = expression.replace("log₁₀", "log10");

        if (expression.contains("Ans"))
            expression = expression.replace("Ans", "" + ans);

        return expression;
    }

    //add a left parenthesis to operators such as sin, cos, log etc.
    private String addLeftParen(String s) {
        if (s.equals("√") || s.equals("³√") || s.equals("sin")
                || s.equals("cos") || s.equals("tan") || s.equals("sin⁻¹")
                || s.equals("cos⁻¹") || s.equals("tan⁻¹") || s.equals("log₁₀")
                || s.equals("log₂") || s.equals("ln"))
            return s += "(";
        return s;
    }

    private String addRightParen(String s) {
        int leftParensCount = 0;
        int rightParensCount = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                leftParensCount++;
            } else if (s.charAt(i) == ')') {
                rightParensCount++;
            }
        }

        if (leftParensCount != rightParensCount) {
                s += ")";
            }
        return s;
    }

    //using exp4j's custom operator creation
    private static void initFactorialOperator() {
        factorial = new Operator("!", 1, true, Operator.PRECEDENCE_POWER + 1) {
            @Override
            public double apply(double... args) {
                final int arg = (int) args[0];
                if ((double) arg != args[0]) {
                    throw new IllegalArgumentException("Operand for factorial has to be an integer");
                }
                if (arg < 0) {
                    throw new IllegalArgumentException("The operand of the factorial can not be less than zero");
                }
                double result = 1;
                for (int i = 1; i <= arg; i++) {
                    result *= i;
                }
                return result;
            }
        };
    }

    private void seePope() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://vine.co/v/ei2Zaa9whPx"));
        startActivity(intent);
    }

    private void seePhilosoraptor() {
        Intent intent = new Intent(getApplicationContext(), EasterEggActivity.class);
        startActivity(intent);
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString(KEY_FOR_HISTORY_VIEW, historyView.getText().toString());
        savedInstanceState.putString(KEY_FOR_TEXT_VIEW, textView.getText().toString());

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
}
