package maxon.guessnumber;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * GuessNumberActivity Activity.
 *
 * @author yfwz100
 */
public class GuessNumberActivity extends ActionBarActivity {

    // UI components.
    private TextView[] numberTextViews;
    private EditText numberInput;
    private Button guessBtn;

    // the expression to generate the number series.
    private Expression expression;
    // the stopwatch monitor.
    private Stopwatch stopwatch = new Stopwatch();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guess_number);
        setTitle(R.string.guess_number_title);

        numberTextViews = new TextView[] {
                ((TextView) findViewById(R.id.number1)),
                ((TextView) findViewById(R.id.number2)),
                ((TextView) findViewById(R.id.number3)),
                ((TextView) findViewById(R.id.number4)),
                ((TextView) findViewById(R.id.number5))
        };

        numberInput = ((EditText) findViewById(R.id.guess_number_input));
        numberInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    guessNumber();
                }
                return false;
            }
        });

        guessBtn = ((Button) findViewById(R.id.guess_number_button));
        guessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guessNumber();
            }
        });

        nextNumbers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_guess_number, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_about:
                showAboutDialog();
                break;
            case R.id.action_settings:
                showSettings();
                break;
            case R.id.action_next_numbers:
                nextNumbers();
                break;
            case R.id.action_show_formula:
                showFormula();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void showFormula() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.show_formula_title).setMessage(expression.toString());
        builder.create().show();
    }

    protected boolean isGuessNumberCorrect() {
        if (expression != null) {
            int number = Integer.parseInt(numberInput.getText().toString());
            return number == expression.getAt(6);
        } else {
            throw new RuntimeException("Unexpected null expression.");
        }
    }

    protected void guessNumber() {
        try {
            if (isGuessNumberCorrect()) {
                showWinDialog(((int) stopwatch.stop() / 1000));
            } else {
                showWrongAnswerDialog();
            }
        } catch (NumberFormatException ex) {
            showErrDialog(ex);
        }
    }

    private void showErrDialog(Exception ex) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error_title).setMessage(R.string.error_message);
        builder.create().show();
        ex.printStackTrace();
    }

    private void showWinDialog(int seconds) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.win_title).setMessage(String.format(getString(R.string.win_message), seconds));
        builder.setPositiveButton(R.string.action_next_numbers, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                numberInput.setText("");
                nextNumbers();
            }
        });
        builder.create().show();
    }

    private void showWrongAnswerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.wrong_answer_title).setMessage(R.string.wrong_answer_message);
        builder.setPositiveButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                numberInput.setText("");
            }
        });
        builder.create().show();
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.about_title).setView(R.layout.dialog_about);
        builder.create().show();
    }

    private void showSettings() {
        // TODO no settings at present.
    }

    private void nextNumbers() {
        expression = Expression.generate();

        try {
            for (int i = 0; i < numberTextViews.length; i++) {
                numberTextViews[i].setText(String.valueOf(expression.getAt(i + 1)));

                Animation animation = AnimationUtils.loadAnimation(this, R.anim.number_translation);
                animation.setDuration(i * 800);
                numberTextViews[i].startAnimation(animation);
            }
        } catch (Exception ex) {
            showErrDialog(ex);
        }

        stopwatch.start();
    }
}
