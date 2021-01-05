package com.example.test1;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QAAdapter extends RecyclerView.Adapter<QAAdapter.ViewHolder> {
    private String[] questions, answers; //Contentgono rispettivamente le stringhe delle domande e delle risposte
    //Si poteva, alternativamente, creare un solo array con alternate domande e risposte

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_question, tv_answer;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        public ViewHolder( View view ) {
            super(view);
            // Define click listener for the ViewHolder's View
            tv_question = (TextView) view.findViewById(R.id.tv_question);
            tv_answer = (TextView) view.findViewById(R.id.tv_answer);
        }

        public TextView getQuestionTV() {
            return tv_question;
        }

        public TextView getAnswerTV() {
            return tv_answer;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     * @param dataSetQ per le stringhe rappresentanti le domande
     * @param dataSetA per le risposte
     * String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public QAAdapter(String[] dataSetQ, String[] dataSetA) {
        questions = dataSetQ;
        answers = dataSetA;
    }


    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position ) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getQuestionTV().setText(questions[position]);
        viewHolder.getAnswerTV().setText(answers[position]);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        //Si suppone che ad ogni domanda corrisponda una risposta e che quindi siano in ugual numero
        return questions.length;
    }
}