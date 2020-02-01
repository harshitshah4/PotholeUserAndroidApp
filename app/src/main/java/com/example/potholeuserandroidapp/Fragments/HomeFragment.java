package com.example.potholeuserandroidapp.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.potholeuserandroidapp.Adapters.PostsRecyclerViewAdapter;
import com.example.potholeuserandroidapp.Helpers.NetworkHelper;
import com.example.potholeuserandroidapp.Interfaces.PostApi;
import com.example.potholeuserandroidapp.Models.Post;
import com.example.potholeuserandroidapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    int filter = 0;
    int pageno = 0;

    private Context context;

    private Spinner postsFilterSpinner;
    private RecyclerView postsRecyclerView;

    private List<Post> postList;

    private ProgressBar postsRecyclerViewProgressBar;
    final List<String> list = new ArrayList<String>();

    private TextView postsEmptyTextView;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();

        postsFilterSpinner = view.findViewById(R.id.postsfilterspinnerid);
        postsRecyclerView = view.findViewById(R.id.postsrecyclerviewid);

        postsRecyclerView.setHasFixedSize(true);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        postsRecyclerViewProgressBar = view.findViewById(R.id.postsrecyclerviewprogressbarid);


        list.add("All");
        list.add("Processing");
        list.add("Assigned");
        list.add("Resolved");
        list.add("Rejected");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postsFilterSpinner.setAdapter(dataAdapter);

        postsFilterSpinner.setSelection(filter);

        postsFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter = position;
                postsFilterSpinner.setSelection(filter);
                getPosts();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getPosts(){
        postsRecyclerViewProgressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkHelper.getRetrofitInstance(context);

        PostApi postApi = retrofit.create(PostApi.class);

        Call<List<Post>> postsCall = postApi.getPosts(pageno,list.get(filter));

        postsCall.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                postsRecyclerViewProgressBar.setVisibility(View.INVISIBLE);

                if(response.isSuccessful()){

                    if(response.body()!= null){
                        postList = response.body();

                        if(postList.size() > 0 ){

                            postsRecyclerView.setAdapter(new PostsRecyclerViewAdapter(context,postList));
                        }else{
                            Toast.makeText(context, "No posts here", Toast.LENGTH_SHORT).show();
                        }
                    }else{

                    }



                }else{

                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                postsRecyclerViewProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });



    }


    @Override
    public void onResume() {
        super.onResume();

//        getPosts();
    }
}
