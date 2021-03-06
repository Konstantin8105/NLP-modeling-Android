package com.modelingbrain.home.model;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.modelingbrain.home.db.DBHelperModel;
import com.modelingbrain.home.main.GlobalFunction;
import com.modelingbrain.home.main.ModelSort;
import com.modelingbrain.home.template.ElementList;

import java.util.*;

/**
 * License: LGPL ver.3
 *
 * @author Izyumov Konstantin
 */

public class ContentManagerModel {
    static private final String TAG = " ContentManager ";

    public static ArrayList<ElementList> getListNormalModel(Context context, ModelSort modelSort) {
        return getListModel(context, modelSort, ModelState.NORMAL);
    }

    public static ArrayList<ElementList> getListArchiveModel(Context context, ModelSort modelSort) {
        return getListModel(context, modelSort, ModelState.ARCHIVE);
    }

    private static ArrayList<ElementList> getListModel(Context context, ModelSort modelSort, ModelState state) {
        DBHelperModel dbHelperModel = new DBHelperModel(context);
        //noinspection ConstantConditions
        if (dbHelperModel == null) {
            throw new NullPointerException("dbHelperModel cannot be null");
        }
        ArrayList<Model> models = dbHelperModel.openHeader(state);
        dbHelperModel.close();
        Iterator<Model> iterator = models.iterator();
        while (iterator.hasNext()){
            Model model = iterator.next();
            if(isIgnore(context, model.getModelID())){
                iterator.remove();
            }
        }
        return sort(context, models, modelSort);
    }

    private static ArrayList<ElementList> convert(Context context, ArrayList<Model> result) {
        ArrayList<ElementList> elements = new ArrayList<>();
        for (Model model : result) {
            elements.add(convert(context, model));
        }
        return elements;
    }

    private static ElementList convert(Context context, Model model) {
        return new ElementList(
                model.getModelID().getResourceIcon(),
                model.getName(),
                String.format("%s. %s", context.getResources().getString(model.getModelType().getStringResource()),
                        context.getResources().getStringArray(model.getModelID().getResourceQuestion())[0]),
                GlobalFunction.convertMillisecondToDate(model.getMillisecond_Date()),
                ContextCompat.getColor(context, model.getModelType().getGeneralColor()),
                model.getDbId()
        );
    }

    public static ArrayList<ElementList> getListChooseModel(Context context) {
        ArrayList<ModelID> elements = new ArrayList<>();

        for (int i = 0; i < ModelID.values().length; i++) {
            if (!isIgnore(context, ModelID.values()[i])) {
                elements.add(ModelID.values()[i]);
            }
        }


        if (elements.size() > 1) {
            Iterator<ModelID> iterator = elements.iterator();
            while (iterator.hasNext()) {
                ModelID present = iterator.next();
                try {
                    if (present == null)
                        iterator.remove();
                    assert present != null;
                    if (context.getResources().getStringArray(present.getResourceQuestion()) == null)
                        iterator.remove();
                } catch (Exception e) {
                    iterator.remove();
                }
            }
            boolean again;
            do {
                again = false;
                for (int i = 0; i < elements.size(); i++) {
                    for (int j = i + 1; j < elements.size(); j++) {
                        if (elements.get(i).getModelType().convert() > elements.get(j).getModelType().convert()) {
                            ModelID temp = elements.get(i);
                            elements.set(i, elements.get(j));
                            elements.set(j, temp);
                            again = true;
                        }
                    }
                }
            } while (again);
            do {
                again = false;
                for (int i = 0; i < elements.size(); i++) {
                    for (int j = i + 1; j < elements.size(); j++) {
                        String nameI = context.getResources().getStringArray(elements.get(i).getResourceQuestion())[0];
                        String nameJ = context.getResources().getStringArray(elements.get(j).getResourceQuestion())[0];
                        if (elements.get(i).getModelType().convert() == elements.get(j).getModelType().convert() &&
                                nameI.compareTo(nameJ) > 0) {
                            ModelID temp = elements.get(i);
                            elements.set(i, elements.get(j));
                            elements.set(j, temp);
                            again = true;
                        }
                    }
                }
            } while (again);
        }


        ArrayList<ElementList> output = new ArrayList<>();
        for (ModelID element : elements) {
            int position = 0;
            for (int j = 0; j < ModelID.values().length; j++) {
                if (element == ModelID.values()[j]) {
                    position = j;
                    j = ModelID.values().length;
                }
            }
            ElementList elementList = new ElementList(
                    element.getResourceIcon(),
                    context.getResources().getStringArray(element.getResourceQuestion())[0],
                    context.getResources().getString(element.getModelType().getStringResource()),
                    "",
                    ContextCompat.getColor(context, element.getModelType().getGeneralColor()),
                    position
            );
            output.add(elementList);
        }

        showLogArray(output);
        return output;
    }

