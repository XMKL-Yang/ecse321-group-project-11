package ca.mcgill.ecse321.cooperator;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EmployersAdapter extends RecyclerView.Adapter<EmployersAdapter.ViewHolder> {

    private static final String TAG = "EmployersAdapter";

    private ArrayList<String> mEmployerNames = new ArrayList<>();
    private ArrayList<String> mEmployerPositions = new ArrayList<>();
    private ArrayList<String> mEmployerCompanies = new ArrayList<>();
    private ArrayList<String> mEmployerEmails = new ArrayList<>();
    private ArrayList<String> mEmployerPhones = new ArrayList<>();
    private ArrayList<String> mUserIDs = new ArrayList<>();
    private Context mContext;

    public EmployersAdapter(Context mContext, ArrayList<String> mEmployerNames, ArrayList<String> mEmployerPositions,
                           ArrayList<String> mEmployerCompanies, ArrayList<String> mEmployerEmails, ArrayList<String> mEmployerPhones, ArrayList<String> mUserIDs) {
        this.mEmployerNames = mEmployerNames;
        this.mEmployerPositions = mEmployerPositions;
        this.mEmployerCompanies = mEmployerCompanies;
        this.mEmployerEmails = mEmployerEmails;
        this.mEmployerPhones = mEmployerPhones;
        this.mUserIDs = mUserIDs;
        this.mContext = mContext;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_employer_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Log.d(TAG, "onBindViewHolder: called.");

        holder.employerName.setText(mEmployerNames.get(position));
        holder.employerPosition.setText(mEmployerPositions.get(position));
        holder.employerCompany.setText(mEmployerCompanies.get(position));
        holder.employerEmail.setText(mEmployerEmails.get(position));
        holder.employerPhone.setText(mEmployerPhones.get(position));

        // Android Toast for full name of Employer
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + mEmployerNames.get(position));

                Toast.makeText(mContext, mEmployerNames.get(position), Toast.LENGTH_SHORT).show();

                /** Called when the user taps the View Students button */
                Intent intent = new Intent(mContext, ViewCoopsActivity.class);
                String userId = mUserIDs.get(position);
                intent.putExtra("userId", userId);
                mContext.startActivity(intent);
            }
        });
    }

    // Return the size of your ArrayLists (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mEmployerNames.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView employerName;
        public TextView employerPosition;
        public TextView employerCompany;
        public TextView employerEmail;
        public TextView employerPhone;
        public RelativeLayout parentLayout;

        public ViewHolder(View v) {
            super(v);
            employerName = v.findViewById(R.id.employer_name);
            employerPosition = v.findViewById(R.id.employer_position);
            employerCompany = v.findViewById(R.id.employer_name);
            employerEmail = v.findViewById(R.id.student_email);
            employerPhone = v.findViewById(R.id.employer_phone);
            parentLayout = v.findViewById(R.id.parent_layout);
        }
    }
}
