package sg.edu.rp.c346.mybmi;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText etweight, etheight;
    TextView tvdate, tvbmi;
    Button btncal, btnreset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etheight = findViewById(R.id.editTextHeight);
        etweight = findViewById(R.id.editTextWeight);
        tvbmi = findViewById(R.id.textViewbmi);
        tvdate = findViewById(R.id.textViewdate);
        btncal = findViewById(R.id.buttonCal);
        btnreset = findViewById(R.id.buttonReset);



        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etweight.setText("");
                etheight.setText("");
                tvdate.setText("Last Calculated Date: ");
                tvbmi.setText("Last Calculated BMI: ");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor preEdit = prefs.edit();
                preEdit.clear();
                preEdit.commit();


            }
        });


        btncal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String str1 = etweight.getText().toString();
                String str2 = etheight.getText().toString();

                if (TextUtils.isEmpty(str1)) {
                    etweight.setError("Please enter your weight");
                    etweight.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(str2)) {
                    etheight.setError("Please enter your height");

                    etheight.requestFocus();
                    return;
                }


                float weight = Float.parseFloat(str1);
                float height = Float.parseFloat(str2);


                float bmiValue = calculateBMI(weight, height);


                String bmiInterpretation = interpretBMI(bmiValue);

                Calendar now = Calendar.getInstance();
                String datetime = now.get(Calendar.DAY_OF_MONTH) + "/" +
                        (now.get(Calendar.MONTH) + 1) + "/" +
                        now.get(Calendar.YEAR) + " " +
                        now.get(Calendar.HOUR_OF_DAY) + ":" +
                        now.get(Calendar.MINUTE);

                tvdate.setText("Last calculated date " + datetime);
                tvbmi.setText("Last calculated BMI " + String.valueOf(bmiValue + "\n" + bmiInterpretation));

            }
        });

    }



    public float calculateBMI(float weight, float height) {
        return (float) (weight / (height * height));
    }

    @Override
    protected void onPause() {
        super.onPause();
        float weight = Float.parseFloat(etweight.getText().toString());
        float height = Float.parseFloat(etheight.getText().toString());
        String strdate = tvdate.getText().toString();
        String strbmi = tvbmi.getText().toString();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preEdit = prefs.edit();
        preEdit.putFloat("weight", weight);
        preEdit.putFloat("height", height);
        preEdit.putString("date", strdate);
        preEdit.putString("bmi", strbmi);


        preEdit.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String date = prefs.getString("date", "");
        String bmi = prefs.getString("bmi", "");
        float weight = prefs.getFloat("weight",0);
        float height = prefs.getFloat("height",0 );



        etweight.setText(Float.toString(weight));
        etheight.setText(Float.toString(height));
        tvdate.setText("Last Calculated date: " + date);
        tvbmi.setText("Last Calculated BMI: " + bmi);


    }



       String interpretBMI(float bmiValue) {

        if (bmiValue < 18.5) {
            return "You are underweight";
        } else if (bmiValue < 24.9) {

            return "You are normal";
        } else if (bmiValue < 29.9) {

            return "You are overweight";

        } else {
            return "Obese";
        }



    }
}

















