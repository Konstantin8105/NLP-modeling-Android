package com.modelingbrain.home.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;

public class ContentManagerModelTest {

    @Test
    public void testSortByModelId() {
        List<Model> list = new ArrayList<>();
        list.add(new Model(ModelID.ID_AIDA));
        list.add(new Model(ModelID.ID_Eye));
        ContentManagerModel.sort(list, ContentManagerModel.modelId);
        assertTrue(list.get(0).getModelID() == ModelID.ID_Eye);
        assertTrue(list.get(1).getModelID() == ModelID.ID_AIDA);
    }

    @Test
    public void testSortByModelName() {
        List<Model> list = new ArrayList<>();
        Model model1 = new Model(ModelID.ID_AIDA);
        model1.setName("a");
        list.add(model1);
        Model model2 = new Model(ModelID.ID_Eye);
        model2.setName("b");
        list.add(model2);
        ContentManagerModel.sort(list, ContentManagerModel.modelName);
        assertTrue(list.get(0).getModelID() == ModelID.ID_AIDA);
        assertTrue(list.get(1).getModelID() == ModelID.ID_Eye);
    }

    @Test
    public void testSortByModelNameAndId1() {
        List<Model> list = new ArrayList<>();

        Model model1 = new Model(ModelID.ID_AIDA);
        model1.setName("c");
        list.add(model1);

        Model model2 = new Model(ModelID.ID_Eye);
        model2.setName("d");
        list.add(model2);

        Model model3 = new Model(ModelID.ID_AIDA);
        model3.setName("a");
        list.add(model3);

        Model model4 = new Model(ModelID.ID_Eye);
        model4.setName("b");
        list.add(model4);

        ContentManagerModel.sort(list, ContentManagerModel.modelId, ContentManagerModel.modelName);
        assertTrue(list.get(0).getName().compareTo("b") == 0);
        assertTrue(list.get(1).getName().compareTo("d") == 0);
        assertTrue(list.get(2).getName().compareTo("a") == 0);
        assertTrue(list.get(3).getName().compareTo("c") == 0);
    }

    @Test
    public void testSortByModelNameAndId2() {
        List<Model> list = new ArrayList<>();

        Model model1 = new Model(ModelID.ID_AIDA);
        model1.setName("c");
        list.add(model1);

        Model model2 = new Model(ModelID.ID_Eye);
        model2.setName("d");
        list.add(model2);

        Model model4 = new Model(ModelID.ID_Eye);
        model4.setName("b");
        list.add(model4);

        ContentManagerModel.sort(list, ContentManagerModel.modelId, ContentManagerModel.modelName);
        assertTrue(list.get(0).getName().compareTo("b") == 0);
        assertTrue(list.get(1).getName().compareTo("d") == 0);
        assertTrue(list.get(2).getName().compareTo("c") == 0);
    }

    @Test
    public void testSortByModelNameAndId3() {
        List<Model> list = new ArrayList<>();

        Model model1 = new Model(ModelID.ID_AIDA);
        model1.setName("c");
        list.add(model1);

        Model model3 = new Model(ModelID.ID_AIDA);
        model3.setName("a");
        list.add(model3);

        Model model4 = new Model(ModelID.ID_Eye);
        model4.setName("b");
        list.add(model4);

        ContentManagerModel.sort(list, ContentManagerModel.modelId, ContentManagerModel.modelName);
        assertTrue(list.get(0).getName().compareTo("b") == 0);
        assertTrue(list.get(1).getName().compareTo("a") == 0);
        assertTrue(list.get(2).getName().compareTo("c") == 0);
    }

    @Test
    public void testSortByModelName2() {
        List<Model> list = new ArrayList<>();

        Model model1 = new Model(ModelID.ID_AIDA);
        model1.setName("c");
        list.add(model1);

        ContentManagerModel.sort(list, ContentManagerModel.modelId, ContentManagerModel.modelName);
        assertTrue(list.get(0).getName().compareTo("c") == 0);
    }
}