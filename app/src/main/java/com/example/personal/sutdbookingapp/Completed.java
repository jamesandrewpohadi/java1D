package com.example.personal.sutdbookingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Upcoming.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Upcoming#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Completed extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USERNAME = "USERNAME";

    // TODO: Rename and change types of parameters
    private String username;
    private ArrayList<BookingInstance> completeds = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public Completed() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param username get username
     * @return A new instance of fragment Upcoming.
     */
    // TODO: Rename and change types and number of parameters
    public static Completed newInstance(String username) {
        Completed fragment = new Completed();
        Bundle args = new Bundle();
        args.putString(USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(USERNAME);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("BookingInstance", "OnCreateView");
        View rootView = inflater.inflate(R.layout.fragment_completed, container, false);
//        for (int i=0; i<20; i++) {
//            BookingInstance bookingInstance = new BookingInstance("120839201","Professor "+Integer.toString(i+1),"12/11/2018","08:30","09:30","Building 1 lvl 5","https://www.biography.com/.image/t_share/MTE5NDg0MDU0OTU2OTAxOTAz/albert-einstein-9285408-1-402.jpg","completed");
//            completeds.add(bookingInstance);
//        }
        Database b = new Database(getContext());
        b.getDataHandlerAll(new Database.DataHandlerAll() {
            @Override
            <T> void postQueryAll(PaginatedList<T> result) {
                for (int i = 0; i < result.size(); i ++) {
                    BookingInstanceTableDO bookingInstance = (BookingInstanceTableDO) result.get(i);
                    if (bookingInstance != null) {
                        if (bookingInstance.getName().equals(username)|| bookingInstance.getStudentName().equals(username)) {
                            LocalDateTime timing = new LocalDateTime(bookingInstance.getTiming());
                            if (timing.isBefore(new LocalDateTime()) && bookingInstance.getStatus().equals("Accepted")) {
                                BookingInstance booking = new BookingInstance(bookingInstance.getBookingID(), bookingInstance.getName(), bookingInstance.getStudentName(), bookingInstance.getTiming(), bookingInstance.getLocation(), "Completed");
                                completeds.add(booking);
                            }
                            else if (timing.isBefore(new LocalDateTime()) && bookingInstance.getStatus().equals("Pending")) {
                                BookingInstance booking = new BookingInstance(bookingInstance.getBookingID(), bookingInstance.getName(), bookingInstance.getStudentName(), bookingInstance.getTiming(), bookingInstance.getLocation(), "Overdue");
                                completeds.add(booking);
                            }
                            else if (bookingInstance.getStatus().equals("Rejected")) {
                                BookingInstance booking = new BookingInstance(bookingInstance.getBookingID(), bookingInstance.getName(), bookingInstance.getStudentName(), bookingInstance.getTiming(), bookingInstance.getLocation(), "Rejected");
                                completeds.add(booking);
                            }
                            else if (bookingInstance.getStatus().equals("Cancelled")) {
                                BookingInstance booking = new BookingInstance(bookingInstance.getBookingID(), bookingInstance.getName(), bookingInstance.getStudentName(), bookingInstance.getTiming(), bookingInstance.getLocation(), "Cancelled");
                                completeds.add(booking);
                            }
                        }
                    }
                }

            }

            @Override
            void showOnUI(Handler handler) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        initRecycler(rootView);
                    }
                });
            }
        }).getAll(BookingInstanceTableDO.class);
        return  rootView;
    }

    public void initRecycler(View rootView) {
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        BookingInstanceAdapter adapter = new BookingInstanceAdapter(this.getContext(), completeds, username);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
