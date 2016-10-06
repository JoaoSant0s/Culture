package com.culture.santos.adapter;

import android.util.Log;

import com.culture.santos.culture.MapsActivity;
import com.culture.santos.culture.R;
import com.culture.santos.module.ViewTargets;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

/**
 * Created by Ricar on 06/10/2016.
 */
public class TutorialAdapter {

    private MapsActivity ctx;
    private final String ADD_EVENT_TITLE = "Adicionando um evento.";
    private final String ADD_EVENT_CONTENT_TEXT = "Clique no ponto no mapa aonde você deseja adicionar o Evento.";

    ViewTarget mapViewTarget;
    ShowcaseView auxShowAddEventTutorialShowcaseView;

    public TutorialAdapter(MapsActivity context){
        ctx = context;
        mapViewTarget = new ViewTarget(R.id.map, ctx);
    }

    public void showAddEventTutorial(){
        auxShowAddEventTutorialShowcaseView = new ShowcaseView.Builder(ctx)
                                            .setTarget(mapViewTarget)
                                            .setContentTitle(ADD_EVENT_TITLE)
                                            .setContentText(ADD_EVENT_CONTENT_TEXT)
                                            .hideOnTouchOutside()
                                            .build();

        auxShowAddEventTutorialShowcaseView.hideButton();
    }

    public void hideAddEventTutorial(){
        if(auxShowAddEventTutorialShowcaseView != null) auxShowAddEventTutorialShowcaseView.hide();
    }
}
