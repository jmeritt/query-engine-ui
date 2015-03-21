package com.datadirect.ui;

import com.datadirect.platform.DataSource;
import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by jmeritt on 3/19/15.
 */
public class SelectSourcesView extends CustomComponent implements View {
    public static final String NAME = "sources";
    TwinColSelect sel = new TwinColSelect("Select Sources");
    Button logout = new Button("Logout", new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {

            try {
                Engine.get().stop();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // "Logout" the user
            getSession().setAttribute("user", null);
            getSession().setAttribute("password", null);

            // Refresh this view, should redirect to login view
            getUI().getNavigator().navigateTo(LoginView.NAME);
        }
    });
    Button virtualize = new Button("Virtualize", new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {

            try {
                Engine.get().virtualize(selectedDatasource);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            getUI().getNavigator().navigateTo(SQLView.NAME);
        }
    });
    private List<DataSource> selectedDatasource;
    private Map<String, DataSource> allDatasources;

    public SelectSourcesView() {
        setSizeFull();
        allDatasources = new HashMap<>();
        selectedDatasource = new ArrayList<>();


        VerticalLayout fields = new VerticalLayout(sel, new HorizontalLayout(virtualize, logout));
        fields.setCaption("Please select the sources you want to access");
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();

        // The view root layout
        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        setCompositionRoot(viewLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        try {
            for (DataSource ds : Engine.get().allDataSources()) {
                sel.addItem(ds.getName());
                allDatasources.put(ds.getName(), ds);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sel.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                selectedDatasource.clear();
                Set selected = (Set)event.getProperty().getValue();
                if (selected != null)
                {
                    for(Object val : selected)
                    {
                        selectedDatasource.add(allDatasources.get(val));
                    }
                }

            }
        });
    }
}
