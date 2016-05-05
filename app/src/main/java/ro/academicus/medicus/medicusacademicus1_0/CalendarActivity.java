package ro.academicus.medicus.medicusacademicus1_0;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class CalendarActivity extends AppCompatActivity {

    public static boolean intentFromCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        intentFromCalendar = false;

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendar_view);
        calendarView.setDate(Calendar.getInstance().getTimeInMillis());
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        final Intent intent = getIntent();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                //Toast.makeText(getApplicationContext(), day + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();

                HashMap<String, String[]> recommendations = (HashMap<String, String[]>) intent.getSerializableExtra(MenuActivity.EXTRA_RECOMMENDATIONS);

                for (String date: recommendations.keySet()) {
                    String dateClicked = day + "/" + (month + 1) + "/" + year;
                    if (date.equals(dateClicked)) {

                        StringBuilder builder = new StringBuilder();
                        for(String recommendaton : recommendations.get(date)) {
                            builder.append(Html.fromHtml("&#8226; "));
                            builder.append(recommendaton);
                            builder.append('\n');
                        }

                        String specificRecommendations = builder.toString();

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CalendarActivity.this);
                        alertDialogBuilder.setMessage(specificRecommendations)
                                .setTitle(R.string.recommendations_popup_title)
                                .setPositiveButton(R.string.recommendations_popup_pos_button, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .show();
                        break;
                    }
                }
            }
        });
    }

    public void backToMenu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);

        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        //PatientConditionActivity.this.finish();

        intentFromCalendar = true;
        startActivity(intent);
    }
}