    public static boolean isIgnore(Context context, ModelID modelID) {
        boolean ignore = false;

        String[] resourse = context.getResources().getStringArray(modelID.getResourceQuestion());
        if (resourse.length != modelID.getSize() + 1) {
            ignore = true;
        }
        if (resourse.length == 0) {
            ignore = true;
        }
        for (String res : resourse) {
            if (res.length() == 0) {
                ignore = true;
                break;
            }
        }
        if (resourse.length > 0)
            if (resourse[0].compareTo("NONE") == 0) {
                ignore = true;
            }
        if (resourse.length > 1)
            if (resourse[1].compareTo("NONE") == 0) {
                ignore = true;
            }
        if (resourse.length < 2) {
            ignore = true;
        }

        if (modelID.isOldModel()) {
            ignore = true;
        }
        Log.i(TAG, "modelID = " + modelID + " ... " + ignore);
        return ignore;
    }

    private static void showLogArray(ArrayList<ElementList> array) {
        for (int i = 0; i < array.size(); i++) {
            Log.i(TAG, TAG + "i = " + i + " array = " + array.get(i).getID());
        }
    }


    private static ArrayList<ElementList> sort(Context context, ArrayList<Model> result, ModelSort modelSort) {
        long start = GlobalFunction.getTime();

        ArrayList<ElementList> output;
        switch (modelSort) {
            case SortName:
            case SortNameInverse:
                output = SortModelName(context, result);
                break;
            case SortAlphabet:
            case SortAlphabetInverse:
                output = SortAlphabet(context, result);
                break;
            case SortDate:
            case SortDateInverse:
                output = SortDate(context, result);
                break;
            default:
                output = SortModelName(context, result);
        }
        if (modelSort == ModelSort.SortNameInverse ||
                modelSort == ModelSort.SortAlphabetInverse ||
                modelSort == ModelSort.SortDateInverse) {
            output = Inverse(output);
        }

        Log.i(TAG, "Time for sort - " + (GlobalFunction.getTime() - start) + " millisecond");
        return output;
    }

    private static final Comparator<Model> modelType = new Comparator<Model>() {
        @Override
        public int compare(Model o1, Model o2) {
            return (o1.getModelType().convert() - o2.getModelType().convert());
        }
    };
    static final Comparator<Model> modelId = new Comparator<Model>() {
        @Override
        public int compare(Model o1, Model o2) {
            return (o1.getModelID().getParameter() - o2.getModelID().getParameter());
        }
    };
    static final Comparator<Model> modelName = new Comparator<Model>() {
        @Override
        public int compare(Model o1, Model o2) {
            return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
        }
    };
    private static final Comparator<Model> modelDate = new Comparator<Model>() {
        @Override
        public int compare(Model o1, Model o2) {
            if (o1.getMillisecond_Date() < o2.getMillisecond_Date())
                return 1;
            if (o1.getMillisecond_Date() > o2.getMillisecond_Date())
                return -1;
            return 0;
        }
    };

    @SuppressWarnings("unchecked")
    private static ArrayList<ElementList> SortDate(Context context, ArrayList<Model> result) {
        sort(result, modelDate);
        return convert(context, result);
    }


    @SuppressWarnings("unchecked")
    private static ArrayList<ElementList> SortAlphabet(Context context, final ArrayList<Model> result) {
        sort(result, modelName, modelType, modelId);
        return convert(context, result);
    }


    @SuppressWarnings("unchecked")
    private static ArrayList<ElementList> SortModelName(Context context, ArrayList<Model> result) {
        sort(result, modelType, modelId, modelName);
        return convert(context, result);
    }

    @SuppressWarnings("unchecked")
    static void sort(List<Model> data, Comparator<Model>... comparators) {
        Log.i(TAG, "ModelMain::sort - start");
        for (Model aData : data) {
            Log.i(TAG, "Input -- " + aData.getName());
        }
        for (int i = 0; i < comparators.length; i++) {
            Log.i(TAG, "Comparator[" + i + "] -- " + comparators[i]);
        }
        if (data.size() < 2) {
            return;
        }
        Collections.sort(data, comparators[0]);
        if (comparators.length > 1) {
            //recurse
            int leftPosition = 0;
            for (int i = 1; i < data.size(); i++) {
                if (comparators[0].compare(data.get(leftPosition), data.get(i)) != 0) {
                    if (i - leftPosition >= 2) {
                        Comparator<Model>[] otherWithoutZero = new Comparator[comparators.length - 1];
                        System.arraycopy(comparators, 1, otherWithoutZero, 0, comparators.length - 1);
                        List<Model> part = data.subList(leftPosition, i);
                        sort(part, otherWithoutZero);
                    }
                    leftPosition = i;
                }
                if (i == data.size() - 1 && data.size() - leftPosition >= 2) {
                    Comparator<Model>[] otherWithoutZero = new Comparator[comparators.length - 1];
                    System.arraycopy(comparators, 1, otherWithoutZero, 0, comparators.length - 1);
                    List<Model> part = data.subList(leftPosition, i + 1);
                    sort(part, otherWithoutZero);
                }
            }
        }
        for (Model element : data) {
            Log.i(TAG, "Output -- " + element.getName());
        }
        Log.i(TAG, "ModelMain::sort - finish");
    }

    private static ArrayList<ElementList> Inverse(ArrayList<ElementList> result) {
        Log.i(TAG, "ModelMain::Inverse - start");
        if (result.size() < 2)
            return result;
        ArrayList<ElementList> output = new ArrayList<>();
        for (int i = 0; i < result.size(); i++)
            output.add(result.get(result.size() - 1 - i));
        result.clear();
        Log.i(TAG, "ModelMain::Inverse - end");
        return output;
    }

}
