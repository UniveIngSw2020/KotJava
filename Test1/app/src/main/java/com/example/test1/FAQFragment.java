package com.example.test1;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FAQFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FAQFragment extends Fragment {
    /*
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "qs";
    private static final String ARG_PARAM2 = "as";
     */

    private String[] questions, answers;

    public FAQFragment() {
        // Required empty public constructor
        super(R.layout.fragment_faq);
    }

/*
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param qs Parameter 1. Contiene le domande
     * @param as Parameter 2. Contiene le risposte
     * @return A new instance of fragment CreditsFragment.*/
    public static FAQFragment newInstance(/* String[] qs, String[] as */) {
        return new FAQFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            questions = getArguments().getStringArray(ARG_PARAM1);
            answers = getArguments().getStringArray(ARG_PARAM2);
        }*/
        Resources res = getResources();
        questions = res.getStringArray(R.array.questions);
        answers = res.getStringArray(R.array.answers);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faq, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager( new LinearLayoutManager( getContext() ) );

        QuestionsAdapter questionsAdapter = new QuestionsAdapter(questions, answers);
        recyclerView.setAdapter(questionsAdapter);

        return view;
    }
}