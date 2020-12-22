package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class FavouritesHistoryActivity extends AppCompatActivity {
//Questa activity dovrebbe fare da base per i fragment di history e favourites
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int val = getIntent().getIntExtra("val", 0 );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_history);

        if(savedInstanceState == null){
            switch(val){
                case 1:
                    //Nel caso l'activity venga aperta con "val = 1", cioè premendo sul bottone dei
                      //preferiti, viene inizializzato il fragment dei preferiti
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setReorderingAllowed(true)//Fatto copiando dagli esempi ufficiali -> da errore, seems legit
                            .add(R.id.fav_hist_frag, FavouritesFragment.newInstance(), null)
                            .commit();
                    break;
                case 2:
                    //Nel caso l'activity venga aperta con "val = 2", cioè premendo sul bottone dei
                        //visitati, viene inizializzato il fragment dei visitati
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.fav_hist_frag, HistoryFragment.newInstance(), null)
                            .commit();
                    break;
/* Ho ancora dei dubbi su stats e location quindi al momento li ometto, c'è due volte history perchè
    sarebbe il quarto bottone, ma al momento non ci sono due frammenti quindi è mmesso secondo
                case 2:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.fav_hist_frag, StatsFragment.newInstance(), null)
                            .commit();
                    break;
                case 3:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.fav_hist_frag, LocationFragment.newInstance(), null)
                            .commit();
                    break;
                case 4:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.fav_hist_frag, HistoryFragment.newInstance(), null)
                            .commit();
                    break;
*/
            }
        }
    }
}