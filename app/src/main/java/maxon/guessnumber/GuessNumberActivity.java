package maxon.guessnumber;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * GuessNumberActivity Activity.
 *
 * @author yfwz100
 */
public class GuessNumberActivity extends ActionBarActivity {

    private Expression expression;
    private TextView[] numberTextViews;
    private EditText numberInput;

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
        }

        return super.onOptionsItemSelected(item);
    }

    protected void guessNumber() {
        if (expression != null) {
            int number;
            try {
                number = Integer.parseInt(((EditText) numberInput).getText().toString());
            } catch (NumberFormatException e) {
                showErrDialog(e);
                return;
            }
            if (number == expression.getAt(6)) {
                showWinDialog(((int) stopwatch.stop() / 1000));
                nextNumbers();
            } else {
                showWrongAnswerDialog();
            }
            numberInput.setText("");
        } else {
            throw new RuntimeException("Unexpected null expression.");
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
        builder.create().show();
    }

    private void showWrongAnswerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.wrong_answer_title).setMessage(R.string.wrong_answer_message);
        builder.create().show();
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.about_title).setMessage(R.string.about_message);
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
                numberTextViews[i].startAnimation(animation);
            }
        } catch (Exception ex) {
            showErrDialog(ex);
        }

        stopwatch.start();
    }
}
