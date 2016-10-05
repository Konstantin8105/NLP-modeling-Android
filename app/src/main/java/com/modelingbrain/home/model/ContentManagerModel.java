package com.modelingbrain.home.model;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.modelingbrain.home.main.GlobalFunction;
import com.modelingbrain.home.db.DBHelperModel;
import com.modelingbrain.home.main.ModelSort;
import com.modelingbrain.home.template.ElementList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
        boolean again;
        do {
            again = false;
            for (int i = 0; i < models.size(); i++) {
                if (isIgnore(context, models.get(i).getModelID()) || models.get(i) == null) {
                    models.remove(i);
                    again = true;
                }
            }
        } while (again);
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
        for (int i = 0; i < elements.size(); i++) {
            ModelID modelID = elements.get(i);
            int position = 0;
            for (int j = 0; j < ModelID.values().length; j++) {
                if (modelID == ModelID.values()[j]) {
                    position = j;
                    j = ModelID.values().length;
                }
            }
            ElementList elementList = new ElementList(
                    modelID.getResourceIcon(),
                    context.getResources().getStringArray(modelID.getResourceQuestion())[0],
                    context.getResources().getString(modelID.getModelType().getStringResource()),
                    "",
                    ContextCompat.getColor(context, modelID.getModelType().getGeneralColor()),
                    position
            );
            output.add(elementList);
        }

        showLogArray(output);
        return output;
    }

    public static boolean isIgnore(Context context, ModelID modelID) {
        Log.d(TAG, "modelID = " + modelID + " ... is ignore ?");
        boolean ignore = false;

        if (context.getResources().getStringArray(modelID.getResourceQuestion()).length != modelID.getSize() + 1) {
            ignore = true;
        }
        if (context.getResources().getStringArray(modelID.getResourceQuestion()).length == 0) {
            ignore = true;
        }
        if (context.getResources().getStringArray(modelID.getResourceQuestion()).length > 0)
            if (context.getResources().getStringArray(modelID.getResourceQuestion())[0].compareTo("NONE") == 0) {
                ignore = true;
            }
        if (context.getResources().getStringArray(modelID.getResourceQuestion()).length > 1)
            if (context.getResources().getStringArray(modelID.getResourceQuestion())[1].compareTo("NONE") == 0) {
                ignore = true;
            }
        if (context.getResources().getStringArray(modelID.getResourceQuestion()).length < 2) {
            ignore = true;
        }

        if (modelID.isOldModel()) {
            ignore = true;
        }
        Log.d(TAG, "modelID = " + modelID + " ... " + ignore);
        return ignore;
    }

    private static void showLogArray(ArrayList<ElementList> array) {
        for (int i = 0; i < array.size(); i++) {
            Log.d(TAG, TAG + "i = " + i + " array = " + array.get(i).getID());
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

        Log.d(TAG, "Time for sort - " + (GlobalFunction.getTime() - start) + " millisecond");
        return output;
    }

    private static ArrayList<ElementList> SortDate(Context context, ArrayList<Model> result) {
        if (result.size() > 1) {

            Collections.sort(result, new Comparator<Model>() {
                @Override
                public int compare(Model lhs, Model rhs) {
                    if (lhs.getMillisecond_Date() < rhs.getMillisecond_Date())
                        return 1;
                    if (lhs.getMillisecond_Date() > rhs.getMillisecond_Date())
                        return -1;
                    return 0;
//                    if (lhs.getName().compareTo(rhs.getName()) != 0)
//                        return lhs.getName().compareTo(rhs.getName());
//                    if ((rhs.getModelID().getParameter() - lhs.getModelID().getParameter()) == 0)
//                        return (rhs.getModelID().getParameter() - lhs.getModelID().getParameter());
//                    return lhs.getModelType().convert() - rhs.getModelType().convert();
                }
            });
        }
        return convert(context, result);
    }


    //TODO not correct sorting
    private static ArrayList<ElementList> SortAlphabet(Context context, final ArrayList<Model> result) {
        if (result.size() > 1) {

            Collections.sort(result, new Comparator<Model>() {
                @Override
                public int compare(Model lhs, Model rhs) {
                    return lhs.getName().compareTo(rhs.getName());
//                    if (lhs.getName().compareTo(rhs.getName()) != 0)
//                        return lhs.getName().compareTo(rhs.getName());
//                    if ((rhs.getModelID().getParameter() - lhs.getModelID().getParameter()) == 0)
//                        return (rhs.getModelID().getParameter() - lhs.getModelID().getParameter());
//                    return lhs.getModelType().convert() - rhs.getModelType().convert();
                }
            });
        }
        return convert(context, result);
    }

    //TODO not correct sorting
    private static ArrayList<ElementList> SortModelName(Context context, ArrayList<Model> result) {
        if (result.size() > 1) {

            Collections.sort(result, new Comparator<Model>() {
                @Override
                public int compare(Model lhs, Model rhs) {
                    return (lhs.getModelType().convert() - rhs.getModelType().convert());
//                    if ((lhs.getModelType().convert() - rhs.getModelType().convert()) != 0)
//                        return (lhs.getModelType().convert() - rhs.getModelType().convert());
//                    if (lhs.getModelType().convert() - rhs.getModelType().convert() == 0)
//                        return lhs.getModelType().convert() - rhs.getModelType().convert();
//                    if ((rhs.getModelID().getParameter() - lhs.getModelID().getParameter()) != 0)
//                        return (rhs.getModelID().getParameter() - lhs.getModelID().getParameter());
//                    return (lhs.getName().compareTo(rhs.getName()));
                }
            });
        }
        return convert(context, result);
    }

    private static ArrayList<ElementList> Inverse(ArrayList<ElementList> result) {
        Log.d(TAG, "ModelMain::Inverse - start");
        if (result.size() < 2)
            return result;
        ArrayList<ElementList> output = new ArrayList<>();
        for (int i = 0; i < result.size(); i++)
            output.add(result.get(result.size() - 1 - i));
        result.clear();
        Log.d(TAG, "ModelMain::Inverse - end");
        return output;
    }

}
