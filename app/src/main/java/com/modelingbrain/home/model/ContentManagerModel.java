package com.modelingbrain.home.model;

import android.content.Context;
import android.util.Log;

import com.modelingbrain.home.GlobalFunction;
import com.modelingbrain.home.db.DBHelperModel;
import com.modelingbrain.home.main.ModelSort;
import com.modelingbrain.home.template.ElementList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ContentManagerModel {
    @SuppressWarnings("unused")
    static private final String TAG = " ContentManager ";

    private static Context context;
    private static DBHelperModel dbHelperModel;
    private static ContentManagerModel contentManager;

    // TODO: 7/30/16  remove that constructor
    public ContentManagerModel(Context context) {
        if (contentManager == null) {
            ContentManagerModel.context = context;
            dbHelperModel = new DBHelperModel(context);
            contentManager = this;
        }
    }

    public static ArrayList<ElementList> getListNormalModel(ModelSort modelSort) {
        return getListModel(modelSort, ModelState.NORMAL);
    }

    public static ArrayList<ElementList> getListArchiveModel(ModelSort modelSort) {
        return getListModel(modelSort, ModelState.ARCHIVE);
    }

    private static ArrayList<ElementList> getListModel(ModelSort modelSort, ModelState state) {
        if(dbHelperModel  == null){
            throw new NullPointerException("dbHelperModel cannot be null");
        }
        ArrayList<Model> models = dbHelperModel.openHeader(state);
        boolean again;
        do {
            again = false;
            for (int i = 0; i < models.size(); i++) {
                if (isIgnore(models.get(i).getModelID())) {
                    models.remove(i);
                    again = true;
                }
            }
        } while (again);
        return sort(models, modelSort);
    }

    private static ArrayList<ElementList> convert(ArrayList<Model> result) {
        ArrayList<ElementList> elements = new ArrayList<>();
        for (Model model : result) {
            elements.add(convert(model));
        }
        return elements;
    }

    private static ElementList convert(Model model) {
        return new ElementList(
                model.getModelID().getResourceIcon(),
                model.getName(),
                String.format("%s. %s",context.getResources().getString(model.getModelType().getStringResource()),
                        context.getResources().getStringArray(model.getModelID().getResourceQuestion())[0]),
                GlobalFunction.ConvertMillisecondToDate(model.getMillisecond_Date()),
                context.getResources().getColor(model.getModelType().getGeneralColor()),
                model.getDbId()
        );
    }

    public static ArrayList<ElementList> getListChooseModel() {
        ArrayList<ModelID> elements = new ArrayList<>();

        for (int i = 0; i < ModelID.values().length; i++) {
            if (!isIgnore(ModelID.values()[i])) {
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
                    context.getResources().getColor(modelID.getModelType().getGeneralColor()),
                    position
            );
            output.add(elementList);
        }

        showLogArray(output);
        return output;
    }

    public static boolean isIgnore(ModelID modelID) {
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
        return ignore;
    }

    static void showLogArray(ArrayList<ElementList> array) {
        for (int i = 0; i < array.size(); i++) {
            Log.d(TAG, TAG + "i = " + i + " array = " + array.get(i).getID());
        }
    }


    private static ArrayList<ElementList> sort(ArrayList<Model> result, ModelSort modelSort) {
        long start = GlobalFunction.getTime();

        ArrayList<ElementList> output;
        switch (modelSort) {
            case SortName:
            case SortNameInverse:
                output = SortName(result);
                break;
            case SortAlphabet:
            case SortAlphabetInverse:
                output = SortAlphabet(result);
                break;
            case SortDate:
            case SortDateInverse:
                output = SortDate(result);
                break;
            default:
                output = SortName(result);
        }
        if (modelSort == ModelSort.SortNameInverse ||
                modelSort == ModelSort.SortAlphabetInverse ||
                modelSort == ModelSort.SortDateInverse) {
            output = Inverse(output);
        }

        Log.d(TAG, "Time for sort - " + (GlobalFunction.getTime() - start) + " millisecond");
        return output;
    }

    private static ArrayList<ElementList> SortDate(ArrayList<Model> result) {
        if (result.size() > 1) {

            Collections.sort(result, new Comparator<Model>() {
                @Override
                public int compare(Model lhs, Model rhs) {
                    return (int) (lhs.getMillisecond_Date() - rhs.getMillisecond_Date());
                }
            });
        }
        return convert(result);
    }


    private static ArrayList<ElementList> SortAlphabet(ArrayList<Model> result) {
        if (result.size() > 1) {

            Collections.sort(result, new Comparator<Model>() {
                @Override
                public int compare(Model lhs, Model rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });
            Collections.sort(result, new Comparator<Model>() {
                @Override
                public int compare(Model lhs, Model rhs) {
                    if (lhs.getName().compareTo(rhs.getName()) == 0)
                        return 0;
                    return lhs.getModelType().convert() - rhs.getModelType().convert();
                }
            });
        }
        return convert(result);
    }

    private static ArrayList<ElementList> SortName(ArrayList<Model> result) {
        if (result.size() > 1) {

            Collections.sort(result, new Comparator<Model>() {
                @Override
                public int compare(Model lhs, Model rhs) {
                    return (lhs.getModelType().convert() - rhs.getModelType().convert());
                }
            });
            Collections.sort(result, new Comparator<Model>() {
                @Override
                public int compare(Model lhs, Model rhs) {
                    if (lhs.getModelType() != rhs.getModelType())
                        return 0;
                    return (lhs.getModelID().getParameter() - rhs.getModelID().getParameter());
                }
            });
            Collections.sort(result, new Comparator<Model>() {
                @Override
                public int compare(Model lhs, Model rhs) {
                    if (lhs.getModelType() != rhs.getModelType())
                        return 0;
                    if (lhs.getModelID().getParameter() != rhs.getModelID().getParameter())
                        return 0;
                    return (lhs.getName().compareTo(rhs.getName()));
                }
            });
        }
        return convert(result);
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
