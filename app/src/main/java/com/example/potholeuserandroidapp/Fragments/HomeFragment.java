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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.potholeuserandroidapp.Adapters.PostsRecyclerViewAdapter;
import com.example.potholeuserandroidapp.Helpers.NetworkHelper;
import com.example.potholeuserandroidapp.Interfaces.PostApi;
import com.example.potholeuserandroidapp.Models.Post;
import com.example.potholeuserandroidapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    int pageno = 0;

    private Context context;

    private RecyclerView postsRecyclerView;

    private List<Post> postList;

    private ProgressBar postsRecyclerViewProgressBar;

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

        postsRecyclerView = view.findViewById(R.id.postsrecyclerviewid);

        postsRecyclerView.setHasFixedSize(true);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        postsRecyclerViewProgressBar = view.findViewById(R.id.postsrecyclerviewprogressbarid);

        postsRecyclerViewProgressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkHelper.getRetrofitInstance(context);

        PostApi postApi = retrofit.create(PostApi.class);

        Call<List<Post>> postsCall = postApi.getPosts(pageno);

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
}
