package com.example.test1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QAAdapter extends RecyclerView.Adapter<QAAdapter.ViewHolder> {
    private final String[] questions, answers; //Contengono rispettivamente le stringhe delle domande e delle risposte


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_question, tv_answer;

        //Assegna ai campi il valore dell'id delle rispettive TextView
        public ViewHolder( View view ) {
            super(view);
            tv_question = view.findViewById(R.id.tv_question);
            tv_answer = view.findViewById(R.id.tv_answer);
        }

        //Ritorna la TextView delle domande
        public TextView getQuestionTV() {
            return tv_question;
        }

        //Ritorna la TextView delle risposte
        public TextView getAnswerTV() {
            return tv_answer;
        }
    }

    /**
     * Inizializza i dati necessari all'Adapterù
     * @param dataSetQ per le stringhe rappresentanti le domande
     * @param dataSetA per le risposte
     */
    public QAAdapter(String[] dataSetQ, String[] dataSetA) {
        questions = dataSetQ;
        answers = dataSetA;
    }

    
    //Crea le nuove views (usato dal layout managet)
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