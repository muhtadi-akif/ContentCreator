/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package multiplexer.contentcreator.adapter;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import multiplexer.contentcreator.R;

import static android.content.Context.MODE_PRIVATE;

public class ScreenSlidePageFragment extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";
    public static final String[] arrAgeGroup = {"Select Age Group of Audiance", "15-25", "26-40", "40-50", "50 above"};
    public static final String[] arrGender = {"Select Gender of Audiance", "Male", "Female"};

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ScreenSlidePageFragment create(int pageNumber) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.activity_audiance, container, false);


        ((TextView) rootView.findViewById(android.R.id.text1)).setText("Audience "+(int)(mPageNumber+1));
        pref = container.getContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        final String[] age = {""};
        final String[] gender = {""};
        Spinner spinnerAgeGroup = (Spinner) rootView.findViewById(R.id.spinnerAgeGroup);
        Spinner spinnerGender = (Spinner) rootView.findViewById(R.id.spinnerGender);
        final EditText  edtSituation = (EditText) rootView.findViewById(R.id.edtSituation);
        final EditText  edtPurpose = (EditText) rootView.findViewById(R.id.edtPurpose);
        final EditText edtOutcome = (EditText) rootView.findViewById(R.id.edtOutcome);
        Button  btnSaveAudience = (Button) rootView.findViewById(R.id.btn_save_audience);
        spinnerAgeGroup.setAdapter(new ArrayAdapter<String>(container.getContext(), android.R.layout.simple_spinner_dropdown_item, arrAgeGroup));
        spinnerGender.setAdapter(new ArrayAdapter<String>(container.getContext(), android.R.layout.simple_spinner_dropdown_item, arrGender));
        if(pref.contains("audGender")){
            if(pref.getString("audGender","").equals("Male")){
                spinnerGender.setSelection(1);
            } else if(pref.getString("audGender","").equals("Female")){
                spinnerGender.setSelection(2);
            }

            if(pref.getString("audAgeGroup","").equals("15-25")){
                spinnerAgeGroup.setSelection(1);
            } else if(pref.getString("audAgeGroup","").equals("26-40")){
                spinnerAgeGroup.setSelection(2);
            }else if(pref.getString("audAgeGroup","").equals("40-50")){
                spinnerAgeGroup.setSelection(3);
            }else if(pref.getString("audAgeGroup","").equals("50 above")){
                spinnerAgeGroup.setSelection(4);
            }

            edtSituation.setText(pref.getString("audSituation",""));
            edtPurpose.setText(pref.getString("audPurpose",""));
            edtOutcome.setText(pref.getString("audOutcome",""));
        }


        spinnerAgeGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                age[0] = arrAgeGroup[position]+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender[0] = arrGender[position]+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Set the title view to show the page number.

        btnSaveAudience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("audSituation",edtSituation.getText().toString());
                editor.putString("audPurpose",edtPurpose.getText().toString());
                editor.putString("audOutcome",edtOutcome.getText().toString());
                editor.putString("audAgeGroup", age[0]);
                editor.putString("audGender", gender[0]);
                editor.commit();
                Toast.makeText(container.getContext(),"Audience Saved",Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }

}
