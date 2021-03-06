package ca.mcgill.ecse321.cooperator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ViewStudentsActivity extends AppCompatActivity {

    private static final String TAG = "ViewStudentsActivity";
    private String error = null;

    // variables
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mIDs = new ArrayList<>();
    private ArrayList<String> mMajors = new ArrayList<>();
    private ArrayList<String> mEmails = new ArrayList<>();
    private ArrayList<String> mPhones = new ArrayList<>();
    private ArrayList<String> mUserIDs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);
        Log.d(TAG, "onCreate: started.");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initStudentNames();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initStudentNames() {
        Log.d(TAG, "initStudentNames: preparing student names.");

        // Restfull call: all students
        HttpUtils.get("students/", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                // Clear lists
                mNames.clear();
                mIDs.clear();
                mMajors.clear();
                mEmails.clear();
                mPhones.clear();
                mUserIDs.clear();
                for( int i = 0; i < response.length(); i++){
                    try {
                        Log.d(TAG, "Restful GET call succesfull (" + i + ").");

                        // Add Student Names
                        mNames.add(response.getJSONObject(i).getString("firstName") + " "
                                + response.getJSONObject(i).getString("lastName"));

                        // Add Student IDs
                        mIDs.add(response.getJSONObject(i).getString("id"));

                        // Add Student Majors & Years
                        mMajors.add(response.getJSONObject(i).getString("major") + ", "
                                + response.getJSONObject(i).getString("academicYear"));

                        // Add Student email
                        mEmails.add(response.getJSONObject(i).getString("email"));

                        // Add Student phone number
                        mPhones.add(
                                // Convert phone number format
                                String.valueOf(response.getJSONObject(i).getString("phone")
                                ).replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1)-$2-$3")
                        );

                        // Add User IDs
                        mUserIDs.add(response.getJSONObject(i).getString("userId"));

                        initRecyclerView();
                    } catch (JSONException e) {
                        Log.d(TAG, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Log.d(TAG, "Restful GET call failure");
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
            }
        });

    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.students_recycler_view);
        StudentsAdapter adapter = new StudentsAdapter(this, mNames, mIDs, mMajors, mEmails, mPhones, mUserIDs);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
