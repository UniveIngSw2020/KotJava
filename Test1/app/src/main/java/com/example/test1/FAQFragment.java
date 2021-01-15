package com.example.test1;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// FAQ Fragment

public class FAQFragment extends Fragment {


    private String[] questions, answers;

    public FAQFragment() {

        super(R.layout.fragment_faq);
    }

    public static FAQFragment newInstance(/* String[] qs, String[] as */) {

        return new FAQFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = getResources();
        questions = res.getStringArray(R.array.questions);
        answers = res.getStringArray(R.array.answers);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faq, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager( new LinearLayoutManager( getContext() ) );

        QAAdapter qaAdapter = new QAAdapter(questions, answers);
        recyclerView.setAdapter(qaAdapter);

        return view;
    }
}