package br.com.nglauber.aula04_filmes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.nglauber.aula04_filmes.database.MovieContract;
import br.com.nglauber.aula04_filmes.database.MovieDBHelper;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class MovieUITests {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Before
    public void setUp(){
        SQLiteDatabase db = mActivityRule.getActivity().openOrCreateDatabase(
                MovieDBHelper.DB_NAME, Context.MODE_PRIVATE, null);
        db.execSQL("DELETE FROM "+ MovieContract.TABLE_NAME);
        db.close();
    }

    @Test
    public void testMovies() {
        final String SEARCH_TEXT = "The Mask";
        final String MOVIE_TO_CLICK = "Beyond the Mask";

        // Selecionando aba de busca
        onView(withId(R.id.pager)).perform(swipeLeft());
        waitFor(1000);
        // Cliando no botão de busca
        onView(withId(R.id.search)).perform(click());
        // Digitando o nome do filme a ser buscado
        onView(withId(R.id.search_src_text)).perform(typeText(SEARCH_TEXT));
        // Pressionando o botão de busca do teclado virtual
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton());
        // Fechando o teclado virtual
        closeSoftKeyboard();
        waitFor(3000);
        // Fazendo o scroll e clicando no filme no resultado da busca
        onView(withId(R.id.main_recycler_movies))
                .perform(scrollTo(hasDescendant(withText(MOVIE_TO_CLICK))));
        onView(withId(R.id.main_recycler_movies))
                .perform(actionOnItem(hasDescendant(withText(MOVIE_TO_CLICK)), click()));
        waitFor(2000);
        // Pressionando o botão de favorito
        onView(withId(R.id.fab)).perform(click());
        waitFor(1000);
        if (mActivityRule.getActivity().getResources().getBoolean(R.bool.phone)) {
            // Volta para tela anterior
            pressBack();
        }
        // Seleciona a aba de favoritos
        onView(withId(R.id.pager)).perform(swipeRight());
        waitFor(2000);
        // Verifica se a lista contém o item adicionado aos favoritos
        onView(withId(R.id.favorites_list))
                .check(matches(hasDescendant(withText(MOVIE_TO_CLICK))));
        onData(anything()).inAdapterView(withId(R.id.favorites_list)).atPosition(0).perform(click());
        waitFor(2000);
        // Pressionando o botão de favorito
        onView(withId(R.id.fab)).perform(click());
        waitFor(1000);
        if (mActivityRule.getActivity().getResources().getBoolean(R.bool.phone)) {
            // Volta para tela anterior
            pressBack();
        }
        onView(withId(R.id.favorites_list)).check(matches(not(hasDescendant(withId(R.id.movie_item_text_title)))));
    }

    private void waitFor(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}